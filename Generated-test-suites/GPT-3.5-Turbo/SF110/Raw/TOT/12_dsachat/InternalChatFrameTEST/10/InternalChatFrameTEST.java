import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.awt.Color;

public class InternalChatFrameTest {
    private InternalChatFrame chatFrame;

    @Before
    public void setup() {
        MainFrame parent = new MainFrame();
        chatFrame = new InternalChatFrame(parent);
    }

    @Test
    public void testAddText_regularMessage() {
        chatFrame.addText("This is a regular message");
        assertEquals("This is a regular message\n", chatFrame.getChatlog());
    }

    @Test
    public void testAddText_whisperMessage() {
        chatFrame.addText("User1 whispers: This is a private message");
        assertEquals("User1 whispers: This is a private message\n", chatFrame.getChatlog());
    }

    @Test
    public void testAddText_boldAndItalicMessage() {
        chatFrame.addText("***Bold and italic message***");
        assertEquals("***Bold and italic message***\n", chatFrame.getChatlog());
    }

    @Test
    public void testAddText_italicMessage() {
        chatFrame.addText("*Italic message*");
        assertEquals("*Italic message*\n", chatFrame.getChatlog());
    }

    @Test
    public void testAddText_boldMessage() {
        chatFrame.addText("+Bold message+");
        assertEquals("+Bold message+\n", chatFrame.getChatlog());
    }

    @Test
    public void testAddText_errorBadRequest() {
        chatFrame.addText("BadRequest: This is an invalid request");
        assertEquals("BadRequest: This is an invalid request\n", chatFrame.getChatlog());
    }

    @Test
    public void testAddText_errorUnauthorized() {
        chatFrame.addText("Unauthorized: Access denied");
        assertEquals("Unauthorized: Access denied\n", chatFrame.getChatlog());
    }

    @Test
    public void testAddText_errorServerUnavailable() {
        chatFrame.addText("ServerUnavailable: The server is currently not available");
        assertEquals("ServerUnavailable: The server is currently not available\n", chatFrame.getChatlog());
    }

    @Test
    public void testAddText_styleNameStartsWithYou() {
        chatFrame.getParent().getHero().setName("Player1");
        chatFrame.addText("You whisper: This is a whisper message");
        assertEquals("Player1 whisper: This is a whisper message\n", chatFrame.getChatlog());
    }

    @Test
    public void testAddText_styleNameStartsWithAsterisk() {
        chatFrame.addText("*User2: This is a styled message");
        assertEquals("User2: This is a styled message\n", chatFrame.getChatlog());
    }

    @Test
    public void testAddText_boldAndItalicStyledMessage() {
        chatFrame.addText("User3 *+_ Message");
        assertEquals("User3 *+_ Message\n", chatFrame.getChatlog());
    }

    @Test
    public void testAddText_italicStyledMessage() {
        chatFrame.addText("User4 * Message");
        assertEquals("User4 * Message\n", chatFrame.getChatlog());
    }

    @Test
    public void testAddText_boldStyledMessage() {
        chatFrame.addText("User5 + Message");
        assertEquals("User5 + Message\n", chatFrame.getChatlog());
    }

    @Test
    public void testCreateGUI_initialState() {
        assertNotNull(chatFrame.getList());
        assertNotNull(chatFrame.getListModel());
        assertNotNull(chatFrame.getShowTxt());
        assertNotNull(chatFrame.getTxtInput());
        assertNotNull(chatFrame.getParent());
        assertNotNull(chatFrame.getDoc());
        assertNotNull(chatFrame.getListPop());
        assertNotNull(chatFrame.getTxtPop());
    }

    @Test
    public void testAddUser_validUser() {
        chatFrame.addUser("User6");
        assertTrue(chatFrame.getListModel().contains("User6"));
        assertEquals("User6", chatFrame.getDoc().getStyle("User6").getName());
    }

    @Test
    public void testDeleteUser_existingUser() {
        chatFrame.getListModel().addElement("User7");
        chatFrame.deleteUser("User7");
        assertFalse(chatFrame.getListModel().contains("User7"));
    }

    @Test
    public void testDeleteUser_nonExistingUser() {
        chatFrame.getListModel().addElement("User8");
        chatFrame.deleteUser("User9");
        assertTrue(chatFrame.getListModel().contains("User8"));
    }

    @Test
    public void testGetUserlist_nonEmptyList() {
        chatFrame.getListModel().addElement("User10");
        chatFrame.getListModel().addElement("User11");
        String[] expected = {"User10", "User11"};
        assertArrayEquals(expected, chatFrame.getUserlist());
    }

    @Test
    public void testGetUserlist_emptyList() {
        String[] expected = {};
        assertArrayEquals(expected, chatFrame.getUserlist());
    }

    @Test
    public void testActionPerformed_chooseColorCommand_validUser() {
        chatFrame.getListModel().addElement("User12");
        chatFrame.getList().setSelectedValue("User12", true);
        chatFrame.actionPerformed(new ActionEvent(new Object(), ActionEvent.ACTION_PERFORMED, "chooseColor"));
        Color expectedColor = JColorChooser.showDialog(chatFrame, "Choose Color for user User12", Color.black);
        assertEquals(expectedColor, chatFrame.getDoc().getStyle("User12").getForegroundColor());
    }

    @Test
    public void testActionPerformed_chooseColorCommand_invalidUser() {
        chatFrame.getListModel().addElement("User13");
        chatFrame.getList().setSelectedValue("", true);
        chatFrame.actionPerformed(new ActionEvent(new Object(), ActionEvent.ACTION_PERFORMED, "chooseColor"));
        assertNull(chatFrame.getDoc().getStyle("").getForegroundColor());
    }

    @Test
    public void testActionPerformed_bgColorCommand_validUser() {
        chatFrame.getListModel().addElement("User14");
        chatFrame.getList().setSelectedValue("User14", true);
        chatFrame.actionPerformed(new ActionEvent(new Object(), ActionEvent.ACTION_PERFORMED, "bgColor"));
        Color expectedBackground = JColorChooser.showDialog(chatFrame, "Choose Color for user User14", Color.black);
        assertEquals(expectedBackground, chatFrame.getShowTxt().getBackground());
    }

    @Test
    public void testActionPerformed_bgColorCommand_invalidUser() {
        chatFrame.getListModel().addElement("User15");
        chatFrame.getList().setSelectedValue("", true);
        chatFrame.actionPerformed(new ActionEvent(new Object(), ActionEvent.ACTION_PERFORMED, "bgColor"));
        assertNull(chatFrame.getShowTxt().getBackground());
    }

    @Test
    public void testActionPerformed_whisperCommand() {
        chatFrame.getListModel().addElement("User16");
        chatFrame.getList().setSelectedValue("User16", true);
        chatFrame.actionPerformed(new ActionEvent(new Object(), ActionEvent.ACTION_PERFORMED, "whisper"));
        assertEquals("/w User16 ", chatFrame.getTxtInput().getText());
    }

    @Test
    public void testMouseClicked() {
        // No special behavior to test
    }

    @Test
    public void testMouseEntered() {
        // No special behavior to test
    }

    @Test
    public void testMouseExited() {
        // No special behavior to test
    }

    @Test
    public void testMousePressed() {
        // No special behavior to test
    }

    @Test
    public void testMouseReleased() {
        // No special behavior to test
    }
}