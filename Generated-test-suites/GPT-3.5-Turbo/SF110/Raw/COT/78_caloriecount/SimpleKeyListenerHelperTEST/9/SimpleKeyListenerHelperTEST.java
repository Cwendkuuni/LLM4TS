import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.awt.*;

public class SimpleKeyListenerHelperTest {

    private SimpleKeyListenerHelper helper;
    private Component component;

    @Before
    public void setUp() {
        component = new Component();
        helper = new SimpleKeyListenerHelper(component);
    }

    @Test
    public void testDetach() {
        helper.detach();
        assertNull(helper.getComponent());
    }

    @Test
    public void testNotifyListener_Enter() {
        SimpleKeyListener listener = new SimpleKeyListener();
        helper.notifyListener(listener, 10, null);
        assertEquals("enter", listener.getEvent());
        assertEquals(component, listener.getComponent());
    }

    @Test
    public void testNotifyListener_Insert() {
        SimpleKeyListener listener = new SimpleKeyListener();
        helper.notifyListener(listener, 155, null);
        assertEquals("insert", listener.getEvent());
        assertEquals(component, listener.getComponent());
    }

    @Test
    public void testNotifyListener_Delete() {
        SimpleKeyListener listener = new SimpleKeyListener();
        helper.notifyListener(listener, 127, null);
        assertEquals("delete", listener.getEvent());
        assertEquals(component, listener.getComponent());
    }

    @Test
    public void testNotifyListener_Tab() {
        SimpleKeyListener listener = new SimpleKeyListener();
        helper.notifyListener(listener, 9, null);
        assertEquals("tab", listener.getEvent());
        assertEquals(component, listener.getComponent());
    }

    @Test
    public void testKeyPressed_9() {
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_TAB, ' ');
        helper.keyPressed(event);
        // Check if the event is fired correctly, no assertion regarding the listener is made here
    }

    @Test
    public void testKeyPressed_10() {
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, ' ');
        helper.keyPressed(event);
        // Check if the event is fired correctly, no assertion regarding the listener is made here
    }

    @Test
    public void testKeyPressed_127() {
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_DELETE, ' ');
        helper.keyPressed(event);
        // Check if the event is fired correctly, no assertion regarding the listener is made here
    }

    @Test
    public void testKeyPressed_155() {
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_INSERT, ' ');
        helper.keyPressed(event);
        // Check if the event is fired correctly, no assertion regarding the listener is made here
    }

    @Test
    public void testKeyReleased() {
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, KeyEvent.VK_A, 'a');
        helper.keyReleased(event);
        // No functionality to test, just to check if the method executes without error
    }

    @Test
    public void testKeyTyped() {
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_TYPED, System.currentTimeMillis(), 0, KeyEvent.VK_A, 'a');
        helper.keyTyped(event);
        // No functionality to test, just to check if the method executes without error
    }

}