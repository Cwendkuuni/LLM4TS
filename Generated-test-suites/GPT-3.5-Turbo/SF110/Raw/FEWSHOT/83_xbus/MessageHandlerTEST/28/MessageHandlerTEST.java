package net.sf.xbus.base.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;


import net.sf.xbus.base.core.trace.Trace;
import net.sf.xbus.base.core.strings.XStringSupport;
import net.sf.xbus.base.core.config.Constants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

public class MessageHandlerTest {

    @Mock
    private Trace traceMock;
    @Mock
    private Hashtable<String, String> mInstances;
    @Mock
    private Hashtable<String, String> mMessages;
    @Mock
    private List<Object> params;

    private MessageHandler messageHandler;

    @Before
    public void setUp() {
        messageHandler = new MessageHandler();
        Trace.traceMock = traceMock;
        MessageHandler.mInstances = mInstances;
        messageHandler.mMessages = mMessages;
    }
    
    @Test
    public void testGetMessage() {
        String key = "foo";
        List<Object> params = Mockito.mock(List.class);
        when(mMessages.get(key)).thenReturn("test message");

        String result = messageHandler.getMessage(key, params);

        assertEquals("test message", result);
        verify(traceMock, times(0)).error(anyString());
    }

    @Test
    public void testGetMessageOptional() {
        String key = "foo";
        List<Object> params = Mockito.mock(List.class);
        when(mMessages.get(key)).thenReturn("test message");

        String result = messageHandler.getMessageOptional(key, params);

        assertEquals("test message", result);
    }

    @Test
    public void testGetMessageOptionalWithNullParams() {
        String key = "foo";
        List<Object> params = null;

        when(mMessages.get(key)).thenReturn("test message");

        String result = messageHandler.getMessageOptional(key, params);

        assertEquals("test message", result);
    }

    @Test
    public void testGetMessageOptionalWithNullMessages() {
        String key = "foo";
        List<Object> params = Mockito.mock(List.class);

        String result = messageHandler.getMessageOptional(key, params);

        assertNull(result);
    }

    @Test
    public void testGetInstance() {
        String basename = "test";

        MessageHandler result = messageHandler.getInstance(basename);

        assertNotNull(result);
        verify(mInstances, times(1)).get(basename);
        verify(mInstances, times(1)).put(basename, result);
    }

    @Test
    public void testGetInstanceWithExistingInstance() {
        String basename = "test";
        MessageHandler instance = Mockito.mock(MessageHandler.class);

        when(mInstances.get(basename)).thenReturn(instance);

        MessageHandler result = messageHandler.getInstance(basename);

        assertEquals(instance, result);
        verify(mInstances, times(1)).get(basename);
        verify(mInstances, times(0)).put(basename, result);
    }

    @Test
    public void testAddMessages() throws FileNotFoundException, IOException {
        String basename = "test";
        String dir = "dir/";
        Locale locale = Locale.ENGLISH;
        String postfix = "_" + locale.toString() + ".properties";
        File dirFile = Mockito.mock(File.class);
        String[] messagesFiles = new String[] {"file1.properties"};
        Properties newProps = Mockito.mock(Properties.class);
        FileInputStream instream = Mockito.mock(FileInputStream.class);
        Enumeration keys = Mockito.mock(Enumeration.class);

        when(dirFile.list(any(FilenameFilter.class))).thenReturn(messagesFiles);
        whenNew(File.class).withArguments(dir).thenReturn(dirFile);
        whenNew(Properties.class).withNoArguments().thenReturn(newProps);
        when(instream.read(any(byte[].class))).thenReturn(-1);
        whenNew(FileInputStream.class).withArguments(anyString()).thenReturn(instream);
        when(newProps.keys()).thenReturn(keys);
        when(keys.hasMoreElements()).thenReturn(true, false);
        when(keys.nextElement()).thenReturn("key");
        when(newProps.get(any(String.class))).thenReturn("message");

        messageHandler.addMessages(basename, dir);

        verify(dirFile, times(1)).list(any(FilenameFilter.class));
        verifyNew(File.class).withArguments(dir);
        verifyNew(Properties.class).withNoArguments();
        verify(instream, times(1)).read(any(byte[].class));
        verifyNew(FileInputStream.class).withArguments(anyString());
        verify(newProps, times(1)).keys();
        verify(keys, times(2)).hasMoreElements();
        verify(keys, times(1)).nextElement();
        verify(newProps, times(1)).get(any(String.class));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddMessagesWithFileNotFoundException() throws FileNotFoundException, IOException {
        String basename = "test";
        String dir = "dir/";
        File dirFile = Mockito.mock(File.class);

        when(dirFile.list(any(FilenameFilter.class))).thenReturn(null);
        whenNew(File.class).withArguments(dir).thenReturn(dirFile);

        messageHandler.addMessages(basename, dir);

        verify(dirFile, times(1)).list(any(FilenameFilter.class));
        verifyNew(File.class).withArguments(dir);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddMessagesWithIOException() throws FileNotFoundException, IOException {
        String basename = "test";
        String dir = "dir/";
        File dirFile = Mockito.mock(File.class);

        when(dirFile.list(any(FilenameFilter.class))).thenReturn(new String[] {"file1.properties"});
        whenNew(File.class).withArguments(dir).thenReturn(dirFile);
        whenNew(FileInputStream.class).withArguments(anyString()).thenThrow(new IOException());
        
        messageHandler.addMessages(basename, dir);

        verify(dirFile, times(1)).list(any(FilenameFilter.class));
        verifyNew(File.class).withArguments(dir);
        verifyNew(FileInputStream.class).withArguments(anyString());
    }
}