import org.junit.*;
import static org.junit.Assert.*;

public class InternalChatFrameTest {

    private InternalChatFrame chatFrame;

    @Before
    public void setUp() {
        MainFrame parent = new MainFrame();
        chatFrame = new InternalChatFrame(parent);
    }

    @Test
    public void testAddText() {
        chatFrame.addText("Test message");
        assertEquals("Test message\n", chatFrame.getChatlog());
    }

    @Test
    public void testAddUser() {
        chatFrame.addUser("TestUser");
        String[] userList = chatFrame.getUserlist();
        assertEquals(1, userList.length);
        assertEquals("TestUser", userList[0]);
    }

    @Test
    public void testDeleteUser() {
        chatFrame.addUser("TestUser");
        chatFrame.deleteUser("TestUser");
        String[] userList = chatFrame.getUserlist();
        assertEquals(0, userList.length);
    }

    @Test
    public void testChangeStyle() {
        chatFrame.changeStyle(16, Color.RED, true, false, "TestStyle");
        // Do assertion here
    }

    // Add more test cases for other methods if needed

    

    // Add test cases for potential edge cases and exception handling scenarios
    // ...

    
}