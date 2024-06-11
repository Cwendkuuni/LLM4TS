import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultDateTypeAdapterTest {

    private DefaultDateTypeAdapter defaultDateTypeAdapter;

    @Before
    public void setUp() {
        defaultDateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
    }

    @Test
    public void testWrite_NullDate_ShouldWriteNullValue() {
        // Arrange
        JsonWriter jsonWriter = new JsonWriter();
        Date date = null;

        // Act
        defaultDateTypeAdapter.write(jsonWriter, date);

        // Assert
        assertEquals(JsonToken.NULL, jsonWriter.peek());
    }

    @Test
    public void testWrite_ValidDate_ShouldWriteFormattedDate() {
        // Arrange
        JsonWriter jsonWriter = new JsonWriter();
        Date date = new Date(120, 10, 30, 13, 30, 45);

        // Act
        defaultDateTypeAdapter.write(jsonWriter, date);

        // Assert
        assertEquals("Nov 30, 2020 1:30:45 PM", jsonWriter.toString());
    }

    @Test
    public void testRead_StringValue_ShouldReturnDateObject() {
        // Arrange
        JsonReader jsonReader = new JsonReader();
        jsonReader.beginObject();
        jsonReader.name("date");
        jsonReader.value("Nov 30, 2020 1:30:45 PM");
        jsonReader.endObject();

        // Act
        Date date = defaultDateTypeAdapter.read(jsonReader);

        // Assert
        assertEquals(new Date(120, 10, 30, 13, 30, 45), date);
    }

    @Test(expected = JsonParseException.class)
    public void testRead_NonStringValue_ShouldThrowJsonParseException() {
        // Arrange
        JsonReader jsonReader = new JsonReader();
        jsonReader.beginObject();
        jsonReader.name("date");
        jsonReader.value(12345678);
        jsonReader.endObject();

        // Act
        Date date = defaultDateTypeAdapter.read(jsonReader);
    }

    @Test
    public void testDeserializeToDate_ValidString_ShouldReturnDateObject() {
        // Arrange
        String dateString = "Nov 30, 2020 1:30:45 PM";

        // Act
        Date date = defaultDateTypeAdapter.deserializeToDate(dateString);

        // Assert
        assertEquals(new Date(120, 10, 30, 13, 30, 45), date);
    }

    @Test(expected = JsonSyntaxException.class)
    public void testDeserializeToDate_InvalidString_ShouldThrowJsonSyntaxException() {
        // Arrange
        String dateString = "Invalid Date String";

        // Act
        Date date = defaultDateTypeAdapter.deserializeToDate(dateString);
    }

}