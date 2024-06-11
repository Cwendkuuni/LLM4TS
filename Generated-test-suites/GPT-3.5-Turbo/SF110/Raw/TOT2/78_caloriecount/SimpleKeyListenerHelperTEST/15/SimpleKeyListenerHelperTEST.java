package com.lts.event;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import java.awt.Component;
import java.awt.event.KeyEvent;
import org.mockito.Mockito;

public class SimpleKeyListenerHelperTest {

    @Test
    public void testDetatch() {
        // Test if detach method sets component to null
        Component mockComponent = Mockito.mock(Component.class);
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(mockComponent);
        helper.detach();
        assertEquals(null, helper.component);
    }

    @Test
    public void testNotifyListener() {
        // Test notifyListener method with different type values
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(new Component());
        SimpleKeyListener mockListener = Mockito.mock(SimpleKeyListener.class);
        helper.notifyListener(mockListener, 10, null);
        Mockito.verify(mockListener).enter(Mockito.any());
        helper.notifyListener(mockListener, 155, null);
        Mockito.verify(mockListener).insert(Mockito.any());
        helper.notifyListener(mockListener, 127, null);
        Mockito.verify(mockListener).delete(Mockito.any());
        helper.notifyListener(mockListener, 9, null);
        Mockito.verify(mockListener).tab(Mockito.any());
    }

    @Test
    public void testKeyPressed() {
        // Test keyPressed method with different key codes
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(new Component());
        KeyEvent eventMock = Mockito.mock(KeyEvent.class);
        Mockito.when(eventMock.getKeyCode()).thenReturn(10);
        helper.keyPressed(eventMock);
        Mockito.verify(helper).fire(10);
    }

    @Test
    public void testKeyReleased() {
        // Test keyReleased method
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(new Component());
        // Nothing to assert, just check if the method runs without error
        helper.keyReleased(Mockito.mock(KeyEvent.class));
    }

    @Test
    public void testKeyTyped() {
        // Test keyTyped method
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(new Component());
        // Nothing to assert, just check if the method runs without error
        helper.keyTyped(Mockito.mock(KeyEvent.class));
    }
}