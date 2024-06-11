import static org.junit.Assert.*;
import org.junit.Test;

public class InternalChatFrameTest {
    
    @Test
    public void testAddText() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        chatFrame.addText("Test");
        assertEquals("Test\n", chatFrame.getChatlog());
    }
    
    @Test
    public void testAddUser() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        chatFrame.addUser("User1");
        assertTrue(chatFrame.getChatlog().contains("User1"));
    }
    
    @Test
    public void testDeleteUser() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        chatFrame.addUser("User1");
        chatFrame.deleteUser("User1");
        assertFalse(chatFrame.getChatlog().contains("User1"));
    }
    
    @Test
    public void testGetUserlist() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        chatFrame.addUser("User1");
        chatFrame.addUser("User2");
        String[] userList = chatFrame.getUserlist();
        assertEquals(2, userList.length);
        assertEquals("User1", userList[0]);
        assertEquals("User2", userList[1]);
    }
    
    @Test
    public void testActionPerformedChooseColor() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        chatFrame.addUser("User1");
        chatFrame.actionPerformed(new ActionEvent(chatFrame, ActionEvent.ACTION_PERFORMED, "chooseColor"));
        // Test logic of choosing color
    }
    
    @Test
    public void testActionPerformedBgColor() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        chatFrame.actionPerformed(new ActionEvent(chatFrame, ActionEvent.ACTION_PERFORMED, "bgColor"));
        // Test logic of choosing background color
    }
    
    @Test
    public void testActionPerformedWhisper() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        chatFrame.addUser("User1");
        chatFrame.actionPerformed(new ActionEvent(chatFrame, ActionEvent.ACTION_PERFORMED, "whisper"));
        assertEquals("/w User1 ", chatFrame.txtInput.getText());
    }

}