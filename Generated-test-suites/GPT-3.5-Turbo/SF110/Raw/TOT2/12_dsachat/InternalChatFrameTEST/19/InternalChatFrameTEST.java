import static org.junit.Assert.*;
import org.junit.Test;

public class InternalChatFrameTest {

    @Test
    public void testAddText() {
        // Test adding a regular message
        InternalChatFrame frame = new InternalChatFrame(null);
        frame.addText("Hello World");
        assertEquals("Hello World\n", frame.getChatlog());
        
        // Test adding a styled message
        frame.addText("*Bold Message*");
        assertEquals("Hello World\nBold Message\n", frame.getChatlog());
        
        // Test adding a whisper message
        frame.addText("You whisper: Secret Message");
        assertEquals("Hello World\nBold Message\nYou: Secret Message\n", frame.getChatlog());
    }
    
    @Test
    public void testAddUser() {
        InternalChatFrame frame = new InternalChatFrame(null);
        frame.addUser("User1");
        String[] userList = frame.getUserlist();
        assertTrue(userList.length == 1 && userList[0].equals("User1"));
    }
    
    @Test
    public void testDeleteUser() {
        InternalChatFrame frame = new InternalChatFrame(null);
        frame.addUser("User1");
        frame.deleteUser("User1");
        String[] userList = frame.getUserlist();
        assertTrue(userList.length == 0);
    }
    
    @Test
    public void testActionPerformed() {
        // Test choosing color for user
        InternalChatFrame frame = new InternalChatFrame(null);
        frame.addUser("User1");
        frame.actionPerformed(new ActionEvent(frame.getChatlog(), ActionEvent.ACTION_PERFORMED, "chooseColor"));
        // Implement actual assertion here
        
        // Test choosing background color
        frame.actionPerformed(new ActionEvent(frame.getChatlog(), ActionEvent.ACTION_PERFORMED, "bgColor"));
        // Implement actual assertion here
        
        // Test whisper action
        frame.actionPerformed(new ActionEvent(frame.getChatlog(), ActionEvent.ACTION_PERFORMED, "whisper"));
        // Implement actual assertion here
    }
    
    @Test
    public void testMaybeShowPopup() {
        // Implement test for maybeShowPopup method
    }
}