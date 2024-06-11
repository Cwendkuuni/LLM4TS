import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import java.awt.Component;
import java.awt.event.KeyEvent;

public class SimpleKeyListenerHelperTest {
    
    private SimpleKeyListenerHelper helper;
    private Component component;
    
    @Before
    public void setUp() {
        component = new Component() {};
        helper = new SimpleKeyListenerHelper(component);
    }
    
    @Test
    public void testDetach() {
        helper.detach();
        assertNull(helper.component);
    }
    
    @Test
    public void testNotifyListener() {
        // Test for case 10
        SimpleKeyListener listener = new SimpleKeyListener() {
            @Override
            public void enter(Component c) {
                assertEquals(c, component);
            }
        };
        helper.notifyListener(listener, 10, null);
        
        // Test for case 155
        listener = new SimpleKeyListener() {
            @Override
            public void insert(Component c) {
                assertEquals(c, component);
            }
        };
        helper.notifyListener(listener, 155, null);
        
        // Test for case 127
        listener = new SimpleKeyListener() {
            @Override
            public void delete(Component c) {
                assertEquals(c, component);
            }
        };
        helper.notifyListener(listener, 127, null);
        
        // Test for case 9
        listener = new SimpleKeyListener() {
            @Override
            public void tab(Component c) {
                assertEquals(c, component);
            }
        };
        helper.notifyListener(listener, 9, null);
    }
    
    @Test
    public void testKeyPressed() {
        // Test for case 9
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_TAB, KeyEvent.CHAR_UNDEFINED);
        helper.keyPressed(event);
        
        // Test for case 10
        event = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, KeyEvent.CHAR_UNDEFINED);
        helper.keyPressed(event);
        
        // Test for case 127
        event = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_DELETE, KeyEvent.CHAR_UNDEFINED);
        helper.keyPressed(event);
        
        // Test for case 155
        event = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, 155, KeyEvent.CHAR_UNDEFINED);
        helper.keyPressed(event);
    }
}