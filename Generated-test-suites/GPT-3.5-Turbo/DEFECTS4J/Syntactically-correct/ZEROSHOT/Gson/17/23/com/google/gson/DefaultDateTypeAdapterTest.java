import static org.junit.Assert.*;
import org.junit.Test;

public class DefaultDateTypeAdapterTest {

  @Test
  public void testWrite_NullValue() {
    // Given
    JsonWriter out = new JsonWriter();
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US), DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT));

    // When
    adapter.write(out, null);

    // Then
    assertEquals("null", out.toString());
  }

  @Test
  public void testWrite_ValidValue() {
    // Given
    JsonWriter out = new JsonWriter();
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US), DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT));
    Date date = new Date();

    // When
    adapter.write(out, date);

    // Then
    assertEquals(date.toString(), out.toString());
  }

  @Test
  public void testRead_InvalidJsonToken() {
    // Given
    JsonReader in = new JsonReader();
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US), DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT));

    // When
    try {
      adapter.read(in);
      fail("Expected JsonParseException");
    } catch (JsonParseException e) {
      // Then
      assertEquals("The date should be a string value", e.getMessage());
    }
  }

  @Test
  public void testRead_ValidValue() {
    // Given
    JsonReader in = new JsonReader();
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US), DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT));
    String dateString = "2022-01-01 12:00:00";

    // When
    adapter.read(in);
    Date date = adapter.deserializeToDate(dateString);

    // Then
    assertNotNull(date);
    assertEquals(dateString, date.toString());
  }

  @Test
  public void testToString() {
    // Given
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US), DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT));

    // When
    String toString = adapter.toString();

    // Then
    assertEquals("DefaultDateTypeAdapter(SimpleDateFormat)", toString);
  }
}