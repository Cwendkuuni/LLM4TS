import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mockito;

public class SimpleKeyListenerHelperTest {
    
    @Test
    public void testConstructor_addKeyListener() {
        Component component = Mockito.mock(Component.class);
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        
        Mockito.verify(component).addKeyListener(helper);
    }
    
    @Test
    public void testDetach_removeKeyListener() {
        Component component = Mockito.mock(Component.class);
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        
        helper.detach();
        Mockito.verify(component).removeKeyListener(helper);
    }
    
    @Test
    public void testDetach_componentNull() {
        Component component = Mockito.mock(Component.class);
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        
        helper.detach();
        assertNull(helper.getComponent());
    }
    
    @Test
    public void testNotifyListener_type10() {
        SimpleKeyListener listener = Mockito.mock(SimpleKeyListener.class);
        Component component = Mockito.mock(Component.class);
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        
        helper.notifyListener(listener, 10, null);
        Mockito.verify(listener).enter(component);
    }
    
    @Test
    public void testNotifyListener_type155() {
        SimpleKeyListener listener = Mockito.mock(SimpleKeyListener.class);
        Component component = Mockito.mock(Component.class);
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        
        helper.notifyListener(listener, 155, null);
        Mockito.verify(listener).insert(component);
    }
    
    @Test
    public void testNotifyListener_type127() {
        SimpleKeyListener listener = Mockito.mock(SimpleKeyListener.class);
        Component component = Mockito.mock(Component.class);
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        
        helper.notifyListener(listener, 127, null);
        Mockito.verify(listener).delete(component);
    }
    
    @Test
    public void testNotifyListener_type9() {
        SimpleKeyListener listener = Mockito.mock(SimpleKeyListener.class);
        Component component = Mockito.mock(Component.class);
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        
        helper.notifyListener(listener, 9, null);
        Mockito.verify(listener).tab(component);
    }
    
    @Test
    public void testKeyPressed_fireValidKeyListenerEvents() {
        SimpleKeyListenerHelper helper = Mockito.mock(SimpleKeyListenerHelper.class);
        KeyEvent event = Mockito.mock(KeyEvent.class);
        Component component = Mockito.mock(Component.class);
        
        Mockito.when(event.getKeyCode()).thenReturn(9);
        helper.keyPressed(event);
        Mockito.verify(helper).fire(9);
        
        Mockito.when(event.getKeyCode()).thenReturn(10);
        helper.keyPressed(event);
        Mockito.verify(helper).fire(10);
        
        Mockito.when(event.getKeyCode()).thenReturn(127);
        helper.keyPressed(event);
        Mockito.verify(helper).fire(127);
        
        Mockito.when(event.getKeyCode()).thenReturn(155);
        helper.keyPressed(event);
        Mockito.verify(helper).fire(155);
    }
    
    // Additional test cases for keyReleased and keyTyped can be added if necessary
    
}