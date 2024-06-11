package com.lts.event;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static org.mockito.Mockito.*;

public class SimpleKeyListenerHelperTest {

    private SimpleKeyListenerHelper simpleKeyListenerHelper;
    private Component component;
    private SimpleKeyListener listener;

    @Before
    public void setUp() {
        component = Mockito.mock(Component.class);
        listener = Mockito.mock(SimpleKeyListener.class);
        simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
    }

    @Test
    public void testDetach() {
        simpleKeyListenerHelper.detach();
        verify(component).removeKeyListener(simpleKeyListenerHelper);
        verifyNoMoreInteractions(component);
    }

    @Test
    public void testNotifyListenerEnter() {
        Object o = listener;
        int type = 10;
        Object data = null;

        simpleKeyListenerHelper.notifyListener(o, type, data);

        verify(listener).enter(component);
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void testNotifyListenerInsert() {
        Object o = listener;
        int type = 155;
        Object data = null;

        simpleKeyListenerHelper.notifyListener(o, type, data);

        verify(listener).insert(component);
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void testNotifyListenerDelete() {
        Object o = listener;
        int type = 127;
        Object data = null;

        simpleKeyListenerHelper.notifyListener(o, type, data);

        verify(listener).delete(component);
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void testNotifyListenerTab() {
        Object o = listener;
        int type = 9;
        Object data = null;

        simpleKeyListenerHelper.notifyListener(o, type, data);

        verify(listener).tab(component);
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void testKeyPressedWithValidKeyCodes() {
        KeyEvent event = mock(KeyEvent.class);

        when(event.getKeyCode()).thenReturn(KeyEvent.VK_TAB);
        simpleKeyListenerHelper.keyPressed(event);
        verify(simpleKeyListenerHelper).fire(KeyEvent.VK_TAB);

        when(event.getKeyCode()).thenReturn(KeyEvent.VK_ENTER);
        simpleKeyListenerHelper.keyPressed(event);
        verify(simpleKeyListenerHelper).fire(KeyEvent.VK_ENTER);

        when(event.getKeyCode()).thenReturn(KeyEvent.VK_DELETE);
        simpleKeyListenerHelper.keyPressed(event);
        verify(simpleKeyListenerHelper).fire(KeyEvent.VK_DELETE);

        when(event.getKeyCode()).thenReturn(155);
        simpleKeyListenerHelper.keyPressed(event);
        verify(simpleKeyListenerHelper).fire(155);
    }

    @Test
    public void testKeyPressedWithInvalidKeyCode() {
        KeyEvent event = mock(KeyEvent.class);
        when(event.getKeyCode()).thenReturn(KeyEvent.VK_A);

        simpleKeyListenerHelper.keyPressed(event);

        verifyNoMoreInteractions(simpleKeyListenerHelper);
    }

    @Test
    public void testKeyReleased() {
        KeyEvent event = mock(KeyEvent.class);
        simpleKeyListenerHelper.keyReleased(event);
        verifyNoMoreInteractions(event);
    }

    @Test
    public void testKeyTyped() {
        KeyEvent event = mock(KeyEvent.class);
        simpleKeyListenerHelper.keyTyped(event);
        verifyNoMoreInteractions(event);
    }
}