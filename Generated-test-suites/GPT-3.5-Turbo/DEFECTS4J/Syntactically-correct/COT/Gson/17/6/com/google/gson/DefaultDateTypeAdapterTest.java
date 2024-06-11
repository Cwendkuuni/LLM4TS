import com.google.gson.stream.JsonParseException;
import org.junit.Before;
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

    private DefaultDateTypeAdapter adapter;

    @Before
    public void setUp() {
        adapter = new DefaultDateTypeAdapter(Date.class);
    }

    @Test
    public void testWriteWithNullDate() throws IOException {
        JsonWriter writer = new JsonWriter();
        adapter.write(writer, null);
        assertEquals(JsonToken.NULL, writer.peek());
    }

    @Test(expected = JsonParseException.class)
    public void testReadWithNonStringValue() throws IOException {
        JsonReader reader = new JsonReader();
        reader.beginObject();
        adapter.read(reader);
    }

    @Test(expected = JsonParseException.class)
    public void testReadWithInvalidDateString() throws IOException {
        JsonReader reader = new JsonReader();
        reader.value("invalid");
        adapter.read(reader);
    }

    @Test
    public void testReadWithDifferentDateFormats() throws IOException {
        DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US);
        DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        
        JsonReader reader1 = new JsonReader();
        reader1.value("2020-01-01T00:00:00-0700");
        Date date1 = adapter.read(reader1);
        assertEquals("2020-01-01T00:00:00-0700", format1.format(date1));

        JsonReader reader2 = new JsonReader();
        reader2.value("2020-01-01");
        Date date2 = adapter.read(reader2);
        assertEquals("2020-01-01", format2.format(date2));
    }

    @Test
    public void testReadWithDifferentDateTypes() throws IOException {
        DefaultDateTypeAdapter adapter1 = new DefaultDateTypeAdapter(Date.class);
        DefaultDateTypeAdapter adapter2 = new DefaultDateTypeAdapter(Timestamp.class);
        DefaultDateTypeAdapter adapter3 = new DefaultDateTypeAdapter(java.sql.Date.class);
        
        JsonReader reader = new JsonReader();
        reader.value("2020-01-01T00:00:00-0700");
        Date date = adapter1.read(reader);

        Timestamp timestamp = adapter2.read(reader);
        assertEquals(date.getTime(), timestamp.getTime());

        java.sql.Date sqlDate = adapter3.read(reader);
        assertEquals(date.getTime(), sqlDate.getTime());
    }

    @Test
    public void testToString() {
        DateFormat format = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US);
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, format, format);
        assertEquals("DefaultDateTypeAdapter(SimpleDateFormat)", adapter.toString());
    }
}

