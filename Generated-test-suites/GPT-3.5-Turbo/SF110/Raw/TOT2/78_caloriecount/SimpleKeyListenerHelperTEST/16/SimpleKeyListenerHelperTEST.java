import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.Component;
import java.awt.event.KeyEvent;
import com.lts.event.SimpleKeyListener;
import com.lts.event.SimpleKeyListenerHelper;

public class SimpleKeyListenerHelperTest {

    @Test
    public void testDetach() {
        Component mockComponent = new Component() {};
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(mockComponent);
        
        helper.detach();
        
        assertNull(helper.component);
    }
    
    @Test
    public void testNotifyListener() {
        // Test enter case
        SimpleKeyListener mockListenerEnter = new SimpleKeyListener() {
            @Override
            public void enter(Component component) {
                assertEquals(helper.component, component);
            }
        };
        SimpleKeyListenerHelper helperEnter = new SimpleKeyListenerHelper(mockComponent);
        helperEnter.notifyListener(mockListenerEnter, 10, null);
        
        // Test insert case
        SimpleKeyListener mockListenerInsert = new SimpleKeyListener() {
            @Override
            public void insert(Component component) {
                assertEquals(helper.component, component);
            }
        };
        SimpleKeyListenerHelper helperInsert = new SimpleKeyListenerHelper(mockComponent);
        helperInsert.notifyListener(mockListenerInsert, 155, null);
        
        // Test delete case
        SimpleKeyListener mockListenerDelete = new SimpleKeyListener() {
            @Override
            public void delete(Component component) {
                assertEquals(helper.component, component);
            }
        };
        SimpleKeyListenerHelper helperDelete = new SimpleKeyListenerHelper(mockComponent);
        helperDelete.notifyListener(mockListenerDelete, 127, null);
        
        // Test tab case
        SimpleKeyListener mockListenerTab = new SimpleKeyListener() {
            @Override
            public void tab(Component component) {
                assertEquals(helper.component, component);
            }
        };
        SimpleKeyListenerHelper helperTab = new SimpleKeyListenerHelper(mockComponent);
        helperTab.notifyListener(mockListenerTab, 9, null);
    }
    
    @Test
    public void testKeyPressed() {
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(mockComponent);
        
        // Test for case 9, 10, 127, and 155
        int[] keyCodes = {9, 10, 127, 155};
        for (int keyCode : keyCodes) {
            KeyEvent mockEvent = new KeyEvent(mockComponent, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, keyCode, 'a');
            helper.keyPressed(mockEvent);
        }
    }

}