import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class InternalChatFrameTest {

    private InternalChatFrame internalChatFrame;

    @Before
    public void setUp() {
        MainFrame mainFrame = new MainFrame();
        internalChatFrame = new InternalChatFrame(mainFrame);
    }

    @Test
    public void testAddText() {
        internalChatFrame.addText("Test message");
        assertEquals("Test message\n", internalChatFrame.getChatlog());
    }

    @Test
    public void testAddUser() {
        internalChatFrame.addUser("TestUser");
        assertTrue(internalChatFrame.getChatlog().contains("TestUser"));
    }

    @Test
    public void testDeleteUser() {
        internalChatFrame.addUser("TestUser");
        internalChatFrame.deleteUser("TestUser");
        assertFalse(internalChatFrame.getChatlog().contains("TestUser"));
    }

    @Test
    public void testGetUserlist() {
        internalChatFrame.addUser("User1");
        internalChatFrame.addUser("User2");
        internalChatFrame.addUser("User3");

        String[] userList = internalChatFrame.getUserlist();
        
        assertEquals(3, userList.length);
        assertEquals("User1", userList[0]);
        assertEquals("User2", userList[1]);
        assertEquals("User3", userList[2]);
    }

    @Test
    public void testActionPerformed() {
        // Test chooseColor action
        internalChatFrame.addUser("TestUser");
        internalChatFrame.actionPerformed(new ActionEvent(internalChatFrame, 0, "chooseColor"));
        
        // Test bgColor action
        internalChatFrame.actionPerformed(new ActionEvent(internalChatFrame, 0, "bgColor"));
        
        // Test whisper action
        internalChatFrame.actionPerformed(new ActionEvent(internalChatFrame, 0, "whisper"));
        assertEquals("/w TestUser ", internalChatFrame.txtInput.getText());
    }

    // Add more test cases for other methods if needed

}