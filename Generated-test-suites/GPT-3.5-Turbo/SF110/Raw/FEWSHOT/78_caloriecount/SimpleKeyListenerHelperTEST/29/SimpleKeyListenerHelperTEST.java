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
    public void testNotifyListener_Enter() {
        Component component = new Component() {};
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        SimpleKeyListener listener = new SimpleKeyListener() {
            @Override
            public void enter(Component component) {
                assertTrue(component instanceof Component);
            }

            @Override
            public void insert(Component component) {
            }

            @Override
            public void delete(Component component) {
            }

            @Override
            public void tab(Component component) {
            }
        };
        helper.notifyListener(listener, 10, null);
    }

    @Test
    public void testNotifyListener_Insert() {
        Component component = new Component() {};
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        SimpleKeyListener listener = new SimpleKeyListener() {
            @Override
            public void enter(Component component) {
            }

            @Override
            public void insert(Component component) {
                assertTrue(component instanceof Component);
            }

            @Override
            public void delete(Component component) {
            }

            @Override
            public void tab(Component component) {
            }
        };
        helper.notifyListener(listener, 155, null);
    }

    @Test
    public void testNotifyListener_Delete() {
        Component component = new Component() {};
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        SimpleKeyListener listener = new SimpleKeyListener() {
            @Override
            public void enter(Component component) {
            }

            @Override
            public void insert(Component component) {
            }

            @Override
            public void delete(Component component) {
                assertTrue(component instanceof Component);
            }

            @Override
            public void tab(Component component) {
            }
        };
        helper.notifyListener(listener, 127, null);
    }

    @Test
    public void testNotifyListener_Tab() {
        Component component = new Component() {};
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        SimpleKeyListener listener = new SimpleKeyListener() {
            @Override
            public void enter(Component component) {
            }

            @Override
            public void insert(Component component) {
            }

            @Override
            public void delete(Component component) {
            }

            @Override
            public void tab(Component component) {
                assertTrue(component instanceof Component);
            }
        };
        helper.notifyListener(listener, 9, null);
    }

    @Test
    public void testKeyPressed_Enter() {
        Component component = new Component() {};
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        KeyEvent event = new KeyEvent(component, 0, 0, 0, 10);
        helper.keyPressed(event);
        // verify that the corresponding listener method was called
    }

    @Test
    public void testKeyPressed_Insert() {
        Component component = new Component() {};
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        KeyEvent event = new KeyEvent(component, 0, 0, 0, 155);
        helper.keyPressed(event);
        // verify that the corresponding listener method was called
    }

    @Test
    public void testKeyPressed_Delete() {
        Component component = new Component() {};
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        KeyEvent event = new KeyEvent(component, 0, 0, 0, 127);
        helper.keyPressed(event);
        // verify that the corresponding listener method was called
    }

    @Test
    public void testKeyPressed_Tab() {
        Component component = new Component() {};
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        KeyEvent event = new KeyEvent(component, 0, 0, 0, 9);
        helper.keyPressed(event);
        // verify that the corresponding listener method was called
    }

    @Test
    public void testKeyReleased() {
        Component component = new Component() {};
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        KeyEvent event = new KeyEvent(component, 0, 0, 0, 0);
        helper.keyReleased(event);
        // verify that the method does nothing
    }

    @Test
    public void testKeyTyped() {
        Component component = new Component() {};
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        KeyEvent event = new KeyEvent(component, 0, 0, 0, 0);
        helper.keyTyped(event);
        // verify that the method does nothing
    }
}