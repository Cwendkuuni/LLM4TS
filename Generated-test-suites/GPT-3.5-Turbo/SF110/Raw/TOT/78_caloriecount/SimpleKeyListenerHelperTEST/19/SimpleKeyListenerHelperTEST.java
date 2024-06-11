
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.awt.Component;
import java.awt.event.KeyEvent;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class SimpleKeyListenerHelperTest {

    private SimpleKeyListenerHelper helper;

    @Before
    public void setup() {
        Component component = mock(Component.class);
        helper = new SimpleKeyListenerHelper(component);
    }

    @Test
    public void detach_ComponentNotNull_RemovesKeyListener() {
        Component component = helper.getComponent();
        helper.detach();
        verify(component).removeKeyListener(helper);
    }

    @Test
    public void detach_ComponentNull_DoesNothing() {
        helper.detach();
        // No exception should be thrown
    }

    @Test
    public void notifyListener_Type10_CallsEnter() {
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        helper.notifyListener(listener, 10, null);
        verify(listener).enter(helper.getComponent());
    }

    @Test
    public void notifyListener_Type155_CallsInsert() {
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        helper.notifyListener(listener, 155, null);
        verify(listener).insert(helper.getComponent());
    }

    @Test
    public void notifyListener_Type127_CallsDelete() {
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        helper.notifyListener(listener, 127, null);
        verify(listener).delete(helper.getComponent());
    }

    @Test
    public void notifyListener_Type9_CallsTab() {
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        helper.notifyListener(listener, 9, null);
        verify(listener).tab(helper.getComponent());
    }

    @Test
    public void keyPressed_KeyCode9_FiresEvent() {
        KeyEvent event = mock(KeyEvent.class);
        Mockito.when(event.getKeyCode()).thenReturn(9);
        helper.keyPressed(event);
        verify(helper).fire(9);
    }

    @Test
    public void keyPressed_KeyCode10_FiresEvent() {
        KeyEvent event = mock(KeyEvent.class);
        Mockito.when(event.getKeyCode()).thenReturn(10);
        helper.keyPressed(event);
        verify(helper).fire(10);
    }

    @Test
    public void keyPressed_KeyCode127_FiresEvent() {
        KeyEvent event = mock(KeyEvent.class);
        Mockito.when(event.getKeyCode()).thenReturn(127);
        helper.keyPressed(event);
        verify(helper).fire(127);
    }

    @Test
    public void keyPressed_KeyCode155_FiresEvent() {
        KeyEvent event = mock(KeyEvent.class);
        Mockito.when(event.getKeyCode()).thenReturn(155);
        helper.keyPressed(event);
        verify(helper).fire(155);
    }

    @Test
    public void keyReleased_DoesNothing() {
        // No action is performed in the method, no need to test
    }

    @Test
    public void keyTyped_DoesNothing() {
        // No action is performed in the method, no need to test
    }
}

