import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultDateTypeAdapterTest {

  @Test
  public void testWrite() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonWriter writer = new JsonWriter();

    // Test with null value
    try {
      adapter.write(writer, null);
      assertNull(writer.getValue());
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }

    // Test with non-null value
    Date date = new Date();
    try {
      adapter.write(writer, date);
      assertEquals(date.toString(), writer.getValue().toString());
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }

  @Test
  public void testRead() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonReader reader = new JsonReader();

    // Test with valid date string
    String dateString = "2022-01-01";
    try {
      reader.setValue(dateString);
      Date date = adapter.read(reader);
      assertNotNull(date);
      assertEquals(dateString, date.toString());
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }

    // Test with invalid date string
    String invalidDateString = "abc";
    try {
      reader.setValue(invalidDateString);
      adapter.read(reader);
      fail("Expected JsonParseException to be thrown");
    } catch (JsonParseException e) {
      // Expected
    } catch (Exception e) {
      fail("Unexpected exception thrown: " + e.getMessage());
    }
  }

  @Test
  public void testDeserializeToDate() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);

    // Test with valid date string
    String dateString = "2022-01-01T00:00:00Z";
    try {
      Date date = adapter.deserializeToDate(dateString);
      assertNotNull(date);
      assertEquals(dateString, date.toString());
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }

    // Test with invalid date string
    String invalidDateString = "abc";
    try {
      adapter.deserializeToDate(invalidDateString);
      fail("Expected JsonSyntaxException to be thrown");
    } catch (JsonSyntaxException e) {
      // Expected
    } catch (Exception e) {
      fail("Unexpected exception thrown: " + e.getMessage());
    }
  }

  @Test
  public void testToString() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    assertEquals("DefaultDateTypeAdapter(SimpleDateFormat)", adapter.toString());
  }
}