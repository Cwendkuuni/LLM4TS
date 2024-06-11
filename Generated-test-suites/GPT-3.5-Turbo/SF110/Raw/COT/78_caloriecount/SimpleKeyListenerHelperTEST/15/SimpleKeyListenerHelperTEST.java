
import org.junit.Before;
import org.junit.Test;
import java.awt.*;
import java.awt.event.KeyEvent;
import static org.mockito.Mockito.*;

public class SimpleKeyListenerHelperTest {

    private SimpleKeyListenerHelper simpleKeyListenerHelper;
    private Component component;

    @Before
    public void setUp() {
        component = mock(Component.class);
        simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
    }

    @Test
    public void detach_Test() {
        simpleKeyListenerHelper.detach();
        verify(component).removeKeyListener(simpleKeyListenerHelper);
    }

    @Test
    public void notifyListener_Enter_Test() {
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        simpleKeyListenerHelper.notifyListener(listener, 10, null);
        verify(listener).enter(component);
    }

    @Test
    public void notifyListener_Insert_Test() {
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        simpleKeyListenerHelper.notifyListener(listener, 155, null);
        verify(listener).insert(component);
    }

    @Test
    public void notifyListener_Delete_Test() {
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        simpleKeyListenerHelper.notifyListener(listener, 127, null);
        verify(listener).delete(component);
    }

    @Test
    public void notifyListener_Tab_Test() {
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        simpleKeyListenerHelper.notifyListener(listener, 9, null);
        verify(listener).tab(component);
    }

    @Test
    public void keyPressed_Test_Tab() {
        KeyEvent event = mock(KeyEvent.class);
        when(event.getKeyCode()).thenReturn(9);
        simpleKeyListenerHelper.keyPressed(event);
        verify(component).getKeyListeners();
    }

    @Test
    public void keyPressed_Test_Enter() {
        KeyEvent event = mock(KeyEvent.class);
        when(event.getKeyCode()).thenReturn(10);
        simpleKeyListenerHelper.keyPressed(event);
        verify(component).getKeyListeners();
    }

    @Test
    public void keyPressed_Test_Delete() {
        KeyEvent event = mock(KeyEvent.class);
        when(event.getKeyCode()).thenReturn(127);
        simpleKeyListenerHelper.keyPressed(event);
        verify(component).getKeyListeners();
    }

    @Test
    public void keyPressed_Test_Insert() {
        KeyEvent event = mock(KeyEvent.class);
        when(event.getKeyCode()).thenReturn(155);
        simpleKeyListenerHelper.keyPressed(event);
        verify(component).getKeyListeners();
    }
}
