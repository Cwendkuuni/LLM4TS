// Generated JUnit 4 test cases for MessageHandler

import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class MessageHandlerTest {
  
  @Test
  public void testGetMessage() {
    MessageHandler handler = new MessageHandler("test");
    String key = "key";
    List<String> params = new ArrayList<>();
    params.add("param1");
    params.add("param2");
    
    String message = handler.getMessage(key, params);
    
    assertEquals("Key: key not found in message file", message);
  }
  
  @Test
  public void testGetMessageOptional() {
    MessageHandler handler = new MessageHandler("test");
    String key = "key";
    List<String> params = new ArrayList<>();
    params.add("param1");
    params.add("param2");
    
    String message = handler.getMessageOptional(key, params);
    
    assertNull(message);
  }
  
  @Test
  public void testGetInstance() {
    String basename = "test";
    
    MessageHandler instance = MessageHandler.getInstance(basename);
    
    assertNotNull(instance);
  }
  
  // Edge cases and exception handling scenarios
  
  @Test
  public void testGetMessageNoParams() {
    MessageHandler handler = new MessageHandler("test");
    String key = "key";
    List<String> params = null;
    
    String message = handler.getMessage(key, params);
    
    assertEquals("Key: key not found in message file", message);
  }
  
  @Test
  public void testGetMessageOptionalNoMessages() {
    MessageHandler handler = new MessageHandler("test");
    String key = "key";
    List<String> params = new ArrayList<>();
    params.add("param1");
    params.add("param2");
    handler.mMessages = null;
    
    String message = handler.getMessageOptional(key, params);
    
    assertNull(message);
  }
  
  @Test
  public void testAddMessages() {
    MessageHandler handler = new MessageHandler("test");
    String basename = "test";
    String dir = ".\\messages\\";
    
    handler.addMessages(basename, dir);
    
    assertNotNull(handler.mMessages);
  }
}