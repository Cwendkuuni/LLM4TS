package com.google.gson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

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

import com.google.gson.internal.bind.util.ISO8601Utils;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public class DefaultDateTypeAdapterTest {

    private DefaultDateTypeAdapter defaultDateTypeAdapter;

    @Before
    public void setUp() {
        defaultDateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
    }

    @Test
    public void testWrite_NullValue() throws IOException {
        JsonWriter jsonWriter = mock(JsonWriter.class);
        defaultDateTypeAdapter.write(jsonWriter, null);
        verify(jsonWriter).nullValue();
    }

    @Test
    public void testWrite_DateNotNull() throws IOException {
        JsonWriter jsonWriter = mock(JsonWriter.class);
        Date date = new Date();
        when(jsonWriter.value(anyString())).thenReturn(jsonWriter);
        defaultDateTypeAdapter.write(jsonWriter, date);
        verify(jsonWriter).value(anyString());
    }

    @Test(expected = JsonParseException.class)
    public void testRead_NonStringValue() throws IOException {
        JsonReader jsonReader = mock(JsonReader.class);
        when(jsonReader.peek()).thenReturn(JsonToken.NUMBER);
        defaultDateTypeAdapter.read(jsonReader);
    }

    @Test
    public void testRead_Date() throws IOException {
        JsonReader jsonReader = mock(JsonReader.class);
        when(jsonReader.peek()).thenReturn(JsonToken.STRING);
        when(jsonReader.nextString()).thenReturn("2022-01-01");
        Date date = defaultDateTypeAdapter.read(jsonReader);
        assertNotNull(date);
    }

    @Test
    public void testRead_DateTypeTimestamp() throws IOException {
        defaultDateTypeAdapter = new DefaultDateTypeAdapter(Timestamp.class);
        JsonReader jsonReader = mock(JsonReader.class);
        when(jsonReader.peek()).thenReturn(JsonToken.STRING);
        when(jsonReader.nextString()).thenReturn("2022-01-01");
        Date date = defaultDateTypeAdapter.read(jsonReader);
        assertNotNull(date);
        assertEquals(Timestamp.class, date.getClass());
    }

    @Test
    public void testRead_DateTypeSqlDate() throws IOException {
        defaultDateTypeAdapter = new DefaultDateTypeAdapter(java.sql.Date.class);
        JsonReader jsonReader = mock(JsonReader.class);
        when(jsonReader.peek()).thenReturn(JsonToken.STRING);
        when(jsonReader.nextString()).thenReturn("2022-01-01");
        Date date = defaultDateTypeAdapter.read(jsonReader);
        assertNotNull(date);
        assertEquals(java.sql.Date.class, date.getClass());
    }

    @Test
    public void testDeserializeToDate_LocalFormat() {
        String dateString = "2022-01-01";
        DateFormat localDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat enUsDateFormat = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US);
        Date expectedDate = null;
        try {
            expectedDate = localDateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date actualDate = defaultDateTypeAdapter.deserializeToDate(dateString);
        assertNotNull(actualDate);
        assertEquals(expectedDate, actualDate);
    }

    @Test
    public void testDeserializeToDate_EnUsFormat() {
        String dateString = "2022-01-01";
        DateFormat localDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat enUsDateFormat = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US);
        Date expectedDate = null;
        try {
            expectedDate = enUsDateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date actualDate = defaultDateTypeAdapter.deserializeToDate(dateString);
        assertNotNull(actualDate);
        assertEquals(expectedDate, actualDate);
    }

    @Test(expected = JsonSyntaxException.class)
    public void testDeserializeToDate_InvalidFormat() {
        String dateString = "2022-01-01T00:00:00";
        defaultDateTypeAdapter.deserializeToDate(dateString);
    }

    @Test
    public void testToString() {
        String expectedString = "DefaultDateTypeAdapter(SimpleDateFormat)";
        String actualString = defaultDateTypeAdapter.toString();
        assertEquals(expectedString, actualString);
    }
}