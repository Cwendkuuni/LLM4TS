package dsachat.client.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class InternalChatFrameTest {
    @Test
    public void testAddText() {
        InternalChatFrame chatFrame = new InternalChatFrame(null);
        chatFrame.addText("This is a test");
        assertEquals("This is a test\n", chatFrame.getChatlog());
    }

    @Test
    public void testAddUser() {
        InternalChatFrame chatFrame = new InternalChatFrame(null);
        chatFrame.addUser("User1");
        chatFrame.addUser("User2");
        assertEquals(2, chatFrame.getUserlist().length);
        assertEquals("User2", chatFrame.getUserlist()[0]);
        assertEquals("User1", chatFrame.getUserlist()[1]);
    }

    @Test
    public void testDeleteUser() {
        InternalChatFrame chatFrame = new InternalChatFrame(null);
        chatFrame.addUser("User1");
        chatFrame.addUser("User2");
        chatFrame.deleteUser("User1");
        assertEquals(1, chatFrame.getUserlist().length);
        assertEquals("User2", chatFrame.getUserlist()[0]);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        InternalChatFrame chatFrame = new InternalChatFrame(null);
        assertEquals("foo", chatFrame.stripLeadingAndTrailingQuotes("\"foo\""));
        assertEquals("foo bar", chatFrame.stripLeadingAndTrailingQuotes("foo \"bar\""));
        assertEquals("foo\" bar", chatFrame.stripLeadingAndTrailingQuotes("\"foo\" bar"));
        assertEquals("foo and bar", chatFrame.stripLeadingAndTrailingQuotes("\"foo\" and \"bar\""));
        assertEquals("", chatFrame.stripLeadingAndTrailingQuotes("\""));
    }

    @Test
    public void testStripLeadingHyphens() {
        InternalChatFrame chatFrame = new InternalChatFrame(null);
        assertEquals("f", chatFrame.stripLeadingHyphens("-f"));
        assertEquals("foo", chatFrame.stripLeadingHyphens("--foo"));
        assertEquals("-foo", chatFrame.stripLeadingHyphens("---foo"));
        assertNull(chatFrame.stripLeadingHyphens(null));
    }
}