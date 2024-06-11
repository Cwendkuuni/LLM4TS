import org.junit.Test;

public class SimpleKeyListenerHelperTest {

    @Test
    public void testDetach() {
        // Arrange
        Component component = new Component();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);

        // Act
        helper.detach();

        // Assert
        assertNull(helper.getComponent());
    }

    @Test
    public void testNotifyListener_Enter() {
        // Arrange
        Component component = new Component();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        SimpleKeyListener listener = new SimpleKeyListenerImpl();
        helper.attachListener(listener);

        // Act
        helper.notifyListener(listener, 10, null);

        // Assert
        assert(listener.isEnterCalled());
        assertEquals(component, listener.getEnterComponent());
    }

    @Test
    public void testNotifyListener_Insert() {
        // Arrange
        Component component = new Component();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        SimpleKeyListener listener = new SimpleKeyListenerImpl();
        helper.attachListener(listener);

        // Act
        helper.notifyListener(listener, 155, null);

        // Assert
        assert(listener.isInsertCalled());
        assertEquals(component, listener.getInsertComponent());
    }

    @Test
    public void testNotifyListener_Delete() {
        // Arrange
        Component component = new Component();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        SimpleKeyListener listener = new SimpleKeyListenerImpl();
        helper.attachListener(listener);

        // Act
        helper.notifyListener(listener, 127, null);

        // Assert
        assert(listener.isDeleteCalled());
        assertEquals(component, listener.getDeleteComponent());
    }

    @Test
    public void testNotifyListener_Tab() {
        // Arrange
        Component component = new Component();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        SimpleKeyListener listener = new SimpleKeyListenerImpl();
        helper.attachListener(listener);

        // Act
        helper.notifyListener(listener, 9, null);

        // Assert
        assert(listener.isTabCalled());
        assertEquals(component, listener.getTabComponent());
    }

    @Test
    public void testKeyPressed_Tab() {
        // Arrange
        Component component = new Component();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(),
                            0, KeyEvent.VK_TAB, KeyEvent.CHAR_UNDEFINED);

        // Act
        helper.keyPressed(event);

        // Assert
        assert(helper.isEventFired());
        assertEquals(KeyEvent.VK_TAB, helper.getFiredEventKeyCode());
    }

    @Test
    public void testKeyPressed_Enter() {
        // Arrange
        Component component = new Component();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(),
                            0, KeyEvent.VK_ENTER, KeyEvent.CHAR_UNDEFINED);

        // Act
        helper.keyPressed(event);

        // Assert
        assert(helper.isEventFired());
        assertEquals(KeyEvent.VK_ENTER, helper.getFiredEventKeyCode());
    }

    @Test
    public void testKeyPressed_Delete() {
        // Arrange
        Component component = new Component();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(),
                            0, KeyEvent.VK_DELETE, KeyEvent.CHAR_UNDEFINED);

        // Act
        helper.keyPressed(event);

        // Assert
        assert(helper.isEventFired());
        assertEquals(KeyEvent.VK_DELETE, helper.getFiredEventKeyCode());
    }

    @Test
    public void testKeyPressed_Insert() {
        // Arrange
        Component component = new Component();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(),
                            0, KeyEvent.VK_INSERT, KeyEvent.CHAR_UNDEFINED);

        // Act
        helper.keyPressed(event);

        // Assert
        assert(helper.isEventFired());
        assertEquals(KeyEvent.VK_INSERT, helper.getFiredEventKeyCode());
    }

    @Test
    public void testKeyReleased() {
        // Arrange
        Component component = new Component();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_RELEASED, System.currentTimeMillis(),
                            0, KeyEvent.VK_DELETE, KeyEvent.CHAR_UNDEFINED);

        // Act
        helper.keyReleased(event);

        // Assert
        // No assertions as the method does not have any behavior
    }

    @Test
    public void testKeyTyped() {
        // Arrange
        Component component = new Component();
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
        KeyEvent event = new KeyEvent(component, KeyEvent.KEY_TYPED, System.currentTimeMillis(),
                            0, KeyEvent.VK_UNDEFINED, 'a');

        // Act
        helper.keyTyped(event);

        // Assert
        // No assertions as the method does not have any behavior
    }

}