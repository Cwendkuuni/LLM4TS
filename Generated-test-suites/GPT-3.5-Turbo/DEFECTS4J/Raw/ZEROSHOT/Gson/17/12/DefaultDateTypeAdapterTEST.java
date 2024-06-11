import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.gson.stream.JsonWriter;
import com.google.gson.stream.JsonReader;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DefaultDateTypeAdapterTest {

  private DefaultDateTypeAdapter adapter;

  @Before
  public void setUp() {
    adapter = new DefaultDateTypeAdapter(Date.class);
  }

  @Test
  public void testWrite_NullValue() throws IOException {
    JsonWriter writer = new JsonWriter(null);
    adapter.write(writer, null);
    writer.close();
    assertEquals("Should write null value", "null", writer.toString());
  }

  @Test
  public void testWrite_NotNullValue() throws IOException {
    JsonWriter writer = new JsonWriter(null);
    Date date = new Date();
    adapter.write(writer, date);
    writer.close();
    assertEquals("Should write date value", "\"" + DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US).format(date) + "\"", writer.toString());
  }

  @Test
  public void testRead_InvalidJsonToken() throws IOException {
    JsonReader reader = new JsonReader(null);
    reader.beginArray();
    try {
      adapter.read(reader);
      fail("Should throw JsonParseException for invalid JSON token");
    } catch (JsonParseException e) {
      // Expected exception
    }
    reader.endArray();
  }

  @Test
  public void testRead_NullValue() throws IOException {
    JsonReader reader = new JsonReader(null);
    reader.nullValue();
    reader.close();
    assertNull("Should return null for null value", adapter.read(reader));
  }

  @Test
  public void testRead_ValidJsonToken() throws IOException {
    JsonReader reader = new JsonReader(null);
    String dateString = "2021-01-01T00:00:00";
    reader.value(dateString);
    reader.close();
    Date date = adapter.read(reader);
    assertNotNull("Should return non-null for valid JSON token", date);
    DateFormat expectedFormat = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US);
    assertEquals("Should return date with expected format", expectedFormat.format(date), dateString);
  }

  @Test
  public void testDeserializeToDate_ValidString() {
    String dateString = "2021-01-01T00:00:00";
    Date date = adapter.deserializeToDate(dateString);
    assertNotNull("Should return non-null for valid string", date);
    DateFormat expectedFormat = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US);
    assertEquals("Should return date with expected format", expectedFormat.format(date), dateString);
  }

  @Test(expected = JsonSyntaxException.class)
  public void testDeserializeToDate_InvalidString() {
    String dateString = "2021/01/01";
    adapter.deserializeToDate(dateString);
  }

  @Test
  public void testToString() {
    String expectedString = "DefaultDateTypeAdapter(SimpleDateFormat)";
    assertEquals("Should return expected string representation", expectedString, adapter.toString());
  }
}