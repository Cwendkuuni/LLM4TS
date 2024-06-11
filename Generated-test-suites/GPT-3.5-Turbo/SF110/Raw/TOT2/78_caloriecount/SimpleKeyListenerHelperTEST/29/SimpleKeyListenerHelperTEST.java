import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SimpleKeyListenerHelperTest {
    
    private SimpleKeyListenerHelper simpleKeyListenerHelper;
    private SimpleKeyListener mockListener;
    private Component mockComponent;
    
    @BeforeEach
    public void setUp() {
        mockListener = mock(SimpleKeyListener.class);
        mockComponent = mock(Component.class);
        simpleKeyListenerHelper = new SimpleKeyListenerHelper(mockComponent);
    }

    @Test
    public void testDetach() {
        // Verify component is not null after initialization
        assertNotNull(simpleKeyListenerHelper.component);
        
        simpleKeyListenerHelper.detach();
        
        // Verify component is null after detaching
        assertNull(simpleKeyListenerHelper.component);
    }

    @Test
    public void testNotifyListener() {
        // Use case for type 10
        simpleKeyListenerHelper.notifyListener(mockListener, 10, null);
        verify(mockListener).enter(any(Component.class));
        
        // Edge case for type 155
        simpleKeyListenerHelper.notifyListener(mockListener, 155, null);
        verify(mockListener).insert(any(Component.class));
        
        // Error scenario for unknown type
        assertThrows(IllegalArgumentException.class, () -> {
            simpleKeyListenerHelper.notifyListener(mockListener, 999, null);
        });
    }

    @Test
    public void testKeyPressed() {
        KeyEvent mockKeyEvent = mock(KeyEvent.class);
        
        // Use case for key code 9
        when(mockKeyEvent.getKeyCode()).thenReturn(9);
        simpleKeyListenerHelper.keyPressed(mockKeyEvent);
        verify(mockComponent).fire(9);
        
        // Edge case for key code 155
        when(mockKeyEvent.getKeyCode()).thenReturn(155);
        simpleKeyListenerHelper.keyPressed(mockKeyEvent);
        verify(mockComponent).fire(155);
        
        // Error scenario for unknown key code
        when(mockKeyEvent.getKeyCode()).thenReturn(999);
        assertThrows(IllegalArgumentException.class, () -> {
            simpleKeyListenerHelper.keyPressed(mockKeyEvent);
        });
    }
    
    // Additional test methods for keyReleased and keyTyped can be added if needed

}