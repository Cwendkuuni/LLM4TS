
package com.lts.event;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.event.*;

public class SimpleKeyListenerHelperTest {

    private SimpleKeyListenerHelper helper;

    @Before
    public void setUp() {
        Component component = new Component();
        helper = new SimpleKeyListenerHelper(component);
    }

    @Test
    public void testDetach() {
        helper.detach();
        assertNull(helper.component);
    }

    @Test
    public void testNotifyListener() {
        // Test case for type 10
        SimpleKeyListener listener1 = new SimpleKeyListener();
        helper.notifyListener(listener1, 10, null);
        // Assert that listener's enter method was called
        assertTrue(listener1.enterCalled);

        // Test case for type 155
        SimpleKeyListener listener2 = new SimpleKeyListener();
        helper.notifyListener(listener2, 155, null);
        // Assert that listener's insert method was called
        assertTrue(listener2.insertCalled);

        // Test case for type 127
        SimpleKeyListener listener3 = new SimpleKeyListener();
        helper.notifyListener(listener3, 127, null);
        // Assert that listener's delete method was called
        assertTrue(listener3.deleteCalled);

        // Test case for type 9
        SimpleKeyListener listener4 = new SimpleKeyListener();
        helper.notifyListener(listener4, 9, null);
        // Assert that listener's tab method was called
        assertTrue(listener4.tabCalled);
    }

    @Test
    public void testKeyPressed() {
        // Test case for key code 9
        KeyEvent event1 = new KeyEvent(new Component(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_TAB, KeyEvent.CHAR_UNDEFINED);
        helper.keyPressed(event1);
        // Assert that the fire method was called with key code 9
        assertTrue(helper.fireCalled && helper.keyEventKeyCode == KeyEvent.VK_TAB);

        // Test case for key code 10
        KeyEvent event2 = new KeyEvent(new Component(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, KeyEvent.CHAR_UNDEFINED);
        helper.keyPressed(event2);
        // Assert that the fire method was called with key code 10
        assertTrue(helper.fireCalled && helper.keyEventKeyCode == KeyEvent.VK_ENTER);

        // Test case for key code 127
        KeyEvent event3 = new KeyEvent(new Component(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_DELETE, KeyEvent.CHAR_UNDEFINED);
        helper.keyPressed(event3);
        // Assert that the fire method was called with key code 127
        assertTrue(helper.fireCalled && helper.keyEventKeyCode == KeyEvent.VK_DELETE);

        // Test case for key code 155
        KeyEvent event4 = new KeyEvent(new Component(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_INSERT, KeyEvent.CHAR_UNDEFINED);
        helper.keyPressed(event4);
        // Assert that the fire method was called with key code 155
        assertTrue(helper.fireCalled && helper.keyEventKeyCode == KeyEvent.VK_INSERT);
    }

    @Test
    public void testKeyReleased() {
        // No functionality to test
    }

    @Test
    public void testKeyTyped() {
        // No functionality to test
    }

    // Test class for SimpleKeyListener
    private class SimpleKeyListener {
        public boolean enterCalled = false;
        public boolean insertCalled = false;
        public boolean deleteCalled = false;
        public boolean tabCalled = false;

        public void enter(Component component) {
            enterCalled = true;
        }

        public void insert(Component component) {
            insertCalled = true;
        }

        public void delete(Component component) {
            deleteCalled = true;
        }

        public void tab(Component component) {
            tabCalled = true;
        }
    }
}
