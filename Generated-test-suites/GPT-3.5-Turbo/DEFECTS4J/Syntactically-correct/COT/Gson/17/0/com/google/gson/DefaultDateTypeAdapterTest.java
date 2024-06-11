import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultDateTypeAdapterTest {

  @Test
  public void testWrite() throws IOException {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    
    // Test writing null date
    JsonWriter writer = new JsonWriter(new StringWriter());
    adapter.write(writer, null);
    assertEquals("Expected null value", "null", writer.toString());
    
    // Test writing non-null date
    Date date = new Date();
    writer = new JsonWriter(new StringWriter());
    adapter.write(writer, date);
    assertEquals("Expected formatted date string", date.toString(), writer.toString());
  }

  @Test
  public void testRead() throws IOException {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonReader reader = new JsonReader(new StringReader("2022-01-01T00:00:00Z"));
    
    // Test reading valid date string
    Date date = adapter.read(reader);
    assertEquals("Expected parsed date", ISO8601Utils.parse("2022-01-01T00:00:00Z").getTime(), date.getTime());
    
    // Test reading invalid date string
    reader = new JsonReader(new StringReader("invalid date"));
    try {
      adapter.read(reader);
      fail("Expected JsonParseException");
    } catch (JsonParseException e) {
      // Exception should be thrown
    }
  }

  @Test
  public void testToString() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    String expectedToString = "DefaultDateTypeAdapter(SimpleDateFormat)";
    assertEquals("Expected toString representation", expectedToString, adapter.toString());
  }
}