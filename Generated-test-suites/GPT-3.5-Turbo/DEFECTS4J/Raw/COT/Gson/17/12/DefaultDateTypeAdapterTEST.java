
import org.junit.Test;
import com.google.gson.JsonParseException;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DefaultDateTypeAdapterTest {

    // Test write() when value is null
    @Test
    public void testWriteNullValue() throws IOException {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        JsonWriter mockWriter = new JsonWriter(null);
        adapter.write(mockWriter, null);
        // Verify that the value is written as null
        assertEquals(JsonToken.NULL, mockWriter.getTop());
    }

    // Test write() when value is not null
    @Test
    public void testWriteNonNullValue() throws IOException {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        JsonWriter mockWriter = new JsonWriter(null);
        Date date = new Date();
        adapter.write(mockWriter, date);
        // Verify that the value is written correctly
        assertEquals(JsonToken.STRING, mockWriter.getTop());
        assertEquals(DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US).format(date), mockWriter.getValue());
    }

    // Test read() when the peeked value is not a string
    @Test(expected = JsonParseException.class)
    public void testReadNonStringValue() throws IOException {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        JsonReader mockReader = new JsonReader(null);
        mockReader.beginObject();
        mockReader.nextName();
        mockReader.nullValue();
        mockReader.endObject();
        adapter.read(mockReader);
        // Expect JsonParseException to be thrown
    }

    // Test read() when the date type is Date.class
    @Test
    public void testReadDateType() throws IOException {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        JsonReader mockReader = new JsonReader(null);
        mockReader.beginObject();
        mockReader.nextName();
        Date expectedDate = new Date();
        mockReader.value(DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US).format(expectedDate));
        mockReader.endObject();
        Date actualDate = adapter.read(mockReader);
        // Verify that the read value is correct
        assertEquals(expectedDate, actualDate);
    }

    // Test read() when the date type is Timestamp.class
    @Test
    public void testReadTimestampType() throws IOException {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Timestamp.class);
        JsonReader mockReader = new JsonReader(null);
        mockReader.beginObject();
        mockReader.nextName();
        Date expectedDate = new Date();
        mockReader.value(DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US).format(expectedDate));
        mockReader.endObject();
        Timestamp actualTimestamp = adapter.read(mockReader);
        // Verify that the read value is correct
        assertEquals(expectedDate, actualTimestamp);
    }

    // Test read() when the date type is java.sql.Date.class
    @Test
    public void testReadSqlDateType() throws IOException {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(java.sql.Date.class);
        JsonReader mockReader = new JsonReader(null);
        mockReader.beginObject();
        mockReader.nextName();
        Date expectedDate = new Date();
        mockReader.value(DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US).format(expectedDate));
        mockReader.endObject();
        java.sql.Date actualSqlDate = adapter.read(mockReader);
        // Verify that the read value is correct
        assertEquals(expectedDate, actualSqlDate);
    }

    // Test read() with an invalid date string
    @Test(expected = JsonParseException.class)
    public void testReadInvalidDateString() throws IOException {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        JsonReader mockReader = new JsonReader(null);
        mockReader.beginObject();
        mockReader.nextName();
        mockReader.value("invalid_date_string");
        mockReader.endObject();
        adapter.read(mockReader);
        // Expect JsonParseException to be thrown
    }

    // Test toString()
    @Test
    public void testToString() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        String expectedString = "DefaultDateTypeAdapter(SimpleDateFormat)";
        String actualString = adapter.toString();
        assertEquals(expectedString, actualString);
    }
}
