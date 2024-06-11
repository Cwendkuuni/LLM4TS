
import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultDateTypeAdapterTest {

    @Test
    public void testWrite_NullValue() {
        // Arrange
        JsonWriterStub jsonWriter = new JsonWriterStub();
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);

        // Act
        adapter.write(jsonWriter, null);

        // Assert
        assertNull(jsonWriter.getLastValue());
    }

    @Test
    public void testWrite_NonNullValue() {
        // Arrange
        JsonWriterStub jsonWriter = new JsonWriterStub();
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);

        // Act
        adapter.write(jsonWriter, new Date());

        // Assert
        assertNotNull(jsonWriter.getLastValue());
        assertTrue(jsonWriter.getLastValue() instanceof String);
    }

    @Test
    public void testRead_InvalidValueType() {
        // Arrange
        JsonReaderStub jsonReader = new JsonReaderStub();
        jsonReader.setNextToken(JsonToken.NUMBER);
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);

        // Act & Assert
        try {
            adapter.read(jsonReader);
            fail("Expected a JsonParseException to be thrown");
        } catch (JsonParseException e) {
            // Test passed
        }
    }

    @Test
    public void testRead_ValidDateString() {
        // Arrange
        JsonReaderStub jsonReader = new JsonReaderStub();
        jsonReader.setNextToken(JsonToken.STRING);
        jsonReader.setNextValue("2020-01-01");
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);

        // Act
        Date result = adapter.read(jsonReader);

        // Assert
        assertNotNull(result);
        assertEquals("Wed Jan 01 00:00:00 UTC 2020", result.toString());
    }

    @Test
    public void testRead_InvalidDateString() {
        // Arrange
        JsonReaderStub jsonReader = new JsonReaderStub();
        jsonReader.setNextToken(JsonToken.STRING);
        jsonReader.setNextValue("2020-01-01T00:00:00"); // Invalid format
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);

        // Act & Assert
        try {
            adapter.read(jsonReader);
            fail("Expected a JsonSyntaxException to be thrown");
        } catch (JsonSyntaxException e) {
            // Test passed
        }
    }

    @Test
    public void testRead_TimestampType() {
        // Arrange
        JsonReaderStub jsonReader = new JsonReaderStub();
        jsonReader.setNextToken(JsonToken.STRING);
        jsonReader.setNextValue("2020-01-01");
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Timestamp.class);

        // Act
        Date result = adapter.read(jsonReader);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof Timestamp);
    }

    @Test
    public void testRead_DateType() {
        // Arrange
        JsonReaderStub jsonReader = new JsonReaderStub();
        jsonReader.setNextToken(JsonToken.STRING);
        jsonReader.setNextValue("2020-01-01");
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(java.sql.Date.class);

        // Act
        Date result = adapter.read(jsonReader);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof java.sql.Date);
    }

    @Test
    public void testToString() {
        // Arrange
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);

        // Act
        String result = adapter.toString();

        // Assert
        assertEquals("DefaultDateTypeAdapter(SimpleDateFormat)", result);
    }
}
