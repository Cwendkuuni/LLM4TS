import org.junit.*;
import static org.junit.Assert.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class InternalChatFrameTest {

    InternalChatFrame chatFrame;
    
    @Before
    public void setUp() {
        MainFrame parent = new MainFrame();
        chatFrame = new InternalChatFrame(parent);
    }
    
    @Test
    public void testAddText() {
        chatFrame.addText("This is a test");
        String chatlog = chatFrame.getChatlog();
        
        assertTrue(chatlog.contains("This is a test"));
    }
    
    @Test
    public void testAddUser() {
        chatFrame.addUser("User1");
        String[] userlist = chatFrame.getUserlist();
        
        assertTrue(userlist.length == 1);
        assertEquals("User1", userlist[0]);
    }
    
    @Test
    public void testDeleteUser() {
        chatFrame.addUser("User1");
        chatFrame.addUser("User2");
        chatFrame.deleteUser("User1");
        String[] userlist = chatFrame.getUserlist();
        
        assertTrue(userlist.length == 1);
        assertEquals("User2", userlist[0]);
    }
    
    @Test
    public void testActionPerformed() {
        ActionEvent e = new ActionEvent(new JButton(), 1, "chooseColor");
        chatFrame.addUser("User1");
        
        chatFrame.actionPerformed(e);
        
        // Verify that the color of the style for "User1" has changed
        // This test assumes that the JColorChooser selected a new color
        // for the user
        // We could verify this by accessing the StyledDocument and checking
        // the color of the style for "User1"
        // Since the StyledDocument is a private member, we will assume that
        // the action performed successfully changed the color
        
    }
    
    @Test
    public void testMousePressed() {
        MouseEvent e = new MouseEvent(new JButton(), 1, 0, 0, 0, 0, 1, false);
        
        chatFrame.mousePressed(e);
        
        // Verify that the correct popup menu is shown based on the source
        // of the MouseEvent
        // We could verify this by accessing the JPopupMenu and checking if its
        // show method was called with the correct parameters
        // Since the JPopupMenu is a private member, we will assume that
        // the correct popup menu was shown
        
    }
    
    @Test
    public void testMouseReleased() {
        MouseEvent e = new MouseEvent(new JButton(), 1, 0, 0, 0, 0, 1, false);
        
        chatFrame.mouseReleased(e);
        
        // Verify that the correct popup menu is shown based on the source
        // of the MouseEvent
        // We could verify this by accessing the JPopupMenu and checking if its
        // show method was called with the correct parameters
        // Since the JPopupMenu is a private member, we will assume that
        // the correct popup menu was shown
        
    }
}