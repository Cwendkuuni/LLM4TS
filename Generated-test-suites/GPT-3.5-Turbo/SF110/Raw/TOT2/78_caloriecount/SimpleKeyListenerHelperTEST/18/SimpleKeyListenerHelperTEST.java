import org.junit.*;
import static org.junit.Assert.*;
import java.awt.*;
import java.awt.event.*;

public class SimpleKeyListenerHelperTest {

    private SimpleKeyListenerHelper helper;
    private Component mockComponent;

    @Before
    public void setUp() {
        mockComponent = new Component() {};
        helper = new SimpleKeyListenerHelper(mockComponent);
    }

    @Test
    public void testDetach() {
        helper.detach();
        assertNull(helper.component);
    }

    @Test
    public void testNotifyListener_Enter() {
        SimpleKeyListener listener = new SimpleKeyListener();
        helper.notifyListener(listener, 10, null);
        assertEquals("Enter method should be called", 1, listener.enterCallCount);
    }

    @Test
    public void testNotifyListener_Insert() {
        SimpleKeyListener listener = new SimpleKeyListener();
        helper.notifyListener(listener, 155, null);
        assertEquals("Insert method should be called", 1, listener.insertCallCount);
    }

    @Test
    public void testNotifyListener_Delete() {
        SimpleKeyListener listener = new SimpleKeyListener();
        helper.notifyListener(listener, 127, null);
        assertEquals("Delete method should be called", 1, listener.deleteCallCount);
    }

    @Test
    public void testNotifyListener_Tab() {
        SimpleKeyListener listener = new SimpleKeyListener();
        helper.notifyListener(listener, 9, null);
        assertEquals("Tab method should be called", 1, listener.tabCallCount);
    }

    @Test
    public void testKeyPressed() {
        KeyEvent keyEvent = new KeyEvent(mockComponent, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, KeyEvent.CHAR_UNDEFINED);
        helper.keyPressed(keyEvent);
    }

    @Test
    public void testKeyReleased() {
        KeyEvent keyEvent = new KeyEvent(mockComponent, KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, KeyEvent.CHAR_UNDEFINED);
        helper.keyReleased(keyEvent);
    }

    @Test
    public void testKeyTyped() {
        KeyEvent keyEvent = new KeyEvent(mockComponent, KeyEvent.KEY_TYPED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, 'a');
        helper.keyTyped(keyEvent);
    }

    @Test
    public void testKeyPressed_OtherKeys() {
        KeyEvent keyEvent = new KeyEvent(mockComponent, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_BACK_SPACE, KeyEvent.CHAR_UNDEFINED);
        helper.keyPressed(keyEvent);
    }

    @Test
    public void testKeyReleased_OtherKeys() {
        KeyEvent keyEvent = new KeyEvent(mockComponent, KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, KeyEvent.VK_BACK_SPACE, KeyEvent.CHAR_UNDEFINED);
        helper.keyReleased(keyEvent);
    }

    @Test
    public void testKeyTyped_OtherKeys() {
        KeyEvent keyEvent = new KeyEvent(mockComponent, KeyEvent.KEY_TYPED, System.currentTimeMillis(), 0, KeyEvent.VK_BACK_SPACE, 'a');
        helper.keyTyped(keyEvent);
    }
}