import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class MessageHandlerTest {

    private MessageHandler messageHandler;

    @Before
    public void setUp() {
        messageHandler = new MessageHandler();
    }
    
    @Test
    public void testGetMessage() {
        String key = "testKey";
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");

        String expected = "Test Message";
        String actual = messageHandler.getMessage(key, params);

        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testGetMessageOptional() {
        String key = "testKey";
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");

        String expected = "Test Optional Message";
        String actual = messageHandler.getMessageOptional(key, params);

        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testGetMessageOptional_NullParams() {
        String key = "testKey";
        List<String> params = null;

        String expected = "Test Optional Message";
        String actual = messageHandler.getMessageOptional(key, params);

        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testGetMessageOptional_NoMessageFound() {
        String key = "invalidKey";
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");

        String actual = messageHandler.getMessageOptional(key, params);

        Assert.assertNull(actual);
    }
    
    @Test
    public void testGetInstance() {
        String basename = "testBasename";

        MessageHandler instance = messageHandler.getInstance(basename);

        Assert.assertNotNull(instance);
    }
    
}