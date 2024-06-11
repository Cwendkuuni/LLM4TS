package net.sf.xbus.base.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import net.sf.xbus.base.core.config.Configuration;
import net.sf.xbus.base.core.strings.XStringSupport;
import net.sf.xbus.base.core.trace.Trace;

public class MessageHandlerTest {

    private MessageHandler messageHandler;
    private Configuration configurationMock;
    private Hashtable<String, Object> mInstancesMock;
    private Hashtable<String, Object> mMessagesMock;
    private File dirFileMock;
    private FileInputStream instreamMock;
    private MessagesFilter messagesFilterMock;

    private String basename;
    private String key;
    private List<Object> params;

    @Before
    public void setup() throws FileNotFoundException, IOException {
        messageHandler = new MessageHandler("");
        
        configurationMock = mock(Configuration.class);
        mInstancesMock = new Hashtable<>();
        mInstancesMock.put("", new Hashtable<>());
        
        mMessagesMock = mock(Hashtable.class);
        messageHandler.mMessages = mMessagesMock;
        
        dirFileMock = mock(File.class);
        instreamMock = mock(FileInputStream.class);
        messagesFilterMock = mock(MessagesFilter.class);
        
        basename = "basename";
        key = "key";
        params = new ArrayList<Object>(Arrays.asList("param1", "param2"));
    }
    
    @Test
    public void testGetMessage() {
        when(mMessagesMock.get(key)).thenReturn("testMessage");
        String message = messageHandler.getMessage(key, params);
        assertEquals("testMessage", message);
        verify(mMessagesMock, times(1)).get(key);
    }

    @Test
    public void testGetMessageOptional() {
        when(mMessagesMock.get(key)).thenReturn("testMessage");
        String message = messageHandler.getMessageOptional(key, params);
        assertEquals("testMessage", message);
        verify(mMessagesMock, times(1)).get(key);
    }

    @Test
    public void testGetMessageOptional_nullParams() {
        when(mMessagesMock.get(key)).thenReturn("testMessage");
        String message = messageHandler.getMessageOptional(key, null);
        assertEquals("testMessage", message);
        verify(mMessagesMock, times(1)).get(key);
    }

    @Test
    public void testGetInstance() {
        messageHandler.mInstances = mInstancesMock;
        when(mInstancesMock.get(basename)).thenReturn(null);
        
        MessageHandler instance = MessageHandler.getInstance(basename);
        
        assertEquals(instance, mInstancesMock.get(basename));
        verify(mInstancesMock, times(1)).get(basename);
        verify(mInstancesMock, times(1)).put(basename, instance);
    }

    @Test
    public void testGetInstance_existing() {
        messageHandler.mInstances = mInstancesMock;
        MessageHandler instanceMock = mock(MessageHandler.class);
        when(mInstancesMock.get(basename)).thenReturn(instanceMock);
        
        MessageHandler instance = MessageHandler.getInstance(basename);
        
        assertEquals(instanceMock, instance);
        verify(mInstancesMock, times(1)).get(basename);
        verify(mInstancesMock, times(0)).put(basename, instance);
    }

