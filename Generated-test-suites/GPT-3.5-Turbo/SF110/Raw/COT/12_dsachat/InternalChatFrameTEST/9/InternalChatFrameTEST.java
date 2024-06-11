import org.junit.*;
import static org.junit.Assert.*;

public class InternalChatFrameTest {

    @Test
    public void testAddText() {
        // Test basic functionality of the addText() method
        
        InternalChatFrame chatFrame = new InternalChatFrame(null);
        chatFrame.addText("Test message");
        
        // Check if the message was added to the chat frame's text pane
        String chatLog = chatFrame.getChatlog();
        assertTrue(chatLog.contains("Test message"));
    }

    @Test
    public void testAddUser() {
        // Test adding a user to the chat frame's user list
        
        InternalChatFrame chatFrame = new InternalChatFrame(null);
        chatFrame.addUser("User1");
        
        // Check if the user was added to the chat frame's user list
        String[] userList = chatFrame.getUserlist();
        assertEquals(1, userList.length);
        assertEquals("User1", userList[0]);
    }

    @Test
    public void testDeleteUser() {
        // Test deleting a user from the chat frame's user list
        
        InternalChatFrame chatFrame = new InternalChatFrame(null);
        chatFrame.addUser("User1");
        chatFrame.deleteUser("User1");
        
        // Check if the user was deleted from the chat frame's user list
        String[] userList = chatFrame.getUserlist();
        assertEquals(0, userList.length);
    }

    @Test
    public void testActionPerformed_chooseColor() {
        // Test choosing a color for a user
        
        InternalChatFrame chatFrame = new InternalChatFrame(null);
        chatFrame.addUser("User1");
        
        // Choose a color for the user
        chatFrame.actionPerformed(new ActionEvent(null, 0, "chooseColor"));
        
        // TODO: Check if the color was changed for the user
    }

    @Test
    public void testActionPerformed_bgColor() {
        // Test choosing a background color for the chat frame
        
        InternalChatFrame chatFrame = new InternalChatFrame(null);
        
        // Choose a background color for the chat frame
        chatFrame.actionPerformed(new ActionEvent(null, 0, "bgColor"));
        
        // TODO: Check if the background color was changed for the chat frame
    }

    @Test
    public void testActionPerformed_whisper() {
        // Test setting the text input to a whisper command
        
        InternalChatFrame chatFrame = new InternalChatFrame(null);
        chatFrame.addUser("User1");
        chatFrame.txtInput.setText("");
        
        // Set the text input to a whisper command for a user
        chatFrame.actionPerformed(new ActionEvent(null, 0, "whisper"));
        
        // Check if the text input was set to the correct whisper command
        assertEquals("/w User1 ", chatFrame.txtInput.getText());
    }

    //TODO: Add test cases for other public methods in the InternalChatFrame class

}