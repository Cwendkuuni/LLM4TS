import org.junit.*;
import static org.junit.Assert.*;

public class InternalChatFrameTest {

    // Test constructor
    @Test
    public void testInternalChatFrameConstructor() {
        MainFrame parent = new MainFrame();
        InternalChatFrame internalChatFrame = new InternalChatFrame(parent);
        
        // Check if the instance variables are initialized correctly
        assertNotNull(internalChatFrame.list);
        assertNotNull(internalChatFrame.listModel);
        assertNotNull(internalChatFrame.showTxt);
        assertNotNull(internalChatFrame.txtInput);
        assertNotNull(internalChatFrame.parent);
        assertNotNull(internalChatFrame.doc);
        assertNotNull(internalChatFrame.listPop);
        assertNotNull(internalChatFrame.txtPop);
        
        // Check if the title and properties are set correctly
        assertEquals("Chat", internalChatFrame.getTitle());
        assertTrue(internalChatFrame.isClosable());
        assertFalse(internalChatFrame.isMaximizable());
        assertTrue(internalChatFrame.isIconifiable());
        assertTrue(internalChatFrame.isResizable());
    }

    // Test addText() method
    @Test
    public void testAddText() {
        MainFrame parent = new MainFrame();
        InternalChatFrame internalChatFrame = new InternalChatFrame(parent);
        
        // Add a text to the chat frame
        internalChatFrame.addText("Test message");
        
        // Check if the text is added correctly
        String chatlog = internalChatFrame.getChatlog();
        assertTrue(chatlog.contains("Test message"));
    }

    // Test addUser() method
    @Test
    public void testAddUser() {
        MainFrame parent = new MainFrame();
        InternalChatFrame internalChatFrame = new InternalChatFrame(parent);
        
        // Add a user to the user list
        internalChatFrame.addUser("User1");
        
        // Check if the user is added correctly
        String[] userlist = internalChatFrame.getUserlist();
        assertEquals(1, userlist.length);
        assertEquals("User1", userlist[0]);
    }

    // Test deleteUser() method
    @Test
    public void testDeleteUser() {
        MainFrame parent = new MainFrame();
        InternalChatFrame internalChatFrame = new InternalChatFrame(parent);
        
        // Add a user to the user list
        internalChatFrame.addUser("User1");
        internalChatFrame.addUser("User2");
        
        // Delete a user from the user list
        internalChatFrame.deleteUser("User1");
        
        // Check if the user is deleted correctly
        String[] userlist = internalChatFrame.getUserlist();
        assertEquals(1, userlist.length);
        assertEquals("User2", userlist[0]);
    }

    // Test getUserlist() method
    @Test
    public void testGetUserlist() {
        MainFrame parent = new MainFrame();
        InternalChatFrame internalChatFrame = new InternalChatFrame(parent);
        
        // Add users to the user list
        internalChatFrame.addUser("User1");
        internalChatFrame.addUser("User2");
        
        // Get the user list
        String[] userlist = internalChatFrame.getUserlist();
        
        // Check if the user list is returned correctly
        assertEquals(2, userlist.length);
        assertEquals("User1", userlist[0]);
        assertEquals("User2", userlist[1]);
    }

    // Test actionPerformed() method
    // Write test cases for different action commands (chooseColor, bgColor, whisper)

    // Test mouseClicked() method
    // Write test cases to cover different scenarios

    // Test mouseEntered() method
    // Write test cases to cover different scenarios

    // Test mouseExited() method
    // Write test cases to cover different scenarios

    // Test mousePressed() method
    // Write test cases to cover different scenarios

    // Test mouseReleased() method
    // Write test cases to cover different scenarios

    // Test maybeShowPopup() method
    // Write test cases to cover different scenarios

    // Test getChatlog() method
    @Test
    public void testGetChatlog() {
        MainFrame parent = new MainFrame();
        InternalChatFrame internalChatFrame = new InternalChatFrame(parent);
        
        // Add a text to the chat frame
        internalChatFrame.addText("Test message");
        
        // Get the chat log
        String chatlog = internalChatFrame.getChatlog();
        
        // Check if the chat log is returned correctly
        assertTrue(chatlog.contains("Test message"));
    }
}