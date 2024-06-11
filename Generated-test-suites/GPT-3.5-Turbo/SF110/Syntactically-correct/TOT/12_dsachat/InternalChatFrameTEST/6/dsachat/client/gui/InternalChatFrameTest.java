import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

import static org.junit.Assert.*;

public class InternalChatFrameTest {

    private InternalChatFrame internalChatFrame;
    private MainFrame parent;

    @Before
    public void setUp() {
        parent = new MainFrame();
        internalChatFrame = new InternalChatFrame(parent);
    }

    @After
    public void tearDown() {
        parent = null;
        internalChatFrame = null;
    }

    @Test
    public void testInternalChatFrameCreation() {
        assertNotNull(internalChatFrame);
        assertTrue(internalChatFrame.isVisible());

        assertEquals("Chat", internalChatFrame.getTitle());
        assertTrue(internalChatFrame.isClosable());
        assertFalse(internalChatFrame.isMaximizable());
        assertTrue(internalChatFrame.isIconifiable());
        assertTrue(internalChatFrame.isResizable());

        assertSame(parent, internalChatFrame.getParent());
        assertNotNull(internalChatFrame.getListModel());
        assertNotNull(internalChatFrame.getShowText());
        assertNotNull(internalChatFrame.getTxtInput());

        assertNotNull(internalChatFrame.getDoc());
        assertEquals(0, internalChatFrame.getListModel().getSize());
    }

    @Test
    public void testCreateGUI() {
        internalChatFrame.createGUI();

        JSplitPane split = (JSplitPane) internalChatFrame.getContentPane().getComponent(0);
        JList list = (JList) split.getLeftComponent();
        JScrollPane scrList = (JScrollPane) list.getParent();
        JViewport viewPort = (JViewport) scrList.getComponent(0);

        assertNotNull(split);
        assertNotNull(list);
        assertNotNull(scrList);
        assertNotNull(viewPort);

        assertSame(internalChatFrame.getListModel(), list.getModel());
        assertEquals(0, list.getSelectionMode());

        assertTrue(list.getListSelectionListeners().length > 0);
        assertTrue(list.getMouseListeners().length > 0);

        assertNotNull(internalChatFrame.getTxtPop());
        assertNotNull(internalChatFrame.getListPop());
    }

    @Test
    public void testAddUser() {
        assertEquals(0, internalChatFrame.getListModel().getSize());

        internalChatFrame.addUser("User1");

        assertEquals(1, internalChatFrame.getListModel().getSize());
        assertEquals("User1", internalChatFrame.getListModel().get(0));
    }

    @Test
    public void testDeleteUser() {
        internalChatFrame.addUser("User1");
        internalChatFrame.addUser("User2");

        assertEquals(2, internalChatFrame.getListModel().getSize());

        internalChatFrame.deleteUser("User1");

        assertEquals(1, internalChatFrame.getListModel().getSize());
        assertEquals("User2", internalChatFrame.getListModel().get(0));
    }

    @Test
    public void testGetUserlist() {
        internalChatFrame.addUser("User1");
        internalChatFrame.addUser("User2");

        String[] userList = internalChatFrame.getUserlist();

        assertEquals(2, userList.length);
        assertEquals("User1", userList[1]);
        assertEquals("User2", userList[0]);
    }

    @Test
    public void testActionPerformed() {
        ActionEvent actionEvent = new ActionEvent(internalChatFrame, ActionEvent.ACTION_PERFORMED, "chooseColor");

        internalChatFrame.actionPerformed(actionEvent);

        assertEquals("Choose Color for user null", JColorChooser.getFrameForComponent(internalChatFrame).getTitle());

        actionEvent = new ActionEvent(internalChatFrame, ActionEvent.ACTION_PERFORMED, "bgColor");

        internalChatFrame.actionPerformed(actionEvent);

        assertEquals("Choose Color for user null", JColorChooser.getFrameForComponent(internalChatFrame).getTitle());
    }

    @Test
    public void testMouseClicked() {
        MouseEvent mouseEvent = new MouseEvent(internalChatFrame, MouseEvent.MOUSE_CLICKED, 0, MouseEvent.BUTTON1, 0, 0, 1, false);

        internalChatFrame.mouseClicked(mouseEvent);

        // No assertions to make as this is an empty method
    }

    @Test
    public void testMouseEntered() {
        MouseEvent mouseEvent = new MouseEvent(internalChatFrame, MouseEvent.MOUSE_ENTERED, 0, 0, 0, 0, 1, false);

        internalChatFrame.mouseEntered(mouseEvent);

        // No assertions to make as this is an empty method
    }

    @Test
    public void testMouseExited() {
        MouseEvent mouseEvent = new MouseEvent(internalChatFrame, MouseEvent.MOUSE_EXITED, 0, 0, 0, 0, 1, false);

        internalChatFrame.mouseExited(mouseEvent);

        // No assertions to make as this is an empty method
    }

    @Test
    public void testMousePressed() {
        MouseEvent mouseEvent = new MouseEvent(internalChatFrame, MouseEvent.MOUSE_PRESSED, 0, MouseEvent.BUTTON1, 0, 0, 1, false);

        internalChatFrame.mousePressed(mouseEvent);

        // No assertions to make as this is an empty method
    }

    @Test
    public void testMouseReleased() {
        MouseEvent mouseEvent = new MouseEvent(internalChatFrame, MouseEvent.MOUSE_RELEASED, 0, MouseEvent.BUTTON1, 0, 0, 1, false);

        internalChatFrame.mouseReleased(mouseEvent);

        // No assertions to make as this is an empty method
    }

    @Test
    public void testGetChatlog() {
        JTextPane showText = internalChatFrame.getShowText();
        DefaultStyledDocument doc = new DefaultStyledDocument();
        showText.setDocument(doc);

        StyleContext styleContext = StyleContext.getDefaultStyleContext();
        Style regular = styleContext.getStyle("regular");
        doc.addStyle("name", regular);
        doc.addStyle("system", regular);
        doc.addStyle("small", regular);
        doc.addStyle("large", regular);

        assertEquals("", internalChatFrame.getChatlog());

        try {
            doc.insertString(0, "Line1\n", regular);
            doc.insertString(doc.getLength(), "Line2\n", regular);
            doc.insertString(doc.getLength(), "Line3\n", regular);
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }

        assertEquals("Line1\nLine2\nLine3\n", internalChatFrame.getChatlog());
    }

    @Test
    public void testAddText() {
        JTextPane showText = internalChatFrame.getShowText();
        DefaultStyledDocument doc = new DefaultStyledDocument();
        showText.setDocument(doc);

        assertEquals(0, doc.getLength());

        internalChatFrame.addText("This is a test line");

        assertEquals(1, doc.getLength());
    }

    @Test
    public void testAddTextWithDifferentStyles() {
        JTextPane showText = internalChatFrame.getShowText();
        DefaultStyledDocument doc = new DefaultStyledDocument();
        showText.setDocument(doc);

        assertEquals(0, doc.getLength());

        internalChatFrame.addText("This is a test line");

        assertEquals(1, doc.getLength());

        try {
            doc.setLogicalStyle(0, null);
            internalChatFrame.addText("*This* is a test line");
            internalChatFrame.addText("+This+ is a test line");
            internalChatFrame.addText("*This+ is a test line");
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }

        assertEquals(5, doc.getLength());
    }

}