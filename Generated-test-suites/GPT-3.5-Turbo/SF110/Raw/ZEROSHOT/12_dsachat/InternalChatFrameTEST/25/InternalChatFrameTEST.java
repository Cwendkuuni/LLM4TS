import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class InternalChatFrameTest {
    private InternalChatFrame chatFrame;

    @Before
    public void setUp() {
        chatFrame = new InternalChatFrame(new MainFrame());
    }

    @Test
    public void testAddText() {
        chatFrame.addText("Test Line");
        assertEquals("Test Line\n", chatFrame.getChatlog());
    }

    @Test
    public void testAddUser() {
        chatFrame.addUser("User1");
        chatFrame.addUser("User2");
        assertEquals(2, chatFrame.getUserlist().length);
    }

    @Test
    public void testDeleteUser() {
        chatFrame.addUser("User1");
        chatFrame.addUser("User2");
        chatFrame.deleteUser("User1");
        assertEquals(1, chatFrame.getUserlist().length);
    }

    @Test
    public void testActionPerformed() {
        // TODO: Implement test for actionPerformed method
    }

    @Test
    public void testMouseClicked() {
        // TODO: Implement test for mouseClicked method
    }

    @Test
    public void testMouseEntered() {
        // TODO: Implement test for mouseEntered method
    }

    @Test
    public void testMouseExited() {
        // TODO: Implement test for mouseExited method
    }

    @Test
    public void testMousePressed() {
        // TODO: Implement test for mousePressed method
    }

    @Test
    public void testMouseReleased() {
        // TODO: Implement test for mouseReleased method
    }

    @Test
    public void testGetChatlog() {
        chatFrame.addText("Test Line");
        assertEquals("Test Line\n", chatFrame.getChatlog());
    }
}