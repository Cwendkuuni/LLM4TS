import org.junit.*;
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
        assertEquals("Test Message\n", internalChatFrame.getChatlog());
    }
    
    @Test
    public void testAddUser() {
        internalChatFrame.addUser("TestUser");
        String[] userList = internalChatFrame.getUserlist();
        assertTrue(userList.length > 0);
        assertEquals("TestUser", userList[0]);
    }
    
    @Test
    public void testDeleteUser() {
        internalChatFrame.addUser("TestUser");
        internalChatFrame.deleteUser("TestUser");
        String[] userList = internalChatFrame.getUserlist();
        assertEquals(0, userList.length);
    }
    
    @Test
    public void testActionPerformed() {
        // Test for chooseColor action
        internalChatFrame.addUser("TestUser");
        internalChatFrame.list.setSelectedIndex(0);
        internalChatFrame.actionPerformed(new ActionEvent(internalChatFrame, ActionEvent.ACTION_PERFORMED, "chooseColor"));
        assertEquals(12, internalChatFrame.doc.getStyle("TestUser").getAttribute(StyleConstants.FontSize));
        // Test for bgCOlor action
        internalChatFrame.actionPerformed(new ActionEvent(internalChatFrame, ActionEvent.ACTION_PERFORMED, "bgColor"));
        assertNotNull(internalChatFrame.showTxt.getBackground());
        // Test for whisper action
        internalChatFrame.actionPerformed(new ActionEvent(internalChatFrame, ActionEvent.ACTION_PERFORMED, "whisper"));
        assertEquals("/w TestUser ", internalChatFrame.txtInput.getText());
    }
    
}