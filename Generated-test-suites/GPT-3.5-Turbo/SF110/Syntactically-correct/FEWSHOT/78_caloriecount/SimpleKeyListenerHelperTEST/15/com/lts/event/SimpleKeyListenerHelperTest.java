package com.lts.event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;
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
    public void testNotifyListener() {
        Component component = new Component();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        SimpleKeyListener listener = new SimpleKeyListener() {
            public void enter(Component comp) {
                assertEquals(component, comp);
            }
            public void insert(Component comp) {
                assertEquals(component, comp);
            }
            public void delete(Component comp) {
                assertEquals(component, comp);
            }
            public void tab(Component comp) {
                assertEquals(component, comp);
            }
        };
        
        helper.notifyListener(listener, 10, null);
        helper.notifyListener(listener, 155, null);
        helper.notifyListener(listener, 127, null);
        helper.notifyListener(listener, 9, null);
    }
    
    @Test
    public void testKeyPressed() {
        Component component = new Component();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        KeyEvent event1 = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_9, '9');
        KeyEvent event2 = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, '\n');
        KeyEvent event3 = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_DELETE, '\u007F');
        KeyEvent event4 = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_UNDEFINED, '\u009B');
        
        helper.keyPressed(event1);
        helper.keyPressed(event2);
        helper.keyPressed(event3);
        helper.keyPressed(event4);
    }
    
    @Test
    public void testKeyReleased() {
        Component component = new Component();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, KeyEvent.VK_9, '9');
        helper.keyReleased(event);
    }
    
    @Test
    public void testKeyTyped() {
        Component component = new Component();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_TYPED, System.currentTimeMillis(), 0, KeyEvent.VK_9, '9');
        helper.keyTyped(event);
    }
}