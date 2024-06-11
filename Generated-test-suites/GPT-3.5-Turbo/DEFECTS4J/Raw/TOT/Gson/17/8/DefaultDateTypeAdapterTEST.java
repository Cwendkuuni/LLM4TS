import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultDateTypeAdapterTest {

  @Test
  public void testWrite_NullDate_WritesNullValue() {
    // Arrange
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);

    // Act
    String result = adapter.write(null); 

    // Assert
    assertNull(result);
  }

  @Test
  public void testWrite_ValidDate_WritesFormattedString() {
    // Arrange
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    Date date = new Date(2022, 10, 25);

    // Act
    String result = adapter.write(date);

    // Assert
    assertNotNull(result);
    assertEquals("Oct 25, 2022 12:00:00 AM", result);
  }

  @Test
  public void testRead_InvalidDateType_ThrowsJsonParseException() {
    // Arrange
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonReader reader = new JsonReader("\"2022-10-25T00:00:00Z\"");

    // Act & Assert
    assertThrows(JsonParseException.class, () -> {
      adapter.read(reader);
    });
  }

  @Test
  public void testRead_ValidDate_ReturnsDateObject() {
    // Arrange
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonReader reader = new JsonReader("\"2022-10-25T00:00:00Z\"");

    // Act
    Date result = adapter.read(reader);

    // Assert
    assertNotNull(result);
    assertEquals(new Date(2022, 10, 25), result);
  }

  @Test
  public void testRead_ValidDate_ReturnsTimestampObject() {
    // Arrange
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Timestamp.class);
    JsonReader reader = new JsonReader("\"2022-10-25T00:00:00Z\"");

    // Act
    Timestamp result = adapter.read(reader);

    // Assert
    assertNotNull(result);
    assertEquals(new Timestamp(new Date(2022, 10, 25).getTime()), result);
  }

  @Test
  public void testRead_ValidDate_ReturnsSqlDateObject() {
    // Arrange
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(java.sql.Date.class);
    JsonReader reader = new JsonReader("\"2022-10-25T00:00:00Z\"");

    // Act
    java.sql.Date result = adapter.read(reader);

    // Assert
    assertNotNull(result);
    assertEquals(new java.sql.Date(new Date(2022, 10, 25).getTime()), result);
  }

  @Test
  public void testToString_ReturnsCorrectStringRepresentation() {
    // Arrange
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Timestamp.class);

    // Act
    String result = adapter.toString();

    // Assert
    assertEquals("DefaultDateTypeAdapter(SimpleDateFormat)", result);
  }
}