
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import net.sf.xbus.base.core.MessageHandler;
import net.sf.xbus.base.core.strings.XStringSupport;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class MessageHandlerTest {
    private MessageHandler messageHandler;

    @Before
    public void setUp() {
        messageHandler = mock(MessageHandler.class);
    }

    @Test
    public void testGetMessage_KeyExists_ReturnsMessageText() {
        // Arrange
        String expectedMessage = "Test message text";
        String key = "testKey";
        List<String> params = new ArrayList<>();

        when(messageHandler.getMessageOptional(key, params)).thenReturn(expectedMessage);

        // Act
        String actualMessage = messageHandler.getMessage(key, params);

        // Assert
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testGetMessage_KeyNotExists_ReturnsErrorMessage() {
        // Arrange
        String key = "nonExistingKey";
        List<String> params = new ArrayList<>();

        when(messageHandler.getMessageOptional(key, params)).thenReturn(null);

        // Act
        String actualMessage = messageHandler.getMessage(key, params);

        // Assert
        assertEquals("Key: " + key + " not found in message file", actualMessage);
    }

    @Test
    public void testGetMessageOptional_MessagesNotInitialized_ReturnsNull() {
        // Arrange
        String key = "testKey";
        List<String> params = new ArrayList<>();

        when(messageHandler.mMessages).thenReturn(null);

        // Act
        String actualMessage = messageHandler.getMessageOptional(key, params);

        // Assert
        assertNull(actualMessage);
    }

    @Test
    public void testGetMessageOptional_MessageFoundWithParams_ReplacesParamsInMessageText() {
        // Arrange
        String expectedMessage = "Hello, John";
        String key = "testKey";
        List<String> params = new ArrayList<>();
        params.add("John");

        Map<String, String> messages = new HashMap<>();
        messages.put(key, "Hello, $1$");

        when(messageHandler.mMessages).thenReturn(messages);

        // Act
        String actualMessage = messageHandler.getMessageOptional(key, params);

        // Assert
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testGetMessageOptional_MessageFoundWithNullParam_ReplacesNullParamInMessageText() {
        // Arrange
        String expectedMessage = "Hello, <null>";
        String key = "testKey";
        List<String> params = new ArrayList<>();
        params.add(null);

        Map<String, String> messages = new HashMap<>();
        messages.put(key, "Hello, $1$");

        when(messageHandler.mMessages).thenReturn(messages);

        // Act
        String actualMessage = messageHandler.getMessageOptional(key, params);

        // Assert
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testGetMessageOptional_MessageNotFound_ReturnsNull() {
        // Arrange
        String key = "nonExistingKey";
        List<String> params = new ArrayList<>();

        Map<String, String> messages = new HashMap<>();

        when(messageHandler.mMessages).thenReturn(messages);

        // Act
        String actualMessage = messageHandler.getMessageOptional(key, params);

        // Assert
        assertNull(actualMessage);
    }

    @Test
    public void testGetInstance_ExistingInstance_ReturnsInstance() {
        // Arrange
        String basename = "testBasename";

        Hashtable<String, MessageHandler> instances = new Hashtable<>();
        instances.put(basename, messageHandler);

        Field mInstancesField = MessageHandler.class.getDeclaredField("mInstances");
        mInstancesField.setAccessible(true);
        mInstancesField.set(null, instances);

        // Act
        MessageHandler actualInstance = MessageHandler.getInstance(basename);

        // Assert
        assertEquals(messageHandler, actualInstance);
    }

    @Test
    public void testGetInstance_NonExistingInstance_CreatesNewInstance() {
        // Arrange
        String basename = "testBasename";

        Hashtable<String, MessageHandler> instances = new Hashtable<>();

        Field mInstancesField = MessageHandler.class.getDeclaredField("mInstances");
        mInstancesField.setAccessible(true);
        mInstancesField.set(null, instances);

        // Act
        MessageHandler actualInstance = MessageHandler.getInstance(basename);

        // Assert
        assertNotNull(actualInstance);
        assertEquals(messageHandler, instances.get(basename));
    }

    @Test
    public void testAddMessages_MessagesFilesExist_LoadsMessages() throws FileNotFoundException, IOException {
        // Arrange
        String basename = "testBasename";
        String dir = "testDir";

        Locale locale = Locale.ENGLISH;
        String postfix = "_" + locale.toString() + ".properties";

        File dirFile = mock(File.class);
        when(dirFile.list(any(FilenameFilter.class))).thenReturn(new String[]{"testFile.properties"});

        Properties newProps = mock(Properties.class);
        when(newProps.keys()).thenReturn(Collections.enumeration(new ArrayList<String>(){{add("testKey");}}));
        when(newProps.get(anyString())).thenReturn("testValue");

        FileInputStream instream = mock(FileInputStream.class);
        whenNew(FileInputStream.class).withArguments(anyString()).thenReturn(instream);
        doNothing().when(newProps).load(instream);
        doNothing().when(instream).close();

        whenNew(File.class).withArguments(dir).thenReturn(dirFile);

        Field mMessagesField = MessageHandler.class.getDeclaredField("mMessages");
        mMessagesField.setAccessible(true);
        mMessagesField.set(messageHandler, new HashMap<>());

        // Act
        messageHandler.addMessages(basename, dir);

        // Assert
        assertEquals("testValue", messageHandler.mMessages.get("testKey"));
    }

    @Test(expected = FileNotFoundException.class)
    public void testAddMessages_MessagesFilesNotExist_ThrowsFileNotFoundException() throws FileNotFoundException, IOException {
        // Arrange
        String basename = "testBasename";
        String dir = "testDir";

        File dirFile = mock(File.class);
        when(dirFile.list(any(FilenameFilter.class))).thenReturn(null);

        whenNew(File.class).withArguments(dir).thenReturn(dirFile);

        // Act
        messageHandler.addMessages(basename, dir);
    }

    @Test(expected = IOException.class)
    public void testAddMessages_FailedToLoadMessages_ThrowsIOException() throws FileNotFoundException, IOException {
        // Arrange
        String basename = "testBasename";
        String dir = "testDir";

        File dirFile = mock(File.class);
        when(dirFile.list(any(FilenameFilter.class))).thenReturn(new String[]{"testFile.properties"});

        FileInputStream instream = mock(FileInputStream.class);
        whenNew(FileInputStream.class).withArguments(anyString()).thenReturn(instream);
        doThrow(new IOException()).when(instream).close();

        whenNew(File.class).withArguments(dir).thenReturn(dirFile);

        // Act
        messageHandler.addMessages(basename, dir);
    }

    @Test
    public void testMessagesFilter_FileNameMatches_ReturnsTrue() {
        // Arrange
        String prefix = "testPrefix";
        String postfix = "testPostfix";

        File dir = mock(File.class);
        String filename = "testPrefix_something_testPostfix";

        MessageHandler.MessagesFilter filter = new MessageHandler.MessagesFilter(prefix, postfix);

        // Act
        boolean result = filter.accept(dir, filename);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testMessagesFilter_FileNameDoesNotMatch_ReturnsFalse() {
        // Arrange
        String prefix = "testPrefix";
        String postfix = "testPostfix";

        File dir = mock(File.class);
        String filename = "somethingElse";

        MessageHandler.MessagesFilter filter = new MessageHandler.MessagesFilter(prefix, postfix);

        // Act
        boolean result = filter.accept(dir, filename);

        // Assert
        assertFalse(result);
    }
}
