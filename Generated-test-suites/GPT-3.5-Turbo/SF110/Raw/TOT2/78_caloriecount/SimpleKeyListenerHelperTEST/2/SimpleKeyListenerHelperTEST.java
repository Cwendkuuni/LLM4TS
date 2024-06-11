import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleKeyListenerHelperTest {
  
    private SimpleKeyListenerHelper helper;
    private MockComponent mockComponent;

    @Before
    public void setUp() {
        mockComponent = new MockComponent();
        helper = new SimpleKeyListenerHelper(mockComponent);
    }

    @After
    public void tearDown() {
        helper.detach();
    }

    @Test
    public void testDetach() {
        helper.detach();
        
        assertNull(mockComponent.getKeyListener());
    }

    @Test
    public void testNotifyListener() {
        SimpleKeyListener listener = new SimpleKeyListener();
        
        listener.enter(mockComponent);
        helper.notifyListener(listener, 10, null);
        assertEquals(mockComponent.getListenerAction(), "enter");

        listener.insert(mockComponent);
        helper.notifyListener(listener, 155, null);
        assertEquals(mockComponent.getListenerAction(), "insert");

        listener.delete(mockComponent);
        helper.notifyListener(listener, 127, null);
        assertEquals(mockComponent.getListenerAction(), "delete");

        listener.tab(mockComponent);
        helper.notifyListener(listener, 9, null);
        assertEquals(mockComponent.getListenerAction(), "tab");
    }

    @Test
    public void testKeyPressed() {
        mockComponent.setKeyPressed(KeyEvent.VK_TAB);
        helper.keyPressed(new KeyEvent(mockComponent, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_TAB, ' '));
        assertEquals(mockComponent.getListenerAction(), "fire");

        mockComponent.setKeyPressed(KeyEvent.VK_ENTER);
        helper.keyPressed(new KeyEvent(mockComponent, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, ' '));
        assertEquals(mockComponent.getListenerAction(), "fire");

        mockComponent.setKeyPressed(KeyEvent.VK_DELETE);
        helper.keyPressed(new KeyEvent(mockComponent, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_DELETE, ' '));
        assertEquals(mockComponent.getListenerAction(), "fire");

        mockComponent.setKeyPressed(KeyEvent.VK_INSERT);
        helper.keyPressed(new KeyEvent(mockComponent, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_INSERT, ' '));
        assertEquals(mockComponent.getListenerAction(), "fire");
    }

}

class MockComponent extends Component {
    private KeyListener keyListener;
    private String listenerAction;
    private int keyPressed;

    public void addKeyListener(KeyListener l) {
        keyListener = l;
    }

    public void removeKeyListener(KeyListener l) {
        keyListener = null;
    }

    public KeyListener getKeyListener() {
        return keyListener;
    }

    public void enter(Component c) {
        listenerAction = "enter";
    }

    public void insert(Component c) {
        listenerAction = "insert";
    }

    public void delete(Component c) {
        listenerAction = "delete";
    }

    public void tab(Component c) {
        listenerAction = "tab";
    }

    public String getListenerAction() {
        return listenerAction;
    }

    public void setKeyPressed(int code) {
        keyPressed = code;
    }

    public int getKeyPressed() {
        return keyPressed;
    }
}