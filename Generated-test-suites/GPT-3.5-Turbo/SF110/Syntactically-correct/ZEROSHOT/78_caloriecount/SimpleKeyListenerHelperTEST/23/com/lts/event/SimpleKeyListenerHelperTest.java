import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import java.awt.Component;
import java.awt.event.KeyEvent;

public class SimpleKeyListenerHelperTest {
    
    private SimpleKeyListenerHelper helper;
    private SimpleKeyListener listener;
    private Component component;
    
    @Before
    public void setUp() {
        listener = mock(SimpleKeyListener.class);
        component = mock(Component.class);
        helper = new SimpleKeyListenerHelper(component);
    }
    
    @Test
    public void testDetach() {
        helper.detach();
        verify(component).removeKeyListener(helper);
    }
    
    @Test
    public void testNotifyListenerEnter() {
        Object o = listener;
        int type = 10;
        Object data = new Object();
        helper.notifyListener(o, type, data);
        verify(listener).enter(component);
    }
    
    @Test
    public void testNotifyListenerInsert() {
        Object o = listener;
        int type = 155;
        Object data = new Object();
        helper.notifyListener(o, type, data);
        verify(listener).insert(component);
    }
    
    @Test
    public void testNotifyListenerDelete() {
        Object o = listener;
        int type = 127;
        Object data = new Object();
        helper.notifyListener(o, type, data);
        verify(listener).delete(component);
    }
    
    @Test
    public void testNotifyListenerTab() {
        Object o = listener;
        int type = 9;
        Object data = new Object();
        helper.notifyListener(o, type, data);
        verify(listener).tab(component);
    }
    
    @Test
    public void testKeyPressedTab() {
        KeyEvent event = mock(KeyEvent.class);
        when(event.getKeyCode()).thenReturn(9);
        helper.keyPressed(event);
        verify(helper).fire(9);
    }
    
    @Test
    public void testKeyPressedEnter() {
        KeyEvent event = mock(KeyEvent.class);
        when(event.getKeyCode()).thenReturn(10);
        helper.keyPressed(event);
        verify(helper).fire(10);
    }
    
    @Test
    public void testKeyPressedDelete() {
        KeyEvent event = mock(KeyEvent.class);
        when(event.getKeyCode()).thenReturn(127);
        helper.keyPressed(event);
        verify(helper).fire(127);
    }
    
    @Test
    public void testKeyPressedInsert() {
        KeyEvent event = mock(KeyEvent.class);
        when(event.getKeyCode()).thenReturn(155);
        helper.keyPressed(event);
        verify(helper).fire(155);
    }
    
    @Test
    public void testKeyReleased() {
        KeyEvent event = mock(KeyEvent.class);
        helper.keyReleased(event);
        verifyNoInteractions(event);
    }
    
    @Test
    public void testKeyTyped() {
        KeyEvent event = mock(KeyEvent.class);
        helper.keyTyped(event);
        verifyNoInteractions(event);
    }
}