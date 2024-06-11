package com.lts.event;

import org.junit.Test;
import static org.junit.Assert.*;

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
        SimpleKeyListener listener = new SimpleKeyListener();
        
        helper.notifyListener(listener, 10, null);
        helper.notifyListener(listener, 155, null);
        helper.notifyListener(listener, 127, null);
        helper.notifyListener(listener, 9, null);
    }

    @Test
    public void testKeyPressed() {
        Component component = new Component();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        KeyEvent event1 = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_TAB, KeyEvent.CHAR_UNDEFINED);
        KeyEvent event2 = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, KeyEvent.CHAR_UNDEFINED);
        KeyEvent event3 = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_DELETE, KeyEvent.CHAR_UNDEFINED);
        KeyEvent event4 = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, 155, KeyEvent.CHAR_UNDEFINED);
        
        helper.keyPressed(event1);
        helper.keyPressed(event2);
        helper.keyPressed(event3);
        helper.keyPressed(event4);
    }

    @Test
    public void testKeyReleased() {
        // Test keyReleased method, no specific test cases for now
    }

    @Test
    public void testKeyTyped() {
        // Test keyTyped method, no specific test cases for now
    }
}