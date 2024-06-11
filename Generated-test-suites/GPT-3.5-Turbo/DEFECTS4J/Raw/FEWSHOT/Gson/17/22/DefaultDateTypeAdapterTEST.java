package com.google.gson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.gson.internal.bind.util.ISO8601Utils;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.junit.Test;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DefaultDateTypeAdapterTest {
    @Test
    public void testConstructorWithDateType() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        assertEquals(Date.class, adapter.dateType);
        assertTrue(adapter.enUsFormat instanceof DateFormat);
        assertTrue(adapter.localFormat instanceof DateFormat);
    }

    @Test
    public void testConstructorWithDateTypeAndPattern() {
        String pattern = "dd-MM-yyyy";
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, pattern);
        assertEquals(Date.class, adapter.dateType);
        assertTrue(adapter.enUsFormat instanceof SimpleDateFormat);
        assertTrue(adapter.localFormat instanceof SimpleDateFormat);
        assertEquals(pattern, ((SimpleDateFormat) adapter.enUsFormat).toPattern());
        assertEquals(pattern, ((SimpleDateFormat) adapter.localFormat).toPattern());
    }

    @Test
    public void testConstructorWithDateTypeAndStyle() {
        int style = DateFormat.SHORT;
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, style);
        assertEquals(Date.class, adapter.dateType);
        assertTrue(adapter.enUsFormat instanceof DateFormat);
        assertTrue(adapter.localFormat instanceof DateFormat);
        assertEquals(style, ((DateFormat) adapter.enUsFormat).getDateStyle());
        assertEquals(style, ((DateFormat) adapter.enUsFormat).getTimeStyle());
        assertEquals(style, ((DateFormat) adapter.localFormat).getDateStyle());
        assertEquals(style, ((DateFormat) adapter.localFormat).getTimeStyle());
    }

    @Test
    public void testConstructorWithDateTypeAndStyleAndLocale() {
        int style = DateFormat.SHORT;
        Locale locale = Locale.US;
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, style, locale);
        assertEquals(Date.class, adapter.dateType);
        assertTrue(adapter.enUsFormat instanceof DateFormat);
        assertTrue(adapter.localFormat instanceof DateFormat);
        assertEquals(style, ((DateFormat) adapter.enUsFormat).getDateStyle());
        assertEquals(style, ((DateFormat) adapter.enUsFormat).getTimeStyle());
        assertEquals(style, ((DateFormat) adapter.localFormat).getDateStyle());
        assertEquals(style, ((DateFormat) adapter.localFormat).getTimeStyle());
        assertEquals(locale, ((DateFormat) adapter.enUsFormat).getLocale());
        assertEquals(locale, ((DateFormat) adapter.localFormat).getLocale());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidDateType() {
        new DefaultDateTypeAdapter(String.class);
    }

    @Test
    public void testWriteWithNullValue() throws IOException {
        JsonWriter writer = new JsonWriter(null);
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        adapter.write(writer, null);
        assertEquals(JsonToken.NULL, writer.peek());
    }

    @Test
    public void testWriteWithValue() throws IOException {
        JsonWriter writer = new JsonWriter(null);
        Date date = new Date();
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        adapter.write(writer, date);
        assertEquals(JsonToken.STRING, writer.peek());
        String formattedDate = adapter.enUsFormat.format(date);
        assertEquals(formattedDate, writer.nextString());
    }

    @Test(expected = JsonParseException.class)
    public void testReadWithNonStringToken() throws IOException {
        JsonReader reader = new JsonReader(null);
        reader.setLenient(true);
        reader.beginArray();
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        adapter.read(reader);
    }

    @Test
    public void testReadWithNullValue() throws IOException {
        JsonReader reader = new JsonReader(null);
        reader.setLenient(true);
        reader.beginNull();
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        assertNull(adapter.read(reader));
        assertEquals(JsonToken.NULL, reader.peek());
    }

    @Test
    public void testReadWithValidDateString() throws IOException {
        JsonReader reader = new JsonReader(null);
        reader.setLenient(true);
        reader.beginString();
        String dateString = "2021-01-01T12:00:00Z";
        reader.value(dateString);
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        Date date = adapter.read(reader);
        assertEquals(dateString, ISO8601Utils.format(date));
    }

    @Test(expected = JsonSyntaxException.class)
    public void testReadWithInvalidDateString() throws IOException {
        JsonReader reader = new JsonReader(null);
        reader.setLenient(true);
        reader.beginString();
        String dateString = "invalid date";
        reader.value(dateString);
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        adapter.read(reader);
    }

    @Test
    public void testDeserializeToDateWithValidDate() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        String dateString = "2021-01-01T12:00:00Z";
        Date date = adapter.deserializeToDate(dateString);
        assertEquals(dateString, ISO8601Utils.format(date));
    }

    @Test(expected = JsonSyntaxException.class)
    public void testDeserializeToDateWithInvalidDate() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        String dateString = "invalid date";
        adapter.deserializeToDate(dateString);
    }

    @Test
    public void testToString() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        assertEquals("DefaultDateTypeAdapter(SimpleDateFormat)", adapter.toString());
    }
}