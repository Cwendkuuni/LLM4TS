import org.junit.Assert;
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
    public void testGetMessage_KeyExists_ReturnsMessageText() {
        String key = "Key1";
        List<String> params = new ArrayList<>();
        params.add("Param1");
        params.add("Param2");
        String expectedMessageText = "Message Text";
        messageHandler.mMessages = new Hashtable();
        messageHandler.mMessages.put(key, expectedMessageText);

        String actualMessageText = messageHandler.getMessage(key, params);

        Assert.assertEquals(expectedMessageText, actualMessageText);
    }

    @Test
    public void testGetMessage_KeyNotFound_ReturnsErrorMessage() {
        String key = "Key2";
        List<String> params = new ArrayList<>();
        params.add("Param1");
        params.add("Param2");
        String expectedMessageText = "Key: Key2 not found in message file";
        messageHandler.mMessages = new Hashtable();

        String actualMessageText = messageHandler.getMessage(key, params);

        Assert.assertEquals(expectedMessageText, actualMessageText);
    }

    @Test
    public void testGetMessageOptional_KeyExists_ReturnsMessageText() {
        String key = "Key1";
        List<String> params = new ArrayList<>();
        params.add("Param1");
        params.add("Param2");
        String expectedMessageText = "Message Text";
        messageHandler.mMessages = new Hashtable();
        messageHandler.mMessages.put(key, expectedMessageText);

        String actualMessageText = messageHandler.getMessageOptional(key, params);

        Assert.assertEquals(expectedMessageText, actualMessageText);
    }

    @Test
    public void testGetMessageOptional_KeyNotFound_ReturnsNull() {
        String key = "Key2";
        List<String> params = new ArrayList<>();
        params.add("Param1");
        params.add("Param2");
        messageHandler.mMessages = new Hashtable();

        String actualMessageText = messageHandler.getMessageOptional(key, params);

        Assert.assertNull(actualMessageText);
    }

    @Test
    public void testGetMessageOptional_NullParams_ReturnsMessageTextWithNullParamsReplaced() {
        String key = "Key1";
        List<String> params = null;
        String expectedMessageText = "Message Text: $1$ $2$";
        messageHandler.mMessages = new Hashtable();
        messageHandler.mMessages.put(key, "Message Text: $1$ $2$");

        String actualMessageText = messageHandler.getMessageOptional(key, params);

        Assert.assertEquals(expectedMessageText, actualMessageText);
    }

    @Test
    public void testGetInstance_InstanceExists_ReturnsExistingInstance() {
        String basename = "Basename";
        messageHandler.mInstances = new Hashtable();
        MessageHandler expectedInstance = new MessageHandler(basename);
        messageHandler.mInstances.put(basename, expectedInstance);

        MessageHandler actualInstance = messageHandler.getInstance(basename);

        Assert.assertSame(expectedInstance, actualInstance);
    }

    @Test
    public void testGetInstance_InstanceDoesNotExist_CreatesNewInstance() {
        String basename = "Basename";
        messageHandler.mInstances = new Hashtable();
        messageHandler.mInstances.clear();
        MessageHandler expectedInstance = new MessageHandler(basename);

        MessageHandler actualInstance = messageHandler.getInstance(basename);

        Assert.assertNotNull(actualInstance);
        Assert.assertSame(expectedInstance, actualInstance);
        Assert.assertTrue(messageHandler.mInstances.containsKey(basename));
        Assert.assertSame(expectedInstance, messageHandler.mInstances.get(basename));
    }

    @Test
    public void testAddMessages_ValidParams_AddsMessages() {
        String basename = "Basename";
        String dir = "Directory";
        String messagesFile1 = basename + "_en.properties";
        String messagesFile2 = basename + "_fr.properties";
        messageHandler.mMessages = new Hashtable();
        File directory = Mockito.mock(File.class);
        String[] messagesFiles = { messagesFile1, messagesFile2 };
        Mockito.when(directory.list(Mockito.any(FilenameFilter.class))).thenReturn(messagesFiles);
        Mockito.when(directory.getAbsolutePath()).thenReturn(dir);
        Mockito.when(directory.listFiles(Mockito.any(FilenameFilter.class))).thenReturn(messagesFiles);
        Properties properties1 = new Properties();
        properties1.put("Key1", "Message1");
        Properties properties2 = new Properties();
        properties2.put("Key2", "Message2");
        FileInputStream inputStream = Mockito.mock(FileInputStream.class);
        Mockito.when(inputStream.read(Mockito.any(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(-1);
        Mockito.when(inputStream.read(Mockito.any())).thenReturn((int) '\n').thenReturn(-1);
        Mockito.when(inputStream.read(Mockito.any())).thenReturn((int) '\n',-1);
        Mockito.when(inputStream.read(Mockito.any(byte[].class))).thenReturn(-1);
        Mockito.when(inputStream.read(Mockito.any(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(-1);
        messageHandler.mMessages.put("Key3","Message3");
        messageHandler.addMessages(basename, dir);

        Assert.assertEquals("Message1", messageHandler.mMessages.get("Key1"));
        Assert.assertEquals("Message2", messageHandler.mMessages.get("Key2"));
        Assert.assertEquals("Message3", messageHandler.mMessages.get("Key3"));
    }
}