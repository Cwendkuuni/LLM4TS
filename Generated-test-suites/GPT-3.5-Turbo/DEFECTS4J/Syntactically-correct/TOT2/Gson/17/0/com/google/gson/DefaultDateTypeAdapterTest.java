import static org.junit.Assert.*;
import org.junit.Test;

public class DefaultDateTypeAdapterTest {

  @Test
  public void testWrite_NullDate() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    try {
      adapter.write(null);
    } catch (IOException e) {
      fail("Exception not expected for null Date");
    }
  }

  @Test
  public void testRead_InvalidJson() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonReader reader = new JsonReader(new StringReader("invalid json"));
    try {
      adapter.read(reader);
      fail("Expected JsonParseException to be thrown for invalid JSON");
    } catch (IOException e) {
      // Expected exception
    }
  }

  @Test
  public void testDeserializeToDate_ValidString() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    Date date = adapter.deserializeToDate("2021-05-10T10:15:30Z");
    assertNotNull(date);
  }

  @Test
  public void testDeserializeToDate_InvalidString() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    Date date = adapter.deserializeToDate("invalid date string");
    assertNull(date);
  }

  @Test
  public void testToString() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    String result = adapter.toString();
    assertNotNull(result);
  }

}