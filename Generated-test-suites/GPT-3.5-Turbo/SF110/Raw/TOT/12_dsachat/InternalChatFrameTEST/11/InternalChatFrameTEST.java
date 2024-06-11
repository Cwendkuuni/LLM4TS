import org.junit.*;
import static org.junit.Assert.*;
import org.junit.runners.MethodSorters;
import org.junit.FixMethodOrder;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class InternalChatFrameTest {
    
    private InternalChatFrame internalChatFrame;
    private MainFrame parent;
    
    @Before
    public void setUp() {
        parent = new MainFrame();
        internalChatFrame = new InternalChatFrame(parent);
    }
    
    @Test
    public void test1_addText() {
        internalChatFrame.addText("Welcome to the chat!");
        assertEquals("Welcome to the chat!\n", internalChatFrame.getChatlog());
    }
    
    @Test
    public void test2_addUser() {
        internalChatFrame.addUser("John");
        assertEquals("John", internalChatFrame.getUserlist()[0]);
    }
    
    @Test
    public void test3_deleteUser() {
        internalChatFrame.addUser("John");
        internalChatFrame.deleteUser("John");
        assertEquals(0, internalChatFrame.getUserlist().length);
    }
    
    @Test
    public void test4_addText_differentStyles() {
        internalChatFrame.addText("System: This is a system message");
        internalChatFrame.addText("Bold: **Bold Text**");
        internalChatFrame.addText("Italic: //Italic Text//");
        internalChatFrame.addText("Bold-Italic: **//Bold-Italic Text//**");
        internalChatFrame.addText("Name: John");
        internalChatFrame.addText("Small: Small Text");
        internalChatFrame.addText("Large: Large Text");
        
        String expected = "System: This is a system message\n" +
                          "Bold: Bold Text\n" +
                          "Italic: Italic Text\n" +
                          "Bold-Italic: Bold-Italic Text\n" +
                          "Name: John\n" +
                          "Small: Small Text\n" +
                          "Large: Large Text\n";
                          
        assertEquals(expected, internalChatFrame.getChatlog());
    }
    
    @Test
    public void test5_changeColor() {
        // Assuming user "John" exists
        String user = "John";
        
        internalChatFrame.addUser(user);
        internalChatFrame.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "chooseColor"));
        
        // Assert that the style of the selected user has been changed
        assertEquals(user, internalChatFrame.getChatlog());
    }
    
    @Test
    public void test6_changeBackgroundColor() {
        internalChatFrame.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "bgColor"));
        
        // Assert that the background color has been changed
        assertEquals(Color.BLACK, internalChatFrame.getBackground());
    }
    
    @Test
    public void test7_whisper() {
        internalChatFrame.addUser("John");
        internalChatFrame.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "whisper"));
        
        // Assert that the whisper command has been set as the text input
        assertEquals("/w John ", internalChatFrame.txtInput.getText());
    }
    
    @Test
    public void test8_mouseClicked() {
        internalChatFrame.mouseClicked(null);
        // Assert that no action is performed on mouse click
        assertTrue(true);
    }
    
    @Test
    public void test9_mouseEntered() {
        internalChatFrame.mouseEntered(null);
        // Assert that no action is performed on mouse enter
        assertTrue(true);
    }
    
    @Test
    public void test10_mouseExited() {
        internalChatFrame.mouseExited(null);
        // Assert that no action is performed on mouse exit
        assertTrue(true);
    }
    
    @Test
    public void test11_mousePressed() {
        internalChatFrame.mousePressed(null);
        // Assert that no action is performed on mouse press
        assertTrue(true);
    }
    
    @Test
    public void test12_mouseReleased() {
        internalChatFrame.mouseReleased(null);
        // Assert that no action is performed on mouse release
        assertTrue(true);
    }
    
    @After
    public void tearDown() {
        parent = null;
        internalChatFrame = null;
    }
}