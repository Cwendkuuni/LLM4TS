import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.event.KeyEvent;

import static org.mockito.Mockito.*;

public class SimpleKeyListenerHelperTest {

    private SimpleKeyListenerHelper helper;
    private SimpleKeyListener listener;
    private Component component;

    @Before
    public void setUp() {
        listener = mock(SimpleKeyListener.class);
        component = mock(Component.class);
        helper = new SimpleKeyListenerHelper(component);
        helper.addListener(listener);
    }
    
    @Test
    public void testDetach() {
        helper.detach();
        verify(component, times(1)).removeKeyListener(helper);
    }
    
    @Test
    public void testDetach_NullComponent() {
        helper.component = null;
        helper.detach(); // No exception should be thrown
    }
    
    @Test
    public void testNotifyListener_Enter() {
        KeyEvent event = mock(KeyEvent.class);
        when(event.getKeyCode()).thenReturn(KeyEvent.VK_ENTER);
        
        helper.keyPressed(event);
        
        verify(listener, times(1)).enter(component);
    }
    
    @Test
    public void testNotifyListener_Insert() {
        KeyEvent event = mock(KeyEvent.class);
        when(event.getKeyCode()).thenReturn(155);
        
        helper.keyPressed(event);
        
        verify(listener, times(1)).insert(component);
    }
    
    @Test
    public void testNotifyListener_Delete() {
        KeyEvent event = mock(KeyEvent.class);
        when(event.getKeyCode()).thenReturn(KeyEvent.VK_DELETE);
        
        helper.keyPressed(event);
        
        verify(listener, times(1)).delete(component);
    }
    
    @Test
    public void testNotifyListener_Tab() {
        KeyEvent event = mock(KeyEvent.class);
        when(event.getKeyCode()).thenReturn(KeyEvent.VK_TAB);
        
        helper.keyPressed(event);
        
        verify(listener, times(1)).tab(component);
    }
    
    @Test
    public void testNotifyListener_NullListener() {
        KeyEvent event = mock(KeyEvent.class);
        when(event.getKeyCode()).thenReturn(KeyEvent.VK_A);
        
        helper.removeListener(listener);
        helper.keyPressed(event); // No exception should be thrown
    }
    
    @Test
    public void testKeyPressed_UnsupportedKey() {
        KeyEvent event = mock(KeyEvent.class);
        when(event.getKeyCode()).thenReturn(KeyEvent.VK_A);
        
        helper.keyPressed(event); // No exception should be thrown
    }

}