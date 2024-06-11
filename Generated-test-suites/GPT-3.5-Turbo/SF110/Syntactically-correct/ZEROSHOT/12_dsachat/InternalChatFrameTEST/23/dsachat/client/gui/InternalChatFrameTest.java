import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyledDocument;

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
        String line = "Test Line";
        chatFrame.addText(line);

        StyledDocument doc = chatFrame.showTxt.getStyledDocument();
        try {
            String text = doc.getText(0, doc.getLength());
            assertTrue(text.contains(line));
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testAddUser() {
        String user = "TestUser";
        chatFrame.addUser(user);

        DefaultListModel listModel = (DefaultListModel) chatFrame.list.getModel();
        assertEquals(1, listModel.size());
        assertEquals(user, listModel.get(0));

        StyledDocument doc = chatFrame.doc;
        assertNotNull(doc.getStyle(user));
    }

    @Test
    public void testDeleteUser() {
        String user = "TestUser";
        chatFrame.listModel.addElement(user);
        chatFrame.deleteUser(user);

        DefaultListModel listModel = (DefaultListModel) chatFrame.list.getModel();
        assertEquals(0, listModel.size());
    }

    @Test
    public void testGetUserlist() {
        String[] expectedList = {"User1", "User2", "User3"};
        for (String user : expectedList) {
            chatFrame.listModel.addElement(user);
        }

        String[] userList = chatFrame.getUserlist();
        assertArrayEquals(expectedList, userList);
    }

    @Test
    public void testActionPerformed_chooseColor() {
        String user = "TestUser";
        chatFrame.list.setSelectedValue(user, true);

        ActionListener actionListener = chatFrame;
        ActionEvent e = new ActionEvent(new JButton(), 0, "chooseColor");
        actionListener.actionPerformed(e);

        StyledDocument doc = chatFrame.doc;
        assertNotNull(doc.getStyle(user));
    }

    @Test
    public void testActionPerformed_bgColor() {
        ActionListener actionListener = chatFrame;
        ActionEvent e = new ActionEvent(new JButton(), 0, "bgColor");
        actionListener.actionPerformed(e);

        assertEquals(UIManager.getColor("Panel.background"), chatFrame.showTxt.getBackground());
    }

    @Test
    public void testActionPerformed_whisper() {
        String user = "TestUser";
        chatFrame.list.setSelectedValue(user, true);

        ActionListener actionListener = chatFrame;
        ActionEvent e = new ActionEvent(new JButton(), 0, "whisper");
        actionListener.actionPerformed(e);

        assertEquals("/w " + user + " ", chatFrame.txtInput.getText());
    }

    @Test
    public void testMousePressed_list() {
        MouseEvent e = new MouseEvent(chatFrame.list, 0, 0, 0, 0, 0, 1, false);
        chatFrame.mousePressed(e);

        JPopupMenu listPop = chatFrame.listPop;
        assertTrue(listPop.isVisible());
    }

    @Test
    public void testMousePressed_showTxt() {
        MouseEvent e = new MouseEvent(chatFrame.showTxt, 0, 0, 0, 0, 0, 1, false);
        chatFrame.mousePressed(e);

        JPopupMenu txtPop = chatFrame.txtPop;
        assertTrue(txtPop.isVisible());
    }

    @Test
    public void testMaybeShowPopup_list() {
        MouseEvent e = new MouseEvent(chatFrame.list, 0, 0, 0, 0, 0, 1, false);
        chatFrame.maybeShowPopup(e);

        JPopupMenu listPop = chatFrame.listPop;
        assertTrue(listPop.isVisible());
    }

    @Test
    public void testMaybeShowPopup_showTxt() {
        MouseEvent e = new MouseEvent(chatFrame.showTxt, 0, 0, 0, 0, 0, 1, false);
        chatFrame.maybeShowPopup(e);

        JPopupMenu txtPop = chatFrame.txtPop;
        assertTrue(txtPop.isVisible());
    }

    @Test
    public void testGetChatlog() {
        String expectedChatlog = "Test Chatlog";
        chatFrame.showTxt.setText(expectedChatlog);

        String chatlog = chatFrame.getChatlog();
        assertEquals(expectedChatlog, chatlog);
    }
}