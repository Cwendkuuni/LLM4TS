package com.lts.event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import java.awt.Component;
import java.awt.event.KeyEvent;

public class SimpleKeyListenerHelperTest {

    private SimpleKeyListenerHelper simpleKeyListenerHelper;
    private SimpleKeyListener listener;
    private Component component;

    @Before
    public void setup() {
        listener = mock(SimpleKeyListener.class);
        component = mock(Component.class);
        simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
        simpleKeyListenerHelper.addListener(listener);
    }

    @Test
    public void testDetach() {
        simpleKeyListenerHelper.detach();
        verify(component).removeKeyListener(simpleKeyListenerHelper);
        assertNull(simpleKeyListenerHelper.getComponent());
    }

    @Test
    public void testNotifyListener_Enter() {
        simpleKeyListenerHelper.notifyListener(listener, 10, null);
        verify(listener).enter(component);
    }

    @Test
    public void testNotifyListener_Insert() {
        simpleKeyListenerHelper.notifyListener(listener, 155, null);
        verify(listener).insert(component);
    }

    @Test
    public void testNotifyListener_Delete() {
        simpleKeyListenerHelper.notifyListener(listener, 127, null);
        verify(listener).delete(component);
    }

    @Test
    public void testNotifyListener_Tab() {
        simpleKeyListenerHelper.notifyListener(listener, 9, null);
        verify(listener).tab(component);
    }

    @Test
    public void testKeyPressed_Tab() {
        KeyEvent event = mock(KeyEvent.class);
        when(event.getKeyCode()).thenReturn(9);

        simpleKeyListenerHelper.keyPressed(event);
        verify(listener).tab(component);
    }

    @Test
    public void testKeyPressed_Enter() {
        KeyEvent event = mock(KeyEvent.class);
        when(event.getKeyCode()).thenReturn(10);

        simpleKeyListenerHelper.keyPressed(event);
        verify(listener).enter(component);
    }

    @Test
    public void testKeyPressed_Delete() {
        KeyEvent event = mock(KeyEvent.class);
        when(event.getKeyCode()).thenReturn(127);

        simpleKeyListenerHelper.keyPressed(event);
        verify(listener).delete(component);
    }

    @Test
    public void testKeyPressed_Insert() {
        KeyEvent event = mock(KeyEvent.class);
        when(event.getKeyCode()).thenReturn(155);

        simpleKeyListenerHelper.keyPressed(event);
        verify(listener).insert(component);
    }

    @Test
    public void testKeyReleased() {
        KeyEvent event = mock(KeyEvent.class);

        simpleKeyListenerHelper.keyReleased(event);
        // No assertion here as it is an empty implementation
    }

    @Test
    public void testKeyTyped() {
        KeyEvent event = mock(KeyEvent.class);

        simpleKeyListenerHelper.keyTyped(event);
        // No assertion here as it is an empty implementation
    }

}