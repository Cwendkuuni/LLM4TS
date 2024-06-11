import org.junit.Before;
import org.junit.Test;
import java.awt.*;
import java.awt.event.*;

public class SimpleKeyListenerHelperTest {

    private SimpleKeyListenerHelper helper;
    private TestListener listener;
    private Component component;

    @Before
    public void setUp() {
        component = new Component() {};
        helper = new SimpleKeyListenerHelper(component);
        listener = new TestListener();
        helper.addListener(listener);
    }

    @Test
    public void testDetach() {
        helper.detach();
        // Check that the component is detached and listener is no longer notified
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, ' ');
        component.dispatchEvent(event);
        assertFalse(listener.enterCalled);
    }

    @Test
    public void testNotifyListener_Enter() {
        // Notify listener with type 10
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, ' ');
        component.dispatchEvent(event);
        assertTrue(listener.enterCalled);
    }

    @Test
    public void testNotifyListener_Insert() {
        // Notify listener with type 155
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_INSERT, ' ');
        component.dispatchEvent(event);
        assertTrue(listener.insertCalled);
    }

    @Test
    public void testNotifyListener_Delete() {
        // Notify listener with type 127
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_DELETE, ' ');
        component.dispatchEvent(event);
        assertTrue(listener.deleteCalled);
    }

    @Test
    public void testNotifyListener_Tab() {
        // Notify listener with type 9
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_TAB, ' ');
        component.dispatchEvent(event);
        assertTrue(listener.tabCalled);
    }

    @Test
    public void testKeyPressed_Tab() {
        // Fire key code 9
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_TAB, ' ');
        helper.keyPressed(event);
        assertTrue(listener.tabCalled);
    }

    @Test
    public void testKeyPressed_Enter() {
        // Fire key code 10
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, ' ');
        helper.keyPressed(event);
        assertTrue(listener.enterCalled);
    }

    @Test
    public void testKeyPressed_Delete() {
        // Fire key code 127
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_DELETE, ' ');
        helper.keyPressed(event);
        assertTrue(listener.deleteCalled);
    }

    @Test
    public void testKeyPressed_Insert() {
        // Fire key code 155
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_INSERT, ' ');
        helper.keyPressed(event);
        assertTrue(listener.insertCalled);
    }

    @Test
    public void testKeyReleased() {
        // No assert, just testing method execution
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, KeyEvent.VK_DELETE, ' ');
        helper.keyReleased(event);
    }

    @Test
    public void testKeyTyped() {
        // No assert, just testing method execution
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_TYPED, System.currentTimeMillis(), 0, KeyEvent.VK_DELETE, ' ');
        helper.keyTyped(event);
    }

    private class TestListener implements SimpleKeyListener {
        boolean enterCalled;
        boolean insertCalled;
        boolean deleteCalled;
        boolean tabCalled;

        @Override
        public void enter(Component c) {
            enterCalled = true;
        }

        @Override
        public void insert(Component c) {
            insertCalled = true;
        }

        @Override
        public void delete(Component c) {
            deleteCalled = true;
        }

        @Override
        public void tab(Component c) {
            tabCalled = true;
        }
    }
}