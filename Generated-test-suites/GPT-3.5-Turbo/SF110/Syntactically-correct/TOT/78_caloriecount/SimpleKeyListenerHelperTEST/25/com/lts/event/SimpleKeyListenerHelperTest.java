import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.awt.*;
import java.awt.event.*;

public class SimpleKeyListenerHelperTest {
    private SimpleKeyListenerHelper simpleKeyListenerHelper;
    private MockComponent mockComponent;
    private MockListener mockListener;
    
    @Before
    public void setUp() {
        mockComponent = new MockComponent();
        mockListener = new MockListener();
        simpleKeyListenerHelper = new SimpleKeyListenerHelper(mockComponent);
    }
    
    @Test
    public void testDetach_withNotNullComponent_shouldRemoveKeyListenerFromComponentAndSetComponentToNull() {
        simpleKeyListenerHelper.detach();
        assertNull(mockComponent.getKeyListener());
        assertNull(simpleKeyListenerHelper.getComponent());
    }
    
    @Test
    public void testDetach_withNullComponent_shouldNotThrowException() {
        simpleKeyListenerHelper = new SimpleKeyListenerHelper(null);
        simpleKeyListenerHelper.detach();
        assertNull(simpleKeyListenerHelper.getComponent());
    }
    
    @Test
    public void testNotifyListener_withTypeTen_shouldInvokeEnterOnListenerWithComponent() {
        simpleKeyListenerHelper.notifyListener(mockListener, 10, null);
        assertTrue(mockListener.isEnterInvoked());
        assertEquals(mockComponent, mockListener.getEnteredComponent());
    }
    
    @Test
    public void testNotifyListener_withType155_shouldInvokeInsertOnListenerWithComponent() {
        simpleKeyListenerHelper.notifyListener(mockListener, 155, null);
        assertTrue(mockListener.isInsertInvoked());
        assertEquals(mockComponent, mockListener.getInsertedComponent());
    }
    
    @Test
    public void testNotifyListener_withType127_shouldInvokeDeleteOnListenerWithComponent() {
        simpleKeyListenerHelper.notifyListener(mockListener, 127, null);
        assertTrue(mockListener.isDeleteInvoked());
        assertEquals(mockComponent, mockListener.getDeletedComponent());
    }
    
    @Test
    public void testNotifyListener_withTypeNine_shouldInvokeTabOnListenerWithComponent() {
        simpleKeyListenerHelper.notifyListener(mockListener, 9, null);
        assertTrue(mockListener.isTabInvoked());
        assertEquals(mockComponent, mockListener.getTabbedComponent());
    }
    
    @Test
    public void testKeyPressed_withKeyCodeNine_shouldFireKeyCode() {
        KeyEvent event = new KeyEvent(mockComponent, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_TAB, KeyEvent.CHAR_UNDEFINED);
        simpleKeyListenerHelper.keyPressed(event);
        assertTrue(mockComponent.isFireInvoked());
        assertEquals(KeyEvent.VK_TAB, mockComponent.getFiredKeyCode());
    }
    
    @Test
    public void testKeyPressed_withKeyCodeTen_shouldFireKeyCode() {
        KeyEvent event = new KeyEvent(mockComponent, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, KeyEvent.CHAR_UNDEFINED);
        simpleKeyListenerHelper.keyPressed(event);
        assertTrue(mockComponent.isFireInvoked());
        assertEquals(KeyEvent.VK_ENTER, mockComponent.getFiredKeyCode());
    }
    
    @Test
    public void testKeyPressed_withKeyCode127_shouldFireKeyCode() {
        KeyEvent event = new KeyEvent(mockComponent, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_DELETE, KeyEvent.CHAR_UNDEFINED);
        simpleKeyListenerHelper.keyPressed(event);
        assertTrue(mockComponent.isFireInvoked());
        assertEquals(KeyEvent.VK_DELETE, mockComponent.getFiredKeyCode());
    }
    
    @Test
    public void testKeyPressed_withKeyCode155_shouldFireKeyCode() {
        KeyEvent event = new KeyEvent(mockComponent, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_UNDEFINED, KeyEvent.CHAR_UNDEFINED);
        simpleKeyListenerHelper.keyPressed(event);
        assertTrue(mockComponent.isFireInvoked());
        assertEquals(KeyEvent.VK_UNDEFINED, mockComponent.getFiredKeyCode());
    }
    
    @Test
    public void testKeyReleased_withAnyArgument_shouldNotThrowException() {
        KeyEvent event = new KeyEvent(mockComponent, KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, KeyEvent.VK_UNDEFINED, KeyEvent.CHAR_UNDEFINED);
        simpleKeyListenerHelper.keyReleased(event);
        // No assertions needed as this method has no behavior
    }
    
    @Test
    public void testKeyTyped_withAnyArgument_shouldNotThrowException() {
        KeyEvent event = new KeyEvent(mockComponent, KeyEvent.KEY_TYPED, System.currentTimeMillis(), 0, KeyEvent.VK_UNDEFINED, 'a');
        simpleKeyListenerHelper.keyTyped(event);
        // No assertions needed as this method has no behavior
    }
    
    private class MockComponent extends Component {
        private KeyListener keyListener;
        private boolean fireInvoked;
        private int firedKeyCode;
        
        public MockComponent() {
            super();
            keyListener = null;
            fireInvoked = false;
            firedKeyCode = -1;
        }
        
        @Override
        public void addKeyListener(KeyListener listener) {
            keyListener = listener;
        }
        
        @Override
        public void removeKeyListener(KeyListener listener) {
            keyListener = null;
        }
        
        @Override
        public KeyListener getKeyListeners() {
            return keyListener;
        }
        
        public void fire(int keyCode) {
            fireInvoked = true;
            firedKeyCode = keyCode;
        }
        
        public boolean isFireInvoked() {
            return fireInvoked;
        }
        
        public int getFiredKeyCode() {
            return firedKeyCode;
        }
    }
    
    private class MockListener implements SimpleKeyListener {
        private boolean enterInvoked;
        private Component enteredComponent;
        private boolean insertInvoked;
        private Component insertedComponent;
        private boolean deleteInvoked;
        private Component deletedComponent;
        private boolean tabInvoked;
        private Component tabbedComponent;
        
        public MockListener() {
            enterInvoked = false;
            enteredComponent = null;
            insertInvoked = false;
            insertedComponent = null;
            deleteInvoked = false;
            deletedComponent = null;
            tabInvoked = false;
            tabbedComponent = null;
        }
        
        @Override
        public void enter(Component component) {
            enterInvoked = true;
            enteredComponent = component;
        }
        
        @Override
        public void insert(Component component) {
            insertInvoked = true;
            insertedComponent = component;
        }
        
        @Override
        public void delete(Component component) {
            deleteInvoked = true;
            deletedComponent = component;
        }
        
        @Override
        public void tab(Component component) {
            tabInvoked = true;
            tabbedComponent = component;
        }
        
        public boolean isEnterInvoked() {
            return enterInvoked;
        }
        
        public Component getEnteredComponent() {
            return enteredComponent;
        }
        
        public boolean isInsertInvoked() {
            return insertInvoked;
        }
        
        public Component getInsertedComponent() {
            return insertedComponent;
        }
        
        public boolean isDeleteInvoked() {
            return deleteInvoked;
        }
        
        public Component getDeletedComponent() {
            return deletedComponent;
        }
        
        public boolean isTabInvoked() {
            return tabInvoked;
        }
        
        public Component getTabbedComponent() {
            return tabbedComponent;
        }
    }
}