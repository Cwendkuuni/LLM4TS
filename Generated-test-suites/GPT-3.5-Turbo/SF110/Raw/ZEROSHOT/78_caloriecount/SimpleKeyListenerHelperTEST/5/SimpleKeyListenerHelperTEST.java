package com.lts.event;

import org.junit.Test;

import java.awt.*;
import java.awt.event.KeyEvent;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SimpleKeyListenerHelperTest {

    @Test
    public void testDetach() {
        Component component = mock(Component.class);
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        helper.detach();
        verify(component).removeKeyListener(helper);
        assertNull(helper.component);
    }

    @Test
    public void testNotifyListenerEnter() {
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        Component component = mock(Component.class);
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);

        helper.notifyListener(listener, 10, null);

        verify(listener).enter(component);
    }

    @Test
    public void testNotifyListenerInsert() {
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        Component component = mock(Component.class);
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);

        helper.notifyListener(listener, 155, null);

        verify(listener).insert(component);
    }

    @Test
    public void testNotifyListenerDelete() {
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        Component component = mock(Component.class);
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);

        helper.notifyListener(listener, 127, null);

        verify(listener).delete(component);
    }

    @Test
    public void testNotifyListenerTab() {
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        Component component = mock(Component.class);
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);

        helper.notifyListener(listener, 9, null);

        verify(listener).tab(component);
    }

    @Test
    public void testKeyPressedEnter() {
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(mock(Component.class));
        KeyEvent event = mock(KeyEvent.class);
        when(event.getKeyCode()).thenReturn(KeyEvent.VK_ENTER);

        helper.keyPressed(event);

        verify(helper).fire(KeyEvent.VK_ENTER);
    }

    @Test
    public void testKeyPressedTab() {
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(mock(Component.class));
        KeyEvent event = mock(KeyEvent.class);
        when(event.getKeyCode()).thenReturn(KeyEvent.VK_TAB);

        helper.keyPressed(event);

        verify(helper).fire(KeyEvent.VK_TAB);
    }

    @Test
    public void testKeyPressedDelete() {
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(mock(Component.class));
        KeyEvent event = mock(KeyEvent.class);
        when(event.getKeyCode()).thenReturn(KeyEvent.VK_DELETE);

        helper.keyPressed(event);

        verify(helper).fire(KeyEvent.VK_DELETE);
    }

    @Test
    public void testKeyPressedInsert() {
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(mock(Component.class));
        KeyEvent event = mock(KeyEvent.class);
        when(event.getKeyCode()).thenReturn(KeyEvent.VK_INSERT);

        helper.keyPressed(event);

        verify(helper).fire(KeyEvent.VK_INSERT);
    }

    @Test
    public void testKeyReleased() {
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(mock(Component.class));
        KeyEvent event = mock(KeyEvent.class);

        helper.keyReleased(event);
        // No assertions to be made since this method does nothing
    }

    @Test
    public void testKeyTyped() {
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(mock(Component.class));
        KeyEvent event = mock(KeyEvent.class);
        
        helper.keyTyped(event);
        // No assertions to be made since this method does nothing
    }
}