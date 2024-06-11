package dsachat.client.gui;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class InternalChatFrameTest {

    private InternalChatFrame internalChatFrame;

    @Before
    public void setUp() {
        MainFrame parent = new MainFrame();
        internalChatFrame = new InternalChatFrame(parent);
    }

    @Test
    public void testAddText() {
        // Test adding regular text
        internalChatFrame.addText("Hello");
        assertEquals("Hello\n", internalChatFrame.getChatlog());
        
        // Test adding text with formatting
        internalChatFrame.addText("You *bold* _italic_");
        assertEquals("Hello\nYou *bold* _italic_\n", internalChatFrame.getChatlog());
        
        // Test adding text with edge cases
        internalChatFrame.addText("*bold*");
        assertEquals("Hello\nYou *bold* _italic_\n*bold*\n", internalChatFrame.getChatlog());
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
    
    // Add more test cases for other methods...

}