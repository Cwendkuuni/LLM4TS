import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import javax.swing.*;
import javax.swing.text.*;

public class InternalChatFrameTest {
    private InternalChatFrame chatFrame;

    @Before
    public void setUp() {
        MainFrame parent = new MainFrame();
        chatFrame = new InternalChatFrame(parent);
    }

    @After
    public void tearDown() {
        chatFrame = null;
    }

    @Test
    public void testAddText() {
        String line = "Hello World";
        chatFrame.addText(line);
        StyledDocument doc = chatFrame.showTxt.getStyledDocument();
        try {
            String text = doc.getText(0, doc.getLength());
            assert(text.contains(line));
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAddUser() {
        String user = "testUser";
        chatFrame.addUser(user);
        DefaultListModel listModel = chatFrame.listModel;
        boolean userExists = false;
        for (int i = 0; i < listModel.getSize(); i++) {
            String element = (String) listModel.elementAt(i);
            if (element.equals(user)) {
                userExists = true;
                break;
            }
        }
        assert(userExists);
    }

    @Test
    public void testDeleteUser() {
        String user = "testUser";
        chatFrame.addUser(user);
        chatFrame.deleteUser(user);
        DefaultListModel listModel = chatFrame.listModel;
        boolean userExists = false;
        for (int i = 0; i < listModel.getSize(); i++) {
            String element = (String) listModel.elementAt(i);
            if (element.equals(user)) {
                userExists = true;
                break;
            }
        }
        assert(!userExists);
    }

    @Test
    public void testGetUserlist() {
        String[] users = {"user1", "user2", "user3"};
        for (String user : users) {
            chatFrame.addUser(user);
        }
        String[] userlist = chatFrame.getUserlist();
        for (int i = 0; i < userlist.length; i++) {
            assert(userlist[i].equals(users[i]));
        }
    }

    @Test
    public void testActionPerformed() {
        String name = "testUser";
        chatFrame.addUser(name);

        // chooseColor test
        ActionEvent chooseColorEvent = new ActionEvent(chatFrame,
                ActionEvent.ACTION_PERFORMED, "chooseColor");
        chatFrame.actionPerformed(chooseColorEvent);

        // bgColor test
        ActionEvent bgColorEvent = new ActionEvent(chatFrame,
                ActionEvent.ACTION_PERFORMED, "bgColor");
        chatFrame.actionPerformed(bgColorEvent);

        // whisper test
        ActionEvent whisperEvent = new ActionEvent(chatFrame,
                ActionEvent.ACTION_PERFORMED, "whisper");
        chatFrame.actionPerformed(whisperEvent);

        JTextField txtInput = chatFrame.txtInput;
        String expectedText = "/w testUser ";
        assert(txtInput.getText().equals(expectedText));
    }

    @Test
    public void testMaybeShowPopup() {
        MouseEvent event = new MouseEvent(chatFrame.list,
                MouseEvent.MOUSE_RELEASED,
                0, MouseEvent.BUTTON3, 10, 10, 1, false);
        chatFrame.maybeShowPopup(event);
        JPopupMenu listPop = chatFrame.listPop;
        assert(listPop.isVisible());

        event = new MouseEvent(chatFrame.showTxt,
                MouseEvent.MOUSE_RELEASED,
                0, MouseEvent.BUTTON3, 10, 10, 1, false);
        chatFrame.maybeShowPopup(event);
        JPopupMenu txtPop = chatFrame.txtPop;
        assert(txtPop.isVisible());
    }

    @Test
    public void testGetChatlog() {
        String line = "Hello World";
        chatFrame.addText(line);
        String chatlog = chatFrame.getChatlog();
        assert(chatlog.equals(line + "\n"));
    }
}