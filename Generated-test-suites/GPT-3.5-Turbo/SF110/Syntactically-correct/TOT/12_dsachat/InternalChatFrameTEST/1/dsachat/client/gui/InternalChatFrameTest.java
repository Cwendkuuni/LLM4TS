import org.junit.Test;
import static org.junit.Assert.*;

public class InternalChatFrameTest {

    @Test
    public void testAddText() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        String lineOne = "This is a normal text message";
        chatFrame.addText(lineOne);
        String chatlog = chatFrame.getChatlog();
        assertTrue(chatlog.contains(lineOne));
        
        String lineTwo = "message with style:italic";
        chatFrame.addText(lineTwo);
        chatlog = chatFrame.getChatlog();
        assertTrue(chatlog.contains(lineTwo));
        
        String lineThree = "message with style:bold";
        chatFrame.addText(lineThree);
        chatlog = chatFrame.getChatlog();
        assertTrue(chatlog.contains(lineThree));
    }
    
    @Test
    public void testAddUser() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        String userOne = "User1";
        chatFrame.addUser(userOne);
        String[] userList = chatFrame.getUserlist();
        assertTrue(userList.length == 1);
        assertTrue(userList[0].equals(userOne));
        
        String userTwo = "User2";
        chatFrame.addUser(userTwo);
        userList = chatFrame.getUserlist();
        assertTrue(userList.length == 2);
        assertTrue(userList[0].equals(userTwo));
        assertTrue(userList[1].equals(userOne));
    }
    
    @Test
    public void testDeleteUser() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        String userOne = "User1";
        chatFrame.addUser(userOne);
        chatFrame.deleteUser(userOne);
        String[] userList = chatFrame.getUserlist();
        assertTrue(userList.length == 0);
        
        String userTwo = "User2";
        chatFrame.addUser(userOne);
        chatFrame.addUser(userTwo);
        chatFrame.deleteUser(userOne);
        userList = chatFrame.getUserlist();
        assertTrue(userList.length == 1);
        assertTrue(userList[0].equals(userTwo));
    }
    
    @Test
    public void testActionPerformed() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        // Test "chooseColor" action
        chatFrame.addUser("User1");
        chatFrame.list.setSelectedValue("User1", true);
        chatFrame.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "chooseColor"));
        // Verify color has been changed
        
        // Test "bgColor" action
        chatFrame.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "bgColor"));
        // Verify background color has been changed
        
        // Test "whisper" action
        chatFrame.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "whisper"));
        // Verify txtInput has been set to "/w User1 "
    }

}