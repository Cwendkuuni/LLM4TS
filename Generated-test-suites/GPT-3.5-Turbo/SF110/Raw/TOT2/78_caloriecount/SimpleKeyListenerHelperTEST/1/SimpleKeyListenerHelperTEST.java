import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.mockito.Mockito;

public class SimpleKeyListenerHelperTest {

    private SimpleKeyListenerHelper simpleKeyListenerHelper;
    private SimpleKeyListener listener;
    private Component component;

    @Before
    public void setUp() {
        component = Mockito.mock(Component.class);
        simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
        listener = Mockito.mock(SimpleKeyListener.class);
    }

    @After
    public void tearDown() {
        simpleKeyListenerHelper = null;
        component = null;
        listener = null;
    }

    @Test
    public void testDetach() {
        simpleKeyListenerHelper.detach();
        assertNull(simpleKeyListenerHelper.component);
    }

    @Test
    public void testNotifyListenerEnter() {
        simpleKeyListenerHelper.notifyListener(listener, 10, null);
        Mockito.verify(listener).enter(component);
    }

    @Test
    public void testNotifyListenerInsert() {
        simpleKeyListenerHelper.notifyListener(listener, 155, null);
        Mockito.verify(listener).insert(component);
    }

    @Test
    public void testNotifyListenerDelete() {
        simpleKeyListenerHelper.notifyListener(listener, 127, null);
        Mockito.verify(listener).delete(component);
    }

    @Test
    public void testNotifyListenerTab() {
        simpleKeyListenerHelper.notifyListener(listener, 9, null);
        Mockito.verify(listener).tab(component);
    }

    @Test
    public void testKeyPressedTab() {
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_TAB, KeyEvent.CHAR_UNDEFINED);
        simpleKeyListenerHelper.keyPressed(event);
        Mockito.verify(component, Mockito.times(1)).fire(event.getKeyCode());
    }

    @Test
    public void testKeyPressedEnter() {
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, KeyEvent.CHAR_UNDEFINED);
        simpleKeyListenerHelper.keyPressed(event);
        Mockito.verify(component, Mockito.times(1)).fire(event.getKeyCode());
    }

    @Test
    public void testKeyPressedDelete() {
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_DELETE, KeyEvent.CHAR_UNDEFINED);
        simpleKeyListenerHelper.keyPressed(event);
        Mockito.verify(component, Mockito.times(1)).fire(event.getKeyCode());
    }

    @Test
    public void testKeyPressedInsert() {
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_INSERT, KeyEvent.CHAR_UNDEFINED);
        simpleKeyListenerHelper.keyPressed(event);
        Mockito.verify(component, Mockito.times(1)).fire(event.getKeyCode());
    }
}