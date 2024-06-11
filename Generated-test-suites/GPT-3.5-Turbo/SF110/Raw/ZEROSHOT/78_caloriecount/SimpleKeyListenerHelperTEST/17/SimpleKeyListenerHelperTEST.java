package com.lts.event;

import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.*;
import java.awt.event.*;

public class SimpleKeyListenerHelperTest {

    @Test
    public void testDetach() {
        Component component = new Component();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        assertNotNull(component.getKeyListeners());
        helper.detach();
        assertNull(component.getKeyListeners());
    }

    @Test
    public void testNotifyListener() {
        Component component = new Component();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        SimpleKeyListener listener = new SimpleKeyListener();
        helper.notifyListener(listener, 10, null);
        assertTrue(listener.isEnterCalled());
        helper.notifyListener(listener, 155, null);
        assertTrue(listener.isInsertCalled());
        helper.notifyListener(listener, 127, null);
        assertTrue(listener.isDeleteCalled());
        helper.notifyListener(listener, 9, null);
        assertTrue(listener.isTabCalled());
    }

    @Test
    public void testKeyPressed() {
        Component component = new Component();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER);
        helper.keyPressed(event);
        assertTrue(event.isConsumed());
        event = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_INSERT);
        helper.keyPressed(event);
        assertTrue(event.isConsumed());
        event = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_DELETE);
        helper.keyPressed(event);
        assertTrue(event.isConsumed());
        event = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_TAB);
        helper.keyPressed(event);
        assertTrue(event.isConsumed());
    }

    @Test
    public void testKeyReleased() {
        Component component = new Component();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, KeyEvent.VK_A);
        helper.keyReleased(event);
        assertFalse(event.isConsumed());
    }

    @Test
    public void testKeyTyped() {
        Component component = new Component();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_TYPED, System.currentTimeMillis(), 0, KeyEvent.VK_A);
        helper.keyTyped(event);
        assertFalse(event.isConsumed());
    }
}