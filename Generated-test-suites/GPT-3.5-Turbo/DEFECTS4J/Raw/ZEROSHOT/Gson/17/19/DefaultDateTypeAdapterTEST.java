import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultDateTypeAdapterTest {

  @Test
  public void testWriteNullValue() {
    // Given
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonWriter writer = new JsonWriter();
    
    // When
    adapter.write(writer, null);
    
    // Then
    assertNull(writer.getValue());
  }
  
  @Test
  public void testWriteDateValue() {
    // Given
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonWriter writer = new JsonWriter();
    Date date = new Date();
    
    // When
    adapter.write(writer, date);
    
    // Then
    assertEquals(date, writer.getValue());
  }
  
  @Test
  public void testWriteTimestampValue() {
    // Given
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Timestamp.class);
    JsonWriter writer = new JsonWriter();
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    
    // When
    adapter.write(writer, timestamp);
    
    // Then
    assertEquals(timestamp, writer.getValue());
  }
  
  @Test
  public void testWriteSqlDateValue() {
    // Given
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(java.sql.Date.class);
    JsonWriter writer = new JsonWriter();
    java.sql.Date sqlDate = new java.sql.Date(System.currentTimeMillis());
    
    // When
    adapter.write(writer, sqlDate);
    
    // Then
    assertEquals(sqlDate, writer.getValue());
  }
  
  @Test
  public void testReadStringDateValue() {
    // Given
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonReader reader = new JsonReader("2021-03-20T12:00:00");
    
    // When
    Date date = adapter.read(reader);
    
    // Then
    assertNotNull(date);
  }
  
  @Test
  public void testReadStringTimestampValue() {
    // Given
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Timestamp.class);
    JsonReader reader = new JsonReader("2021-03-20T12:00:00");
    
    // When
    Timestamp timestamp = adapter.read(reader);
    
    // Then
    assertNotNull(timestamp);
  }
  
  @Test
  public void testReadStringSqlDateValue() {
    // Given
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(java.sql.Date.class);
    JsonReader reader = new JsonReader("2021-03-20T12:00:00");
    
    // When
    java.sql.Date sqlDate = adapter.read(reader);
    
    // Then
    assertNotNull(sqlDate);
  }
  
  @Test(expected = JsonParseException.class)
  public void testReadNonStringValue() {
    // Given
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonReader reader = new JsonReader(123);
    
    // When
    adapter.read(reader);
  }
  
  @Test
  public void testDeserializeToDate() {
    // Given
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    String dateString = "2021-03-20T12:00:00";
    
    // When
    Date date = adapter.deserializeToDate(dateString);
    
    // Then
    assertNotNull(date);
  }
  
  @Test
  public void testToString() {
    // Given
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    
    // When
    String toString = adapter.toString();
    
    // Then
    assertEquals("DefaultDateTypeAdapter(SimpleDateFormat)", toString);
  }
}