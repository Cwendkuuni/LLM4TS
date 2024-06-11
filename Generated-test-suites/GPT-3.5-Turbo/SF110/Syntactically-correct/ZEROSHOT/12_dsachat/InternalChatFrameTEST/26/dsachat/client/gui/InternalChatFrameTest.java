import org.junit.Before;
import org.junit.Test;
import java.awt.Color;
import javax.swing.JColorChooser;
import static org.junit.Assert.*;

public class InternalChatFrameTest {

    private MainFrame parent;
    private InternalChatFrame internalChatFrame;

    @Before
    public void setUp() {
        parent = new MainFrame();
        internalChatFrame = new InternalChatFrame(parent);
    }

    @Test
    public void testAddText() {
        internalChatFrame.addText("Test message");
        assertEquals("Test message\n", internalChatFrame.getChatlog());
    }

    @Test
    public void testAddUser() {
        internalChatFrame.addUser("User1");
        internalChatFrame.addUser("User2");
        internalChatFrame.addUser("User3");
        assertEquals(3, internalChatFrame.getUserlist().length);
    }

    @Test
    public void testDeleteUser() {
        internalChatFrame.addUser("User1");
        internalChatFrame.addUser("User2");
        internalChatFrame.deleteUser("User1");
        assertEquals(1, internalChatFrame.getUserlist().length);
    }

    @Test
    public void testChangeStyle() {
        internalChatFrame.addUser("User1");
        internalChatFrame.changeStyle(12, Color.RED, true, false, "User1");
    }

    @Test
    public void testActionPerformed() {
        internalChatFrame.addUser("User1");
        ActionEvent e = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "chooseColor");
        internalChatFrame.actionPerformed(e);
    }

    @Test
    public void testMouseClicked() {
    }

    @Test
    public void testMouseEntered() {
    }

    @Test
    public void testMouseExited() {
    }

    @Test
    public void testMousePressed() {
    }

    @Test
    public void testMouseReleased() {
    }

    @Test
    public void testGetChatlog() {
        internalChatFrame.addText("Test message 1");
        internalChatFrame.addText("Test message 2");
        assertEquals("Test message 1\nTest message 2\n", internalChatFrame.getChatlog());
    }
}