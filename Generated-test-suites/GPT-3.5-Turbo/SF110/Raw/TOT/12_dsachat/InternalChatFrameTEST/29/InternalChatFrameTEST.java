import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class InternalChatFrameTest {

    private InternalChatFrame chatFrame;
    private MainFrame parent;

    @Before
    public void setUp() {
        parent = new MainFrame();
        chatFrame = new InternalChatFrame(parent);
    }

    @After
    public void tearDown() {
        chatFrame = null;
        parent = null;
    }

    /**
     * Test case for the addText method
     * Testing regular input - no special formatting
     */
    @Test
    public void testAddTextRegularInput() {
        chatFrame.addText("This is a regular input");
        assertEquals("This is a regular input\n", chatFrame.getChatlog());
    }

    /**
     * Test case for the addText method
     * Testing input with italic formatting
     */
    @Test
    public void testAddTextItalicFormatting() {
        chatFrame.addText("*Italic Text*");
        assertEquals("*Italic Text*\n", chatFrame.getChatlog());
    }

    /**
     * Test case for the addText method
     * Testing input with bold formatting
     */
    @Test
    public void testAddTextBoldFormatting() {
        chatFrame.addText("+Bold Text+");
        assertEquals("+Bold Text+\n", chatFrame.getChatlog());
    }

    /**
     * Test case for the addText method
     * Testing input with both italic and bold formatting
     */
    @Test
    public void testAddTextItalicAndBoldFormatting() {
        chatFrame.addText("*Italic Text 1*+Bold Text 1+*Italic Text 2*+Bold Text 2+");
        assertEquals("*Italic Text 1*\n+Bold Text 1+\n*Italic Text 2*\n+Bold Text 2+\n", chatFrame.getChatlog());
    }

    /**
     * Test case for the addText method
     * Testing input with user-specific formatting
     */
    @Test
    public void testAddTextUserSpecificFormatting() {
        parent.hero.setName("User1");
        chatFrame.addText("User1: Message from User1");
        assertEquals("<html><font color=\"blue\" size=\"16\">User1</font>: Message from User1</html>\n", chatFrame.getChatlog());
    }

    /**
     * Test case for the addText method
     * Testing input with system formatting
     */
    @Test
    public void testAddTextSystemFormatting() {
        chatFrame.addText("System message");
        assertEquals("<html><font color=\"#009900\" size=\"14\"><i>System message</i></font><font size=\"14\">&nbsp;</font></html>\n", chatFrame.getChatlog());
    }

    /**
     * Test case for the addUser method
     * Testing adding a user to the user list
     */
    @Test
    public void testAddUser() {
        chatFrame.addUser("User1");
        String[] userlist = chatFrame.getUserlist();
        assertEquals(1, userlist.length);
        assertEquals("User1", userlist[0]);
    }

    /**
     * Test case for the deleteUser method
     * Testing deleting a user from the user list
     */
    @Test
    public void testDeleteUser() {
        chatFrame.addUser("User1");
        chatFrame.addUser("User2");
        chatFrame.deleteUser("User1");
        String[] userlist = chatFrame.getUserlist();
        assertEquals(1, userlist.length);
        assertEquals("User2", userlist[0]);
    }

    /**
     * Test case for the actionPerformed method
     * Testing choosing a color for a user
     */
    @Test
    public void testActionPerformedChooseColor() {
        chatFrame.addUser("User1");
        chatFrame.list.setSelectedIndex(0);
        chatFrame.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "chooseColor"));
        // Verify if the color is properly set for the selected user
        // Mock the JColorChooser to return Color.RED
        assertEquals(Color.RED, chatFrame.doc.getStyle("User1").getAttribute(StyleConstants.Foreground));
    }

    /**
     * Test case for the actionPerformed method
     * Testing choosing a background color
     */
    @Test
    public void testActionPerformedChooseBackgroundColor() {
        chatFrame.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "bgColor"));
        // Verify if the background color is properly set
        // Mock the JColorChooser to return Color.YELLOW
        assertEquals(Color.YELLOW, chatFrame.showTxt.getBackground());
    }

    /**
     * Test case for the actionPerformed method
     * Testing whispering to a user
     */
    @Test
    public void testActionPerformedWhisper() {
        chatFrame.addUser("User1");
        chatFrame.list.setSelectedIndex(0);
        chatFrame.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "whisper"));
        // Verify if the whisper command is properly set in the input field
        assertEquals("/w User1 ", chatFrame.txtInput.getText());
    }
}