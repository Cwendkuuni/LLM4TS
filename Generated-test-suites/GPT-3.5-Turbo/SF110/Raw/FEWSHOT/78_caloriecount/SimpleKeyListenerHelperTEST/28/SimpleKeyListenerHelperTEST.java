
package com.lts.event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.awt.Component;
import java.awt.event.KeyEvent;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class SimpleKeyListenerHelperTest {
    private SimpleKeyListenerHelper keyListenerHelper;
    private Component mockedComponent;
    private SimpleKeyListener mockedListener;
    private KeyEvent mockedKeyEvent;
    
    @Before
    public void setup() {
        mockedComponent = mock(Component.class);
        mockedListener = mock(SimpleKeyListener.class);
        mockedKeyEvent = mock(KeyEvent.class);
        keyListenerHelper = new SimpleKeyListenerHelper(mockedComponent);
    }
    
    @Test
    public void testDetach() {
        keyListenerHelper.detach();
        assertNull(keyListenerHelper.getComponent());
    }
    
    @Test
    public void testNotifyListenerEnter() {
        keyListenerHelper.notifyListener(mockedListener, 10, null);
        Mockito.verify(mockedListener).enter(mockedComponent);
    }
    
    @Test
    public void testNotifyListenerInsert() {
        keyListenerHelper.notifyListener(mockedListener, 155, null);
        Mockito.verify(mockedListener).insert(mockedComponent);
    }
    
    @Test
    public void testNotifyListenerDelete() {
        keyListenerHelper.notifyListener(mockedListener, 127, null);
        Mockito.verify(mockedListener).delete(mockedComponent);
    }
    
    @Test
    public void testNotifyListenerTab() {
        keyListenerHelper.notifyListener(mockedListener, 9, null);
        Mockito.verify(mockedListener).tab(mockedComponent);
    }
    
    @Test
    public void testKeyPressedTab() {
        Mockito.when(mockedKeyEvent.getKeyCode()).thenReturn(9);
        keyListenerHelper.keyPressed(mockedKeyEvent);
        Mockito.verify(mockedListener).fire(9);
    }
    
    @Test
    public void testKeyPressedEnter() {
        Mockito.when(mockedKeyEvent.getKeyCode()).thenReturn(10);
        keyListenerHelper.keyPressed(mockedKeyEvent);
        Mockito.verify(mockedListener).fire(10);
    }
    
    @Test
    public void testKeyPressedDelete() {
        Mockito.when(mockedKeyEvent.getKeyCode()).thenReturn(127);
        keyListenerHelper.keyPressed(mockedKeyEvent);
        Mockito.verify(mockedListener).fire(127);
    }
    
    @Test
    public void testKeyPressedInsert() {
        Mockito.when(mockedKeyEvent.getKeyCode()).thenReturn(155);
        keyListenerHelper.keyPressed(mockedKeyEvent);
        Mockito.verify(mockedListener).fire(155);
    }
    
    @Test
    public void testKeyPressedOtherKey() {
        Mockito.when(mockedKeyEvent.getKeyCode()).thenReturn(32);
        keyListenerHelper.keyPressed(mockedKeyEvent);
        Mockito.verifyZeroInteractions(mockedListener);
    }
    
    @Test
    public void testKeyReleased() {
        // Test that the method does nothing
        keyListenerHelper.keyReleased(mockedKeyEvent);
    }
    
    @Test
    public void testKeyTyped() {
        // Test that the method does nothing
        keyListenerHelper.keyTyped(mockedKeyEvent);
    }
}
