
import org.junit.Before;
import org.junit.Test;
import java.awt.*;
import java.awt.event.KeyEvent;

import static org.mockito.Mockito.*;

public class SimpleKeyListenerHelperTest {
    
    private SimpleKeyListenerHelper simpleKeyListenerHelper;
    private SimpleKeyListener mockListener;
    private Component mockComponent;
    
    @Before
    public void setUp() {
        mockListener = mock(SimpleKeyListener.class);
        mockComponent = mock(Component.class);
        simpleKeyListenerHelper = new SimpleKeyListenerHelper(mockComponent);
        simpleKeyListenerHelper.addListener(mockListener);
    }
    
    @Test
    public void detach_shouldRemoveKeyListenerFromComponent() {
        simpleKeyListenerHelper.detach();
        verify(mockComponent).removeKeyListener(simpleKeyListenerHelper);
    }
    
    @Test
    public void detach_shouldSetComponentToNull() {
        simpleKeyListenerHelper.detach();
        assert(simpleKeyListenerHelper.getComponent() == null);
    }
    
    @Test
    public void notifyListener_shouldCallEnterWhenTypeIs10() {
        simpleKeyListenerHelper.notifyListener(mockListener, 10, null);
        verify(mockListener).enter(simpleKeyListenerHelper.getComponent());
    }
    
    @Test
    public void notifyListener_shouldCallInsertWhenTypeIs155() {
        simpleKeyListenerHelper.notifyListener(mockListener, 155, null);
        verify(mockListener).insert(simpleKeyListenerHelper.getComponent());
    }
    
    @Test
    public void notifyListener_shouldCallDeleteWhenTypeIs127() {
        simpleKeyListenerHelper.notifyListener(mockListener, 127, null);
        verify(mockListener).delete(simpleKeyListenerHelper.getComponent());
    }
    
    @Test
    public void notifyListener_shouldCallTabWhenTypeIs9() {
        simpleKeyListenerHelper.notifyListener(mockListener, 9, null);
        verify(mockListener).tab(simpleKeyListenerHelper.getComponent());
    }
    
    @Test
    public void keyPressed_shouldFireListenerWhenKeyCodeIs9() {
        KeyEvent mockEvent = mock(KeyEvent.class);
        when(mockEvent.getKeyCode()).thenReturn(9);
        simpleKeyListenerHelper.keyPressed(mockEvent);
        verify(mockListener).fire(9);
    }
    
    @Test
    public void keyPressed_shouldFireListenerWhenKeyCodeIs10() {
        KeyEvent mockEvent = mock(KeyEvent.class);
        when(mockEvent.getKeyCode()).thenReturn(10);
        simpleKeyListenerHelper.keyPressed(mockEvent);
        verify(mockListener).fire(10);
    }
    
    @Test
    public void keyPressed_shouldFireListenerWhenKeyCodeIs127() {
        KeyEvent mockEvent = mock(KeyEvent.class);
        when(mockEvent.getKeyCode()).thenReturn(127);
        simpleKeyListenerHelper.keyPressed(mockEvent);
        verify(mockListener).fire(127);
    }
    
    @Test
    public void keyPressed_shouldFireListenerWhenKeyCodeIs155() {
        KeyEvent mockEvent = mock(KeyEvent.class);
        when(mockEvent.getKeyCode()).thenReturn(155);
        simpleKeyListenerHelper.keyPressed(mockEvent);
        verify(mockListener).fire(155);
    }
    
    @Test
    public void keyReleased_shouldNotHaveAnyFunctionality() {
        // No functionality to test
    }
    
    @Test
    public void keyTyped_shouldNotHaveAnyFunctionality() {
        // No functionality to test
    }
}

