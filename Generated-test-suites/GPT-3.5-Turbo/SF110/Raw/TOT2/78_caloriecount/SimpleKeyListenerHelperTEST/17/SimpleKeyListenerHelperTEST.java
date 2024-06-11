
import static org.junit.Assert.*;
import org.junit.Test;

public class SimpleKeyListenerHelperTest {

    @Test
    public void testSimpleKeyListenerHelperConstructor() {
        // Create a new SimpleKeyListenerHelper object
        Component component = new Component();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        assertNotNull(helper);
    }

    @Test
    public void testDetach() {
        // Test the detach method
        Component component = new Component();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        helper.detach();
        assertNull(helper.component);
    }

    @Test
    public void testNotifyListener() {
        // Test the notifyListener method with different types
        Component component = new Component();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        // Test case 10
        helper.notifyListener(new SimpleKeyListener(), 10, null);
        // Test case 155
        helper.notifyListener(new SimpleKeyListener(), 155, null);
        // Test case 127
        helper.notifyListener(new SimpleKeyListener(), 127, null);
        // Test case 9
        helper.notifyListener(new SimpleKeyListener(), 9, null);
    }

    @Test
    public void testKeyPressed() {
        // Test the keyPressed method with different key codes
        Component component = new Component();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        // Test case 9
        KeyEvent event1 = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_TAB, KeyEvent.CHAR_UNDEFINED);
        helper.keyPressed(event1);
        // Test case 10
        KeyEvent event2 = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, KeyEvent.CHAR_UNDEFINED);
        helper.keyPressed(event2);
        // Test case 127
        KeyEvent event3 = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_DELETE, KeyEvent.CHAR_UNDEFINED);
        helper.keyPressed(event3);
        // Test case 155
        KeyEvent event4 = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_UNDEFINED, 'i');
        helper.keyPressed(event4);
    }

}
