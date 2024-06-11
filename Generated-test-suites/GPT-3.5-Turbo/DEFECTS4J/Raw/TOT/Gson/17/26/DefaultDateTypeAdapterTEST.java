import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultDateTypeAdapterTest {

  private DefaultDateTypeAdapter adapter;

  @Before
  public void setup() {
    adapter = new DefaultDateTypeAdapter(Date.class);
  }

  @Test
  public void write_NullDate_ShouldWriteNullJson() {
    // Arrange
    JsonWriter mockWriter = new JsonWriter();

    // Act
    adapter.write(mockWriter, null);

    // Assert
    assertEquals(JsonToken.NULL, mockWriter.getToken());
  }

  @Test
  public void write_ValidDate_ShouldWriteCorrectJsonFormat() {
    // Arrange
    JsonWriter mockWriter = new JsonWriter();
    Date date = new Date(1234567890); // Unix timestamp

    // Act
    adapter.write(mockWriter, date);

    // Assert
    assertEquals(JsonToken.STRING, mockWriter.getToken());
    assertEquals("1970-01-15 06:56:07", mockWriter.getValue());
  }

  @Test
  public void read_InvalidDataType_ShouldThrowJsonParseException() {
    // Arrange
    JsonReader mockReader = new JsonReader();
    mockReader.addToken(JsonToken.NUMBER); // Simulate invalid data type input

    // Act & Assert
    assertThrows(JsonParseException.class, () -> {
      adapter.read(mockReader);
    });
  }

  @Test
  public void read_ValidDateString_ShouldReturnValidDateObject() {
    // Arrange
    JsonReader mockReader = new JsonReader();
    mockReader.addToken(JsonToken.STRING);
    mockReader.setValue("2000-01-01 00:00:00"); // Simulate valid date string

    // Act
    Date result = adapter.read(mockReader);

    // Assert
    assertNotNull(result);
    assertEquals(946684800000L, result.getTime());
  }

  @Test
  public void read_InvalidDateString_ShouldThrowJsonSyntaxException() {
    // Arrange
    JsonReader mockReader = new JsonReader();
    mockReader.addToken(JsonToken.STRING);
    mockReader.setValue("invalid"); // Simulate invalid date string

    // Act & Assert
    assertThrows(JsonSyntaxException.class, () -> {
      adapter.read(mockReader);
    });
  }

  @Test
  public void read_TimestampClass_ShouldReturnTimestampObject() {
    // Arrange
    DefaultDateTypeAdapter timestampAdapter = new DefaultDateTypeAdapter(Timestamp.class);
    JsonReader mockReader = new JsonReader();
    mockReader.addToken(JsonToken.STRING);
    mockReader.setValue("2000-01-01 00:00:00");

    // Act
    Date result = timestampAdapter.read(mockReader);

    // Assert
    assertNotNull(result);
    assertTrue(result instanceof Timestamp);
    assertEquals(946684800000L, result.getTime());
  }

  @Test
  public void read_SqlDateClass_ShouldReturnSqlDateObject() {
    // Arrange
    DefaultDateTypeAdapter sqlDateAdapter = new DefaultDateTypeAdapter(java.sql.Date.class);
    JsonReader mockReader = new JsonReader();
    mockReader.addToken(JsonToken.STRING);
    mockReader.setValue("2000-01-01 00:00:00");

    // Act
    Date result = sqlDateAdapter.read(mockReader);

    // Assert
    assertNotNull(result);
    assertTrue(result instanceof java.sql.Date);
    assertEquals(946684800000L, result.getTime());
  }

}