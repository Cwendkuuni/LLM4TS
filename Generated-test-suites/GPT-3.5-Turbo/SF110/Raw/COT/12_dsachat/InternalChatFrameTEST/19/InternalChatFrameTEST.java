import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import dsachat.client.gui.InternalChatFrame;
import dsachat.client.gui.MainFrame;

public class InternalChatFrameTest {
    private InternalChatFrame internalChatFrame;
    private MainFrame parentMock;

    @Before
    public void setUp() {
        parentMock = Mockito.mock(MainFrame.class);
        internalChatFrame = new InternalChatFrame(parentMock);
    }

    @Test
    public void testAddText() {
        internalChatFrame.addText("Test Message");
        String chatlog = internalChatFrame.getChatlog();
        assertTrue(chatlog.contains("Test Message"));
    }

    @Test
    public void testAddUser() {
        internalChatFrame.addUser("User1");
        internalChatFrame.addUser("User2");
        String[] userList = internalChatFrame.getUserlist();
        assertEquals(2, userList.length);
        assertTrue(userList[0].equals("User2"));
        assertTrue(userList[1].equals("User1"));
        assertTrue(internalChatFrame.getChatlog().contains("User1"));
    }

    @Test
    public void testDeleteUser() {
        internalChatFrame.addUser("User1");
        internalChatFrame.addUser("User2");
        internalChatFrame.deleteUser("User1");
        String[] userList = internalChatFrame.getUserlist();
        assertEquals(1, userList.length);
        assertTrue(userList[0].equals("User2"));
        assertFalse(internalChatFrame.getChatlog().contains("User1"));
    }

    @Test
    public void testActionPerformed_ChooseColor() {
        internalChatFrame.addUser("User1");
        internalChatFrame.list.setSelectedIndex(0);
        internalChatFrame.actionPerformed(new ActionEvent(internalChatFrame, ActionEvent.ACTION_PERFORMED, "chooseColor"));
        // Verify that JColorChooser.showDialog was called
        Mockito.verify(parentMock).showDialog(Mockito.any(), Mockito.eq("Choose Color for user User1"), Mockito.eq(Color.black));
    }

    @Test
    public void testActionPerformed_BgColor() {
        internalChatFrame.actionPerformed(new ActionEvent(internalChatFrame, ActionEvent.ACTION_PERFORMED, "bgColor"));
        // Verify that JColorChooser.showDialog was called
        Mockito.verify(parentMock).showDialog(Mockito.any(), Mockito.eq("Choose Color for user null"), Mockito.eq(Color.black));
    }

    @Test
    public void testActionPerformed_Whisper() {
        internalChatFrame.addUser("User1");
        internalChatFrame.list.setSelectedIndex(0);
        internalChatFrame.actionPerformed(new ActionEvent(internalChatFrame, ActionEvent.ACTION_PERFORMED, "whisper"));
        assertEquals("/w User1 ", internalChatFrame.txtInput.getText());
    }
}

