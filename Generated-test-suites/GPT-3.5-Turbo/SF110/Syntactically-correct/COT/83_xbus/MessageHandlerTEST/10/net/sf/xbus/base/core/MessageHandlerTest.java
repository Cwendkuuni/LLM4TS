import org.junit.Test;
import static org.junit.Assert.*;

public class MessageHandlerTest {

    @Test
    public void testGetMessage() {
        MessageHandler messageHandler = MessageHandler.getInstance("basename");
        String key = "key";
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");
        String expectedResult = "Expected Result";
        
        // Test case 1: Valid key and params
        String result = messageHandler.getMessage(key, params);
        assertEquals(expectedResult, result);
        
        // Test case 2: Valid key but null params
        params = null;
        result = messageHandler.getMessage(key, params);
        assertEquals(expectedResult, result);
        
        // Test case 3: Invalid key
        key = "invalidKey";
        result = messageHandler.getMessage(key, params);
        assertEquals("Key: " + key + " not found in message file", result);
    }
    
    @Test
    public void testGetMessageOptional() {
        MessageHandler messageHandler = MessageHandler.getInstance("basename");
        String key = "key";
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");
        String expectedResult = "Expected Result";
        
        // Test case 1: Valid key and params
        String result = messageHandler.getMessageOptional(key, params);
        assertEquals(expectedResult, result);
        
        // Test case 2: Valid key but null params
        params = null;
        result = messageHandler.getMessageOptional(key, params);
        assertEquals(expectedResult, result);
        
        // Test case 3: Invalid key
        key = "invalidKey";
        result = messageHandler.getMessageOptional(key, params);
        assertNull(result);
    }
    
    @Test
    public void testGetInstance() {
        // Test case 1: Valid basename
        String basename = "basename";
        MessageHandler instance = MessageHandler.getInstance(basename);
        assertNotNull(instance);
        
        // Test case 2: Invalid basename
        basename = "invalidBasename";
        instance = MessageHandler.getInstance(basename);
        assertNotNull(instance);
    }
    
    private static class TestMessagesFilter implements FilenameFilter {
        private String mPrefix;
        private String mPostfix;
        
        public TestMessagesFilter(final String prefix, final String postfix) {
            this.mPrefix = prefix;
            this.mPostfix = postfix;
        }
        
        @Override
        public boolean accept(final File dir, final String filename) {
            return filename.startsWith(this.mPrefix) && filename.endsWith(this.mPostfix);
        }
    }
    
    @Test
    public void testAddMessages() {
        MessageHandler messageHandler = new MessageHandler("basename");
        String basename = "basename";
        String dir = Constants.XBUS_ETC;
        String postfix = "_" + Locale.ENGLISH.toString() + ".properties";
        File dirFile = new File(dir);
        String[] messagesFiles = dirFile.list(new TestMessagesFilter(basename, postfix));
        
        // Test case 1: Valid basename and dir
        assertNotNull(messagesFiles);
        
        // Test case 2: Invalid basename or dir
        basename = "invalidBasename";
        dir = "invalidDir";
        messagesFiles = dirFile.list(new TestMessagesFilter(basename, postfix));
        assertNull(messagesFiles);
    }
}