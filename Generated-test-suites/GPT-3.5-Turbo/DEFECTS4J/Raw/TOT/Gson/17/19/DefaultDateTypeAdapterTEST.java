import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import com.google.gson.internal.bind.util.ISO8601Utils;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public class DefaultDateTypeAdapterTest {

  // Test Case 1: write method should correctly serialize the Date object
  @Test
  public void testWrite() throws IOException {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonWriter out = new JsonWriter(null);
    Date date = new Date();
    adapter.write(out, date);
    assertEquals("\"" + date.toString() + "\"", out.toString());
  }

  // Test Case 2: write method should handle null value by calling nullValue on the JsonWriter
  @Test
  public void testWriteNullValue() throws IOException {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonWriter out = new JsonWriter(null);
    adapter.write(out, null);
    assertEquals("null", out.toString());
  }

  // Test Case 3: read method should correctly deserialize the Date object
  @Test
  public void testRead() throws IOException {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonReader in = new JsonReader(null);
    in.addToken(JsonToken.STRING, "\"2019-01-01\"");
    Date date = adapter.read(in);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Date expectedDate = sdf.parse("2019-01-01");
    assertEquals(expectedDate, date);
  }

  // Test Case 4: read method should throw JsonParseException when the input is not a string
  @Test(expected = JsonParseException.class)
  public void testReadInvalidInput() throws IOException {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonReader in = new JsonReader(null);
    in.addToken(JsonToken.NUMBER, "123");
    adapter.read(in);
  }

  // Test Case 5: read method should correctly deserialize the Timestamp object
  @Test
  public void testReadTimestamp() throws IOException {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Timestamp.class);
    JsonReader in = new JsonReader(null);
    in.addToken(JsonToken.STRING, "\"2019-01-01 12:00:00\"");
    Timestamp timestamp = adapter.read(in);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Timestamp expectedTimestamp = new Timestamp(sdf.parse("2019-01-01 12:00:00").getTime());
    assertEquals(expectedTimestamp, timestamp);
  }

  // Test Case 6: read method should correctly deserialize the java.sql.Date object
  @Test
  public void testReadSQLDate() throws IOException {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(java.sql.Date.class);
    JsonReader in = new JsonReader(null);
    in.addToken(JsonToken.STRING, "\"2019-01-01\"");
    java.sql.Date sqlDate = adapter.read(in);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    java.sql.Date expectedSqlDate = new java.sql.Date(sdf.parse("2019-01-01").getTime());
    assertEquals(expectedSqlDate, sqlDate);
  }

  // Test Case 7: read method should throw AssertionError when an unknown date type is encountered
  @Test(expected = AssertionError.class)
  public void testReadUnknownDateType() throws IOException {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Object.class);
    JsonReader in = new JsonReader(null);
    in.addToken(JsonToken.STRING, "\"2019-01-01\"");
    adapter.read(in);
  }

  // Test Case 8: deserializeToDate method should correctly parse the input string
  @Test
  public void testDeserializeToDate() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    String input = "2019-01-01";
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Date expectedDate = sdf.parse(input);
    Date date = adapter.deserializeToDate(input);
    assertEquals(expectedDate, date);
  }

  // Test Case 9: deserializeToDate method should throw JsonSyntaxException when parsing fails
  @Test(expected = JsonSyntaxException.class)
  public void testDeserializeToDateInvalidInput() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    String input = "invalid";
    adapter.deserializeToDate(input);
  }

  // Test Case 10: toString method should return the expected string representation
  @Test
  public void testToString() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    String expectedString = "DefaultDateTypeAdapter(" + SimpleDateFormat.class.getSimpleName() + ")";
    assertEquals(expectedString, adapter.toString());
  }

}