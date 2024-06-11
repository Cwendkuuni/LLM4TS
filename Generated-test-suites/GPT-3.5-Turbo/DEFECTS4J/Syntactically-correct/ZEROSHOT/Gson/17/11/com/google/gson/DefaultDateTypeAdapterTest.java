import com.google.gson.*;
import com.google.gson.internal.bind.util.ISO8601Utils;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
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
    public void testWriteWithNullValue() throws IOException {
        JsonWriter writer = new JsonWriter();
        adapter.write(writer, null);
        assertEquals(JsonToken.NULL, writer.peek());
    }

    @Test
    public void testWriteWithValue() throws IOException {
        JsonWriter writer = new JsonWriter();
        Date date = new Date();
        adapter.write(writer, date);
        assertEquals(date.toString(), writer.peek());
    }

    @Test
    public void testReadWithString() throws IOException {
        String jsonString = "\"2022-01-01T00:00:00Z\"";
        JsonReader reader = new JsonReader(jsonString);
        Date date = adapter.read(reader);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date expectedDate = sdf.parse("2022-01-01T00:00:00Z");
        assertEquals(expectedDate, date);
    }

    @Test(expected = JsonParseException.class)
    public void testReadWithInvalidDate() throws IOException {
        String jsonString = "\"invalid_date\"";
        JsonReader reader = new JsonReader(jsonString);
        adapter.read(reader);
    }

    @Test
    public void testReadWithValidDateString() throws IOException {
        String jsonString = "\"2022-01-01T00:00:00Z\"";
        JsonReader reader = new JsonReader(jsonString);
        Date date = adapter.read(reader);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date expectedDate = sdf.parse("2022-01-01T00:00:00Z");
        assertEquals(expectedDate, date);
    }

    @Test
    public void testReadWithValidDateNumber() throws IOException {
        String jsonString = "1640995200000";
        JsonReader reader = new JsonReader(jsonString);
        Date date = adapter.read(reader);
        Date expectedDate = new Date(1640995200000L);
        assertEquals(expectedDate, date);
    }

    @Test
    public void testToString() {
        assertEquals("DefaultDateTypeAdapter(SimpleDateFormat)", adapter.toString());
    }
}