    @Test
    public void testConstructor() throws IOException {
        String dir = "dir/";
        String[] messagesFiles = new String[]{"file1.properties", "file2.properties"};

        when(configurationMock.getXBUS_ETC()).thenReturn(dir);
        when(configurationMock.getXBUS_PLUGIN_ETC()).thenReturn(dir);
        when(configurationMock.getLocale()).thenReturn(Locale.ENGLISH);
        when(dirFileMock.list(messagesFilterMock)).thenReturn(messagesFiles);
        when(messagesFilterMock.accept(dirFileMock, messagesFiles[0])).thenReturn(true);
        when(messagesFilterMock.accept(dirFileMock, messagesFiles[1])).thenReturn(true);
        when(instreamMock.read()).thenReturn(0);
        when(instreamMock.read(any(byte[].class))).thenReturn(0);
        when(instreamMock.read(any(byte[].class), anyInt(), anyInt())).thenReturn(0);
        
        List<String> messageKeys = new ArrayList<>();
        messageKeys.add("key1");
        messageKeys.add("key2");
        
        Properties newProps = new Properties();
        newProps.put("key1", "value1");
        newProps.put("key2", "value2");
        
        Hashtable<String, Object> propsMock = mock(Hashtable.class);
        when(propsMock.keys()).thenReturn(messageKeys.elements());
        when(newProps.get("key1")).thenReturn("value1");
        when(newProps.get("key2")).thenReturn("value2");
        
        when(mMessagesMock.put(anyString(), any())).thenReturn(null);
        
        messageHandler = new MessageHandler(basename);
        
        verify(configurationMock, times(2)).getXBUS_ETC();
        verify(configurationMock, times(2)).getXBUS_PLUGIN_ETC();
        verify(configurationMock, times(1)).getLocale();
        verify(dirFileMock, times(2)).list(messagesFilterMock);
        verify(messagesFilterMock, times(2)).accept(dirFileMock, messagesFiles[0]);
        verify(messagesFilterMock, times(2)).accept(dirFileMock, messagesFiles[1]);
        verify(instreamMock, times(2)).close();
        
        verify(mMessagesMock, times(1)).put(eq("key1"), eq("value1"));
        verify(mMessagesMock, times(1)).put(eq("key2"), eq("value2"));
        
        verify(propsMock, times(2)).keys();
        verify(newProps, times(2)).get(anyString());
    }

    @Test
    public void testConstructor_exception() throws IOException {
        String dir = "dir/";
        String[] messagesFiles = new String[]{"file.properties"};

        when(configurationMock.getXBUS_ETC()).thenReturn(dir);
        when(configurationMock.getLocale()).thenReturn(Locale.ENGLISH);
        when(dirFileMock.list(messagesFilterMock)).thenReturn(messagesFiles);
        when(messagesFilterMock.accept(dirFileMock, messagesFiles[0])).thenReturn(true);
        when(instreamMock.close()).thenThrow(new IOException());
        
        messageHandler = new MessageHandler(basename);
        
        verify(configurationMock, times(1)).getXBUS_ETC();
        verify(configurationMock, times(1)).getLocale();
        verify(dirFileMock, times(1)).list(messagesFilterMock);
        verify(messagesFilterMock, times(1)).accept(dirFileMock, messagesFiles[0]);
        verify(instreamMock, times(1)).close();
        
        verify(mMessagesMock, times(1)).put(anyString(), any());
    }

    @Test
    public void testConstructor_exception2() throws IOException {
        String dir = "dir/";
        String[] messagesFiles = new String[]{"file.properties"};

        when(configurationMock.getXBUS_ETC()).thenReturn(dir);
        when(configurationMock.getLocale()).thenReturn(Locale.ENGLISH);
        when(dirFileMock.list(messagesFilterMock)).thenReturn(messagesFiles);
        when(messagesFilterMock.accept(dirFileMock, messagesFiles[0])).thenReturn(true);
        when(messagesFilterMock.accept(dirFileMock, messagesFiles[1])).thenReturn(true);
        when(instreamMock.close()).thenReturn(null);
        
        messageHandler = new MessageHandler(basename);
        
        verify(configurationMock, times(1)).getXBUS_ETC();
        verify(configurationMock, times(1)).getLocale();
        verify(dirFileMock, times(1)).list(messagesFilterMock);
        verify(messagesFilterMock, times(1)).accept(dirFileMock, messagesFiles[0]);
        verify(instreamMock, times(1)).close();
        
        verify(mMessagesMock, times(0)).put(anyString(), any());
    }

    @Test
    public void testMessagesFilter() {
        String prefix = "prefix";
        String postfix = "postfix";
        String filename = "filename";
        
        MessagesFilter messagesFilter = new MessagesFilter(prefix, postfix);
        
        boolean accept = messagesFilter.accept(dirFileMock, filename);
        assertEquals(true, accept);
    }
    
    @Test
    public void testMessagesFilter_notAccept() {
        String prefix = "prefix";
        String postfix = "postfix";
        String filename = "file";
        
        MessagesFilter messagesFilter = new MessagesFilter(prefix, postfix);
        
        boolean accept = messagesFilter.accept(dirFileMock, filename);
        assertEquals(false, accept);
    }
}