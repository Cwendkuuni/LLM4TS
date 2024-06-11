package net.sf.xbus.base.core;

import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class MessageHandlerTest {

    @Test
    public void testGetMessage() {
        MessageHandler messageHandler = MessageHandler.getInstance("basename");
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");
        String expected = "Key: key1 not found in message file";
        String result = messageHandler.getMessage("key1", params);
        assertEquals(expected, result);
    }

    @Test
    public void testGetMessageOptional() {
        MessageHandler messageHandler = MessageHandler.getInstance("basename");
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");
        String expected = null;
        String result = messageHandler.getMessageOptional("key1", params);
        assertEquals(expected, result);
    }

    @Test
    public void testGetInstance() {
        MessageHandler messageHandler1 = MessageHandler.getInstance("basename1");
        Hashtable<String, MessageHandler> mInstances = new Hashtable<>();
        mInstances.put("basename1", messageHandler1);
        Hashtable<String, MessageHandler> expected = mInstances;
        Hashtable<String, MessageHandler> result = MessageHandler.mInstances;
        assertEquals(expected, result);
    }

    @Test
    public void testAddMessages() {
        MessageHandler messageHandler = MessageHandler.getInstance("basename");
        String basename = "basename";
        String dir = "dir";
        Locale locale = Locale.ENGLISH;
        String postfix = "_" + locale.toString() + ".properties";
        File dirFile = new File(dir);
        String[] messagesFiles = dirFile.list(new MessageHandler.MessagesFilter(basename, postfix));
        Properties newProps = new Properties();
        String key = "key1";
        String value = "value1";
        newProps.put(key, value);
        Hashtable<String, String> mMessages = new Hashtable<>();
        mMessages.put(key, value);
        Hashtable<String, String> expected = mMessages;
        Hashtable<String, String> result = messageHandler.mMessages;
        assertEquals(expected, result);
    }
}