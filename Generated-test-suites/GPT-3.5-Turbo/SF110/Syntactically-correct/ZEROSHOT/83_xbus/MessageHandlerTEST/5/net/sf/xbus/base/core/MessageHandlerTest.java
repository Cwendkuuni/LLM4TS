
package net.sf.xbus.base.core;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class MessageHandlerTest {
    
    private MessageHandler messageHandler;
    
    @Mock
    private Hashtable<String, String> messagesMock;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        messageHandler = new MessageHandler();
    }
    
    @Test
    public void testGetMessage_shouldReturnMessageTextWhenKeyExists() {
        String key = "testKey";
        List<String> params = new ArrayList<>();
        
        when(messagesMock.get(key)).thenReturn("Test Message");
        messageHandler.setMessages(messagesMock);
        
        String actual = messageHandler.getMessage(key, params);
        
        assertEquals("Test Message", actual);
    }
    
    @Test
    public void testGetMessage_shouldReturnErrorMessageWhenKeyNotFound() {
        String key = "testKey";
        List<String> params = new ArrayList<>();
        
        when(messagesMock.get(key)).thenReturn(null);
        messageHandler.setMessages(messagesMock);
        
        String actual = messageHandler.getMessage(key, params);
        
        assertEquals("Key: testKey not found in message file", actual);
    }
    
    @Test
    public void testGetMessageOptional_shouldReturnMessageTextWhenKeyExists() {
        String key = "testKey";
        List<String> params = new ArrayList<>();
        
        when(messagesMock.get(key)).thenReturn("Test Message");
        messageHandler.setMessages(messagesMock);
        
        String actual = messageHandler.getMessageOptional(key, params);
        
        assertEquals("Test Message", actual);
    }
    
    @Test
    public void testGetMessageOptional_shouldReturnNullWhenKeyNotFound() {
        String key = "testKey";
        List<String> params = new ArrayList<>();
        
        when(messagesMock.get(key)).thenReturn(null);
        messageHandler.setMessages(messagesMock);
        
        String actual = messageHandler.getMessageOptional(key, params);
        
        assertEquals(null, actual);
    }
    
    @Test
    public void testGetMessageOptional_shouldReturnMessageTextWithParams() {
        String key = "testKey";
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");
        
        when(messagesMock.get(key)).thenReturn("Test Message: $1$, $2$");
        messageHandler.setMessages(messagesMock);
        
        String actual = messageHandler.getMessageOptional(key, params);
        
        assertEquals("Test Message: param1, param2", actual);
    }
    
    @Test
    public void testGetMessageOptional_shouldReturnMessageTextWithoutNullParams() {
        String key = "testKey";
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add(null);
        params.add("param3");
        
        when(messagesMock.get(key)).thenReturn("Test Message: $1$, $2$, $3$");
        messageHandler.setMessages(messagesMock);
        
        String actual = messageHandler.getMessageOptional(key, params);
        
        assertEquals("Test Message: param1, <null>, param3", actual);
    }
    
    @Test
    public void testGetInstance_shouldReturnExistingInstanceWhenBasenameExists() {
        String basename = "testBasename";
        
        when(messageHandler.getMessages()).thenReturn(messagesMock);
        when(messagesMock.get(basename)).thenReturn(new MessageHandler());
        MessageHandler existingInstance = messageHandler.getInstance(basename);
        
        MessageHandler newInstance = messageHandler.getInstance(basename);
        
        assertEquals(existingInstance, newInstance);
    }
    
    @Test
    public void testGetInstance_shouldCreateAndReturnNewInstanceWhenBasenameDoesNotExist() {
        String basename = "testBasename";
        
        when(messageHandler.getMessages()).thenReturn(messagesMock);
        when(messagesMock.get(basename)).thenReturn(null);
        
        MessageHandler newInstance = messageHandler.getInstance(basename);
        
        assertEquals(MessageHandler.class, newInstance.getClass());
    }
    
    @Test
    public void testAddMessages_shouldAddNewPropsToMessagesWhenFilesExist() {
        String basename = "testBasename";
        String dir = "testDir";
        String[] messageFiles = {"testFile1.properties", "testFile2.properties"};
        
        when(messageHandler.getMessages()).thenReturn(messagesMock);
        when(messagesMock.put("testKey1", "Test Message 1")).thenReturn(null);
        when(messagesMock.put("testKey2", "Test Message 2")).thenReturn(null);
        when(messagesMock.put("testKey3", "Test Message 3")).thenReturn(null);
        when(messagesMock.put("testKey4", "Test Message 4")).thenReturn(null);
        
        messageHandler.addMessages(basename, dir, messageFiles);
        
        assertEquals(4, messagesMock.size());
    }
    
    @Test
    public void testAddMessages_shouldNotAddNewPropsToMessagesWhenFilesDoNotExist() {
        String basename = "testBasename";
        String dir = "testDir";
        String[] messageFiles = null;
        
        when(messageHandler.getMessages()).thenReturn(messagesMock);
        
        messageHandler.addMessages(basename, dir, messageFiles);
        
        assertEquals(0, messagesMock.size());
    }
    
    @Test
    public void testMessagesFilter_shouldAcceptFileWithMatchingPrefixAndPostfix() {
        String prefix = "testPrefix";
        String postfix = "testPostfix";
        
        MessageHandler.MessagesFilter messagesFilter = new MessageHandler.MessagesFilter(prefix, postfix);
        
        boolean actual = messagesFilter.accept(new File("testDir"), "testPrefix_testPostfix.properties");
        
        assertEquals(true, actual);
    }
    
    @Test
    public void testMessagesFilter_shouldNotAcceptFileWithMismatchedPrefixAndPostfix() {
        String prefix = "testPrefix";
        String postfix = "testPostfix";
        
        MessageHandler.MessagesFilter messagesFilter = new MessageHandler.MessagesFilter(prefix, postfix);
        
        boolean actual = messagesFilter.accept(new File("testDir"), "mismatchedPrefix_mismatchedPostfix.properties");
        
        assertEquals(false, actual);
    }
}
