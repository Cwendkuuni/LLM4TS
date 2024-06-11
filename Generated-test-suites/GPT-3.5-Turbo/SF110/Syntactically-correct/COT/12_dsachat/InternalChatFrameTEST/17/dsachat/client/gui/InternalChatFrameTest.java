import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class InternalChatFrameTest {

    private MainFrame parent;
    private InternalChatFrame frame;

    @Before
    public void setUp() {
        parent = new MainFrame();
        frame = new InternalChatFrame(parent);
    }

    @Test
    public void testAddText() {
        frame.addText("Test Message");
        assertEquals("Test Message\n", frame.getChatlog());
    }

    @Test
    public void testAddUser() {
        frame.addUser("User1");
        assertEquals(1, frame.getUserlist().length);
        assertEquals("User1", frame.getUserlist()[0]);
    }

    @Test
    public void testDeleteUser() {
        frame.addUser("User1");
        frame.deleteUser("User1");
        assertEquals(0, frame.getUserlist().length);
    }

    @Test
    public void testActionPerformedChooseColor() {
        frame.addUser("User1");
        frame.list.setSelectedIndex(0);
        frame.actionPerformed(new ActionEvent(this, 0, "chooseColor"));
    }

    @Test
    public void testActionPerformedBgColor() {
        frame.addUser("User1");
        frame.list.setSelectedIndex(0);
        frame.actionPerformed(new ActionEvent(this, 0, "bgColor"));
    }

    @Test
    public void testActionPerformedWhisper() {
        frame.addUser("User1");
        frame.list.setSelectedIndex(0);
        frame.actionPerformed(new ActionEvent(this, 0, "whisper"));
    }

}
