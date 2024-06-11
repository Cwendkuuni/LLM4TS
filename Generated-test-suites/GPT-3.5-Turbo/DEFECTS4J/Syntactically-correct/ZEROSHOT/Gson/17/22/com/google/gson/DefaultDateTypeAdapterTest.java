import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultDateTypeAdapterTest {

    @Test
    public void write_WhenValueIsNull_ShouldWriteNullValueToWriter() {
        // Arrange
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);

        // Act
        JsonWriter writer = new JsonWriter();
        adapter.write(writer, null);

        // Assert
        assertEquals(JsonToken.NULL, writer.peek());
    }

    @Test
    public void write_WhenValueIsNotNull_ShouldWriteFormattedDateToWriter() {
        // Arrange
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        Date date = new Date();

        // Act
        JsonWriter writer = new JsonWriter();
        adapter.write(writer, date);

        // Assert
        assertEquals(JsonToken.STRING, writer.peek());
    }

    @Test(expected = JsonParseException.class)
    public void read_WhenNextTokenIsNotString_ShouldThrowJsonParseException() throws IOException {
        // Arrange
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        JsonReader reader = new JsonReader();
        reader.beginArray();

        // Act
        adapter.read(reader);

        // Assert (Expected Exception)
    }

    @Test
    public void read_WhenValidDateString_ShouldParseAndReturnDateObject() throws IOException {
        // Arrange
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        String dateString = "2022-01-01T12:00:00Z";
        JsonReader reader = new JsonReader();
        reader.beginArray();
        reader.value(dateString);
        reader.endArray();

        // Act
        Date date = adapter.read(reader);

        // Assert
        assertNotNull(date);
    }

    @Test
    public void read_WhenDateTypeIsDate_ShouldReturnDateObject() throws IOException {
        // Arrange
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        String dateString = "2022-01-01T12:00:00Z";
        JsonReader reader = new JsonReader();
        reader.beginArray();
        reader.value(dateString);
        reader.endArray();

        // Act
        Date date = adapter.read(reader);

        // Assert
        assertTrue(date instanceof Date);
    }

    @Test
    public void read_WhenDateTypeIsTimestamp_ShouldReturnTimestampObject() throws IOException {
        // Arrange
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Timestamp.class);
        String dateString = "2022-01-01T12:00:00Z";
        JsonReader reader = new JsonReader();
        reader.beginArray();
        reader.value(dateString);
        reader.endArray();

        // Act
        Date date = adapter.read(reader);

        // Assert
        assertTrue(date instanceof Timestamp);
    }

    @Test
    public void read_WhenDateTypeIsSqlDate_ShouldReturnSqlDateObject() throws IOException {
        // Arrange
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(java.sql.Date.class);
        String dateString = "2022-01-01T12:00:00Z";
        JsonReader reader = new JsonReader();
        reader.beginArray();
        reader.value(dateString);
        reader.endArray();

        // Act
        Date date = adapter.read(reader);

        // Assert
        assertTrue(date instanceof java.sql.Date);
    }
}