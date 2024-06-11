
import org.junit.*;
import static org.junit.Assert.*;

public class InternalChatFrameTest {

    @Test
    public void testAddText() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        chatFrame.addText("This is a test message");
        assertEquals("This is a test message\n", chatFrame.getChatlog());
    }

    @Test
    public void testAddUser() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        chatFrame.addUser("User1");
        String[] userList = chatFrame.getUserlist();
        assertEquals("User1", userList[0]);
    }

    @Test
    public void testDeleteUser() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        chatFrame.addUser("User1");
        chatFrame.deleteUser("User1");
        String[] userList = chatFrame.getUserlist();
        assertEquals(0, userList.length);
    }

    @Test
    public void testActionPerformed_ChooseColor() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        chatFrame.addUser("User1");
        chatFrame.list.setSelectedValue("User1", true);
        chatFrame.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "chooseColor"));

        // Assertion to check if style changed
        // assertEquals(expectedStyle, chatFrame.getStyle("User1"));
    }

    @Test
    public void testActionPerformed_BGColor() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        chatFrame.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "bgColor"));

        // Assertion to check if background color changed
        // assertEquals(expectedColor, chatFrame.getBackground());
    }

    @Test
    public void testActionPerformed_Whisper() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        chatFrame.addUser("User1");
        chatFrame.list.setSelectedValue("User1", true);
        chatFrame.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "whisper"));
        assertEquals("/w User1 ", chatFrame.txtInput.getText());
    }

    @Test
    public void testMouseClicked() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        // TODO: Implement test for mouseClicked method
    }

    @Test
    public void testMouseEntered() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        // TODO: Implement test for mouseEntered method
    }

    @Test
    public void testMouseExited() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        // TODO: Implement test for mouseExited method
    }

    @Test
    public void testMousePressed() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        // TODO: Implement test for mousePressed method
    }

    @Test
    public void testMouseReleased() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        // TODO: Implement test for mouseReleased method
    }

    // TODO: Add more test cases for other methods in InternalChatFrame class

}

