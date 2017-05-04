package menu;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class GameMenu {

    private long windowHandle;

    private void run() {
        init();
        loop();
        cleanUp();
    }

    private void cleanUp() {
        glfwFreeCallbacks(windowHandle);
        glfwDestroyWindow(windowHandle);

        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init() {
        GLFWErrorCallback.createPrint(System.err).set();
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        Integer windowWidth = 800;
        Integer windowHeight = 600;
        String windowTitle = "Game menu";

        windowHandle = glfwCreateWindow(windowWidth, windowHeight, windowTitle, NULL, NULL);
        if (windowHandle == NULL) {
            throw new RuntimeException("Failed to create the GLFW windowHandle");
        }

        glfwSetKeyCallback(windowHandle, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
            }
        });

        // noinspection Duplicates
        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            glfwGetWindowSize(windowHandle, pWidth, pHeight);

            GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            glfwSetWindowPos(
                windowHandle,
                (vidMode.width() - pWidth.get(0)) / 2,
                (vidMode.height() - pHeight.get(0)) / 2
            );
        }

        glfwMakeContextCurrent(windowHandle);
        glfwSwapInterval(1);

        glfwShowWindow(windowHandle);
    }

    private void loop() {
        GL.createCapabilities();

        glClearColor(0.0f, 0.0f, 0.5f, 0.0f);

        while (!glfwWindowShouldClose(windowHandle)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            glfwSwapBuffers(windowHandle);

            glfwPollEvents();
        }
    }

    public static void main(String[] args) {
        new GameMenu().run();
    }
}
