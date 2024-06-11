import static org.junit.Assert.*;
import org.junit.Test;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class DefaultDateTypeAdapterTest {

  private DefaultDateTypeAdapter adapter;

  @Before
  public void setup() {
    adapter = new DefaultDateTypeAdapter(Date.class);
  }

  @Test
  public void testWrite_NullValue() {
    JsonWriter writer = new JsonWriter();
    Date date = null;
    try {
      adapter.write(writer, date);
      assertEquals("null", writer.toString());
    } catch (IOException e) {
      fail("Unexpected IOException");
    }
  }

  @Test
  public void testWrite_ValidValue() {
    JsonWriter writer = new JsonWriter();
    Date date = new Date(1609473600000L); // Jan 1, 2021 00:00:00 UTC
    try {
      adapter.write(writer, date);
      assertEquals("\"2021-01-01T00:00:00\"", writer.toString());
    } catch (IOException e) {
      fail("Unexpected IOException");
    }
  }

  @Test
  public void testRead_InvalidValue() {
    JsonReader reader = new JsonReader();
    reader.beginObject();
    reader.name("date");
    reader.value("2021-01-01T00:00:00");
    reader.endObject();
    try {
      Date date = adapter.read(reader);
      fail("Expected JsonParseException");
    } catch (IOException e) {
      fail("Unexpected IOException");
    } catch (JsonParseException e) {
      // Expected exception
    }
  }

  @Test
  public void testRead_ValidValue() {
    JsonReader reader = new JsonReader();
    reader.beginObject();
    reader.name("date");
    reader.value("2021-01-01T00:00:00Z");
    reader.endObject();
    try {
      Date date = adapter.read(reader);
      assertEquals(new Date(1609473600000L), date);
    } catch (IOException e) {
      fail("Unexpected IOException");
    }
  }
}