package com.lts.event;

import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleKeyListenerHelperTest {

    @Test
    public void testDetach() {
        // Test case for detach method
        Component component = new Component();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        assertNotNull(helper.component);
        helper.detach();
        assertNull(helper.component);
    }

    @Test
    public void testNotifyListener() {
        // Test case for notifyListener method
        Component component = new Component();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        SimpleKeyListener listener = new SimpleKeyListener() {
            @Override
            public void enter(Component c) {
                assertTrue(c.equals(component));
            }

            @Override
            public void insert(Component c) {
                assertTrue(c.equals(component));
            }

            @Override
            public void delete(Component c) {
                assertTrue(c.equals(component));
            }

            @Override
            public void tab(Component c) {
                assertTrue(c.equals(component));
            }
        };

        helper.notifyListener(listener, 10, null);
        helper.notifyListener(listener, 155, null);
        helper.notifyListener(listener, 127, null);
        helper.notifyListener(listener, 9, null);
    }

    @Test
    public void testKeyPressed() {
        // Test case for keyPressed method
        KeyEvent event = new KeyEvent(new Component(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER);
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(new Component());
        helper.keyPressed(event);
        // Test for edge cases
        event = new KeyEvent(new Component(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_TAB);
        helper.keyPressed(event);

    }

    @Test
    public void testKeyReleased() {
        // Test case for keyReleased method
        // No functionality to test
    }

    @Test
    public void testKeyTyped() {
        // Test case for keyTyped method
        // No functionality to test
    }
}