import org.junit.*;
import static org.junit.Assert.*;
import java.awt.Color;
import javax.swing.JColorChooser;
import javax.swing.text.*;

public class InternalChatFrameTest {

    private InternalChatFrame chatFrame;

    @Before
    public void setUp() {
        MainFrame parent = new MainFrame();
        chatFrame = new InternalChatFrame(parent);
    }

    @Test
    public void testAddText() {
        String line = "Test message";
        chatFrame.addText(line);
        StyledDocument doc = chatFrame.showTxt.getStyledDocument();
        String chatlog = chatFrame.getChatlog();

        assertEquals(chatlog.trim(), line);

        Style s = doc.getStyle("regular");
        AttributeSet attributes = doc.getCharacterElement(chatlog.length()).getAttributes();

        bold = StyleConstants.isBold(attributes);
        italic = StyleConstants.isItalic(attributes);
        Color foreground = StyleConstants.getForeground(attributes);

        assertFalse(bold);
        assertFalse(italic);
        assertEquals(foreground, Color.BLACK);
    }

    @Test
    public void testChangeStyle() {
        chatFrame.changeStyle(12, Color.BLUE, true, true, "testStyle");

        Style s = chatFrame.doc.getStyle("testStyle");

        AttributeSet attributes = chatFrame.doc.getCharacterElement(0).getAttributes();
        Color foreground = StyleConstants.getForeground(attributes);
        assertTrue(StyleConstants.isBold(attributes));
        assertTrue(StyleConstants.isItalic(attributes));
        assertEquals(foreground, Color.BLUE);
    }

    @Test
    public void testAddUser() {
        String user = "testUser";
        chatFrame.addUser(user);

        ListModel listModel = chatFrame.list.getModel();
        assertEquals(listModel.getElementAt(0), user);

        StyledDocument doc = chatFrame.doc;
        AttributeSet attributes = doc.getStyle(user);

        assertNotNull(attributes);
        assertEquals(StyleConstants.getForeground(attributes), Color.BLUE);

    }

    @Test
    public void testDeleteUser() {
        String user = "testUser";
        chatFrame.addUser(user);
        chatFrame.deleteUser(user);

        ListModel listModel = chatFrame.list.getModel();
        assertEquals(listModel.getSize(), 0);
    }

    @Test
    public void testGetUserlist() {
        String[] users = new String[]{"user1", "user2", "user3"};
        for (String user : users) {
            chatFrame.addUser(user);
        }

        String[] userList = chatFrame.getUserlist();

        assertArrayEquals(userList, users);
    }

    @Test
    public void testActionPerformed() {
        ActionEvent e = new ActionEvent(chatFrame.list, 1, "chooseColor");
        chatFrame.actionPerformed(e);

        assertEquals(JColorChooser.showDialog(chatFrame, "Choose Color for user ", Color.BLACK), chatFrame.parent.getContentPane().getBackground());
    }

    @Test
    public void testMouseClicked() {
        // implement test
    }

    @Test
    public void testMouseEntered() {
        // implement test
    }

    @Test
    public void testMouseExited() {
        // implement test
    }

    @Test
    public void testMousePressed() {
        // implement test
    }

    @Test
    public void testMouseReleased() {
        // implement test
    }

    @After
    public void tearDown() {
        chatFrame = null;
    }
}