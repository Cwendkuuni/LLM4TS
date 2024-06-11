
import org.junit.*;
import static org.junit.Assert.*;

public class SimpleKeyListenerHelperTest {

    @Test
    public void testConstructor_AttachesKeyListener() {
        // Arrange
        Component component = new Component();
        
        // Act
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        
        // Assert
        assertNotNull(component.getKeyListeners());
        assertEquals(1, component.getKeyListeners().length);
        assertSame(helper, component.getKeyListeners()[0]);
    }
    
    @Test
    public void testDetach_RemovesKeyListener() {
        // Arrange
        Component component = new Component();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        
        // Act
        helper.detach();
        
        // Assert
        assertNotNull(component.getKeyListeners());
        assertEquals(0, component.getKeyListeners().length);
    }
    
    @Test
    public void testNotifyListener_EnterType_CallsEnterMethod() {
        // Arrange
        Component component = new Component();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        SimpleKeyListener listener = new SimpleKeyListener();
        helper.attachListener(listener, 10);
        
        // Act
        helper.notifyListener(listener, 10, null);
        
        // Assert
        assertTrue(listener.enterMethodCalled);
        assertFalse(listener.insertMethodCalled);
        assertFalse(listener.deleteMethodCalled);
        assertFalse(listener.tabMethodCalled);
    }
    
    @Test
    public void testNotifyListener_InsertType_CallsInsertMethod() {
        // Arrange
        Component component = new Component();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        SimpleKeyListener listener = new SimpleKeyListener();
        helper.attachListener(listener, 155);
        
        // Act
        helper.notifyListener(listener, 155, null);
        
        // Assert
        assertFalse(listener.enterMethodCalled);
        assertTrue(listener.insertMethodCalled);
        assertFalse(listener.deleteMethodCalled);
        assertFalse(listener.tabMethodCalled);
    }
    
    @Test
    public void testNotifyListener_DeleteType_CallsDeleteMethod() {
        // Arrange
        Component component = new Component();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        SimpleKeyListener listener = new SimpleKeyListener();
        helper.attachListener(listener, 127);
        
        // Act
        helper.notifyListener(listener, 127, null);
        
        // Assert
        assertFalse(listener.enterMethodCalled);
        assertFalse(listener.insertMethodCalled);
        assertTrue(listener.deleteMethodCalled);
        assertFalse(listener.tabMethodCalled);
    }
    
    @Test
    public void testNotifyListener_TabType_CallsTabMethod() {
        // Arrange
        Component component = new Component();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        SimpleKeyListener listener = new SimpleKeyListener();
        helper.attachListener(listener, 9);
        
        // Act
        helper.notifyListener(listener, 9, null);
        
        // Assert
        assertFalse(listener.enterMethodCalled);
        assertFalse(listener.insertMethodCalled);
        assertFalse(listener.deleteMethodCalled);
        assertTrue(listener.tabMethodCalled);
    }
    
    @Test
    public void testKeyPressed_InvalidKeyCode_DoesNotCallAnyMethod() {
        // Arrange
        Component component = new Component();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        SimpleKeyListener listener = new SimpleKeyListener();
        helper.attachListener(listener, 10);
        
        // Act
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_A, KeyEvent.CHAR_UNDEFINED);
        helper.keyPressed(event);
        
        // Assert
        assertFalse(listener.enterMethodCalled);
        assertFalse(listener.insertMethodCalled);
        assertFalse(listener.deleteMethodCalled);
        assertFalse(listener.tabMethodCalled);
    }
    
    @Test
    public void testKeyPressed_EnterKeyCode_CallsEnterMethod() {
        // Arrange
        Component component = new Component();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        SimpleKeyListener listener = new SimpleKeyListener();
        helper.attachListener(listener, 10);
        
        // Act
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, KeyEvent.CHAR_UNDEFINED);
        helper.keyPressed(event);
        
        // Assert
        assertTrue(listener.enterMethodCalled);
        assertFalse(listener.insertMethodCalled);
        assertFalse(listener.deleteMethodCalled);
        assertFalse(listener.tabMethodCalled);
    }
    
    @Test
    public void testKeyPressed_InsertKeyCode_CallsInsertMethod() {
        // Arrange
        Component component = new Component();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        SimpleKeyListener listener = new SimpleKeyListener();
        helper.attachListener(listener, 155);
        
        // Act
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_INSERT, KeyEvent.CHAR_UNDEFINED);
        helper.keyPressed(event);
        
        // Assert
        assertFalse(listener.enterMethodCalled);
        assertTrue(listener.insertMethodCalled);
        assertFalse(listener.deleteMethodCalled);
        assertFalse(listener.tabMethodCalled);
    }
    
    @Test
    public void testKeyPressed_DeleteKeyCode_CallsDeleteMethod() {
        // Arrange
        Component component = new Component();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        SimpleKeyListener listener = new SimpleKeyListener();
        helper.attachListener(listener, 127);
        
        // Act
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_DELETE, KeyEvent.CHAR_UNDEFINED);
        helper.keyPressed(event);
        
        // Assert
        assertFalse(listener.enterMethodCalled);
        assertFalse(listener.insertMethodCalled);
        assertTrue(listener.deleteMethodCalled);
        assertFalse(listener.tabMethodCalled);
    }
    
    @Test
    public void testKeyPressed_TabKeyCode_CallsTabMethod() {
        // Arrange
        Component component = new Component();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        SimpleKeyListener listener = new SimpleKeyListener();
        helper.attachListener(listener, 9);
        
        // Act
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_TAB, KeyEvent.CHAR_UNDEFINED);
        helper.keyPressed(event);
        
        // Assert
        assertFalse(listener.enterMethodCalled);
        assertFalse(listener.insertMethodCalled);
        assertFalse(listener.deleteMethodCalled);
        assertTrue(listener.tabMethodCalled);
    }
    
    
    private class SimpleKeyListener {
        public boolean enterMethodCalled = false;
        public boolean insertMethodCalled = false;
        public boolean deleteMethodCalled = false;
        public boolean tabMethodCalled = false;
        
        public void enter(Component component) {
            enterMethodCalled = true;
        }
        
        public void insert(Component component) {
            insertMethodCalled = true;
        }
        
        public void delete(Component component) {
            deleteMethodCalled = true;
        }
        
        public void tab(Component component) {
            tabMethodCalled = true;
        }
    }
}
