package net.sf.xbus.base.core;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

public class MessageHandlerTest {

    @Mock
    private Hashtable mMessages;
    
    @InjectMocks
    private MessageHandler messageHandler;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetMessage() {
        List<String> params = new ArrayList<>();
        params.add("param1");
        String expectedMessage = "Key: key1 not found in message file";
        String actualMessage = messageHandler.getMessage("key1", params);
        assertEquals(expectedMessage, actualMessage);
    }
    
    @Test
    public void testGetMessageOptional() {
        List<String> params = new ArrayList<>();
        params.add("param1");
        String expectedMessage = "message1";
        String actualMessage = messageHandler.getMessageOptional("key1", params);
        assertEquals(expectedMessage, actualMessage);
    }
    
    @Test
    public void testGetInstance() {
        String basename = "basename";
        MessageHandler instance = MessageHandler.getInstance(basename);
        assertNotNull(instance);
    }
    
    @Test
    public void testConstructor() {
        String basename = "basename";
        MessageHandler instance = new MessageHandler(basename);
        
        assertNotNull(instance.mMessages);
        assertEquals(0, instance.mMessages.size());
        
        String dir = Constants.XBUS_ETC;
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
                assertEquals(newProps.get(key), instance.mMessages.get(key));
            }
        }
    }
    
    private static class MessagesFilter implements FilenameFilter {
        private String mPrefix;
        private String mPostfix;
        
        public MessagesFilter(final String prefix, final String postfix) {
            this.mPrefix = prefix;
            this.mPostfix = postfix;
        }
        
        @Override
        public boolean accept(final File dir, final String filename) {
            return filename.startsWith(this.mPrefix) && filename.endsWith(this.mPostfix);
        }
    }
}