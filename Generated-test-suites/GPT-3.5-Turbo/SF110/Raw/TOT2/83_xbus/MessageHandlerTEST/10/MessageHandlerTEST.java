package net.sf.xbus.base.core;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class MessageHandlerTest {

    @Test
    public void testGetMessage() {
        List<String> params = new ArrayList<>();
        params.add("param1");
        String key = "testKey";
        MessageHandler messageHandler = new MessageHandler();
        String message = messageHandler.getMessage(key, params);
        assertNotNull(message);
    }

    @Test
    public void testGetMessageOptional() {
        List<String> params = new ArrayList<>();
        params.add("param1");
        String key = "testKey";
        MessageHandler messageHandler = new MessageHandler();
        String message = messageHandler.getMessageOptional(key, params);
        assertNotNull(message);
    }

    @Test
    public void testGetInstance() {
        String basename = "test";
        MessageHandler messageHandler = MessageHandler.getInstance(basename);
        assertNotNull(messageHandler);
    }

    @Test
    public void testAddMessages() {
        String basename = "test";
        String dir = "src/test/resources/";
        MessageHandler messageHandler = new MessageHandler();
        messageHandler.addMessages(basename, dir);
        assertNotNull(messageHandler.mMessages);
    }
  
    // Other edge cases and error scenarios can be added here

}