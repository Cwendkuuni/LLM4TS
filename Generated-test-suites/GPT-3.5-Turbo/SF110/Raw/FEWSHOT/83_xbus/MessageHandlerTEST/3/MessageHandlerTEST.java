package net.sf.xbus.base.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import net.sf.xbus.base.core.config.Constants;
import net.sf.xbus.base.core.trace.Trace;
import net.sf.xbus.base.core.strings.XStringSupport;

public class MessageHandlerTest {
    
    private MessageHandler messageHandler;
    
    @Before
    public void setup() {
        messageHandler = spy(new MessageHandler("basename"));
        messageHandler.mInstances = new Hashtable<>();
    }
    
    @Test
    public void testGetMessage() {
        String key = "key";
        List<String> params = any(List.class);
        
        doReturn(null).when(messageHandler).getMessageOptional(key, params);
        doNothing().when(Trace.class);
        Trace.error(anyString());
        
        assertEquals("Key: key not found in message file", messageHandler.getMessage(key, params));
        verify(messageHandler).getMessageOptional(key, params);
        verify(Trace.class).error(anyString());
    }

    @Test
    public void testGetMessageOptional() {
        String key = "key";
        List<String> params = any(List.class);
        
        messageHandler.mMessages = null;
        assertNull(messageHandler.getMessageOptional(key, params));
        
        messageHandler.mMessages = new Hashtable<>();
        messageHandler.mMessages.put(key, "message");
        List<String> testParams = List.of("param1", "param2");
        assertEquals("message", messageHandler.getMessageOptional(key, testParams));
    }

    @Test
    public void testGetInstance() {
        String basename = "basename";
        
        MessageHandler instance = mock(MessageHandler.class);
        
        when(MessageHandler.getInstance(basename)).thenCallRealMethod();
        when(MessageHandler.mInstances.get(basename)).thenReturn(instance);
        assertEquals(instance, MessageHandler.getInstance(basename));
        
        verify(MessageHandler.classLock);
        verify(MessageHandler.class);
    }

    @Test
    public void testMessageHandler() {
        String basename = "basename";
        ClassLock classLock = mock(ClassLock.class);
        
        MessageHandler instance = mock(MessageHandler.class);
        Hashtable instances = mock(Hashtable.class);
        
        when(MessageHandler.classLock).thenReturn(classLock);
        doReturn(instance).when(messageHandler).new MessageHandler(basename);
        when(messageHandler.mInstances).thenReturn(instances);
        when(instances.put(basename, instance)).thenReturn(null);
        
        MessageHandler handler = MessageHandler.getInstance(basename);
        assertEquals(instances.get(basename), handler);
        
        verify(MessageHandler.classLock);
        verify(MessageHandler.class);
        verify(messageHandler).new MessageHandler(basename);
        verify(instances).put(basename, instance);
    }
    
    @Test
    public void testAddMessages() throws FileNotFoundException, IOException {
        String basename = "basename";
        String dir = "dir";
        Locale locale = Locale.ENGLISH;
        String postfix = "_" + locale.toString() + ".properties";
        File dirFile = mock(File.class);
        String[] messagesFiles = new String[1];
        Properties newProps = mock(Properties.class);
        FileInputStream instream = mock(FileInputStream.class);
        Enumeration keys = mock(Enumeration.class);
        
        when(dirFile.list(any(MessageHandler.MessagesFilter.class))).thenReturn(messagesFiles);
        doReturn(newProps).when(messageHandler).new Properties();
        when(messageHandler.new FileInputStream(dir + messagesFiles[0])).thenReturn(instream);
        when(newProps.keys()).thenReturn(keys);
        when(keys.hasMoreElements()).thenReturn(true, false);
        when(keys.nextElement()).thenReturn("key1");
        when(newProps.get("key1")).thenReturn("value1");
        
        messageHandler.addMessages(basename, dir);
        
        verify(dirFile).list(any(MessageHandler.MessagesFilter.class));
        verify(newProps).load(instream);
    }
    
    @Test(expected = FileNotFoundException.class)
    public void testAddMessagesFileNotFoundException() throws IOException {
        String basename = "basename";
        String dir = "dir";
        Locale locale = Locale.ENGLISH;
        String postfix = "_" + locale.toString() + ".properties";
        File dirFile = mock(File.class);
        String[] messagesFiles = new String[1];
        
        when(dirFile.list(any(MessageHandler.MessagesFilter.class))).thenReturn(messagesFiles);
        doThrow(new FileNotFoundException()).when(messageHandler).new FileInputStream(dir + messagesFiles[0]);
        
        messageHandler.addMessages(basename, dir);
        
        verify(dirFile).list(any(MessageHandler.MessagesFilter.class));
    }

    @Test(expected = IOException.class)
    public void testAddMessagesIOException() throws IOException {
        String basename = "basename";
        String dir = "dir";
        Locale locale = Locale.ENGLISH;
        String postfix = "_" + locale.toString() + ".properties";
        File dirFile = mock(File.class);
        String[] messagesFiles = new String[1];
        Properties newProps = mock(Properties.class);
        FileInputStream instream = mock(FileInputStream.class);
        
        when(dirFile.list(any(MessageHandler.MessagesFilter.class))).thenReturn(messagesFiles);
        doReturn(newProps).when(messageHandler).new Properties();
        when(messageHandler.new FileInputStream(dir + messagesFiles[0])).thenReturn(instream);
        doThrow(new IOException()).when(newProps).load(instream);
        
        messageHandler.addMessages(basename, dir);
        
        verify(dirFile).list(any(MessageHandler.MessagesFilter.class));
        verify(newProps).load(instream);
    }
    
    @Test
    public void testMessagesFilter() {
        String prefix = "prefix";
        String postfix = "postfix";
        File dir = mock(File.class);
        String filename = "filename";
        MessageHandler.MessagesFilter filter = new MessageHandler.MessagesFilter(prefix, postfix);
        
        when(filename.startsWith(prefix)).thenReturn(true);
        when(filename.endsWith(postfix)).thenReturn(true);
        
        assertEquals(true, filter.accept(dir, filename));
        
        verify(filename).startsWith(prefix);
        verify(filename).endsWith(postfix);
    }
}