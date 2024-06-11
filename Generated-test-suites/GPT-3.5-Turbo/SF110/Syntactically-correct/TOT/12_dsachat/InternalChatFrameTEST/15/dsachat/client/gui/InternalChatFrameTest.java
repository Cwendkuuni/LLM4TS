
import org.junit.Test;
import static org.junit.Assert.*;

public class InternalChatFrameTest {

    @Test
    public void testAddText() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        chatFrame.addText("Hello");
        String expectedText = "Hello\n";
        assertEquals(expectedText, chatFrame.getChatlog());
    }

    @Test
    public void testAddTextWithStyle() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        chatFrame.addText("You: Hello");
        String expectedText = "You: Hello\n";
        assertEquals(expectedText, chatFrame.getChatlog());
    }

    @Test
    public void testAddUser() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        chatFrame.addUser("User1");
        String[] expectedUserList = {"User1"};
        assertArrayEquals(expectedUserList, chatFrame.getUserlist());
    }

    @Test
    public void testDeleteUser() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        chatFrame.addUser("User1");
        chatFrame.deleteUser("User1");
        String[] expectedUserList = {};
        assertArrayEquals(expectedUserList, chatFrame.getUserlist());
    }

    @Test
    public void testChangeStyle() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        chatFrame.addUser("User1");
        chatFrame.changeStyle(12, Color.RED, true, false, "User1");
        // TODO: Add assertion for style change
    }

    // Add more test cases for other methods...

}

