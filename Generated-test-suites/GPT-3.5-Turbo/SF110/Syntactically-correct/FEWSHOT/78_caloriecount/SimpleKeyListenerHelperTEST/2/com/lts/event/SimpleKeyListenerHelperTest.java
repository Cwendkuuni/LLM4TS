package com.lts.event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.awt.Component;
import java.awt.event.KeyEvent;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SimpleKeyListenerHelperTest {
    private Component component;
    private SimpleKeyListenerHelper listenerHelper;

    @Before
    public void setup() {
        component = mock(Component.class);
        listenerHelper = new SimpleKeyListenerHelper(component);
    }

    @After
    public void teardown() {
        listenerHelper.detach();
    }

    @Test
    public void testDetach() {
        listenerHelper.detach();
        assertNull(listenerHelper.getComponent());
        verify(component, times(1)).removeKeyListener(listenerHelper);
    }

    @Test
    public void testNotifyListenerEnter() {
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, 0, KeyEvent.VK_ENTER);
        listenerHelper.notifyListener(listener, 10, null);
        verify(listener, times(1)).enter(component);
    }

    @Test
    public void testNotifyListenerInsert() {
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, 0, KeyEvent.VK_INSERT);
        listenerHelper.notifyListener(listener, 155, null);
        verify(listener, times(1)).insert(component);
    }

    @Test
    public void testNotifyListenerDelete() {
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, 0, KeyEvent.VK_DELETE);
        listenerHelper.notifyListener(listener, 127, null);
        verify(listener, times(1)).delete(component);
    }

    @Test
    public void testNotifyListenerTab() {
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, 0, KeyEvent.VK_TAB);
        listenerHelper.notifyListener(listener, 9, null);
        verify(listener, times(1)).tab(component);
    }

    @Test
    public void testKeyPressedEnter() {
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, 0, KeyEvent.VK_ENTER);
        listenerHelper.keyPressed(event);
        verify(component, times(1)).fire(10);
    }

    @Test
    public void testKeyPressedInsert() {
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, 0, KeyEvent.VK_INSERT);
        listenerHelper.keyPressed(event);
        verify(component, times(1)).fire(155);
    }

    @Test
    public void testKeyPressedDelete() {
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, 0, KeyEvent.VK_DELETE);
        listenerHelper.keyPressed(event);
        verify(component, times(1)).fire(127);
    }

    @Test
    public void testKeyPressedTab() {
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, 0, KeyEvent.VK_TAB);
        listenerHelper.keyPressed(event);
        verify(component, times(1)).fire(9);
    }

    @Test
    public void testKeyReleased() {
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_RELEASED, 0, KeyEvent.VK_ENTER);
        listenerHelper.keyReleased(event);
        // No action expected
    }

    @Test
    public void testKeyTyped() {
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_TYPED, 0);
        listenerHelper.keyTyped(event);
        // No action expected
    }
}