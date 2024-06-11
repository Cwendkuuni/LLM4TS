import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.event.KeyEvent;

import static org.mockito.Mockito.*;

public class SimpleKeyListenerHelperTest {

    private SimpleKeyListenerHelper helper;
    private Component mockComponent;
    private SimpleKeyListener mockListener;

    @Before
    public void setUp() {
        mockComponent = mock(Component.class);
        mockListener = mock(SimpleKeyListener.class);

        helper = new SimpleKeyListenerHelper(mockComponent);
    }

    @Test
    public void testDetach_success() {
        helper.detach();

        verify(mockComponent, times(1)).removeKeyListener(any(KeyListener.class));
    }

    @Test
    public void testNotifyListener_enter() {
        int type = 10;
        Object data = new Object();

        helper.notifyListener(mockListener, type, data);

        verify(mockListener, times(1)).enter(mockComponent);
    }

    @Test
    public void testNotifyListener_insert() {
        int type = 155;
        Object data = new Object();

        helper.notifyListener(mockListener, type, data);

        verify(mockListener, times(1)).insert(mockComponent);
    }

    @Test
    public void testNotifyListener_delete() {
        int type = 127;
        Object data = new Object();

        helper.notifyListener(mockListener, type, data);

        verify(mockListener, times(1)).delete(mockComponent);
    }

    @Test
    public void testNotifyListener_tab() {
        int type = 9;
        Object data = new Object();

        helper.notifyListener(mockListener, type, data);

        verify(mockListener, times(1)).tab(mockComponent);
    }

    @Test
    public void testKeyPressed_withValidKeyCode() {
        KeyEvent mockEvent = mock(KeyEvent.class);
        when(mockEvent.getKeyCode()).thenReturn(KeyEvent.VK_ENTER);

        helper.keyPressed(mockEvent);

        verify(helper, times(1)).fire(KeyEvent.VK_ENTER);
    }

    @Test
    public void testKeyPressed_withInvalidKeyCode() {
        KeyEvent mockEvent = mock(KeyEvent.class);
        when(mockEvent.getKeyCode()).thenReturn(KeyEvent.VK_SPACE);

        helper.keyPressed(mockEvent);

        verify(helper, never()).fire(anyInt());
    }

    @Test
    public void testKeyReleased() {
        KeyEvent mockEvent = mock(KeyEvent.class);

        helper.keyReleased(mockEvent);

        // No verification needed as method does nothing
    }

    @Test
    public void testKeyTyped() {
        KeyEvent mockEvent = mock(KeyEvent.class);

        helper.keyTyped(mockEvent);

        // No verification needed as method does nothing
    }
}