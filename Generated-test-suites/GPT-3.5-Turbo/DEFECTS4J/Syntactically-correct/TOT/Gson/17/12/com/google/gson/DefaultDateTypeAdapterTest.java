import static org.junit.Assert.*;

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
  public void testWrite_NullValue_ShouldReturnNullValue() {
    // Arrange
    JsonWriter writer = new JsonWriter();
    
    // Act
    adapter.write(writer, null);
    
    // Assert
    assertEquals(JsonToken.NULL, writer.peek());
  }
  
  @Test
  public void testWrite_ValidDate_ShouldWriteFormattedDate() {
    // Arrange
    JsonWriter writer = new JsonWriter();
    Date date = new Date(1622712220000L); // June 3, 2021 12:30:20 PM
    
    // Act
    adapter.write(writer, date);
    
    // Assert
    assertEquals(JsonToken.STRING, writer.peek());
    assertEquals("Jun 3, 2021 12:30:20 PM", writer.nextString());
  }
  
  @Test(expected = JsonParseException.class)
  public void testRead_InvalidDateType_ShouldThrowJsonParseException() throws IOException {
    // Arrange
    JsonReader reader = new JsonReader();
    reader.beginObject();
    
    // Act
    adapter.read(reader);
    
    // Assert (Exception expected)
  }
  
  @Test
  public void testRead_ValidDateString_ShouldReturnDateObject() throws IOException {
    // Arrange
    JsonReader reader = new JsonReader();
    reader.beginObject();
    reader.name("date").value("Jun 3, 2021 12:30:20 PM");
    reader.endObject();
    
    // Act
    Date date = adapter.read(reader);
    
    // Assert
    assertEquals(new Date(1622712220000L), date);
  }
  
  @Test(expected = JsonSyntaxException.class)
  public void testRead_InvalidDateString_ShouldThrowJsonSyntaxException() throws IOException {
    // Arrange
    JsonReader reader = new JsonReader();
    reader.beginObject();
    reader.name("date").value("InvalidDate");
    reader.endObject();
    
    // Act
    adapter.read(reader);
    
    // Assert (Exception expected)
  }
}