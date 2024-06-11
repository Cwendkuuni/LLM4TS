import static org.junit.Assert.*;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DefaultDateTypeAdapterTest {
  
  private DefaultDateTypeAdapter adapter;
  
  @Before
  public void setUp() {
    adapter = new DefaultDateTypeAdapter(Date.class);
  }
  
  @After
  public void tearDown() {
    adapter = null;
  }
  
  @Test
  public void testWrite_NullDate_WritesNullValue() throws IOException {
    JsonWriterMock writer = new JsonWriterMock();
    
    adapter.write(writer, null);
    
    assertEquals(JsonToken.NULL, writer.peek());
  }
  
  @Test
  public void testWrite_NonNullDate_WritesDateAsString() throws IOException {
    JsonWriterMock writer = new JsonWriterMock();
    Date date = new Date();
    
    adapter.write(writer, date);
    
    assertEquals(JsonToken.STRING, writer.peek());
    assertNotNull(writer.value);
  }
  
  @Test(expected = JsonParseException.class)
  public void testRead_InvalidJson_ThrowsJsonParseException() throws IOException {
    JsonReaderMock reader = new JsonReaderMock("\"invalidDate\"");
    
    adapter.read(reader);
  }
  
  @Test
  public void testRead_ValidDateString_ReturnsParsedDate() throws IOException {
    String dateString = "2020-01-01T12:00:00Z";
    JsonReaderMock reader = new JsonReaderMock("\"" + dateString + "\"");
    
    Date result = adapter.read(reader);
    
    assertNotNull(result);
    assertEquals(dateString, result.toInstant().toString());
  }
  
  @Test
  public void testRead_ValidTimestampString_ReturnsParsedTimestamp() throws IOException {
    String timestampString = "2020-01-01T12:00:00Z";
    JsonReaderMock reader = new JsonReaderMock("\"" + timestampString + "\"");
    adapter = new DefaultDateTypeAdapter(Timestamp.class);
    
    Date result = adapter.read(reader);
    
    assertNotNull(result);
    assertEquals(timestampString, result.toInstant().toString());
  }
  
  @Test
  public void testRead_ValidSQLDateString_ReturnsParsedSQLDate() throws IOException {
    String sqlDateString = "2020-01-01";
    JsonReaderMock reader = new JsonReaderMock("\"" + sqlDateString + "\"");
    adapter = new DefaultDateTypeAdapter(java.sql.Date.class);
    
    Date result = adapter.read(reader);
    
    assertNotNull(result);
    assertEquals(sqlDateString, result.toInstant().toString());
  }
}