import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class InternalChatFrameTest {

    private InternalChatFrame internalChatFrame;

    @Before
    public void setup() {
        internalChatFrame = new InternalChatFrame(new MainFrame());
    }

    @Test
    public void testAddText() {
        internalChatFrame.addText("Test Message");
        assertEquals("Test Message\n", internalChatFrame.getChatlog());
    }

    @Test
    public void testAddUser() {
        internalChatFrame.addUser("Test User");
        String[] userList = internalChatFrame.getUserlist();
        assertEquals(1, userList.length);
        assertEquals("Test User", userList[0]);
    }

    @Test
    public void testDeleteUser() {
        internalChatFrame.addUser("Test User");
        internalChatFrame.deleteUser("Test User");
        String[] userList = internalChatFrame.getUserlist();
        assertEquals(0, userList.length);
    }

    @Test
    public void testActionPerformed() {
        internalChatFrame.addUser("Test User");
        internalChatFrame.actionPerformed(new ActionEvent(internalChatFrame, 0, "chooseColor"));
        internalChatFrame.actionPerformed(new ActionEvent(internalChatFrame, 0, "bgColor"));
        internalChatFrame.actionPerformed(new ActionEvent(internalChatFrame, 0, "whisper"));
        assertEquals("/w Test User ", internalChatFrame.txtInput.getText());
    }

    @Test
    public void testMousePressed() {
        internalChatFrame.mousePressed(null);
    }

    @Test
    public void testMouseReleased() {
        internalChatFrame.mouseReleased(null);
    }
}