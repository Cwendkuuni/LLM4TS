import org.junit.*;

import java.io.*;
import java.util.*;

public class MessageHandlerTest {

    @Test
    public void testGetMessage() {
        MessageHandler messageHandler = MessageHandler.getInstance("test");
        String message = messageHandler.getMessage("key", null);
        Assert.assertEquals("Key: key not found in message file", message);
    }

    @Test
    public void testGetMessageOptional() {
        MessageHandler messageHandler = MessageHandler.getInstance("test");
        String message = messageHandler.getMessageOptional("key", null);
        Assert.assertNull(message);
    }

    @Test
    public void testGetInstance() {
        MessageHandler messageHandler = MessageHandler.getInstance("test");
        Assert.assertNotNull(messageHandler);
    }

    @Test
    public void testConstructor() {
        MessageHandler messageHandler = new MessageHandler("test");
        Assert.assertNotNull(messageHandler);
    }

    @Test
    public void testAddMessages() {
        MessageHandler messageHandler = new MessageHandler("test");
        messageHandler.addMessages("test", "testDir");
        Assert.assertEquals(0, messageHandler.mMessages.size());
    }

    // Edge cases and exception handling scenarios

    @Test
    public void testGetMessageNotFound() {
        MessageHandler messageHandler = MessageHandler.getInstance("test");
        String message = messageHandler.getMessage("nonexistentKey", null);
        Assert.assertEquals("Key: nonexistentKey not found in message file", message);
    }

    @Test
    public void testGetMessageOptionalNullParams() {
        MessageHandler messageHandler = MessageHandler.getInstance("test");
        String message = messageHandler.getMessageOptional("key", null);
        Assert.assertNull(message);
    }

    @Test
    public void testGetMessageOptionalNullParam() {
        MessageHandler messageHandler = MessageHandler.getInstance("test");
        List<Object> params = new ArrayList<>();
        params.add(null);
        String message = messageHandler.getMessageOptional("key", params);
        Assert.assertNotNull(message);
        Assert.assertTrue(message.contains("<null>"));
    }

    @Test
    public void testGetMessageOptionalParamReplacement() {
        MessageHandler messageHandler = MessageHandler.getInstance("test");
        List<Object> params = new ArrayList<>();
        params.add("param1");
        String message = messageHandler.getMessageOptional("key$1$", params);
        Assert.assertNotNull(message);
        Assert.assertEquals("param1", message);
    }

    @Test
    public void testGetMessageOptionalMissingParam() {
        MessageHandler messageHandler = MessageHandler.getInstance("test");
        List<Object> params = new ArrayList<>();
        params.add("param1");
        String message = messageHandler.getMessageOptional("key$2$", params);
        Assert.assertNotNull(message);
        Assert.assertEquals("Key: key$2$ not found in message file", message);
    }

    @Test
    public void testAddMessagesIOException() {
        MessageHandler messageHandler = new MessageHandler("test");
        messageHandler.addMessages("test", "nonexistentDir");
        Assert.assertEquals(0, messageHandler.mMessages.size());
    }

    @Test
    public void testMessagesFilterAcceptTrue() {
        MessageHandler.MessagesFilter filter = new MessageHandler.MessagesFilter("prefix", "postfix");
        File dir = new File("testDir");
        Assert.assertTrue(filter.accept(dir, "prefix_file_postfix"));
    }

    @Test
    public void testMessagesFilterAcceptFalse() {
        MessageHandler.MessagesFilter filter = new MessageHandler.MessagesFilter("prefix", "postfix");
        File dir = new File("testDir");
        Assert.assertFalse(filter.accept(dir, "file_postfix"));
        Assert.assertFalse(filter.accept(dir, "prefix_file"));
        Assert.assertFalse(filter.accept(dir, "prefix_file_post"));
    }

    // Test suite marker methods

    @BeforeClass
    public static void beforeClass() {
        System.out.println("");
    }

    @AfterClass
    public static void afterClass() {
        System.out.println("");
    }

}