import static org.junit.Assert.*;
import org.junit.Test;

public class InternalChatFrameTest {

    @Test
    public void testAddText() {
        // Test adding regular text
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        chatFrame.addText("Test message");
        assertEquals("Test message\n", chatFrame.getChatlog());
        
        // Test adding styled text
        chatFrame.addText("Test *bold* and _italic_ message");
        assertEquals("Test bold and italic message\n", chatFrame.getChatlog());
    }

    @Test
    public void testAddUser() {
        // Test adding a user to the list
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        chatFrame.addUser("User1");
        String[] userList = chatFrame.getUserlist();
        assertTrue(userList.length == 1);
        assertEquals("User1", userList[0]);
        
        // Test adding multiple users to the list
        chatFrame.addUser("User2");
        chatFrame.addUser("User3");
        userList = chatFrame.getUserlist();
        assertTrue(userList.length == 3);
        assertEquals("User3", userList[0]);
    }

    @Test
    public void testDeleteUser() {
        // Test deleting a user from the list
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        chatFrame.addUser("User1");
        chatFrame.addUser("User2");
        chatFrame.deleteUser("User1");
        String[] userList = chatFrame.getUserlist();
        assertTrue(userList.length == 1);
        assertEquals("User2", userList[0]);
    }

    // Add more test cases for the remaining methods

}