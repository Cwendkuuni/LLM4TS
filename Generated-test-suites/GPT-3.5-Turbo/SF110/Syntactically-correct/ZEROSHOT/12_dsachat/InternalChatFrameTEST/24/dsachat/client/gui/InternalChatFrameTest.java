import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class InternalChatFrameTest {
    
    private InternalChatFrame chatFrame;
    private MainFrame parent;
    
    @Before
    public void setUp() {
        parent = new MainFrame();
        chatFrame = new InternalChatFrame(parent);
    }
    
    @Test
    public void testAddText() {
        chatFrame.addText("Test Message");
        String chatlog = chatFrame.getChatlog();
        assertTrue(chatlog.contains("Test Message"));
    }
    
    @Test
    public void testAddUser() {
        chatFrame.addUser("User1");
        String[] userlist = chatFrame.getUserlist();
        assertTrue(userlist.length == 1);
        assertEquals("User1", userlist[0]);
    }
    
    @Test
    public void testDeleteUser() {
        chatFrame.addUser("User1");
        chatFrame.addUser("User2");
        chatFrame.deleteUser("User1");
        String[] userlist = chatFrame.getUserlist();
        assertTrue(userlist.length == 1);
        assertNotEquals("User1", userlist[0]);
    }
    
    @Test
    public void testActionPerformed() {
        // Test chooseColor
        chatFrame.addUser("User1");
        chatFrame.list.setSelectedIndex(0);
        chatFrame.actionPerformed(null);
        
        // Test bgColor
        chatFrame.actionPerformed(null);
        
        // Test whisper
        chatFrame.list.setSelectedIndex(0);
        chatFrame.actionPerformed(null);
        String inputText = chatFrame.txtInput.getText();
        assertTrue(inputText.contains("/w User1"));
    }
    
    @Test
    public void testMouseClicked() {
        // No assertion, only testing method execution
        chatFrame.mouseClicked(null);
    }

    @Test
    public void testMouseEntered() {
        // No assertion, only testing method execution
        chatFrame.mouseEntered(null);
    }
    
    @Test
    public void testMouseExited() {
        // No assertion, only testing method execution
        chatFrame.mouseExited(null);
    }
    
    @Test
    public void testMousePressed() {
        // No assertion, only testing method execution
        chatFrame.mousePressed(null);
    }
    
    @Test
    public void testMouseReleased() {
        // No assertion, only testing method execution
        chatFrame.mouseReleased(null);
    }
}