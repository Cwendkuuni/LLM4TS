package com.lts.event;

import org.junit.Test;
import static org.mockito.Mockito.*;

import java.awt.*;
import java.awt.event.*;

public class SimpleKeyListenerHelperTest {

    @Test
    public void testDetach() {
        Component mockedComponent = mock(Component.class);
        SimpleKeyListenerHelper listenerHelper = new SimpleKeyListenerHelper(mockedComponent);

        listenerHelper.detach();

        verify(mockedComponent, times(1)).removeKeyListener(listenerHelper);
        assertNull(listenerHelper.component);
    }

    @Test
    public void testNotifyListener() {
        Component mockedComponent = mock(Component.class);
        SimpleKeyListenerHelper listenerHelper = new SimpleKeyListenerHelper(mockedComponent);
        SimpleKeyListener mockedListener = mock(SimpleKeyListener.class);

        listenerHelper.notifyListener(mockedListener, 10, null);
        verify(mockedListener, times(1)).enter(mockedComponent);

        listenerHelper.notifyListener(mockedListener, 155, null);
        verify(mockedListener, times(1)).insert(mockedComponent);

        listenerHelper.notifyListener(mockedListener, 127, null);
        verify(mockedListener, times(1)).delete(mockedComponent);

        listenerHelper.notifyListener(mockedListener, 9, null);
        verify(mockedListener, times(1)).tab(mockedComponent);
    }

    @Test
    public void testKeyPressed_fireEvent() {
        Component mockedComponent = mock(Component.class);
        SimpleKeyListenerHelper listenerHelper = new SimpleKeyListenerHelper(mockedComponent);

        KeyEvent mockedEvent = mock(KeyEvent.class);
        when(mockedEvent.getKeyCode()).thenReturn(KeyEvent.VK_ENTER);

        listenerHelper.keyPressed(mockedEvent);

        verify(mockedComponent, times(1)).fire(KeyEvent.VK_ENTER);
    }

    @Test
    public void testKeyPressed_noEvent() {
        Component mockedComponent = mock(Component.class);
        SimpleKeyListenerHelper listenerHelper = new SimpleKeyListenerHelper(mockedComponent);

        KeyEvent mockedEvent = mock(KeyEvent.class);
        when(mockedEvent.getKeyCode()).thenReturn(KeyEvent.VK_ESCAPE);

        listenerHelper.keyPressed(mockedEvent);

        verify(mockedComponent, times(0)).fire(anyInt());
    }

    @Test
    public void testKeyReleased() {
        SimpleKeyListenerHelper listenerHelper = new SimpleKeyListenerHelper(mock(Component.class));

        KeyEvent mockedEvent = mock(KeyEvent.class);

        listenerHelper.keyReleased(mockedEvent);

        // nothing to test here, just testing the method does not produce any exceptions
    }

    @Test
    public void testKeyTyped() {
        SimpleKeyListenerHelper listenerHelper = new SimpleKeyListenerHelper(mock(Component.class));

        KeyEvent mockedEvent = mock(KeyEvent.class);

        listenerHelper.keyTyped(mockedEvent);

        // nothing to test here, just testing the method does not produce any exceptions
    }
}