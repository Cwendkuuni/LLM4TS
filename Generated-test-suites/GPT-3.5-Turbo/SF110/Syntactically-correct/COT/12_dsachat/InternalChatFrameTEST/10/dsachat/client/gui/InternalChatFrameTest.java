
package dsachat.client.gui;

import org.junit.Before;
import org.junit.Test;

public class InternalChatFrameTest {

    private InternalChatFrame chatFrame;

    @Before
    public void setUp() {
        chatFrame = new InternalChatFrame(new MainFrame());
    }

    @Test
    public void testAddText() {
        chatFrame.addText("Test message");
        // Assert that the message is displayed correctly in the showTxt JTextPane
    }

    @Test
    public void testAddUser() {
        chatFrame.addUser("Test User");
        // Assert that the user is added to the JList and the StyledDocument
    }

    @Test
    public void testDeleteUser() {
        chatFrame.addUser("Test User");
        chatFrame.deleteUser("Test User");
        // Assert that the user is removed from the JList and the StyledDocument
    }

    @Test
    public void testGetUserlist() {
        chatFrame.addUser("Test User");
        String[] userList = chatFrame.getUserlist();
        // Assert that the returned user list contains "Test User"
    }

    @Test
    public void testActionPerformedForChooseColor() {
      // Simulate action event for "chooseColor"
      // Assert that the user's color style is changed in the StyledDocument
    }

    @Test
    public void testActionPerformedForBgColor() {
      // Simulate action event for "bgColor"
      // Assert that the background color of the showTxt JTextPane is changed
    }

    @Test
    public void testActionPerformedForWhisper() {
      // Simulate action event for "whisper"
      // Assert that the text input field contains "/w <user>"
    }

    @Test
    public void testMousePressed() {
      // Simulate mouse pressed event for the JList
      // Assert that the listPop JPopupMenu is shown
    }

    @Test
    public void testMouseReleased() {
      // Simulate mouse released event for the showTxt JTextPane
      // Assert that the txtPop JPopupMenu is shown
    }

    @Test
    public void testGetChatlog() {
      // Set some text in the showTxt JTextPane
      String chatlog = chatFrame.getChatlog();
      // Assert that the returned chatlog is correct
    }
}

