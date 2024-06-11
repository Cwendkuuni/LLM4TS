
import org.junit.Test;
import static org.junit.Assert.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.Timestamp;
import java.util.Locale;

public class DefaultDateTypeAdapterTest {

  // Test write() method
  @Test
  public void testWrite() throws Exception {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonWriter writer = new JsonWriter(new StringWriter());
    Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2021-01-01");
    
    adapter.write(writer, date);
    
    assertEquals("\"2021-01-01\"", writer.toString());
  }
  
  // Test read() method
  @Test
  public void testRead() throws Exception {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonReader reader = new JsonReader(new StringReader("\"2021-01-01\""));
    
    Date date = adapter.read(reader);
    
    Date expectedDate = new SimpleDateFormat("yyyy-MM-dd").parse("2021-01-01");
    assertEquals(expectedDate, date);
  }
  
  // Test read() method with invalid date format
  @Test(expected = JsonParseException.class)
  public void testReadInvalidDateFormat() throws Exception {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonReader reader = new JsonReader(new StringReader("\"2021-01-01T00:00:00Z\""));
    
    adapter.read(reader);
  }
  
  // Test read() method with null value
  @Test
  public void testReadNull() throws Exception {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonReader reader = new JsonReader(new StringReader("null"));
    
    Date date = adapter.read(reader);
    
    assertNull(date);
  }
  
  // Test write() method with Timestamp type
  @Test
  public void testWriteTimestamp() throws Exception {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Timestamp.class);
    JsonWriter writer = new JsonWriter(new StringWriter());
    Timestamp timestamp = Timestamp.valueOf("2021-01-01 00:00:00");
    
    adapter.write(writer, timestamp);
    
    assertEquals("\"2021-01-01T00:00:00\"", writer.toString());
  }
  
  // Test read() method with Timestamp type
  @Test
  public void testReadTimestamp() throws Exception {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Timestamp.class);
    JsonReader reader = new JsonReader(new StringReader("\"2021-01-01T00:00:00\""));
    
    Timestamp timestamp = adapter.read(reader);
    
    Timestamp expectedTimestamp = Timestamp.valueOf("2021-01-01 00:00:00");
    assertEquals(expectedTimestamp, timestamp);
  }
  
  // Test write() method with java.sql.Date type
  @Test
  public void testWriteSqlDate() throws Exception {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(java.sql.Date.class);
    JsonWriter writer = new JsonWriter(new StringWriter());
    java.sql.Date sqlDate = java.sql.Date.valueOf("2021-01-01");
    
    adapter.write(writer, sqlDate);
    
    assertEquals("\"2021-01-01\"", writer.toString());
  }
  
  // Test read() method with java.sql.Date type
  @Test
  public void testReadSqlDate() throws Exception {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(java.sql.Date.class);
    JsonReader reader = new JsonReader(new StringReader("\"2021-01-01\""));
    
    java.sql.Date sqlDate = adapter.read(reader);
    
    java.sql.Date expectedSqlDate = java.sql.Date.valueOf("2021-01-01");
    assertEquals(expectedSqlDate, sqlDate);
  }
 }
