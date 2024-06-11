import static org.junit.Assert.*;
import org.junit.Test;

public class InternalChatFrameTest {

    @Test
    public void testInternalChatFrameConstructor() {
        MainFrame parent = new MainFrame();
        InternalChatFrame icf = new InternalChatFrame(parent);
        assertNotNull(icf);
    }

    @Test
    public void testAddText() {
        InternalChatFrame icf = new InternalChatFrame(new MainFrame());
        icf.addText("Test message");
        assertEquals("Test message\n", icf.getChatlog());
    }

    @Test
    public void testAddUser() {
        InternalChatFrame icf = new InternalChatFrame(new MainFrame());
        icf.addUser("User1");
        String[] userList = icf.getUserlist();
        assertEquals(1, userList.length);
        assertEquals("User1", userList[0]);
    }

    @Test
    public void testDeleteUser() {
        InternalChatFrame icf = new InternalChatFrame(new MainFrame());
        icf.addUser("User1");
        icf.deleteUser("User1");
        String[] userList = icf.getUserlist();
        assertEquals(0, userList.length);
    }

    @Test
    public void testActionPerformed() {
        InternalChatFrame icf = new InternalChatFrame(new MainFrame());
        // Test case for "chooseColor" action
        icf.addUser("User1");
        // Assuming "chooseColor" action changes color to red
        String user = icf.getUserlist()[0];
        icf.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "chooseColor"));
        // Check if color was changed
        assertEquals(Color.RED, icf.getDoc().getStyle(user).getAttributes().get(StyleConstants.Foreground));
    }

    @Test
    public void testMaybeShowPopup() {
        InternalChatFrame icf = new InternalChatFrame(new MainFrame());
        // Test case for showing popup on list component
        MouseEvent eList = new MouseEvent(icf.getList(), MouseEvent.MOUSE_PRESSED, System.currentTimeMillis(), 0, 10, 10, 1, false);
        icf.mousePressed(eList);
        assertTrue(icf.getListPop().isVisible());

        // Test case for showing popup on text component
        MouseEvent eTxt = new MouseEvent(icf.getShowTxt(), MouseEvent.MOUSE_PRESSED, System.currentTimeMillis(), 0, 10, 10, 1, false);
        icf.mousePressed(eTxt);
        assertTrue(icf.getTxtPop().isVisible());
    }

    @Test
    public void testChangeStyle() {
        InternalChatFrame icf = new InternalChatFrame(new MainFrame());
        icf.addUser("User1");
        icf.changeStyle(12, Color.BLUE, true, false, "User1");
        assertEquals(Color.BLUE, icf.getDoc().getStyle("User1").getAttributes().get(StyleConstants.Foreground));
        assertTrue((boolean) icf.getDoc().getStyle("User1").getAttributes().get(StyleConstants.Bold));
        assertFalse((boolean) icf.getDoc().getStyle("User1").getAttributes().get(StyleConstants.Italic));
    }

}