package net.sf.xbus.base.core;

import net.sf.xbus.base.core.trace.*;
import net.sf.xbus.base.core.strings.*;
import java.io.*;
import java.util.*;
import net.sf.xbus.base.core.config.*;

public class MessageHandler
{
    private static Hashtable mInstances;
    private static final Object classLock;
    private Hashtable mMessages;

    public String getMessage(final String key, final List params) {
        String messageText = null;
        messageText = this.getMessageOptional(key, params);
        if (messageText == null) {
            messageText = "Key: " + key + " not found in message file";
            Trace.error(messageText);
        }
        return messageText;
    }

    public String getMessageOptional(final String key, final List params) {
        String messageText = null;
        if (this.mMessages == null) {
            return null;
        }
        messageText = this.mMessages.get(key);
        if (messageText == null) {
            return null;
        }
        int counter = 1;
        if (params != null) {
            String paramText = null;
            Object paramObject = null;
            final Iterator it = params.iterator();
            while (it.hasNext()) {
                paramObject = it.next();
                if (paramObject != null) {
                    paramText = paramObject.toString();
                }
                else {
                    paramText = "<null>";
                }
                final String paramCounter = "$" + counter + "$";
                if (messageText.indexOf(paramCounter) >= 0) {
                    messageText = XStringSupport.replaceAll(messageText, paramCounter, paramText);
                    ++counter;
                }
            }
        }
        return messageText;
    }

    public static MessageHandler getInstance(final String basename) {
        synchronized (MessageHandler.classLock) {
            MessageHandler instance = MessageHandler.mInstances.get(basename);
            if (instance == null) {
                instance = new MessageHandler(basename);
                MessageHandler.mInstances.put(basename, instance);
            }
            return instance;
        }
    }

    private MessageHandler(final String basename) {
        this.mMessages = null;
        this.mMessages = new Hashtable();
        this.addMessages(basename, Constants.XBUS_ETC);
        this.addMessages(basename, Constants.XBUS_PLUGIN_ETC);
    }

    private void addMessages(final String basename, final String dir) {
        final Locale locale = Locale.ENGLISH;
        final String postfix = "_" + locale.toString() + ".properties";
        final File dirFile = new File(dir);
        final String[] messagesFiles = dirFile.list(new MessagesFilter(basename, postfix));
        for (int i = 0; messagesFiles != null && i < messagesFiles.length; ++i) {
            final Properties newProps = new Properties();
            try {
                final FileInputStream instream = new FileInputStream(dir + messagesFiles[i]);
                newProps.load(instream);
                instream.close();
            }
            catch (FileNotFoundException e) {
                System.out.println("Cannot find messagefile");
                System.exit(1);
            }
            catch (IOException e2) {
                System.out.println("Cannot find messagefile");
                System.exit(1);
            }
            String key = null;
            final Enumeration keys = newProps.keys();
            while (keys.hasMoreElements()) {
                key = keys.nextElement();
                this.mMessages.put(key, ((Hashtable<K, Object>)newProps).get(key));
            }
        }
    }

    static {
        MessageHandler.mInstances = new Hashtable();
        classLock = Configuration.class;
    }

    private static class MessagesFilter implements FilenameFilter
    {
        private String mPrefix;
        private String mPostfix;

        public MessagesFilter(final String prefix, final String postfix) {
            this.mPrefix = null;
            this.mPostfix = null;
            this.mPrefix = prefix;
            this.mPostfix = postfix;
        }

        @Override
        public boolean accept(final File dir, final String filename) {
            return filename.startsWith(this.mPrefix) && filename.endsWith(this.mPostfix);
        }
    }
}