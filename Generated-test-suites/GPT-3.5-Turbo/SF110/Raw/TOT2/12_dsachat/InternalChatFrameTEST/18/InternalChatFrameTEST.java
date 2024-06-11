import org.junit.Test;
import static org.junit.Assert.*;

public class InternalChatFrameTest {

    @Test
    public void testConstructor() {
        MainFrame mainFrame = new MainFrame();
        InternalChatFrame internalChatFrame = new InternalChatFrame(mainFrame);
        assertNotNull(internalChatFrame);
        assertEquals("Chat", internalChatFrame.getTitle());
    }

    @Test
    public void testAddText() {
        MainFrame mainFrame = new MainFrame();
        InternalChatFrame internalChatFrame = new InternalChatFrame(mainFrame);
        internalChatFrame.addText("Test Message");
        assertEquals("Test Message\n", internalChatFrame.getChatlog());
    }

    @Test
    public void testAddUser() {
        MainFrame mainFrame = new MainFrame();
        InternalChatFrame internalChatFrame = new InternalChatFrame(mainFrame);
        internalChatFrame.addUser("TestUser");
        String[] userList = internalChatFrame.getUserlist();
        assertEquals(1, userList.length);
        assertEquals("TestUser", userList[0]);
    }

    @Test
    public void testDeleteUser() {
        MainFrame mainFrame = new MainFrame();
        InternalChatFrame internalChatFrame = new InternalChatFrame(mainFrame);
        internalChatFrame.addUser("TestUser1");
        internalChatFrame.addUser("TestUser2");
        internalChatFrame.deleteUser("TestUser1");
        String[] userList = internalChatFrame.getUserlist();
        assertEquals(1, userList.length);
        assertEquals("TestUser2", userList[0]);
    }

    // Add more test cases for other methods as needed

}