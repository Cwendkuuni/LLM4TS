import static org.junit.Assert.*;
import org.junit.*;
import java.util.*;

public class MessageHandlerTest {

    @Test
    public void testGetMessage() {
        // Typical Use Case
        MessageHandler handler = MessageHandler.getInstance("basename");
        List<String> params = new ArrayList<String>();
        params.add("param1");
        params.add("param2");
        String message = handler.getMessage("key1", params);
        assertEquals("Expected message text", message);
        
        // Edge Case - Empty key
        String emptyKeyMessage = handler.getMessage("", null);
        assertEquals("Key:  not found in message file", emptyKeyMessage);
        
        // Error Scenario - Null key
        String nullKeyMessage = handler.getMessage(null, null);
        assertEquals("Key:  not found in message file", nullKeyMessage);
    }
    
    @Test
    public void testGetMessageOptional() {
        // Typical Use Case
        MessageHandler handler = MessageHandler.getInstance("basename");
        List<String> params = new ArrayList<String>();
        params.add("param1");
        params.add("param2");
        String message = handler.getMessageOptional("key1", params);
        assertEquals("Expected message text", message);
        
        // Edge Case - Key not found
        String notFoundMessage = handler.getMessageOptional("key2", null);
        assertNull(notFoundMessage);
        
        // Error Scenario - Null key
        String nullKeyMessage = handler.getMessageOptional(null, null);
        assertNull(nullKeyMessage);
    }
    
    @Test
    public void testGetInstance() {
        // Typical Use Case - Existing instance
        MessageHandler handler1 = MessageHandler.getInstance("basename");
        assertNotNull(handler1);
        MessageHandler handler2 = MessageHandler.getInstance("basename");
        assertSame(handler1, handler2);
        
        // Typical Use Case - New instance
        MessageHandler handler3 = MessageHandler.getInstance("newbasename");
        assertNotNull(handler3);
        assertNotSame(handler1, handler3);
        
        // Edge Case - Empty basename
        MessageHandler handler4 = MessageHandler.getInstance("");
        assertNotNull(handler4);
        
        // Error Scenario - Null basename
        MessageHandler handler5 = MessageHandler.getInstance(null);
        assertNotNull(handler5);
    }

    @Test
    public void testAddMessages() {
        // Typical Use Case - Valid basename and dir
        MessageHandler handler = MessageHandler.getInstance("basename");
        handler.addMessages("basename", "dir");
        assertNotNull(handler.getMessages());
        
        // Typical Use Case - Invalid basename
        MessageHandler invalidBasenameHandler = MessageHandler.getInstance("invalid");
        invalidBasenameHandler.addMessages("invalid", "dir");
        assertNull(invalidBasenameHandler.getMessages());
        
        // Edge Case - Invalid dir
        MessageHandler invalidDirHandler = MessageHandler.getInstance("basename");
        invalidDirHandler.addMessages("basename", "invalid");
        assertNull(invalidDirHandler.getMessages());
    }
}