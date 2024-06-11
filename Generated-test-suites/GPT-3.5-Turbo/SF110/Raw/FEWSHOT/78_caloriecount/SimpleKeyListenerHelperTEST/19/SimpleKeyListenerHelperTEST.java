package com.lts.event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.awt.*;
import java.awt.event.*;

import org.junit.Before;
import org.junit.Test;

public class SimpleKeyListenerHelperTest {
    
    private SimpleKeyListenerHelper helper;
    private Component component;
    private SimpleKeyListener listener;
    
    @Before
    public void setUp() {
        component = mock(Component.class);
        listener = mock(SimpleKeyListener.class);
        helper = new SimpleKeyListenerHelper(component);
    }
    
    @Test
    public void detach_ShouldRemoveKeyListener() {
        helper.detach();
        
        verify(component, times(1)).removeKeyListener(helper);
        assertNull(helper.getComponent());
    }
    
    @Test
    public void notifyListener_ShouldCallEnterWhenTypeIs10() {
        helper.notifyListener(listener, 10, null);
        
        verify(listener, times(1)).enter(helper.getComponent());
    }
    
    @Test
    public void notifyListener_ShouldCallInsertWhenTypeIs155() {
        helper.notifyListener(listener, 155, null);
        
        verify(listener, times(1)).insert(helper.getComponent());
    }
    
    @Test
    public void notifyListener_ShouldCallDeleteWhenTypeIs127() {
        helper.notifyListener(listener, 127, null);
        
        verify(listener, times(1)).delete(helper.getComponent());
    }
    
    @Test
    public void notifyListener_ShouldCallTabWhenTypeIs9() {
        helper.notifyListener(listener, 9, null);
        
        verify(listener, times(1)).tab(helper.getComponent());
    }
    
    @Test
    public void keyPressed_ShouldFireEventForTabKey() {
        KeyEvent event = mock(KeyEvent.class);
        when(event.getKeyCode()).thenReturn(9);
        
        helper.keyPressed(event);
        
        verify(helper, times(1)).fire(KeyEvent.VK_TAB);
    }
    
    @Test
    public void keyPressed_ShouldFireEventForEnterKey() {
        KeyEvent event = mock(KeyEvent.class);
        when(event.getKeyCode()).thenReturn(10);
        
        helper.keyPressed(event);
        
        verify(helper, times(1)).fire(KeyEvent.VK_ENTER);
    }
    
    @Test
    public void keyPressed_ShouldFireEventForDeleteKey() {
        KeyEvent event = mock(KeyEvent.class);
        when(event.getKeyCode()).thenReturn(127);
        
        helper.keyPressed(event);
        
        verify(helper, times(1)).fire(KeyEvent.VK_DELETE);
    }
    
    @Test
    public void keyPressed_ShouldFireEventForInsertKey() {
        KeyEvent event = mock(KeyEvent.class);
        when(event.getKeyCode()).thenReturn(155);
        
        helper.keyPressed(event);
        
        verify(helper, times(1)).fire(KeyEvent.VK_INSERT);
    }
    
    @Test
    public void keyReleased_ShouldDoNothing() {
        KeyEvent event = mock(KeyEvent.class);
        
        helper.keyReleased(event);
        
        // No verification as there should be no interactions
    }
    
    @Test
    public void keyTyped_ShouldDoNothing() {
        KeyEvent event = mock(KeyEvent.class);
        
        helper.keyTyped(event);
        
        // No verification as there should be no interactions
    }
}