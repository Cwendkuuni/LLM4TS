import org.junit.Test;
import static org.junit.Assert.*;

public class InternalChatFrameTest {

    @Test
    public void testAddText() {
        // Test adding text with different styling
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        chatFrame.addText("Hello");
        assertEquals("Hello\n", chatFrame.getChatlog());
    }

    @Test
    public void testAddUser() {
        // Test adding a user to the user list
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        chatFrame.addUser("User1");
        assertEquals("User1", chatFrame.getUserlist()[0]);
    }

    @Test
    public void testDeleteUser() {
        // Test deleting a user from the user list
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        chatFrame.addUser("User1");
        chatFrame.deleteUser("User1");
        assertEquals(0, chatFrame.getUserlist().length);
    }

    // More test cases can be added for the remaining methods in InternalChatFrame class

}