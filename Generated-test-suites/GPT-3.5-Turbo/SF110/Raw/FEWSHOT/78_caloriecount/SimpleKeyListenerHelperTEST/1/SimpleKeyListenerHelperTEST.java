import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.awt.Component;
import java.awt.event.KeyEvent;

public class SimpleKeyListenerHelperTest {

    private SimpleKeyListenerHelper helper;
    private SimpleKeyListener listener;
    private Component component;

    @Before
    public void setUp() {
        component = Mockito.mock(Component.class);
        helper = new SimpleKeyListenerHelper(component);
        listener = Mockito.mock(SimpleKeyListener.class);
    }

    @Test
    public void testDetach() {
        helper.detach();
        Mockito.verify(component).removeKeyListener(helper);
    }

    @Test
    public void testNotifyListenerEnter() {
        helper.notifyListener(listener, 10, null);
        Mockito.verify(listener).enter(component);
    }

    @Test
    public void testNotifyListenerInsert() {
        helper.notifyListener(listener, 155, null);
        Mockito.verify(listener).insert(component);
    }

    @Test
    public void testNotifyListenerDelete() {
        helper.notifyListener(listener, 127, null);
        Mockito.verify(listener).delete(component);
    }

    @Test
    public void testNotifyListenerTab() {
        helper.notifyListener(listener, 9, null);
        Mockito.verify(listener).tab(component);
    }

    @Test
    public void testKeyPressedTab() {
        KeyEvent event = Mockito.mock(KeyEvent.class);
        Mockito.when(event.getKeyCode()).thenReturn(KeyEvent.VK_TAB);
        helper.keyPressed(event);
        Mockito.verify(listener).tab(component);
    }

    @Test
    public void testKeyPressedEnter() {
        KeyEvent event = Mockito.mock(KeyEvent.class);
        Mockito.when(event.getKeyCode()).thenReturn(KeyEvent.VK_ENTER);
        helper.keyPressed(event);
        Mockito.verify(listener).enter(component);
    }

    @Test
    public void testKeyPressedDelete() {
        KeyEvent event = Mockito.mock(KeyEvent.class);
        Mockito.when(event.getKeyCode()).thenReturn(KeyEvent.VK_DELETE);
        helper.keyPressed(event);
        Mockito.verify(listener).delete(component);
    }

    @Test
    public void testKeyPressedInsert() {
        KeyEvent event = Mockito.mock(KeyEvent.class);
        Mockito.when(event.getKeyCode()).thenReturn(KeyEvent.VK_INSERT);
        helper.keyPressed(event);
        Mockito.verify(listener).insert(component);
    }
}