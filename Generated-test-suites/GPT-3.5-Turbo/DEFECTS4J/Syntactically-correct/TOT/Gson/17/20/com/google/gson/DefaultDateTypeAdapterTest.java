import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Test;

public class DefaultDateTypeAdapterTest {

    @Test
    public void testWrite_NullDate_WritesNullValue() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        JsonWriterMock writer = new JsonWriterMock();
        adapter.write(writer, null);
        assertEquals("The date should be null", "null", writer.getValue());
    }

    @Test
    public void testWrite_Date_WritesFormattedDate() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        JsonWriterMock writer = new JsonWriterMock();
        Date date = new Date(1600000000000L);
        adapter.write(writer, date);
        assertEquals("Unexpected date format", "\"Sep 13, 2020 11:33:20 AM\"", writer.getValue());
    }

    @Test
    public void testWrite_Timestamp_WritesFormattedTimestamp() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Timestamp.class);
        JsonWriterMock writer = new JsonWriterMock();
        Timestamp timestamp = new Timestamp(1600000000000L);
        adapter.write(writer, timestamp);
        assertEquals("Unexpected timestamp format", "\"Sep 13, 2020 11:33:20 AM\"", writer.getValue());
    }

    @Test
    public void testWrite_JavaSqlDate_WritesFormattedSqlDate() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(java.sql.Date.class);
        JsonWriterMock writer = new JsonWriterMock();
        java.sql.Date sqlDate = new java.sql.Date(1600000000000L);
        adapter.write(writer, sqlDate);
        assertEquals("Unexpected SQL date format", "\"Sep 13, 2020 11:33:20 AM\"", writer.getValue());
    }

    @Test
    public void testWrite_UnsupportedDateType_ThrowsException() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(TimeZone.class);
        JsonWriterMock writer = new JsonWriterMock();
        try {
            adapter.write(writer, TimeZone.getDefault());
            fail("Expected exception was not thrown");
        } catch (IOException e) {
            assertEquals("Unexpected exception message", "Date type must be one of class java.util.Date, class java.sql.Timestamp, or class java.sql.Date but was class java.util.TimeZone", e.getMessage());
        }
    }

    @Test
    public void testRead_NonStringValue_ThrowsException() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        JsonReaderMock reader = new JsonReaderMock();
        reader.setNextToken(JsonToken.NUMBER);
        try {
            adapter.read(reader);
            fail("Expected exception was not thrown");
        } catch (IOException e) {
            assertEquals("Unexpected exception message", "The date should be a string value", e.getMessage());
        }
    }

    @Test
    public void testRead_ValidDateString_ReturnsDate() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        JsonReaderMock reader = new JsonReaderMock();
        reader.setNextToken(JsonToken.STRING);
        reader.setNextValue("\"Sep 13, 2020 11:33:20 AM\"");
        Date date = adapter.read(reader);
        assertEquals("Unexpected date", 1600000000000L, date.getTime());
    }

    @Test
    public void testRead_InvalidDateString_ThrowsException() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        JsonReaderMock reader = new JsonReaderMock();
        reader.setNextToken(JsonToken.STRING);
        reader.setNextValue("\"Invalid Date\"");
        try {
            adapter.read(reader);
            fail("Expected exception was not thrown");
        } catch (JsonSyntaxException e) {
            assertEquals("Unexpected exception message", "Invalid Date", e.getMessage());
        }
    }

    @Test
    public void testRead_ValidDateStringWithLocale_ReturnsDate() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        adapter.setLocale(Locale.FRANCE);
        JsonReaderMock reader = new JsonReaderMock();
        reader.setNextToken(JsonToken.STRING);
        reader.setNextValue("\"13 sept. 2020 11:33:20\"");
        Date date = adapter.read(reader);
        assertEquals("Unexpected date", 1600000000000L, date.getTime());
    }

    @Test
    public void testRead_ValidDateStringWithCustomFormat_ReturnsDate() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        adapter.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US));
        JsonReaderMock reader = new JsonReaderMock();
        reader.setNextToken(JsonToken.STRING);
        reader.setNextValue("\"2020-09-13T11:33:20\"");
        Date date = adapter.read(reader);
        assertEquals("Unexpected date", 1600000000000L, date.getTime());
    }

    @Test
    public void testRead_ValidDateStringWithUnsupportedFormat_ThrowsException() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        adapter.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US));
        JsonReaderMock reader = new JsonReaderMock();
        reader.setNextToken(JsonToken.STRING);
        reader.setNextValue("\"13 sept. 2020 11:33:20\"");
        try {
            adapter.read(reader);
            fail("Expected exception was not thrown");
        } catch (JsonSyntaxException e) {
            assertEquals("Unexpected exception message", "Invalid Date", e.getMessage());
        }
    }
}

class JsonWriterMock extends JsonWriter {

    private StringBuilder builder;

    public JsonWriterMock() {
        super(null);
        this.builder = new StringBuilder();
    }

    @Override
    public JsonWriter value(String value) throws IOException {
        builder.append("\"").append(value).append("\"");
        return this;
    }

    public String getValue() {
        return builder.toString();
    }
}

class JsonReaderMock extends JsonReader {

    private JsonToken nextToken;
    private String nextValue;

    public JsonReaderMock() {
        super(null);
        this.nextToken = null;
        this.nextValue = null;
    }

    public void setNextToken(JsonToken nextToken) {
        this.nextToken = nextToken;
    }

    public void setNextValue(String nextValue) {
        this.nextValue = nextValue;
    }

    @Override
    public JsonToken peek() throws IOException {
        return nextToken;
    }

    @Override
    public String nextString() throws IOException {
        return nextValue;
    }
}