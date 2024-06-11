package dsachat.client.gui;
import dsachat.client.gui.InternalChatFrame;
import dsachat.client.gui.MainFrame;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class InternalChatFrameTest {
    private InternalChatFrame chatFrame;
    
    @Before
    public void setUp() {
        chatFrame = new InternalChatFrame(new MainFrame());
    }
    
    @Test
    public void testAddText() {
        chatFrame.addText("Test Text");
        assertEquals("Test Text\n", chatFrame.getChatlog());
    }
    
    @Test
    public void testAddUser() {
        chatFrame.addUser("TestUser");
        String[] userList = chatFrame.getUserlist();
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
    public void testActionPerformed() {
        // Need to simulate user clicking on "chooseColor", "bgColor", and "whisper" options
        // Ensure the expected behavior occurs
    }
    
    // Add more test cases for other methods in the class
    
    @Test
    public void testMousePressed() {
        // Need to simulate a mousePressed event and check if the popup is shown
    }
    
    @Test
    public void testMouseReleased() {
        // Need to simulate a mouseReleased event and check if the popup is shown
    }
}