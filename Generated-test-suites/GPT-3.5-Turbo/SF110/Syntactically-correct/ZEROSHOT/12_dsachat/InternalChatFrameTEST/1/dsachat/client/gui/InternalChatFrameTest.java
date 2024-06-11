import org.junit.*;
import static org.junit.Assert.*;

public class InternalChatFrameTest {
    
    @Test
    public void testAddText() {
        InternalChatFrame chatFrame = new InternalChatFrame(null);
        chatFrame.addText("Test Message");
        
        String chatlog = chatFrame.getChatlog();
        assertTrue(chatlog.contains("Test Message"));
    }
    
    @Test
    public void testAddUser() {
        InternalChatFrame chatFrame = new InternalChatFrame(null);
        chatFrame.addUser("User1");
        
        String[] userlist = chatFrame.getUserlist();
        assertEquals(1, userlist.length);
        assertEquals("User1", userlist[0]);
    }
    
    @Test
    public void testDeleteUser() {
        InternalChatFrame chatFrame = new InternalChatFrame(null);
        chatFrame.addUser("User1");
        chatFrame.addUser("User2");
        chatFrame.deleteUser("User1");
        
        String[] userlist = chatFrame.getUserlist();
        assertEquals(1, userlist.length);
        assertEquals("User2", userlist[0]);
    }
    
    @Test
    public void testActionPerformed_ChooseColor() {
        InternalChatFrame chatFrame = new InternalChatFrame(null);
        chatFrame.addUser("User1");
        chatFrame.list.setSelectedIndex(0);
        
        chatFrame.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "chooseColor"));
        
        // Verify the color has changed for the selected user
        // This test assumes that the JColorChooser always returns a non-null color
        // and that the changeStyle() method correctly changes the style of the user
    }
    
    @Test
    public void testActionPerformed_BgColor() {
        InternalChatFrame chatFrame = new InternalChatFrame(null);
        chatFrame.addUser("User1");
        chatFrame.showTxt.setBackground(Color.WHITE);
        chatFrame.list.setSelectedIndex(0);
        
        chatFrame.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "bgColor"));
        
        // Verify the background color has changed for the JTextPane
        // This test assumes that the JColorChooser always returns a non-null color
        // and that the changeStyle() method correctly changes the background color
    }
    
    @Test
    public void testActionPerformed_Whisper() {
        InternalChatFrame chatFrame = new InternalChatFrame(null);
        chatFrame.addUser("User1");
        chatFrame.list.setSelectedIndex(0);
        
        chatFrame.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "whisper"));
        
        String inputText = chatFrame.txtInput.getText();
        assertTrue(inputText.startsWith("/w User1"));
    }
    
    // Add more test cases for the remaining methods in InternalChatFrame class
    
}