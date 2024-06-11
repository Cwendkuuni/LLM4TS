package net.sf.xbus.base.core;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import net.sf.xbus.base.core.config.Configuration;
import net.sf.xbus.base.core.strings.XStringSupport;
import net.sf.xbus.base.core.trace.Trace;

public class MessageHandlerTest {
    private MessageHandler messageHandler;
    private Hashtable<String, String> mMessages;

    @Before
    public void setUp() {
        messageHandler = new MessageHandler("test_basname");
    }

    @Test
    public void testGetMessage() {
        List<String> params = new ArrayList<>();
        params.add("param1");

        assertEquals("Message not found", messageHandler.getMessage("non_existing_key", params));

        String key = "existing_key";
        String messageText = "Message with $1$ param";
        mMessages.put(key, messageText);
        String expected = "Message with param1 param";
        assertEquals(expected, messageHandler.getMessage(key, params));

        assertNull(messageHandler.getMessage("null_key", params));
    }

    @Test
    public void testGetMessageOptional() {
        List<String> params = new ArrayList<>();
        params.add("param1");

        assertNull(messageHandler.getMessageOptional("non_existing_key", params));

        String key = "existing_key";
        String messageText = "Message with $1$ param";
        mMessages.put(key, messageText);
        String expected = "Message with param1 param";
        assertEquals(expected, messageHandler.getMessageOptional(key, params));

        assertNull(messageHandler.getMessageOptional("null_key", params));
    }

    @Test
    public void testGetInstance() {
        synchronized (MessageHandler.classLock) {
            assertNull(MessageHandler.getInstance("non_existing_basename"));

            String basename = "existing_basename";
            MessageHandler instance = messageHandler.getInstance(basename);
            assertNotNull(instance);
            assertEquals(instance, MessageHandler.mInstances.get(basename));
        }
    }

    @Test
    public void testAddMessages() {
        String basename = "test_basename";
        String dir = "test_dir";

        Locale locale = Locale.ENGLISH;
        String postfix = "_" + locale.toString() + ".properties";
        File dirFile = new File(dir);
        String[] messagesFiles = dirFile.list(new MessagesFilter(basename, postfix));
        
        Properties newProps = new Properties();
        try {
            FileInputStream instream = new FileInputStream(dir + messagesFiles[0]);
            newProps.load(instream);
            instream.close();
        } catch (FileNotFoundException e) {
            System.out.println("Cannot find messagefile");
            System.exit(1);
        } catch (IOException e) {
            System.out.println("Cannot find messagefile");
            System.exit(1);
        }

        String key = null;
        Enumeration keys = newProps.keys();
        while (keys.hasMoreElements()) {
            key = keys.nextElement();
            mMessages.put(key, ((Hashtable<String, Object>) newProps).get(key));
        }

        assertFalse(mMessages.isEmpty());
    }

    private static class MessagesFilter implements java.io.FilenameFilter {
        private String mPrefix;
        private String mPostfix;

        public MessagesFilter(String prefix, String postfix) {
            this.mPrefix = null;
            this.mPostfix = null;
            this.mPrefix = prefix;
            this.mPostfix = postfix;
        }

        @Override
        public boolean accept(File dir, String filename) {
            return filename.startsWith(this.mPrefix) && filename.endsWith(this.mPostfix);
        }
    }
}