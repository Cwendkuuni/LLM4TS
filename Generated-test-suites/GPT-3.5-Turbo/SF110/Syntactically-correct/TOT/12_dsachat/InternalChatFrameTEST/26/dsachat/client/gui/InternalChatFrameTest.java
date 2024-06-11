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
        chatFrame.addText("Regular message");
        assertEquals("Regular message\n", chatFrame.getChatlog());

        chatFrame.addText("Italic message");
        assertEquals("Regular message\nItalic message\n", chatFrame.getChatlog());

        chatFrame.addText("Bold message");
        assertEquals("Regular message\nItalic message\nBold message\n", chatFrame.getChatlog());

        chatFrame.addText("*Italic and Bold*");
        assertEquals("Regular message\nItalic message\nBold message\n*Italic and Bold*\n", chatFrame.getChatlog());

        chatFrame.addText("+Bold and Italic+");
        assertEquals("Regular message\nItalic message\nBold message\n*Italic and Bold*\n+Bold and Italic+\n", chatFrame.getChatlog());

        chatFrame.addText("+Bold+");
        assertEquals("Regular message\nItalic message\nBold message\n*Italic and Bold*\n+Bold and Italic+\n+Bold+\n", chatFrame.getChatlog());
    }

    @Test
    public void testAddUser() {
        chatFrame.addUser("User1");
        chatFrame.addUser("User2");
        chatFrame.addUser("User3");

        String[] expectedUserlist = {"User3", "User2", "User1"};
        assertArrayEquals(expectedUserlist, chatFrame.getUserlist());
    }

    @Test
    public void testDeleteUser() {
        chatFrame.addUser("User1");
        chatFrame.addUser("User2");
        chatFrame.addUser("User3");

        chatFrame.deleteUser("User2");

        String[] expectedUserlist = {"User3", "User1"};
        assertArrayEquals(expectedUserlist, chatFrame.getUserlist());
    }

    @Test
    public void testActionPerformed_ChooseColor() {
        // Action is triggered when choosing color for user
        // Test can be implemented as a UI test
        // Mock the JColorChooser behavior and verify that the changeStyle method is called with the correct parameters
    }

    @Test
    public void testActionPerformed_BGColor() {
        // Action is triggered when choosing background color for user
        // Test can be implemented as a UI test
        // Mock the JColorChooser behavior and verify that the showTxt background color is set correctly
    }
}