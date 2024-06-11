import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class InternalChatFrameTest {
    
    private MainFrame parent;
    private InternalChatFrame chatFrame;
    
    @Before
    public void setUp() {
        parent = new MainFrame();
        chatFrame = new InternalChatFrame(parent);
    }
    
    @Test
    public void testAddText() {
        chatFrame.addText("Test Message");
        assertEquals("Test Message\n", chatFrame.getChatlog());
    }
    
    @Test
    public void testAddUser() {
        chatFrame.addUser("TestUser");
        String[] userList = chatFrame.getUserlist();
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
    public void testActionPerformed() {
        // ToDo: Write test cases for actionPerformed method
    }
    
    @Test
    public void testMouseClicked() {
        // ToDo: Write test cases for mouseClicked method
    }
    
    @Test
    public void testMouseEntered() {
        // ToDo: Write test cases for mouseEntered method
    }
    
    @Test
    public void testMouseExited() {
        // ToDo: Write test cases for mouseExited method
    }
    
    @Test
    public void testMousePressed() {
        // ToDo: Write test cases for mousePressed method
    }
    
    @Test
    public void testMouseReleased() {
        // ToDo: Write test cases for mouseReleased method
    }
    
    @Test
    public void testGetChatlog() {
        chatFrame.addText("Test Message 1");
        chatFrame.addText("Test Message 2");
        assertEquals("Test Message 1\nTest Message 2\n", chatFrame.getChatlog());
    }
}