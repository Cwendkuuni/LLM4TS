package dsachat.client.gui;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.awt.Color;

public class InternalChatFrameTest {
    
    private InternalChatFrame chatFrame;
    
    @Before
    public void setup() {
        //Setup code here
        chatFrame = new InternalChatFrame(new MainFrame());
    }
    
    @Test
    public void testAddText() {
        chatFrame.addText("This is a test");
        assertEquals("This is a test\n", chatFrame.getChatlog());
    }
    
    @Test
    public void testAddUser() {
        chatFrame.addUser("User1");
        assertEquals(1, chatFrame.getUserlist().length);
        assertEquals("User1", chatFrame.getUserlist()[0]);
    }
    
    @Test
    public void testDeleteUser() {
        chatFrame.addUser("User1");
        chatFrame.deleteUser("User1");
        assertEquals(0, chatFrame.getUserlist().length);
    }
    
    @Test
    public void testChangeStyle() {
        chatFrame.addUser("User1");
        chatFrame.changeStyle(12, Color.BLUE, true, false, "User1");
        //Assert style changes
    }
    
    
}