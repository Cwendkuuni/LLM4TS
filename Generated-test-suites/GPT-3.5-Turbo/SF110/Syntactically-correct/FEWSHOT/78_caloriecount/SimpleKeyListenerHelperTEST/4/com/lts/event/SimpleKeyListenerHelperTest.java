package com.lts.event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.awt.*;
import java.awt.event.*;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class SimpleKeyListenerHelperTest {
    
    @Test
    public void testConstructor() {
        Component component = mock(Component.class);
        
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        
        verify(component).addKeyListener(helper);
        assertNotNull(helper.getComponent());
    }
    
    @Test
    public void testConstructor_NullComponent() {
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(null);
        
        assertNull(helper.getComponent());
    }
    
    @Test
    public void testDetach() {
        Component component = mock(Component.class);
        
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        helper.detach();
        
        verify(component).removeKeyListener(helper);
        assertNull(helper.getComponent());
    }
    
    @Test
    public void testDetach_NullComponent() {
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(null);
        helper.detach();
        
        assertNull(helper.getComponent());
    }
    
    @Test
    public void testNotifyListener_Enter() {
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(mock(Component.class));
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        KeyEvent event = mock(KeyEvent.class);
        when(event.getKeyCode()).thenReturn(KeyEvent.VK_ENTER);
        
        helper.notifyListener(listener, 10, null);
        
        verify(listener).enter(helper.getComponent());
    }
    
    @Test
    public void testNotifyListener_Insert() {
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(mock(Component.class));
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        KeyEvent event = mock(KeyEvent.class);
        when(event.getKeyCode()).thenReturn(155);
        
        helper.notifyListener(listener, 155, null);
        
        verify(listener).insert(helper.getComponent());
    }
    
    @Test
    public void testNotifyListener_Delete() {
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(mock(Component.class));
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        KeyEvent event = mock(KeyEvent.class);
        when(event.getKeyCode()).thenReturn(127);
        
        helper.notifyListener(listener, 127, null);
        
        verify(listener).delete(helper.getComponent());
    }
    
    @Test
    public void testNotifyListener_Tab() {
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(mock(Component.class));
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        KeyEvent event = mock(KeyEvent.class);
        when(event.getKeyCode()).thenReturn(KeyEvent.VK_TAB);
        
        helper.notifyListener(listener, 9, null);
        
        verify(listener).tab(helper.getComponent());
    }
    
    @Test
    public void testKeyPressed_Enter() {
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(mock(Component.class));
        KeyEvent event = mock(KeyEvent.class);
        when(event.getKeyCode()).thenReturn(KeyEvent.VK_ENTER);
        
        helper.keyPressed(event);
        
        ArgumentCaptor<Integer> captor = ArgumentCaptor.forClass(Integer.class);
        verify(helper).fire(captor.capture());
        assertEquals(KeyEvent.VK_ENTER, captor.getValue().intValue());
    }
    
    @Test
    public void testKeyPressed_Insert() {
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(mock(Component.class));
        KeyEvent event = mock(KeyEvent.class);
        when(event.getKeyCode()).thenReturn(155);
        
        helper.keyPressed(event);
        
        ArgumentCaptor<Integer> captor = ArgumentCaptor.forClass(Integer.class);
        verify(helper).fire(captor.capture());
        assertEquals(155, captor.getValue().intValue());
    }
    
    @Test
    public void testKeyPressed_Delete() {
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(mock(Component.class));
        KeyEvent event = mock(KeyEvent.class);
        when(event.getKeyCode()).thenReturn(127);
        
        helper.keyPressed(event);
        
        ArgumentCaptor<Integer> captor = ArgumentCaptor.forClass(Integer.class);
        verify(helper).fire(captor.capture());
        assertEquals(127, captor.getValue().intValue());
    }
    
    @Test
    public void testKeyPressed_Tab() {
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(mock(Component.class));
        KeyEvent event = mock(KeyEvent.class);
        when(event.getKeyCode()).thenReturn(KeyEvent.VK_TAB);
        
        helper.keyPressed(event);
        
        ArgumentCaptor<Integer> captor = ArgumentCaptor.forClass(Integer.class);
        verify(helper).fire(captor.capture());
        assertEquals(KeyEvent.VK_TAB, captor.getValue().intValue());
    }
    
    @Test
    public void testKeyReleased() {
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(mock(Component.class));
        KeyEvent event = mock(KeyEvent.class);
        
        helper.keyReleased(event);
        
        // Nothing to test
    }
    
    @Test
    public void testKeyTyped() {
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(mock(Component.class));
        KeyEvent event = mock(KeyEvent.class);
        
        helper.keyTyped(event);
        
        // Nothing to test
    }
}