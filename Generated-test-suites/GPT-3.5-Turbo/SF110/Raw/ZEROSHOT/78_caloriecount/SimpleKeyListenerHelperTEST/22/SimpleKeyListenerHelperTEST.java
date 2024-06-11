package com.lts.event;

import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.event.KeyEvent;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class SimpleKeyListenerHelperTest {

    private Component component;
    private SimpleKeyListenerHelper helper;
    
    @Before
    public void setUp() {
        component = mock(Component.class);
        helper = new SimpleKeyListenerHelper(component);
    }
    
    @Test
    public void testDetach() {
        helper.detach();
        
        verify(component, times(1)).removeKeyListener(helper);
        assertEquals(null, helper.getComponent());
    }
    
    @Test
    public void testNotifyListener_Enter() {
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        helper.notifyListener(listener, 10, null);
        
        verify(listener, times(1)).enter(component);
        verifyNoMoreInteractions(listener);
    }
    
    @Test
    public void testNotifyListener_Insert() {
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        helper.notifyListener(listener, 155, null);
        
        verify(listener, times(1)).insert(component);
        verifyNoMoreInteractions(listener);
    }
    
    @Test
    public void testNotifyListener_Delete() {
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        helper.notifyListener(listener, 127, null);
        
        verify(listener, times(1)).delete(component);
        verifyNoMoreInteractions(listener);
    }
    
    @Test
    public void testNotifyListener_Tab() {
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        helper.notifyListener(listener, 9, null);
        
        verify(listener, times(1)).tab(component);
        verifyNoMoreInteractions(listener);
    }
    
    @Test
    public void testKeyPressed_Tab() {
        KeyEvent event = mock(KeyEvent.class);
        when(event.getKeyCode()).thenReturn(KeyEvent.VK_TAB);
        
        helper.keyPressed(event);
        
        verify(component, times(1)).getKeyCode();
        verifyNoMoreInteractions(component);
        
        // Verify the fired event
    }
    
    @Test
    public void testKeyPressed_Enter() {
        KeyEvent event = mock(KeyEvent.class);
        when(event.getKeyCode()).thenReturn(KeyEvent.VK_ENTER);
        
        helper.keyPressed(event);
        
        verify(component, times(1)).getKeyCode();
        verifyNoMoreInteractions(component);
        
        // Verify the fired event
    }
    
    @Test
    public void testKeyPressed_Delete() {
        KeyEvent event = mock(KeyEvent.class);
        when(event.getKeyCode()).thenReturn(KeyEvent.VK_DELETE);
        
        helper.keyPressed(event);
        
        verify(component, times(1)).getKeyCode();
        verifyNoMoreInteractions(component);
        
        // Verify the fired event
    }
    
    @Test
    public void testKeyPressed_Insert() {
        KeyEvent event = mock(KeyEvent.class);
        when(event.getKeyCode()).thenReturn(KeyEvent.VK_INSERT);
        
        helper.keyPressed(event);
        
        verify(component, times(1)).getKeyCode();
        verifyNoMoreInteractions(component);
        
        // Verify the fired event
    }
    
    @Test
    public void testKeyReleased() {
        // No code in method, nothing to test
    }
    
    @Test
    public void testKeyTyped() {
        // No code in method, nothing to test
    }
}