import org.junit.*;
import static org.junit.Assert.*;

public class InternalChatFrameTest {
    @Test
    public void testAddText() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        String line = "This is a test message.";
        chatFrame.addText(line);
        assertTrue(chatFrame.getChatlog().contains(line));
    }
    
    @Test
    public void testAddUser() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        String user = "TestUser";
        chatFrame.addUser(user);
        String[] userList = chatFrame.getUserlist();
        boolean found = false;
        for (String u : userList) {
            if (u.equals(user)) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }
    
    @Test
    public void testDeleteUser() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        String user = "TestUser";
        chatFrame.addUser(user);
        chatFrame.deleteUser(user);
        String[] userList = chatFrame.getUserlist();
        boolean found = false;
        for (String u : userList) {
            if (u.equals(user)) {
                found = true;
                break;
            }
        }
        assertFalse(found);
    }
    
    @Test
    public void testActionPerformed() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        chatFrame.addUser("TestUser");
        
        ActionEvent chooseColorEvent = new ActionEvent(chatFrame, ActionEvent.ACTION_PERFORMED, "chooseColor");
        chatFrame.actionPerformed(chooseColorEvent);
        // Need to verify that the color has actually changed
        
        ActionEvent bgColorEvent = new ActionEvent(chatFrame, ActionEvent.ACTION_PERFORMED, "bgColor");
        chatFrame.actionPerformed(bgColorEvent);
        // Need to verify that the background color has actually changed
        
        ActionEvent whisperEvent = new ActionEvent(chatFrame, ActionEvent.ACTION_PERFORMED, "whisper");
        chatFrame.actionPerformed(whisperEvent);
        assertEquals("/w TestUser ", chatFrame.txtInput.getText());
    }
    
    // Add more test methods to cover other functionalities of the class
    
}