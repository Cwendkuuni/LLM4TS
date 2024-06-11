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
    public void testNotifyListenerEnter() {
        Component component = new Component();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        SimpleKeyListener listener = new SimpleKeyListener() {
            @Override
            public void enter(Component c) {
                assertEquals(c, component);
            }
        };
        
        helper.notifyListener(listener, 10, null);
    }

    @Test
    public void testNotifyListenerInsert() {
        Component component = new Component();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        SimpleKeyListener listener = new SimpleKeyListener() {
            @Override
            public void insert(Component c) {
                assertEquals(c, component);
            }
        };
        
        helper.notifyListener(listener, 155, null);
    }

    @Test
    public void testNotifyListenerDelete() {
        Component component = new Component();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        SimpleKeyListener listener = new SimpleKeyListener() {
            @Override
            public void delete(Component c) {
                assertEquals(c, component);
            }
        };
        
        helper.notifyListener(listener, 127, null);
    }

    @Test
    public void testNotifyListenerTab() {
        Component component = new Component();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        SimpleKeyListener listener = new SimpleKeyListener() {
            @Override
            public void tab(Component c) {
                assertEquals(c, component);
            }
        };
        
        helper.notifyListener(listener, 9, null);
    }

    @Test
    public void testKeyPressed() {
        Component component = new Component();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        KeyEvent keyEvent = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, 10, KeyEvent.CHAR_UNDEFINED);

        helper.keyPressed(keyEvent);
        // Add assertion for expected behavior
    }
}