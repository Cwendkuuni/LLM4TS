import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DefaultDateTypeAdapterTest {

  private DefaultDateTypeAdapter adapter;

  @Before
  public void setup() {
    adapter = new DefaultDateTypeAdapter(Date.class);
  }

  @Test
  public void testWrite_NullValue() throws IOException {
    // Arrange
    JsonWriter writer = new JsonWriter();

    // Act
    adapter.write(writer, null);

    // Assert
    assertEquals("Expected null value to be written", "null", writer.toString());
  }

  @Test
  public void testWrite_DateValue() throws IOException {
    // Arrange
    JsonWriter writer = new JsonWriter();
    Date date = new Date();

    // Act
    adapter.write(writer, date);

    // Assert
    assertEquals("Expected date value to be written", date.toString(), writer.toString());
  }

  @Test(expected = JsonParseException.class)
  public void testRead_InvalidDate() throws IOException {
    // Arrange
    JsonReader reader = new JsonReader();
    reader.addToken(JsonToken.STRING);
    reader.addValue("invalid");

    // Act
    adapter.read(reader);

    // Assert
    // Expects JsonParseException to be thrown
  }

  @Test
  public void testRead_DateValue() throws IOException {
    // Arrange
    JsonReader reader = new JsonReader();
    reader.addToken(JsonToken.STRING);
    Date expectedDate = new Date();
    reader.addValue(expectedDate.toString());

    // Act
    Date actualDate = adapter.read(reader);

    // Assert
    assertEquals("Expected date value to be read correctly", expectedDate, actualDate);
  }

  @Test
  public void testRead_TimestampValue() throws IOException {
    // Arrange
    JsonReader reader = new JsonReader();
    reader.addToken(JsonToken.STRING);
    Date expectedDate = new Date();
    reader.addValue(expectedDate.toString());
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Timestamp.class);

    // Act
    Date actualDate = adapter.read(reader);

    // Assert
    assertEquals("Expected timestamp value to be read correctly", expectedDate.getTime(), actualDate.getTime());
  }

  @Test
  public void testRead_SQLDateValue() throws IOException {
    // Arrange
    JsonReader reader = new JsonReader();
    reader.addToken(JsonToken.STRING);
    Date expectedDate = new Date();
    reader.addValue(expectedDate.toString());
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(java.sql.Date.class);

    // Act
    Date actualDate = adapter.read(reader);

    // Assert
    assertEquals("Expected SQL date value to be read correctly", expectedDate.getTime(), actualDate.getTime());
  }

  @Test(expected = AssertionError.class)
  public void testRead_UnknownDateType() throws IOException {
    // Arrange
    JsonReader reader = new JsonReader();
    reader.addToken(JsonToken.STRING);
    reader.addValue(new Date().toString());
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Object.class);

    // Act
    adapter.read(reader);

    // Assert
    // Expects AssertionError to be thrown
  }

  @Test
  public void testToString() {
    // Arrange
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, DateFormat.DEFAULT, DateFormat.DEFAULT);
    String expectedToString = "DefaultDateTypeAdapter(SimpleDateFormat)";

    // Act
    String actualToString = adapter.toString();

    // Assert
    assertEquals("Expected toString to return correct value", expectedToString, actualToString);
  }
}