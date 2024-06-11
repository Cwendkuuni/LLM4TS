package net.sf.xbus.base.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.File;
import java.util.Hashtable;
import java.util.Locale;
import java.util.List;
import java.util.Properties;

public class MessageHandlerTest {

    private MessageHandler messageHandler;

    @Before
    public void setup() {
        messageHandler = new MessageHandler("test");
    }

    @Test
    public void testGetMessage() {
        List params = mock(List.class);

        when(messageHandler.getMessageOptional("key", params)).thenReturn("message");
        assertEquals("message", messageHandler.getMessage("key", params));

        when(messageHandler.getMessageOptional("key", params)).thenReturn(null);
        assertNull(messageHandler.getMessage("key", params));
    }

    @Test
    public void testGetMessageOptional() {
        List params = mock(List.class);
        Hashtable mMessages = mock(Hashtable.class);

        messageHandler.mMessages = mMessages;

        when(mMessages.get("key")).thenReturn("message");
        assertEquals("message", messageHandler.getMessageOptional("key", params));

        when(mMessages.get("key")).thenReturn(null);
        assertNull(messageHandler.getMessageOptional("key", params));
    }

    @Test
    public void testGetInstance() {
        MessageHandler instance = MessageHandler.getInstance("basename");

        assertNotNull(instance);
    }

    @Test
    public void testAddMessages() {
        String basename = "basename";
        String dir = "dir";
        Locale locale = Locale.ENGLISH;
        String postfix = "_" + locale.toString() + ".properties";
        File dirFile = mock(File.class);
        Properties newProps = mock(Properties.class);
        FileInputStream instream = mock(FileInputStream.class);
        File[] messagesFiles = {mock(File.class)};
        Hashtable mMessages = mock(Hashtable.class);
        Enumeration keys = mock(Enumeration.class);

        whenNew(File.class).withArguments(dir).thenReturn(dirFile);
        when(dirFile.list(any(FilenameFilter.class))).thenReturn(messagesFiles);

        whenNew(Properties.class).thenReturn(newProps);
        whenNew(FileInputStream.class).withArguments(anyString()).thenReturn(instream);

        when(newProps.keys()).thenReturn(keys);
        when(keys.hasMoreElements()).thenReturn(true, false);
        when(keys.nextElement()).thenReturn("key");

        when(newProps.get(any())).thenReturn("message");

        messageHandler.mMessages = mMessages;

        messageHandler.addMessages(basename, dir);

        verify(mMessages).put("key", "message");
    }

}