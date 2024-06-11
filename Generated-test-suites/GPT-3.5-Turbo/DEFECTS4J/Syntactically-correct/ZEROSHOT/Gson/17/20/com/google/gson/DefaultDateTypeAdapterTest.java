import org.junit.Test;
import static org.junit.Assert.*;
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
  
  @Test
  public void testWrite_NullValue() throws IOException {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonWriter writer = new JsonWriter(); // Replace with actual JsonWriter object
    Date value = null;
    adapter.write(writer, value);
    String expected = "null";
    String actual = writer.toString();
    assertEquals(expected, actual);
  }
  
  @Test
  public void testWrite_NonNullValue() throws IOException {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonWriter writer = new JsonWriter(); // Replace with actual JsonWriter object
    Date value = new Date();
    adapter.write(writer, value);
    String expected = value.toString();
    String actual = writer.toString();
    assertEquals(expected, actual);
  }
  
  @Test
  public void testRead_InvalidDataType() throws IOException {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonReader reader = new JsonReader(); // Replace with actual JsonReader object
    reader.beginObject();
    try {
      Date value = adapter.read(reader);
      fail("Expected JsonParseException to be thrown");
    } catch (JsonParseException e) {
      // Test passed
    }
  }
  
  @Test
  public void testRead_ValidDataType_Date() throws IOException {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonReader reader = new JsonReader(); // Replace with actual JsonReader object
    reader.beginObject();
    reader.nextString("2020-04-01");
    Date expected = new SimpleDateFormat("yyyy-MM-dd").parse("2020-04-01");
    Date actual = adapter.read(reader);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testRead_ValidDataType_Timestamp() throws IOException {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Timestamp.class);
    JsonReader reader = new JsonReader(); // Replace with actual JsonReader object
    reader.beginObject();
    reader.nextString("2020-04-01 12:00:00");
    Timestamp expected = new Timestamp(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2020-04-01 12:00:00").getTime());
    Date actual = adapter.read(reader);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testRead_ValidDataType_JavaSqlDate() throws IOException {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(java.sql.Date.class);
    JsonReader reader = new JsonReader(); // Replace with actual JsonReader object
    reader.beginObject();
    reader.nextString("2020-04-01");
    java.sql.Date expected = new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse("2020-04-01").getTime());
    Date actual = adapter.read(reader);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testToString() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    String expected = "DefaultDateTypeAdapter(SimpleDateFormat)";
    String actual = adapter.toString();
    assertEquals(expected, actual);
  }
}