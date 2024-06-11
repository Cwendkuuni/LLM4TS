package com.lts.event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.awt.event.KeyEvent;
import java.awt.Component;

import org.junit.Before;
import org.junit.Test;

public class SimpleKeyListenerHelperTest {

    private Component component;
    private SimpleKeyListenerHelper keyListenerHelper;

    @Before
    public void setUp() {
        component = mock(Component.class);
        keyListenerHelper = new SimpleKeyListenerHelper(component);
    }

    @Test
    public void testDetach() {
        keyListenerHelper.detach();
        assertNull(keyListenerHelper.component);
    }

    @Test
    public void testNotifyListener_enterType() {
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        keyListenerHelper.notifyListener(listener, 10, null);
        verify(listener).enter(component);
    }

    @Test
    public void testNotifyListener_insertType() {
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        keyListenerHelper.notifyListener(listener, 155, null);
        verify(listener).insert(component);
    }

    @Test
    public void testNotifyListener_deleteType() {
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        keyListenerHelper.notifyListener(listener, 127, null);
        verify(listener).delete(component);
    }

    @Test
    public void testNotifyListener_tabType() {
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        keyListenerHelper.notifyListener(listener, 9, null);
        verify(listener).tab(component);
    }

    @Test
    public void testKeyPressed_tabKeyCode() {
        KeyEvent event = mock(KeyEvent.class);
        keyListenerHelper.keyPressed(event);
        verify(keyListenerHelper).fire(KeyEvent.VK_TAB);
    }

    @Test
    public void testKeyPressed_enterKeyCode() {
        KeyEvent event = mock(KeyEvent.class);
        keyListenerHelper.keyPressed(event);
        verify(keyListenerHelper).fire(KeyEvent.VK_ENTER);
    }

    @Test
    public void testKeyPressed_deleteKeyCode() {
        KeyEvent event = mock(KeyEvent.class);
        keyListenerHelper.keyPressed(event);
        verify(keyListenerHelper).fire(KeyEvent.VK_DELETE);
    }

    @Test
    public void testKeyPressed_insertKeyCode() {
        KeyEvent event = mock(KeyEvent.class);
        keyListenerHelper.keyPressed(event);
        verify(keyListenerHelper).fire(KeyEvent.VK_INSERT);
    }

    @Test
    public void testKeyReleased() {
        KeyEvent event = mock(KeyEvent.class);
        keyListenerHelper.keyReleased(event);
        // No logic to test
    }

    @Test
    public void testKeyTyped() {
        KeyEvent event = mock(KeyEvent.class);
        keyListenerHelper.keyTyped(event);
        // No logic to test
    }
}