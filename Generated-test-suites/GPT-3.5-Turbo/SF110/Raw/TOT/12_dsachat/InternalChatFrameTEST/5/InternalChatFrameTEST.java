import org.junit.Test;
import static org.junit.Assert.*;

public class InternalChatFrameTest {

    // Test case for the constructor
    @Test
    public void testConstructor() {
        // Create a MainFrame object for testing
        MainFrame parent = new MainFrame();
        
        // Create an InternalChatFrame object
        InternalChatFrame chatFrame = new InternalChatFrame(parent);
        
        // Check if the parent field is set correctly
        assertEquals(parent, chatFrame.getParent());
    }
    
    // Test case for the addText() method
    @Test
    public void testAddText() {
        // Create a MainFrame object for testing
        MainFrame parent = new MainFrame();
        
        // Create an InternalChatFrame object
        InternalChatFrame chatFrame = new InternalChatFrame(parent);
        
        // Add a text message to the chat frame
        String line = "User1: Hello World!";
        chatFrame.addText(line);
        
        // Check if the text message is added to the showTxt JTextPane
        assertEquals("User1: Hello World!\n", chatFrame.getChatlog());
    }
    
    // Test case for the addUser() method
    @Test
    public void testAddUser() {
        // Create a MainFrame object for testing
        MainFrame parent = new MainFrame();
        
        // Create an InternalChatFrame object
        InternalChatFrame chatFrame = new InternalChatFrame(parent);
        
        // Add a user to the chat frame
        String user = "User1";
        chatFrame.addUser(user);
        
        // Check if the user is added to the listModel
        String[] userList = chatFrame.getUserlist();
        assertEquals(user, userList[0]);
    }
    
    // Test case for the deleteUser() method
    @Test
    public void testDeleteUser() {
        // Create a MainFrame object for testing
        MainFrame parent = new MainFrame();
        
        // Create an InternalChatFrame object
        InternalChatFrame chatFrame = new InternalChatFrame(parent);
        
        // Add a user to the chat frame
        String user = "User1";
        chatFrame.addUser(user);
        
        // Delete the user from the chat frame
        chatFrame.deleteUser(user);
        
        // Check if the user is deleted from the listModel
        String[] userList = chatFrame.getUserlist();
        assertEquals(0, userList.length);
    }
    
    // Test case for the actionPerformed() method
    @Test
    public void testActionPerformed() {
        // Create a MainFrame object for testing
        MainFrame parent = new MainFrame();
        
        // Create an InternalChatFrame object
        InternalChatFrame chatFrame = new InternalChatFrame(parent);
        
        // Add a user to the chat frame
        String user = "User1";
        chatFrame.addUser(user);
        
        // Simulate choosing a color for a user
        chatFrame.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "chooseColor"));
        
        // Simulate choosing a background color for a user
        chatFrame.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "bgColor"));
        
        // Simulate whispering to a user
        chatFrame.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "whisper"));
        
        // Check if the actionPerformed methods are executed without errors
        // No specific assertions can be made here as the methods do not have any return values
    }
    
    // Test case for the mousePressed() and mouseReleased() methods
    @Test
    public void testMouseEvents() {
        // Create a MainFrame object for testing
        MainFrame parent = new MainFrame();
        
        // Create an InternalChatFrame object
        InternalChatFrame chatFrame = new InternalChatFrame(parent);
        
        // Simulate a mouse pressed event on the list
        chatFrame.mousePressed(new MouseEvent(this, MouseEvent.MOUSE_PRESSED, 0, 0, 0, 0, 1, false));
        
        // Simulate a mouse released event on the list
        chatFrame.mouseReleased(new MouseEvent(this, MouseEvent.MOUSE_RELEASED, 0, 0, 0, 0, 1, false));
        
        // Check if the mouse events are handled correctly
        // No specific assertions can be made here as the methods do not have any return values
    }
}