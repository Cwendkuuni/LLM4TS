import org.junit.*;
import static org.junit.Assert.*;
import org.mockito.*;

public class SimpleKeyListenerHelperTest {

    @Mock
    private Component component;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testConstructor() {
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        Mockito.verify(component).addKeyListener(Mockito.any(KeyListener.class));
        assertNotNull(helper);
    }

    @Test
    public void testDetach() {
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        helper.detach();
        Mockito.verify(component).removeKeyListener(Mockito.any(KeyListener.class));
        assertNull(helper.component);
    }

    @Test
    public void testNotifyListenerEnter(){
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        SimpleKeyListener listener = Mockito.mock(SimpleKeyListener.class);
        helper.notifyListener(listener, 10, null);
        Mockito.verify(listener).enter(component);
    }

    @Test
    public void testNotifyListenerInsert(){
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        SimpleKeyListener listener = Mockito.mock(SimpleKeyListener.class);
        helper.notifyListener(listener, 155, null);
        Mockito.verify(listener).insert(component);
    }

    @Test
    public void testNotifyListenerDelete(){
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        SimpleKeyListener listener = Mockito.mock(SimpleKeyListener.class);
        helper.notifyListener(listener, 127, null);
        Mockito.verify(listener).delete(component);
    }

    @Test
    public void testNotifyListenerTab(){
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        SimpleKeyListener listener = Mockito.mock(SimpleKeyListener.class);
        helper.notifyListener(listener, 9, null);
        Mockito.verify(listener).tab(component);
    }

    @Test
    public void testKeyPressedTab(){
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        KeyEvent event = Mockito.mock(KeyEvent.class);
        Mockito.when(event.getKeyCode()).thenReturn(9);
        helper.keyPressed(event);
        Mockito.verify(helper).fire(9);
    }

    @Test
    public void testKeyPressedEnter(){
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        KeyEvent event = Mockito.mock(KeyEvent.class);
        Mockito.when(event.getKeyCode()).thenReturn(10);
        helper.keyPressed(event);
        Mockito.verify(helper).fire(10);
    }

    @Test
    public void testKeyPressedDelete(){
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        KeyEvent event = Mockito.mock(KeyEvent.class);
        Mockito.when(event.getKeyCode()).thenReturn(127);
        helper.keyPressed(event);
        Mockito.verify(helper).fire(127);
    }

    @Test
    public void testKeyPressedInsert(){
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        KeyEvent event = Mockito.mock(KeyEvent.class);
        Mockito.when(event.getKeyCode()).thenReturn(155);
        helper.keyPressed(event);
        Mockito.verify(helper).fire(155);
    }
    
    @Test
    public void testKeyReleased(){
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        KeyEvent event = Mockito.mock(KeyEvent.class);
        helper.keyReleased(event);
        // No verification needed for keyReleased
    }
    
    @Test
    public void testKeyTyped(){
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        KeyEvent event = Mockito.mock(KeyEvent.class);
        helper.keyTyped(event);
        // No verification needed for keyTyped
    }
}