import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;
import java.util.ArrayList;

public class MessageHandlerTest {
    
    @Test
    public void testGetMessage() {
        MessageHandler handler = new MessageHandler();
        List<String> params = new ArrayList<>();
        params.add("param1");
        String message = handler.getMessage("testKey", params);
        assertNotNull(message);
    }
    
    @Test
    public void testGetMessageOptional() {
        MessageHandler handler = new MessageHandler();
        List<String> params = new ArrayList<>();
        params.add("param1");
        String message = handler.getMessageOptional("testKey", params);
        assertNull(message);
    }
    
    @Test
    public void testGetInstance() {
        MessageHandler handler = MessageHandler.getInstance("testInstance");
        assertNotNull(handler);
    }
    
    @Test
    public void testAddMessages() {
        MessageHandler handler = new MessageHandler("testInstance");
        handler.addMessages("testBaseName", "testDir");
        assertNotNull(handler.mMessages);
    }
    
    @Test
    public void testMessagesFilterAccept() {
        MessageHandler.MessagesFilter filter = new MessageHandler.MessagesFilter("testPrefix", "testPostfix");
        assertTrue(filter.accept(new File("testDir"), "testPrefix_testPostfix"));
    }
}