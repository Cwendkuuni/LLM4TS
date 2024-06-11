import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class InternalChatFrameTest {

    private InternalChatFrame chatFrame;
    private MainFrame parent;

    @Before
    public void setUp() {
        parent = Mockito.mock(MainFrame.class);
        chatFrame = new InternalChatFrame(parent);
    }

    @Test
    public void testAddText() {
        String line = "This is a test line";
        chatFrame.addText(line);
        String chatlog = chatFrame.getChatlog();
        assertTrue(chatlog.contains(line));
    }

    @Test
    public void testAddUser() {
        String user = "User1";
        chatFrame.addUser(user);
        assertEquals(1, chatFrame.getUserlist().length);
        assertEquals(user, chatFrame.getUserlist()[0]);
    }

    @Test
    public void testDeleteUser() {
        String user = "User1";
        chatFrame.addUser(user);
        chatFrame.deleteUser(user);
        assertEquals(0, chatFrame.getUserlist().length);
    }

    @Test
    public void testActionPerformed_ChooseColor() {
        String user = "User1";
        chatFrame.addUser(user);
        chatFrame.list.setSelectedValue(user, true);
        
        // Simulate choosing a color
        chatFrame.actionPerformed(new ActionEvent(this, 0, "chooseColor"));

        // Verify that the style for the selected user has changed
        assertTrue(chatFrame.getChatlog().contains("style='" + user + "'"));
    }

    @Test
    public void testActionPerformed_BgColor() {
        // Simulate choosing a background color
        chatFrame.actionPerformed(new ActionEvent(this, 0, "bgColor"));

        // TODO: Verify that the background color has changed
    }

    @Test
    public void testActionPerformed_Whisper() {
        String user = "User1";
        chatFrame.addUser(user);
        chatFrame.list.setSelectedValue(user, true);

        // Simulate clicking on "Whisper" menu item
        chatFrame.actionPerformed(new ActionEvent(this, 0, "whisper"));

        // Verify that the text input field has been updated with the whisper command
        assertEquals("/w User1 ", chatFrame.txtInput.getText());
    }
}