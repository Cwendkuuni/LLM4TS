package com.lts.event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.awt.Component;
import java.awt.event.KeyEvent;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class SimpleKeyListenerHelperTest {

    @Mock
    private Component component;
    private SimpleKeyListenerHelper helper;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        helper = new SimpleKeyListenerHelper(component);
    }

    @Test
    public void testDetach() {
        helper.detach();
        verify(component).removeKeyListener(helper);
        assertNull(helper.component);
    }

    @Test
    public void testNotifyListener_enter() {
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        KeyEvent event = mock(KeyEvent.class);
        when(event.getKeyCode()).thenReturn(10);
        helper.notifyListener(listener, 10, null);
        verify(listener).enter(component);
    }

    @Test
    public void testNotifyListener_insert() {
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        KeyEvent event = mock(KeyEvent.class);
        when(event.getKeyCode()).thenReturn(155);
        helper.notifyListener(listener, 155, null);
        verify(listener).insert(component);
    }

    @Test
    public void testNotifyListener_delete() {
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        KeyEvent event = mock(KeyEvent.class);
        when(event.getKeyCode()).thenReturn(127);
        helper.notifyListener(listener, 127, null);
        verify(listener).delete(component);
    }

    @Test
    public void testNotifyListener_tab() {
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        KeyEvent event = mock(KeyEvent.class);
        when(event.getKeyCode()).thenReturn(9);
        helper.notifyListener(listener, 9, null);
        verify(listener).tab(component);
    }

    @Test
    public void testKeyPressed_tab() {
        KeyEvent event = mock(KeyEvent.class);
        when(event.getKeyCode()).thenReturn(9);
        helper.keyPressed(event);
        verify(helper).fire(9);
    }

    @Test
    public void testKeyPressed_enter() {
        KeyEvent event = mock(KeyEvent.class);
        when(event.getKeyCode()).thenReturn(10);
        helper.keyPressed(event);
        verify(helper).fire(10);
    }

    @Test
    public void testKeyPressed_delete() {
        KeyEvent event = mock(KeyEvent.class);
        when(event.getKeyCode()).thenReturn(127);
        helper.keyPressed(event);
        verify(helper).fire(127);
    }

    @Test
    public void testKeyPressed_insert() {
        KeyEvent event = mock(KeyEvent.class);
        when(event.getKeyCode()).thenReturn(155);
        helper.keyPressed(event);
        verify(helper).fire(155);
    }

    @Test
    public void testKeyReleased() {
        KeyEvent event = mock(KeyEvent.class);
        helper.keyReleased(event);
    }

    @Test
    public void testKeyTyped() {
        KeyEvent event = mock(KeyEvent.class);
        helper.keyTyped(event);
    }
}