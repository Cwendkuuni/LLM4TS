package com.lts.event;

import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class SimpleKeyListenerHelperTest {

    private SimpleKeyListenerHelper simpleKeyListenerHelper;
    private SimpleKeyListener simpleKeyListenerMock;
    private Component componentMock;

    @Before
    public void setUp() {
        componentMock = mock(Component.class);
        simpleKeyListenerMock = mock(SimpleKeyListener.class);
        simpleKeyListenerHelper = new SimpleKeyListenerHelper(componentMock);
        simpleKeyListenerHelper.addListener(simpleKeyListenerMock);
    }

    @Test
    public void testDetach() {
        simpleKeyListenerHelper.detach();

        verify(componentMock).removeKeyListener(simpleKeyListenerHelper);
    }

    @Test
    public void testNotifyListener_Enter() {
        simpleKeyListenerHelper.notifyListener(simpleKeyListenerMock, 10, null);

        verify(simpleKeyListenerMock).enter(componentMock);
    }

    @Test
    public void testNotifyListener_Insert() {
        simpleKeyListenerHelper.notifyListener(simpleKeyListenerMock, 155, null);

        verify(simpleKeyListenerMock).insert(componentMock);
    }

    @Test
    public void testNotifyListener_Delete() {
        simpleKeyListenerHelper.notifyListener(simpleKeyListenerMock, 127, null);

        verify(simpleKeyListenerMock).delete(componentMock);
    }

    @Test
    public void testNotifyListener_Tab() {
        simpleKeyListenerHelper.notifyListener(simpleKeyListenerMock, 9, null);

        verify(simpleKeyListenerMock).tab(componentMock);
    }

    @Test
    public void testKeyPressed_Tab() {
        KeyEvent tabKeyEvent = mock(KeyEvent.class);
        when(tabKeyEvent.getKeyCode()).thenReturn(KeyEvent.VK_TAB);

        simpleKeyListenerHelper.keyPressed(tabKeyEvent);

        verify(simpleKeyListenerMock).tab(componentMock);
    }

    @Test
    public void testKeyPressed_Enter() {
        KeyEvent enterKeyEvent = mock(KeyEvent.class);
        when(enterKeyEvent.getKeyCode()).thenReturn(KeyEvent.VK_ENTER);

        simpleKeyListenerHelper.keyPressed(enterKeyEvent);

        verify(simpleKeyListenerMock).enter(componentMock);
    }

    @Test
    public void testKeyPressed_Delete() {
        KeyEvent deleteKeyEvent = mock(KeyEvent.class);
        when(deleteKeyEvent.getKeyCode()).thenReturn(KeyEvent.VK_DELETE);

        simpleKeyListenerHelper.keyPressed(deleteKeyEvent);

        verify(simpleKeyListenerMock).delete(componentMock);
    }

    @Test
    public void testKeyPressed_Insert() {
        KeyEvent insertKeyEvent = mock(KeyEvent.class);
        when(insertKeyEvent.getKeyCode()).thenReturn(KeyEvent.VK_INSERT);

        simpleKeyListenerHelper.keyPressed(insertKeyEvent);

        verify(simpleKeyListenerMock).insert(componentMock);
    }

    @Test
    public void testKeyPressed_UnrelatedKeyCode() {
        KeyEvent unrelatedKeyEvent = mock(KeyEvent.class);
        when(unrelatedKeyEvent.getKeyCode()).thenReturn(KeyEvent.VK_SPACE);

        simpleKeyListenerHelper.keyPressed(unrelatedKeyEvent);

        verifyNoInteractions(simpleKeyListenerMock);
    }

    @Test
    public void testKeyReleased() {
        KeyEvent arg0 = mock(KeyEvent.class);

        simpleKeyListenerHelper.keyReleased(arg0);

        // No interactions expected
    }

    @Test
    public void testKeyTyped() {
        KeyEvent event = mock(KeyEvent.class);

        simpleKeyListenerHelper.keyTyped(event);

        // No interactions expected
    }

}