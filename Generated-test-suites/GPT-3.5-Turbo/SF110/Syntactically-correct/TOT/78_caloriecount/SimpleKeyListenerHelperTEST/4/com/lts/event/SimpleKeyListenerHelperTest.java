import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.event.*;

public class SimpleKeyListenerHelperTest {
    
    private SimpleKeyListenerHelper helper;
    private MockComponent component;
    
    private class MockComponent extends Component {
        private String lastMethodCalled;
        
        public String getLastMethodCalled() {
            return lastMethodCalled;
        }
        
        @Override
        public void addKeyListener(KeyListener listener) {
            lastMethodCalled = "addKeyListener";
        }
        
        @Override
        public void removeKeyListener(KeyListener listener) {
            lastMethodCalled = "removeKeyListener";
        }
    }
    
    private class MockListener implements SimpleKeyListener {
        private String lastMethodCalled;
        private Component lastComponent;
        
        public String getLastMethodCalled() {
            return lastMethodCalled;
        }
        
        public Component getLastComponent() {
            return lastComponent;
        }
        
        @Override
        public void enter(Component c) {
            lastMethodCalled = "enter";
            lastComponent = c;
        }
        
        @Override
        public void insert(Component c) {
            lastMethodCalled = "insert";
            lastComponent = c;
        }
        
        @Override
        public void delete(Component c) {
            lastMethodCalled = "delete";
            lastComponent = c;
        }
        
        @Override
        public void tab(Component c) {
            lastMethodCalled = "tab";
            lastComponent = c;
        }
    }
    
    @Before
    public void setup() {
        component = new MockComponent();
        helper = new SimpleKeyListenerHelper(component);
    }
    
    @Test
    public void testDetach() {
        helper.detach();
        assertNull(helper.component);
        assertEquals("removeKeyListener", component.getLastMethodCalled());
    }
    
    @Test
    public void testNotifyListenerEnter() {
        MockListener listener = new MockListener();
        helper.notifyListener(listener, 10, null);
        assertEquals("enter", listener.getLastMethodCalled());
        assertEquals(component, listener.getLastComponent());
    }
    
    @Test
    public void testNotifyListenerInsert() {
        MockListener listener = new MockListener();
        helper.notifyListener(listener, 155, null);
        assertEquals("insert", listener.getLastMethodCalled());
        assertEquals(component, listener.getLastComponent());
    }
    
    @Test
    public void testNotifyListenerDelete() {
        MockListener listener = new MockListener();
        helper.notifyListener(listener, 127, null);
        assertEquals("delete", listener.getLastMethodCalled());
        assertEquals(component, listener.getLastComponent());
    }
    
    @Test
    public void testNotifyListenerTab() {
        MockListener listener = new MockListener();
        helper.notifyListener(listener, 9, null);
        assertEquals("tab", listener.getLastMethodCalled());
        assertEquals(component, listener.getLastComponent());
    }
    
    @Test
    public void testKeyPressedTab() {
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_TAB, KeyEvent.CHAR_UNDEFINED);
        helper.keyPressed(event);
        assertEquals("fire", component.getLastMethodCalled());
        assertEquals(KeyEvent.VK_TAB, event.getKeyCode());
    }
    
    @Test
    public void testKeyPressedEnter() {
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, KeyEvent.CHAR_UNDEFINED);
        helper.keyPressed(event);
        assertEquals("fire", component.getLastMethodCalled());
        assertEquals(KeyEvent.VK_ENTER, event.getKeyCode());
    }
    
    @Test
    public void testKeyPressedDelete() {
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_DELETE, KeyEvent.CHAR_UNDEFINED);
        helper.keyPressed(event);
        assertEquals("fire", component.getLastMethodCalled());
        assertEquals(KeyEvent.VK_DELETE, event.getKeyCode());
    }
    
    @Test
    public void testKeyPressedInsert() {
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_INSERT, KeyEvent.CHAR_UNDEFINED);
        helper.keyPressed(event);
        assertEquals("fire", component.getLastMethodCalled());
        assertEquals(KeyEvent.VK_INSERT, event.getKeyCode());
    }
    
    @Test
    public void testKeyReleased() {
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, KeyEvent.VK_TAB, KeyEvent.CHAR_UNDEFINED);
        helper.keyReleased(event);
        // No action is performed in keyReleased
    }
    
    @Test
    public void testKeyTyped() {
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_TYPED, System.currentTimeMillis(), 0, KeyEvent.VK_TAB, 'a');
        helper.keyTyped(event);
        // No action is performed in keyTyped
    }
}