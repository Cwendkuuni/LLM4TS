
package dsachat.client.gui;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class InternalChatFrameTest {
    private InternalChatFrame internalChatFrame;

    @Before
    public void setUp() {
        MainFrame parent = new MainFrame();
        internalChatFrame = new InternalChatFrame(parent);
    }

    @Test
    public void testAddText() {
        internalChatFrame.addText("Regular message");
        internalChatFrame.addText("*Italic message*");
        internalChatFrame.addText("+Bold message+");
        internalChatFrame.addText("*Italic message with +bold+ text*");
        internalChatFrame.addText("*Italic message with [normal] text*");
        internalChatFrame.addText("Regular message with __underscored__ text");
        internalChatFrame.addText("Regular message with _underscored_ text");
        internalChatFrame.addText("__Bold message__ with regular text");
        internalChatFrame.addText("_Bold message_ with regular text");
        internalChatFrame.addText("Regular message with *italic* text");
        internalChatFrame.addText("Regular message with +bold+ text");
        internalChatFrame.addText("*Italic message* with regular text");
        internalChatFrame.addText("+Bold message+ with regular text");
    }

    @Test
    public void testAddUser() {
        internalChatFrame.addUser("user1");
        internalChatFrame.addUser("user2");
        internalChatFrame.addUser("user3");
    }

    @Test
    public void testDeleteUser() {
        internalChatFrame.addUser("user1");
        internalChatFrame.addUser("user2");
        internalChatFrame.addUser("user3");

        internalChatFrame.deleteUser("user1");
        internalChatFrame.deleteUser("user2");
        internalChatFrame.deleteUser("user3");
    }

    @Test
    public void testGetUserlist() {
        internalChatFrame.addUser("user1");
        internalChatFrame.addUser("user2");
        internalChatFrame.addUser("user3");

        String[] userlist = internalChatFrame.getUserlist();
        assertEquals(3, userlist.length);
        assertEquals("user1", userlist[0]);
        assertEquals("user2", userlist[1]);
        assertEquals("user3", userlist[2]);
    }

    @Test
    public void testActionPerformed() {
        // Test chooseColor
        internalChatFrame.addUser("user1");
        internalChatFrame.list.setSelectedValue("user1", true);
        internalChatFrame.actionPerformed(new ActionEvent(internalChatFrame, 0, "chooseColor"));

        // Test bgColor
        internalChatFrame.actionPerformed(new ActionEvent(internalChatFrame, 0, "bgColor"));

        // Test whisper
        internalChatFrame.addUser("user2");
        internalChatFrame.list.setSelectedValue("user2", true);
        internalChatFrame.actionPerformed(new ActionEvent(internalChatFrame, 0, "whisper"));
    }

    @Test
    public void testMouseClicked() {
        // Test mouseClicked
        internalChatFrame.mouseClicked(null);

        // Test mouseEntered
        internalChatFrame.mouseEntered(null);

        // Test mouseExited
        internalChatFrame.mouseExited(null);

    }

    @Test
    public void testMousePressed() {
        // Test mousePressed
        internalChatFrame.mousePressed(null);

        // Test mouseReleased
        internalChatFrame.mouseReleased(null);

    }

    @Test
    public void testGetChatlog() {
        internalChatFrame.addText("Message 1");
        internalChatFrame.addText("Message 2");
        internalChatFrame.addText("Message 3");

        assertEquals("Message 1\nMessage 2\nMessage 3\n", internalChatFrame.getChatlog());
    }
}
