import org.junit.*;
import static org.junit.Assert.*;

public class SimpleKeyListenerHelperTest {
    
    private SimpleKeyListenerHelper helper;
    
    @Before
    public void setUp() {
        Component component = new Component();
        helper = new SimpleKeyListenerHelper(component);
    }
    
    @After
    public void tearDown() {
        helper.detach();
    }
    
    @Test
    public void testDetach() {
        helper.detach();
        assertNull(helper.getComponent());
    }
    
    @Test
    public void testNotifyListenerEnter() {
        SimpleKeyListener listener = new SimpleKeyListener();
        helper.notifyListener(listener, 10, null);
        assertTrue(listener.isEnterCalled());
    }
    
    @Test
    public void testNotifyListenerInsert() {
        SimpleKeyListener listener = new SimpleKeyListener();
        helper.notifyListener(listener, 155, null);
        assertTrue(listener.isInsertCalled());
    }
    
    @Test
    public void testNotifyListenerDelete() {
        SimpleKeyListener listener = new SimpleKeyListener();
        helper.notifyListener(listener, 127, null);
        assertTrue(listener.isDeleteCalled());
    }
    
    @Test
    public void testNotifyListenerTab() {
        SimpleKeyListener listener = new SimpleKeyListener();
        helper.notifyListener(listener, 9, null);
        assertTrue(listener.isTabCalled());
    }
    
    @Test
    public void testKeyPressedTab() {
        KeyEvent event = new KeyEvent(new Component(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_TAB);
        helper.keyPressed(event);
        assertEquals(KeyEvent.VK_TAB, event.getKeyCode());
    }
    
    @Test
    public void testKeyPressedEnter() {
        KeyEvent event = new KeyEvent(new Component(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER);
        helper.keyPressed(event);
        assertEquals(KeyEvent.VK_ENTER, event.getKeyCode());
    }
    
    @Test
    public void testKeyPressedDelete() {
        KeyEvent event = new KeyEvent(new Component(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_DELETE);
        helper.keyPressed(event);
        assertEquals(KeyEvent.VK_DELETE, event.getKeyCode());
    }
    
    @Test
    public void testKeyPressedInsert() {
        KeyEvent event = new KeyEvent(new Component(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_INSERT);
        helper.keyPressed(event);
        assertEquals(KeyEvent.VK_INSERT, event.getKeyCode());
    }
    
    @Test
    public void testKeyReleased() {
        KeyEvent event = new KeyEvent(new Component(), KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, KeyEvent.VK_TAB);
        helper.keyReleased(event);
        // No assertions as method does not have any code
    }
    
    @Test
    public void testKeyTyped() {
        KeyEvent event = new KeyEvent(new Component(), KeyEvent.KEY_TYPED, System.currentTimeMillis(), 0, KeyEvent.VK_TAB);
        helper.keyTyped(event);
        // No assertions as method does not have any code
    }
}