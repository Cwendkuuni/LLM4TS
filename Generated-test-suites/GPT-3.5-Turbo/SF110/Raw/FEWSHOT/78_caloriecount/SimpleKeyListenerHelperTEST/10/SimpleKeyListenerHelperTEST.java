package com.lts.event;

import java.awt.Component;
import java.awt.event.KeyEvent;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class SimpleKeyListenerHelperTest {

    private SimpleKeyListenerHelper helper;
    private Component component;
    private SimpleKeyListener listener;

    @Before
    public void setUp() {
        component = Mockito.mock(Component.class);
        listener = Mockito.mock(SimpleKeyListener.class);
        helper = new SimpleKeyListenerHelper(component);
        helper.addListener(listener);
    }

    @After
    public void tearDown() {
        helper.detach();
    }

    @Test
    public void testDetach() {
        helper.detach();
        Mockito.verify(component, Mockito.times(1)).removeKeyListener(helper);
    }

    @Test
    public void testNotifyListenerEnter() {
        helper.notifyListener(listener, 10, null);
        Mockito.verify(listener, Mockito.times(1)).enter(component);
    }

    @Test
    public void testNotifyListenerInsert() {
        helper.notifyListener(listener, 155, null);
        Mockito.verify(listener, Mockito.times(1)).insert(component);
    }

    @Test
    public void testNotifyListenerDelete() {
        helper.notifyListener(listener, 127, null);
        Mockito.verify(listener, Mockito.times(1)).delete(component);
    }

    @Test
    public void testNotifyListenerTab() {
        helper.notifyListener(listener, 9, null);
        Mockito.verify(listener, Mockito.times(1)).tab(component);
    }

    @Test
    public void testKeyPressedTab() {
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_TAB, KeyEvent.CHAR_UNDEFINED);
        helper.keyPressed(event);
        Mockito.verify(listener, Mockito.times(1)).tab(component);
    }

    @Test
    public void testKeyPressedEnter() {
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, KeyEvent.CHAR_UNDEFINED);
        helper.keyPressed(event);
        Mockito.verify(listener, Mockito.times(1)).enter(component);
    }

    @Test
    public void testKeyPressedDelete() {
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_DELETE, KeyEvent.CHAR_UNDEFINED);
        helper.keyPressed(event);
        Mockito.verify(listener, Mockito.times(1)).delete(component);
    }

    @Test
    public void testKeyPressedInsert() {
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_INSERT, KeyEvent.CHAR_UNDEFINED);
        helper.keyPressed(event);
        Mockito.verify(listener, Mockito.times(1)).insert(component);
    }

    @Test
    public void testKeyReleased() {
        KeyEvent event = Mockito.mock(KeyEvent.class);
        helper.keyReleased(event);
        // No action should be taken in keyReleased
    }

    @Test
    public void testKeyTyped() {
        KeyEvent event = Mockito.mock(KeyEvent.class);
        helper.keyTyped(event);
        // No action should be taken in keyTyped
    }
}