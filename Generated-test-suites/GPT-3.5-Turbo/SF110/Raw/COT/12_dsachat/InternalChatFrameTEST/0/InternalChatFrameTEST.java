import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class InternalChatFrameTest {
    
    private InternalChatFrame chatFrame;
    
    @Before
    public void setUp() {
        MainFrame parent = new MainFrame();
        chatFrame = new InternalChatFrame(parent);
    }
    
    @Test
    public void testAddText() {
        chatFrame.addText("Test message");
        assertEquals("Test message\n", chatFrame.getChatlog());
    }
    
    @Test
    public void testAddUser() {
        chatFrame.addUser("User1");
        chatFrame.addUser("User2");
        chatFrame.addUser("User3");
        String[] userList = chatFrame.getUserlist();
        assertEquals(3, userList.length);
        assertEquals("User3", userList[0]);
        assertEquals("User2", userList[1]);
        assertEquals("User1", userList[2]);
    }
    
    @Test
    public void testDeleteUser() {
        chatFrame.addUser("User1");
        chatFrame.addUser("User2");
        chatFrame.addUser("User3");
        chatFrame.deleteUser("User2");
        String[] userList = chatFrame.getUserlist();
        assertEquals(2, userList.length);
        assertEquals("User3", userList[0]);
        assertEquals("User1", userList[1]);
    }
    
    @Test
    public void testActionPerformed_chooseColor() {
        chatFrame.addUser("User1");
        chatFrame.list.setSelectedIndex(0);
        chatFrame.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "chooseColor"));
        // Check if the style of the selected user is changed to the chosen color
        // Assertion code...
    }
    
    @Test
    public void testActionPerformed_bgColor() {
        chatFrame.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "bgColor"));
        // Check if the background color of the chat frame is changed to the chosen color
        // Assertion code...
    }
    
    @Test
    public void testActionPerformed_whisper() {
        chatFrame.addUser("User1");
        chatFrame.list.setSelectedIndex(0);
        chatFrame.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "whisper"));
        assertEquals("/w User1 ", chatFrame.txtInput.getText());
    }
    
    // Additional test cases for other methods...
    
}