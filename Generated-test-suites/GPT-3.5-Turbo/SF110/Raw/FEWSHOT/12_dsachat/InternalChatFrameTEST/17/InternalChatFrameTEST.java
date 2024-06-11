package dsachat.client.gui;

import org.junit.*;
import static org.junit.Assert.*;

public class InternalChatFrameTest {
    private InternalChatFrame internalChatFrame;
    private MainFrame parent;
    
    @Before
    public void setUp() {
        parent = new MainFrame();
        internalChatFrame = new InternalChatFrame(parent);
    }
    
    @Test
    public void testAddText() {
        internalChatFrame.addText("Hello");
        assertEquals("Hello\n", internalChatFrame.getChatlog());
        internalChatFrame.addText("User1: Hello");
        assertEquals("Hello\nUser1: Hello\n", internalChatFrame.getChatlog());
        internalChatFrame.addText("You whispered to User1: Hello");
        assertEquals("Hello\nUser1: Hello\nYou whispered to User1: Hello\n", internalChatFrame.getChatlog());
        internalChatFrame.addText("*You* _whispered_ to User1: Hello");
        assertEquals("Hello\nUser1: Hello\nYou whispered to User1: Hello\n*You* _whispered_ to User1: Hello\n", internalChatFrame.getChatlog());
    }
    
    @Test
    public void testAddUser() {
        internalChatFrame.addUser("User1");
        assertEquals(1, internalChatFrame.getUserlist().length);
        assertEquals("User1", internalChatFrame.getUserlist()[0]);
    }
    
    @Test
    public void testDeleteUser() {
        internalChatFrame.addUser("User1");
        internalChatFrame.deleteUser("User1");
        assertEquals(0, internalChatFrame.getUserlist().length);
    }
}