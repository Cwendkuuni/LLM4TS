import static org.junit.Assert.*;
import org.junit.Test;

public class InternalChatFrameTest {
    @Test
    public void testAddText() {
        // Test adding a normal message
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        chatFrame.addText("Hello, world!");
        assertEquals("Hello, world!\n", chatFrame.getChatlog());
        
        // Test adding a message with special styling
        chatFrame.addText("*This is bold* _This is italic_");
        assertEquals("Hello, world!\n*This is bold* _This is italic_\n", chatFrame.getChatlog());
        
        // Test adding a message with a flagged username
        chatFrame.addText("*Username: message*");
        assertEquals("Hello, world!\n*This is bold* _This is italic_\n*Username: message*\n", chatFrame.getChatlog());
    }

    @Test
    public void testAddUserAndDeleteUser() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        chatFrame.addUser("User1");
        chatFrame.addUser("User2");
        assertEquals(2, chatFrame.getUserlist().length);
        
        chatFrame.deleteUser("User1");
        assertEquals(1, chatFrame.getUserlist().length);
    }

    @Test
    public void testActionPerformed() {
        // Test changing color for a user
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        chatFrame.addUser("User1");
        chatFrame.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "chooseColor"));
        // Assert color change
        
        // Test changing background color
        chatFrame.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "bgColor"));
        // Assert background color change
        
        // Test initiating a whisper
        chatFrame.addUser("User2");
        chatFrame.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "whisper"));
        assertEquals("/w User2 ", chatFrame.txtInput.getText());
    }
}