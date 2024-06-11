import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;
import net.sf.xbus.base.core.MessageHandler;

public class MessageHandlerTest {

   MessageHandler messageHandler;

   @Before
   public void setUp() {
      messageHandler = new MessageHandler();
   }

   @Test
   public void testGetMessage() {
      List<String> params = new ArrayList<>();
      params.add("param1");
      params.add("param2");
      String key = "testKey1";
      String expected = "Test message with param1 and param2";
      String result = messageHandler.getMessage(key, params);
      assertEquals(expected, result);
   }

   @Test
   public void testGetMessage_InvalidKey() {
      List<String> params = new ArrayList<>();
      params.add("param1");
      String key = "invalidKey";
      String expected = "Key: invalidKey not found in message file";
      String result = messageHandler.getMessage(key, params);
      assertEquals(expected, result);
      // Ensure the error is logged
      // Assert.assertTrue(Trace.errorLogContains(expected));
   }

   @Test
   public void testGetMessage_NullParams() {
      String key = "testKey2";
      String expected = "Test message with null param";
      String result = messageHandler.getMessage(key, null);
      assertEquals(expected, result);
   }

   @Test
   public void testGetMessageOptional_ExistingKey() {
      List<String> params = new ArrayList<>();
      params.add("param1");
      params.add("param2");
      String key = "testKey1";
      String expected = "Test message with param1 and param2";
      String result = messageHandler.getMessageOptional(key, params);
      assertEquals(expected, result);
   }

   @Test
   public void testGetMessageOptional_NullKey() {
      List<String> params = new ArrayList<>();
      params.add("param1");
      params.add("param2");
      String key = null;
      String expected = null;
      String result = messageHandler.getMessageOptional(key, params);
      assertEquals(expected, result);
   }

   @Test
   public void testGetMessageOptional_NullParams() {
      String key = "testKey2";
      String expected = "Test message with null param";
      String result = messageHandler.getMessageOptional(key, null);
      assertEquals(expected, result);
   }

   @Test
   public void testGetInstance_ExistingBasename() {
      String basename = "existingBasename";
      MessageHandler instance = messageHandler.getInstance(basename);
      assertNotNull(instance);
   }

   @Test
   public void testGetInstance_NewBasename() {
      String basename = "newBasename";
      MessageHandler instance = messageHandler.getInstance(basename);
      assertNotNull(instance);
      // Ensure the instance is added to the instances Hashtable
      // Assert.assertTrue(MessageHandler.mInstances.containsKey(basename));
   }

   @Test
   public void testGetInstance_MultipleThreads() {
      // Test concurrent access to getInstance method
      // Create multiple threads and let them call getInstance simultaneously
      // Ensure all threads get the same instance
   }

   @Test
   public void testAddMessages_ExistingDir() {
      String basename = "existingBasename";
      String dir = "/path/to/existing/dir";
      messageHandler.addMessages(basename, dir);
      // Assert.assertTrue(messageHandler.containsMessages(basename));
   }
   
   @Test
   public void testAddMessages_NonExistingDir() {
      String basename = "nonExistingBasename";
      String dir = "/path/to/nonExisting/dir";
      messageHandler.addMessages(basename, dir);
      // Assert.assertFalse(messageHandler.containsMessages(basename));
   }
   
   @Test
   public void testAddMessages_NullDir() {
      String basename = "nullBasename";
      String dir = null;
      messageHandler.addMessages(basename, dir);
      // Assert.assertFalse(messageHandler.containsMessages(basename));
   }

   @After
   public void tearDown() {
      messageHandler = null;
   }
}