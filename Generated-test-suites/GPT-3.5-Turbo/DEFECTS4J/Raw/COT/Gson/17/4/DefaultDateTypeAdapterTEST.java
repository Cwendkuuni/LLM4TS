import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultDateTypeAdapterTest {

  private DefaultDateTypeAdapter adapter;

  @Before
  public void setup() {
    adapter = new DefaultDateTypeAdapter(Date.class);
  }

  @Test
  public void testWriteNullValue() {
    // Test writing null value
    assertEquals("null", adapter.write(null));
  }

  @Test
  public void testWriteDateValue() {
    // Test writing date value
    Date date = new Date();
    String expected = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US).format(date);
    assertEquals(expected, adapter.write(date));
  }

  @Test
  public void testReadNullValue() {
    // Test reading null value
    assertNull(adapter.read(null));
  }

  @Test
  public void testReadInvalidJsonToken() {
    // Test reading invalid json token
    JsonReader reader = new JsonReader();
    reader.beginArray();
    try {
      adapter.read(reader);
      fail("Expected JsonParseException");
    } catch (JsonParseException e) {
      // Expected exception
    }
    reader.endArray();
  }

  @Test
  public void testReadDateString() {
    // Test reading date string
    String dateString = "2021-01-01 12:00:00";
    Date expected = new SimpleDateFormat(dateString, Locale.US).parse(dateString);
    assertEquals(expected, adapter.read(dateString));
  }

  @Test
  public void testReadInvalidDateString() {
    // Test reading invalid date string
    String invalidDateString = "2021-01-01T12:00:00Z";
    try {
      adapter.read(invalidDateString);
      fail("Expected JsonSyntaxException");
    } catch (JsonSyntaxException e) {
      // Expected exception
    }
  }

  @Test
  public void testToString() {
    // Test toString method
    assertEquals("DefaultDateTypeAdapter(SimpleDateFormat)", adapter.toString());
  }
}