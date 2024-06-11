package net.sf.xbus.base.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import net.sf.xbus.base.core.trace.Trace;
import net.sf.xbus.base.core.strings.XStringSupport;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class MessageHandlerTest {

    private MessageHandler messageHandlerMock;

    @Before
    public void setUp() {
        messageHandlerMock = spy(MessageHandler.class);
    }

    @Test
    public void testGetMessage() {
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");

        doReturn(null).when(messageHandlerMock).getMessageOptional(any(String.class), any(List.class));

        String expected = "Key: key not found in message file";
        String actual = messageHandlerMock.getMessage("key", params);
        assertEquals(expected, actual);

        verify(messageHandlerMock, times(1)).getMessageOptional(eq("key"), eq(params));
        verify(Trace, times(1)).error(expected);
    }

    @Test
    public void testGetMessageOptional() {
        doReturn(null).when(messageHandlerMock).getMessages();

        String actual = messageHandlerMock.getMessageOptional("key", null);
        assertNull(actual);

        actual = messageHandlerMock.getMessageOptional("key", new ArrayList<>());
        assertNull(actual);

        verify(messageHandlerMock, times(2)).getMessages();
        verify(XStringSupport, never()).replaceAll(anyString(), anyString(), anyString());
    }

    @Test
    public void testGetMessageOptionalWithParams() {
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");

        Hashtable<String, Object> messagesMock = new Hashtable<>();
        messagesMock.put("key", "Message $1$ with $2$ params");
        doReturn(messagesMock).when(messageHandlerMock).getMessages();

        String expected = "Message param1 with param2 params";
        String actual = messageHandlerMock.getMessageOptional("key", params);
        assertEquals(expected, actual);

        verify(messageHandlerMock, times(1)).getMessages();
        verify(XStringSupport, times(1)).replaceAll(eq("Message $1$ with $2$ params"), eq("$1$"), eq("param1"));
        verify(XStringSupport, times(1)).replaceAll(eq("Message param1 with $2$ params"), eq("$2$"), eq("param2"));
    }

    @Test
    public void testGetInstance() {
        MessageHandler messageHandlerMock = mock(MessageHandler.class);

        doNothing().when(MessageHandler.classLock);
        doReturn(messageHandlerMock).when(MessageHandler.mInstances).get(any(String.class));
        doNothing().when(MessageHandler.mInstances).put(any(String.class), any(MessageHandler.class));

        MessageHandler actual = MessageHandler.getInstance("basename");
        assertEquals(messageHandlerMock, actual);

        verify(messageHandlerMock, never()).addMessages(any(String.class), any(String.class));
    }

    @Test
    public void testAddMessages() throws FileNotFoundException, IOException {
        String basename = "basename";
        String dir = "dir";
        Locale locale = Locale.ENGLISH;
        String postfix = "_" + locale.toString() + ".properties";

        File dirFileMock = mock(File.class);
        when(dirFileMock.list(any(MessageHandler.MessagesFilter.class))).thenReturn(new String[] {"file1.properties"});

        Properties propertiesMock = mock(Properties.class);
        when(propertiesMock.keys()).thenReturn(Collections.enumeration(Arrays.asList("key1", "key2")));
        when(propertiesMock.get(any(String.class))).thenReturn("message");

        FileInputStream fileInputStreamMock = mock(FileInputStream.class);
        doReturn(fileInputStreamMock).when(messageHandlerMock).getFileInputStream(any(String.class));
        doNothing().when(propertiesMock).load(any(FileInputStream.class));

        Hashtable<String, Object> messagesMock = spy(Hashtable.class);
        doReturn(null).when(messagesMock).put(any(String.class), any(Object.class));
        doReturn(messagesMock).when(messageHandlerMock).getMessages();

        messageHandlerMock.addMessages(basename, dir);

        verify(messageHandlerMock, times(1)).getFileInputStream(eq("dir/file1.properties"));
        verify(propertiesMock, times(1)).load(fileInputStreamMock);
        verify(messagesMock, times(1)).put(eq("key1"), eq("message"));
        verify(messagesMock, times(1)).put(eq("key2"), eq("message"));
    }

    private FileInputStream getFileInputStream(String path) throws FileNotFoundException {
        return new FileInputStream(new File(path));
    }
}