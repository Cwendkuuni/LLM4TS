import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class InternalChatFrameTest {
    
    @Mock
    private MainFrame mockMainFrame;
    
    private InternalChatFrame internalChatFrame;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        internalChatFrame = new InternalChatFrame(mockMainFrame);
    }
    
    @Test
    public void testConstructor() {
        assertNotNull(internalChatFrame.list);
        assertNotNull(internalChatFrame.listModel);
        assertNotNull(internalChatFrame.showTxt);
        assertNotNull(internalChatFrame.txtInput);
        assertNotNull(internalChatFrame.parent);
        assertNotNull(internalChatFrame.doc);
        assertNotNull(internalChatFrame.listPop);
        assertNotNull(internalChatFrame.txtPop);
        assertTrue(internalChatFrame.isClosable());
        assertFalse(internalChatFrame.isResizable());
    }
    
    @Test
    public void testAddText_SystemMessage() {
        internalChatFrame.addText("Welcome to the chat");
        assertEquals("Welcome to the chat\n", internalChatFrame.getChatlog());
    }
    
    @Test
    public void testAddText_RegularMessage() {
        internalChatFrame.addText("User1: Hello, how are you?");
        assertEquals("User1: Hello, how are you?\n", internalChatFrame.getChatlog());
    }
    
    @Test
    public void testAddText_WhisperMessage() {
        internalChatFrame.addText("User1 whispers to User2: Please change your color");
        assertEquals("User1 whispers to User2: Please change your color\n", internalChatFrame.getChatlog());
    }
    
    @Test
    public void testAddText_BoldAndItalicMessage() {
        internalChatFrame.addText("User1: *This should be bold* and _this should be italic_");
        assertEquals("User1: *This should be bold* and _this should be italic_\n", internalChatFrame.getChatlog());
    }
    
    @Test
    public void testAddText_BoldMessage() {
        internalChatFrame.addText("User1: *This should be bold*");
        assertEquals("User1: *This should be bold*\n", internalChatFrame.getChatlog());
    }
    
    @Test
    public void testAddText_ItalicMessage() {
        internalChatFrame.addText("User1: _This should be italic_");
        assertEquals("User1: _This should be italic_\n", internalChatFrame.getChatlog());
    }
    
    @Test
    public void testAddText_NameStyle_StartsWithYou() {
        internalChatFrame.addText("You User1: Hello");
        assertEquals("User1: Hello\n", internalChatFrame.getChatlog());
    }
    
    @Test
    public void testAddText_NameStyle_StartsWithAsterisk() {
        internalChatFrame.addText("*User1: Hello*");
        assertEquals("User1: Hello\n", internalChatFrame.getChatlog());
    }
    
    @Test
    public void testAddText_MultipleStyles() {
        internalChatFrame.addText("User1: *Hello* _world_");
        assertEquals("User1: *Hello* _world_\n", internalChatFrame.getChatlog());
    }
    
    @Test
    public void testAddUser() {
        internalChatFrame.addUser("User1");
        assertEquals(1, internalChatFrame.listModel.size());
        assertEquals("User1", internalChatFrame.listModel.get(0));
    }
    
    @Test
    public void testDeleteUser() {
        internalChatFrame.addUser("User1");
        internalChatFrame.deleteUser("User1");
        assertEquals(0, internalChatFrame.listModel.size());
    }
    
    @Test
    public void testGetUserlist() {
        internalChatFrame.addUser("User1");
        internalChatFrame.addUser("User2");
        String[] userlist = internalChatFrame.getUserlist();
        assertEquals(2, userlist.length);
        assertEquals("User2", userlist[0]);
        assertEquals("User1", userlist[1]);
    }
    
    @Test
    public void testActionPerformed_ChooseColorCommand() {
        // Test implementation of JColorChooser.showDialog() needed
    }
    
    @Test
    public void testActionPerformed_BgColorCommand() {
        // Test implementation of JColorChooser.showDialog() needed
    }
    
    @Test
    public void testActionPerformed_WhisperCommand() {
        internalChatFrame.addUser("User1");
        internalChatFrame.list.setSelectedIndex(0);
        internalChatFrame.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "whisper"));
        assertEquals("/w User1 ", internalChatFrame.txtInput.getText());
    }
    
    @Test
    public void testMouseClicked() {
        // Test implementation of mouseClicked() needed (dummy test case)
    }
    
    @Test
    public void testMouseEntered() {
        // Test implementation of mouseEntered() needed (dummy test case)
    }
    
    @Test
    public void testMouseExited() {
        // Test implementation of mouseExited() needed (dummy test case)
    }
    
    @Test
    public void testMousePressed() {
        // Test implementation of mousePressed() needed (dummy test case)
    }
    
    @Test
    public void testMouseReleased() {
        // Test implementation of mouseReleased() needed (dummy test case)
    }
}