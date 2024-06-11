package com.lts.event;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.awt.*;
import java.awt.event.*;

public class SimpleKeyListenerHelperTest {

    private static class MockSimpleKeyListener implements SimpleKeyListener {

        boolean enterCalled;
        boolean insertCalled;
        boolean deleteCalled;
        boolean tabCalled;

        @Override
        public void enter(Component component) {
            enterCalled = true;
        }

        @Override
        public void insert(Component component) {
            insertCalled = true;
        }

        @Override
        public void delete(Component component) {
            deleteCalled = true;
        }

        @Override
        public void tab(Component component) {
            tabCalled = true;
        }
    }

    private SimpleKeyListenerHelper listenerHelperMock;
    private MockSimpleKeyListener mockListener;
    private Component mockComponent;

    @Before
    public void setUp() {
        mockListener = new MockSimpleKeyListener();
        mockComponent = new Component() {};
        listenerHelperMock = new SimpleKeyListenerHelper(mockComponent);
        listenerHelperMock.addListener(mockListener);
    }

    @Test
    public void testDetach() {
        listenerHelperMock.detach();
        assertNull(listenerHelperMock.component);
    }

    @Test
    public void testKeyPressed_Enter() {
        KeyEvent event = new KeyEvent(mockComponent, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, KeyEvent.CHAR_UNDEFINED);
        listenerHelperMock.keyPressed(event);
        assertTrue(mockListener.enterCalled);
        assertFalse(mockListener.insertCalled);
        assertFalse(mockListener.deleteCalled);
        assertFalse(mockListener.tabCalled);
    }

    @Test
    public void testKeyPressed_Insert() {
        KeyEvent event = new KeyEvent(mockComponent, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_INSERT, KeyEvent.CHAR_UNDEFINED);
        listenerHelperMock.keyPressed(event);
        assertFalse(mockListener.enterCalled);
        assertTrue(mockListener.insertCalled);
        assertFalse(mockListener.deleteCalled);
        assertFalse(mockListener.tabCalled);
    }

    @Test
    public void testKeyPressed_Delete() {
        KeyEvent event = new KeyEvent(mockComponent, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_DELETE, KeyEvent.CHAR_UNDEFINED);
        listenerHelperMock.keyPressed(event);
        assertFalse(mockListener.enterCalled);
        assertFalse(mockListener.insertCalled);
        assertTrue(mockListener.deleteCalled);
        assertFalse(mockListener.tabCalled);
    }

    @Test
    public void testKeyPressed_Tab() {
        KeyEvent event = new KeyEvent(mockComponent, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_TAB, KeyEvent.CHAR_UNDEFINED);
        listenerHelperMock.keyPressed(event);
        assertFalse(mockListener.enterCalled);
        assertFalse(mockListener.insertCalled);
        assertFalse(mockListener.deleteCalled);
        assertTrue(mockListener.tabCalled);
    }

    @Test
    public void testKeyReleased() {
        // no implementation, nothing to test
    }

    @Test
    public void testKeyTyped() {
        // no implementation, nothing to test
    }
}