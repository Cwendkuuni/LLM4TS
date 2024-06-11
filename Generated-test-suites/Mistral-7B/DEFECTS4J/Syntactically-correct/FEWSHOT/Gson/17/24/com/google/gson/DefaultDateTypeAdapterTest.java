package com.google.gson.internal.bind.util; // Import ISO8601Utils for testing

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.JsonWriter;
import com.google.gson.stream.JsonReader;

@RunWith(JUnit4.class)
public class DefaultDateTypeAdapterTest {

    @Test
    public void testDefaultConstruction() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        assertEquals(Date.class, adapter.dateType);
        assertEquals(DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US), adapter.enUsFormat);
        assertEquals(DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT), adapter.localFormat);
    }

    @Test
    public void testConstructionWithDatePattern() {
        String datePattern = "dd-MMM-yyyy HH:mm:ss.SSS z";
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, datePattern);
        assertEquals(Date.class, adapter.dateType);
        SimpleDateFormat formatter = new SimpleDateFormat(datePattern, Locale.US);
        assertEquals(formatter, adapter.enUsFormat);
        assertEquals(formatter, adapter.localFormat);
    }

    @Test
    public void testConstructionWithDateStyles() {
        int dateStyle = DateFormat.SHORT;
        int timeStyle = DateFormat.SHORT;
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(dateStyle, timeStyle);
        assertEquals(Date.class, adapter.dateType);
        assertEquals(DateFormat.getDateInstance(dateStyle, Locale.US), adapter.enUsFormat);
        assertEquals(DateFormat.getDateInstance(dateStyle), adapter.localFormat);
    }

    @Test
    public void testConstructionWithDateStylesAndTimeZone() {
        int dateStyle = DateFormat.SHORT;
        int timeStyle = DateFormat.SHORT;
        Locale locale = Locale.ENGLISH;
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(dateStyle, timeStyle, locale);
        assertEquals(Date.class, adapter.dateType);
        assertEquals(DateFormat.getDateInstance(dateStyle, locale), adapter.enUsFormat);
        assertEquals(DateFormat.getDateInstance(dateStyle), adapter.localFormat);
    }

    @Test
    public void testSerializationWithDefaultConstruction() throws IOException {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        JsonWriter writer = new JsonWriter(System.out);
        adapter.write(writer, new Date());
        writer.flush();
        assertEquals("2022-11-23 14:56:23.045 EST", System.out.getBytes());
    }

    @Test
    public void testSerializationWithDatePatternConstruction() throws IOException {
        String datePattern = "dd-MMM-yyyy HH:mm:ss.SSS z";
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, datePattern);
        JsonWriter writer = new JsonWriter(System.out);
        adapter.write(writer, new Date());
        writer.flush();
        assertEquals("23-Nov-2022 14:56:23.045 EST".getBytes(), System.out.getBytes());
    }

    @Test
    public void testSerializationWithDateStylesConstruction() throws IOException {
        int dateStyle = DateFormat.SHORT;
        int timeStyle = DateFormat.SHORT;
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(dateStyle, timeStyle);
        JsonWriter writer = new JsonWriter(System.out);
        adapter.write(writer, new Date());
        writer.flush();
        assertEquals("23-Nov-21".getBytes(), System.out.getBytes());
    }

    @Test
    public void testSerializationWithDateStylesAndTimeZoneConstruction() throws IOException {
        int dateStyle = DateFormat.SHORT;
        int timeStyle = DateFormat.SHORT;
        Locale locale = Locale.ENGLISH;
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(dateStyle, timeStyle, locale);
        JsonWriter writer = new JsonWriter(System.out);
        adapter.write(writer, new Date());
        writer.flush();
        assertEquals("23-Nov-21".getBytes(), System.out.getBytes());
    }

    @Test
    public void testDeserializationWithDefaultConstruction() throws IOException {
        JsonReader reader = new JsonReader(new StringReader("\"2022-11-23 14:56:23.045 EST\""));
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        Date date = adapter.read(reader);
        assertEquals(new Date(1668884463000L), date);
    }

    @Test
    public void testDeserializationWithDatePatternConstruction() throws IOException {
        String jsonString = "\"23-Nov-22 14:56:23.045 EST\"";
        JsonReader reader = new JsonReader(new StringReader(jsonString));
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, "dd-MMM-yyyy HH:mm:ss.SSS z");
        Date date = adapter.read(reader);
        assertEquals(new Date(1669482663000L), date);
    }

    @Test
    public void testDeserializationWithDateStylesConstruction() throws IOException {
        JsonReader reader = new JsonReader(new StringReader("\"23-Nov-21\""));
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, DateFormat.SHORT, DateFormat.SHORT);
        Date date = adapter.read(reader);
        assertEquals(new Date(1669482663000L), date);
    }

    @Test
    public void testDeserializationWithDateStylesAndTimeZoneConstruction() throws IOException {
        JsonReader reader = new JsonReader(new StringReader("\"23-Nov-21\""));
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, DateFormat.SHORT, DateFormat.SHORT, Locale.ENGLISH);
        Date date = adapter.read(reader);
        assertEquals(new Date(1669482663000L), date);
    }

    @Test
    public void testDeserializationWithTimestampConstruction() throws IOException {
        JsonReader reader = new JsonReader(new StringReader("\"2022-11-23 14:56:23.045 EST\""));
        DefaultDateTypeAdapter<Timestamp> adapter = new DefaultDateTypeAdapter<>(Timestamp.class);
        Timestamp timestamp = adapter.read(reader);
        assertEquals(new Timestamp(1668884463000L), timestamp);
    }

    @Test
    public void testDeserializationWithJavaSqlDateConstruction() throws IOException {
        JsonReader reader = new JsonReader(new StringReader("\"2022-11-23\""));
        DefaultDateTypeAdapter<java.sql.Date> adapter = new DefaultDateTypeAdapter<>(java.sql.Date.class);
        java.sql.Date date = adapter.read(reader);
        assertEquals(new java.sql.Date(1668884400000L), date);
    }

    @Test(expected = JsonParseException.class)
    public void testDeserializationWithInvalidJsonString() throws IOException {
        JsonReader reader = new JsonReader(new StringReader("\"abc\""));
        DefaultDateTypeAdapter<Date> adapter = new DefaultDateTypeAdapter<>(Date.class);
        adapter.read(reader);
    }

    @Test(expected = JsonSyntaxException.class)
    public void testDeserializationWithInvalidDateString() throws IOException {
        JsonReader reader = new JsonReader(new StringReader("\"2022-11-32\""));
        DefaultDateTypeAdapter<Date> adapter = new DefaultDateTypeAdapter<>(Date.class);
        adapter.read(reader);
    }
}

class StringReader {
    StringReader(String s) {
        this.s = s;
    }

    String readLine() throws IOException {
        return s;
    }

    private String s;
}