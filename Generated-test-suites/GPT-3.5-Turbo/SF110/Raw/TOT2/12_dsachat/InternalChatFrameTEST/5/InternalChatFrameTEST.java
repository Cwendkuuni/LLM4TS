import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class InternalChatFrameTest {
    @Test
    public void testCreateGUI() {
        // Test if the GUI components are created successfully
        InternalChatFrame internalChatFrame = new InternalChatFrame(new MainFrame());
        assertNotNull(internalChatFrame.getList());
        assertNotNull(internalChatFrame.getShowTxt());
        assertNotNull(internalChatFrame.getTxtInput());
        assertNotNull(internalChatFrame.getParent());
        assertNotNull(internalChatFrame.getDoc());
        assertNotNull(internalChatFrame.getListPop());
        assertNotNull(internalChatFrame.getTxtPop());
    }
    
    @Test
    public void testAddText() {
        // Test adding text to the chat frame
        InternalChatFrame internalChatFrame = new InternalChatFrame(new MainFrame());
        internalChatFrame.addText("Test message");
        assertEquals("Test message\n", internalChatFrame.getChatlog());
    }
    
    @Test
    public void testAddUser() {
        // Test adding a user to the user list
        InternalChatFrame internalChatFrame = new InternalChatFrame(new MainFrame());
        internalChatFrame.addUser("TestUser");
        String[] userList = internalChatFrame.getUserlist();
        assertEquals("TestUser", userList[0]);
    }
    
    @Test
    public void testDeleteUser() {
        // Test deleting a user from the user list
        InternalChatFrame internalChatFrame = new InternalChatFrame(new MainFrame());
        internalChatFrame.addUser("TestUser");
        internalChatFrame.deleteUser("TestUser");
        String[] userList = internalChatFrame.getUserlist();
        assertEquals(0, userList.length);
    }
    
    @Test
    public void testActionPerformed() {
        // Test actionPerformed method for different action commands
        InternalChatFrame internalChatFrame = new InternalChatFrame(new MainFrame());
        internalChatFrame.addUser("TestUser");
        // Choose color action command
        internalChatFrame.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "chooseColor"));
        // Background color action command
        internalChatFrame.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "bgColor"));
        // Whisper action command
        internalChatFrame.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "whisper"));
    }
}