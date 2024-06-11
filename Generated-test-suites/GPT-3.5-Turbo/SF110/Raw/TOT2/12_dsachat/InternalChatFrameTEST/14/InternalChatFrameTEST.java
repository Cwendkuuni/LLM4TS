import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class InternalChatFrameTest {

    @Test
    public void testAddUser() {
        InternalChatFrame chatFrame = new InternalChatFrame(mock(MainFrame.class));
        chatFrame.addUser("User1");
        assertEquals(1, chatFrame.listModel.size());
        assertTrue(chatFrame.listModel.contains("User1"));
    }

    @Test
    public void testDeleteUser() {
        InternalChatFrame chatFrame = new InternalChatFrame(mock(MainFrame.class));
        chatFrame.addUser("User1");
        assertEquals(1, chatFrame.listModel.size());
        chatFrame.deleteUser("User1");
        assertEquals(0, chatFrame.listModel.size());
        assertFalse(chatFrame.listModel.contains("User1"));
    }

    @Test
    public void testGetUserlist() {
        InternalChatFrame chatFrame = new InternalChatFrame(mock(MainFrame.class));
        chatFrame.addUser("User1");
        chatFrame.addUser("User2");
        String[] userList = chatFrame.getUserlist();
        assertEquals(2, userList.length);
        assertTrue(userList[0].equals("User1") || userList[0].equals("User2"));
        assertTrue(userList[1].equals("User1") || userList[1].equals("User2"));
    }
    
    // Add more test cases for other methods here

}