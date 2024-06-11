import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import org.mockito.Mockito;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class DefaultDateTypeAdapterTest {
  
  @Test
  public void testWrite_NullValue_ShouldWriteNullValueToOutput() {
    JsonWriter writer = mock(JsonWriter.class);
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    
    adapter.write(writer, null);
    
    verify(writer).nullValue();
  }
  
  @Test
  public void testWrite_ValidDate_ShouldWriteFormattedStringToOutput() {
    JsonWriter writer = mock(JsonWriter.class);
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    Date date = new Date(1619544000000L); // May 28, 2021 12:00:00 AM UTC
    
    adapter.write(writer, date);
    
    verify(writer).value("May 28, 2021 12:00:00 AM");
  }
  
  @Test
  public void testRead_NonStringToken_ShouldThrowJsonParseException() throws IOException {
    JsonReader reader = mock(JsonReader.class);
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    when(reader.peek()).thenReturn(JsonToken.BOOLEAN);
    
    assertThrows(JsonParseException.class, () -> adapter.read(reader));
  }
  
  @Test
  public void testRead_ValidDateString_ShouldReturnCorrectDateObject() throws IOException {
    JsonReader reader = mock(JsonReader.class);
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    when(reader.peek()).thenReturn(JsonToken.STRING);
    
    Date result = adapter.read(reader);
    
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Date expected = sdf.parse("2021-05-28");
    assertEquals(expected, result);
  }
  
  @Test
  public void testRead_ValidDateString_TimestampType_ShouldReturnCorrectTimestampObject() throws IOException {
    JsonReader reader = mock(JsonReader.class);
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Timestamp.class);
    when(reader.peek()).thenReturn(JsonToken.STRING);
    
    Date result = adapter.read(reader);
    
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Date expected = sdf.parse("2021-05-28");
    Timestamp expectedTimestamp = new Timestamp(expected.getTime());
    assertEquals(expectedTimestamp, result);
  }
  
  @Test
  public void testRead_ValidDateString_SQLDateType_ShouldReturnCorrectSQLDateObject() throws IOException {
    JsonReader reader = mock(JsonReader.class);
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(java.sql.Date.class);
    when(reader.peek()).thenReturn(JsonToken.STRING);
    
    Date result = adapter.read(reader);
    
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Date expected = sdf.parse("2021-05-28");
    java.sql.Date expectedSQLDate = new java.sql.Date(expected.getTime());
    assertEquals(expectedSQLDate, result);
  }
  
  @Test
  public void testRead_InvalidDateString_ShouldThrowJsonSyntaxException() throws IOException {
    JsonReader reader = mock(JsonReader.class);
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    when(reader.peek()).thenReturn(JsonToken.STRING);
    when(reader.nextString()).thenReturn("2021-13-01");
    
    assertThrows(JsonSyntaxException.class, () -> adapter.read(reader));
  }
  
  @Test
  public void testRead_InvalidDateString_WithParseException_ShouldThrowJsonSyntaxException() throws IOException {
    JsonReader reader = mock(JsonReader.class);
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    when(reader.peek()).thenReturn(JsonToken.STRING);
    when(reader.nextString()).thenReturn("2021-05-28");
    doThrow(ParseException.class).when(adapter).deserializeToDate(anyString());
    
    assertThrows(JsonSyntaxException.class, () -> adapter.read(reader));
  }
  
  @Test
  public void testToString_ShouldReturnClassNameAndDateFormat() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(java.sql.Date.class);
    
    String result = adapter.toString();
    
    assertEquals("DefaultDateTypeAdapter(SimpleDateFormat)", result);
  }
}