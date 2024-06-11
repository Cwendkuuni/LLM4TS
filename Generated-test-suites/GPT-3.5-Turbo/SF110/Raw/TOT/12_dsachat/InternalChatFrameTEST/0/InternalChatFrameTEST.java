import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class InternalChatFrameTest {
    
    private InternalChatFrame chatFrame;
    private MainFrame parent;
    
    @Before
    public void setUp() {
        parent = new MainFrame();
        chatFrame = new InternalChatFrame(parent);
    }
    
    @Test
    public void testAddText_validLine() {
        String line = "This is a valid line";
        chatFrame.addText(line);
        assertEquals(line + "\n", chatFrame.getChatlog());
    }
    
    @Test
    public void testAddText_lineWithoutColon() {
        String line = "This line does not contain a colon";
        String expectedLine = line + "\n";
        chatFrame.addText(line);
        assertEquals(expectedLine, chatFrame.getChatlog());
    }
    
    @Test
    public void testAddText_whispersStyleName() {
        String line = "whispers: This is a whispered message";
        String expectedLine = "This is a whispered message\n";
        chatFrame.addText(line);
        assertEquals(expectedLine, chatFrame.getChatlog());
    }
    
    @Test
    public void testAddText_styleNameStartsWithYou() {
        String line = "You Some message from you";
        String expectedLine = parent.hero.getName() + " Some message from you\n";
        chatFrame.addText(line);
        assertEquals(expectedLine, chatFrame.getChatlog());
    }
    
    @Test
    public void testAddText_styleNameStartsWithStar() {
        String line = "*Bold text* _Italic text_";
        String expectedLine = "Bold text" + "Italic text\n";
        chatFrame.addText(line);
        assertEquals(expectedLine, chatFrame.getChatlog());
    }
    
    @Test
    public void testAddText_lineWithBoldAndItalic() {
        String line = "Text *Bold and Italic*";
        String expectedLine = "Text " + "Bold and Italic" + "\n";
        chatFrame.addText(line);
        assertEquals(expectedLine, chatFrame.getChatlog());
    }
    
    @Test
    public void testAddText_lineWithMultipleBold() {
        String line = "Text *Bold* text *Bold* text";
        String expectedLine = "Text " + "Bold" + " text " + "Bold" + " text\n";
        chatFrame.addText(line);
        assertEquals(expectedLine, chatFrame.getChatlog());
    }
    
    @Test
    public void testAddText_lineWithMultipleItalic() {
        String line = "Text _Italic_ text _Italic_ text";
        String expectedLine = "Text " + "Italic" + " text " + "Italic" + " text\n";
        chatFrame.addText(line);
        assertEquals(expectedLine, chatFrame.getChatlog());
    }
    
    @Test
    public void testAddText_lineWithNoStyleApplied() {
        String line = "Text with no style";
        String expectedLine = line + "\n";
        chatFrame.addText(line);
        assertEquals(expectedLine, chatFrame.getChatlog());
    }
    
    @Test(expected = BadLocationException.class)
    public void testAddText_badLocationException() {
        String line = null;
        chatFrame.addText(line);
    }
    
    @Test
    public void testAddUser_validUser() {
        String user = "User1";
        chatFrame.addUser(user);
        String[] expectedUserlist = {user};
        assertArrayEquals(expectedUserlist, chatFrame.getUserlist());
    }
    
    @Test
    public void testDeleteUser_existingUser() {
        String user = "User1";
        chatFrame.addUser(user);
        chatFrame.deleteUser(user);
        String[] expectedUserlist = {};
        assertArrayEquals(expectedUserlist, chatFrame.getUserlist());
    }
    
    @Test
    public void testGetUserlist_noUser() {
        String[] expectedUserlist = {};
        assertArrayEquals(expectedUserlist, chatFrame.getUserlist());
    }
    
    @Test
    public void testActionPerformed_chooseColorCommand() {
        String name = "User1";
        Color newColor = Color.red;
        chatFrame.addUser(name);
        chatFrame.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "chooseColor"));
        assertEquals(newColor, chatFrame.getUserColor(name));
    }
    
    @Test
    public void testActionPerformed_bgColorCommand() {
        Color newColor = Color.blue;
        chatFrame.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "bgColor"));
        assertEquals(newColor, chatFrame.getBackgroundColor());
    }
    
    @Test
    public void testActionPerformed_whisperCommand() {
        String name = "User1";
        chatFrame.addUser(name);
        chatFrame.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "whisper"));
        String wUser = "/w " + name + " ";
        assertEquals(wUser, chatFrame.getTxtInputText());
    }
    
    @Test
    public void testMouseClicked() {
        // Implementation specific test, no assertions
    }
    
    @Test
    public void testMouseEntered() {
        // Implementation specific test, no assertions        
    }
    
    @Test
    public void testMouseExited() {
        // Implementation specific test, no assertions  
    }
    
    @Test
    public void testMousePressed() {
        // Implementation specific test, no assertions  
    }
    
    @Test
    public void testMouseReleased() {
        // Implementation specific test, no assertions  
    }
    
    @Test
    public void testGetChatlog_emptyChatlog() {
        String expectedChatlog = "";
        assertEquals(expectedChatlog, chatFrame.getChatlog());
    }
}