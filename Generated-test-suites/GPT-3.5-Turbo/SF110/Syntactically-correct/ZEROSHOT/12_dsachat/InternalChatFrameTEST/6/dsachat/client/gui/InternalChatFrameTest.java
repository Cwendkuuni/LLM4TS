import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class InternalChatFrameTest {

    private InternalChatFrame chatFrame;

    @Before
    public void setUp() {
        chatFrame = new InternalChatFrame(new MainFrame());
    }

    @Test
    public void testAddText() {
        chatFrame.addText("Test message");
        assertEquals("Test message\n", chatFrame.getChatlog());
    }

    @Test
    public void testAddUser() {
        chatFrame.addUser("TestUser");
        String[] userList = chatFrame.getUserlist();
        assertEquals("TestUser", userList[0]);
    }

    @Test
    public void testDeleteUser() {
        chatFrame.addUser("TestUser");
        chatFrame.deleteUser("TestUser");
        String[] userList = chatFrame.getUserlist();
        assertEquals(0, userList.length);
    }

    @Test
    public void testActionPerformed_ChooseColor() {
        // Simulate choosing color for user "TestUser"
        chatFrame.addUser("TestUser");
        chatFrame.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "chooseColor"));
        // Perform assertion on the style of "TestUser"
        // assertTrue(condition);
    }

    @Test
    public void testActionPerformed_BgColor() {
        // Simulate choosing background color
        chatFrame.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "bgColor"));
        // Perform assertion on the background color
        // assertTrue(condition);
    }

    @Test
    public void testActionPerformed_Whisper() {
        // Simulate choosing whisper action for user "TestUser"
        chatFrame.addUser("TestUser");
        chatFrame.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "whisper"));
        // Perform assertion on the content of txtInput
        // assertEquals(expectedValue, chatFrame.txtInput.getText());
    }

    @Test
    public void testMouseClicked() {
        // Simulate mouse click event
        // Implement the test logic
    }

    @Test
    public void testMouseEntered() {
        // Simulate mouse entered event
        // Implement the test logic
    }

    @Test
    public void testMouseExited() {
        // Simulate mouse exited event
        // Implement the test logic
    }

    @Test
    public void testMousePressed() {
        // Simulate mouse pressed event
        // Implement the test logic
    }

    @Test
    public void testMouseReleased() {
        // Simulate mouse released event
        // Implement the test logic
    }

    @Test
    public void testGetChatlog() {
        chatFrame.addText("Test message 1");
        chatFrame.addText("Test message 2");
        assertEquals("Test message 1\nTest message 2\n", chatFrame.getChatlog());
    }
}