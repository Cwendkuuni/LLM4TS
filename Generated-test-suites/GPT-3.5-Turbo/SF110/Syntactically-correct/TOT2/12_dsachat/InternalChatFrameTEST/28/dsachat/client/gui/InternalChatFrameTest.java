package dsachat.client.gui;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class InternalChatFrameTest {

    private InternalChatFrame chatFrame;

    @Before
    public void setUp() {
        MainFrame parent = new MainFrame();
        chatFrame = new InternalChatFrame(parent);
    }

    @Test
    public void testAddText() {
        String line = "Test message";
        chatFrame.addText(line);
        assertEquals("Test message\n", chatFrame.getChatlog());
    }

    @Test
    public void testAddUser() {
        String user = "TestUser";
        chatFrame.addUser(user);
        String[] userList = chatFrame.getUserlist();
        assertTrue(userList.length > 0 && userList[0].equals(user));
    }

    @Test
    public void testDeleteUser() {
        String user = "TestUser";
        chatFrame.addUser(user);
        chatFrame.deleteUser(user);
        String[] userList = chatFrame.getUserlist();
        assertTrue(userList.length == 0);
    }

    @Test
    public void testActionPerformed() {
        // Test implementation of actionPerformed method
    }

    @Test
    public void testMouseClicked() {
        // Test implementation of mouseClicked method
    }

    @Test
    public void testMouseEntered() {
        // Test implementation of mouseEntered method
    }

    @Test
    public void testMouseExited() {
        // Test implementation of mouseExited method
    }

    @Test
    public void testMousePressed() {
        // Test implementation of mousePressed method
    }

    @Test
    public void testMouseReleased() {
        // Test implementation of mouseReleased method
    }

    @Test
    public void testGetChatlog() {
        String expected = chatFrame.getChatlog();
        assertNotNull(expected);
    }

}