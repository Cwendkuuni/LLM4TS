import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;
import java.awt.Component;
import java.awt.event.KeyEvent;
import com.lts.event.SimpleKeyListener;
import com.lts.event.SimpleKeyListenerHelper;

public class SimpleKeyListenerHelperTest {

    @Test
    public void testDetach() {
        Component component = Mockito.mock(Component.class);
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        helper.detach();
        assertNull(helper.component);
    }

    @Test
    public void testNotifyListener() {
        Component component = Mockito.mock(Component.class);
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        SimpleKeyListener listener = Mockito.mock(SimpleKeyListener.class);
        
        helper.notifyListener(listener, 10, null);
        Mockito.verify(listener).enter(component);
        
        helper.notifyListener(listener, 155, null);
        Mockito.verify(listener).insert(component);
        
        helper.notifyListener(listener, 127, null);
        Mockito.verify(listener).delete(component);
        
        helper.notifyListener(listener, 9, null);
        Mockito.verify(listener).tab(component);
    }

    @Test
    public void testKeyPressed() {
        Component component = Mockito.mock(Component.class);
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        
        KeyEvent eventTab = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_TAB, KeyEvent.CHAR_UNDEFINED);
        helper.keyPressed(eventTab);
        // Check if fire method is called
        
        KeyEvent eventEnter = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, KeyEvent.CHAR_UNDEFINED);
        helper.keyPressed(eventEnter);
        // Check if fire method is called
        
        KeyEvent eventDelete = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_DELETE, KeyEvent.CHAR_UNDEFINED);
        helper.keyPressed(eventDelete);
        // Check if fire method is called
        
        KeyEvent eventInsert = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_INSERT, KeyEvent.CHAR_UNDEFINED);
        helper.keyPressed(eventInsert);
        // Check if fire method is called
    }
}