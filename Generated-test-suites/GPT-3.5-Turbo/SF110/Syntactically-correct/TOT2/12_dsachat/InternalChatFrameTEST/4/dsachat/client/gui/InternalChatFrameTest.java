package dsachat.client.gui;
import org.junit.Test;
import static org.junit.Assert.*;

public class InternalChatFrameTest {
    
    @Test
    public void testAddText() {
        // Test typical functionality
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        chatFrame.addText("Test message");
        assertEquals("Test message\n", chatFrame.getChatlog());
        
        // Test edge case - empty message
        chatFrame.addText("");
        assertEquals("Test message\n\n", chatFrame.getChatlog());
        
        // Test exception handling - BadLocationException
        // This scenario can't be directly tested with JUnit as it requires triggering an exception
    }
    
    @Test
    public void testAddUser() {
        // Test typical functionality
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        chatFrame.addUser("User1");
        chatFrame.addUser("User2");
        String[] userList = chatFrame.getUserlist();
        assertEquals("User2", userList[0]);
        assertEquals("User1", userList[1]);
        
        // Test edge case - adding duplicate user
        chatFrame.addUser("User1");
        userList = chatFrame.getUserlist();
        assertEquals(2, userList.length);
        
        // Test edge case - adding multiple users
        chatFrame.addUser("User3");
        chatFrame.addUser("User4");
        userList = chatFrame.getUserlist();
        assertEquals(4, userList.length);
    }
    
    @Test
    public void testDeleteUser() {
        // Test typical functionality
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        chatFrame.addUser("User1");
        chatFrame.addUser("User2");
        chatFrame.deleteUser("User1");
        String[] userList = chatFrame.getUserlist();
        assertEquals("User2", userList[0]);
        
        // Test edge case - deleting non-existing user
        chatFrame.deleteUser("User3");
        userList = chatFrame.getUserlist();
        assertEquals(1, userList.length);
        
        // Test edge case - deleting last user
        chatFrame.deleteUser("User2");
        userList = chatFrame.getUserlist();
        assertEquals(0, userList.length);
    }
    
    // Additional test cases for other methods can be added here
    
}