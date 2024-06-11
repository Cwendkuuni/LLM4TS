
import org.junit.Test;
import static org.junit.Assert.*;

public class InternalChatFrameTest {

    @Test
    public void testAddText() {
        InternalChatFrame chatFrame = new InternalChatFrame(null);
        chatFrame.addText("This is a test");
        assertEquals("This is a test\n", chatFrame.getChatlog());
    }

    @Test
    public void testAddUser() {
        InternalChatFrame chatFrame = new InternalChatFrame(null);
        chatFrame.addUser("User1");
        chatFrame.addUser("User2");
        chatFrame.addUser("User3");
        String[] userList = chatFrame.getUserlist();
        assertEquals(3, userList.length);
        assertEquals("User3", userList[0]);
        assertEquals("User2", userList[1]);
        assertEquals("User1", userList[2]);
    }

    @Test
    public void testDeleteUser() {
        InternalChatFrame chatFrame = new InternalChatFrame(null);
        chatFrame.addUser("User1");
        chatFrame.addUser("User2");
        chatFrame.addUser("User3");
        chatFrame.deleteUser("User2");
        String[] userList = chatFrame.getUserlist();
        assertEquals(2, userList.length);
        assertEquals("User3", userList[0]);
        assertEquals("User1", userList[1]);
    }

    @Test
    public void testActionPerformed_ChooseColor() {
        InternalChatFrame chatFrame = new InternalChatFrame(null);
        chatFrame.addUser("User1");
        
        ActionEvent event = new ActionEvent(chatFrame, ActionEvent.ACTION_PERFORMED, "chooseColor");
        chatFrame.actionPerformed(event);
        // Check if the color is changed for the user "User1"
        // Assertion code goes here
    }

    @Test
    public void testActionPerformed_BgColor() {
        InternalChatFrame chatFrame = new InternalChatFrame(null);
        
        ActionEvent event = new ActionEvent(chatFrame, ActionEvent.ACTION_PERFORMED, "bgColor");
        chatFrame.actionPerformed(event);
        // Check if the background color is changed
        // Assertion code goes here
    }

    @Test
    public void testActionPerformed_Whisper() {
        InternalChatFrame chatFrame = new InternalChatFrame(null);
        chatFrame.addUser("User1");
        
        chatFrame.list.setSelectedIndex(0);
        ActionEvent event = new ActionEvent(chatFrame, ActionEvent.ACTION_PERFORMED, "whisper");
        chatFrame.actionPerformed(event);
        assertEquals("/w User1 ", chatFrame.txtInput.getText());
    }
}
