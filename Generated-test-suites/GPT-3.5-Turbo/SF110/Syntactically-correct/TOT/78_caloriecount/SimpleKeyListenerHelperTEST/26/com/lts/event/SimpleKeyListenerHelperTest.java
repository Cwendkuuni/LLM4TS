import static org.junit.Assert.*;
import org.junit.*;

public class SimpleKeyListenerHelperTest {

    private SimpleKeyListenerHelper helper;

    @Before
    public void setUp() {
        helper = new SimpleKeyListenerHelper(new Component());
    }

    @After
    public void tearDown() {
        helper.detach();
    }

    @Test
    public void testDetach() {
        assertNotNull(helper.getComponent()); 

        helper.detach();

        assertNull(helper.getComponent());
    }

    @Test
    public void testNotifyListener_Enter() {
        SimpleKeyListener listener = new SimpleKeyListener();

        helper.notifyListener(listener, 10, null);

        assertTrue(listener.enterCalled);
        assertFalse(listener.insertCalled);
        assertFalse(listener.deleteCalled);
        assertFalse(listener.tabCalled);
    }

    @Test
    public void testNotifyListener_Insert() {
        SimpleKeyListener listener = new SimpleKeyListener();

        helper.notifyListener(listener, 155, null);

        assertFalse(listener.enterCalled);
        assertTrue(listener.insertCalled);
        assertFalse(listener.deleteCalled);
        assertFalse(listener.tabCalled);
    }

    @Test
    public void testNotifyListener_Delete() {
        SimpleKeyListener listener = new SimpleKeyListener();

        helper.notifyListener(listener, 127, null);

        assertFalse(listener.enterCalled);
        assertFalse(listener.insertCalled);
        assertTrue(listener.deleteCalled);
        assertFalse(listener.tabCalled);
    }

    @Test
    public void testNotifyListener_Tab() {
        SimpleKeyListener listener = new SimpleKeyListener();

        helper.notifyListener(listener, 9, null);

        assertFalse(listener.enterCalled);
        assertFalse(listener.insertCalled);
        assertFalse(listener.deleteCalled);
        assertTrue(listener.tabCalled);
    }

    @Test
    public void testKeyPressed_Enter() {
        KeyEvent event = new KeyEvent(new Component(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, '\0');

        helper.keyPressed(event);

        SimpleKeyListener listener = (SimpleKeyListener) helper.getListeners().get(0);

        assertTrue(listener.enterCalled);
        assertFalse(listener.insertCalled);
        assertFalse(listener.deleteCalled);
        assertFalse(listener.tabCalled);
    }

    @Test
    public void testKeyPressed_Insert() {
        KeyEvent event = new KeyEvent(new Component(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_INSERT, '\0');

        helper.keyPressed(event);

        SimpleKeyListener listener = (SimpleKeyListener) helper.getListeners().get(0);

        assertFalse(listener.enterCalled);
        assertTrue(listener.insertCalled);
        assertFalse(listener.deleteCalled);
        assertFalse(listener.tabCalled);
    }

    @Test
    public void testKeyPressed_Delete() {
        KeyEvent event = new KeyEvent(new Component(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_DELETE, '\0');

        helper.keyPressed(event);

        SimpleKeyListener listener = (SimpleKeyListener) helper.getListeners().get(0);

        assertFalse(listener.enterCalled);
        assertFalse(listener.insertCalled);
        assertTrue(listener.deleteCalled);
        assertFalse(listener.tabCalled);
    }

    @Test
    public void testKeyPressed_Tab() {
        KeyEvent event = new KeyEvent(new Component(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_TAB, '\0');

        helper.keyPressed(event);

        SimpleKeyListener listener = (SimpleKeyListener) helper.getListeners().get(0);

        assertFalse(listener.enterCalled);
        assertFalse(listener.insertCalled);
        assertFalse(listener.deleteCalled);
        assertTrue(listener.tabCalled);
    }

    @Test
    public void testKeyReleased() {
        KeyEvent event = new KeyEvent(new Component(), KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, '\0');

        helper.keyReleased(event);

        // No action performed, no listener checks needed
    }

    @Test
    public void testKeyTyped() {
        KeyEvent event = new KeyEvent(new Component(), KeyEvent.KEY_TYPED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, '\0');

        helper.keyTyped(event);

        // No action performed, no listener checks needed
    }

}