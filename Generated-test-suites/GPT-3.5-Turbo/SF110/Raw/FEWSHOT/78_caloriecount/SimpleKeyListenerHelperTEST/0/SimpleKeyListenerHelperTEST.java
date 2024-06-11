package com.lts.event;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.awt.Component;
import java.awt.event.KeyEvent;

public class SimpleKeyListenerHelperTest {
    
    @Mock
    private Component component;
    
    private SimpleKeyListenerHelper simpleKeyListenerHelper;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
    }
    
    @Test
    public void testDetach() {
        simpleKeyListenerHelper.detach();
        assertNull(simpleKeyListenerHelper.component);
    }
    
    @Test
    public void testNotifyListener() {
        SimpleKeyListener listener = new SimpleKeyListener() {
            @Override
            public void enter(Component component) {
                assertEquals(simpleKeyListenerHelper.component, component);
            }
    
            @Override
            public void insert(Component component) {
                assertEquals(simpleKeyListenerHelper.component, component);
            }
    
            @Override
            public void delete(Component component) {
                assertEquals(simpleKeyListenerHelper.component, component);
            }
    
            @Override
            public void tab(Component component) {
                assertEquals(simpleKeyListenerHelper.component, component);
            }
        };
        
        simpleKeyListenerHelper.notifyListener(listener, 10, null);
        simpleKeyListenerHelper.notifyListener(listener, 155, null);
        simpleKeyListenerHelper.notifyListener(listener, 127, null);
        simpleKeyListenerHelper.notifyListener(listener, 9, null);
    }
    
    @Test
    public void testKeyPressed() {
        KeyEvent keyEvent = new KeyEvent(component, 10, 0, 0, KeyEvent.VK_ENTER);
        simpleKeyListenerHelper.keyPressed(keyEvent);
        // assert something
        
        keyEvent = new KeyEvent(component, 10, 0, 0, KeyEvent.VK_TAB);
        simpleKeyListenerHelper.keyPressed(keyEvent);
        // assert something
        
        keyEvent = new KeyEvent(component, 10, 0, 0, KeyEvent.VK_DELETE);
        simpleKeyListenerHelper.keyPressed(keyEvent);
        // assert something
        
        keyEvent = new KeyEvent(component, 10, 0, 0, KeyEvent.VK_UNDEFINED);
        simpleKeyListenerHelper.keyPressed(keyEvent);
        // assert something
    }
    
    @Test
    public void testKeyReleased() {
        KeyEvent keyEvent = new KeyEvent(component, 10, 0, 0, KeyEvent.VK_ENTER);
        simpleKeyListenerHelper.keyReleased(keyEvent);
        // assert something
    }
    
    @Test
    public void testKeyTyped() {
        KeyEvent keyEvent = new KeyEvent(component, 10, 0, 0, KeyEvent.VK_ENTER);
        simpleKeyListenerHelper.keyTyped(keyEvent);
        // assert something
    }
}