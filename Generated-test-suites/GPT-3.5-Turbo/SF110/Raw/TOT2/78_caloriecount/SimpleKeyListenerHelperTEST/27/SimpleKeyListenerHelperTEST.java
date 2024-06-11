import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.Component;
import java.awt.event.KeyEvent;

public class SimpleKeyListenerHelperTest {

    @Test
    public void testDetach() {
        Component component = new Component();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        
        helper.detach();
        
        assertNull(helper.component);
    }
    
    @Test
    public void testNotifyListener() {
        Component component = new Component();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        
        helper.notifyListener(new SimpleKeyListener() {
            @Override
            public void enter(Component c) {
                assertEquals(component, c);
            }

            @Override
            public void insert(Component c) {
                assertEquals(component, c);
            }

            @Override
            public void delete(Component c) {
                assertEquals(component, c);
            }

            @Override
            public void tab(Component c) {
                assertEquals(component, c);
            }
        }, 10, null);
    }
    
    @Test
    public void testKeyPressed() {
        Component component = new Component();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        
        helper.keyPressed(new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, ' '));
    }

    // Additional test cases for keyReleased and keyTyped methods could be added here
    
}