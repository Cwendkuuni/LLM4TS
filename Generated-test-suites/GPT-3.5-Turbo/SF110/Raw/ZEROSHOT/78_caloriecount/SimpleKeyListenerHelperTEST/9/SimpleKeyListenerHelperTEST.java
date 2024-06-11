package com.lts.event;

import org.junit.*;
import static org.junit.Assert.*;

import java.awt.*;
import java.awt.event.*;

public class SimpleKeyListenerHelperTest {
    
    @Test
    public void testDetach() {
        Component component = new Component();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        
        helper.detach();
        
        assertNull(helper.component);
    }
    
    @Test
    public void testNotifyListenerEnter() {
        Component component = new Component();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        SimpleKeyListener listener = new SimpleKeyListener() {
            @Override
            public void enter(Component component) {
                assertEquals(component, helper.component);
            }
            @Override
            public void insert(Component component) {}
            @Override
            public void delete(Component component) {}
            @Override
            public void tab(Component component) {}
        };
        
        helper.notifyListener(listener, 10, null);
    }
    
    @Test
    public void testNotifyListenerInsert() {
        Component component = new Component();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        SimpleKeyListener listener = new SimpleKeyListener() {
            @Override
            public void enter(Component component) {}
            @Override
            public void insert(Component component) {
                assertEquals(component, helper.component);
            }
            @Override
            public void delete(Component component) {}
            @Override
            public void tab(Component component) {}
        };
        
        helper.notifyListener(listener, 155, null);
    }
    
    @Test
    public void testNotifyListenerDelete() {
        Component component = new Component();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        SimpleKeyListener listener = new SimpleKeyListener() {
            @Override
            public void enter(Component component) {}
            @Override
            public void insert(Component component) {}
            @Override
            public void delete(Component component) {
                assertEquals(component, helper.component);
            }
            @Override
            public void tab(Component component) {}
        };
        
        helper.notifyListener(listener, 127, null);
    }
    
    @Test
    public void testNotifyListenerTab() {
        Component component = new Component();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        SimpleKeyListener listener = new SimpleKeyListener() {
            @Override
            public void enter(Component component) {}
            @Override
            public void insert(Component component) {}
            @Override
            public void delete(Component component) {}
            @Override
            public void tab(Component component) {
                assertEquals(component, helper.component);
            }
        };
        
        helper.notifyListener(listener, 9, null);
    }
    
    @Test
    public void testKeyPressedTab() {
        KeyEvent event = new KeyEvent(new Component(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_TAB, KeyEvent.CHAR_UNDEFINED);
        
        Component component = new Component();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        SimpleKeyListener listener = new SimpleKeyListener() {
            @Override
            public void enter(Component component) {}
            @Override
            public void insert(Component component) {}
            @Override
            public void delete(Component component) {}
            @Override
            public void tab(Component component) {
                assertEquals(component, helper.component);
            }
        };
        
        helper.notifyListener(listener, 9, null);
        helper.keyPressed(event);
    }
    
    @Test
    public void testKeyPressedEnter() {
        KeyEvent event = new KeyEvent(new Component(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, KeyEvent.CHAR_UNDEFINED);
        
        Component component = new Component();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        SimpleKeyListener listener = new SimpleKeyListener() {
            @Override
            public void enter(Component component) {
                assertEquals(component, helper.component);
            }
            @Override
            public void insert(Component component) {}
            @Override
            public void delete(Component component) {}
            @Override
            public void tab(Component component) {}
        };
        
        helper.notifyListener(listener, 10, null);
        helper.keyPressed(event);
    }
    
    @Test
    public void testKeyPressedDelete() {
        KeyEvent event = new KeyEvent(new Component(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_DELETE, KeyEvent.CHAR_UNDEFINED);
        
        Component component = new Component();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        SimpleKeyListener listener = new SimpleKeyListener() {
            @Override
            public void enter(Component component) {}
            @Override
            public void insert(Component component) {}
            @Override
            public void delete(Component component) {
                assertEquals(component, helper.component);
            }
            @Override
            public void tab(Component component) {}
        };
        
        helper.notifyListener(listener, 127, null);
        helper.keyPressed(event);
    }
    
    @Test
    public void testKeyPressedInsert() {
        KeyEvent event = new KeyEvent(new Component(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_INSERT, KeyEvent.CHAR_UNDEFINED);
        
        Component component = new Component();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        SimpleKeyListener listener = new SimpleKeyListener() {
            @Override
            public void enter(Component component) {}
            @Override
            public void insert(Component component) {
                assertEquals(component, helper.component);
            }
            @Override
            public void delete(Component component) {}
            @Override
            public void tab(Component component) {}
        };
        
        helper.notifyListener(listener, 155, null);
        helper.keyPressed(event);
    }
    
    @Test
    public void testKeyReleased() {
        KeyEvent event = new KeyEvent(new Component(), KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, KeyEvent.VK_BACK_SPACE, KeyEvent.CHAR_UNDEFINED);
        
        Component component = new Component();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        
        helper.keyReleased(event);
    }
    
    @Test
    public void testKeyTyped() {
        KeyEvent event = new KeyEvent(new Component(), KeyEvent.KEY_TYPED, System.currentTimeMillis(), 0, KeyEvent.VK_UNDEFINED, 'a');
        
        Component component = new Component();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        
        helper.keyTyped(event);
    }
}