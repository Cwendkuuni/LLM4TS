import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.internal.bind.util.ISO8601Utils;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public class DefaultDateTypeAdapterTest {

    DefaultDateTypeAdapter defaultDateTypeAdapter;

    @Before
    public void setUp() {
        defaultDateTypeAdapter = new DefaultDateTypeAdapter(Date.class, DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US), DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT));
    }

    @Test
    public void testWrite_NullValue() throws IOException {
        JsonWriter jsonWriter = new JsonWriter(null);
        defaultDateTypeAdapter.write(jsonWriter, null);
        assertEquals(JsonToken.NULL, jsonWriter.peek());
    }

    @Test
    public void testWrite() throws IOException {
        Date date = new Date();
        JsonWriter jsonWriter = new JsonWriter(null);
        defaultDateTypeAdapter.write(jsonWriter, date);
        assertEquals(JsonToken.STRING, jsonWriter.peek());
    }

    @Test(expected = JsonParseException.class)
    public void testRead_InvalidJsonToken() throws IOException {
        JsonReader jsonReader = new JsonReader(null);
        defaultDateTypeAdapter.read(jsonReader);
    }

    @Test
    public void testRead_DateType() throws IOException {
        Date date = new Date();
        JsonReader jsonReader = new JsonReader(null);
        jsonReader.setLenient(true);
        jsonReader.nextString();
        assertEquals(date, defaultDateTypeAdapter.read(jsonReader));
    }

    @Test
    public void testRead_TimestampType() throws IOException {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        JsonReader jsonReader = new JsonReader(null);
        jsonReader.setLenient(true);
        jsonReader.nextString();
        assertEquals(timestamp, defaultDateTypeAdapter.read(jsonReader));
    }

    @Test
    public void testRead_JavaSqlDateType() throws IOException {
        java.sql.Date sqlDate = new java.sql.Date(System.currentTimeMillis());
        JsonReader jsonReader = new JsonReader(null);
        jsonReader.setLenient(true);
        jsonReader.nextString();
        assertEquals(sqlDate, defaultDateTypeAdapter.read(jsonReader));
    }

    @Test(expected = AssertionError.class)
    public void testRead_UnknownType() throws IOException {
        DefaultDateTypeAdapter unknownTypeAdapter = new DefaultDateTypeAdapter(String.class, DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US), DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT));
        JsonReader jsonReader = new JsonReader(null);
        jsonReader.setLenient(true);
        jsonReader.nextString();
        unknownTypeAdapter.read(jsonReader);
    }

    @Test
    public void testDeserializeToDate() {
        String dateStr = "2021-01-01T00:00:00Z";
        Date expectedDate = ISO8601Utils.parse(dateStr, new ParsePosition(0));
        assertEquals(expectedDate, defaultDateTypeAdapter.deserializeToDate(dateStr));
    }

    @Test(expected = JsonSyntaxException.class)
    public void testDeserializeToDate_InvalidDateFormat() {
        String invalidDateStr = "2021-01-01";
        defaultDateTypeAdapter.deserializeToDate(invalidDateStr);
    }

    @Test
    public void testToString() {
        String expectedToString = "DefaultDateTypeAdapter(SimpleDateFormat)";
        assertEquals(expectedToString, defaultDateTypeAdapter.toString());
    }

}