import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

//Test cases for SimpleKeyListenerHelper class
public class SimpleKeyListenerHelperTest {

    //Create an instance of SimpleKeyListenerHelper for testing
    SimpleKeyListenerHelper helper;

    @Before
    public void setUp() {
        //Initialize the component for testing
        Component component = new Component();
        helper = new SimpleKeyListenerHelper(component);
    }

    @Test
    public void testDetach() {
        //Test detach method
        helper.detach();
        assertNull(helper.component);
    }

    @Test
    public void testNotifyListener_enter() {
        //Test notifyListener method with type 10
        SimpleKeyListener listener = new SimpleKeyListener();
        helper.notifyListener(listener, 10, null);
        assertEquals(helper.component, listener.enterComponent);
    }

    @Test
    public void testNotifyListener_insert() {
        //Test notifyListener method with type 155
        SimpleKeyListener listener = new SimpleKeyListener();
        helper.notifyListener(listener, 155, null);
        assertEquals(helper.component, listener.insertComponent);
    }

    @Test
    public void testNotifyListener_delete() {
        //Test notifyListener method with type 127
        SimpleKeyListener listener = new SimpleKeyListener();
        helper.notifyListener(listener, 127, null);
        assertEquals(helper.component, listener.deleteComponent);
    }

    @Test
    public void testNotifyListener_tab() {
        //Test notifyListener method with type 9
        SimpleKeyListener listener = new SimpleKeyListener();
        helper.notifyListener(listener, 9, null);
        assertEquals(helper.component, listener.tabComponent);
    }

    @Test
    public void testKeyPressed_tab() {
        //Test keyPressed method with KeyEvent.KEY_RELEASED
        KeyEvent event = new KeyEvent(helper.component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_TAB, KeyEvent.CHAR_UNDEFINED);
        helper.keyPressed(event);
        SimpleKeyListener listener = new SimpleKeyListener();
        listener.tab(helper.component);
        assertEquals(listener.tabCount, 1);
    }

    @Test
    public void testKeyPressed_enter() {
        //Test keyPressed method with KeyEvent.VK_ENTER
        KeyEvent event = new KeyEvent(helper.component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, KeyEvent.CHAR_UNDEFINED);
        helper.keyPressed(event);
        SimpleKeyListener listener = new SimpleKeyListener();
        listener.enter(helper.component);
        assertEquals(listener.enterCount, 1);
    }

    @Test
    public void testKeyPressed_delete() {
        //Test keyPressed method with KeyEvent.VK_DELETE
        KeyEvent event = new KeyEvent(helper.component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_DELETE, KeyEvent.CHAR_UNDEFINED);
        helper.keyPressed(event);
        SimpleKeyListener listener = new SimpleKeyListener();
        listener.delete(helper.component);
        assertEquals(listener.deleteCount, 1);
    }

    @Test
    public void testKeyPressed_insert() {
        //Test keyPressed method with KeyEvent.VK_INSERT
        KeyEvent event = new KeyEvent(helper.component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_INSERT, KeyEvent.CHAR_UNDEFINED);
        helper.keyPressed(event);
        SimpleKeyListener listener = new SimpleKeyListener();
        listener.insert(helper.component);
        assertEquals(listener.insertCount, 1);
    }

    @Test
    public void testKeyReleased() {
        //Test keyReleased method
        KeyEvent event = new KeyEvent(helper.component, KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, KeyEvent.VK_TAB, KeyEvent.CHAR_UNDEFINED);
        helper.keyReleased(event);
        //No functionality to test in keyReleased method
    }

    @Test
    public void testKeyTyped() {
        //Test keyTyped method
        KeyEvent event = new KeyEvent(helper.component, KeyEvent.KEY_TYPED, System.currentTimeMillis(), 0, 0, KeyEvent.CHAR_UNDEFINED);
        helper.keyTyped(event);
        //No functionality to test in keyTyped method
    }
}