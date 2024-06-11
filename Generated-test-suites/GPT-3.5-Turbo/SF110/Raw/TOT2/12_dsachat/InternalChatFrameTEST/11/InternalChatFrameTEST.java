import static org.junit.Assert.*;
import org.junit.Test;

public class InternalChatFrameTest {
    
    // Test case for constructor
    @Test
    public void testConstructor() {
        MainFrame parent = new MainFrame();
        InternalChatFrame chatFrame = new InternalChatFrame(parent);
        assertNotNull(chatFrame);
    }
    
    // Test case for addText method
    @Test
    public void testAddText() {
        MainFrame parent = new MainFrame();
        InternalChatFrame chatFrame = new InternalChatFrame(parent);
        chatFrame.addText("Test message");
        assertEquals("Test message\n", chatFrame.getChatlog());
    }
    
    // Test case for addUser method
    @Test
    public void testAddUser() {
        MainFrame parent = new MainFrame();
        InternalChatFrame chatFrame = new InternalChatFrame(parent);
        chatFrame.addUser("TestUser");
        assertTrue(chatFrame.getUserlist().length == 1);
    }
    
    // Test case for deleteUser method
    @Test
    public void testDeleteUser() {
        MainFrame parent = new MainFrame();
        InternalChatFrame chatFrame = new InternalChatFrame(parent);
        chatFrame.addUser("TestUser");
        chatFrame.deleteUser("TestUser");
        assertTrue(chatFrame.getUserlist().length == 0);
    }
    
    // Test case for actionPerformed method
    @Test
    public void testActionPerformed() {
        MainFrame parent = new MainFrame();
        InternalChatFrame chatFrame = new InternalChatFrame(parent);
        ActionEvent e = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "chooseColor");
        chatFrame.addUser("TestUser");
        chatFrame.actionPerformed(e);
        // Add assertions for the actionPerformed method
    }
    
    // Test case for mousePressed method
    @Test
    public void testMousePressed() {
        MainFrame parent = new MainFrame();
        InternalChatFrame chatFrame = new InternalChatFrame(parent);
        MouseEvent e = new MouseEvent(chatFrame.list, MouseEvent.MOUSE_PRESSED, System.currentTimeMillis(), 0, 10, 10, 1, false);
        chatFrame.mousePressed(e);
        // Add assertions for the mousePressed method
    }
    
    // Add more test cases for the remaining methods
    
}