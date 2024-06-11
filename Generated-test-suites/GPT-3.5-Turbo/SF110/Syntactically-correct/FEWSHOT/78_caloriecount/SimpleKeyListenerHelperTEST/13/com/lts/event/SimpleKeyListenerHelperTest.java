package com.lts.event;

import static org.junit.Assert.*;
import org.junit.Test;
import java.awt.*;
import java.awt.event.*;

public class SimpleKeyListenerHelperTest {

    @Test
    public void testDetach() {
        Component component = new Component() {};
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        helper.detach();
        assertNull(helper.component);
    }

    @Test
    public void testNotifyListenerEnter() {
        Component component = new Component() {};
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        SimpleKeyListener listener = new SimpleKeyListener() {
            @Override
            public void enter(Component component) {
                assertEquals(component, helper.component);
            }
        };
        helper.notifyListener(listener, 10, null);
    }

    @Test
    public void testNotifyListenerInsert() {
        Component component = new Component() {};
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        SimpleKeyListener listener = new SimpleKeyListener() {
            @Override
            public void insert(Component component) {
                assertEquals(component, helper.component);
            }
        };
        helper.notifyListener(listener, 155, null);
    }

    @Test
    public void testNotifyListenerDelete() {
        Component component = new Component() {};
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        SimpleKeyListener listener = new SimpleKeyListener() {
            @Override
            public void delete(Component component) {
                assertEquals(component, helper.component);
            }
        };
        helper.notifyListener(listener, 127, null);
    }

    @Test
    public void testNotifyListenerTab() {
        Component component = new Component() {};
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        SimpleKeyListener listener = new SimpleKeyListener() {
            @Override
            public void tab(Component component) {
                assertEquals(component, helper.component);
            }
        };
        helper.notifyListener(listener, 9, null);
    }

    @Test
    public void testKeyPressedFireTab() {
        Component component = new Component() {};
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_TAB);
        helper.keyPressed(event);
        // Verify that fire() is called with the expected keyCode
    }

    @Test
    public void testKeyPressedFireEnter() {
        Component component = new Component() {};
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER);
        helper.keyPressed(event);
        // Verify that fire() is called with the expected keyCode
    }

    @Test
    public void testKeyPressedFireDelete() {
        Component component = new Component() {};
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_DELETE);
        helper.keyPressed(event);
        // Verify that fire() is called with the expected keyCode
    }

    @Test
    public void testKeyPressedFireInsert() {
        Component component = new Component() {};
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_INSERT);
        helper.keyPressed(event);
        // Verify that fire() is called with the expected keyCode
    }

    @Test
    public void testKeyReleased() {
        Component component = new Component() {};
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER);
        helper.keyReleased(event);
        // Verify that no action is taken
    }

    @Test
    public void testKeyTyped() {
        Component component = new Component() {};
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_TYPED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER);
        helper.keyTyped(event);
        // Verify that no action is taken
    }
}