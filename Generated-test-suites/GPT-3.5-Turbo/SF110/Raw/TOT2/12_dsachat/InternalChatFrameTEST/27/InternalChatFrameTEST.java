import org.junit.Test;
import static org.junit.Assert.*;
import dsachat.client.gui.InternalChatFrame;

public class InternalChatFrameTest {

    @Test
    public void testAddText() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        chatFrame.addText("Test message");
        assertEquals("Test message\n", chatFrame.getChatlog());
    }

    @Test
    public void testAddUser() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        chatFrame.addUser("User1");
        String[] userList = chatFrame.getUserlist();
        assertEquals("User1", userList[0]);
    }

    @Test
    public void testDeleteUser() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        chatFrame.addUser("User1");
        chatFrame.deleteUser("User1");
        String[] userList = chatFrame.getUserlist();
        assertEquals(0, userList.length);
    }

    @Test
    public void testActionPerformed() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        chatFrame.addUser("User1");
        chatFrame.showTxt.setBackground(Color.WHITE);
        chatFrame.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "bgColor"));
        assertEquals(Color.WHITE, chatFrame.showTxt.getBackground());
    }

    @Test
    public void testMouseClicked() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        //TODO: Write test case for mouseClicked method
    }

    @Test
    public void testMousePressed() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        //TODO: Write test case for mousePressed method
    }

    @Test
    public void testMouseReleased() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        //TODO: Write test case for mouseReleased method
    }

    // Add more test cases for other methods as needed

}