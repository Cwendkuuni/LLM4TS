package net.sf.xbus.base.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

public class MessageHandlerTest {
    
    private MessageHandler messageHandler;
    
    @Before
    public void setup() {
        messageHandler = spy(MessageHandler.class);
    }

    @Test
    public void testGetMessage() {
        List<String> params = new ArrayList<>();
        params.add("value1");
        params.add("value2");
        
        doReturn("MessageText").when(messageHandler).getMessageOptional(anyString(), any(List.class));
        
        assertEquals("MessageText", messageHandler.getMessage("key", params));
        verify(messageHandler).getMessageOptional("key", params);
    }
    
    @Test
    public void testGetMessageOptional() {
        List<String> params = new ArrayList<>();
        params.add("value1");
        params.add("value2");
        
        messageHandler.mMessages = new Hashtable();
        messageHandler.mMessages.put("key", "MessageText");
        
        assertEquals("MessageText", messageHandler.getMessageOptional("key", params));
        assertNull(messageHandler.getMessageOptional("wrongKey", params));
    }
    
    @Test
    public void testGetMessageOptional_NullParams() {
        List<String> params = null;
        
        messageHandler.mMessages = new Hashtable();
        messageHandler.mMessages.put("key", "MessageText");
        
        assertEquals("MessageText", messageHandler.getMessageOptional("key", params));
        assertNull(messageHandler.getMessageOptional("wrongKey", params));
    }
    
    @Test
    public void testGetInstance() {
        String basename = "basename";
        
        doReturn(null).when(messageHandler).addMessages(anyString(), anyString());
        
        MessageHandler instance1 = MessageHandler.getInstance(basename);
        MessageHandler instance2 = MessageHandler.getInstance(basename);
        
        verify(messageHandler, times(2)).addMessages(basename, Constants.XBUS_ETC);
        verify(messageHandler, times(2)).addMessages(basename, Constants.XBUS_PLUGIN_ETC);
        
        assertEquals(instance1, instance2);
    }
    
    @Test
    public void testAddMessages() throws FileNotFoundException, IOException {
        String basename = "basename";
        String dir = "dir/";
        
        String postfix = "_" + Locale.ENGLISH.toString() + ".properties";
        
        String[] messagesFiles = {"messagefile.properties"};
        
        File dirFile = mock(File.class);
        doReturn(messagesFiles).when(dirFile).list(any(MessageHandler.MessagesFilter.class));
        
        Properties newProps = mock(Properties.class);
        FileInputStream instream = mock(FileInputStream.class);
        doNothing().when(newProps).load(instream);
        doReturn(instream).when(messageHandler).createFileInputStream(anyString());
        
        Hashtable mMessages = mock(Hashtable.class);
        
        doReturn(newProps).when(messageHandler, "createPropertiesInstance");
        doReturn(keyIterator()).when(newProps).keys();
        doReturn("value").when(newProps).get(anyString());
        doReturn(mMessages).when(messageHandler).getMessagesInstance();
        
        messageHandler.addMessages(basename, dir);
        
        verify(messageHandler, times(1)).createPropertiesInstance();
        verify(messageHandler, times(1)).createFileInputStream(anyString());
    }
    
    private Iterator keyIterator() {
        Iterator<String> iterator = mock(Iterator.class);
        when(iterator.hasNext()).thenReturn(true, false);
        when(iterator.next()).thenReturn("key");
        return iterator;
    }
}