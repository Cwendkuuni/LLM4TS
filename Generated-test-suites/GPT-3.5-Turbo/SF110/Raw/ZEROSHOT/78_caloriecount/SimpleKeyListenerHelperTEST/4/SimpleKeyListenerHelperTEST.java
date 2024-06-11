package com.lts.event;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.awt.*;
import java.awt.event.*;

public class SimpleKeyListenerHelperTest {

    private SimpleKeyListenerHelper simpleKeyListenerHelper;
    private Component component;

    @Before
    public void setUp() {
        component = new Component() {};
        simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
    }

    @After
    public void tearDown() {
        simpleKeyListenerHelper.detach();
    }

    @Test
    public void testDetach() {
        simpleKeyListenerHelper.detach();
        Assert.assertNull(simpleKeyListenerHelper.component);
    }

    @Test
    public void testNotifyListener_Enter() {
        SimpleKeyListener listener = new SimpleKeyListener() {
            public void enter(Component component) {}
            public void insert(Component component) {}
            public void delete(Component component) {}
            public void tab(Component component) {}
        };
        simpleKeyListenerHelper.notifyListener(listener, 10, null);
        // Add assertion here
    }

    @Test
    public void testNotifyListener_Insert() {
        SimpleKeyListener listener = new SimpleKeyListener() {
            public void enter(Component component) {}
            public void insert(Component component) {}
            public void delete(Component component) {}
            public void tab(Component component) {}
        };
        simpleKeyListenerHelper.notifyListener(listener, 155, null);
        // Add assertion here
    }

    @Test
    public void testNotifyListener_Delete() {
        SimpleKeyListener listener = new SimpleKeyListener() {
            public void enter(Component component) {}
            public void insert(Component component) {}
            public void delete(Component component) {}
            public void tab(Component component) {}
        };
        simpleKeyListenerHelper.notifyListener(listener, 127, null);
        // Add assertion here
    }

    @Test
    public void testNotifyListener_Tab() {
        SimpleKeyListener listener = new SimpleKeyListener() {
            public void enter(Component component) {}
            public void insert(Component component) {}
            public void delete(Component component) {}
            public void tab(Component component) {}
        };
        simpleKeyListenerHelper.notifyListener(listener, 9, null);
        // Add assertion here
    }

    @Test
    public void testKeyPressed_Tab() {
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_TAB, KeyEvent.CHAR_UNDEFINED);
        simpleKeyListenerHelper.keyPressed(event);
        // Add assertion here
    }

    @Test
    public void testKeyPressed_Enter() {
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, KeyEvent.CHAR_UNDEFINED);
        simpleKeyListenerHelper.keyPressed(event);
        // Add assertion here
    }

    @Test
    public void testKeyPressed_Delete() {
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_DELETE, KeyEvent.CHAR_UNDEFINED);
        simpleKeyListenerHelper.keyPressed(event);
        // Add assertion here
    }

    @Test
    public void testKeyPressed_Insert() {
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_INSERT, KeyEvent.CHAR_UNDEFINED);
        simpleKeyListenerHelper.keyPressed(event);
        // Add assertion here
    }

}