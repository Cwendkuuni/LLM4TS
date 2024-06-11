import static org.junit.Assert.*;
import org.junit.Test;

public class InternalChatFrameTest {

    @Test
    public void testAddText() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        chatFrame.addText("Test message");
        assertEquals("Test message\n", chatFrame.getChatlog());
    }
    
    @Test
    public void testAddTextWithStyle() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        chatFrame.addText("You: Test message");
        assertEquals("You: Test message\n", chatFrame.getChatlog());
    }
    
    @Test
    public void testAddUser() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        chatFrame.addUser("User1");
        chatFrame.addUser("User2");
        String[] userlist = chatFrame.getUserlist();
        assertArrayEquals(new String[]{"User2", "User1"}, userlist);
    }
    
    @Test
    public void testDeleteUser() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        chatFrame.addUser("User1");
        chatFrame.addUser("User2");
        chatFrame.deleteUser("User1");
        String[] userlist = chatFrame.getUserlist();
        assertArrayEquals(new String[]{"User2"}, userlist);
    }
    
    @Test
    public void testActionPerformedChooseColor() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        chatFrame.addUser("User1");
        chatFrame.list.setSelectedValue("User1", true);
        chatFrame.actionPerformed(new ActionEvent(chatFrame, ActionEvent.ACTION_PERFORMED, "chooseColor"));
        // Verify if color style is changed
    }
    
    @Test
    public void testActionPerformedBgColor() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        chatFrame.actionPerformed(new ActionEvent(chatFrame, ActionEvent.ACTION_PERFORMED, "bgColor"));
        // Verify if background color is changed
    }
    
    @Test
    public void testActionPerformedWhisper() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        chatFrame.addUser("User1");
        chatFrame.list.setSelectedValue("User1", true);
        chatFrame.actionPerformed(new ActionEvent(chatFrame, ActionEvent.ACTION_PERFORMED, "whisper"));
        assertEquals("/w User1 ", chatFrame.txtInput.getText());
    }
}