
package dsachat.client.gui;

import org.junit.*;
import static org.junit.Assert.*;

public class InternalChatFrameTest {
    // Test addText method
    @Test
    public void testAddText() {
        InternalChatFrame chatFrame = new InternalChatFrame(null);
        chatFrame.addText("This is a test");
        assertEquals("This is a test\n", chatFrame.getChatlog());
    }
    
    // Test addUser method
    @Test
    public void testAddUser() {
        InternalChatFrame chatFrame = new InternalChatFrame(null);
        chatFrame.addUser("User1");
        chatFrame.addUser("User2");
        String[] userList = chatFrame.getUserlist();
        
        assertEquals(2, userList.length);
        assertEquals("User2", userList[0]);
        assertEquals("User1", userList[1]);
    }
    
    // Test deleteUser method
    @Test
    public void testDeleteUser() {
        InternalChatFrame chatFrame = new InternalChatFrame(null);
        chatFrame.addUser("User1");
        chatFrame.addUser("User2");
        chatFrame.deleteUser("User2");
        String[] userList = chatFrame.getUserlist();
        
        assertEquals(1, userList.length);
        assertEquals("User1", userList[0]);
    }
    
    // Test actionPerformed method
    @Test
    public void testActionPerformed() {
        InternalChatFrame chatFrame = new InternalChatFrame(null);
        chatFrame.addUser("User1");
        chatFrame.addUser("User2");
        
        ActionEvent chooseColorEvent = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "chooseColor");
        chatFrame.actionPerformed(chooseColorEvent);
        
        ActionEvent bgColorEvent = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "bgColor");
        chatFrame.actionPerformed(bgColorEvent);
        
        ActionEvent whisperEvent = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "whisper");
        chatFrame.actionPerformed(whisperEvent);
        
        // No assertion as these actions are for UI interactions
    }
    
    // Test mousePressed method
    @Test
    public void testMousePressed() {
        InternalChatFrame chatFrame = new InternalChatFrame(null);
        MouseEvent mouseEvent = new MouseEvent(chatFrame.getList(), MouseEvent.MOUSE_PRESSED, 0, 0, 0, 0, 1, false);
        chatFrame.mousePressed(mouseEvent);
        
        // No assertion as this action is for UI interactions
    }
    
    // Test mouseReleased method
    @Test
    public void testMouseReleased() {
        InternalChatFrame chatFrame = new InternalChatFrame(null);
        MouseEvent mouseEvent = new MouseEvent(chatFrame.getList(), MouseEvent.MOUSE_RELEASED, 0, 0, 0, 0, 1, false);
        chatFrame.mouseReleased(mouseEvent);
        
        // No assertion as this action is for UI interactions
    }
    
    // Test getChatlog method
    @Test
    public void testGetChatlog() {
        InternalChatFrame chatFrame = new InternalChatFrame(null);
        chatFrame.addText("This is a test");
        
        assertEquals("This is a test\n", chatFrame.getChatlog());
    }
}
