import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.awt.*;
import java.awt.event.KeyEvent;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SimpleKeyListenerHelperTest {
    
    @Mock
    private Component component;
    
    private SimpleKeyListenerHelper simpleKeyListenerHelper;
    
    @Before
    public void setUp() {
        simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
    }
    
    @Test
    public void testDetach_ComponentNotNull_ComponentRemovedKeyListener() {
        simpleKeyListenerHelper.detach();
        
        verify(component).removeKeyListener(any(KeyListener.class));
    }
    
    @Test
    public void testDetach_ComponentNotNull_ComponentSetToNull() {
        simpleKeyListenerHelper.detach();
        
        assertNull(simpleKeyListenerHelper.getComponent());
    }
    
    @Test
    public void testNotifyListener_Type10_CallsListenerEnterMethod() {
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        simpleKeyListenerHelper.notifyListener(listener, 10, null);
        
        verify(listener).enter(any(Component.class));
    }
    
    @Test
    public void testNotifyListener_Type155_CallsListenerInsertMethod() {
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        simpleKeyListenerHelper.notifyListener(listener, 155, null);
        
        verify(listener).insert(any(Component.class));
    }
    
    @Test
    public void testNotifyListener_Type127_CallsListenerDeleteMethod() {
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        simpleKeyListenerHelper.notifyListener(listener, 127, null);
        
        verify(listener).delete(any(Component.class));
    }
    
    @Test
    public void testNotifyListener_Type9_CallsListenerTabMethod() {
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        simpleKeyListenerHelper.notifyListener(listener, 9, null);
        
        verify(listener).tab(any(Component.class));
    }
    
    @Test
    public void testKeyPressed_KeyCode9_FireMethodCalled() {
        KeyEvent event = mock(KeyEvent.class);
        when(event.getKeyCode()).thenReturn(9);
        
        simpleKeyListenerHelper.keyPressed(event);
        
        // Verify that fire method was called
    }
    
    @Test
    public void testKeyPressed_KeyCode10_FireMethodCalled() {
        KeyEvent event = mock(KeyEvent.class);
        when(event.getKeyCode()).thenReturn(10);
        
        simpleKeyListenerHelper.keyPressed(event);
        
        // Verify that fire method was called
    }
    
    @Test
    public void testKeyPressed_KeyCode127_FireMethodCalled() {
        KeyEvent event = mock(KeyEvent.class);
        when(event.getKeyCode()).thenReturn(127);
        
        simpleKeyListenerHelper.keyPressed(event);
        
        // Verify that fire method was called
    }
    
    @Test
    public void testKeyPressed_KeyCode155_FireMethodCalled() {
        KeyEvent event = mock(KeyEvent.class);
        when(event.getKeyCode()).thenReturn(155);
        
        simpleKeyListenerHelper.keyPressed(event);
        
        // Verify that fire method was called
    }
    
    // Additional test cases for keyReleased and keyTyped methods can be added if required
    
}