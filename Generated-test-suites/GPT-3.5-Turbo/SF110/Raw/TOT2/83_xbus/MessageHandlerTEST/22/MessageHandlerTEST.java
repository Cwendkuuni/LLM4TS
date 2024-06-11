package net.sf.xbus.base.core;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.List;
import java.util.ArrayList;

public class MessageHandlerTest {

    @Test
    public void testGetMessage() {
        MessageHandler handler = new MessageHandler();
        String key = "Test";
        List<String> params = new ArrayList<String>();
        params.add("param1");
        params.add("param2");

        String result = handler.getMessage(key, params);

        assertNotNull(result);
        assertEquals("Key: Test not found in message file", result);
        
        // Add more test cases here to cover edge cases and error scenarios
    }

    @Test
    public void testGetMessageOptional() {
        MessageHandler handler = new MessageHandler();
        String key = "Test";
        List<String> params = new ArrayList<String>();
        params.add("param1");

        String result = handler.getMessageOptional(key, params);

        assertNull(result);
        
        // Add more test cases here to cover edge cases and error scenarios
    }

    @Test
    public void testGetInstance() {
        String basename = "Test";
        MessageHandler instance = MessageHandler.getInstance(basename);

        assertNotNull(instance);
    }

    // Add more test cases for other methods in MessageHandler class

}