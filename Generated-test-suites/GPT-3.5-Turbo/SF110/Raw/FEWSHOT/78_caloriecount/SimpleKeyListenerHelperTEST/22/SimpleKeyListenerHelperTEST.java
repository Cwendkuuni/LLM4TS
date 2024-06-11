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

        assertNull(helper.component);
    }

    @Test
    public void testNotifyListener() {
        Component component = new Component();
        SimpleKeyListener listener = new SimpleKeyListener() {
            public void enter(Component component) {}
            public void insert(Component component) {}
            public void delete(Component component) {}
            public void tab(Component component) {}
        };

        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        helper.notifyListener(listener, 10, null);
        helper.notifyListener(listener, 155, null);
        helper.notifyListener(listener, 127, null);
        helper.notifyListener(listener, 9, null);
    }

    @Test
    public void testKeyPressed() {
        Component component = new Component();
        
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        KeyEvent event1 = new KeyEvent(component, KeyEvent.KEY_PRESSED, 0, 0, KeyEvent.VK_TAB, KeyEvent.CHAR_UNDEFINED);
        KeyEvent event2 = new KeyEvent(component, KeyEvent.KEY_PRESSED, 0, 0, KeyEvent.VK_ENTER, KeyEvent.CHAR_UNDEFINED);
        KeyEvent event3 = new KeyEvent(component, KeyEvent.KEY_PRESSED, 0, 0, KeyEvent.VK_DELETE, KeyEvent.CHAR_UNDEFINED);
        KeyEvent event4 = new KeyEvent(component, KeyEvent.KEY_PRESSED, 0, 0, 155, KeyEvent.CHAR_UNDEFINED);

        helper.keyPressed(event1);
        helper.keyPressed(event2);
        helper.keyPressed(event3);
        helper.keyPressed(event4);
    }

    @Test
    public void testKeyReleased() {
        Component component = new Component();
        
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_RELEASED, 0, 0, KeyEvent.VK_A, KeyEvent.CHAR_UNDEFINED);

        helper.keyReleased(event);
    }

    @Test
    public void testKeyTyped() {
        Component component = new Component();
        
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_TYPED, 0, 0, KeyEvent.VK_A, KeyEvent.CHAR_UNDEFINED);

        helper.keyTyped(event);
    }
    
}