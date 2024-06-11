import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.google.gson.internal.bind.util.ISO8601Utils;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public class DefaultDateTypeAdapterTest {

    // Test the write() method for null value
    @Test
    public void testWriteNull() throws IOException {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        JsonWriter writer = new JsonWriter(null);
        adapter.write(writer, null);
        assertEquals(JsonToken.NULL, writer.peek());
    }

    // Test the write() method for a valid date value
    @Test
    public void testWriteValidDate() throws IOException {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        JsonWriter writer = new JsonWriter(null);
        Date date = new Date();
        adapter.write(writer, date);
        assertEquals(JsonToken.STRING, writer.peek());
    }

    // Test the read() method for a string value
    @Test
    public void testReadString() throws IOException {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        JsonReader reader = new JsonReader(null);
        reader.setLenient(true);
        reader.beginObject();
        reader.nextName();
        reader.nextString();
        reader.endObject();
        Date date = adapter.read(reader);
        assertNotNull(date);
    }

    // Test the read() method for a non-string value
    @Test(expected = JsonParseException.class)
    public void testReadNonString() throws IOException {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        JsonReader reader = new JsonReader(null);
        reader.setLenient(true);
        reader.beginObject();
        reader.nextName();
        reader.nextInt();
        reader.endObject();
        adapter.read(reader);
    }

    // Test the read() method for a valid date string
    @Test
    public void testReadValidDate() throws IOException {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        JsonReader reader = new JsonReader(null);
        reader.setLenient(true);
        reader.beginObject();
        reader.nextName();
        reader.nextString();
        reader.endObject();
        Date date = adapter.read(reader);
        assertNotNull(date);
    }

    // Test the read() method for an invalid date string
    @Test(expected = JsonSyntaxException.class)
    public void testReadInvalidDate() throws IOException {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        JsonReader reader = new JsonReader(null);
        reader.setLenient(true);
        reader.beginObject();
        reader.nextName();
        reader.nextString();
        reader.endObject();
        adapter.read(reader);
    }

    // Test the toString() method
    @Test
    public void testToString() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        String expected = "DefaultDateTypeAdapter(SimpleDateFormat)";
        String actual = adapter.toString();
        assertEquals(expected, actual);
    }
}