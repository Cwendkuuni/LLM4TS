import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.*;
import java.awt.event.*;

public class SimpleKeyListenerHelperTest {
    
    private Component mockComponent;
    private SimpleKeyListenerHelper listenerHelper;
    
    @Before
    public void setUp() {
        mockComponent = mock(Component.class);
        listenerHelper = new SimpleKeyListenerHelper(mockComponent);
    }
    
    @Test
    public void detach_ComponentNotNull_RemoveKeyListenerFromComponent() {
        listenerHelper.detach();
        verify(mockComponent).removeKeyListener(listenerHelper);
    }
    
    @Test
    public void detach_ComponentNull_DoNothing() {
        listenerHelper.detach();
        assertNull(listenerHelper.getComponent());
    }
    
    @Test
    public void notifyListener_Type10_CallbackListenerEnter() {
        SimpleKeyListener mockListener = mock(SimpleKeyListener.class);
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(mockComponent);
        helper.attachListener(mockListener);
        
        helper.notifyListener(mockListener, 10, null);
        
        verify(mockListener).enter(mockComponent);
    }
    
    @Test
    public void notifyListener_Type155_CallbackListenerInsert() {
        SimpleKeyListener mockListener = mock(SimpleKeyListener.class);
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(mockComponent);
        helper.attachListener(mockListener);
        
        helper.notifyListener(mockListener, 155, null);
        
        verify(mockListener).insert(mockComponent);
    }
    
    @Test
    public void notifyListener_Type127_CallbackListenerDelete() {
        SimpleKeyListener mockListener = mock(SimpleKeyListener.class);
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(mockComponent);
        helper.attachListener(mockListener);
        
        helper.notifyListener(mockListener, 127, null);
        
        verify(mockListener).delete(mockComponent);
    }
    
    @Test
    public void notifyListener_Type9_CallbackListenerTab() {
        SimpleKeyListener mockListener = mock(SimpleKeyListener.class);
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(mockComponent);
        helper.attachListener(mockListener);
        
        helper.notifyListener(mockListener, 9, null);
        
        verify(mockListener).tab(mockComponent);
    }
    
    @Test
    public void keyPressed_KeyCode9Or10Or127Or155_FireEvent() {
        KeyEvent mockEvent = mock(KeyEvent.class);
        when(mockEvent.getKeyCode()).thenReturn(9);
        
        listenerHelper.keyPressed(mockEvent);
        
        verify(mockComponent).fire(9);
    }
    
    @Test
    public void keyReleased_DoNothing() {
        KeyEvent mockEvent = mock(KeyEvent.class);
        
        listenerHelper.keyReleased(mockEvent);
        
        // No assertion needed as the method does nothing
    }
    
    @Test
    public void keyTyped_DoNothing() {
        KeyEvent mockEvent = mock(KeyEvent.class);
        
        listenerHelper.keyTyped(mockEvent);
        
        // No assertion needed as the method does nothing
    }
}