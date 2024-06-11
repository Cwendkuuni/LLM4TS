package net.sf.xbus.base.core;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

public class MessageHandlerTest {

    private MessageHandler messageHandler;

    @Before
    public void setup() {
        messageHandler = new MessageHandler();
    }

    @Test
    public void testGetMessage_WhenKeyExists_ReturnsMessageText() {
        String key = "MessageKey";
        List<String> params = new ArrayList<>();
        params.add("Param1");
        
        // Add message to mMessages
        messageHandler.mMessages.put(key, "MessageText with $1$");

        String expected = "MessageText with Param1";
        String actual = messageHandler.getMessage(key, params);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetMessage_WhenKeyNotExist_ReturnsErrorMessage() {
        String key = "NonExistentKey";
        List<String> params = new ArrayList<>();

        String expected = "Key: NonExistentKey not found in message file";
        String actual = messageHandler.getMessage(key, params);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetMessageOptional_WhenKeyExists_ReturnsMessageText() {
        String key = "MessageKey";
        List<String> params = new ArrayList<>();
        params.add("Param1");
        
        // Add message to mMessages
        messageHandler.mMessages.put(key, "MessageText with $1$");

        String expected = "MessageText with Param1";
        String actual = messageHandler.getMessageOptional(key, params);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetMessageOptional_WhenKeyNotExist_ReturnsNull() {
        String key = "NonExistentKey";
        List<String> params = new ArrayList<>();

        String actual = messageHandler.getMessageOptional(key, params);

        assertNull(actual);
    }

    @Test
    public void testGetMessageOptional_WhenParamsIsNull_ReturnsMessageText() {
        String key = "MessageKey";

        // Add message to mMessages
        messageHandler.mMessages.put(key, "MessageText");

        String expected = "MessageText";
        String actual = messageHandler.getMessageOptional(key, null);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetInstance_WhenInstanceExists_ReturnsExistingInstance() {
        String basename = "Basename";

        // Add instance to mInstances
        MessageHandler existingInstance = new MessageHandler(basename);
        messageHandler.mInstances.put(basename, existingInstance);

        MessageHandler actual = MessageHandler.getInstance(basename);

        assertSame(existingInstance, actual);
    }

    @Test
    public void testGetInstance_WhenInstanceDoesNotExist_CreatesNewInstance() {
        String basename = "Basename";

        // Remove any existing instance from mInstances
        messageHandler.mInstances.remove(basename);

        MessageHandler actual = MessageHandler.getInstance(basename);

        assertNotNull(actual);
    }

    @Test
    public void testGetInstance_WhenMultipleThreadsAccess_GetSameInstance() throws InterruptedException {
        String basename = "Basename";

        // Remove any existing instance from mInstances
        messageHandler.mInstances.remove(basename);

        // Create multiple threads accessing getInstance concurrently
        List<MessageHandler> instances = new ArrayList<>();
        int numThreads = 5;
        for (int i = 0; i < numThreads; i++) {
            Thread thread = new Thread(() -> {
                MessageHandler instance = MessageHandler.getInstance(basename);
                instances.add(instance);
            });
            thread.start();
        }

        // Wait for all threads to finish
        for (Thread thread : threads) {
            thread.join();
        }

        // Ensure all instances are the same
        for (MessageHandler instance : instances) {
            assertSame(instances.get(0), instance);
        }
    }

    // Additional test cases can be added for constructor, addMessages, MessagesFilter methods

}