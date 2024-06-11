
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import net.sf.xbus.base.core.config.Constants;
import net.sf.xbus.base.core.trace.Trace;
import net.sf.xbus.base.core.strings.XStringSupport;


public class MessageHandlerTest {

    @Mock
    private static Hashtable<String, MessageHandler> mInstancesMock;
    
    private MessageHandler messageHandler;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void testGetMessage() {
        messageHandler = new MessageHandler("basename");
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");
        
        String messageText = messageHandler.getMessage("key1", params);
        
        assertEquals("Key: key1 not found in message file", messageText);
    }

    @Test
    public void testGetMessageOptional() {
        messageHandler = new MessageHandler("basename");
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");
        
        String messageText = messageHandler.getMessageOptional("key1", params);
        
        assertNull(messageText);
    }
    
    @Test
    public void testGetInstance() {
        messageHandler = new MessageHandler("basename");
        Hashtable<String, MessageHandler> mInstancesStub = new Hashtable<>();
        mInstancesStub.put("basename", messageHandler);
        
        mInstancesMock.put("basename", messageHandler);
        when(mInstancesMock.get("basename")).thenReturn(messageHandler);
        
        MessageHandler instance = MessageHandler.getInstance("basename");
        
        assertEquals(messageHandler, instance);
    }
    
    @Test
    public void testAddMessages() throws FileNotFoundException, IOException {
        messageHandler = new MessageHandler("basename");
        String basename = "basename";
        String dir = Constants.XBUS_ETC;
        
        Properties propertiesMock = mock(Properties.class);
        
        File dirFileMock = mock(File.class);
        when(dirFileMock.list(new MessagesFilter(basename, "_" + Locale.ENGLISH.toString() + ".properties"))).thenReturn(new String[] {"messages_en.properties"});
        
        when(propertiesMock.keys()).thenReturn(new Hashtable<String, Object>().keys());
        when(propertiesMock.get(null)).thenReturn(new Hashtable<String, Object>().get(null));
        
        FileInputStream inputStreamMock = mock(FileInputStream.class);
        when(inputStreamMock).thenReturn(mock(FileInputStream.class));
        when(inputStreamMock.read()).thenReturn(-1);
        when(inputStreamMock.close()).thenReturn(mock(FileInputStream.class));
        
        Hashtable<String, Object> messagesMock = mock(Hashtable.class);
        when(messagesMock.get(null)).thenReturn(newProps.get(null));
        when(messagesMock.put(key, propertiesMock.get(null))).thenReturn(newProps.put(key, propertiesMock.get(null)));
        
        when(propertiesMock).thenReturn(propertiesMock);
        
        Hashtable<String, Object> newProps = new Hashtable<>();
        newProps.put(null, null);
        
        when(newProps.keys()).thenReturn(propertiesMock.keys());
            
        String key = null;
        Enumeration<String> keys = propertiesMock.keys();
        while (keys.hasMoreElements()) {
            key = keys.nextElement();
            messagesMock.put(key, propertiesMock.get(key));
        }

        String postfix = "_" + Locale.ENGLISH.toString() + ".properties";
        File dirFile = new File(dir);
        String[] messagesFiles = dirFile.list(new MessagesFilter(basename, postfix));
        
        for (int i = 0; messagesFiles != null && i < messagesFiles.length; ++i) {
            Properties newProps = new Properties();
            FileInputStream instream = new FileInputStream(dir + messagesFiles[i]);
            newProps.load(instream);
            instream.close();
            
            String key = null;
            Enumeration<String> keys = newProps.keys();
            while (keys.hasMoreElements()) {
                key = keys.nextElement();
                this.mMessages.put(key, newProps.get(key));
            }
        }
        
        assertEquals(messagesMock, messageHandler.mMessages);
    }
    
    @Test
    public void testMessagesFilter() {
        TestFilter filter = new TestFilter("prefix", "postfix");
        
        File dirMock = mock(File.class);
        String filename = "filename";
        
        when(dirMock.list(filter)).thenReturn(new String[] {"prefix_filename_postfix.properties"});
        
        boolean result = filter.accept(dirMock, filename);
        
        assertEquals(true, result);
    }
    
    private class TestFilter implements FilenameFilter {
        
        private String mPrefix;
        private String mPostfix;
        
        public TestFilter(final String prefix, final String postfix) {
            this.mPrefix = prefix;
            this.mPostfix = postfix;
        }
        
        @Override
        public boolean accept(final File dir, final String filename) {
            return filename.startsWith(this.mPrefix) && filename.endsWith(this.mPostfix);
        }
    }
}
