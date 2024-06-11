import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.internal.bind.util.ISO8601Utils;
import com.google.gson.stream.JsonParseException;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public class DefaultDateTypeAdapterTest {

  private DefaultDateTypeAdapter adapter;

  @Before
  public void setup() {
    adapter = new DefaultDateTypeAdapter(Date.class);
  }

  @Test
  public void testWrite_NullValue() {
    JsonWriter writer = new JsonWriter(null); // Create a dummy JsonWriter
    assertNull(writer.value);

    adapter.write(writer, null);

    assertNull(writer.value);
  }

  @Test
  public void testWrite_ValidDate() throws Exception {
    Date date = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse("2022-01-01");
    JsonWriter writer = new JsonWriter(null); // Create a dummy JsonWriter
    assertNull(writer.value);

    adapter.write(writer, date);

    assertNotNull(writer.value);
    assertEquals("2022-01-01", writer.value);
  }

  @Test(expected = JsonParseException.class)
  public void testRead_InvalidJsonToken() throws Exception {
    JsonReader reader = new JsonReader(null); // Create a dummy JsonReader
    reader.setLenient(false);

    adapter.read(reader);
  }

  @Test
  public void testRead_ValidDate() throws Exception {
    String json = "\"2022-01-01\"";
    JsonReader reader = new JsonReader(null); // Create a dummy JsonReader
    reader.setLenient(false);
    reader.json = json;

    Date date = adapter.read(reader);

    assertNotNull(date);
    assertEquals("2022-01-01", new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(date));
  }

  @Test
  public void testRead_ValidTimestamp() throws Exception {
    String json = "\"2022-01-01T00:00:00.000Z\"";
    JsonReader reader = new JsonReader(null); // Create a dummy JsonReader
    reader.setLenient(false);
    reader.json = json;

    Date date = adapter.read(reader);

    assertNotNull(date);
    assertEquals("2022-01-01 00:00:00.000", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US).format(date));
    assertEquals(Timestamp.class, date.getClass());
  }

  @Test
  public void testRead_ValidSqlDate() throws Exception {
    String json = "\"2022-01-01\"";
    adapter = new DefaultDateTypeAdapter(java.sql.Date.class);
    JsonReader reader = new JsonReader(null); // Create a dummy JsonReader
    reader.setLenient(false);
    reader.json = json;

    Date date = adapter.read(reader);

    assertNotNull(date);
    assertEquals("2022-01-01", new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(date));
    assertEquals(java.sql.Date.class, date.getClass());
  }

  @Test(expected = AssertionError.class)
  public void testRead_InvalidDateType() throws Exception {
    String json = "\"2022-01-01\"";
    adapter = new DefaultDateTypeAdapter(String.class);
    JsonReader reader = new JsonReader(null); // Create a dummy JsonReader
    reader.setLenient(false);
    reader.json = json;

    adapter.read(reader);
  }

  @Test(expected = JsonParseException.class)
  public void testRead_InvalidDateFormat() throws Exception {
    String json = "\"2022/01/01\"";
    JsonReader reader = new JsonReader(null); // Create a dummy JsonReader
    reader.setLenient(false);
    reader.json = json;

    adapter.read(reader);
  }

  @Test(expected = JsonParseException.class)
  public void testRead_InvalidISO8601Format() throws Exception {
    String json = "\"2022-01-01T00:00:00Z\"";
    JsonReader reader = new JsonReader(null); // Create a dummy JsonReader
    reader.setLenient(false);
    reader.json = json;

    adapter.read(reader);
  }

  @Test
  public void testDeserializeToDate_ValidDate() throws ParseException {
    String dateString = "2022-01-01";
    Date result = adapter.deserializeToDate(dateString);
    assertNotNull(result);
    assertEquals(dateString, new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(result));
  }

  @Test(expected = JsonParseException.class)
  public void testDeserializeToDate_InvalidDate() throws ParseException {
    String dateString = "2022/01/01";
    adapter.deserializeToDate(dateString);
  }

  @Test
  public void testToString() {
    String expected = "DefaultDateTypeAdapter(SimpleDateFormat)";
    String result = adapter.toString();
    assertEquals(expected, result);
  }

}