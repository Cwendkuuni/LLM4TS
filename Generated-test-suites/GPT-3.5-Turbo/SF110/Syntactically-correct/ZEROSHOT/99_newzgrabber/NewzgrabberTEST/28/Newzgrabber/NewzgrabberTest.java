package Newzgrabber;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.util.Vector;

import static org.junit.Assert.*;

public class NewzgrabberTest {
    private Newzgrabber newzgrabber;

    @Before
    public void setUp() {
        newzgrabber = new Newzgrabber();
    }

    @After
    public void tearDown() {
        newzgrabber.dispose();
    }

    @Test
    public void testSetupBasicSystem() {
        Newzgrabber.setupBasicSystem();

        assertEquals(newzgrabber.Newzdirectory, Newzgrabber.findDirectory(Newzgrabber.JarFilename));
        assertTrue(System.getProperty("java.library.path").contains(Newzgrabber.Newzdirectory));
        assertTrue(System.out.println().contains(Newzgrabber.Newzdirectory));
    }

    @Test
    public void testWindowClosed() {
        WindowEvent windowEvent = new WindowEvent(newzgrabber, WindowEvent.WINDOW_CLOSED);
        newzgrabber.windowClosed(windowEvent);

        // No assertions to be made, since the method does not have any functionality
    }

    // More test methods for other methods in the Newzgrabber class...

}