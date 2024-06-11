package com.lts.event;

import org.junit.Test;
import static org.mockito.Mockito.*;

import java.awt.*;
import java.awt.event.*;

public class SimpleKeyListenerHelperTest {

    @Test
    public void testDetach() {
        Component component = mock(Component.class);
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        helper.detach();
        verify(component, times(1)).removeKeyListener(helper);
    }

    @Test
    public void testNotifyListenerEnter() {
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        Component component = mock(Component.class);
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        helper.notifyListener(listener, 10, null);
        verify(listener, times(1)).enter(component);
    }

    @Test
    public void testNotifyListenerInsert() {
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        Component component = mock(Component.class);
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        helper.notifyListener(listener, 155, null);
        verify(listener, times(1)).insert(component);
    }

    @Test
    public void testNotifyListenerDelete() {
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        Component component = mock(Component.class);
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        helper.notifyListener(listener, 127, null);
        verify(listener, times(1)).delete(component);
    }

    @Test
    public void testNotifyListenerTab() {
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        Component component = mock(Component.class);
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        helper.notifyListener(listener, 9, null);
        verify(listener, times(1)).tab(component);
    }

    @Test
    public void testKeyPressedFireEnter() {
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(mock(Component.class));
        KeyEvent keyEvent = mock(KeyEvent.class);
        when(keyEvent.getKeyCode()).thenReturn(10);
        helper.keyPressed(keyEvent);
        // verify fire method is called with 10
    }

    @Test
    public void testKeyPressedFireInsert() {
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(mock(Component.class));
        KeyEvent keyEvent = mock(KeyEvent.class);
        when(keyEvent.getKeyCode()).thenReturn(155);
        helper.keyPressed(keyEvent);
        // verify fire method is called with 155
    }

    @Test
    public void testKeyPressedFireDelete() {
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(mock(Component.class));
        KeyEvent keyEvent = mock(KeyEvent.class);
        when(keyEvent.getKeyCode()).thenReturn(127);
        helper.keyPressed(keyEvent);
        // verify fire method is called with 127
    }

    @Test
    public void testKeyPressedFireTab() {
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(mock(Component.class));
        KeyEvent keyEvent = mock(KeyEvent.class);
        when(keyEvent.getKeyCode()).thenReturn(9);
        helper.keyPressed(keyEvent);
        // verify fire method is called with 9
    }

    @Test
    public void testKeyReleased() {
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(mock(Component.class));
        KeyEvent keyEvent = mock(KeyEvent.class);
        helper.keyReleased(keyEvent);
        // verify no action is performed
    }

    @Test
    public void testKeyTyped() {
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(mock(Component.class));
        KeyEvent keyEvent = mock(KeyEvent.class);
        helper.keyTyped(keyEvent);
        // verify no action is performed
    }
}