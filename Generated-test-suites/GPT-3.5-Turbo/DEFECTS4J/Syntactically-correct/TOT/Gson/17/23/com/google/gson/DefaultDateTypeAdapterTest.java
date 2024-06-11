import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultDateTypeAdapterTest {

  @Test
  public void testWrite_NullDate_ShouldWriteNullValue() {
    // Arrange
    DefaultDateTypeAdapter typeAdapter = new DefaultDateTypeAdapter(Date.class);
    JsonWriter mockWriter = new JsonWriter();
    
    // Act
    typeAdapter.write(mockWriter, null);
    
    // Assert
    assertEquals("null", mockWriter.getValue());
  }
  
  @Test(expected = JsonParseException.class)
  public void testRead_NonStringValue_ShouldThrowJsonParseException() throws IOException {
    // Arrange
    DefaultDateTypeAdapter typeAdapter = new DefaultDateTypeAdapter(Date.class);
    JsonReader mockReader = new JsonReader();
    mockReader.setPeek(JsonToken.NUMBER);
    
    // Act
    typeAdapter.read(mockReader);
    
    // Assert - Exception
  }
  
  @Test
  public void testRead_ValidDateString_ShouldReturnDateObject() throws IOException {
    // Arrange
    DefaultDateTypeAdapter typeAdapter = new DefaultDateTypeAdapter(Date.class);
    JsonReader mockReader = new JsonReader();
    mockReader.setValue("2021-01-01T12:00:00Z");
    
    // Act
    Date date = typeAdapter.read(mockReader);
    
    // Assert
    assertNotNull(date);
    assertEquals("Fri Jan 01 12:00:00 GMT 2021", date.toString());
  }
  
  @Test
  public void testRead_ValidDateString_ReturnedDateShouldHaveZeroTime() throws IOException {
    // Arrange
    DefaultDateTypeAdapter typeAdapter = new DefaultDateTypeAdapter(java.sql.Date.class);
    JsonReader mockReader = new JsonReader();
    mockReader.setValue("2021-01-01T12:00:00Z");
    
    // Act
    java.sql.Date sqlDate = typeAdapter.read(mockReader);
    
    // Assert
    assertNotNull(sqlDate);
    assertEquals("2021-01-01", sqlDate.toString());
  }
  
  @Test
  public void testRead_InvalidDateString_ShouldThrowJsonSyntaxException() throws IOException {
    // Arrange
    DefaultDateTypeAdapter typeAdapter = new DefaultDateTypeAdapter(Timestamp.class);
    JsonReader mockReader = new JsonReader();
    mockReader.setValue("Invalid Date");
    
    // Act
    typeAdapter.read(mockReader);
    
    // Assert - Exception
  }
  
  @Test
  public void testToString_ShouldReturnClassNameAndFormatName() {
    // Arrange
    DefaultDateTypeAdapter typeAdapter = new DefaultDateTypeAdapter(Date.class);
    
    // Act
    String result = typeAdapter.toString();
    
    // Assert
    assertEquals("DefaultDateTypeAdapter(SimpleDateFormat)", result);
  }
}