import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import static org.junit.Assert.*;

public class MessageHandlerTest {
    private MessageHandler messageHandler;

    @Before
    public void setUp() {
        messageHandler = MessageHandler.getInstance("basename");
    }

    @Test
    public void testGetMessage() {
        String key = "key";
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");
        
        String messageText = messageHandler.getMessage(key, params);
        
        assertNotNull(messageText);
    }

    @Test
    public void testGetMessageOptional() {
        String key = "key";
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");
        
        String messageText = messageHandler.getMessageOptional(key, params);
        
        assertNotNull(messageText);
    }

    @Test
    public void testGetMessageOptional_NullParams() {
        String key = "key";
        
        String messageText = messageHandler.getMessageOptional(key, null);
        
        assertNotNull(messageText);
    }

    @Test
    public void testGetMessageOptional_NullMessages() {
        MessageHandler messageHandler = new MessageHandler("basename");

        String key = "key";
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");

        String messageText = messageHandler.getMessageOptional(key, params);

        assertNull(messageText);
    }
}