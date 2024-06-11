import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.event.KeyEvent;

public class SimpleKeyListenerHelperTest {
    private SimpleKeyListenerHelper simpleKeyListenerHelper;
    private MockComponent component;

    @Before
    public void setUp() {
        component = new MockComponent();
        simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
    }

    @After
    public void tearDown() {
        simpleKeyListenerHelper.detach();
    }

    @Test
    public void testDetach() {
        ActionListener[] listeners = component.getKeyListeners();
        assertTrue(listeners.length > 0);

        simpleKeyListenerHelper.detach();

        listeners = component.getKeyListeners();
        assertEquals(0, listeners.length);
        assertNull(simpleKeyListenerHelper.component);
    }

    @Test
    public void testNotifyListener_enter() {
        SimpleKeyListener listener = new SimpleKeyListener() {
            @Override
            public void enter(Component component) {
                assertEquals(simpleKeyListenerHelper.component, component);
            }
        };

        simpleKeyListenerHelper.notifyListener(listener, 10, null);
    }

    @Test
    public void testNotifyListener_insert() {
        SimpleKeyListener listener = new SimpleKeyListener() {
            @Override
            public void insert(Component component) {
                assertEquals(simpleKeyListenerHelper.component, component);
            }
        };

        simpleKeyListenerHelper.notifyListener(listener, 155, null);
    }

    @Test
    public void testNotifyListener_delete() {
        SimpleKeyListener listener = new SimpleKeyListener() {
            @Override
            public void delete(Component component) {
                assertEquals(simpleKeyListenerHelper.component, component);
            }
        };

        simpleKeyListenerHelper.notifyListener(listener, 127, null);
    }

    @Test
    public void testNotifyListener_tab() {
        SimpleKeyListener listener = new SimpleKeyListener() {
            @Override
            public void tab(Component component) {
                assertEquals(simpleKeyListenerHelper.component, component);
            }
        };

        simpleKeyListenerHelper.notifyListener(listener, 9, null);
    }

    @Test
    public void testKeyPressed_tab() {
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, 0, 0, KeyEvent.VK_TAB, KeyEvent.CHAR_UNDEFINED);

        simpleKeyListenerHelper.keyPressed(event);

        //assert action fired
    }

    @Test
    public void testKeyPressed_enter() {
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, 0, 0, KeyEvent.VK_ENTER,
                KeyEvent.CHAR_UNDEFINED);

        simpleKeyListenerHelper.keyPressed(event);

        //assert action fired
    }

    @Test
    public void testKeyPressed_delete() {
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, 0, 0, KeyEvent.VK_DELETE,
                KeyEvent.CHAR_UNDEFINED);

        simpleKeyListenerHelper.keyPressed(event);

        //assert action fired
    }

    @Test
    public void testKeyPressed_insert() {
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, 0, 0, KeyEvent.VK_INSERT,
                KeyEvent.CHAR_UNDEFINED);

        simpleKeyListenerHelper.keyPressed(event);

        //assert action fired
    }

    @Test
    public void testKeyReleased() {
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_RELEASED, 0, 0, KeyEvent.VK_INSERT,
                KeyEvent.CHAR_UNDEFINED);

        simpleKeyListenerHelper.keyReleased(event);

        //assert no action fired
    }

    @Test
    public void testKeyTyped() {
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_TYPED, 0, 0, KeyEvent.VK_UNDEFINED, 'a');

        simpleKeyListenerHelper.keyTyped(event);

        //assert no action fired
    }

    //MockComponent class for testing purposes
    private class MockComponent extends Component {
    }
}