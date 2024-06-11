import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultDateTypeAdapterTest {

  @Test
  public void testWrite() {
    // Test case 1: value is null
    // Arrange
    JsonWriter out = new JsonWriter();
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    
    // Act
    adapter.write(out, null);
    
    // Assert
    assertNull(out.getValue());
    
    // Test case 2: value is not null
    // Arrange
    out = new JsonWriter();
    Date date = new Date();
    
    // Act
    adapter.write(out, date);
    
    // Assert
    assertEquals(date, out.getValue());
  }

  @Test
  public void testRead() {
    // Test case 1: peek returns JsonToken.STRING
    // Arrange
    JsonReader in = new JsonReader();
    in.setPeek(JsonToken.STRING);
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    
    // Act and Assert
    try {
      adapter.read(in);
      fail("Expected JsonParseException to be thrown");
    } catch (IOException e) {
      assertEquals("The date should be a string value", e.getMessage());
    }
    
    // Test case 2: peek returns a non-string token
    // Arrange
    in = new JsonReader();
    in.setPeek(JsonToken.NUMBER);
    
    // Act and Assert
    try {
      adapter.read(in);
      fail("Expected JsonParseException to be thrown");
    } catch (IOException e) {
      assertEquals("The date should be a string value", e.getMessage());
    }
    
    // Test case 3: dateType is Date
    // Arrange
    in = new JsonReader();
    in.setPeek(JsonToken.STRING);
    in.setNextString("2021-01-01");
    adapter = new DefaultDateTypeAdapter(Date.class);
    
    // Act
    Date result = adapter.read(in);
    
    // Assert
    assertEquals(new SimpleDateFormat("yyyy-MM-dd").parse("2021-01-01"), result);
    
    // Test case 4: dateType is Timestamp
    // Arrange
    in = new JsonReader();
    in.setPeek(JsonToken.STRING);
    in.setNextString("2021-01-01");
    adapter = new DefaultDateTypeAdapter(Timestamp.class);
    
    // Act
    result = adapter.read(in);
    
    // Assert
    assertEquals(new Timestamp(new SimpleDateFormat("yyyy-MM-dd").parse("2021-01-01").getTime()), result);
    
    // Test case 5: dateType is java.sql.Date
    // Arrange
    in = new JsonReader();
    in.setPeek(JsonToken.STRING);
    in.setNextString("2021-01-01");
    adapter = new DefaultDateTypeAdapter(java.sql.Date.class);
    
    // Act
    result = adapter.read(in);
    
    // Assert
    assertEquals(new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse("2021-01-01").getTime()), result);
  }
}