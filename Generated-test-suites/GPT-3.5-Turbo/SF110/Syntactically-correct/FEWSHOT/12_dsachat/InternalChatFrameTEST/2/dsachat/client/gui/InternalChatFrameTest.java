package dsachat.clent.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class InternalChatFrameTest {
    @Test
    public void testAddText() {
        InternalChatFrame chatFrame = new InternalChatFrame(null);
        
        chatFrame.addText("Test");
        assertEquals("Test\n", chatFrame.getChatlog());
        
        chatFrame.addText("Test: Message");
        assertEquals("Test: Message\n", chatFrame.getChatlog());
        
        chatFrame.addText("System Message");
        assertEquals("Test: Message\nSystem Message\n", chatFrame.getChatlog());
    }

    @Test
    public void testAddUser() {
        InternalChatFrame chatFrame = new InternalChatFrame(null);
        
        chatFrame.addUser("User1");
        assertEquals("User1\n", chatFrame.getUserlist());
        
        chatFrame.addUser("User2");
        assertEquals("User2\nUser1\n", chatFrame.getUserlist());
    }

    @Test
    public void testDeleteUser() {
        InternalChatFrame chatFrame = new InternalChatFrame(null);
        
        chatFrame.addUser("User1");
        chatFrame.addUser("User2");
        chatFrame.deleteUser("User1");
        assertEquals("User2\n", chatFrame.getUserlist());
        
        chatFrame.deleteUser("User2");
        assertEquals("", chatFrame.getUserlist());
    }
}