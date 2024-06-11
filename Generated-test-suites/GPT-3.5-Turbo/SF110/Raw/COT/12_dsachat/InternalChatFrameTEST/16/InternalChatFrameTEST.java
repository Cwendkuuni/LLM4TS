
import org.junit.*;
import static org.junit.Assert.*;

public class InternalChatFrameTest {
    
    private InternalChatFrame chatFrame;
    private MainFrame parent;
    
    @Before
    public void setup() {
        parent = new MainFrame();
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
        assertEquals("User1", chatFrame.getUserlist()[0]);
    }
    
    @Test
    public void testDeleteUser() {
        chatFrame.addUser("User1");
        chatFrame.deleteUser("User1");
        assertEquals(0, chatFrame.getUserlist().length);
    }
    
    @Test
    public void testActionPerformed_chooseColor() {
        chatFrame.addUser("User1");
        ActionEvent e = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "chooseColor");
        chatFrame.actionPerformed(e);
        // Verify that the color for "User1" has changed
    }
    
    @Test
    public void testActionPerformed_bgColor() {
        ActionEvent e = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "bgColor");
        chatFrame.actionPerformed(e);
        // Verify that the background color has changed
    }
    
    @Test
    public void testActionPerformed_whisper() {
        chatFrame.addUser("User1");
        ActionEvent e = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "whisper");
        chatFrame.actionPerformed(e);
        // Verify that the text input is set to "/w User1 "
    }
    
    // Additional test cases for other methods can be added here
    
    @After
    public void teardown() {
        chatFrame = null;
        parent = null;
    }
}
