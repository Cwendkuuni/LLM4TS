import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class InternalChatFrameTest {

    private MainFrame parent;
    private InternalChatFrame internalChatFrame;

    @Before
    public void setUp() {
        parent = new MainFrame();
        internalChatFrame = new InternalChatFrame(parent);
    }

    @Test
    public void testAddText() {
        // Test adding text to the chat frame
        internalChatFrame.addText("Test Message");
        assertEquals("Test Message\n", internalChatFrame.getChatlog());
    }

    @Test
    public void testAddUser() {
        // Test adding a user to the user list
        internalChatFrame.addUser("Test User");
        String[] userList = internalChatFrame.getUserlist();
        assertEquals("Test User", userList[0]);
    }

    @Test
    public void testDeleteUser() {
        // Test deleting a user from the user list
        internalChatFrame.addUser("Test User");
        internalChatFrame.deleteUser("Test User");
        String[] userList = internalChatFrame.getUserlist();
        assertEquals(0, userList.length);
    }

    @Test
    public void testActionPerformed_ChooseColor() {
        // Test choosing a color for a user
        internalChatFrame.addUser("Test User");
        ActionEvent e = new ActionEvent(internalChatFrame, ActionEvent.ACTION_PERFORMED, "chooseColor");
        internalChatFrame.actionPerformed(e);

        // Check if the color was changed
        // Not implemented as it's UI related
    }

    @Test
    public void testActionPerformed_BgColor() {
        // Test choosing a background color for the chat
        ActionEvent e = new ActionEvent(internalChatFrame, ActionEvent.ACTION_PERFORMED, "bgColor");
        internalChatFrame.actionPerformed(e);

        // Check if the background color was changed
        // Not implemented as it's UI related
    }

    @Test
    public void testActionPerformed_Whisper() {
        // Test setting input text for whispering to a user
        internalChatFrame.addUser("Test User");
        ActionEvent e = new ActionEvent(internalChatFrame, ActionEvent.ACTION_PERFORMED, "whisper");
        internalChatFrame.actionPerformed(e);

        assertEquals("/w Test User ", internalChatFrame.txtInput.getText());
    }

}