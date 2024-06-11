package com.lts.event;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.event.*;

public class SimpleKeyListenerHelperTest {

    private Component component;
    private SimpleKeyListenerHelper listenerHelper;

    @Before
    public void setUp() {
        component = new Component();
        listenerHelper = new SimpleKeyListenerHelper(component);
    }

    @Test
    public void testDetach() {
        listenerHelper.detach();
        assertNull(listenerHelper.getComponent());
    }

    @Test
    public void testNotifyListenerEnter() {
        SimpleKeyListener listener = new SimpleKeyListener();

        listenerHelper.notifyListener(listener, 10, null);

        assertTrue(listener.isEnterCalled);
        assertFalse(listener.isInsertCalled);
        assertFalse(listener.isDeleteCalled);
        assertFalse(listener.isTabCalled);
    }

    @Test
    public void testNotifyListenerInsert() {
        SimpleKeyListener listener = new SimpleKeyListener();

        listenerHelper.notifyListener(listener, 155, null);

        assertFalse(listener.isEnterCalled);
        assertTrue(listener.isInsertCalled);
        assertFalse(listener.isDeleteCalled);
        assertFalse(listener.isTabCalled);
    }

    @Test
    public void testNotifyListenerDelete() {
        SimpleKeyListener listener = new SimpleKeyListener();

        listenerHelper.notifyListener(listener, 127, null);

        assertFalse(listener.isEnterCalled);
        assertFalse(listener.isInsertCalled);
        assertTrue(listener.isDeleteCalled);
        assertFalse(listener.isTabCalled);
    }

    @Test
    public void testNotifyListenerTab() {
        SimpleKeyListener listener = new SimpleKeyListener();

        listenerHelper.notifyListener(listener, 9, null);

        assertFalse(listener.isEnterCalled);
        assertFalse(listener.isInsertCalled);
        assertFalse(listener.isDeleteCalled);
        assertTrue(listener.isTabCalled);
    }

    @Test
    public void testKeyPressedEnter() {
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, KeyEvent.CHAR_UNDEFINED);

        listenerHelper.keyPressed(event);

        verifyListenerCalls(true, false, false, false);
    }

    @Test
    public void testKeyPressedInsert() {
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_INSERT, KeyEvent.CHAR_UNDEFINED);

        listenerHelper.keyPressed(event);

        verifyListenerCalls(false, true, false, false);
    }

    @Test
    public void testKeyPressedDelete() {
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_DELETE, KeyEvent.CHAR_UNDEFINED);

        listenerHelper.keyPressed(event);

        verifyListenerCalls(false, false, true, false);
    }

    @Test
    public void testKeyPressedTab() {
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_TAB, KeyEvent.CHAR_UNDEFINED);

        listenerHelper.keyPressed(event);

        verifyListenerCalls(false, false, false, true);
    }

    @Test
    public void testKeyReleased() {
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, KeyEvent.CHAR_UNDEFINED);

        listenerHelper.keyReleased(event);

        // No action to be tested
    }

    @Test
    public void testKeyTyped() {
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_TYPED, System.currentTimeMillis(), 0, KeyEvent.VK_UNDEFINED, 'a');

        listenerHelper.keyTyped(event);

        // No action to be tested
    }

    private void verifyListenerCalls(boolean isEnterCalled, boolean isInsertCalled, boolean isDeleteCalled, boolean isTabCalled) {
        SimpleKeyListener listener = new SimpleKeyListener();
        listenerHelper.notifyListener(listener, 10, null);
        assertEquals(isEnterCalled, listener.isEnterCalled);

        listener = new SimpleKeyListener();
        listenerHelper.notifyListener(listener, 155, null);
        assertEquals(isInsertCalled, listener.isInsertCalled);

        listener = new SimpleKeyListener();
        listenerHelper.notifyListener(listener, 127, null);
        assertEquals(isDeleteCalled, listener.isDeleteCalled);

        listener = new SimpleKeyListener();
        listenerHelper.notifyListener(listener, 9, null);
        assertEquals(isTabCalled, listener.isTabCalled);
    }
    
    private class SimpleKeyListener implements KeyListener {
        boolean isEnterCalled = false;
        boolean isInsertCalled = false;
        boolean isDeleteCalled = false;
        boolean isTabCalled = false;

        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }

        public void enter(Component component) {
            isEnterCalled = true;
        }

        public void insert(Component component) {
            isInsertCalled = true;
        }

        public void delete(Component component) {
            isDeleteCalled = true;
        }

        public void tab(Component component) {
            isTabCalled = true;
        }
    }
}