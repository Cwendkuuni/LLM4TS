import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

public class DefaultDateTypeAdapterTest {

  private DefaultDateTypeAdapter adapter;

  @Before
  public void setUp() {
    adapter = new DefaultDateTypeAdapter(Date.class);
  }

  @Test
  public void testWrite_NullDate_ShouldWriteNullValue() {
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
    Date date = new Date();
    DateFormat format = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US);
    String expected = format.format(date);

    // Act
    adapter.write(writer, date);

    // Assert
    assertEquals(JsonToken.STRING, writer.peek());
    assertEquals(expected, writer.getPath());
  }

  @Test
  public void testRead_InvalidValue_ShouldThrowJsonParseException() {
    // Arrange
    JsonReader reader = new JsonReader();
    reader.beginArray();
    reader.value("invalid_date");
    reader.endArray();

    // Act & Assert
    assertThrows(JsonParseException.class, () -> adapter.read(reader));
  }

  @Test
  public void testRead_ValidDate_ShouldReturnCorrectDateType() {
    // Arrange
    JsonReader reader = new JsonReader();
    DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    String dateString = "2022-12-31T23:59:59.999Z";
    Date expected = format.parse(dateString);

    // Act
    Date result = adapter.read(reader);

    // Assert
    assertEquals(expected, result);
    assertTrue(result instanceof Date);
  }

  @Test
  public void testRead_ValidTimestamp_ShouldReturnTimestamp() {
    // Arrange
    JsonReader reader = new JsonReader();
    DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String timestampString = "2022-12-31 23:59:59";
    Date date = format.parse(timestampString);
    Timestamp expected = new Timestamp(date.getTime());

    // Act
    Timestamp result = adapter.read(reader);

    // Assert
    assertEquals(expected, result);
    assertTrue(result instanceof Timestamp);
  }

  @Test
  public void testRead_ValidSQLDate_ShouldReturnSQLDate() {
    // Arrange
    JsonReader reader = new JsonReader();
    DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    String dateString = "2022-12-31";
    Date date = format.parse(dateString);
    java.sql.Date expected = new java.sql.Date(date.getTime());

    // Act
    java.sql.Date result = adapter.read(reader);

    // Assert
    assertEquals(expected, result);
    assertTrue(result instanceof java.sql.Date);
  }

  @Test
  public void testToString_ShouldReturnClassNameAndFormatClassName() {
    // Arrange
    String expected = "DefaultDateTypeAdapter(SimpleDateFormat)";

    // Act
    String result = adapter.toString();

    // Assert
    assertEquals(expected, result);
  }
}