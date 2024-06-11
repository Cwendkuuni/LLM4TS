import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

import static org.junit.Assert.*;

public class InternalChatFrameTest {

    private InternalChatFrame internalChatFrame;

    @Before
    public void setUp() {
        internalChatFrame = new InternalChatFrame(new MainFrame());
    }

    @Test
    public void testAddText() {
        internalChatFrame.addText("Test Message");
        StyledDocument doc = internalChatFrame.getDoc();
        String text = doc.getText(0, doc.getLength());
        assertEquals("Test Message\n", text);
    }

    @Test
    public void testAddUser() {
        String user = "Test User";
        internalChatFrame.addUser(user);
        DefaultListModel listModel = internalChatFrame.getListModel();
        assertEquals(1, listModel.getSize());
        assertEquals(user, listModel.get(0));
    }

    @Test
    public void testDeleteUser() {
        String user = "Test User";
        internalChatFrame.addUser(user);
        internalChatFrame.deleteUser(user);
        DefaultListModel listModel = internalChatFrame.getListModel();
        assertEquals(0, listModel.getSize());
    }

    @Test
    public void testGetUserlist() {
        String user1 = "User 1";
        String user2 = "User 2";
        internalChatFrame.addUser(user1);
        internalChatFrame.addUser(user2);
        String[] userList = internalChatFrame.getUserlist();
        assertEquals(2, userList.length);
        assertEquals(user1, userList[0]);
        assertEquals(user2, userList[1]);
    }

    @Test
    public void testActionPerformed_chooseColor() {
        ActionEvent event = new ActionEvent(internalChatFrame, ActionEvent.ACTION_PERFORMED, "chooseColor");
        internalChatFrame.actionPerformed(event);
        // Verify that color chooser dialog is shown and style is changed accordingly
    }

    @Test
    public void testActionPerformed_bgColor() {
        ActionEvent event = new ActionEvent(internalChatFrame, ActionEvent.ACTION_PERFORMED, "bgColor");
        internalChatFrame.actionPerformed(event);
        // Verify that color chooser dialog is shown and background color is changed accordingly
    }

    @Test
    public void testActionPerformed_whisper() {
        internalChatFrame.addUser("Test User");
        ActionEvent event = new ActionEvent(internalChatFrame, ActionEvent.ACTION_PERFORMED, "whisper");
        internalChatFrame.actionPerformed(event);
        JTextField txtInput = internalChatFrame.getTxtInput();
        assertEquals("/w Test User ", txtInput.getText());
    }

    @Test
    public void testMousePressed_list() {
        MouseEvent event = new MouseEvent(internalChatFrame.getList(), MouseEvent.MOUSE_PRESSED,
                System.currentTimeMillis(), 0, 0, 0, 1, false);
        internalChatFrame.mousePressed(event);
        // Verify that list popup menu is shown
    }

    @Test
    public void testMousePressed_showTxt() {
        MouseEvent event = new MouseEvent(internalChatFrame.getShowTxt(), MouseEvent.MOUSE_PRESSED,
                System.currentTimeMillis(), 0, 0, 0, 1, false);
        internalChatFrame.mousePressed(event);
        // Verify that text popup menu is shown
    }

    @Test
    public void testMouseReleased_list() {
        MouseEvent event = new MouseEvent(internalChatFrame.getList(), MouseEvent.MOUSE_RELEASED,
                System.currentTimeMillis(), 0, 0, 0, 1, false);
        internalChatFrame.mouseReleased(event);
        // Verify that list popup menu is shown
    }

    @Test
    public void testMouseReleased_showTxt() {
        MouseEvent event = new MouseEvent(internalChatFrame.getShowTxt(), MouseEvent.MOUSE_RELEASED,
                System.currentTimeMillis(), 0, 0, 0, 1, false);
        internalChatFrame.mouseReleased(event);
        // Verify that text popup menu is shown
    }

    @Test
    public void testGetChatlog() {
        String chatlog = internalChatFrame.getChatlog();
        assertEquals("", chatlog);
    }

    // Additional tests can be added to test other methods

}