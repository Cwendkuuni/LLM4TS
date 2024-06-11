
import static org.junit.Assert.*;
import org.junit.Test;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public class DefaultDateTypeAdapterTest {

  @Test
  public void testWrite_NullDate_NullValue() throws IOException {
    // Arrange
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonWriter writer = new JsonWriter();

    // Act
    adapter.write(writer, null);

    // Assert
    assertEquals(JsonToken.NULL, writer.peek());
  }

  @Test
  public void testWrite_NonNullDate_ValueIsFormattedString() throws IOException {
    // Arrange
    Date date = new Date();
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonWriter writer = new JsonWriter();

    // Act
    adapter.write(writer, date);

    // Assert
    String formattedDate = writer.toString();
    assertEquals(date.toString(), formattedDate);
  }

  @Test
  public void testRead_InvalidJson_ThrowsJsonParseException() throws IOException {
    // Arrange
    String invalidJson = "{";

    // Act
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonReader reader = new JsonReader(invalidJson);

    // Assert
    assertThrows(JsonParseException.class, () -> adapter.read(reader));
  }

  @Test
  public void testRead_ValidJson_ReturnsDate() throws IOException {
    // Arrange
    String json = "\"2022-01-01\"";  // Assuming valid JSON date string
    Date expectedDate = new SimpleDateFormat("yyyy-MM-dd").parse("2022-01-01");

    // Act
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonReader reader = new JsonReader(json);
    Date actualDate = adapter.read(reader);

    // Assert
    assertEquals(expectedDate, actualDate);
  }

  @Test
  public void testRead_InvalidDate_ThrowsJsonSyntaxException() throws IOException {
    // Arrange
    String invalidDate = "not-a-date";

    // Act
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    JsonReader reader = new JsonReader(invalidDate);

    // Assert
    assertThrows(JsonSyntaxException.class, () -> adapter.read(reader));
  }
}

