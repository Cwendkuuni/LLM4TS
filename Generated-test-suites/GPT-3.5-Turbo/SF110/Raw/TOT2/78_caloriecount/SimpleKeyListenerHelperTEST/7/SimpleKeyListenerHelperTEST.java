import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.*;
import java.awt.event.*;

public class SimpleKeyListenerHelperTest {
    
    @Test
    public void testDetach() {
        Component component = new Component();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        helper.detach();
        assertNull(helper.component);
    }
    
    @Test
    public void testNotifyListener() {
        Component component = new Component();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        SimpleKeyListener listener = new SimpleKeyListener() {
            public void enter(Component c) {}
            public void insert(Component c) {}
            public void delete(Component c) {}
            public void tab(Component c) {}
        };
        helper.notifyListener(listener, 10, null);
        helper.notifyListener(listener, 155, null);
        helper.notifyListener(listener, 127, null);
        helper.notifyListener(listener, 9, null);
    }
    
    @Test
    public void testKeyPressed() {
        Component component = new Component();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        KeyEvent mockEvent = new KeyEvent(component, 10, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, KeyEvent.CHAR_UNDEFINED);
        
        helper.keyPressed(mockEvent);
    }
    
    @Test
    public void testKeyReleased() {
        Component component = new Component();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        KeyEvent mockEvent = new KeyEvent(component, 10, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, KeyEvent.CHAR_UNDEFINED);
        
        helper.keyReleased(mockEvent);
    }
    
    @Test
    public void testKeyTyped() {
        Component component = new Component();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        KeyEvent mockEvent = new KeyEvent(component, 10, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, KeyEvent.CHAR_UNDEFINED);
        
        helper.keyTyped(mockEvent);
    }
}