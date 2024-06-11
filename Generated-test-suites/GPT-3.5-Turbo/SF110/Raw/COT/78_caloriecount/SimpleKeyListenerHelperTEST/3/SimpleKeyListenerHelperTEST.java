package com.lts.event;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class SimpleKeyListenerHelperTest {

    private SimpleKeyListenerHelper simpleKeyListenerHelper;
    private Component mockedComponent;
    private SimpleKeyListener mockedListener;

    @Before
    public void setup() {
        mockedComponent = Mockito.mock(Component.class);
        mockedListener = Mockito.mock(SimpleKeyListener.class);
        simpleKeyListenerHelper = new SimpleKeyListenerHelper(mockedComponent);
    }

    @Test
    public void testDetach() {
        simpleKeyListenerHelper.detach();
        
        Mockito.verify(mockedComponent).removeKeyListener(simpleKeyListenerHelper);
        Assert.assertNull(simpleKeyListenerHelper.getComponent());
    }

    @Test
    public void testNotifyListenerEnter() {
        Object o = mockedListener;
        int type = 10;
        Object data = null;

        simpleKeyListenerHelper.notifyListener(o, type, data);
        
        Mockito.verify(mockedListener).enter(mockedComponent);
    }

    @Test
    public void testNotifyListenerInsert() {
        Object o = mockedListener;
        int type = 155;
        Object data = null;

        simpleKeyListenerHelper.notifyListener(o, type, data);
        
        Mockito.verify(mockedListener).insert(mockedComponent);
    }

    @Test
    public void testNotifyListenerDelete() {
        Object o = mockedListener;
        int type = 127;
        Object data = null;

        simpleKeyListenerHelper.notifyListener(o, type, data);
        
        Mockito.verify(mockedListener).delete(mockedComponent);
    }

    @Test
    public void testNotifyListenerTab() {
        Object o = mockedListener;
        int type = 9;
        Object data = null;

        simpleKeyListenerHelper.notifyListener(o, type, data);
        
        Mockito.verify(mockedListener).tab(mockedComponent);
    }

    @Test
    public void testKeyPressedTab() {
        KeyEvent event = new KeyEvent(mockedComponent, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_TAB, ' ');

        simpleKeyListenerHelper.keyPressed(event);
        
        Mockito.verify(mockedListener).tab(mockedComponent);
    }

    @Test
    public void testKeyPressedEnter() {
        KeyEvent event = new KeyEvent(mockedComponent, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, ' ');

        simpleKeyListenerHelper.keyPressed(event);
        
        Mockito.verify(mockedListener).enter(mockedComponent);
    }

    @Test
    public void testKeyPressedDelete() {
        KeyEvent event = new KeyEvent(mockedComponent, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_DELETE, ' ');

        simpleKeyListenerHelper.keyPressed(event);
        
        Mockito.verify(mockedListener).delete(mockedComponent);
    }

    @Test
    public void testKeyPressedInsert() {
        KeyEvent event = new KeyEvent(mockedComponent, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_INSERT, ' ');

        simpleKeyListenerHelper.keyPressed(event);
        
        Mockito.verify(mockedListener).insert(mockedComponent);
    }

    @Test
    public void testKeyReleased() {
        KeyEvent event = new KeyEvent(mockedComponent, KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, KeyEvent.VK_TAB, ' ');

        simpleKeyListenerHelper.keyReleased(event);
        
        // No action is performed for keyReleased
    }

    @Test
    public void testKeyTyped() {
        KeyEvent event = new KeyEvent(mockedComponent, KeyEvent.KEY_TYPED, System.currentTimeMillis(), 0, KeyEvent.VK_TAB, ' ');

        simpleKeyListenerHelper.keyTyped(event);
        
        // No action is performed for keyTyped
    }
}