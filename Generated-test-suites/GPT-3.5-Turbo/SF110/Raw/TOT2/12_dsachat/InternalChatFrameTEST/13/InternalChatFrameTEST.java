import org.junit.Before;
import org.junit.Test;

public class InternalChatFrameTest {
    
    private InternalChatFrame internalChatFrame;
    private MainFrame parent;
    
    @Before
    public void setUp() {
        parent = new MainFrame();
        internalChatFrame = new InternalChatFrame(parent);
    }
    
    @Test
    public void testAddText() {
        internalChatFrame.addText("Test message");
        String chatlog = internalChatFrame.getChatlog();
        assertEquals("Test message\n", chatlog);
    }
    
    @Test
    public void testAddUser() {
        internalChatFrame.addUser("testUser");
        String[] userList = internalChatFrame.getUserlist();
        assertEquals("testUser", userList[0]);
    }
    
    @Test
    public void testDeleteUser() {
        internalChatFrame.addUser("testUser");
        internalChatFrame.deleteUser("testUser");
        String[] userList = internalChatFrame.getUserlist();
        assertEquals(0, userList.length);
    }
    
    @Test
    public void testActionPerformedChooseColor() {
        internalChatFrame.addUser("testUser");
        internalChatFrame.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "chooseColor"));
        // Check if color style for testUser is changed
    }
    
    @Test
    public void testActionPerformedBgColor() {
        internalChatFrame.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "bgColor"));
        // Check if background color of showTxt is changed
    }
    
    @Test
    public void testActionPerformedWhisper() {
        internalChatFrame.addUser("testUser");
        internalChatFrame.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "whisper"));
        assertEquals("/w testUser ", internalChatFrame.txtInput.getText());
    }
    
    // Additional test cases for edge cases and exception handling scenarios
    
}