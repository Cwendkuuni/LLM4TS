import org.junit.*;
import static org.junit.Assert.*;

public class InternalChatFrameTest {

    @Test
    public void testAddText() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        chatFrame.addText("This is a test");
        assertEquals("This is a test\n", chatFrame.getChatlog());
    }

    @Test
    public void testAddUser() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        chatFrame.addUser("User1");
        chatFrame.addUser("User2");
        assertEquals("User2\nUser1\n", chatFrame.getUserlist());
    }

    @Test
    public void testDeleteUser() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        chatFrame.addUser("User1");
        chatFrame.addUser("User2");
        chatFrame.deleteUser("User1");
        assertEquals("User2\n", chatFrame.getUserlist());
    }

    @Test
    public void testActionPerformed() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        chatFrame.addUser("User1");
        chatFrame.addUser("User2");
        chatFrame.actionPerformed(new ActionEvent(chatFrame, ActionEvent.ACTION_PERFORMED, "chooseColor"));
        //assert expected behavior
    }

    @Test
    public void testMouseClicked() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        chatFrame.mouseClicked(null);
        //assert expected behavior
    }

    @Test
    public void testMouseEntered() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        chatFrame.mouseEntered(null);
        //assert expected behavior
    }

    @Test
    public void testMouseExited() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        chatFrame.mouseExited(null);
        //assert expected behavior
    }

    @Test
    public void testMousePressed() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        chatFrame.mousePressed(null);
        //assert expected behavior
    }

    @Test
    public void testMouseReleased() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        chatFrame.mouseReleased(null);
        //assert expected behavior
    }

    @Test
    public void testGetChatlog() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        chatFrame.addText("This is a test");
        assertEquals("This is a test\n", chatFrame.getChatlog());
    }

}