package com.google.gson;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DefaultDateTypeAdapterTest {

    private DefaultDateTypeAdapter defaultDateTypeAdapter;

    @Before
    public void setUp() {
        defaultDateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
    }

    @Test
    public void write_NullDate_WritesNullValue() throws IOException {
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);

        defaultDateTypeAdapter.write(jsonWriter, null);
        jsonWriter.close();

        Assert.assertEquals("null", stringWriter.toString());
    }

    @Test
    public void write_ValidDate_WritesFormattedString() throws IOException {
        DateFormat enUsFormat = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US);
        Date date = new Date();
        String expectedValue = enUsFormat.format(date);

        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);

        defaultDateTypeAdapter.write(jsonWriter, date);
        jsonWriter.close();

        Assert.assertEquals(expectedValue, stringWriter.toString());
    }

    @Test
    public void read_NonStringValue_ThrowsJsonParseException() throws IOException {
        String nonStringValue = "123";

        JsonReader jsonReader = new JsonReader(new StringReader(nonStringValue));

        try {
            defaultDateTypeAdapter.read(jsonReader);
            Assert.fail("Expected JsonParseException");
        } catch (JsonParseException e) {
            // Expected exception
        }
    }

    @Test
    public void read_InvalidDateString_ThrowsJsonSyntaxException() throws IOException {
        String invalidDateString = "InvalidDate";

        JsonReader jsonReader = new JsonReader(new StringReader("\"" + invalidDateString + "\""));

        try {
            defaultDateTypeAdapter.read(jsonReader);
            Assert.fail("Expected JsonSyntaxException");
        } catch (JsonSyntaxException e) {
            // Expected exception
        }
    }

    @Test
    public void read_ValidDateString_ReturnsParsedDate() throws IOException {
        DateFormat enUsFormat = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US);
        String dateString = "2021-01-01T00:00:00";
        Date expectedDate = null;

        try {
            expectedDate = enUsFormat.parse(dateString);
        } catch (Exception e) {
            Assert.fail("Failed to parse expected date");
        }

        JsonReader jsonReader = new JsonReader(new StringReader("\"" + dateString + "\""));
        Date actualDate = defaultDateTypeAdapter.read(jsonReader);

        Assert.assertEquals(expectedDate, actualDate);
    }

    @Test
    public void toString_ReturnsCorrectStringRepresentation() {
        String expectedString = "DefaultDateTypeAdapter(SimpleDateFormat)";

        Assert.assertEquals(expectedString, defaultDateTypeAdapter.toString());
    }
}