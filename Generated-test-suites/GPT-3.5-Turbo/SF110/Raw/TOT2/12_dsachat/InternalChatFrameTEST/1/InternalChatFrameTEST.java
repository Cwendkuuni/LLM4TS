import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class InternalChatFrameTest {
    
    private InternalChatFrame chatFrame;
    
    @Before
    public void setUp() {
        chatFrame = new InternalChatFrame(new MainFrame());
    }
    
    @Test
    public void testAddText() {
        chatFrame.addText("Test message");
        assertEquals("Test message\n", chatFrame.getChatlog());
    }
    
    @Test
    public void testAddUser() {
        chatFrame.addUser("TestUser");
        String[] userList = chatFrame.getUserlist();
        assertEquals(1, userList.length);
        assertEquals("TestUser", userList[0]);
    }
    
    @Test
    public void testDeleteUser() {
        chatFrame.addUser("TestUser");
        chatFrame.deleteUser("TestUser");
        String[] userList = chatFrame.getUserlist();
        assertEquals(0, userList.length);
    }
    
    @Test
    public void testActionPerformedChooseColor() {
        // Test when choosing color
    }
    
    @Test
    public void testActionPerformedBgColor() {
        // Test when choosing background color
    }
    
    @Test
    public void testActionPerformedWhisper() {
        // Test whisper action
    }
    
}