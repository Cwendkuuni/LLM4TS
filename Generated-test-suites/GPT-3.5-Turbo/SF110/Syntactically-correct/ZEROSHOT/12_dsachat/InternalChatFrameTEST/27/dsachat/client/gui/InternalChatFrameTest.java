import junit.framework.*;
import org.junit.*;
import static org.junit.Assert.*;
import dsachat.client.gui.InternalChatFrame;


public class InternalChatFrameTest {
    private InternalChatFrame chatFrame;
    
    @Before
    public void setUp() {
        MainFrame parent = new MainFrame();
        chatFrame = new InternalChatFrame(parent);
    }
    
    @Test
    public void testAddText() {
        chatFrame.addText("Test Message");
        assertEquals("Test Message\n", chatFrame.getChatlog());
    }
    
    @Test
    public void testAddUser() {
        chatFrame.addUser("Test User");
        assertEquals("Test User", chatFrame.getUserlist()[0]);
    }
    
    @Test
    public void testDeleteUser() {
        chatFrame.addUser("Test User");
        chatFrame.deleteUser("Test User");
        assertEquals(0, chatFrame.getUserlist().length);
    }
    
    @Test
    public void testActionPerformed() {
        chatFrame.addUser("Test User");
        chatFrame.list.setSelectedIndex(0);
        ActionEvent chooseColorEvent = new ActionEvent(chatFrame, ActionEvent.ACTION_PERFORMED, "chooseColor");
        chatFrame.actionPerformed(chooseColorEvent);
        //assert expected color change
    }
    
    @Test
    public void testMaybeShowPopup() {
        MouseEvent listMouseEvent = new MouseEvent(chatFrame.list, MouseEvent.MOUSE_PRESSED, 0, 0, 0, 0, 1, false);
        chatFrame.maybeShowPopup(listMouseEvent);
        //assert listPop is shown
        
        MouseEvent showTxtMouseEvent = new MouseEvent(chatFrame.showTxt, MouseEvent.MOUSE_PRESSED, 0, 0, 0, 0, 1, false);
        chatFrame.maybeShowPopup(showTxtMouseEvent);
        //assert txtPop is shown
    }
}