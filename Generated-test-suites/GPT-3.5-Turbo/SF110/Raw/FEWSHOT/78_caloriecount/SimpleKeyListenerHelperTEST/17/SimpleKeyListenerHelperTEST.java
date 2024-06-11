
package com.lts.event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.awt.Component;
import java.awt.event.KeyEvent;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SimpleKeyListenerHelperTest {
    private SimpleKeyListenerHelper helper;
    private SimpleKeyListener listener;
    private Component component;

    @Before
    public void setUp() {
        component = mock(Component.class);
        helper = new SimpleKeyListenerHelper(component);
        listener = mock(SimpleKeyListener.class);
    }

    @After
    public void tearDown() {
        helper.detach();
    }

    @Test
    public void testDetach() {
        assertNotNull(helper.getComponent());
        helper.detach();
        assertNull(helper.getComponent());
    }

    @Test
    public void testNotifyListener_Enter() {
        helper.notifyListener(listener, 10, null);
        verify(listener).enter(component);
    }

    @Test
    public void testNotifyListener_Insert() {
        helper.notifyListener(listener, 155, null);
        verify(listener).insert(component);
    }

    @Test
    public void testNotifyListener_Delete() {
        helper.notifyListener(listener, 127, null);
        verify(listener).delete(component);
    }

    @Test
    public void testNotifyListener_Tab() {
        helper.notifyListener(listener, 9, null);
        verify(listener).tab(component);
    }

    @Test
    public void testKeyPressed_Tab() {
        KeyEvent event = mock(KeyEvent.class);
        when(event.getKeyCode()).thenReturn(KeyEvent.VK_TAB);
        helper.keyPressed(event);
        verify(helper).fire(KeyEvent.VK_TAB);
    }

    @Test
    public void testKeyPressed_Enter() {
        KeyEvent event = mock(KeyEvent.class);
        when(event.getKeyCode()).thenReturn(KeyEvent.VK_ENTER);
        helper.keyPressed(event);
        verify(helper).fire(KeyEvent.VK_ENTER);
    }

    @Test
    public void testKeyPressed_Delete() {
        KeyEvent event = mock(KeyEvent.class);
        when(event.getKeyCode()).thenReturn(KeyEvent.VK_DELETE);
        helper.keyPressed(event);
        verify(helper).fire(KeyEvent.VK_DELETE);
    }

    @Test
    public void testKeyPressed_Insert() {
        KeyEvent event = mock(KeyEvent.class);
        when(event.getKeyCode()).thenReturn(KeyEvent.VK_INSERT);
        helper.keyPressed(event);
        verify(helper).fire(KeyEvent.VK_INSERT);
    }

    @Test
    public void testKeyReleased() {
        KeyEvent event = mock(KeyEvent.class);
        helper.keyReleased(event);
        // No verification as the method does not have any behavior
    }

    @Test
    public void testKeyTyped() {
        KeyEvent event = mock(KeyEvent.class);
        helper.keyTyped(event);
        // No verification as the method does not have any behavior
    }
}
