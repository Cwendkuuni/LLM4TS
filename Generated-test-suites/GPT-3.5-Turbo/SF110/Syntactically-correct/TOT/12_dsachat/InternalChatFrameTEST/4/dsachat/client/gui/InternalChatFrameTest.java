import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class InternalChatFrameTest {

    private InternalChatFrame internalChatFrame;

    @Before
    public void setUp() {
        MainFrame parent = new MainFrame();
        internalChatFrame = new InternalChatFrame(parent);
    }

    // Test for addText method
    @Test
    public void testAddText() {
        internalChatFrame.addText("This is a test message");
        
        String expected = "This is a test message\n";
        String actual = internalChatFrame.getChatlog();
        
        assertEquals(expected, actual);
    }

    // Test for addStylesToDocument method
    @Test
    public void testAddStylesToDocument() {
        internalChatFrame.addStylesToDocument();

        // Check if style "regular" is added
        Style regularStyle = internalChatFrame.doc.getStyle("regular");
        assertNotNull(regularStyle);

        // Check if style "italic" is added
        Style italicStyle = internalChatFrame.doc.getStyle("italic");
        assertNotNull(italicStyle);

        // Check if style "bold" is added
        Style boldStyle = internalChatFrame.doc.getStyle("bold");
        assertNotNull(boldStyle);

        // Check if style "bold-italic" is added
        Style boldItalicStyle = internalChatFrame.doc.getStyle("bold-italic");
        assertNotNull(boldItalicStyle);

        // Check if style "name" is added
        Style nameStyle = internalChatFrame.doc.getStyle("name");
        assertNotNull(nameStyle);

        // Check if style "system" is added
        Style systemStyle = internalChatFrame.doc.getStyle("system");
        assertNotNull(systemStyle);

        // Check if style "small" is added
        Style smallStyle = internalChatFrame.doc.getStyle("small");
        assertNotNull(smallStyle);

        // Check if style "large" is added
        Style largeStyle = internalChatFrame.doc.getStyle("large");
        assertNotNull(largeStyle);
    }

    // Test for addUser method
    @Test
    public void testAddUser() {
        internalChatFrame.addUser("User1");

        String[] expected = {"User1"};
        String[] actual = internalChatFrame.getUserlist();

        assertArrayEquals(expected, actual);
    }

    // Test for deleteUser method
    @Test
    public void testDeleteUser() {
        internalChatFrame.addUser("User1");

        internalChatFrame.deleteUser("User1");

        String[] expected = {};
        String[] actual = internalChatFrame.getUserlist();

        assertArrayEquals(expected, actual);
    }

    // Test for actionPerformed method - chooseColor
    @Test
    public void testActionPerformed_ChooseColor() {
        internalChatFrame.addUser("User1");

        internalChatFrame.list.setSelectedIndex(0);

        ActionEvent e = new ActionEvent(internalChatFrame, ActionEvent.ACTION_PERFORMED, "chooseColor");
        internalChatFrame.actionPerformed(e);

        // Check if the color is changed for the selected user
        // No direct way to test, as it depends on the JColorChooser dialog
        // Can be manually verified by checking the changed color of the user

    }

    // Test for actionPerformed method - bgColor
    @Test
    public void testActionPerformed_BgColor() {
        ActionEvent e = new ActionEvent(internalChatFrame, ActionEvent.ACTION_PERFORMED, "bgColor");
        internalChatFrame.actionPerformed(e);

        // Check if the background color is changed
        // No direct way to test, as it depends on the JColorChooser dialog
        // Can be manually verified by checking the changed background color of the chat frame

    }

    // Test for actionPerformed method - whisper
    @Test
    public void testActionPerformed_Whisper() {
        internalChatFrame.addUser("User1");

        internalChatFrame.list.setSelectedIndex(0);

        ActionEvent e = new ActionEvent(internalChatFrame, ActionEvent.ACTION_PERFORMED, "whisper");
        internalChatFrame.actionPerformed(e);

        String expected = "/w User1 ";
        String actual = internalChatFrame.txtInput.getText();

        assertEquals(expected, actual);
    }

    // Test for mouseClicked method
    @Test
    public void testMouseClicked() {
        // Cannot be tested as it is an empty method
    }

    // Test for mouseEntered method
    @Test
    public void testMouseEntered() {
        // Cannot be tested as it is an empty method
    }

    // Test for mouseExited method
    @Test
    public void testMouseExited() {
        // Cannot be tested as it is an empty method
    }

    // Test for mousePressed method
    @Test
    public void testMousePressed() {
        // Cannot be directly tested as it depends on the maybeShowPopup method
        // Can be indirectly tested by testing the maybeShowPopup method
    }

    // Test for mouseReleased method
    @Test
    public void testMouseReleased() {
        // Cannot be directly tested as it depends on the maybeShowPopup method
        // Can be indirectly tested by testing the maybeShowPopup method
    }

    // Test for maybeShowPopup method
    @Test
    public void testMaybeShowPopup_List() {
        MouseEvent e = new MouseEvent(internalChatFrame.list, MouseEvent.MOUSE_RELEASED, System.currentTimeMillis(),
                0, 100, 100, 1, false);
        internalChatFrame.maybeShowPopup(e);

        // Check if the list popup is shown
        // No direct way to test, as it depends on the JPopupMenu and MouseEvent

    }

    // Test for maybeShowPopup method
    @Test
    public void testMaybeShowPopup_ShowTxt() {
        MouseEvent e = new MouseEvent(internalChatFrame.showTxt, MouseEvent.MOUSE_RELEASED, System.currentTimeMillis(),
                0, 100, 100, 1, false);
        internalChatFrame.maybeShowPopup(e);

        // Check if the text popup is shown
        // No direct way to test, as it depends on the JPopupMenu and MouseEvent

    }

    // Test for getChatlog method
    @Test
    public void testGetChatlog() {
        internalChatFrame.addText("This is a test message");

        String expected = "This is a test message\n";
        String actual = internalChatFrame.getChatlog();

        assertEquals(expected, actual);
    }
}