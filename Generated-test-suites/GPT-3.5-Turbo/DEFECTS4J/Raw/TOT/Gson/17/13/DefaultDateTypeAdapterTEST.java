
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

public class DefaultDateTypeAdapterTest {

  private DefaultDateTypeAdapter dateTypeAdapter;
  private SimpleDateFormat dateFormat;

  @Before
  public void setup() {
    dateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
    dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  }

  @Test
  public void testWrite_NullDate_WritesNullValue() {
    try {
      JsonWriter writer = new JsonWriter();
      dateTypeAdapter.write(writer, null);
      assertEquals("null", writer.toString());
    } catch (IOException e) {
      fail("Unexpected IOException occurred");
    }
  }

  @Test
  public void testWrite_ValidDate_WritesCorrectValue() {
    try {
      JsonWriter writer = new JsonWriter();
      Date validDate = dateFormat.parse("2021-01-01 12:00:00");
      dateTypeAdapter.write(writer, validDate);
      assertEquals("\"2021-01-01 12:00:00\"", writer.toString());
    } catch (IOException | ParseException e) {
      fail("Unexpected exception occurred");
    }
  }

  @Test
  public void testRead_InvalidJson_ThrowsJsonParseException() {
    try {
      JsonReader reader = new JsonReader();
      reader.beginObject();
      reader.name("date");
      reader.value("2021-01-01 12:00:00");
      reader.endObject();

      dateTypeAdapter.read(reader);
      fail("Expected JsonParseException to be thrown");
    } catch (IOException | JsonParseException e) {
      assertTrue(e instanceof JsonParseException);
    }
  }

  @Test
  public void testRead_ValidJson_ReturnsCorrectDate() {
    try {
      JsonReader reader = new JsonReader();
      reader.value("\"2021-01-01 12:00:00\"");

      Date expectedDate = dateFormat.parse("2021-01-01 12:00:00");
      Date actualDate = dateTypeAdapter.read(reader);
      assertEquals(expectedDate, actualDate);
    } catch (IOException | ParseException e) {
      fail("Unexpected exception occurred");
    }
  }

  @Test
  public void testToString_ReturnsCorrectStringRepresentation() {
    String expectedString = "DefaultDateTypeAdapter(SimpleDateFormat)";
    String actualString = dateTypeAdapter.toString();
    assertEquals(expectedString, actualString);
  }
}
