package dsachat.client.gui;

import static org.junit.Assert.*;
import org.junit.Test;

public class InternalChatFrameTest {

    @Test
    public void testAddText() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        chatFrame.addText("test");
        assertEquals("test", chatFrame.getChatlog());
    }

    @Test
    public void testAddUser() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        chatFrame.addUser("user1");
        assertEquals("user1", chatFrame.getUserlist()[0]);
    }

    @Test
    public void testDeleteUser() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        chatFrame.addUser("user1");
        chatFrame.deleteUser("user1");
        assertEquals(0, chatFrame.getUserlist().length);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        assertEquals("foo", chatFrame.stripLeadingAndTrailingQuotes("\"foo\""));
    }

    @Test
    public void testStripLeadingHyphens() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        assertEquals("f", chatFrame.stripLeadingHyphens("-f"));
    }
}