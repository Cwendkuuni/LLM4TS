package com.lts.event;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.awt.*;
import java.awt.event.KeyEvent;

import static org.mockito.Mockito.verify;

public class SimpleKeyListenerHelperTest {

    @Mock
    private Component component;

    private SimpleKeyListenerHelper simpleKeyListenerHelper;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
    }

    @After
    public void tearDown() {
        simpleKeyListenerHelper.detach();
    }

    @Test
    public void testDetach() {
        simpleKeyListenerHelper.detach();
        verify(component).removeKeyListener(simpleKeyListenerHelper);
    }

    @Test
    public void testNotifyListener_Enter() {
        SimpleKeyListener listener = new SimpleKeyListener() {
            @Override
            public void enter(Component component) {
            }

            @Override
            public void insert(Component component) {
            }

            @Override
            public void delete(Component component) {
            }

            @Override
            public void tab(Component component) {
            }
        };
        simpleKeyListenerHelper.notifyListener(listener, 10, null);
        verify(listener).enter(component);
    }

    @Test
    public void testNotifyListener_Insert() {
        SimpleKeyListener listener = new SimpleKeyListener() {
            @Override
            public void enter(Component component) {
            }

            @Override
            public void insert(Component component) {
            }

            @Override
            public void delete(Component component) {
            }

            @Override
            public void tab(Component component) {
            }
        };
        simpleKeyListenerHelper.notifyListener(listener, 155, null);
        verify(listener).insert(component);
    }

    @Test
    public void testNotifyListener_Delete() {
        SimpleKeyListener listener = new SimpleKeyListener() {
            @Override
            public void enter(Component component) {
            }

            @Override
            public void insert(Component component) {
            }

            @Override
            public void delete(Component component) {
            }

            @Override
            public void tab(Component component) {
            }
        };
        simpleKeyListenerHelper.notifyListener(listener, 127, null);
        verify(listener).delete(component);
    }

    @Test
    public void testNotifyListener_Tab() {
        SimpleKeyListener listener = new SimpleKeyListener() {
            @Override
            public void enter(Component component) {
            }

            @Override
            public void insert(Component component) {
            }

            @Override
            public void delete(Component component) {
            }

            @Override
            public void tab(Component component) {
            }
        };
        simpleKeyListenerHelper.notifyListener(listener, 9, null);
        verify(listener).tab(component);
    }

    @Test
    public void testKeyPressed_Enter() {
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, 0, 0, KeyEvent.VK_ENTER, KeyEvent.CHAR_UNDEFINED);
        simpleKeyListenerHelper.keyPressed(event);
        verify(component).fire(10);
    }

    @Test
    public void testKeyPressed_Insert() {
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, 0, 0, KeyEvent.VK_INSERT, KeyEvent.CHAR_UNDEFINED);
        simpleKeyListenerHelper.keyPressed(event);
        verify(component).fire(155);
    }

    @Test
    public void testKeyPressed_Delete() {
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, 0, 0, KeyEvent.VK_DELETE, KeyEvent.CHAR_UNDEFINED);
        simpleKeyListenerHelper.keyPressed(event);
        verify(component).fire(127);
    }

    @Test
    public void testKeyPressed_Tab() {
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, 0, 0, KeyEvent.VK_TAB, KeyEvent.CHAR_UNDEFINED);
        simpleKeyListenerHelper.keyPressed(event);
        verify(component).fire(9);
    }

    @Test
    public void testKeyReleased() {
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_RELEASED, 0, 0, 0, KeyEvent.CHAR_UNDEFINED);
        simpleKeyListenerHelper.keyReleased(event);
        // No verification as the method does not have any implementation
    }

    @Test
    public void testKeyTyped() {
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_TYPED, 0, 0, 0, 'a');
        simpleKeyListenerHelper.keyTyped(event);
        // No verification as the method does not have any implementation
    }
}