
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
        // Test case without any formatting
        String line1 = "Hello, world!";
        chatFrame.addText(line1);
        assertEquals(line1 + "\n", chatFrame.getChatlog());
        
        // Test case with bold formatting
        String line2 = "This is bold text.";
        chatFrame.addText("*" + line2 + "*");
        assertEquals(line1 + "\n" + line2 + "\n", chatFrame.getChatlog());
        
        // Test case with italic formatting
        String line3 = "This is italic text.";
        chatFrame.addText("_" + line3 + "_");
        assertEquals(line1 + "\n" + line2 + "\n" + line3 + "\n", chatFrame.getChatlog());
        
        // Test case with bold and italic formatting
        String line4 = "This is bold and italic text.";
        chatFrame.addText("*_" + line4 + "_*");
        assertEquals(line1 + "\n" + line2 + "\n" + line3 + "\n" + line4 + "\n", chatFrame.getChatlog());
    }
    
    @Test
    public void testAddUser() {
        chatFrame.addUser("User1");
        assertEquals(1, chatFrame.getUserlist().length);
        assertEquals("User1", chatFrame.getUserlist()[0]);
        
        chatFrame.addUser("User2");
        assertEquals(2, chatFrame.getUserlist().length);
        assertEquals("User2", chatFrame.getUserlist()[0]);
        assertEquals("User1", chatFrame.getUserlist()[1]);
    }
    
    @Test
    public void testDeleteUser() {
        chatFrame.addUser("User1");
        chatFrame.addUser("User2");
        chatFrame.deleteUser("User1");
        assertEquals(1, chatFrame.getUserlist().length);
        assertEquals("User2", chatFrame.getUserlist()[0]);
    }
    
    @Test
    public void testActionPerformed() {
        // Test case for "chooseColor"
        chatFrame.addUser("User1");
        chatFrame.list.setSelectedIndex(0);
        chatFrame.actionPerformed(new ActionEvent(this, 0, "chooseColor"));
        assertNotNull(chatFrame.doc.getStyle("User1"));
        
        // Test case for "bgColor"
        chatFrame.actionPerformed(new ActionEvent(this, 0, "bgColor"));
        assertNotNull(chatFrame.showTxt.getBackground());
        
        // Test case for "whisper"
        chatFrame.addUser("User2");
        chatFrame.list.setSelectedIndex(0);
        chatFrame.actionPerformed(new ActionEvent(this, 0, "whisper"));
        assertEquals("/w User1 ", chatFrame.txtInput.getText());
    }
}
