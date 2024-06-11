import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class MessageHandlerTest {

    @Test
    public void testGetMessage() {
        List<String> params = new ArrayList<>();
        params.add("1");
        params.add("2");

        MessageHandler messageHandler = MessageHandler.getInstance("basename");
        String actualMessage = messageHandler.getMessage("key", params);

        String expectedMessage = "Key: key not found in message file";
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testGetMessageOptional() {
        List<String> params = new ArrayList<>();
        params.add("1");
        params.add("2");

        MessageHandler messageHandler = MessageHandler.getInstance("basename");
        String actualMessage = messageHandler.getMessageOptional("key", params);

        assertNull(actualMessage);
    }

    @Test
    public void testGetInstance() {
        MessageHandler messageHandler1 = MessageHandler.getInstance("basename1");
        MessageHandler messageHandler2 = MessageHandler.getInstance("basename2");

        assertNotNull(messageHandler1);
        assertNotNull(messageHandler2);
        assertNotSame(messageHandler1, messageHandler2);
    }

    @Test
    public void testAddMessages() {
        MessageHandler messageHandler = MessageHandler.getInstance("basename");
        messageHandler.addMessages("basename", "dir");

        // Test if messages are added to mMessages hashtable correctly
        assertNotNull(messageHandler.mMessages);
        assertFalse(messageHandler.mMessages.isEmpty());
    }

    @Test
    public void testMessagesFilter() {
        MessageHandler.MessagesFilter filter = new MessageHandler.MessagesFilter("prefix", "postfix");

        File dir = new File("dir");
        String filename1 = "prefix_locale.properties";
        String filename2 = "other_prefix_locale.properties";
        String filename3 = "prefix_otherpostfix.properties";

        assertTrue(filter.accept(dir, filename1));
        assertFalse(filter.accept(dir, filename2));
        assertFalse(filter.accept(dir, filename3));
    }
}