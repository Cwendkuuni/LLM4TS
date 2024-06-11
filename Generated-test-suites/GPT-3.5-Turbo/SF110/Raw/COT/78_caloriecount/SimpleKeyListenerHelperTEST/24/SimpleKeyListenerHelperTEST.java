
package com.lts.event;

import static org.junit.Assert.*;
import org.junit.Test;
import java.awt.*;
import java.awt.event.*;

public class SimpleKeyListenerHelperTest {

    @Test
    public void testDetach() {
        Component component = new Component();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        
        helper.detach();
        
        assertNull(helper.getComponent());
    }
    
    @Test
    public void testNotifyListener_Enter() {
        Component component = new Component();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        SimpleKeyListener listener = new SimpleKeyListener(){
            @Override
            public void enter(Component c) {
                assertEquals(component, c);
            }
        };
        
        helper.notifyListener(listener, 10, null);
    }
    
    @Test
    public void testNotifyListener_Insert() {
        Component component = new Component();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        SimpleKeyListener listener = new SimpleKeyListener(){
            @Override
            public void insert(Component c) {
                assertEquals(component, c);
            }
        };
        
        helper.notifyListener(listener, 155, null);
    }
    
    @Test
    public void testNotifyListener_Delete() {
        Component component = new Component();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        SimpleKeyListener listener = new SimpleKeyListener(){
            @Override
            public void delete(Component c) {
                assertEquals(component, c);
            }
        };
        
        helper.notifyListener(listener, 127, null);
    }
    
    @Test
    public void testNotifyListener_Tab() {
        Component component = new Component();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        SimpleKeyListener listener = new SimpleKeyListener(){
            @Override
            public void tab(Component c) {
                assertEquals(component, c);
            }
        };
        
        helper.notifyListener(listener, 9, null);
    }
    
    @Test
    public void testKeyPressed_Tab() {
        Component component = new Component();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_TAB, KeyEvent.CHAR_UNDEFINED);
        
        helper.keyPressed(event);
        
        // Verify that the key code is fired
        // TODO: Add additional verification
    }
    
    @Test
    public void testKeyPressed_Enter() {
        Component component = new Component();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, KeyEvent.CHAR_UNDEFINED);
        
        helper.keyPressed(event);
        
        // Verify that the key code is fired
        // TODO: Add additional verification
    }
    
    @Test
    public void testKeyPressed_Delete() {
        Component component = new Component();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_DELETE, KeyEvent.CHAR_UNDEFINED);
        
        helper.keyPressed(event);
        
        // Verify that the key code is fired
        // TODO: Add additional verification
    }
    
    @Test
    public void testKeyPressed_Insert() {
        Component component = new Component();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_INSERT, KeyEvent.CHAR_UNDEFINED);
        
        helper.keyPressed(event);
        
        // Verify that the key code is fired
        // TODO: Add additional verification
    }
    
    @Test
    public void testKeyReleased() {
        // TODO: Implement test
    }
    
    @Test
    public void testKeyTyped() {
        // TODO: Implement test
    }
}

