import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DefaultDateTypeAdapterTest {

    private DefaultDateTypeAdapter defaultDateTypeAdapter;

    @Before
    public void setUp() {
        defaultDateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
    }

    @Test
    public void testWrite_NullValue_ShouldWriteNull() {
        // Arrange
        JsonWriter writer = new JsonWriterStub();
        Date value = null;

        // Act
        assertDoesNotThrow(() -> defaultDateTypeAdapter.write(writer, value));

        // Assert
        assertEquals("null", writer.getValue());
    }

    @Test
    public void testWrite_DateValue_ShouldWriteCorrectValue() {
        // Arrange
        JsonWriter writer = new JsonWriterStub();
        Date value = new Date(1606752000000L); // 2020-11-30 00:00:00 UTC
        DateFormat expectedDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

        // Act
        assertDoesNotThrow(() -> defaultDateTypeAdapter.write(writer, value));

        // Assert
        assertEquals(expectedDateFormat.format(value), writer.getValue().replace("\"", ""));
    }

    @Test
    public void testRead_InvalidJsonToken_ThrowsJsonParseException() {
        // Arrange
        JsonReader reader = new JsonReaderStub(JsonToken.NUMBER);

        // Act and Assert
        assertThrows(JsonParseException.class, () -> defaultDateTypeAdapter.read(reader));
    }

    @Test
    public void testRead_NullJsonToken_ThrowsJsonParseException() {
        // Arrange
        JsonReader reader = new JsonReaderStub(null);

        // Act and Assert
        assertThrows(JsonParseException.class, () -> defaultDateTypeAdapter.read(reader));
    }

    @Test
    public void testRead_ValidJsonToken_ReturnsDate() {
        // Arrange
        JsonReader reader = new JsonReaderStub(JsonToken.STRING);
        String dateString = "2020-11-30T00:00:00Z";
        Date expectedDate = new Date(1606752000000L); // 2020-11-30 00:00:00 UTC
        reader.setNextString(dateString);

        // Act
        Date result = defaultDateTypeAdapter.read(reader);

        // Assert
        assertEquals(expectedDate, result);
    }

    @Test
    public void testRead_InvalidDateString_ThrowsJsonSyntaxException() {
        // Arrange
        JsonReader reader = new JsonReaderStub(JsonToken.STRING);
        String dateString = "InvalidDate";
        reader.setNextString(dateString);

        // Act and Assert
        assertThrows(JsonSyntaxException.class, () -> defaultDateTypeAdapter.read(reader));
    }

    @Test
    public void testRead_TimestampDateType_ReturnsTimestampDate() {
        // Arrange
        DefaultDateTypeAdapter timestampAdapter = new DefaultDateTypeAdapter(Timestamp.class);
        JsonReader reader = new JsonReaderStub(JsonToken.STRING);
        String dateString = "2020-11-30T00:00:00Z";
        Date expectedDate = new Timestamp(1606752000000L); // 2020-11-30 00:00:00 UTC
        reader.setNextString(dateString);

        // Act
        Date result = timestampAdapter.read(reader);

        // Assert
        assertEquals(expectedDate, result);
    }

    @Test
    public void testRead_SqlDateType_ReturnsSqlDate() {
        // Arrange
        DefaultDateTypeAdapter sqlDateAdapter = new DefaultDateTypeAdapter(java.sql.Date.class);
        JsonReader reader = new JsonReaderStub(JsonToken.STRING);
        String dateString = "2020-11-30T00:00:00Z";
        Date expectedDate = new java.sql.Date(1606752000000L); // 2020-11-30 00:00:00 UTC
        reader.setNextString(dateString);

        // Act
        Date result = sqlDateAdapter.read(reader);

        // Assert
        assertEquals(expectedDate, result);
    }

}

class JsonWriterStub extends JsonWriter {
    private StringBuilder value;

    public JsonWriterStub() {
        super(null);
        this.value = new StringBuilder();
    }

    public void value(String value) {
        this.value.append(value);
    }

    public String getValue() {
        return value.toString();
    }
}

class JsonReaderStub extends JsonReader {

    private JsonToken jsonToken;
    private String nextString;

    public JsonReaderStub(JsonToken jsonToken) {
        super(null);
        this.jsonToken = jsonToken;
        this.nextString = null;
    }

    public void setNextString(String nextString) {
        this.nextString = nextString;
    }

    @Override
    public JsonToken peek() {
        return jsonToken;
    }

    @Override
    public String nextString() {
        return nextString;
    }
}