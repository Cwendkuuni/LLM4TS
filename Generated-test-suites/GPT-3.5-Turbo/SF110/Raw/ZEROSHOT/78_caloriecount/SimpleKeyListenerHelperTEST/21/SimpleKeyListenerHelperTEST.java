package com.lts.event;

import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.event.KeyEvent;

import static org.mockito.Mockito.*;

public class SimpleKeyListenerHelperTest {

    private Component component;
    private SimpleKeyListener simpleKeyListener;
    private SimpleKeyListenerHelper simpleKeyListenerHelper;

    @Before
    public void setUp() {
        component = mock(Component.class);
        simpleKeyListener = mock(SimpleKeyListener.class);
        simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
        simpleKeyListenerHelper.addListener(simpleKeyListener);
    }

    @Test
    public void testDetach() {
        simpleKeyListenerHelper.detach();

        verify(component).removeKeyListener(simpleKeyListenerHelper);
        verifyNoMoreInteractions(component, simpleKeyListener);
    }

    @Test
    public void testNotifyListener_Enter() {
        KeyEvent event = mock(KeyEvent.class);
        when(event.getKeyCode()).thenReturn(KeyEvent.VK_ENTER);

        simpleKeyListenerHelper.keyPressed(event);

        verify(simpleKeyListener).enter(component);
        verifyNoMoreInteractions(component, simpleKeyListener);
    }

    @Test
    public void testNotifyListener_Insert() {
        KeyEvent event = mock(KeyEvent.class);
        when(event.getKeyCode()).thenReturn(155);

        simpleKeyListenerHelper.keyPressed(event);

        verify(simpleKeyListener).insert(component);
        verifyNoMoreInteractions(component, simpleKeyListener);
    }

    @Test
    public void testNotifyListener_Delete() {
        KeyEvent event = mock(KeyEvent.class);
        when(event.getKeyCode()).thenReturn(127);

        simpleKeyListenerHelper.keyPressed(event);

        verify(simpleKeyListener).delete(component);
        verifyNoMoreInteractions(component, simpleKeyListener);
    }

    @Test
    public void testNotifyListener_Tab() {
        KeyEvent event = mock(KeyEvent.class);
        when(event.getKeyCode()).thenReturn(KeyEvent.VK_TAB);

        simpleKeyListenerHelper.keyPressed(event);

        verify(simpleKeyListener).tab(component);
        verifyNoMoreInteractions(component, simpleKeyListener);
    }
}