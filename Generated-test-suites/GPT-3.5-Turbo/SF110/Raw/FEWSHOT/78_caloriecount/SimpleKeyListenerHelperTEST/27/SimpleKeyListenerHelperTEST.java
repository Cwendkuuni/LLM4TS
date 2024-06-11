package com.lts.event;

import static org.junit.Assert.*;
import org.junit.*;
import java.awt.*;
import java.awt.event.*;

public class SimpleKeyListenerHelperTest {
    private Component component;
    private SimpleKeyListenerHelper helper;
    
    @Before
    public void setUp() {
        component = new Component() {};
        helper = new SimpleKeyListenerHelper(component);
    }
    
    @After
    public void tearDown() {
        helper.detach();
    }
    
    @Test
    public void testDetach() {
        helper.detach();
        assertNull(helper.component);
    }
    
    @Test
    public void testNotifyListenerEnter() {
        SimpleKeyListener listener = new SimpleKeyListener();
        helper.notifyListener(listener, 10, null);
        assertEquals(component, listener.getComponentEntered());
    }
    
    @Test
    public void testNotifyListenerInsert() {
        SimpleKeyListener listener = new SimpleKeyListener();
        helper.notifyListener(listener, 155, null);
        assertEquals(component, listener.getComponentInserted());
    }
    
    @Test
    public void testNotifyListenerDelete() {
        SimpleKeyListener listener = new SimpleKeyListener();
        helper.notifyListener(listener, 127, null);
        assertEquals(component, listener.getComponentDeleted());
    }
    
    @Test
    public void testNotifyListenerTab() {
        SimpleKeyListener listener = new SimpleKeyListener();
        helper.notifyListener(listener, 9, null);
        assertEquals(component, listener.getComponentTabbed());
    }
    
    @Test
    public void testKeyPressed9() {
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_TAB, KeyEvent.CHAR_UNDEFINED);
        helper.keyPressed(event);
        // Assert that the corresponding method call is fired
    }
    
    @Test
    public void testKeyPressed10() {
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, KeyEvent.CHAR_UNDEFINED);
        helper.keyPressed(event);
        // Assert that the corresponding method call is fired
    }
    
    @Test
    public void testKeyPressed127() {
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_DELETE, KeyEvent.CHAR_UNDEFINED);
        helper.keyPressed(event);
        // Assert that the corresponding method call is fired
    }
    
    @Test
    public void testKeyPressed155() {
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_INSERT, KeyEvent.CHAR_UNDEFINED);
        helper.keyPressed(event);
        // Assert that the corresponding method call is fired
    }
    
    @Test
    public void testKeyReleased() {
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, KeyEvent.VK_A, 'a');
        helper.keyReleased(event);
        // Assert that no method call is fired
    }
    
    @Test
    public void testKeyTyped() {
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_TYPED, System.currentTimeMillis(), 0, KeyEvent.VK_UNDEFINED, 'a');
        helper.keyTyped(event);
        // Assert that no method call is fired
    }
}