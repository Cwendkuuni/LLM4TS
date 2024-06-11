import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultDateTypeAdapterTest {

    private DefaultDateTypeAdapter adapter;

    @Before
    public void setUp() {
        adapter = new DefaultDateTypeAdapter(Date.class);
    }

    @Test
    public void testWrite_WithNullDate_ShouldWriteNullValue() throws IOException {
        // Arrange
        JsonWriter out = new JsonWriter(new StringWriter());

        // Act
        adapter.write(out, null);

        // Assert
        assertEquals("null", out.toString());
    }

    @Test
    public void testWrite_WithNonNullDate_ShouldWriteFormattedDateValue() throws IOException {
        // Arrange
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2021-01-01");
        String expected = "\"2021-01-01\"";

        JsonWriter out = new JsonWriter(new StringWriter());

        // Act
        adapter.write(out, date);

        // Assert
        assertEquals(expected, out.toString());
    }

    @Test
    public void testRead_WithNonStringJsonValue_ShouldThrowJsonParseException() throws IOException {
        // Arrange
        String json = "123";

        JsonReader in = new JsonReader(new StringReader(json));

        // Act & Assert
        assertThrows(JsonParseException.class, () -> adapter.read(in));
    }

    @Test
    public void testRead_WithInvalidDateString_ShouldThrowJsonSyntaxException() throws IOException {
        // Arrange
        String json = "\"2021-01-32\"";

        JsonReader in = new JsonReader(new StringReader(json));

        // Act & Assert
        assertThrows(JsonSyntaxException.class, () -> adapter.read(in));
    }

    @Test
    public void testRead_WithValidDateStringAndDateType_ShouldReturnDate() throws IOException {
        // Arrange
        String json = "\"2021-01-01\"";
        Date expected = new SimpleDateFormat("yyyy-MM-dd").parse("2021-01-01");

        JsonReader in = new JsonReader(new StringReader(json));

        // Act
        Date result = adapter.read(in);

        // Assert
        assertEquals(expected, result);
    }

    @Test
    public void testRead_WithValidDateStringAndTimestampType_ShouldReturnTimestamp() throws IOException {
        // Arrange
        adapter = new DefaultDateTypeAdapter(Timestamp.class);

        String json = "\"2021-01-01\"";
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2021-01-01");
        Timestamp expected = new Timestamp(date.getTime());

        JsonReader in = new JsonReader(new StringReader(json));

        // Act
        Date result = adapter.read(in);

        // Assert
        assertEquals(expected, result);
    }

    @Test
    public void testRead_WithValidDateStringAndSQLDateType_ShouldReturnSQLDate() throws IOException {
        // Arrange
        adapter = new DefaultDateTypeAdapter(java.sql.Date.class);

        String json = "\"2021-01-01\"";
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2021-01-01");
        java.sql.Date expected = new java.sql.Date(date.getTime());

        JsonReader in = new JsonReader(new StringReader(json));

        // Act
        Date result = adapter.read(in);

        // Assert
        assertEquals(expected, result);
    }

}