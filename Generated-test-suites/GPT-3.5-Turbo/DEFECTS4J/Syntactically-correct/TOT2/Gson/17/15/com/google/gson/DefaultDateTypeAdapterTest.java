import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.junit.Test;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static org.junit.Assert.*;

public class DefaultDateTypeAdapterTest {

    @Test
    public void testWrite_NullDate() throws IOException {
        DefaultDateTypeAdapter dateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
        JsonWriter writer = new JsonWriter(null);
        dateTypeAdapter.write(writer, null);
        // Assert
        assertEquals("null", writer.toString());
    }

    @Test
    public void testWrite_ValidDate() throws IOException {
        DefaultDateTypeAdapter dateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
        JsonWriter writer = new JsonWriter(null);
        Date date = new Date();
        dateTypeAdapter.write(writer, date);
        // Assert
        assertEquals(date, writer.toString());
    }

    @Test
    public void testRead_InvalidType() throws IOException {
        DefaultDateTypeAdapter dateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
        JsonReader reader = new JsonReader(null);
        // Assert
        assertThrows(JsonParseException.class, () -> dateTypeAdapter.read(reader));
    }

    @Test
    public void testRead_ValidDateType() throws IOException {
        DefaultDateTypeAdapter dateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
        JsonReader reader = new JsonReader(null);
        String jsonData = "{ \"date\": \"2022-01-01\" }";
        reader.beginObject();
        reader.nextName();
        reader.nextString();
        reader.endObject();
        Date date = dateTypeAdapter.read(reader);
        // Assert
        assertNotNull(date);
    }

    @Test
    public void testDeserializeToDate_InvalidDate() {
        DefaultDateTypeAdapter dateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
        // Assert
        assertThrows(JsonSyntaxException.class, () -> dateTypeAdapter.deserializeToDate("invalid-date"));
    }

    @Test
    public void testToString() {
        DefaultDateTypeAdapter dateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
        String expected = "DefaultDateTypeAdapter(SimpleDateFormat)";
        // Assert
        assertEquals(expected, dateTypeAdapter.toString());
    }
}