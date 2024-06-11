import org.junit.Before;
import org.junit.Test;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

import static org.junit.Assert.*;

public class InternalChatFrameTest {
    private InternalChatFrame chatFrame;

    @Before
    public void setUp() {
        chatFrame = new InternalChatFrame(new MainFrame());
    }

    @Test
    public void testAddText() {
        chatFrame.addText("Hello, World!");
        String expectedText = "Hello, World!\n";
        assertEquals(expectedText, chatFrame.getChatlog());
    }

    @Test
    public void testAddUser() {
        chatFrame.addUser("User1");
        chatFrame.addUser("User2");
        String[] expectedUsers = {"User2", "User1"};
        assertArrayEquals(expectedUsers, chatFrame.getUserlist());
    }

    @Test
    public void testDeleteUser() {
        chatFrame.addUser("User1");
        chatFrame.addUser("User2");
        chatFrame.deleteUser("User1");
        String[] expectedUsers = {"User2"};
        assertArrayEquals(expectedUsers, chatFrame.getUserlist());
    }

    @Test
    public void testActionPerformed_chooseColor() {
        ActionEvent e = new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, "chooseColor");
        chatFrame.addUser("User1");
        chatFrame.setSelectedValue("User1");
        chatFrame.actionPerformed(e);
        // Verify that a color chooser dialog was shown and style was changed accordingly
    }

    @Test
    public void testActionPerformed_bgColor() {
        ActionEvent e = new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, "bgColor");
        chatFrame.addUser("User1");
        chatFrame.setSelectedValue("User1");
        chatFrame.actionPerformed(e);
        // Verify that a color chooser dialog was shown and text pane's background color was changed accordingly
    }

    @Test
    public void testActionPerformed_whisper() {
        ActionEvent e = new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, "whisper");
        chatFrame.addUser("User1");
        chatFrame.setSelectedValue("User1");
        chatFrame.actionPerformed(e);
        assertEquals("/w User1 ", chatFrame.getInputText());
    }

    @Test
    public void testMouseClicked() {
        MouseEvent e = new MouseEvent(chatFrame.getList(), MouseEvent.MOUSE_CLICKED, System.currentTimeMillis(), 0, 10, 10, 1, false);
        chatFrame.mouseClicked(e);
        // Verify that nothing happens when the mouse is clicked
    }

    @Test
    public void testMouseEntered() {
        MouseEvent e = new MouseEvent(chatFrame.getShowTxt(), MouseEvent.MOUSE_ENTERED, System.currentTimeMillis(), 0, 10, 10, 1, false);
        chatFrame.mouseEntered(e);
        // Verify that nothing happens when the mouse enters the text pane
    }

    @Test
    public void testMouseExited() {
        MouseEvent e = new MouseEvent(chatFrame.getShowTxt(), MouseEvent.MOUSE_EXITED, System.currentTimeMillis(), 0, 10, 10, 1, false);
        chatFrame.mouseExited(e);
        // Verify that nothing happens when the mouse exits the text pane
    }

    @Test
    public void testMousePressed() {
        MouseEvent e = new MouseEvent(chatFrame.getList(), MouseEvent.MOUSE_PRESSED, System.currentTimeMillis(), 0, 10, 10, 1, false);
        chatFrame.mousePressed(e);
        // Verify that the popup menu is shown when the mouse is pressed on the list
    }

    @Test
    public void testMouseReleased() {
        MouseEvent e = new MouseEvent(chatFrame.getList(), MouseEvent.MOUSE_RELEASED, System.currentTimeMillis(), 0, 10, 10, 1, false);
        chatFrame.mouseReleased(e);
        // Verify that the popup menu is shown when the mouse is released on the list
    }

    @Test
    public void testGetChatlog() {
        chatFrame.addText("Hello, World!");
        String expectedText = "Hello, World!\n";
        assertEquals(expectedText, chatFrame.getChatlog());
    }
}