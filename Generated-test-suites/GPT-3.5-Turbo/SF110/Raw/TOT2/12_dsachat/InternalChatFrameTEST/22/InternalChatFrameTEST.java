import static org.junit.Assert.*;
import org.junit.Test;

public class InternalChatFrameTest {
    
    // Test case for addText method
    @Test
    public void testAddText() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        chatFrame.addText("Test message");
        assertEquals("Test message\n", chatFrame.getChatlog());
    }
    
    // Test case for addUser method
    @Test
    public void testAddUser() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        chatFrame.addUser("TestUser");
        String[] userList = chatFrame.getUserlist();
        assertEquals("TestUser", userList[0]);
    }
    
    // Test case for deleteUser method
    @Test
    public void testDeleteUser() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        chatFrame.addUser("TestUser");
        chatFrame.deleteUser("TestUser");
        String[] userList = chatFrame.getUserlist();
        assertEquals(0, userList.length);
    }
    
    // Test case for actionPerformed method - chooseColor
    @Test
    public void testActionPerformed_ChooseColor() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        chatFrame.addUser("TestUser");
        assertEquals(12, chatFrame.doc.getStyle("TestUser").getAttribute("size"));
    }
    
    // Add more test cases for other methods as needed
    
}