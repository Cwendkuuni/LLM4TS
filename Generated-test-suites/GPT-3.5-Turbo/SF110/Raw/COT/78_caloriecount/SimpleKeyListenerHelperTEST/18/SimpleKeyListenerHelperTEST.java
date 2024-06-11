
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.awt.Component;
import java.awt.event.KeyEvent;
import com.lts.event.SimpleKeyListener;
import com.lts.event.SimpleKeyListenerHelper;

public class SimpleKeyListenerHelperTest {

    private Component component;
    private SimpleKeyListenerHelper listenerHelper;

    @Before
    public void setUp() {
        component = new Component() {};
        listenerHelper = new SimpleKeyListenerHelper(component);
    }

    @Test
    public void testDetach() {
        listenerHelper.detach();
        Assert.assertNull(listenerHelper.component);
    }

    @Test
    public void testNotifyListenerEnter() {
        Object o = new SimpleKeyListener() {
            public void enter(Component component) {
                Assert.assertEquals(component, listenerHelper.component);
            }
            public void insert(Component component) {}
            public void delete(Component component) {}
            public void tab(Component component) {}
        };
        listenerHelper.notifyListener(o, 10, null);
    }

    @Test
    public void testNotifyListenerInsert() {
        Object o = new SimpleKeyListener() {
            public void enter(Component component) {}
            public void insert(Component component) {
                Assert.assertEquals(component, listenerHelper.component);
            }
            public void delete(Component component) {}
            public void tab(Component component) {}
        };
        listenerHelper.notifyListener(o, 155, null);
    }

    @Test
    public void testNotifyListenerDelete() {
        Object o = new SimpleKeyListener() {
            public void enter(Component component) {}
            public void insert(Component component) {}
            public void delete(Component component) {
                Assert.assertEquals(component, listenerHelper.component);
            }
            public void tab(Component component) {}
        };
        listenerHelper.notifyListener(o, 127, null);
    }

    @Test
    public void testNotifyListenerTab() {
        Object o = new SimpleKeyListener() {
            public void enter(Component component) {}
            public void insert(Component component) {}
            public void delete(Component component) {}
            public void tab(Component component) {
                Assert.assertEquals(component, listenerHelper.component);
            }
        };
        listenerHelper.notifyListener(o, 9, null);
    }

    @Test
    public void testKeyPressedTab() {
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_TAB, KeyEvent.CHAR_UNDEFINED);
        listenerHelper.keyPressed(event);
    }

    @Test
    public void testKeyPressedEnter() {
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, KeyEvent.CHAR_UNDEFINED);
        listenerHelper.keyPressed(event);
    }

    @Test
    public void testKeyPressedDelete() {
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_DELETE, KeyEvent.CHAR_UNDEFINED);
        listenerHelper.keyPressed(event);
    }

    @Test
    public void testKeyPressedInsert() {
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_INSERT, KeyEvent.CHAR_UNDEFINED);
        listenerHelper.keyPressed(event);
    }

    @Test
    public void testKeyReleased() {
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, KeyEvent.VK_A, KeyEvent.CHAR_UNDEFINED);
        listenerHelper.keyReleased(event);
    }

    @Test
    public void testKeyTyped() {
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_TYPED, System.currentTimeMillis(), 0, KeyEvent.VK_A, KeyEvent.CHAR_UNDEFINED);
        listenerHelper.keyTyped(event);
    }
}

