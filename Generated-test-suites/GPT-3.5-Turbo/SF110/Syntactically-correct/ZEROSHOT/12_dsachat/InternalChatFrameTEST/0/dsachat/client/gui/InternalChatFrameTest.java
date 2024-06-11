import org.junit.*;
import static org.junit.Assert.*;
import org.mockito.Mockito;

public class InternalChatFrameTest {
    
    private MainFrame parent;
    private InternalChatFrame chatFrame;
    
    @Before
    public void setUp() {
        parent = Mockito.mock(MainFrame.class);
        chatFrame = new InternalChatFrame(parent);
    }
    
    @Test
    public void testAddText_regularMessage() {
        String line = "Hello world";
        chatFrame.addText(line);
        assertEquals(line + "\n", chatFrame.getChatlog());
    }
    
    @Test
    public void testAddText_boldItalicMessage() {
        String line = "*Hello*+_world";
        chatFrame.addText(line);
        assertEquals("Hello_world\n", chatFrame.getChatlog());
    }
    
    @Test
    public void testAddText_boldMessage() {
        String line = "*Hello*";
        chatFrame.addText(line);
        assertEquals("Hello\n", chatFrame.getChatlog());
    }
    
    @Test
    public void testAddText_italicMessage() {
        String line = "+_Hello_+";
        chatFrame.addText(line);
        assertEquals("Hello\n", chatFrame.getChatlog());
    }
    
    @Test
    public void testAddText_changeStyleMessage() {
        String line = "You Hello";
        chatFrame.addText(line);
        assertEquals(line + "\n", chatFrame.getChatlog());
    }
    
    @Test
    public void testAddUser() {
        String user = "user1";
        chatFrame.addUser(user);
        assertEquals(1, chatFrame.getUserlist().length);
        assertEquals(user, chatFrame.getUserlist()[0]);
    }
    
    @Test
    public void testDeleteUser() {
        String user = "user1";
        chatFrame.addUser(user);
        chatFrame.deleteUser(user);
        assertEquals(0, chatFrame.getUserlist().length);
    }
    
    @Test
    public void testActionPerformed_chooseColor() {
        String name = "user1";
        chatFrame.addUser(name);
        ActionEvent e = new ActionEvent(this, 0, "chooseColor");
        chatFrame.actionPerformed(e);
        
        // Verify that the style of the user is changed
        // You can use Mockito to mock the JColorChooser and check the method calls
    }
    
    @Test
    public void testActionPerformed_bgColor() {
        String name = "user1";
        ActionControllerMock actionControllerMock = new ActionControllerMock();
        chatFrame.addActionListener(actionControllerMock);
        chatFrame.addUser(name);
        ActionEvent e = new ActionEvent(this, 0, "bgColor");
        chatFrame.actionPerformed(e);
        
        // Verify that the background color of the JTextPane is changed
        // You can use Mockito to mock the JColorChooser and check the method calls
    }
    
    @Test
    public void testActionPerformed_whisper() {
        String name = "user1";
        chatFrame.addUser(name);
        ActionEvent e = new ActionEvent(this, 0, "whisper");
        chatFrame.actionPerformed(e);
        
        assertEquals("/w user1 ", chatFrame.txtInput.getText());
    }
    
    @Test
    public void testMouseClicked() {
        MouseEvent e = Mockito.mock(MouseEvent.class);
        chatFrame.mouseClicked(e);
        // Add necessary assertions
    }
    
    @Test
    public void testMouseEntered() {
        MouseEvent e = Mockito.mock(MouseEvent.class);
        chatFrame.mouseEntered(e);
        // Add necessary assertions
    }
    
    @Test
    public void testMouseExited() {
        MouseEvent e = Mockito.mock(MouseEvent.class);
        chatFrame.mouseExited(e);
        // Add necessary assertions
    }
    
    @Test
    public void testMousePressed() {
        MouseEvent e = Mockito.mock(MouseEvent.class);
        chatFrame.mousePressed(e);
        // Add necessary assertions
    }
    
    @Test
    public void testMouseReleased() {
        MouseEvent e = Mockito.mock(MouseEvent.class);
        chatFrame.mouseReleased(e);
        // Add necessary assertions
    }
    
    private class ActionControllerMock implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
        }
    }
}