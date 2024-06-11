package com.lts.event;

import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

public class SimpleKeyListenerHelperTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    private Component mockComponent;

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
        mockComponent = new JPanel();
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    public void testDetach() {
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(mockComponent);

        helper.detach();

        assertEquals(null, helper.component);
    }

    @Test
    public void testKeyEventForEnter() {
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(mockComponent);
        KeyEvent mockKeyEvent = new KeyEvent(mockComponent, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, '\n');
        
        helper.keyPressed(mockKeyEvent);

        // Verify that the listener's enter method is called
        assertEquals("Enter key pressed", outContent.toString().trim());
    }

    @Test
    public void testKeyEventForInsert() {
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(mockComponent);
        KeyEvent mockKeyEvent = new KeyEvent(mockComponent, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_UNDEFINED, 'i');

        helper.keyPressed(mockKeyEvent);

        // Verify that the listener's insert method is called
        assertEquals("Insert key pressed", outContent.toString().trim());
    }

    @Test
    public void testKeyEventForDelete() {
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(mockComponent);
        KeyEvent mockKeyEvent = new KeyEvent(mockComponent, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_DELETE, '\u007F');

        helper.keyPressed(mockKeyEvent);

        // Verify that the listener's delete method is called
        assertEquals("Delete key pressed", outContent.toString().trim());
    }

    @Test
    public void testKeyEventForTab() {
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(mockComponent);
        KeyEvent mockKeyEvent = new KeyEvent(mockComponent, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_TAB, '\t');

        helper.keyPressed(mockKeyEvent);

        // Verify that the listener's tab method is called
        assertEquals("Tab key pressed", outContent.toString().trim());
    }

    @Test
    public void testKeyEventForInvalidKey() {
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(mockComponent);
        KeyEvent mockKeyEvent = new KeyEvent(mockComponent, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_UNDEFINED, ' ');

        helper.keyPressed(mockKeyEvent);

        // Verify that no method is called
        assertEquals("", outContent.toString().trim());
    }

    @Test
    public void testNotifyListenerForEnter() {
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(mockComponent);
        SimpleKeyListener mockListener = new SimpleKeyListener() {
            @Override
            public void enter(Component component) {
                System.out.println("Enter method called");
            }

            // Implement other listener methods for test purposes
        };

        helper.notifyListener(mockListener, 10, null);

        // Verify that the listener's enter method is called
        assertEquals("Enter method called", outContent.toString().trim());
    }

    // Implement other test methods for other listener methods

}