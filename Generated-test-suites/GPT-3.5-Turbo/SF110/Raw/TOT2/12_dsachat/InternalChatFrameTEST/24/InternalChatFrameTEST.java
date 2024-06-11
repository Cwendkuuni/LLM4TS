import org.junit.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class InternalChatFrameTest {

    private InternalChatFrame internalChatFrame;

    @Before
    public void setUp() {
        internalChatFrame = new InternalChatFrame(new MainFrame());
    }

    @Test
    public void testAddText() {
        String line = "Sample text";
        internalChatFrame.addText(line);
        assertEquals(internalChatFrame.getChatlog(), line + "\n");
    }

    @Test
    public void testAddUser() {
        String user = "Alice";
        internalChatFrame.addUser(user);
        assertTrue(Arrays.asList(internalChatFrame.getUserlist()).contains(user));
    }

    @Test
    public void testDeleteUser() {
        String user = "Bob";
        internalChatFrame.addUser(user);
        internalChatFrame.deleteUser(user);
        assertFalse(Arrays.asList(internalChatFrame.getUserlist()).contains(user));
    }

    @Test
    public void testActionPerformed_ChooseColor() {
        String user = "Alice";
        ActionEvent e = mock(ActionEvent.class);
        when(e.getActionCommand()).thenReturn("chooseColor");
        internalChatFrame.addUser(user);
        internalChatFrame.actionPerformed(e);
        // Verify that style is changed successfully
        // Add appropriate assertion based on the implementation
    }

    @Test
    public void testActionPerformed_BGColor() {
        String user = "Alice";
        ActionEvent e = mock(ActionEvent.class);
        when(e.getActionCommand()).thenReturn("bgColor");
        internalChatFrame.addUser(user);
        internalChatFrame.actionPerformed(e);
        // Verify that background color is changed successfully
        // Add appropriate assertion based on the implementation
    }

    @Test
    public void testActionPerformed_Whisper() {
        String user = "Alice";
        ActionEvent e = mock(ActionEvent.class);
        when(e.getActionCommand()).thenReturn("whisper");
        internalChatFrame.addUser(user);
        internalChatFrame.actionPerformed(e);
        // Verify that whisper action sets text input correctly
        // Add appropriate assertion based on the implementation
    }

    @Test
    public void testMouseClicked() {
        // TODO: Implement test for mouseClicked method
    }

    @Test
    public void testMouseEntered() {
        // TODO: Implement test for mouseEntered method
    }

    @Test
    public void testMouseExited() {
        // TODO: Implement test for mouseExited method
    }

    @Test
    public void testMousePressed() {
        // TODO: Implement test for mousePressed method
    }

    @Test
    public void testMouseReleased() {
        // TODO: Implement test for mouseReleased method
    }

    @After
    public void tearDown() {
        internalChatFrame = null;
    }

}