import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class MessageHandlerTest {

   @Test
   public void testGetMessage() {
      MessageHandler mh = MessageHandler.getInstance("basename");
      List<String> params = new ArrayList<>();
      params.add("param1");
      String result = mh.getMessage("key", params);
      assertNotNull(result);
   }

   @Test
   public void testGetMessageOptional() {
      MessageHandler mh = MessageHandler.getInstance("basename");
      List<String> params = new ArrayList<>();
      params.add("param1");
      String result = mh.getMessageOptional("key", params);
      assertNotNull(result);
   }

   @Test
   public void testGetMessageOptional_nullParams() {
      MessageHandler mh = MessageHandler.getInstance("basename");
      List<String> params = null;
      String result = mh.getMessageOptional("key", params);
      assertNotNull(result);
   }

   @Test
   public void testGetMessageOptional_nullMessage() {
      MessageHandler mh = MessageHandler.getInstance("basename");
      List<String> params = new ArrayList<>();
      params.add("param1");
      String result = mh.getMessageOptional("non-existent-key", params);
      assertNull(result);
   }

   @Test
   public void testGetInstance() {
      MessageHandler mh1 = MessageHandler.getInstance("basename1");
      MessageHandler mh2 = MessageHandler.getInstance("basename1");
      assertSame(mh1, mh2);
   }

   @Test
   public void testAddMessages() {
      MessageHandler mh = MessageHandler.getInstance("basename");
      mh.addMessages("basename", "dir");
      assertNotNull(mh);
   }
}