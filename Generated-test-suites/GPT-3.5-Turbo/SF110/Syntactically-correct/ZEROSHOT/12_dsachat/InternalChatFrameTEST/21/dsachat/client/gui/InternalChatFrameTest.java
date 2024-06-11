import org.junit.*;
import static org.junit.Assert.*;

public class InternalChatFrameTest {

    @Test
    public void testAddText() {
        InternalChatFrame chatFrame = new InternalChatFrame(null);
        chatFrame.addText("Test message");
        assertEquals("Test message\n", chatFrame.getChatlog());
    }

    @Test
    public void testAddUser() {
        InternalChatFrame chatFrame = new InternalChatFrame(null);
        chatFrame.addUser("User1");
        chatFrame.addUser("User2");
        String[] expected = { "User2", "User1" };
        assertArrayEquals(expected, chatFrame.getUserlist());
    }

    @Test
    public void testDeleteUser() {
        InternalChatFrame chatFrame = new InternalChatFrame(null);
        chatFrame.addUser("User1");
        chatFrame.addUser("User2");
        chatFrame.deleteUser("User1");
        String[] expected = { "User2" };
        assertArrayEquals(expected, chatFrame.getUserlist());
    }

    @Test
    public void testActionPerformedChooseColor() {
        InternalChatFrame chatFrame = new InternalChatFrame(null);
        chatFrame.addUser("User1");
        chatFrame.list.setSelectedValue("User1", true);
        chatFrame.actionPerformed(new java.awt.event.ActionEvent(chatFrame, 0, "chooseColor"));
        assertEquals("SansSerif", chatFrame.doc.getStyle("User1").getAttribute(StyleConstants.FontFamily));
        assertEquals(false, chatFrame.doc.getStyle("User1").getAttribute(StyleConstants.Bold));
        assertEquals(true, chatFrame.doc.getStyle("User1").getAttribute(StyleConstants.Italic));
    }

    @Test
    public void testActionPerformedBgColor() {
        InternalChatFrame chatFrame = new InternalChatFrame(null);
        chatFrame.actionPerformed(new java.awt.event.ActionEvent(chatFrame, 0, "bgColor"));
        assertEquals(Color.BLACK, chatFrame.showTxt.getBackground());
    }

    @Test
    public void testActionPerformedWhisper() {
        InternalChatFrame chatFrame = new InternalChatFrame(null);
        chatFrame.addUser("User1");
        chatFrame.list.setSelectedValue("User1", true);
        chatFrame.actionPerformed(new java.awt.event.ActionEvent(chatFrame, 0, "whisper"));
        assertEquals("/w User1 ", chatFrame.txtInput.getText());
    }

    @Test
    public void testMouseClicked() {
        InternalChatFrame chatFrame = new InternalChatFrame(null);
        chatFrame.mouseClicked(null);
        // No assertion since it is an empty method
    }

    @Test
    public void testMouseEntered() {
        InternalChatFrame chatFrame = new InternalChatFrame(null);
        chatFrame.mouseEntered(null);
        // No assertion since it is an empty method
    }

    @Test
    public void testMouseExited() {
        InternalChatFrame chatFrame = new InternalChatFrame(null);
        chatFrame.mouseExited(null);
        // No assertion since it is an empty method
    }

    @Test
    public void testMousePressed() {
        InternalChatFrame chatFrame = new InternalChatFrame(null);
        chatFrame.mouseReleased(null);
        // No assertion since it is an empty method
    }

    @Test
    public void testMouseReleased() {
        InternalChatFrame chatFrame = new InternalChatFrame(null);
        chatFrame.mouseReleased(null);
        // No assertion since it is an empty method
    }

    @Test
    public void testGetChatlog() {
        InternalChatFrame chatFrame = new InternalChatFrame(null);
        chatFrame.addText("Test message 1");
        chatFrame.addText("Test message 2");
        assertEquals("Test message 1\nTest message 2\n", chatFrame.getChatlog());
    }
}