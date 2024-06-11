import org.junit.Before;
import org.junit.Test;

import java.awt.*;

import static org.mockito.Mockito.*;

public class SimpleKeyListenerHelperTest {

    private SimpleKeyListenerHelper helper;
    private Component component;
    private SimpleKeyListener listener;
    
    @Before
    public void setUp() {
        component = mock(Component.class);
        listener = mock(SimpleKeyListener.class);
        helper = new SimpleKeyListenerHelper(component);
    }
    
    @Test
    public void testDetach_withNonNullComponent_shouldRemoveKeyListener() {
        helper.detach();
        verify(component).removeKeyListener(helper);
    }
    
    @Test
    public void testDetach_withNullComponent_shouldNotRemoveKeyListener() {
        helper.detach();
        verify(component, never()).removeKeyListener(helper);
    }
    
    @Test
    public void testNotifyListener_withType10_shouldInvokeListenerEnterMethod() {
        Object object = listener;
        int type = 10;
        Object data = new Object();
        
        helper.notifyListener(object, type, data);
        
        verify(listener).enter(component);
    }
    
    @Test
    public void testNotifyListener_withType155_shouldInvokeListenerInsertMethod() {
        Object object = listener;
        int type = 155;
        Object data = new Object();
        
        helper.notifyListener(object, type, data);
        
        verify(listener).insert(component);
    }
    
    @Test
    public void testNotifyListener_withType127_shouldInvokeListenerDeleteMethod() {
        Object object = listener;
        int type = 127;
        Object data = new Object();
        
        helper.notifyListener(object, type, data);
        
        verify(listener).delete(component);
    }
    
    @Test
    public void testNotifyListener_withType9_shouldInvokeListenerTabMethod() {
        Object object = listener;
        int type = 9;
        Object data = new Object();
        
        helper.notifyListener(object, type, data);
        
        verify(listener).tab(component);
    }
    
    @Test
    public void testKeyPressed_withKeyCode9_shouldFireEvent() {
        KeyEvent event = mock(KeyEvent.class);
        when(event.getKeyCode()).thenReturn(9);
        
        helper.keyPressed(event);
        
        verify(helper).fire(9);
    }
    
    @Test
    public void testKeyPressed_withKeyCode10_shouldFireEvent() {
        KeyEvent event = mock(KeyEvent.class);
        when(event.getKeyCode()).thenReturn(10);
        
        helper.keyPressed(event);
        
        verify(helper).fire(10);
    }
    
    @Test
    public void testKeyPressed_withKeyCode127_shouldFireEvent() {
        KeyEvent event = mock(KeyEvent.class);
        when(event.getKeyCode()).thenReturn(127);
        
        helper.keyPressed(event);
        
        verify(helper).fire(127);
    }
    
    @Test
    public void testKeyPressed_withKeyCode155_shouldFireEvent() {
        KeyEvent event = mock(KeyEvent.class);
        when(event.getKeyCode()).thenReturn(155);
        
        helper.keyPressed(event);
        
        verify(helper).fire(155);
    }
    
    @Test
    public void testKeyReleased_shouldNotDoAnything() {
        KeyEvent event = mock(KeyEvent.class);
        
        helper.keyReleased(event);
        
        // No verification since the method does nothing
    }
    
    @Test
    public void testKeyTyped_shouldNotDoAnything() {
        KeyEvent event = mock(KeyEvent.class);
        
        helper.keyTyped(event);
        
        // No verification since the method does nothing
    }
}