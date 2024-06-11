import org.junit.*;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class SimpleKeyListenerHelperTest {

    private SimpleKeyListenerHelper helper;
    private Component component;

    @Before
    public void setup() {
        component = new Component();
        helper = new SimpleKeyListenerHelper(component);
    }

    @Test
    public void testDetach() {
        helper.detach();
        assertNull(helper.component);
    }

    @Test
    public void testNotifyListenerEnter() {
        SimpleKeyListener listener = new SimpleKeyListener();
        helper.notifyListener(listener, 10, null);
        // Check if enter method is called on listener
    }

    @Test
    public void testNotifyListenerInsert() {
        SimpleKeyListener listener = new SimpleKeyListener();
        helper.notifyListener(listener, 155, null);
        // Check if insert method is called on listener
    }

    @Test
    public void testNotifyListenerDelete() {
        SimpleKeyListener listener = new SimpleKeyListener();
        helper.notifyListener(listener, 127, null);
        // Check if delete method is called on listener
    }

    @Test
    public void testNotifyListenerTab() {
        SimpleKeyListener listener = new SimpleKeyListener();
        helper.notifyListener(listener, 9, null);
        // Check if tab method is called on listener
    }

    @Test
    public void testKeyPressedEnter() {
        KeyEvent event = new KeyEvent(component, 0, 0, 0, KeyEvent.VK_ENTER);
        helper.keyPressed(event);
        // Check if fire method is called for enter key
    }

    @Test
    public void testKeyPressedTab() {
        KeyEvent event = new KeyEvent(component, 0, 0, 0, KeyEvent.VK_TAB);
        helper.keyPressed(event);
        // Check if fire method is called for tab key
    }

    @Test
    public void testKeyPressedDelete() {
        KeyEvent event = new KeyEvent(component, 0, 0, 0, KeyEvent.VK_DELETE);
        helper.keyPressed(event);
        // Check if fire method is called for delete key
    }

    @Test
    public void testKeyPressedInsert() {
        KeyEvent event = new KeyEvent(component, 0, 0, 0, KeyEvent.VK_INSERT);
        helper.keyPressed(event);
        // Check if fire method is called for insert key
    }

}