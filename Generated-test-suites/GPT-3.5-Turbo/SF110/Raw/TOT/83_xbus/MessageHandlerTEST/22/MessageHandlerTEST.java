import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

@RunWith(JUnit4.class)
public class MessageHandlerTest {
  
  @Test
  public void testGetMessage() {
    // Typical use case
    MessageHandler handler = MessageHandler.getInstance("messages");
    List<String> params = new ArrayList<String>();
    params.add("John");
    params.add("Doe");
    String message = handler.getMessage("welcome_message", params);
    assertEquals("Welcome John Doe", message);
    
    // Edge case: Empty key
    message = handler.getMessage("", params);
    assertEquals("Key:  not found in message file", message);
    
    // Error scenario: Null params
    message = handler.getMessage("welcome_message", null);
    assertEquals("Welcome ", message);
  }
  
  @Test
  public void testGetMessageOptional() {
    // Typical use case
    MessageHandler handler = MessageHandler.getInstance("messages");
    List<String> params = new ArrayList<String>();
    params.add("John");
    params.add("Doe");
    String message = handler.getMessageOptional("welcome_message", params);
    assertEquals("Welcome John Doe", message);
    
    // Edge case: Empty key
    message = handler.getMessageOptional("", params);
    assertNull(message);
    
    // Edge case: Empty params
    params = new ArrayList<String>();
    message = handler.getMessageOptional("welcome_message", params);
    assertEquals("Welcome ", message);
  }
  
  @Test
  public void testGetInstance() {
    // Typical use case
    MessageHandler handler = MessageHandler.getInstance("messages");
    assertNotNull(handler);
    
    // Error scenario: Null basename
    handler = MessageHandler.getInstance(null);
    assertNull(handler);
    
    // Error scenario: Invalid basename
    handler = MessageHandler.getInstance("invalid_basename");
    assertNull(handler);
  }
  
}