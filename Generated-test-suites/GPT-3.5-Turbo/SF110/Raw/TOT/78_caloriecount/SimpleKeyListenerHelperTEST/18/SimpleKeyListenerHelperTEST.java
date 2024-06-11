
import org.junit.Test;
import org.junit.Before;
import java.awt.*;
import java.awt.event.*;

import static org.junit.Assert.*;

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
        assertNotNull(helper.getComponent());
        helper.detach();
        assertNull(helper.getComponent());
    }
    
    @Test
    public void testNotifyListener_Enter() {
        SimpleKeyListener listener = new SimpleKeyListener() {
            @Override
            public void enter(Component component) {
                assertTrue(component == helper.getComponent());
            }
        };
        helper.notifyListener(listener, 10, null);
    }
    
    @Test
    public void testNotifyListener_Insert() {
        SimpleKeyListener listener = new SimpleKeyListener() {
            @Override
            public void insert(Component component) {
                assertTrue(component == helper.getComponent());
            }
        };
        helper.notifyListener(listener, 155, null);
    }
    
    @Test
    public void testNotifyListener_Delete() {
        SimpleKeyListener listener = new SimpleKeyListener() {
            @Override
            public void delete(Component component) {
                assertTrue(component == helper.getComponent());
            }
        };
        helper.notifyListener(listener, 127, null);
    }
    
    @Test
    public void testNotifyListener_Tab() {
        SimpleKeyListener listener = new SimpleKeyListener() {
            @Override
            public void tab(Component component) {
                assertTrue(component == helper.getComponent());
            }
        };
        helper.notifyListener(listener, 9, null);
    }
    
    @Test
    public void testKeyPressed_Enter() {
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER);
        boolean fired = helper.fire(event.getKeyCode());
        assertTrue(fired);
    }
    
    @Test
    public void testKeyPressed_Insert() {
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_INSERT);
        boolean fired = helper.fire(event.getKeyCode());
        assertTrue(fired);
    }
    
    @Test
    public void testKeyPressed_Delete() {
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_DELETE);
        boolean fired = helper.fire(event.getKeyCode());
        assertTrue(fired);
    }
    
    @Test
    public void testKeyPressed_Tab() {
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_TAB);
        boolean fired = helper.fire(event.getKeyCode());
        assertTrue(fired);
    }
    
    @Test
    public void testKeyPressed_OtherKey() {
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_A);
        boolean fired = helper.fire(event.getKeyCode());
        assertFalse(fired);
    }
}
