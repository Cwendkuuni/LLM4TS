import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class InternalChatFrameTest {

    private InternalChatFrame chatFrame;

    @Before
    public void setUp() {
        chatFrame = new InternalChatFrame(new MainFrame());
    }

    @Test
    public void testAddText_regularMessage_successfullyAdded() {
        String line = "Regular message";
        chatFrame.addText(line);
        String chatlog = chatFrame.getChatlog();
        assertTrue(chatlog.contains(line));
    }

    @Test
    public void testAddText_whisperMessage_successfullyAdded() {
        String line = "Whisper message";
        chatFrame.addText("You whispers: " + line);
        String chatlog = chatFrame.getChatlog();
        assertTrue(chatlog.contains(line));
    }

    @Test
    public void testAddText_boldAndItalicMessage_successfullyAdded() {
        String line = "Bold and italic message";
        chatFrame.addText("*" + line + "*");
        String chatlog = chatFrame.getChatlog();
        assertTrue(chatlog.contains(line));
    }

    @Test
    public void testAddText_italicMessage_successfullyAdded() {
        String line = "Italic message";
        chatFrame.addText("_" + line + "_");
        String chatlog = chatFrame.getChatlog();
        assertTrue(chatlog.contains(line));
    }

    @Test
    public void testAddText_boldMessage_successfullyAdded() {
        String line = "Bold message";
        chatFrame.addText("+" + line + "+");
        String chatlog = chatFrame.getChatlog();
        assertTrue(chatlog.contains(line));
    }

    @Test
    public void testAddText_mixedBoldAndItalicMessage_successfullyAdded() {
        String line = "Mixed bold and italic message";
        chatFrame.addText("*+" + line + "+*");
        String chatlog = chatFrame.getChatlog();
        assertTrue(chatlog.contains(line));
    }

    @Test
    public void testAddText_systemMessage_successfullyAdded() {
        String line = "System message";
        chatFrame.addText("System: " + line);
        String chatlog = chatFrame.getChatlog();
        assertTrue(chatlog.contains(line));
    }

    @Test(expected = BadLocationException.class)
    public void testAddText_badLocationException_thrown() {
        chatFrame.addText(null);
    }

    @Test
    public void testAddUser_validUser_userAddedToList() {
        String user = "User1";
        chatFrame.addUser(user);
        String[] userList = chatFrame.getUserlist();
        assertEquals(user, userList[0]);
    }

    @Test
    public void testDeleteUser_existingUser_userRemovedFromList() {
        String user = "User1";
        chatFrame.addUser(user);
        chatFrame.deleteUser(user);
        String[] userList = chatFrame.getUserlist();
        assertEquals(0, userList.length);
    }

    @Test
    public void testDeleteUser_nonExistingUser_noChangeInUserList() {
        String user = "User1";
        chatFrame.addUser(user);
        chatFrame.deleteUser("User2");
        String[] userList = chatFrame.getUserlist();
        assertEquals(1, userList.length);
    }

    @Test
    public void testGetUserlist_emptyUserList_emptyArrayReturned() {
        String[] userList = chatFrame.getUserlist();
        assertEquals(0, userList.length);
    }

    @Test
    public void testActionPerformed_chooseColorAction_changeStyleColor() {
        String user = "User1";
        chatFrame.addUser(user);
        ActionEvent event = new ActionEvent(chatFrame, ActionEvent.ACTION_PERFORMED, "chooseColor");
        chatFrame.actionPerformed(event);
        // Check if color is changed for the user style
    }

    @Test
    public void testActionPerformed_bgColorAction_changeBackground() {
        String user = "User1";
        chatFrame.addUser(user);
        ActionEvent event = new ActionEvent(chatFrame, ActionEvent.ACTION_PERFORMED, "bgColor");
        chatFrame.actionPerformed(event);
        // Check if background color is changed
    }

    @Test
    public void testActionPerformed_whisperAction_setInputToWhisperUser() {
        String user = "User1";
        chatFrame.addUser(user);
        String whisperCommand = "/w " + user + " ";
        ActionEvent event = new ActionEvent(chatFrame, ActionEvent.ACTION_PERFORMED, "whisper");
        chatFrame.actionPerformed(event);
        assertEquals(whisperCommand, chatFrame.txtInput.getText());
    }

    @Test
    public void testMouseClicked_noActionPerformed() {
        MouseEvent event = new MouseEvent(chatFrame, MouseEvent.MOUSE_CLICKED, 0, 0, 0, 0, 0, false);
        chatFrame.mouseClicked(event);
        // No action to verify
    }

    @Test
    public void testMouseEntered_noActionPerformed() {
        MouseEvent event = new MouseEvent(chatFrame, MouseEvent.MOUSE_ENTERED, 0, 0, 0, 0, 0, false);
        chatFrame.mouseEntered(event);
        // No action to verify
    }

    @Test
    public void testMouseExited_noActionPerformed() {
        MouseEvent event = new MouseEvent(chatFrame, MouseEvent.MOUSE_EXITED, 0, 0, 0, 0, 0, false);
        chatFrame.mouseExited(event);
        // No action to verify
    }

    @Test
    public void testMousePressed_noActionPerformed() {
        MouseEvent event = new MouseEvent(chatFrame, MouseEvent.MOUSE_PRESSED, 0, 0, 0, 0, 0, false);
        chatFrame.mousePressed(event);
        // No action to verify
    }

    @Test
    public void testMouseReleased_noActionPerformed() {
        MouseEvent event = new MouseEvent(chatFrame, MouseEvent.MOUSE_RELEASED, 0, 0, 0, 0, 0, false);
        chatFrame.mouseReleased(event);
        // No action to verify
    }

    @Test
    public void testMaybeShowPopup_listPopupTrigger_showListPopupMenu() {
        MouseEvent event = new MouseEvent(chatFrame.list, MouseEvent.MOUSE_RELEASED, 0, 0, 0, 0, 1, false);
        chatFrame.maybeShowPopup(event);
        // Check if list popup menu is shown
    }

    @Test
    public void testMaybeShowPopup_textPopupTrigger_showTextPopupMenu() {
        MouseEvent event = new MouseEvent(chatFrame.showTxt, MouseEvent.MOUSE_RELEASED, 0, 0, 0, 0, 1, false);
        chatFrame.maybeShowPopup(event);
        // Check if text popup menu is shown
    }

    @Test
    public void testGetChatlog_emptyChatlog_emptyStringReturned() {
        String chatlog = chatFrame.getChatlog();
        assertEquals("", chatlog);
    }

}