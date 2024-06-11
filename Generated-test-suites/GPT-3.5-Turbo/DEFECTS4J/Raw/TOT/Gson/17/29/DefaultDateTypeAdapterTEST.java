import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class DefaultDateTypeAdapterTest {

    private DefaultDateTypeAdapter adapter;

    @Before
    public void setUp() {
        adapter = new DefaultDateTypeAdapter(Date.class);
    }

    @Test
    public void testWrite_NullValue() {
        // Arrange
        JsonWriter writer = new JsonWriter();
        Date date = null;

        // Act
        adapter.write(writer, date);

        // Assert
        assertNull(writer.getBuffer());
        assertEquals(JsonToken.NULL, writer.getTokens().get(0));
    }

    @Test
    public void testWrite_ValidDate() {
        // Arrange
        JsonWriter writer = new JsonWriter();
        Date date = new Date();

        // Act
        adapter.write(writer, date);

        // Assert
        assertNotNull(writer.getBuffer());
        assertEquals(JsonToken.STRING, writer.getTokens().get(0));
    }

    @Test(expected = JsonParseException.class)
    public void testRead_InvalidValueType() {
        // Arrange
        JsonReader reader = new JsonReader();
        reader.addToken(JsonToken.BOOLEAN);
        reader.addToken(JsonToken.NULL);

        // Act
        adapter.read(reader);
    }

    @Test
    public void testRead_ValidDateString() {
        // Arrange
        JsonReader reader = new JsonReader();
        reader.addToken(JsonToken.STRING, "2020-01-01");
        Date expectedDate = new Date(2020, 1, 1);

        // Act
        Date actualDate = adapter.read(reader);

        // Assert
        assertEquals(expectedDate, actualDate);
    }

    @Test
    public void testRead_ValidTimestampString() {
        // Arrange
        JsonReader reader = new JsonReader();
        reader.addToken(JsonToken.STRING, "2020-01-01T00:00:00Z");
        Timestamp expectedTimestamp = new Timestamp(2020, 1, 1, 0, 0, 0, 0);

        // Act
        Timestamp actualTimestamp = (Timestamp) adapter.read(reader);

        // Assert
        assertEquals(expectedTimestamp, actualTimestamp);
    }

    @Test(expected = JsonSyntaxException.class)
    public void testRead_InvalidDateString() {
        // Arrange
        JsonReader reader = new JsonReader();
        reader.addToken(JsonToken.STRING, "invalid-date");

        // Act
        adapter.read(reader);
    }

    @Test
    public void testToString() {
        // Arrange
        String expectedString = "DefaultDateTypeAdapter(SimpleDateFormat)";

        // Act
        String actualString = adapter.toString();

        // Assert
        assertEquals(expectedString, actualString);
    }
}