import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import javax.swing.text.Style;

public class InternalChatFrameTest {
    
    InternalChatFrame chatFrame;
    
    @Before
    public void setUp() {
        chatFrame = new InternalChatFrame(new MainFrame());
    }
    
    @Test
    public void testAddText() {
        String line = "Test message";
        chatFrame.addText(line);
        assertEquals(line + "\n", chatFrame.getChatlog());
    }
    
    @Test
    public void testAddUser() {
        String user = "TestUser";
        chatFrame.addUser(user);
        String[] userList = chatFrame.getUserlist();
        assertEquals(user, userList[0]);
    }
    
    @Test
    public void testDeleteUser() {
        String user = "TestUser";
        chatFrame.addUser(user);
        chatFrame.deleteUser(user);
        String[] userList = chatFrame.getUserlist();
        assertEquals(0, userList.length);
    }
    
    @Test
    public void testChangeStyle() {
        String styleName = "testStyle";
        chatFrame.addUser(styleName);
        Style style = chatFrame.doc.getStyle(styleName);
        chatFrame.changeStyle(12, null, true, true, styleName);
        assertTrue(StyleConstants.isBold(style));
        assertTrue(StyleConstants.isItalic(style));
    }
    
    @Test
    public void testActionPerformed_chooseColor() {
        String user = "TestUser";
        chatFrame.addUser(user);
        ActionEvent e = new ActionEvent(chatFrame.list, ActionEvent.ACTION_PERFORMED, "chooseColor");
        chatFrame.actionPerformed(e);
        Style style = chatFrame.doc.getStyle(user);
        assertNotNull(StyleConstants.getForeground(style));
    }
    
    @Test
    public void testActionPerformed_bgColor() {
        ActionEvent e = new ActionEvent(chatFrame.list, ActionEvent.ACTION_PERFORMED, "bgColor");
        chatFrame.actionPerformed(e);
        assertNotNull(chatFrame.showTxt.getBackground());
    }
    
    @Test
    public void testActionPerformed_whisper() {
        String user = "TestUser";
        chatFrame.addUser(user);
        ActionEvent e = new ActionEvent(chatFrame.list, ActionEvent.ACTION_PERFORMED, "whisper");
        chatFrame.actionPerformed(e);
        assertEquals("/w " + user + " ", chatFrame.txtInput.getText());
    }
}