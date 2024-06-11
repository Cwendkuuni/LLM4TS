
import org.junit.*;
import static org.junit.Assert.*;

public class InternalChatFrameTest {
    
    @Test
    public void testConstructor() {
        MainFrame parent = new MainFrame();
        InternalChatFrame internalChatFrame = new InternalChatFrame(parent);
        
        assertNotNull(internalChatFrame);
    }
    
    @Test
    public void testAddText() {
        MainFrame parent = new MainFrame();
        InternalChatFrame internalChatFrame = new InternalChatFrame(parent);
        
        internalChatFrame.addText("Test message");
        String chatlog = internalChatFrame.getChatlog();
        
        assertEquals("Test message\n", chatlog);
    }
    
    @Test
    public void testAddUser() {
        MainFrame parent = new MainFrame();
        InternalChatFrame internalChatFrame = new InternalChatFrame(parent);
        
        internalChatFrame.addUser("User1");
        String[] userlist = internalChatFrame.getUserlist();
        
        assertEquals("User1", userlist[0]);
    }
    
    @Test
    public void testDeleteUser() {
        MainFrame parent = new MainFrame();
        InternalChatFrame internalChatFrame = new InternalChatFrame(parent);
        
        internalChatFrame.addUser("User1");
        internalChatFrame.deleteUser("User1");
        String[] userlist = internalChatFrame.getUserlist();
        
        assertEquals(0, userlist.length);
    }
    
    @Test
    public void testActionPerformedChooseColor() {
        MainFrame parent = new MainFrame();
        InternalChatFrame internalChatFrame = new InternalChatFrame(parent);
        
        internalChatFrame.addUser("User1");
        
        ActionEvent e = new ActionEvent(internalChatFrame, ActionEvent.ACTION_PERFORMED, "chooseColor");
        internalChatFrame.actionPerformed(e);
        
        // verify that the color of the user style is different than the default color
        // assumes that the default color is black
        assertNotEquals(Color.BLACK, internalChatFrame.getChatlog().getStyle("User1").getAttributes().get(StyleConstants.Foreground));
    }
    
    @Test
    public void testActionPerformedBgColor() {
        MainFrame parent = new MainFrame();
        InternalChatFrame internalChatFrame = new InternalChatFrame(parent);
        
        ActionEvent e = new ActionEvent(internalChatFrame, ActionEvent.ACTION_PERFORMED, "bgColor");
        internalChatFrame.actionPerformed(e);
        
        // verify that the background color of the text pane is different than the default color
        // assumes that the default color is black
        assertNotEquals(Color.BLACK, internalChatFrame.getChatlog().getBackground());
    }
    
    @Test
    public void testActionPerformedWhisper() {
        MainFrame parent = new MainFrame();
        InternalChatFrame internalChatFrame = new InternalChatFrame(parent);
        
        internalChatFrame.addUser("User1");
        
        ActionEvent e = new ActionEvent(internalChatFrame, ActionEvent.ACTION_PERFORMED, "whisper");
        internalChatFrame.actionPerformed(e);
        
        assertEquals("/w User1 ", internalChatFrame.txtInput.getText());
    }
}
