import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.awt.Component;
import java.awt.event.KeyEvent;

public class SimpleKeyListenerHelperTest {

    private SimpleKeyListenerHelper helper;
    private MockComponent component;

    @Before
    public void setup() {
        component = new MockComponent();
        helper = new SimpleKeyListenerHelper(component);
    }

    @Test
    public void testDetach() {
        helper.detach();
        assertNull(component.getKeyListener());
    }

    @Test
    public void testNotifyListenerEnter() {
        SimpleKeyListener listener = new SimpleKeyListener();
        helper.notifyListener(listener, 10, null);
        assertTrue(component.enterCalled);
    }

    @Test
    public void testNotifyListenerInsert() {
        SimpleKeyListener listener = new SimpleKeyListener();
        helper.notifyListener(listener, 155, null);
        assertTrue(component.insertCalled);
    }

    @Test
    public void testNotifyListenerDelete() {
        SimpleKeyListener listener = new SimpleKeyListener();
        helper.notifyListener(listener, 127, null);
        assertTrue(component.deleteCalled);
    }

    @Test
    public void testNotifyListenerTab() {
        SimpleKeyListener listener = new SimpleKeyListener();
        helper.notifyListener(listener, 9, null);
        assertTrue(component.tabCalled);
    }

    @Test
    public void testKeyPressed() {
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_TAB, 'a');
        helper.keyPressed(event);
        assertTrue(component.fireCalled);
    }

    @Test
    public void testKeyReleased() {
        // No functionality to test here, leaving empty
    }

    @Test
    public void testKeyTyped() {
        // No functionality to test here, leaving empty
    }

    // Mock Component class for testing
    private class MockComponent extends Component {
        private KeyListener keyListener;
        public boolean enterCalled, insertCalled, deleteCalled, tabCalled, fireCalled;

        @Override
        public void addKeyListener(KeyListener l) {
            keyListener = l;
        }

        @Override
        public void removeKeyListener(KeyListener l) {
            keyListener = null;
        }

        // Methods called by notifyListener
        public void enter(Component c) {
            enterCalled = true;
        }

        public void insert(Component c) {
            insertCalled = true;
        }

        public void delete(Component c) {
            deleteCalled = true;
        }

        public void tab(Component c) {
            tabCalled = true;
        }

        public KeyListener getKeyListener() {
            return keyListener;
        }

        // Method called by keyPressed
        public void fire(int keyCode) {
            fireCalled = true;
        }
    }
}