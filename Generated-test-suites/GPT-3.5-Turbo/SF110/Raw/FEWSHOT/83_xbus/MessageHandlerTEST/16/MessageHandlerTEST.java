package net.sf.xbus.base.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

public class MessageHandlerTest {
    private MessageHandler messageHandler;
    
    @Before
    public void setUp() {
        messageHandler = MessageHandler.getInstance("basename");
    }
    
    @Test
    public void testGetMessage() {
        List<Object> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");
        
        assertEquals("Key: testKey not found in message file", messageHandler.getMessage("testKey", null));
        assertEquals("Key: testKey not found in message file", messageHandler.getMessage("testKey", params));
    }
    
    @Test
    public void testGetMessageOptional() {
        List<Object> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");
        
        assertNull(messageHandler.getMessageOptional("testKey", null));
        assertNull(messageHandler.getMessageOptional("testKey", params));
    }
}