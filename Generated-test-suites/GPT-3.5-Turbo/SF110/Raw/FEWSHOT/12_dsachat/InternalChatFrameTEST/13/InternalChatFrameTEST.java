package dsachat.client.gui;

import org.junit.Before;
import org.junit.Test;

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
        chatFrame.addText("Test");
        assertNotNull(chatFrame.getChatlog());
        assertTrue(chatFrame.getChatlog().contains("Test"));
    }

    @Test
    public void testAddUser() {
        String user = "testUser";
        chatFrame.addUser(user);
        String[] userList = chatFrame.getUserlist();
        assertNotNull(userList);
        assertEquals(1, userList.length);
        assertEquals(user, userList[0]);
    }

    @Test
    public void testDeleteUser() {
        String user = "testUser";
        chatFrame.addUser(user);
        chatFrame.deleteUser(user);
        String[] userList = chatFrame.getUserlist();
        assertNotNull(userList);
        assertEquals(0, userList.length);
    }

    @Test
    public void testActionPerformed_ChooseColor() {
        String user = "testUser";
        chatFrame.addUser(user);
        ActionEvent e = new ActionEvent(chatFrame, ActionEvent.ACTION_PERFORMED, "chooseColor");
        chatFrame.actionPerformed(e);
        // Verify color change logic
    }

    @Test
    public void testActionPerformed_BgColor() {
        String user = "testUser";
        chatFrame.addUser(user);
        ActionEvent e = new ActionEvent(chatFrame, ActionEvent.ACTION_PERFORMED, "bgColor");
        chatFrame.actionPerformed(e);
        // Verify background color change logic
    }

    @Test
    public void testActionPerformed_Whisper() {
        String user = "testUser";
        chatFrame.addUser(user);
        ActionEvent e = new ActionEvent(chatFrame, ActionEvent.ACTION_PERFORMED, "whisper");
        chatFrame.actionPerformed(e);
        // Verify whisper logic
    }

    // Additional test cases for other methods in the class
}