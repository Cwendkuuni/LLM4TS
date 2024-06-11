import org.junit.*;
import static org.junit.Assert.*;

public class InternalChatFrameTest {
    private InternalChatFrame chatFrame;
    
    @Before
    public void setUp() {
        MainFrame parent = new MainFrame();
        chatFrame = new InternalChatFrame(parent);
    }
    
    @Test
    public void testAddText_regularMessage() {
        chatFrame.addText("Hello world");
        assertEquals("Hello world\n", chatFrame.getChatlog());
    }
    
    @Test
    public void testAddText_whisperMessage() {
        chatFrame.addText("User1 whispers: Hello User2");
        assertEquals("User1 whispers: Hello User2\n", chatFrame.getChatlog());
    }
    
    @Test
    public void testAddText_boldAndItalicMessage() {
        chatFrame.addText("*Bold* _Italic_");
        assertEquals("*Bold* _Italic_\n", chatFrame.getChatlog());
    }
    
    @Test
    public void testAddText_boldMessage() {
        chatFrame.addText("*Bold* text");
        assertEquals("*Bold* text\n", chatFrame.getChatlog());
    }
    
    @Test
    public void testAddText_italicMessage() {
        chatFrame.addText("_Italic_ text");
        assertEquals("_Italic_ text\n", chatFrame.getChatlog());
    }
    
    @Test
    public void testAddText_nameStyleMessage() {
        chatFrame.addText("User: Hello");
        assertEquals("User: Hello\n", chatFrame.getChatlog());
    }
    
    @Test
    public void testAddText_systemMessage() {
        chatFrame.addText("System message");
        assertEquals("System message\n", chatFrame.getChatlog());
    }
    
    @Test
    public void testAddText_smallMessage() {
        chatFrame.addText("Small message");
        assertEquals("Small message\n", chatFrame.getChatlog());
    }
    
    @Test
    public void testAddText_largeMessage() {
        chatFrame.addText("Large message");
        assertEquals("Large message\n", chatFrame.getChatlog());
    }
    
    @Test
    public void testAddUser_singleUser() {
        chatFrame.addUser("User1");
        String[] userList = chatFrame.getUserlist();
        assertEquals(1, userList.length);
        assertEquals("User1", userList[0]);
    }
    
    @Test
    public void testAddUser_multipleUsers() {
        chatFrame.addUser("User1");
        chatFrame.addUser("User2");
        chatFrame.addUser("User3");
        String[] userList = chatFrame.getUserlist();
        assertEquals(3, userList.length);
        assertEquals("User1", userList[0]);
        assertEquals("User2", userList[1]);
        assertEquals("User3", userList[2]);
    }
    
    @Test
    public void testDeleteUser_singleUser() {
        chatFrame.addUser("User1");
        chatFrame.deleteUser("User1");
        String[] userList = chatFrame.getUserlist();
        assertEquals(0, userList.length);
    }
    
    @Test
    public void testDeleteUser_multipleUsers() {
        chatFrame.addUser("User1");
        chatFrame.addUser("User2");
        chatFrame.addUser("User3");
        chatFrame.deleteUser("User2");
        String[] userList = chatFrame.getUserlist();
        assertEquals(2, userList.length);
        assertEquals("User1", userList[0]);
        assertEquals("User3", userList[1]);
    }
    
    @After
    public void tearDown() {
        chatFrame = null;
    }
}