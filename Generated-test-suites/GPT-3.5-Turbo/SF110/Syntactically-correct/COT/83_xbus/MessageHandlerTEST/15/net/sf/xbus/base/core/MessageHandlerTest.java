import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class MessageHandlerTest {
  
  @Test
  public void testGetMessage() {
    MessageHandler mh = new MessageHandler("basename");
    List<String> params = new ArrayList<>();
    params.add("param1");
    params.add("param2");
    String result = mh.getMessage("key", params);
    assertNotNull(result);
    assertEquals("Expected result", result);
  }
  
  @Test
  public void testGetMessageOptional() {
    MessageHandler mh = new MessageHandler("basename");
    List<String> params = new ArrayList<>();
    params.add("param1");
    params.add("param2");
    String result = mh.getMessageOptional("key", params);
    assertNotNull(result);
    assertEquals("Expected result", result);
  }
  
  @Test
  public void testGetInstance() {
    MessageHandler mh = MessageHandler.getInstance("basename");
    assertNotNull(mh);
  }
  
  @Test
  public void testAddMessages() {
    MessageHandler mh = new MessageHandler("basename");
    mh.addMessages("basename", "dir");
    assertNotNull(mh);
  }
}