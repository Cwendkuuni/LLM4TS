import org.junit.Before;
import org.junit.Test;
import java.awt.Component;
import java.awt.event.KeyEvent;

public class SimpleKeyListenerHelperTest {
    
    private SimpleKeyListenerHelper helper;
    private MockComponent mockComponent;
    
    class MockComponent extends Component {
        boolean keyListenerAdded = false;
        boolean keyListenerRemoved = false;
        
        public void addKeyListener(KeyListener l) {
            keyListenerAdded = true;
        }
        
        public void removeKeyListener(KeyListener l) {
            keyListenerRemoved = true;
        }
    }
    
    class MockKeyListener implements SimpleKeyListener {
        boolean enterCalled = false;
        boolean insertCalled = false;
        boolean deleteCalled = false;
        boolean tabCalled = false;
        Component componentParameter = null;
        
        public void enter(Component component) {
            enterCalled = true;
            componentParameter = component;
        }
        
        public void insert(Component component) {
            insertCalled = true;
            componentParameter = component;
        }
        
        public void delete(Component component) {
            deleteCalled = true;
            componentParameter = component;
        }
        
        public void tab(Component component) {
            tabCalled = true;
            componentParameter = component;
        }
    }
    
    @Before
    public void setUp() {
        mockComponent = new MockComponent();
        helper = new SimpleKeyListenerHelper(mockComponent);
    }
    
    @Test
    public void testDetach() {
        helper.detach();
        assert(mockComponent.keyListenerRemoved == true);
        assert(helper.component == null);
    }
    
    @Test
    public void testNotifyListener_Enter() {
        SimpleKeyListener listener = new MockKeyListener();
        helper.notifyListener(listener, 10, null);
        
        assert(((MockKeyListener)listener).enterCalled == true);
        assert(((MockKeyListener)listener).componentParameter == mockComponent);
    }
    
    @Test
    public void testNotifyListener_Insert() {
        SimpleKeyListener listener = new MockKeyListener();
        helper.notifyListener(listener, 155, null);
        
        assert(((MockKeyListener)listener).insertCalled == true);
        assert(((MockKeyListener)listener).componentParameter == mockComponent);
    }
    
    @Test
    public void testNotifyListener_Delete() {
        SimpleKeyListener listener = new MockKeyListener();
        helper.notifyListener(listener, 127, null);
        
        assert(((MockKeyListener)listener).deleteCalled == true);
        assert(((MockKeyListener)listener).componentParameter == mockComponent);
    }
    
    @Test
    public void testNotifyListener_Tab() {
        SimpleKeyListener listener = new MockKeyListener();
        helper.notifyListener(listener, 9, null);
        
        assert(((MockKeyListener)listener).tabCalled == true);
        assert(((MockKeyListener)listener).componentParameter == mockComponent);
    }
    
    @Test
    public void testKeyPressed_Tab() {
        KeyEvent event = new KeyEvent(mockComponent, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_TAB);
        helper.keyPressed(event);
        
        SimpleKeyListener listener = new MockKeyListener();
        helper.notifyListener(listener, event.getKeyCode(), null);
        
        assert(((MockKeyListener)listener).tabCalled == true);
        assert(((MockKeyListener)listener).componentParameter == mockComponent);
    }
    
    @Test
    public void testKeyPressed_Enter() {
        KeyEvent event = new KeyEvent(mockComponent, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER);
        helper.keyPressed(event);
        
        SimpleKeyListener listener = new MockKeyListener();
        helper.notifyListener(listener, event.getKeyCode(), null);
        
        assert(((MockKeyListener)listener).enterCalled == true);
        assert(((MockKeyListener)listener).componentParameter == mockComponent);
    }
    
    @Test
    public void testKeyPressed_Delete() {
        KeyEvent event = new KeyEvent(mockComponent, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_DELETE);
        helper.keyPressed(event);
        
        SimpleKeyListener listener = new MockKeyListener();
        helper.notifyListener(listener, event.getKeyCode(), null);
        
        assert(((MockKeyListener)listener).deleteCalled == true);
        assert(((MockKeyListener)listener).componentParameter == mockComponent);
    }
    
    @Test
    public void testKeyPressed_Insert() {
        KeyEvent event = new KeyEvent(mockComponent, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_INSERT);
        helper.keyPressed(event);
        
        SimpleKeyListener listener = new MockKeyListener();
        helper.notifyListener(listener, event.getKeyCode(), null);
        
        assert(((MockKeyListener)listener).insertCalled == true);
        assert(((MockKeyListener)listener).componentParameter == mockComponent);
    }
    
    @Test
    public void testKeyReleased() {
        KeyEvent event = new KeyEvent(mockComponent, KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, KeyEvent.VK_A);
        helper.keyReleased(event);
    }
    
    @Test
    public void testKeyTyped() {
        KeyEvent event = new KeyEvent(mockComponent, KeyEvent.KEY_TYPED, System.currentTimeMillis(), 0, KeyEvent.VK_A);
        helper.keyTyped(event);
    }
}