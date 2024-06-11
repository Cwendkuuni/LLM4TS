package com.lts.event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class SimpleKeyListenerHelperTest {
    @Test
    public void testDetach() {
        // not null case
        Component component = new Component();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        helper.detach();
        assertNull(helper.getComponent());
        
        // null case
        SimpleKeyListenerHelper helper2 = new SimpleKeyListenerHelper(null);
        helper2.detach();
        assertNull(helper2.getComponent());
    }
    
    @Test
    public void testNotifyListener() {
        Component component = new Component();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        SimpleKeyListener listener = new SimpleKeyListener();
        
        // case 10
        helper.notifyListener(listener, 10, null);
        // assert some condition
        
        
        // case 155
        helper.notifyListener(listener, 155, null);
        // assert some condition
        
        
        // case 127
        helper.notifyListener(listener, 127, null);
        // assert some condition
        
        
        // case 9
        helper.notifyListener(listener, 9, null);
        // assert some condition
        
        assertNull(helper.getComponent());
    }
    
    @Test
    public void testKeyPressed() {
        Component component = new Component();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        
        KeyEvent event1 = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_9, KeyEvent.CHAR_UNDEFINED);
        KeyEvent event2 = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, KeyEvent.CHAR_UNDEFINED);
        KeyEvent event3 = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_DELETE, KeyEvent.CHAR_UNDEFINED);
        KeyEvent event4 = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_UNDEFINED, KeyEvent.CHAR_UNDEFINED);
        
        // case VK_9
        helper.keyPressed(event1);
        // assert some condition
        
        
        // case VK_ENTER
        helper.keyPressed(event2);
        // assert some condition
        
        
        // case VK_DELETE
        helper.keyPressed(event3);
        // assert some condition
        
        
        // case VK_UNDEFINED
        helper.keyPressed(event4);
        // assert some condition
        
        assertNull(helper.getComponent());
    }
    
    @Test
    public void testKeyReleased() {
        Component component = new Component();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, KeyEvent.VK_UNDEFINED, KeyEvent.CHAR_UNDEFINED);
        
        helper.keyReleased(event);
        // assert some condition
        
        assertNull(helper.getComponent());
    }
    
    @Test
    public void testKeyTyped() {
        Component component = new Component();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_TYPED, System.currentTimeMillis(), 0, KeyEvent.VK_UNDEFINED, 'a');
        
        helper.keyTyped(event);
        // assert some condition
        
        assertNull(helper.getComponent());
    }
}