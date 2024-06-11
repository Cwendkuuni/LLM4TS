import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

public class MessageHandlerTest {

    @Test
    public void testGetMessage() {
        MessageHandler handler = new MessageHandler();
        String message = handler.getMessage("TestKey", new ArrayList<>());
        assertEquals("Key: TestKey not found in message file", message);
    }

    @Test
    public void testGetMessageOptional() {
        MessageHandler handler = new MessageHandler();
        String message = handler.getMessageOptional("TestKey", new ArrayList<>());
        assertNull(message);
    }

    @Test
    public void testGetInstance() {
        MessageHandler handler1 = MessageHandler.getInstance("Instance1");
        MessageHandler handler2 = MessageHandler.getInstance("Instance2");
        assertNotSame(handler1, handler2);
    }

    @Test
    public void testAddMessages() {
        MessageHandler handler = new MessageHandler();
        handler.addMessages("TestBaseName", "TestDir");
        assertNotNull(handler.mMessages);
    }

    // Add more test cases for edge cases and exceptions as needed

}