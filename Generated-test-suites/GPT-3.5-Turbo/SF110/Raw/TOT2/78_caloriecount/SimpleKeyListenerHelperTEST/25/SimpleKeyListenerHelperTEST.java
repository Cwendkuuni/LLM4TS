import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.Component;
import java.awt.event.KeyEvent;

public class SimpleKeyListenerHelperTest {

    private SimpleKeyListenerHelper simpleKeyListenerHelper;
    private MockComponent component;

    @Before
    public void setUp() {
        component = new MockComponent();
        simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
    }

    @Test
    public void testDetach() {
        simpleKeyListenerHelper.detach();
        assertNull(component.getKeyListener());
    }

    @Test
    public void testNotifyListener() {
        // Edge case: type = 10
        simpleKeyListenerHelper.notifyListener(new SimpleKeyListener(), 10, null);

        // Edge case: type = 155
        simpleKeyListenerHelper.notifyListener(new SimpleKeyListener(), 155, null);

        // Edge case: type = 127
        simpleKeyListenerHelper.notifyListener(new SimpleKeyListener(), 127, null);

        // Edge case: type = 9
        simpleKeyListenerHelper.notifyListener(new SimpleKeyListener(), 9, null);
    }

    @Test
    public void testKeyPressed() {
        // Edge case: keyCode = 9
        simpleKeyListenerHelper.keyPressed(new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_TAB, KeyEvent.CHAR_UNDEFINED));
        
        // Edge case: keyCode = 10
        simpleKeyListenerHelper.keyPressed(new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, KeyEvent.CHAR_UNDEFINED));
        
        // Edge case: keyCode = 127
        simpleKeyListenerHelper.keyPressed(new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_DELETE, KeyEvent.CHAR_UNDEFINED));
        
        // Edge case: keyCode = 155
        simpleKeyListenerHelper.keyPressed(new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, 155, KeyEvent.CHAR_UNDEFINED));
    }

    class MockComponent extends Component {
        private KeyListener keyListener;
        
        @Override
        public synchronized void addKeyListener(KeyListener l) {
            keyListener = l;
        }

        @Override
        public synchronized void removeKeyListener(KeyListener l) {
            keyListener = null;
        }

        public KeyListener getKeyListener() {
            return keyListener;
        }
    }
}