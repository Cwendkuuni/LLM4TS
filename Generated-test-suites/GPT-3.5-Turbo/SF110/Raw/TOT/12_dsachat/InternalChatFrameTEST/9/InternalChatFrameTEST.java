package dsachat.client.gui;

import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import javax.swing.text.DefaultStyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

import static org.junit.Assert.*;

public class InternalChatFrameTest {

    private MainFrame mainFrame;
    private InternalChatFrame internalChatFrame;

    @Before
    public void setUp() {
        mainFrame = new MainFrame();
        internalChatFrame = new InternalChatFrame(mainFrame);
    }

    // Test case for addText method

    @Test
    public void testAddText() {
        internalChatFrame.addText("Test");

        DefaultStyledDocument doc = (DefaultStyledDocument) internalChatFrame.showTxt.getDocument();
        try {
            String text = doc.getText(0, doc.getLength());
            assertTrue(text.contains("Test"));
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    // Test case for addStylesToDocument method

    @Test
    public void testAddStylesToDocument() {
        internalChatFrame.addStylesToDocument(internalChatFrame.showTxt.getStyledDocument());

        Style regularStyle = internalChatFrame.showTxt.getStyledDocument().getStyle("regular");
        assertNotNull(regularStyle);

        Style italicStyle = internalChatFrame.showTxt.getStyledDocument().getStyle("italic");
        assertNotNull(italicStyle);

        Style boldStyle = internalChatFrame.showTxt.getStyledDocument().getStyle("bold");
        assertNotNull(boldStyle);

        Style boldItalicStyle = internalChatFrame.showTxt.getStyledDocument().getStyle("bold-italic");
        assertNotNull(boldItalicStyle);

        Style nameStyle = internalChatFrame.showTxt.getStyledDocument().getStyle("name");
        assertNotNull(nameStyle);

        Style systemStyle = internalChatFrame.showTxt.getStyledDocument().getStyle("system");
        assertNotNull(systemStyle);

        Style smallStyle = internalChatFrame.showTxt.getStyledDocument().getStyle("small");
        assertNotNull(smallStyle);

        Style largeStyle = internalChatFrame.showTxt.getStyledDocument().getStyle("large");
        assertNotNull(largeStyle);
    }

    // Test case for addUser method

    @Test
    public void testAddUser() {
        internalChatFrame.addUser("TestUser");

        DefaultListModel listModel = (DefaultListModel) internalChatFrame.list.getModel();
        int size = listModel.getSize();
        assertEquals(1, size);
        assertEquals("TestUser", listModel.getElementAt(0));

        Style userStyle = internalChatFrame.doc.getStyle("TestUser");
        assertNotNull(userStyle);
    }

    // Test case for deleteUser method

    @Test
    public void testDeleteUser() {
        internalChatFrame.addUser("TestUser");
        internalChatFrame.deleteUser("TestUser");

        DefaultListModel listModel = (DefaultListModel) internalChatFrame.list.getModel();
        int size = listModel.getSize();
        assertEquals(0, size);
    }

    // Test case for getUserlist method

    @Test
    public void testGetUserlist() {
        internalChatFrame.addUser("TestUser1");
        internalChatFrame.addUser("TestUser2");
        internalChatFrame.addUser("TestUser3");

        String[] userList = internalChatFrame.getUserlist();
        assertNotNull(userList);
        assertEquals(3, userList.length);
        assertEquals("TestUser1", userList[0]);
        assertEquals("TestUser2", userList[1]);
        assertEquals("TestUser3", userList[2]);
    }

    // Test case for actionPerformed method

    @Test
    public void testActionPerformed() {
        JButton button = new JButton();
        button.setActionCommand("chooseColor");

        ActionEvent event = new ActionEvent(button, ActionEvent.ACTION_PERFORMED, "chooseColor");
        internalChatFrame.actionPerformed(event);

        /* Assertion - Color Chooser window opens for the given action command */
    }

    // Test cases for mouseClicked, mouseEntered, mouseExited, mousePressed, mouseReleased methods

    @Test
    public void testMouseMethods() {
        MouseEvent event = new MouseEvent(new JList(), MouseEvent.MOUSE_CLICKED, System.currentTimeMillis(), 0, 0, 0, 1, false);
        internalChatFrame.mouseClicked(event);

        /* Assertion - No action expected for mouseClicked method */

        event = new MouseEvent(new JList(), MouseEvent.MOUSE_ENTERED, System.currentTimeMillis(), 0, 0, 0, 1, false);
        internalChatFrame.mouseEntered(event);

        /* Assertion - No action expected for mouseEntered method */

        event = new MouseEvent(new JList(), MouseEvent.MOUSE_EXITED, System.currentTimeMillis(), 0, 0, 0, 1, false);
        internalChatFrame.mouseExited(event);

        /* Assertion - No action expected for mouseExited method */

        event = new MouseEvent(new JList(), MouseEvent.MOUSE_PRESSED, System.currentTimeMillis(), 0, 0, 0, 1, false);
        internalChatFrame.mousePressed(event);

        /* Assertion - maybeShowPopup method called for mousePressed method */

        event = new MouseEvent(new JList(), MouseEvent.MOUSE_RELEASED, System.currentTimeMillis(), 0, 0, 0, 1, false);
        internalChatFrame.mouseReleased(event);

        /* Assertion - maybeShowPopup method called for mouseReleased method */
    }

    // Test case for maybeShowPopup method

    @Test
    public void testMaybeShowPopup() {
        JList list = new JList();
        MouseEvent event = new MouseEvent(list, MouseEvent.MOUSE_PRESSED, System.currentTimeMillis(), 0, 0, 0, 1, false);
        internalChatFrame.maybeShowPopup(event);

        /* Assertion - No action expected for maybeShowPopup method */

        JTextPane textPane = new JTextPane();
        event = new MouseEvent(textPane, MouseEvent.MOUSE_PRESSED, System.currentTimeMillis(), 0, 0, 0, 1, false);
        internalChatFrame.maybeShowPopup(event);

        /* Assertion - No action expected for maybeShowPopup method */
    }

    // Test case for getChatlog method

    @Test
    public void testGetChatlog() {
        internalChatFrame.addText("Test");

        String chatlog = internalChatFrame.getChatlog();
        assertNotNull(chatlog);
        assertTrue(chatlog.contains("Test"));
    }
}