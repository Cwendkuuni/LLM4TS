
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

public class MessageHandlerTest {

    @Test
    public void testGetMessage() {
        MessageHandler messageHandler = new MessageHandler();
        String message = messageHandler.getMessage("testKey", new ArrayList<>());
        assertEquals("Key: testKey not found in message file", message);
    }

    @Test
    public void testGetMessageOptional() {
        MessageHandler messageHandler = new MessageHandler();
        String message = messageHandler.getMessageOptional("testKey", new ArrayList<>());
        assertNull(message);
    }

    @Test
    public void testGetInstance() {
        MessageHandler messageHandler = MessageHandler.getInstance("testInstance");
        assertNotNull(messageHandler);
    }

    @Test
    public void testAddMessages() {
        MessageHandler messageHandler = new MessageHandler("testBasename");
        messageHandler.addMessages("testBasename", "testDir");
        assertEquals(0, messageHandler.mMessages.size());
    }

}
