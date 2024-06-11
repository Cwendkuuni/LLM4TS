import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class InternalChatFrameTest {

    private InternalChatFrame chatFrame;
    
    @Before
    public void setUp() {
        MainFrame parent = mock(MainFrame.class);
        chatFrame = new InternalChatFrame(parent);
    }
    
    @Test
    public void testAddText() {
        String line = "Test Message";
        chatFrame.addText(line);
        assertEquals(line + "\n", chatFrame.getChatlog());
    }
    
    @Test
    public void testAddUser() {
        String user = "TestUser";
        chatFrame.addUser(user);
        assertEquals(user, chatFrame.getUserlist()[0]);
    }
    
    @Test
    public void testDeleteUser() {
        String user = "TestUser";
        chatFrame.addUser(user);
        chatFrame.deleteUser(user);
        assertEquals(0, chatFrame.getUserlist().length);
    }

    @Test
    public void testActionPerformedChooseColor() {
        ActionEvent e = new ActionEvent(chatFrame.list, 0, "chooseColor");
        chatFrame.addUser("TestUser");
        chatFrame.actionPerformed(e);
        // Verify that color change happens
        // assertNotNull(chatFrame.doc.getStyle("TestUser"));
    }
    
    @Test
    public void testActionPerformedBgColor() {
        ActionEvent e = new ActionEvent(chatFrame.list, 0, "bgColor");
        chatFrame.actionPerformed(e);
        // Verify background color change
        // assertNotNull(chatFrame.showTxt.getBackground());
    }
    
    @Test
    public void testActionPerformedWhisper() {
        ActionEvent e = new ActionEvent(chatFrame.list, 0, "whisper");
        chatFrame.addUser("TestUser");
        chatFrame.actionPerformed(e);
        assertEquals("/w TestUser ", chatFrame.txtInput.getText());
    }
}