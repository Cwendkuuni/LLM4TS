import org.junit.Test;
import static org.junit.Assert.*;

public class InternalChatFrameTest {
    
    @Test
    public void testAddText() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        // Test adding regular text
        chatFrame.addText("Hello, world!");
        assertEquals("Hello, world!\n", chatFrame.getChatlog());
        
        // Test adding styled text with italics
        chatFrame.addText("This is in italics");
        assertEquals("Hello, world!\nThis is in italics\n", chatFrame.getChatlog());
        
        // Test adding styled text with bold
        chatFrame.addText("This is bold");
        assertEquals("Hello, world!\nThis is in italics\nThis is bold\n", chatFrame.getChatlog());
    }
    
    @Test
    public void testAddUser() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        // Test adding a user
        chatFrame.addUser("User1");
        String[] userList = chatFrame.getUserlist();
        assertEquals(1, userList.length);
        assertEquals("User1", userList[0]);
    }
    
    @Test
    public void testDeleteUser() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        // Test deleting a user
        chatFrame.addUser("User1");
        chatFrame.addUser("User2");
        chatFrame.deleteUser("User1");
        String[] userList = chatFrame.getUserlist();
        assertEquals(1, userList.length);
        assertEquals("User2", userList[0]);
    }
    
    // Add more test cases for the remaining methods...

}