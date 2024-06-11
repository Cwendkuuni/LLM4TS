
import static org.junit.Assert.*;
import org.junit.*;
import java.awt.*;
import java.awt.event.*;

public class SimpleKeyListenerHelperTest {

    private static final Component TEST_COMPONENT = new Component() {};

    private SimpleKeyListenerHelper helper;

    @Before
    public void setUp() {
        helper = new SimpleKeyListenerHelper(TEST_COMPONENT);
    }

    @Test
    public void testDetach() {
        helper.detach();
        assertNull(helper.component);
    }

    @Test
    public void testNotifyListenerEnter() {
        SimpleKeyListener mockListener = new SimpleKeyListener(){};
        helper.notifyListener(mockListener, 10, null);
        // Assert that "enter" method of listener is called
        // with the correct argument
        assertTrue(mockListener.enterCalled);
        assertEquals(TEST_COMPONENT, mockListener.enterComponent);
    }

    @Test
    public void testNotifyListenerInsert() {
        SimpleKeyListener mockListener = new SimpleKeyListener(){};
        helper.notifyListener(mockListener, 155, null);
        // Assert that "insert" method of listener is called
        // with the correct argument
        assertTrue(mockListener.insertCalled);
        assertEquals(TEST_COMPONENT, mockListener.insertComponent);
    }

    @Test
    public void testNotifyListenerDelete() {
        SimpleKeyListener mockListener = new SimpleKeyListener(){};
        helper.notifyListener(mockListener, 127, null);
        // Assert that "delete" method of listener is called
        // with the correct argument
        assertTrue(mockListener.deleteCalled);
        assertEquals(TEST_COMPONENT, mockListener.deleteComponent);
    }

    @Test
    public void testNotifyListenerTab() {
        SimpleKeyListener mockListener = new SimpleKeyListener(){};
        helper.notifyListener(mockListener, 9, null);
        // Assert that "tab" method of listener is called
        // with the correct argument
        assertTrue(mockListener.tabCalled);
        assertEquals(TEST_COMPONENT, mockListener.tabComponent);
    }

    @Test
    public void testKeyPressedWithValidKeyCode() {
        helper.keyPressed(new KeyEvent(TEST_COMPONENT, KeyEvent.KEY_PRESSED, 0,
                0, KeyEvent.VK_ENTER, KeyEvent.CHAR_UNDEFINED));

        // Assert that correct event is fired
        assertTrue(helper.fireCalled);
        assertEquals(KeyEvent.VK_ENTER, helper.firedKeyCode);
    }

    @Test
    public void testKeyPressedWithInvalidKeyCode() {
        helper.keyPressed(new KeyEvent(TEST_COMPONENT, KeyEvent.KEY_PRESSED, 0,
                0, KeyEvent.VK_A, KeyEvent.CHAR_UNDEFINED));

        // Assert that no event is fired
        assertFalse(helper.fireCalled);
    }

    @Test
    public void testKeyReleased() {
        helper.keyReleased(new KeyEvent(TEST_COMPONENT, KeyEvent.KEY_RELEASED, 0,
                0, KeyEvent.VK_ENTER, KeyEvent.CHAR_UNDEFINED));
        // No specific behavior to test for keyReleased.
        // We just need to ensure there are no exceptions thrown.
    }

    @Test
    public void testKeyTyped() {
        helper.keyTyped(new KeyEvent(TEST_COMPONENT, KeyEvent.KEY_TYPED, 0,
                0, KeyEvent.VK_UNDEFINED, 'a'));
        // No specific behavior to test for keyTyped.
        // We just need to ensure there are no exceptions thrown.
    }
}

