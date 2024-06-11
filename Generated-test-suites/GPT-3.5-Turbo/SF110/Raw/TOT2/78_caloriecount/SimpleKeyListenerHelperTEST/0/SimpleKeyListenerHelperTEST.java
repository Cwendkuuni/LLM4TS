import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

public class SimpleKeyListenerHelperTest {

    private SimpleKeyListenerHelper keyListenerHelper;
    private Component component;

    @Before
    public void setUp() {
        component = new Component();
        keyListenerHelper = new SimpleKeyListenerHelper(component);
    }

    @After
    public void tearDown() {
        keyListenerHelper.detach();
    }

    @Test
    public void testDetach() {
        keyListenerHelper.detach();
        assertNull(keyListenerHelper.component);
    }

    @Test
    public void testNotifyListener_Enter() {
        SimpleKeyListener listener = new SimpleKeyListener();
        keyListenerHelper.notifyListener(listener, 10, null);
        // Verify that enter method is called on the listener
        assertTrue(listener.enterCalled);
    }

    @Test
    public void testNotifyListener_Insert() {
        SimpleKeyListener listener = new SimpleKeyListener();
        keyListenerHelper.notifyListener(listener, 155, null);
        // Verify that insert method is called on the listener
        assertTrue(listener.insertCalled);
    }

    @Test
    public void testNotifyListener_Delete() {
        SimpleKeyListener listener = new SimpleKeyListener();
        keyListenerHelper.notifyListener(listener, 127, null);
        // Verify that delete method is called on the listener
        assertTrue(listener.deleteCalled);
    }

    @Test
    public void testNotifyListener_Tab() {
        SimpleKeyListener listener = new SimpleKeyListener();
        keyListenerHelper.notifyListener(listener, 9, null);
        // Verify that tab method is called on the listener
        assertTrue(listener.tabCalled);
    }

    @Test
    public void testKeyPressed_FireEvent() {
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, 'a');
        keyListenerHelper.keyPressed(event);
        // Verify that the event key code triggers the fire method
        assertTrue(keyListenerHelper.fireCalled);
    }
    
    @Test
    public void testKeyPressed_NonFireEvent() {
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_SPACE, 'a');
        keyListenerHelper.keyPressed(event);
        // Verify that the event key code does not trigger the fire method
        assertFalse(keyListenerHelper.fireCalled);
    }
}