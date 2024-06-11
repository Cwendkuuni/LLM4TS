import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class SimpleKeyListenerHelperTest {
    
    private SimpleKeyListenerHelper helper;
    private MockComponent component;
    private MockListener listener;
    
    // A mock component for testing purposes
    private class MockComponent extends Component {
        private boolean keyPressed = false;
        
        @Override
        public void addKeyListener(KeyListener listener) {
            assertNotNull(listener);
            assertTrue(listener instanceof SimpleKeyListenerHelper);
            assertEquals(helper, listener);
        }
        
        @Override
        public void removeKeyListener(KeyListener listener) {
            assertNotNull(listener);
            assertTrue(listener instanceof SimpleKeyListenerHelper);
            assertEquals(helper, listener);
        }
        
        @Override
        public void fire(int keyCode) {
            keyPressed = true;
        }
        
        @Override
        public boolean isKeyPressed() {
            return keyPressed;
        }
    }
    
    // A mock listener for testing purposes
    private class MockListener extends Listener {
        private Component lastComponent;
        
        public void enter(Component component) {
            lastComponent = component;
        }
        
        public void insert(Component component) {
            lastComponent = component;
        }
        
        public void delete(Component component) {
            lastComponent = component;
        }
        
        public void tab(Component component) {
            lastComponent = component;
        }
        
        public Component getLastComponent() {
            return lastComponent;
        }
    }
    
    @Before
    public void setUp() {
        component = new MockComponent();
        listener = new MockListener();
        helper = new SimpleKeyListenerHelper(component);
        helper.addListener(listener);
    }
    
    @After
    public void tearDown() {
        helper.detach();
    }
    
    @Test
    public void testDetach() {
        assertNotNull(component);
        assertNotNull(helper);
        
        helper.detach();
        
        assertNull(helper.getComponent());
        assertNull(component);
    }
    
    @Test
    public void testNotifyListener() {
        assertEquals(listener, helper.getListeners().get(0));
        
        helper.notifyListener(listener, 10, null);
        assertEquals(component, listener.getLastComponent());
        
        helper.notifyListener(listener, 155, null);
        assertEquals(component, listener.getLastComponent());
        
        helper.notifyListener(listener, 127, null);
        assertEquals(component, listener.getLastComponent());
        
        helper.notifyListener(listener, 9, null);
        assertEquals(component, listener.getLastComponent());
    }
    
    @Test
    public void testKeyPressed() {
        assertFalse(component.isKeyPressed());
        
        KeyEvent event1 = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), KeyEvent.ALT_DOWN_MASK, KeyEvent.VK_9, KeyEvent.CHAR_UNDEFINED);
        helper.keyPressed(event1);
        
        assertTrue(component.isKeyPressed());
        
        KeyEvent event2 = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), KeyEvent.ALT_DOWN_MASK, KeyEvent.VK_10, KeyEvent.CHAR_UNDEFINED);
        helper.keyPressed(event2);
        
        assertTrue(component.isKeyPressed());
        
        KeyEvent event3 = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), KeyEvent.ALT_DOWN_MASK, KeyEvent.VK_127, KeyEvent.CHAR_UNDEFINED);
        helper.keyPressed(event3);
        
        assertTrue(component.isKeyPressed());
        
        KeyEvent event4 = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), KeyEvent.ALT_DOWN_MASK, KeyEvent.VK_155, KeyEvent.CHAR_UNDEFINED);
        helper.keyPressed(event4);
        
        assertTrue(component.isKeyPressed());
    }
    
    @Test
    public void testKeyReleased() {
        // No test case needed as the method does not perform any action
    }
    
    @Test
    public void testKeyTyped() {
        // No test case needed as the method does not perform any action
    }
}