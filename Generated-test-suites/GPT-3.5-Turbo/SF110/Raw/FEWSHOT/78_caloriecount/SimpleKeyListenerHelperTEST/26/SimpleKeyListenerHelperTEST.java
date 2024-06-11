
package com.lts.event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.awt.Component;
import java.awt.event.KeyEvent;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;


public class SimpleKeyListenerHelperTest {
    private SimpleKeyListenerHelper helper;

    @Before
    public void setUp() {
        Component component = mock(Component.class);
        helper = new SimpleKeyListenerHelper(component);
    }

    @Test
    public void testDetach() {
        assertNull(helper.component);

        helper.detach();

        assertNull(helper.component);
    }

    @Test
    public void testNotifyListener() {
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        Component component = mock(Component.class);

        helper.component = component;

        helper.notifyListener(listener, 10, null);
        verify(listener).enter(component);

        helper.notifyListener(listener, 155, null);
        verify(listener).insert(component);

        helper.notifyListener(listener, 127, null);
        verify(listener).delete(component);

        helper.notifyListener(listener, 9, null);
        verify(listener).tab(component);
    }

    @Test
    public void testKeyPressed() {
        KeyEvent event = mock(KeyEvent.class);
        ArgumentCaptor<Integer> captor = ArgumentCaptor.forClass(Integer.class);

        helper.keyPressed(event);

        verify(helper).fire(captor.capture());
        assertEquals(KeyEvent.VK_ENTER, captor.getValue().intValue());

        helper.keyPressed(event);

        verify(helper).fire(captor.capture());
        assertEquals(KeyEvent.VK_TAB, captor.getValue().intValue());

        helper.keyPressed(event);

        verify(helper).fire(captor.capture());
        assertEquals(KeyEvent.VK_DELETE, captor.getValue().intValue());

        helper.keyPressed(event);

        verify(helper).fire(captor.capture());
        assertEquals(KeyEvent.VK_INSERT, captor.getValue().intValue());
    }

    @Test
    public void testKeyReleased() {
        // No need to test keyReleased method as it does nothing.
    }

    @Test
    public void testKeyTyped() {
        // No need to test keyTyped method as it does nothing.
    }
}
