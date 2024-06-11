import org.junit.Before;
import org.junit.Test;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static org.junit.Assert.*;

public class DefaultDateTypeAdapterTest {

    private DefaultDateTypeAdapter dateTypeAdapter;

    @Before
    public void setUp() {
        dateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
    }

    @Test
    public void testWrite_NullValue_ShouldWriteNull() {
        // Arrange
        JsonWriter writer = new MockJsonWriter();

        // Act
        try {
            dateTypeAdapter.write(writer, null);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        // Assert
        assertEquals(JsonToken.NULL, writer.getTokens().get(0));
    }

    @Test
    public void testWrite_ValidDate_ShouldWriteFormattedString() {
        // Arrange
        JsonWriter writer = new MockJsonWriter();
        Date validDate = new Date(2020, 4, 15);

        // Act
        try {
            dateTypeAdapter.write(writer, validDate);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        // Assert
        assertEquals(JsonToken.STRING, writer.getTokens().get(0));
        assertEquals("2020-05-15", writer.getTokens().get(1));
    }

    @Test
    public void testRead_InvalidToken_ShouldThrowJsonParseException() {
        // Arrange
        JsonReader reader = new MockJsonReader("[invalid]");

        // Act
        try {
            dateTypeAdapter.read(reader);
            fail("Expected JsonParseException");
        } catch (JsonParseException e) {
            // Assert
            assertEquals("The date should be a string value", e.getMessage());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testRead_ValidDate_ShouldReturnDate() {
        // Arrange
        JsonReader reader = new MockJsonReader("\"2020-05-15\"");

        // Act
        Date result = null;
        try {
            result = dateTypeAdapter.read(reader);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        // Assert
        assertNotNull(result);
        assertEquals(new Date(2020, 4, 15), result);
    }

    @Test
    public void testRead_ValidDateAsTimestamp_ShouldReturnTimestamp() {
        // Arrange
        dateTypeAdapter = new DefaultDateTypeAdapter(Timestamp.class);
        JsonReader reader = new MockJsonReader("\"2020-05-15\"");

        // Act
        Timestamp result = null;
        try {
            result = (Timestamp) dateTypeAdapter.read(reader);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        // Assert
        assertNotNull(result);
        assertEquals(new Timestamp(new Date(2020, 4, 15).getTime()), result);
    }

    @Test
    public void testRead_ValidDateAsSQLDate_ShouldReturnSQLDate() {
        // Arrange
        dateTypeAdapter = new DefaultDateTypeAdapter(java.sql.Date.class);
        JsonReader reader = new MockJsonReader("\"2020-05-15\"");

        // Act
        java.sql.Date result = null;
        try {
            result = (java.sql.Date) dateTypeAdapter.read(reader);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        // Assert
        assertNotNull(result);
        assertEquals(new java.sql.Date(new Date(2020, 4, 15).getTime()), result);
    }

    @Test
    public void testToString_ShouldReturnClassName() {
        // Act
        String result = dateTypeAdapter.toString();

        // Assert
        assertEquals("DefaultDateTypeAdapter(SimpleDateFormat)", result);
    }
}

class MockJsonWriter extends JsonWriter {

    private List<JsonToken> tokens;

    public MockJsonWriter() {
        super(new StringWriter());
        tokens = new ArrayList<>();
    }

    public List<JsonToken> getTokens() {
        return tokens;
    }

    @Override
    public void nullValue() throws IOException {
        tokens.add(JsonToken.NULL);
    }

    @Override
    public void value(String value) throws IOException {
        tokens.add(JsonToken.STRING);
        tokens.add(value);
    }
}

class MockJsonReader extends JsonReader {

    private List<String> tokens;
    private int position;

    public MockJsonReader(String json) {
        super(new StringReader(json));
        tokens = new ArrayList<>();
        position = 0;
    }

    @Override
    public JsonToken peek() throws IOException {
        String token = tokens.get(position);
        if ("null".equals(token)) {
            return JsonToken.NULL;
        } else if ("\"".equals(token.substring(0, 1))) {
            return JsonToken.STRING;
        } else {
            return JsonToken.NUMBER;
        }
    }

    @Override
    public String nextString() throws IOException {
        return tokens.get(position++);
    }
}