import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.junit.Test;

import com.google.gson.DefaultDateTypeAdapter;
import com.google.gson.JsonParseException;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public class DefaultDateTypeAdapterTest {

  @Test
  public void writeNullDate() throws IOException {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonWriter writer = new JsonWriter(null);
    
    adapter.write(writer, null);
    writer.close();
    
    // Validate JSON output
    assertEquals("null", writer.toString());
  }

  @Test
  public void writeNonNullDate() throws IOException {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonWriter writer = new JsonWriter(null);
    Date date = new Date();
    
    adapter.write(writer, date);
    writer.close();
    
    // Validate JSON output
    assertNotNull(writer.toString());
  }

  @Test
  public void readValidDateString() throws IOException {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonReader reader = new JsonReader(null);
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    String dateStr = "2022-01-01";
    
    reader.beginObject();
    reader.nextName();
    reader.nextString(dateStr);
    reader.endObject();
    
    Date result = adapter.read(reader);
    
    // Validate date object
    Date expectedDate = dateFormat.parse(dateStr);
    assertEquals(expectedDate, result);
  }

  @Test(expected = JsonParseException.class)
  public void readInvalidDateString() throws IOException {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonReader reader = new JsonReader(null);
    
    reader.beginObject();
    reader.nextName();
    reader.nextString("2022-01"); // Invalid date string
    reader.endObject();
    
    adapter.read(reader); // Should throw JsonParseException
  }

  @Test
  public void deserializeToLocalFormat() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    SimpleDateFormat localFormat = new SimpleDateFormat("MM/dd/yyyy");
    String dateStr = "01/01/2022";
    
    Date result = adapter.deserializeToDate(dateStr);
    
    // Validate date object
    Date expectedDate = localFormat.parse(dateStr);
    assertEquals(expectedDate, result);
  }

  @Test(expected = JsonParseException.class)
  public void readInvalidDateFormat() throws IOException {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonReader reader = new JsonReader(null);
    
    reader.beginObject();
    reader.nextName();
    reader.nextString("2022-01-01T12:00:00Z"); // Invalid date format
    reader.endObject();
    
    adapter.read(reader); // Should throw JsonParseException
  }

  @Test
  public void edgeCaseTimestamp() throws IOException {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Timestamp.class);
    JsonWriter writer = new JsonWriter(null);
    Date date = new Date();
    Timestamp timestamp = new Timestamp(date.getTime());
    
    adapter.write(writer, timestamp);
    writer.close();
    
    // Validate JSON output for Timestamp
    assertNotNull(writer.toString());
  }

  @Test
  public void edgeCaseSqlDate() throws IOException {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(java.sql.Date.class);
    JsonWriter writer = new JsonWriter(null);
    Date date = new Date();
    java.sql.Date sqlDate = new java.sql.Date(date.getTime());
    
    adapter.write(writer, sqlDate);
    writer.close();
    
    // Validate JSON output for java.sql.Date
    assertNotNull(writer.toString());
  }

}