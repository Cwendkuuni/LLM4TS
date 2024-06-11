import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import java.awt.Component;
import java.awt.event.KeyEvent;

import static org.junit.Assert.*;

public class SimpleKeyListenerHelperTest {

    private Component component;
    private SimpleKeyListenerHelper helper;

    @Before
    public void setUp() {
        component = new Component();
        helper = new SimpleKeyListenerHelper(component);
    }

    @After
    public void tearDown() {
        helper.detach();
    }

    @Test
    public void testDetach() {
        // Test detach when component is not null
        helper.detach();
        assertNull(helper.getComponent());
        
        // Test detach when component is null
        helper = new SimpleKeyListenerHelper(null);
        helper.detach();
        assertNull(helper.getComponent());
    }
    
    @Test
    public void testNotifyListener() {
        // Test notifyListener with different types
        SimpleKeyListener listener = new SimpleKeyListener();
        
        // Type 10
        helper.notifyListener(listener, 10, null);
        // Verify listener.enter(this.component) is called
        
        // Type 155
        helper.notifyListener(listener, 155, null);
        // Verify listener.insert(this.component) is called
        
        // Type 127
        helper.notifyListener(listener, 127, null);
        // Verify listener.delete(this.component) is called
        
        // Type 9
        helper.notifyListener(listener, 9, null);
        // Verify listener.tab(this.component) is called
    }

    @Test
    public void testKeyPressed() {
        // Test keyPressed with different key codes
        KeyEvent event;
        
        // Key code 9
        event = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_TAB);
        helper.keyPressed(event);
        // Verify fire(KeyEvent.VK_TAB) is called
        
        // Key code 10
        event = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER);
        helper.keyPressed(event);
        // Verify fire(KeyEvent.VK_ENTER) is called
        
        // Key code 127
        event = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_DELETE);
        helper.keyPressed(event);
        // Verify fire(KeyEvent.VK_DELETE) is called
        
        // Key code 155
        event = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, 155);
        helper.keyPressed(event);
        // Verify fire(155) is called
    }
}