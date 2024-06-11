package dsachat.client.gui;

import static org.junit.Assert.*;

import org.junit.Test;

public class InternalChatFrameTest {
    @Test
    public void testAddText() {
        InternalChatFrame chatFrame = new InternalChatFrame(null);
        String line1 = "This is a test line.";
        chatFrame.addText(line1);
        assertEquals(line1 + "\n", chatFrame.getChatlog());
        
        String line2 = "This is another test line.";
        chatFrame.addText(line2);
        assertEquals(line1 + "\n" + line2 + "\n", chatFrame.getChatlog());
    }

    @Test
    public void testAddUser() {
        InternalChatFrame chatFrame = new InternalChatFrame(null);
        String user1 = "User1";
        chatFrame.addUser(user1);
        String[] userList1 = chatFrame.getUserlist();
        assertTrue(userList1.length == 1);
        assertEquals(user1, userList1[0]);

        String user2 = "User2";
        chatFrame.addUser(user2);
        String[] userList2 = chatFrame.getUserlist();
        assertTrue(userList2.length == 2);
        assertEquals(user1, userList2[1]);
        assertEquals(user2, userList2[0]);
    }

    @Test
    public void testDeleteUser() {
        InternalChatFrame chatFrame = new InternalChatFrame(null);
        String user1 = "User1";
        chatFrame.addUser(user1);
        String user2 = "User2";
        chatFrame.addUser(user2);
        
        chatFrame.deleteUser(user1);
        String[] userList1 = chatFrame.getUserlist();
        assertTrue(userList1.length == 1);
        assertEquals(user2, userList1[0]);

        chatFrame.deleteUser(user2);
        String[] userList2 = chatFrame.getUserlist();
        assertTrue(userList2.length == 0);
    }
}