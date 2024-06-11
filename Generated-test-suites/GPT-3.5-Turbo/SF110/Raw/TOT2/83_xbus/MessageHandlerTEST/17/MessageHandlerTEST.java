import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

public class MessageHandlerTest {
    
    @Test
    public void testGetMessage() {
        MessageHandler handler = new MessageHandler();
        String key = "testKey";
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");
        
        String result = handler.getMessage(key, params);
        
        assertEquals("Key: testKey not found in message file", result);
    }
    
    @Test
    public void testGetMessageOptional() {
        MessageHandler handler = new MessageHandler();
        String key = "testKey";
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");
        
        String result = handler.getMessageOptional(key, params);
        
        assertNull(result);
    }
    
    @Test
    public void testGetInstance() {
        MessageHandler instance1 = MessageHandler.getInstance("basename1");
        MessageHandler instance2 = MessageHandler.getInstance("basename2");
        
        assertNotNull(instance1);
        assertNotNull(instance2);
        assertNotSame(instance1, instance2);
    }
    
    @Test
    public void testAddMessages() {
        MessageHandler handler = new MessageHandler();
        
        handler.addMessages("testBasename", "testDir");
        
        assertNotNull(handler.mMessages);
        assertTrue(handler.mMessages.size() > 0);
    }
    
    @Test
    public void testMessagesFilter() {
        MessageHandler.MessagesFilter filter = new MessageHandler.MessagesFilter("testPrefix", "testPostfix");
        File testDir = new File("testDir");
        String testFilename = "testPrefix_testPostfix.properties";
        
        assertTrue(filter.accept(testDir, testFilename));
    }

}