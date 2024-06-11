import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

public class InternalChatFrameTest {

    private InternalChatFrame chatFrame;

    @Before
    public void setUp() {
        MainFrame mainFrame = new MainFrame();
        chatFrame = new InternalChatFrame(mainFrame);
    }

    @Test
    public void testAddText() {
        chatFrame.addText("Test message: This is a test");
        assertEquals("Test message: This is a test\n", chatFrame.getChatlog());
    }

    @Test
    public void testAddUser() {
        chatFrame.addUser("User1");
        assertTrue(chatFrame.getUserlist().length == 1);
        assertEquals("User1", chatFrame.getUserlist()[0]);
    }

    @Test
    public void testDeleteUser() {
        chatFrame.addUser("User1");
        chatFrame.deleteUser("User1");
        assertTrue(chatFrame.getUserlist().length == 0);
    }

    @Test
    public void testActionPerformed() {
        // Test action performed when chooseColor on a user
        chatFrame.addUser("User1");
        chatFrame.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "chooseColor"));
        // Test action performed when bgColor
        chatFrame.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "bgColor"));
        // Test action performed when whisper
        chatFrame.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "whisper"));
    }

}