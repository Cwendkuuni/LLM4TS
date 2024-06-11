import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class MessageHandlerTest {

    private MessageHandler messageHandler;

    @Before
    public void setUp() {
        messageHandler = new MessageHandler("testBasename");
    }

    @Test
    public void testGetMessage_keyExistsInMessages_ReturnsMessageText() {
        String key = "existingKey";
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");

        String expectedMessage = "This is a message";
        messageHandler.mMessages.put(key, expectedMessage);

        String actualMessage = messageHandler.getMessage(key, params);

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testGetMessage_keyNotExistsInMessages_ReturnsErrorMessage() {
        String key = "nonExistingKey";
        List<String> params = new ArrayList<>();
        params.add("param1");

        String expectedMessage = "Key: " + key + " not found in message file";
        String actualMessage = messageHandler.getMessage(key, params);

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testGetMessageOptional_keyExistsInMessages_ReturnsMessageText() {
        String key = "existingKey";
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");

        String expectedMessage = "This is a message";
        messageHandler.mMessages.put(key, expectedMessage);

        String actualMessage = messageHandler.getMessageOptional(key, params);

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testGetMessageOptional_keyNotExistsInMessages_ReturnsNull() {
        String key = "nonExistingKey";
        List<String> params = new ArrayList<>();
        params.add("param1");

        String actualMessage = messageHandler.getMessageOptional(key, params);

        assertNull(actualMessage);
    }

    @Test
    public void testGetMessageOptional_NoParams_ReturnsMessageText() {
        String key = "existingKey";
        List<String> params = new ArrayList<>();

        String expectedMessage = "This is a message";
        messageHandler.mMessages.put(key, expectedMessage);

        String actualMessage = messageHandler.getMessageOptional(key, params);

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testGetMessageOptional_NullParam_ReturnsMessageTextWithNullValue() {
        String key = "existingKey";
        List<String> params = new ArrayList<>();
        params.add(null);

        String expectedMessage = "This is a message with null value: <null>";
        messageHandler.mMessages.put(key, expectedMessage);

        String actualMessage = messageHandler.getMessageOptional(key, params);

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testGetInstance_FirstInstance_ReturnsNewInstance() {
        String basename = "testBasename";

        MessageHandler actualInstance = MessageHandler.getInstance(basename);

        assertNotNull(actualInstance);
        assertEquals(1, MessageHandler.mInstances.size());
        assertTrue(MessageHandler.mInstances.containsKey(basename));
        assertEquals(actualInstance, MessageHandler.mInstances.get(basename));
    }

    @Test
    public void testGetInstance_ExistingInstance_ReturnsSameInstance() {
        String basename = "testBasename";

        MessageHandler existingInstance = MessageHandler.getInstance(basename);
        MessageHandler actualInstance = MessageHandler.getInstance(basename);

        assertNotNull(actualInstance);
        assertEquals(1, MessageHandler.mInstances.size());
        assertTrue(MessageHandler.mInstances.containsKey(basename));
        assertEquals(existingInstance, actualInstance);
    }

    @Test
    public void testAddMessages_MessagesFilesExist_PopulatesMessagesHashtable() {
        String basename = "testBasename";
        String dir = "testDir";

        String messagesFile1 = "testBasename_en.properties";
        String messagesFile2 = "testBasename_de.properties";
        String messagesFile3 = "testBasename_fr.properties";
        String[] messagesFiles = {messagesFile1, messagesFile2, messagesFile3};

        File dirFile = new File(dir);
        dirFile.list((dir1, filename) -> {
            return filename.startsWith(basename) && filename.endsWith(".properties");
        });

        for (String messagesFile : messagesFiles) {
            Properties properties = new Properties();
            properties.put("key1", "value1");
            properties.put("key2", "value2");
            messageHandler.mMessages.put(messagesFile, properties);
        }

        messageHandler.addMessages(basename, dir);

        assertEquals(6, messageHandler.mMessages.size());
        assertEquals("value1", messageHandler.mMessages.get("key1"));
        assertEquals("value2", messageHandler.mMessages.get("key2"));
    }

    @Test(expected = FileNotFoundException.class)
    public void testAddMessages_MessagesFilesNotExist_ThrowsFileNotFoundException() {
        String basename = "testBasename";
        String dir = "testDir";

        String[] messagesFiles = {};

        File dirFile = new File(dir);
        dirFile.list((dir1, filename) -> {
            return filename.startsWith(basename) && filename.endsWith(".properties");
        });

        for (String messagesFile : messagesFiles) {
            Properties properties = new Properties();
            properties.put("key1", "value1");
            properties.put("key2", "value2");
            messageHandler.mMessages.put(messagesFile, properties);
        }

        messageHandler.addMessages(basename, dir);
    }

    @Test(expected = IOException.class)
    public void testAddMessages_IOException_ThrowsIOException() {
        String basename = "testBasename";
        String dir = "testDir";

        String messagesFile = "testBasename_en.properties";

        File dirFile = new File(dir);
        dirFile.list((dir1, filename) -> {
            return filename.startsWith(basename) && filename.endsWith(".properties");
        });

        Properties properties = new Properties();
        properties.put("key1", "value1");
        properties.put("key2", "value2");
        messageHandler.mMessages.put(messagesFile, properties);

        messageHandler.addMessages(basename, dir);
    }

    @Test
    public void testMessagesFilter_AcceptsCorrectFiles_ReturnsTrue() {
        String prefix = "testBasename";
        String postfix = "_en.properties";
        File dir = new File("testDir");

        boolean accept = new MessageHandler.MessagesFilter(prefix, postfix).accept(dir, "testBasename_en.properties");

        assertTrue(accept);
    }

    @Test
    public void testMessagesFilter_DoesNotAcceptIncorrectFiles_ReturnsFalse() {
        String prefix = "testBasename";
        String postfix = "_en.properties";
        File dir = new File("testDir");

        boolean accept = new MessageHandler.MessagesFilter(prefix, postfix).accept(dir, "testBasename_fr.properties");

        assertFalse(accept);
    }
}