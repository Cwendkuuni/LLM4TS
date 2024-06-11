import org.junit.*;
import static org.junit.Assert.*;

public class InternalChatFrameTest {

    @Test
    public void testAddText() {
        InternalChatFrame frame = new InternalChatFrame(null);
        frame.addText("Test Message");

        String expected = "Test Message\n";
        String actual = frame.getChatlog();
        assertEquals(expected, actual);
    }

    @Test
    public void testAddUser() {
        InternalChatFrame frame = new InternalChatFrame(null);
        frame.addUser("User123");

        String[] expected = {"User123"};
        String[] actual = frame.getUserlist();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testDeleteUser() {
        InternalChatFrame frame = new InternalChatFrame(null);
        frame.addUser("User123");
        frame.deleteUser("User123");

        String[] expected = {};
        String[] actual = frame.getUserlist();
        assertArrayEquals(expected, actual);
    }
}