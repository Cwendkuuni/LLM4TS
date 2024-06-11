import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.awt.Color;

public class InternalChatFrameTest {

    private InternalChatFrame chatFrame;

    @Before
    public void setUp() {
        // Initialize InternalChatFrame object with a dummy MainFrame parent
        MainFrame parent = new MainFrame();
        chatFrame = new InternalChatFrame(parent);
    }

    @Test
    public void testAddText() {
        // Test basic addText functionality
        chatFrame.addText("Test message");
        assertEquals("Test message\n", chatFrame.getChatlog());

        // Test empty input line
        chatFrame.addText("");
        assertEquals("Test message\n\n", chatFrame.getChatlog());

        // Test line without a colon
        chatFrame.addText("Invalid line");
        assertEquals("Test message\n\nInvalid line\n", chatFrame.getChatlog());

        // Test line with a colon, but missing style name or message
        chatFrame.addText(":");
        assertEquals("Test message\n\nInvalid line\n:\n", chatFrame.getChatlog());

        // Test line with a whisper style
        chatFrame.addText("whispers:");
        assertEquals("Test message\n\nInvalid line\n:\nwhispers:\n", chatFrame.getChatlog());

        // Test line with style name starting with "You "
        chatFrame.addText("You Test:");
        assertEquals("Test message\n\nInvalid line\n:\nwhispers:\nYou Test:\n", chatFrame.getChatlog());

        // Test line with style name starting with "*"
        chatFrame.addText("*Test:");
        assertEquals("Test message\n\nInvalid line\n:\nwhispers:\nYou Test:\n*Test:\n", chatFrame.getChatlog());

        // Test line with alternating italic and non-italic segments
        chatFrame.addText("Italic*_Segments:");
        assertEquals("Test message\n\nInvalid line\n:\nwhispers:\nYou Test:\n*Test:\nItalic*_Segments:\n", chatFrame.getChatlog());

        // Test line with alternating bold and non-bold segments
        chatFrame.addText("Bold+_Segments:");
        assertEquals("Test message\n\nInvalid line\n:\nwhispers:\nYou Test:\n*Test:\nItalic*_Segments:\nBold+_Segments:\n", chatFrame.getChatlog());
    }

    @Test
    public void testAddUser() {
        // Test basic addUser functionality
        chatFrame.addUser("User1");
        String[] userlist = chatFrame.getUserlist();
        assertArrayEquals(new String[]{"User1"}, userlist);

        // Test adding a user with an empty name
        chatFrame.addUser("");
        userlist = chatFrame.getUserlist();
        assertArrayEquals(new String[]{"", "User1"}, userlist);

        // Test adding a user with a name that already exists in the user list
        chatFrame.addUser("User1");
        userlist = chatFrame.getUserlist();
        assertArrayEquals(new String[]{"", "User1"}, userlist);
    }

    @Test
    public void testDeleteUser() {
        // Test basic deleteUser functionality
        chatFrame.addUser("User1");
        chatFrame.addUser("User2");
        chatFrame.deleteUser("User1");
        String[] userlist = chatFrame.getUserlist();
        assertArrayEquals(new String[]{"User2"}, userlist);

        // Test deleting a user that does not exist in the user list
        chatFrame.deleteUser("User1");
        userlist = chatFrame.getUserlist();
        assertArrayEquals(new String[]{"User2"}, userlist);
    }

    @Test
    public void testGetUserlist() {
        // Test empty user list
        String[] emptyList = chatFrame.getUserlist();
        assertEquals(0, emptyList.length);

        // Test user list after adding users
        chatFrame.addUser("User1");
        chatFrame.addUser("User2");
        String[] userList = chatFrame.getUserlist();
        assertArrayEquals(new String[]{"User1", "User2"}, userList);

        // Test user list after deleting users
        chatFrame.deleteUser("User1");
        userList = chatFrame.getUserlist();
        assertArrayEquals(new String[]{"User2"}, userList);
    }

    @Test
    public void testGetChatlog() {
        // Test empty chat log
        String emptyLog = chatFrame.getChatlog();
        assertEquals("", emptyLog);

        // Test chat log after adding messages
        chatFrame.addText("Test message");
        String log = chatFrame.getChatlog();
        assertEquals("Test message\n", log);
    }
}