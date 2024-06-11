import org.junit.Test;
import static org.junit.Assert.*;

public class InternalChatFrameTest {

    @Test
    public void testAddTextTypical() {
        // Test typical scenario: adding a regular line of text
        InternalChatFrame chatFrame = new InternalChatFrame(null);
        chatFrame.addText("This is a regular line");
        String expected = chatFrame.getChatlog();
        assertTrue(expected.contains("This is a regular line"));
    }

    @Test
    public void testAddTextError() {
        // Test error scenario: adding a line without any content
        InternalChatFrame chatFrame = new InternalChatFrame(null);
        chatFrame.addText("");
        String expected = chatFrame.getChatlog();
        assertFalse(expected.contains("\n"));  // No new line added for empty line
    }

    @Test
    public void testAddUserTypical() {
        // Test typical scenario: adding a user to the user list
        InternalChatFrame chatFrame = new InternalChatFrame(null);
        chatFrame.addUser("User1");
        String[] userList = chatFrame.getUserlist();
        assertEquals(1, userList.length);
        assertEquals("User1", userList[0]);
    }

    @Test
    public void testAddUserEdge() {
        // Test edge scenario: adding multiple users to the user list
        InternalChatFrame chatFrame = new InternalChatFrame(null);
        chatFrame.addUser("User1");
        chatFrame.addUser("User2");
        chatFrame.addUser("User3");
        String[] userList = chatFrame.getUserlist();
        assertEquals(3, userList.length);
        assertEquals("User3", userList[0]);  // Users are added at the top of the list
        assertEquals("User2", userList[1]);
        assertEquals("User1", userList[2]);
    }

    @Test
    public void testAddUserError() {
        // Test error scenario: adding a user with an empty name
        InternalChatFrame chatFrame = new InternalChatFrame(null);
        chatFrame.addUser("");
        String[] userList = chatFrame.getUserlist();
        assertEquals(0, userList.length);
    }

    @Test
    public void testDeleteUserTypical() {
        // Test typical scenario: deleting a user from the user list
        InternalChatFrame chatFrame = new InternalChatFrame(null);
        chatFrame.addUser("User1");
        chatFrame.addUser("User2");
        chatFrame.deleteUser("User1");
        String[] userList = chatFrame.getUserlist();
        assertEquals(1, userList.length);
        assertEquals("User2", userList[0]);
    }

    @Test
    public void testDeleteUserEdge() {
        // Test edge scenario: deleting the last user from the user list
        InternalChatFrame chatFrame = new InternalChatFrame(null);
        chatFrame.addUser("User1");
        chatFrame.deleteUser("User1");
        String[] userList = chatFrame.getUserlist();
        assertEquals(0, userList.length);
    }

    @Test
    public void testDeleteUserError() {
        // Test error scenario: deleting a non-existent user from the user list
        InternalChatFrame chatFrame = new InternalChatFrame(null);
        chatFrame.addUser("User1");
        chatFrame.deleteUser("User2");  // User2 does not exist
        String[] userList = chatFrame.getUserlist();
        assertEquals(1, userList.length);  // User1 still exists
        assertEquals("User1", userList[0]);
    }
}