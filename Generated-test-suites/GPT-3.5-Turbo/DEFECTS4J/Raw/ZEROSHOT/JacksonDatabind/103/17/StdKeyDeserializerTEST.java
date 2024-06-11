import static org.junit.Assert.*;
import org.junit.Test;

public class StdKeyDeserializerTest {
  
  @Test
  public void testDeserializeKey() throws Exception {
    StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
    assertEquals("test", deserializer.deserializeKey("test", null));
  }

  @Test
  public void testGetInt() throws Exception {
    StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
    assertEquals(42, deserializer._parseInt("42"));
  }

  @Test
  public void testGetLong() throws Exception {
    StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LONG, Long.class);
    assertEquals(10000000000L, deserializer._parseLong("10000000000"));
  }

  @Test
  public void testGetDouble() throws Exception {
    StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DOUBLE, Double.class);
    assertEquals(3.14, deserializer._parseDouble("3.14"), 0.1);
  }

  @Test
  public void testWeirdKey() throws Exception {
    StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
    try {
      deserializer._weirdKey(null, "key", new Exception("Test exception"));
    } catch(Exception e) {
      assertEquals("Test exception", e.getMessage());
    }
  }

}