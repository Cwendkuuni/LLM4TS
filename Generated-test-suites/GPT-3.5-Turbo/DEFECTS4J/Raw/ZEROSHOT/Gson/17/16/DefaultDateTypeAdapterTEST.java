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

    private DefaultDateTypeAdapter defaultDateTypeAdapter;

    @Before
    public void setUp() {
        defaultDateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
    }

    @Test
    public void write_withNullValue_shouldWriteNullValue() throws IOException {
        JsonWriter jsonWriter = new JsonWriter(null);
        defaultDateTypeAdapter.write(jsonWriter, null);
        assertEquals("null", jsonWriter.toString());
    }

    @Test
    public void write_withNonNullValue_shouldWriteFormattedStringValue() throws IOException {
        JsonWriter jsonWriter = new JsonWriter(null);
        Date date = new Date(1612195200000L); // January 2, 2021 00:00:00 UTC
        defaultDateTypeAdapter.write(jsonWriter, date);
        assertEquals("\"Jan 2, 2021 12:00:00 AM\"", jsonWriter.toString());
    }

    @Test
    public void read_withNonStringValue_shouldThrowJsonParseException() throws IOException {
        JsonReader jsonReader = new JsonReader(null);
        jsonReader.beginObject();
        assertThrows(JsonParseException.class, () -> {
            defaultDateTypeAdapter.read(jsonReader);
        });
        jsonReader.endObject();
    }

    @Test
    public void read_withInvalidDateString_shouldThrowJsonSyntaxException() throws IOException {
        JsonReader jsonReader = new JsonReader(null);
        jsonReader.beginObject();
        String invalidDateString = "abc";
        jsonReader.nextString(invalidDateString);
        assertThrows(JsonSyntaxException.class, () -> {
            defaultDateTypeAdapter.read(jsonReader);
        });
        jsonReader.endObject();
    }

    @Test
    public void read_withValidDateString_shouldReturnDateObject() throws IOException {
        JsonReader jsonReader = new JsonReader(null);
        jsonReader.beginObject();
        String validDateString = "\"Jan 2, 2021 12:00:00 AM\"";
        jsonReader.nextString(validDateString);
        Date date = defaultDateTypeAdapter.read(jsonReader);
        assertNotNull(date);
        assertEquals(1612195200000L, date.getTime()); // January 2, 2021 00:00:00 UTC
        jsonReader.endObject();
    }

    @Test
    public void read_withValidDateStringAndDifferentDateType_shouldReturnCorrectDateTypeObject() throws IOException {
        DefaultDateTypeAdapter timestampTypeAdapter = new DefaultDateTypeAdapter(Timestamp.class);
        JsonReader jsonReader = new JsonReader(null);
        jsonReader.beginObject();
        String validDateString = "\"Jan 2, 2021 12:00:00 AM\"";
        jsonReader.nextString(validDateString);
        Timestamp timestamp = (Timestamp) timestampTypeAdapter.read(jsonReader);
        assertNotNull(timestamp);
        assertEquals(1612195200000L, timestamp.getTime()); // January 2, 2021 00:00:00 UTC
        jsonReader.endObject();
    }

    @Test
    public void read_withValidDateStringAndInvalidDateType_shouldThrowAssertionError() throws IOException {
        DefaultDateTypeAdapter invalidDateTypeAdapter = new DefaultDateTypeAdapter(String.class);
        JsonReader jsonReader = new JsonReader(null);
        jsonReader.beginObject();
        String validDateString = "\"Jan 2, 2021 12:00:00 AM\"";
        jsonReader.nextString(validDateString);
        assertThrows(AssertionError.class, () -> {
            invalidDateTypeAdapter.read(jsonReader);
        });
        jsonReader.endObject();
    }

    @Test
    public void toString_shouldReturnSimpleClassNameAndDateFormatClassName() {
        String expectedToString = "DefaultDateTypeAdapter(SimpleDateFormat)";
        assertEquals(expectedToString, defaultDateTypeAdapter.toString());
    }
}