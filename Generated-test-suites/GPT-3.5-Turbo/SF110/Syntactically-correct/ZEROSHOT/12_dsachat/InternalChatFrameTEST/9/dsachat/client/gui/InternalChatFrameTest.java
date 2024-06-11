package dsachat.client.gui;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class InternalChatFrameTest {

    private MainFrame parent;
    private InternalChatFrame chatFrame;

    @Before
    public void setUp() {
        parent = new MainFrame();
        chatFrame = new InternalChatFrame(parent);
    }

    @Test
    public void testAddText() {
        chatFrame.addText("This is a test");
        assertEquals("This is a test\n", chatFrame.getChatlog());
    }

    @Test
    public void testAddUser() {
        chatFrame.addUser("testUser");
        String[] userList = chatFrame.getUserlist();
        assertEquals("testUser", userList[0]);
    }

    @Test
    public void testDeleteUser() {
        chatFrame.addUser("testUser");
        chatFrame.deleteUser("testUser");
        String[] userList = chatFrame.getUserlist();
        assertEquals(0, userList.length);
    }

    @Test
    public void testActionPerformed() {
        ActionEvent actionEvent = new ActionEvent("chooseColor", 1, "chooseColor");
        chatFrame.actionPerformed(actionEvent);
        // Add assertions
    }

    @Test
    public void testMouseClicked() {
        MouseEvent mouseEvent = new MouseEvent(chatFrame, MouseEvent.MOUSE_CLICKED, 1, MouseEvent.BUTTON1, 10, 10, 1, false);
        chatFrame.mouseClicked(mouseEvent);
        // Add assertions
    }

    @Test
    public void testMouseEntered() {
        MouseEvent mouseEvent = new MouseEvent(chatFrame, MouseEvent.MOUSE_ENTERED, 1, MouseEvent.BUTTON1, 10, 10, 1, false);
        chatFrame.mouseEntered(mouseEvent);
        // Add assertions
    }

    @Test
    public void testMouseExited() {
        MouseEvent mouseEvent = new MouseEvent(chatFrame, MouseEvent.MOUSE_EXITED, 1, MouseEvent.BUTTON1, 10, 10, 1, false);
        chatFrame.mouseExited(mouseEvent);
        // Add assertions
    }

    @Test
    public void testMousePressed() {
        MouseEvent mouseEvent = new MouseEvent(chatFrame, MouseEvent.MOUSE_PRESSED, 1, MouseEvent.BUTTON1, 10, 10, 1, false);
        chatFrame.mousePressed(mouseEvent);
        // Add assertions
    }

    @Test
    public void testMouseReleased() {
        MouseEvent mouseEvent = new MouseEvent(chatFrame, MouseEvent.MOUSE_RELEASED, 1, MouseEvent.BUTTON1, 10, 10, 1, false);
        chatFrame.mouseReleased(mouseEvent);
        // Add assertions
    }

    @Test
    public void testGetChatlog() {
        assertEquals("", chatFrame.getChatlog());
    }
}