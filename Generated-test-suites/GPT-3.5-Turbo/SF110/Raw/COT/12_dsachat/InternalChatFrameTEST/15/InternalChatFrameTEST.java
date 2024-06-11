import org.junit.*;
import static org.junit.Assert.*;

public class InternalChatFrameTest {

    @Test
    public void testAddText() {
        InternalChatFrame chatFrame = new InternalChatFrame(null);
        chatFrame.addText("Test message");
        assertEquals("Test message\n", chatFrame.getChatlog());
    }

    @Test
    public void testAddUser() {
        InternalChatFrame chatFrame = new InternalChatFrame(null);
        chatFrame.addUser("User1");
        assertEquals("User1", chatFrame.getUserlist()[0]);
    }

    @Test
    public void testDeleteUser() {
        InternalChatFrame chatFrame = new InternalChatFrame(null);
        chatFrame.addUser("User1");
        chatFrame.deleteUser("User1");
        assertEquals(0, chatFrame.getUserlist().length);
    }

    @Test
    public void testGetUserlist() {
        InternalChatFrame chatFrame = new InternalChatFrame(null);
        chatFrame.addUser("User1");
        chatFrame.addUser("User2");
        assertEquals(2, chatFrame.getUserlist().length);
    }

    @Test
    public void testAddText_NullInput() {
        InternalChatFrame chatFrame = new InternalChatFrame(null);
        chatFrame.addText(null);
        assertEquals("\n", chatFrame.getChatlog());
    }

    @Test(expected = BadLocationException.class)
    public void testAddText_BadLocationException() throws BadLocationException {
        InternalChatFrame chatFrame = new InternalChatFrame(null);
        chatFrame.addText("Test message");
        // Try to insert a message at a bad location
        chatFrame.doc.insertString(-1, "Bad location", chatFrame.doc.getStyle("regular"));
    }

    @Test
    public void testAddUser_DuplicateUser() {
        InternalChatFrame chatFrame = new InternalChatFrame(null);
        chatFrame.addUser("User1");
        chatFrame.addUser("User1");
        assertEquals(1, chatFrame.getUserlist().length);
    }

    @Test
    public void testDeleteUser_UserNotFound() {
        InternalChatFrame chatFrame = new InternalChatFrame(null);
        chatFrame.addUser("User1");
        chatFrame.deleteUser("User2");
        assertEquals(1, chatFrame.getUserlist().length);
    }
}