package com.lts.event;

import org.junit.Before;
import org.junit.Test;

import java.awt.Component;
import java.awt.event.KeyEvent;

import static org.mockito.Mockito.*;

public class SimpleKeyListenerHelperTest {

    private SimpleKeyListenerHelper simpleKeyListenerHelper;
    private SimpleKeyListener dummyListener;
    private Component mockComponent;

    @Before
    public void setUp() {
        mockComponent = mock(Component.class);
        dummyListener = mock(SimpleKeyListener.class);
        simpleKeyListenerHelper = new SimpleKeyListenerHelper(mockComponent);
    }

    @Test
    public void testDetach() {
        simpleKeyListenerHelper.detach();
        verify(mockComponent).removeKeyListener(simpleKeyListenerHelper);
    }

    @Test
    public void testNotifyListener_Enter() {
        Object dummyObject = new Object();
        int type = 10;
        Object data = new Object();

        simpleKeyListenerHelper.notifyListener(dummyListener, type, data);
        verify(dummyListener).enter(mockComponent);
    }

    @Test
    public void testNotifyListener_Insert() {
        Object dummyObject = new Object();
        int type = 155;
        Object data = new Object();

        simpleKeyListenerHelper.notifyListener(dummyListener, type, data);
        verify(dummyListener).insert(mockComponent);
    }

    @Test
    public void testNotifyListener_Delete() {
        Object dummyObject = new Object();
        int type = 127;
        Object data = new Object();

        simpleKeyListenerHelper.notifyListener(dummyListener, type, data);
        verify(dummyListener).delete(mockComponent);
    }

    @Test
    public void testNotifyListener_Tab() {
        Object dummyObject = new Object();
        int type = 9;
        Object data = new Object();

        simpleKeyListenerHelper.notifyListener(dummyListener, type, data);
        verify(dummyListener).tab(mockComponent);
    }

    @Test
    public void testKeyPressed() {
        KeyEvent mockKeyEvent = mock(KeyEvent.class);

        when(mockKeyEvent.getKeyCode()).thenReturn(KeyEvent.VK_TAB);
        simpleKeyListenerHelper.keyPressed(mockKeyEvent);
        verify(dummyListener).tab(mockComponent);

        when(mockKeyEvent.getKeyCode()).thenReturn(KeyEvent.VK_ENTER);
        simpleKeyListenerHelper.keyPressed(mockKeyEvent);
        verify(dummyListener).enter(mockComponent);

        when(mockKeyEvent.getKeyCode()).thenReturn(KeyEvent.VK_DELETE);
        simpleKeyListenerHelper.keyPressed(mockKeyEvent);
        verify(dummyListener).delete(mockComponent);

        when(mockKeyEvent.getKeyCode()).thenReturn(KeyEvent.VK_UNDEFINED);
        simpleKeyListenerHelper.keyPressed(mockKeyEvent);
        verifyZeroInteractions(dummyListener);
    }

    @Test
    public void testKeyReleased() {
        KeyEvent mockKeyEvent = mock(KeyEvent.class);

        simpleKeyListenerHelper.keyReleased(mockKeyEvent);
        verifyZeroInteractions(dummyListener);
    }

    @Test
    public void testKeyTyped() {
        KeyEvent mockKeyEvent = mock(KeyEvent.class);

        simpleKeyListenerHelper.keyTyped(mockKeyEvent);
        verifyZeroInteractions(dummyListener);
    }
}