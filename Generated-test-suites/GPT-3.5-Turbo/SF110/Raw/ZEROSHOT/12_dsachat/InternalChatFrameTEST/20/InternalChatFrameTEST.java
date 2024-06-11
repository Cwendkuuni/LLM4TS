import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

import static org.junit.Assert.*;

public class InternalChatFrameTest {

    private InternalChatFrame chatFrame;

    @Before
    public void setUp() {
        MainFrame parent = new MainFrame();
        chatFrame = new InternalChatFrame(parent);
    }

    @Test
    public void testAddText() {
        String line = "Test line";
        chatFrame.addText(line);
        JTextPane showTxt = chatFrame.getShowTxt();
        StyledDocument doc = showTxt.getStyledDocument();
        try {
            String text = doc.getText(0, doc.getLength());
            assertTrue(text.contains(line));
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAddUser() {
        String user = "Test User";
        chatFrame.addUser(user);
        JList list = chatFrame.getList();
        DefaultListModel listModel = (DefaultListModel) list.getModel();
        assertEquals(user, listModel.getElementAt(0));
    }

    @Test
    public void testDeleteUser() {
        String user = "Test User";
        chatFrame.addUser(user);
        chatFrame.deleteUser(user);
        JList list = chatFrame.getList();
        DefaultListModel listModel = (DefaultListModel) list.getModel();
        assertEquals(0, listModel.size());
    }

    @Test
    public void testGetUserlist() {
        chatFrame.addUser("User 1");
        chatFrame.addUser("User 2");
        chatFrame.addUser("User 3");
        String[] userlist = chatFrame.getUserlist();
        assertEquals(3, userlist.length);
        assertEquals("User 1", userlist[0]);
        assertEquals("User 2", userlist[1]);
        assertEquals("User 3", userlist[2]);
    }

    @Test
    public void testActionPerformed_ChooseColor() {
        ActionEvent e = new ActionEvent(chatFrame, ActionEvent.ACTION_PERFORMED, "chooseColor");
        chatFrame.actionPerformed(e);
        JColorChooser colorChooser = JColorChooser.createDialog(null, "Choose Color for user", true, null, null, null);
        assertTrue(colorChooser.isVisible());
    }

    @Test
    public void testActionPerformed_BgColor() {
        ActionEvent e = new ActionEvent(chatFrame, ActionEvent.ACTION_PERFORMED, "bgColor");
        chatFrame.actionPerformed(e);
        JColorChooser colorChooser = JColorChooser.createDialog(null, "Choose Color for user", true, null, null, null);
        assertTrue(colorChooser.isVisible());
    }

    @Test
    public void testActionPerformed_Whisper() {
        JList list = chatFrame.getList();
        DefaultListModel listModel = (DefaultListModel) list.getModel();
        listModel.addElement("Test User");
        list.setSelectedIndex(0);

        ActionEvent e = new ActionEvent(chatFrame, ActionEvent.ACTION_PERFORMED, "whisper");
        chatFrame.actionPerformed(e);

        JTextField txtInput = chatFrame.getTxtInput();
        assertEquals("/w Test User ", txtInput.getText());
    }

    @Test
    public void testMouseClicked() {
        MouseEvent e = new MouseEvent(chatFrame, MouseEvent.MOUSE_CLICKED, System.currentTimeMillis(),
                0, 0, 0, 1, false);
        chatFrame.mouseClicked(e);
        // No assertion. Just test if the method runs without errors.
    }

    @Test
    public void testMouseEntered() {
        MouseEvent e = new MouseEvent(chatFrame, MouseEvent.MOUSE_ENTERED, System.currentTimeMillis(),
                0, 0, 0, 1, false);
        chatFrame.mouseEntered(e);
        // No assertion. Just test if the method runs without errors.
    }

    @Test
    public void testMouseExited() {
        MouseEvent e = new MouseEvent(chatFrame, MouseEvent.MOUSE_EXITED, System.currentTimeMillis(),
                0, 0, 0, 1, false);
        chatFrame.mouseExited(e);
        // No assertion. Just test if the method runs without errors.
    }

    @Test
    public void testMousePressed() {
        MouseEvent e = new MouseEvent(chatFrame, MouseEvent.MOUSE_PRESSED, System.currentTimeMillis(),
                0, 0, 0, 1, false);
        chatFrame.mousePressed(e);
        // No assertion. Just test if the method runs without errors.
    }

    @Test
    public void testMouseReleased() {
        MouseEvent e = new MouseEvent(chatFrame, MouseEvent.MOUSE_RELEASED, System.currentTimeMillis(),
                0, 0, 0, 1, false);
        chatFrame.mouseReleased(e);
        // No assertion. Just test if the method runs without errors.
    }

    @Test
    public void testGetChatlog() {
        JTextPane showTxt = chatFrame.getShowTxt();
        showTxt.setText("Test chat log");
        String chatlog = chatFrame.getChatlog();
        assertEquals("Test chat log", chatlog);
    }
}