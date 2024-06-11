import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleKeyListenerHelperTest {

    @Test
    public void testDetach() {
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        helper.detach();
        // Verify that component is null after detach
        assertNull(helper.component);
    }

    @Test
    public void testNotifyListener_Enter() {
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        SimpleKeyListener listener = new SimpleKeyListener();
        helper.notifyListener(listener, 10, null);
        // Verify that enter method is called on the listener
        assertTrue(listener.enterCalled);
    }

    @Test
    public void testNotifyListener_Insert() {
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        SimpleKeyListener listener = new SimpleKeyListener();
        helper.notifyListener(listener, 155, null);
        // Verify that insert method is called on the listener
        assertTrue(listener.insertCalled);
    }

    @Test
    public void testNotifyListener_Delete() {
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        SimpleKeyListener listener = new SimpleKeyListener();
        helper.notifyListener(listener, 127, null);
        // Verify that delete method is called on the listener
        assertTrue(listener.deleteCalled);
    }

    @Test
    public void testNotifyListener_Tab() {
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        SimpleKeyListener listener = new SimpleKeyListener();
        helper.notifyListener(listener, 9, null);
        // Verify that tab method is called on the listener
        assertTrue(listener.tabCalled);
    }

    @Test
    public void testKeyPressed_Tab() {
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, 0, 9, KeyEvent.VK_UNDEFINED);
        helper.keyPressed(event);
        // Verify that fire method is called with the key code 9
        assertTrue(helper.fireCalledWithKeyCode == 9);
    }

    @Test
    public void testKeyPressed_Enter() {
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, 0, 10, KeyEvent.VK_UNDEFINED);
        helper.keyPressed(event);
        // Verify that fire method is called with the key code 10
        assertTrue(helper.fireCalledWithKeyCode == 10);
    }

    @Test
    public void testKeyPressed_Delete() {
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, 0, 127, KeyEvent.VK_UNDEFINED);
        helper.keyPressed(event);
        // Verify that fire method is called with the key code 127
        assertTrue(helper.fireCalledWithKeyCode == 127);
    }

    @Test
    public void testKeyPressed_Insert() {
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, 0, 155, KeyEvent.VK_UNDEFINED);
        helper.keyPressed(event);
        // Verify that fire method is called with the key code 155
        assertTrue(helper.fireCalledWithKeyCode == 155);
    }
}