package net.sf.xbus.base.core;

import org.junit.Test;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class MessageHandlerTest {

    @Test
    public void testGetMessage() {
        // Test case 1: messageText is null
        MessageHandler messageHandler = mock(MessageHandler.class);
        List<String> params = new ArrayList<>();
        when(messageHandler.getMessageOptional(anyString(), anyList())).thenReturn(null);
        String actualOutput = messageHandler.getMessage("testKey", params);
        String expectedOutput = "Key: testKey not found in message file";
        assertEquals(expectedOutput, actualOutput);
        verify(messageHandler).getMessageOptional(anyString(), anyList());

        // Test case 2: messageText is not null
        when(messageHandler.getMessageOptional(anyString(), anyList())).thenReturn("Test message");
        actualOutput = messageHandler.getMessage("testKey", params);
        expectedOutput = "Test message";
        assertEquals(expectedOutput, actualOutput);
        verify(messageHandler, times(2)).getMessageOptional(anyString(), anyList());
    }
  
    @Test
    public void testGetMessageOptional() {
        // Test case 1: mMessages is null
        MessageHandler messageHandler = new MessageHandler("testBasename");
        List<String> params = new ArrayList<>();
        String actualOutput = messageHandler.getMessageOptional("testKey", params);
        String expectedOutput = null;
        assertEquals(expectedOutput, actualOutput);

        // Test case 2: mMessages is not null, params is null
        messageHandler.mMessages = new Hashtable();
        messageHandler.mMessages.put("testKey", "Test message");
        actualOutput = messageHandler.getMessageOptional("testKey", null);
        expectedOutput = "Test message";
        assertEquals(expectedOutput, actualOutput);

        // Test case 3: mMessages is not null, params is not null
        params.add("param1");
        params.add("param2");
        actualOutput = messageHandler.getMessageOptional("testKey", params);
        expectedOutput = "Test message";
        assertEquals(expectedOutput, actualOutput.replace("$1$", "param1").replace("$2$", "param2"));
    }

    @Test
    public void testGetInstance() {
        // Test case 1: instance is null
        MessageHandler instance = MessageHandler.getInstance("testBasename");
        assertEquals("testBasename", instance.getClass().getName().split("\\$")[0]);
        assertEquals("testBasename", instance.getClass().getSimpleName().split("\\$")[0]);

        // Test case 2: instance is not null
        MessageHandler.mInstances.put("testBasename", instance);
        instance = MessageHandler.getInstance("testBasename");
        assertEquals("testBasename", instance.getClass().getName().split("\\$")[0]);
        assertEquals("testBasename", instance.getClass().getSimpleName().split("\\$")[0]);
    }

    @Test
    public void testAddMessages() {
        // Test case 1: messagesFiles is null
        MessageHandler messageHandler = new MessageHandler("testBasename");
        String dir = "testDir";
        messageHandler.addMessages("testBasename", dir);
        assertEquals(null, messageHandler.mMessages);

        // Test case 2: messagesFiles is not null
        File dirFile = mock(File.class);
        when(dirFile.list(any(MessageHandler.MessagesFilter.class))).thenReturn(new String[]{"test.properties"});
        messageHandler.addMessages("testBasename", dir);
        assertEquals("test message", messageHandler.mMessages.get("testKey"));
    }
}