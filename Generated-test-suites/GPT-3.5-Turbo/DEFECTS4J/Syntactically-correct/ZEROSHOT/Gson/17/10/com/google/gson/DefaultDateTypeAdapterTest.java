import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static org.junit.Assert.*;

public class DefaultDateTypeAdapterTest {

    private DefaultDateTypeAdapter adapter;

    @Before
    public void setUp() {
        adapter = new DefaultDateTypeAdapter(Date.class);
    }

    @Test
    public void writeNullValue() throws IOException {
        // Arrange
        JsonWriter writer = new JsonWriter(null);
        Date value = null;

        // Act
        adapter.write(writer, value);

        // Assert
        assertEquals("null", writer.toString());
    }

    @Test
    public void writeNonNullValue() throws IOException {
        // Arrange
        JsonWriter writer = new JsonWriter(null);
        Date value = new Date(1625583926000L);

        // Act
        adapter.write(writer, value);

        // Assert
        assertEquals("\"Jul 6, 2021, 4:52:06 PM\"", writer.toString());
    }

    @Test
    public void readValidDateString() throws IOException {
        // Arrange
        JsonReader reader = new JsonReader(null);
        reader.beginObject();
        reader.nextName();
        String dateString = "Jul 6, 2021, 4:52:06 PM";
        reader.value(dateString);
        reader.endObject();

        // Act
        Date date = adapter.read(reader);

        // Assert
        assertNotNull(date);
        assertEquals(dateString, DateFormat.getDateTimeInstance().format(date));
    }

    @Test(expected = JsonParseException.class)
    public void readInvalidDate() throws IOException {
        // Arrange
        JsonReader reader = new JsonReader(null);
        reader.beginObject();
        reader.nextName();
        String dateString = "InvalidDate";
        reader.value(dateString);
        reader.endObject();

        // Act
        adapter.read(reader);
    }

    @Test
    public void readNullValue() throws IOException {
        // Arrange
        JsonReader reader = new JsonReader(null);
        reader.beginObject();
        reader.nextName();
        reader.nullValue();
        reader.endObject();

        // Act
        Date date = adapter.read(reader);

        // Assert
        assertNull(date);
    }

    @Test
    public void toStringTest() {
        // Act
        String result = adapter.toString();

        // Assert
        assertEquals("DefaultDateTypeAdapter(SimpleDateFormat)", result);
    }
}