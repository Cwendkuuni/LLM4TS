import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class MessageHandlerTest {
    
    @Test
    public void testGetMessage() {
        MessageHandler messageHandler = MessageHandler.getInstance("basename");
        String message = messageHandler.getMessage("key", new ArrayList<>());
        assertNotNull(message);
    }
    
    @Test
    public void testGetMessageOptional() {
        MessageHandler messageHandler = MessageHandler.getInstance("basename");
        String message = messageHandler.getMessageOptional("key", new ArrayList<>());
        assertNotNull(message);
    }
    
    @Test
    public void testGetMessageOptional_NullParams() {
        MessageHandler messageHandler = MessageHandler.getInstance("basename");
        String message = messageHandler.getMessageOptional("key", null);
        assertNotNull(message);
    }
    
    @Test
    public void testGetMessageOptional_NullMessage() {
        MessageHandler messageHandler = MessageHandler.getInstance("basename");
        String message = messageHandler.getMessageOptional("invalid_key", new ArrayList<>());
        assertNull(message);
    }
    
    @Test
    public void testGetInstance() {
        MessageHandler messageHandler = MessageHandler.getInstance("basename");
        assertNotNull(messageHandler);
    }
    
    @Test
    public void testAddMessages() {
        MessageHandler messageHandler = new MessageHandler("basename");
        messageHandler.addMessages("basename", Constants.XBUS_ETC);
        assertNotNull(messageHandler.mMessages);
    }
    
    @Test
    public void testAddMessages_InvalidDir() {
        MessageHandler messageHandler = new MessageHandler("basename");
        messageHandler.addMessages("basename", "invalid_dir");
        assertNotNull(messageHandler.mMessages);
    }
}