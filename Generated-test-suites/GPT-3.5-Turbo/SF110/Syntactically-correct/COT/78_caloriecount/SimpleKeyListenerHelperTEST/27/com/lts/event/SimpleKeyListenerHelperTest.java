import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.awt.*;
import java.awt.event.*;

public class SimpleKeyListenerHelperTest {

    private SimpleKeyListenerHelper helper;
    private Component component;

    @Before
    public void setUp() {
        component = new Component() {};
        helper = new SimpleKeyListenerHelper(component);
    }

    @Test
    public void testDetach() {
        helper.detach();
        Assert.assertNull(component.getKeyListeners());
    }

    @Test
    public void testNotifyListenerEnter() {
        SimpleKeyListener listener = new SimpleKeyListener() {

            boolean isCalled = false;

            @Override
            public void enter(Component component) {
                isCalled = true;
            }

            @Override
            public void insert(Component component) {
            }

            @Override
            public void delete(Component component) {
            }

            @Override
            public void tab(Component component) {
            }
        };

        helper.notifyListener(listener, 10, null);
        
        Assert.assertTrue(listener.isCalled);
    }

    @Test
    public void testNotifyListenerInsert() {
        SimpleKeyListener listener = new SimpleKeyListener() {

            boolean isCalled = false;

            @Override
            public void enter(Component component) {
            }

            @Override
            public void insert(Component component) {
                isCalled = true;
            }

            @Override
            public void delete(Component component) {
            }

            @Override
            public void tab(Component component) {
            }
        };

        helper.notifyListener(listener, 155, null);

        Assert.assertTrue(listener.isCalled);
    }

    @Test
    public void testNotifyListenerDelete() {
        SimpleKeyListener listener = new SimpleKeyListener() {

            boolean isCalled = false;

            @Override
            public void enter(Component component) {
            }

            @Override
            public void insert(Component component) {
            }

            @Override
            public void delete(Component component) {
                isCalled = true;
            }

            @Override
            public void tab(Component component) {
            }
        };

        helper.notifyListener(listener, 127, null);

        Assert.assertTrue(listener.isCalled);
    }

    @Test
    public void testNotifyListenerTab() {
        SimpleKeyListener listener = new SimpleKeyListener() {

            boolean isCalled = false;

            @Override
            public void enter(Component component) {
            }

            @Override
            public void insert(Component component) {
            }

            @Override
            public void delete(Component component) {
            }

            @Override
            public void tab(Component component) {
                isCalled = true;
            }
        };

        helper.notifyListener(listener, 9, null);

        Assert.assertTrue(listener.isCalled);
    }

    @Test
    public void testKeyPressedEnter() {
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, KeyEvent.CHAR_UNDEFINED);
        helper.keyPressed(event);

        // Verify the event is fired
        Assert.assertEquals(10, helper.getEventType());
    }

    @Test
    public void testKeyPressedInsert() {
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_UNDEFINED, KeyEvent.CHAR_UNDEFINED);
        helper.keyPressed(event);

        // Verify the event is fired
        Assert.assertEquals(155, helper.getEventType());
    }

    @Test
    public void testKeyPressedDelete() {
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_BACK_SPACE, KeyEvent.CHAR_UNDEFINED);
        helper.keyPressed(event);

        // Verify the event is fired
        Assert.assertEquals(127, helper.getEventType());
    }

    @Test
    public void testKeyPressedTab() {
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_TAB, KeyEvent.CHAR_UNDEFINED);
        helper.keyPressed(event);

        // Verify the event is fired
        Assert.assertEquals(9, helper.getEventType());
    }

    @Test
    public void testKeyReleased() {
        // Not testing any functionality, as the method is empty
    }

    @Test
    public void testKeyTyped() {
        // Not testing any functionality, as the method is empty
    }
}