import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SimpleKeyListenerHelperTest {

    @Test
    public void testConstructor() {
        Component mockComponent = mock(Component.class);
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(mockComponent);
        assertNotNull(helper);
    }

    @Test
    public void testDetach() {
        Component mockComponent = mock(Component.class);
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(mockComponent);
        helper.detach();
        assertNull(helper.component);
    }

    @Test
    public void testNotifyListener() {
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        Component mockComponent = mock(Component.class);
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(mockComponent);
        helper.notifyListener(listener, 10, null);
        verify(listener).enter(mockComponent);
    }

    @Test
    public void testKeyPressed() {
        SimpleKeyListenerHelper helper = mock(SimpleKeyListenerHelper.class);
        KeyEvent mockEvent = mock(KeyEvent.class);
        when(mockEvent.getKeyCode()).thenReturn(10);
        helper.keyPressed(mockEvent);
        verify(helper).fire(10);
    }
    
    @Test
    public void testKeyPressedInvalidCode() {
        SimpleKeyListenerHelper helper = mock(SimpleKeyListenerHelper.class);
        KeyEvent mockEvent = mock(KeyEvent.class);
        when(mockEvent.getKeyCode()).thenReturn(20);
        helper.keyPressed(mockEvent);
        verify(helper, never()).fire(anyInt());
    }
    
    @Test
    public void testKeyReleased() {
        Component mockComponent = mock(Component.class);
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(mockComponent);
        helper.keyReleased(mock(KeyEvent.class));
        // No assertions needed for this method
    }
    
    @Test
    public void testKeyTyped() {
        Component mockComponent = mock(Component.class);
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(mockComponent);
        helper.keyTyped(mock(KeyEvent.class));
        // No assertions needed for this method
    }
}