import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleKeyListenerHelperTest {

    @Test
    public void testDetach() {
        Component component = new Component();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        
        helper.detach();
        
        assertNull(helper.component);
    }
    
    @Test
    public void testNotifyListenerEnter() {
        Component component = new Component();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        SimpleKeyListener listener = new SimpleKeyListener();
        
        helper.notifyListener(listener, 10, null);
        
        // Add assertion here to test listener.enter() method call
    }
    
    @Test
    public void testNotifyListenerInsert() {
        Component component = new Component();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        SimpleKeyListener listener = new SimpleKeyListener();
        
        helper.notifyListener(listener, 155, null);
        
        // Add assertion here to test listener.insert() method call
    }
    
    @Test
    public void testNotifyListenerDelete() {
        Component component = new Component();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        SimpleKeyListener listener = new SimpleKeyListener();
        
        helper.notifyListener(listener, 127, null);
        
        // Add assertion here to test listener.delete() method call
    }
    
    @Test
    public void testNotifyListenerTab() {
        Component component = new Component();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        SimpleKeyListener listener = new SimpleKeyListener();
        
        helper.notifyListener(listener, 9, null);
        
        // Add assertion here to test listener.tab() method call
    }
    
    @Test
    public void testKeyPressed() {
        Component component = new Component();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        KeyEvent enterEvent = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER);
        
        helper.keyPressed(enterEvent);
        
        // Add assertion here to test fire() method call with enter key code
    }
    
    // Add similar test methods for other cases
    
}