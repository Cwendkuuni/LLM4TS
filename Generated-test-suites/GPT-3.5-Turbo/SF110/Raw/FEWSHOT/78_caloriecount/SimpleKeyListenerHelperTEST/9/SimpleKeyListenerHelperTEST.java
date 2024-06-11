package com.lts.event;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.awt.*;
import java.awt.event.*;

public class SimpleKeyListenerHelperTest {

    private SimpleKeyListenerHelper simpleKeyListenerHelper;
    private Component mockComponent;

    @Before
    public void setUp() {
        mockComponent = new Component() {};
        simpleKeyListenerHelper = new SimpleKeyListenerHelper(mockComponent);
    }

    @Test
    public void testDetach() {
      assertNotNull(simpleKeyListenerHelper.component);
      simpleKeyListenerHelper.detach();
      assertNull(simpleKeyListenerHelper.component);
    }

    @Test
    public void testNotifyListenerEnter() {
      SimpleKeyListener listener = new SimpleKeyListener() {
        @Override
        public void enter(Component component) {
          assertEquals(mockComponent, component);
          enterMethodCalled = true;
        }
      };
      simpleKeyListenerHelper.notifyListener(listener, 10, null);
      assertTrue(listener.enterMethodCalled);
    }

    @Test
    public void testNotifyListenerInsert() {
      SimpleKeyListener listener = new SimpleKeyListener() {
        @Override
        public void insert(Component component) {
          assertEquals(mockComponent, component);
          insertMethodCalled = true;
        }
      };
      simpleKeyListenerHelper.notifyListener(listener, 155, null);
      assertTrue(listener.insertMethodCalled);
    }

    @Test
    public void testNotifyListenerDelete() {
      SimpleKeyListener listener = new SimpleKeyListener() {
        @Override
        public void delete(Component component) {
          assertEquals(mockComponent, component);
          deleteMethodCalled = true;
        }
      };
      simpleKeyListenerHelper.notifyListener(listener, 127, null);
      assertTrue(listener.deleteMethodCalled);
    }

    @Test
    public void testNotifyListenerTab() {
      SimpleKeyListener listener = new SimpleKeyListener() {
        @Override
        public void tab(Component component) {
          assertEquals(mockComponent, component);
          tabMethodCalled = true;
        }
      };
      simpleKeyListenerHelper.notifyListener(listener, 9, null);
      assertTrue(listener.tabMethodCalled);
    }

    @Test
    public void testKeyPressed() {
      KeyEvent enterKeyEvent = new KeyEvent(mockComponent, KeyEvent.KEY_PRESSED, 0, KeyEvent.VK_ENTER, KeyEvent.CHAR_UNDEFINED);
      KeyEvent insertKeyEvent = new KeyEvent(mockComponent, KeyEvent.KEY_PRESSED, 0, KeyEvent.VK_INSERT, KeyEvent.CHAR_UNDEFINED);
      KeyEvent deleteKeyEvent = new KeyEvent(mockComponent, KeyEvent.KEY_PRESSED, 0, KeyEvent.VK_DELETE, KeyEvent.CHAR_UNDEFINED);
      KeyEvent tabKeyEvent = new KeyEvent(mockComponent, KeyEvent.KEY_PRESSED, 0, KeyEvent.VK_TAB, KeyEvent.CHAR_UNDEFINED);

      simpleKeyListenerHelper.keyPressed(enterKeyEvent);
      assertTrue(enterMethodCalled);

      simpleKeyListenerHelper.keyPressed(insertKeyEvent);
      assertTrue(insertMethodCalled);

      simpleKeyListenerHelper.keyPressed(deleteKeyEvent);
      assertTrue(deleteMethodCalled);

      simpleKeyListenerHelper.keyPressed(tabKeyEvent);
      assertTrue(tabMethodCalled);
    }

    @Test
    public void testKeyReleased() {
      assertNull(arg0);
    }

    @Test
    public void testKeyTyped() {
      assertNull(event);
    }

}