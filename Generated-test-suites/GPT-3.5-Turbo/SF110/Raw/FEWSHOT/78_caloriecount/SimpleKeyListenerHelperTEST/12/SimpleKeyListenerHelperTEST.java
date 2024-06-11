package com.lts.event;

import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;
import java.awt.*;
import java.awt.event.*;

public class SimpleKeyListenerHelperTest {
    
    @Test
    public void testDetach() {
        Component component = Mockito.mock(Component.class);
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        
        helper.detach();
        
        assertNull(helper.component);
        Mockito.verify(component, Mockito.times(1)).removeKeyListener(helper);
    }
    
    @Test
    public void testNotifyListenerEnter() {
        Component component = Mockito.mock(Component.class);
        SimpleKeyListener listener = Mockito.mock(SimpleKeyListener.class);
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        helper.notifyListener(listener, 10, null);
        
        Mockito.verify(listener, Mockito.times(1)).enter(component);
    }
    
    @Test
    public void testNotifyListenerInsert() {
        Component component = Mockito.mock(Component.class);
        SimpleKeyListener listener = Mockito.mock(SimpleKeyListener.class);
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        helper.notifyListener(listener, 155, null);
        
        Mockito.verify(listener, Mockito.times(1)).insert(component);
    }
    
    @Test
    public void testNotifyListenerDelete() {
        Component component = Mockito.mock(Component.class);
        SimpleKeyListener listener = Mockito.mock(SimpleKeyListener.class);
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        helper.notifyListener(listener, 127, null);
        
        Mockito.verify(listener, Mockito.times(1)).delete(component);
    }
    
    @Test
    public void testNotifyListenerTab() {
        Component component = Mockito.mock(Component.class);
        SimpleKeyListener listener = Mockito.mock(SimpleKeyListener.class);
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        helper.notifyListener(listener, 9, null);
        
        Mockito.verify(listener, Mockito.times(1)).tab(component);
    }
    
    @Test
    public void testKeyPressed() {
        KeyEvent event = Mockito.mock(KeyEvent.class);
        Component component = Mockito.mock(Component.class);
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        
        Mockito.when(event.getKeyCode()).thenReturn(10);
        helper.keyPressed(event);
        Mockito.verify(helper, Mockito.times(1)).fire(10);
        
        Mockito.reset(event);
        Mockito.when(event.getKeyCode()).thenReturn(9);
        helper.keyPressed(event);
        Mockito.verify(helper, Mockito.times(1)).fire(9);
        
        Mockito.reset(event);
        Mockito.when(event.getKeyCode()).thenReturn(127);
        helper.keyPressed(event);
        Mockito.verify(helper, Mockito.times(1)).fire(127);
        
        Mockito.reset(event);
        Mockito.when(event.getKeyCode()).thenReturn(155);
        helper.keyPressed(event);
        Mockito.verify(helper, Mockito.times(1)).fire(155);
    }
    
    @Test
    public void testKeyReleased() {
        KeyEvent event = Mockito.mock(KeyEvent.class);
        SimpleKeyListenerHelper helper = Mockito.mock(SimpleKeyListenerHelper.class, Mockito.CALLS_REAL_METHODS);
        
        helper.keyReleased(event);
        
        Mockito.verify(helper, Mockito.times(0)).fire(Mockito.anyInt());
    }
    
    @Test
    public void testKeyTyped() {
        KeyEvent event = Mockito.mock(KeyEvent.class);
        SimpleKeyListenerHelper helper = Mockito.mock(SimpleKeyListenerHelper.class, Mockito.CALLS_REAL_METHODS);
        
        helper.keyTyped(event);
        
        Mockito.verify(helper, Mockito.times(0)).fire(Mockito.anyInt());
    }
}