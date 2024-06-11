import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class MessageHandlerTest {

    @Test
    public void testGetMessage() {
        // Basic functionality test
        MessageHandler messageHandler = MessageHandler.getInstance("basename");
        String message = messageHandler.getMessage("key", null);
        assertNull(message);

        // Edge case: empty key
        message = messageHandler.getMessage("", null);
        assertEquals("Key:  not found in message file", message);
    }

    @Test
    public void testGetMessageOptional() {
        // Basic functionality test
        MessageHandler messageHandler = MessageHandler.getInstance("basename");
        String message = messageHandler.getMessageOptional("key", null);
        assertNull(message);

        // Edge case: empty key
        message = messageHandler.getMessageOptional("", null);
        assertNull(message);

        // Edge case: empty params list
        message = messageHandler.getMessageOptional("key", new ArrayList<>());
        assertNull(message);

        // Edge case: null param in params list
        List<Object> params = new ArrayList<>();
        params.add(null);
        message = messageHandler.getMessageOptional("key", params);
        assertNull(message);
    }

    @Test
    public void testGetInstance() {
        // Basic functionality test
        MessageHandler messageHandler = MessageHandler.getInstance("basename");
        assertNotNull(messageHandler);

        // Edge case: null basename
        messageHandler = MessageHandler.getInstance(null);
        assertNotNull(messageHandler);
    }

    @Test
    public void testAddMessages() {
        // Basic functionality test
        MessageHandler messageHandler = new MessageHandler("basename");
        try {
            messageHandler.addMessages("basename", "dir");
        } catch (FileNotFoundException e) {
            fail("FileNotFoundException should not be thrown");
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }

        // Exception handling test: FileNotFoundException
        try {
            messageHandler.addMessages("basename", "nonexistentdir");
            fail("FileNotFoundException should be thrown");
        } catch (FileNotFoundException e) {
            // Exception is expected
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }

        // Exception handling test: IOException
        try {
            messageHandler.addMessages("basename", "existingdir");
            fail("IOException should be thrown");
        } catch (FileNotFoundException e) {
            fail("FileNotFoundException should not be thrown");
        } catch (IOException e) {
            // Exception is expected
        }
    }
}