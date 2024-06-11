import org.junit.*;
import static org.junit.Assert.*;

public class InternalChatFrameTest {
    private InternalChatFrame chatFrame;
    
    @Before
    public void setup() {
        chatFrame = new InternalChatFrame(new MainFrame());
    }
    
    @Test
    public void testAddText() {
        // Test case 1: Line does not contain ":"
        chatFrame.addText("Line without colon");
        assertEquals("Line without colon\n", chatFrame.getChatlog());
        
        // Test case 2: Line with special characters
        chatFrame.addText("Line with special characters: * + _");
        assertEquals("Line with special characters: * + _\n", chatFrame.getChatlog());
        
        // Test case 3: Line with "*" and "+_" characters
        chatFrame.addText("Line with * and +_ characters");
        assertEquals("Line with * and +_ characters\n", chatFrame.getChatlog());
    }
    
    @Test
    public void testAddUser() {
        // Test case 1: Add a new user
        chatFrame.addUser("User1");
        assertArrayEquals(new String[] {"User1"}, chatFrame.getUserlist());
        
        // Test case 2: Add an existing user
        chatFrame.addUser("User1");
        assertArrayEquals(new String[] {"User1"}, chatFrame.getUserlist());
    }
    
    @Test
    public void testDeleteUser() {
        // Test case 1: Delete an existing user
        chatFrame.addUser("User1");
        chatFrame.deleteUser("User1");
        assertArrayEquals(new String[] {}, chatFrame.getUserlist());
        
        // Test case 2: Delete a non-existing user
        chatFrame.deleteUser("User1");
        assertArrayEquals(new String[] {}, chatFrame.getUserlist());
    }
    
    @Test
    public void testGetUserlist() {
        // Test case 1: Empty user list
        assertArrayEquals(new String[] {}, chatFrame.getUserlist());
        
        // Test case 2: Non-empty user list
        chatFrame.addUser("User1");
        chatFrame.addUser("User2");
        assertArrayEquals(new String[] {"User2", "User1"}, chatFrame.getUserlist());
    }
    
    @Test
    public void testActionPerformed() {
        // Test case 1: "chooseColor" action command
        ActionEvent event1 = new ActionEvent(
            chatFrame, ActionEvent.ACTION_PERFORMED, "chooseColor");
        chatFrame.actionPerformed(event1);
        // Validate the result here
        
        // Test case 2: "bgColor" action command
        ActionEvent event2 = new ActionEvent(
            chatFrame, ActionEvent.ACTION_PERFORMED, "bgColor");
        chatFrame.actionPerformed(event2);
        // Validate the result here
        
        // Test case 3: "whisper" action command
        ActionEvent event3 = new ActionEvent(
            chatFrame, ActionEvent.ACTION_PERFORMED, "whisper");
        chatFrame.actionPerformed(event3);
        // Validate the result here
    }
    
    // Implement the remaining test cases for the other methods
    
}