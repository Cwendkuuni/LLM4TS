
package com.lts.event;

import org.junit.Before;
import org.junit.Test;

import java.awt.Component;
import java.awt.event.KeyEvent;

import static org.mockito.Mockito.*;

public class SimpleKeyListenerHelperTest {
  
  private SimpleKeyListenerHelper simpleKeyListenerHelper;
  private SimpleKeyListener simpleKeyListenerMock;
  private Component componentMock;
  
  @Before
  public void setup() {
    simpleKeyListenerMock = mock(SimpleKeyListener.class);
    componentMock = mock(Component.class);
    
    simpleKeyListenerHelper = new SimpleKeyListenerHelper(componentMock);
    simpleKeyListenerHelper.registerListener(simpleKeyListenerMock);
  }
  
  @Test
  public void testDetach() {
    simpleKeyListenerHelper.detach();
    verify(componentMock, times(1)).removeKeyListener(simpleKeyListenerHelper);
  }
  
  @Test
  public void testNotifyListener_EnterType() {
    Object o = simpleKeyListenerMock;
    int type = 10;
    Object data = null;
    
    simpleKeyListenerHelper.notifyListener(o, type, data);
    
    verify(simpleKeyListenerMock, times(1)).enter(componentMock);
  }
  
  @Test
  public void testNotifyListener_InsertType() {
    Object o = simpleKeyListenerMock;
    int type = 155;
    Object data = null;
    
    simpleKeyListenerHelper.notifyListener(o, type, data);
    
    verify(simpleKeyListenerMock, times(1)).insert(componentMock);
  }
  
  @Test
  public void testNotifyListener_DeleteType() {
    Object o = simpleKeyListenerMock;
    int type = 127;
    Object data = null;
    
    simpleKeyListenerHelper.notifyListener(o, type, data);
    
    verify(simpleKeyListenerMock, times(1)).delete(componentMock);
  }
  
  @Test
  public void testNotifyListener_TabType() {
    Object o = simpleKeyListenerMock;
    int type = 9;
    Object data = null;
    
    simpleKeyListenerHelper.notifyListener(o, type, data);
    
    verify(simpleKeyListenerMock, times(1)).tab(componentMock);
  }
  
  @Test
  public void testKeyPressed_EnterKeyCode() {
    KeyEvent eventMock = mock(KeyEvent.class);
    when(eventMock.getKeyCode()).thenReturn(10);
    
    simpleKeyListenerHelper.keyPressed(eventMock);
    
    verify(simpleKeyListenerMock, times(1)).enter(componentMock);
    verify(simpleKeyListenerHelper, times(1)).fire(10);
  }
  
  @Test
  public void testKeyPressed_InsertKeyCode() {
    KeyEvent eventMock = mock(KeyEvent.class);
    when(eventMock.getKeyCode()).thenReturn(155);
    
    simpleKeyListenerHelper.keyPressed(eventMock);
    
    verify(simpleKeyListenerMock, times(1)).insert(componentMock);
    verify(simpleKeyListenerHelper, times(1)).fire(155);
  }
  
  @Test
  public void testKeyPressed_DeleteKeyCode() {
    KeyEvent eventMock = mock(KeyEvent.class);
    when(eventMock.getKeyCode()).thenReturn(127);
    
    simpleKeyListenerHelper.keyPressed(eventMock);
    
    verify(simpleKeyListenerMock, times(1)).delete(componentMock);
    verify(simpleKeyListenerHelper, times(1)).fire(127);
  }
  
  @Test
  public void testKeyPressed_TabKeyCode() {
    KeyEvent eventMock = mock(KeyEvent.class);
    when(eventMock.getKeyCode()).thenReturn(9);
    
    simpleKeyListenerHelper.keyPressed(eventMock);
    
    verify(simpleKeyListenerMock, times(1)).tab(componentMock);
    verify(simpleKeyListenerHelper, times(1)).fire(9);
  }
  
}

