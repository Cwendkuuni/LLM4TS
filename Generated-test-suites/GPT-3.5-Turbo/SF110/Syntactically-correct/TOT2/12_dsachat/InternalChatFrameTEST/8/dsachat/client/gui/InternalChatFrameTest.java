package dsachat.client.gui;
import org.junit.*;
import static org.junit.Assert.*;
import org.junit.Test;

public class InternalChatFrameTest {
    
    InternalChatFrame internalChatFrame;
    MainFrame parent;
    
    @Before
    public void setUp() {
        parent = new MainFrame();
        internalChatFrame = new InternalChatFrame(parent);
    }
    
    @Test
    public void testAddText() {
        String line = "Test message";
        internalChatFrame.addText(line);
        assertEquals(line + "\n", internalChatFrame.getChatlog());
    }
    
    @Test
    public void testAddUser() {
        String user = "TestUser";
        internalChatFrame.addUser(user);
        assertTrue(internalChatFrame.getChatlog().contains(user));
    }
    
    @Test
    public void testDeleteUser() {
        String user = "TestUser";
        internalChatFrame.addUser(user);
        internalChatFrame.deleteUser(user);
        assertFalse(internalChatFrame.getChatlog().contains(user));
    }
    
    @Test
    public void testGetUserlist() {
        String user1 = "User1";
        String user2 = "User2";
        internalChatFrame.addUser(user1);
        internalChatFrame.addUser(user2);
        String[] userList = internalChatFrame.getUserlist();
        assertEquals(user1, userList[0]);
        assertEquals(user2, userList[1]);
    }
    
    @After
    public void tearDown() {
        internalChatFrame = null;
        parent = null;
    }
}