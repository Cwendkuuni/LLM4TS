import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.awt.Color;

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
        String line = "Test line";
        internalChatFrame.addText(line);
        String chatlog = internalChatFrame.getChatlog();
        Assert.assertTrue(chatlog.contains(line));
    }

    @Test
    public void testAddUser() {
        String user = "TestUser";
        internalChatFrame.addUser(user);
        String[] userlist = internalChatFrame.getUserlist();
        Assert.assertTrue(userlist.length == 1);
        Assert.assertEquals(user, userlist[0]);
    }

    @Test
    public void testDeleteUser() {
        String user = "TestUser";
        internalChatFrame.addUser(user);
        internalChatFrame.deleteUser(user);
        String[] userlist = internalChatFrame.getUserlist();
        Assert.assertTrue(userlist.length == 0);
        Assert.assertFalse(internalChatFrame.getChatlog().contains(user));
    }

    @Test
    public void testActionPerformed() {
        internalChatFrame.addUser("TestUser");
        internalChatFrame.list.setSelectedIndex(0);

        // Test chooseColor
        internalChatFrame.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_FIRST, "chooseColor"));
        Assert.assertNotNull(internalChatFrame.doc.getStyle("TestUser"));
        Assert.assertNotNull(internalChatFrame.doc.getStyle("TestUser").getAttribute(StyleConstants.Foreground));

        // Test bgColor
        internalChatFrame.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_FIRST, "bgColor"));
        Color bgColor = internalChatFrame.showTxt.getBackground();
        Assert.assertNotNull(bgColor);

        // Test whisper
        internalChatFrame.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_FIRST, "whisper"));
        Assert.assertNotNull(internalChatFrame.txtInput.getText());
    }

    // Add more test cases for the remaining methods in the InternalChatFrame class

}