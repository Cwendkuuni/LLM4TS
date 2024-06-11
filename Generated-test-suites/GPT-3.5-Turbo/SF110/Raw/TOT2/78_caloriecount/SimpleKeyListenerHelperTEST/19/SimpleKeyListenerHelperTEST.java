import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import java.awt.Component;
import java.awt.event.KeyEvent;

public class SimpleKeyListenerHelperTest {

    private SimpleKeyListenerHelper listenerHelper;

    @Before
    public void setUp() {
        Component component = new Component() {};
        listenerHelper = new SimpleKeyListenerHelper(component);
    }

    @After
    public void tearDown() {
        listenerHelper.detach();
    }

    @Test
    public void testDetach() {
        listenerHelper.detach();
        assertNull(listenerHelper.component);
    }

    @Test
    public void testNotifyListener() {
        // test for case 10
        KeyEvent event10 = new KeyEvent(listenerHelper.component, 10, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, KeyEvent.CHAR_UNDEFINED);
        listenerHelper.notifyListener(new SimpleKeyListener(), 10, null);
        // add assertion here
        
        // test for case 155
        KeyEvent event155 = new KeyEvent(listenerHelper.component, 155, System.currentTimeMillis(), 0, KeyEvent.VK_UNDEFINED, 'i');
        listenerHelper.notifyListener(new SimpleKeyListener(), 155, null);
        // add assertion here
        
        // test for case 127
        KeyEvent event127 = new KeyEvent(listenerHelper.component, 127, System.currentTimeMillis(), 0, KeyEvent.VK_UNDEFINED, 'd');
        listenerHelper.notifyListener(new SimpleKeyListener(), 127, null);
        // add assertion here
        
        // test for case 9
        KeyEvent event9 = new KeyEvent(listenerHelper.component, 9, System.currentTimeMillis(), 0, KeyEvent.VK_TAB, KeyEvent.CHAR_UNDEFINED);
        listenerHelper.notifyListener(new SimpleKeyListener(), 9, null);
        // add assertion here
    }

    @Test
    public void testKeyPressed() {
        // test for case 9
        KeyEvent event9 = new KeyEvent(listenerHelper.component, 9, System.currentTimeMillis(), 0, KeyEvent.VK_TAB, KeyEvent.CHAR_UNDEFINED);
        listenerHelper.keyPressed(event9);
        // add assertion here
        
        // test for case 10
        KeyEvent event10 = new KeyEvent(listenerHelper.component, 10, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, KeyEvent.CHAR_UNDEFINED);
        listenerHelper.keyPressed(event10);
        // add assertion here
        
        // test for case 127
        KeyEvent event127 = new KeyEvent(listenerHelper.component, 127, System.currentTimeMillis(), 0, KeyEvent.VK_UNDEFINED, 'd');
        listenerHelper.keyPressed(event127);
        // add assertion here
        
        // test for case 155
        KeyEvent event155 = new KeyEvent(listenerHelper.component, 155, System.currentTimeMillis(), 0, KeyEvent.VK_UNDEFINED, 'i');
        listenerHelper.keyPressed(event155);
        // add assertion here
    }

    // test methods keyReleased and keyTyped can be tested with additional test cases

}