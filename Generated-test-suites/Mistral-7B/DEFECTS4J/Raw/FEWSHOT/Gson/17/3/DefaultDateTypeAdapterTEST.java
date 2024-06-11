package com.google.gson.internal.test; // assuming we have a test package for internal tests

import static com.google.gson.internal.bind.util.ISO8601Utils.parse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.gson.*;
import com.google.gson.internal.bind.util.ISO8601Utils;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DefaultDateTypeAdapterTest {

    @Test
    public void testDefaultDateTypeAdapterConstruction() {
        // Default constructor
        DefaultDateTypeAdapter defaultDateTypeAdapter1 = new DefaultDateTypeAdapter(Date.class);
        DefaultDateTypeAdapter defaultDateTypeAdapter2 = new DefaultDateTypeAdapter(java.sql.Date.class);
        DefaultDateTypeAdapter defaultDateTypeAdapter3 = new DefaultDateTypeAdapter(Timestamp.class);

        assertEquals("DefaultDateTypeAdapter(java.util.Date)", defaultDateTypeAdapter1.toString());
        assertEquals("DefaultDateTypeAdapter(java.sql.Date)", defaultDateTypeAdapter2.toString());
        assertEquals("DefaultDateTypeAdapter(com.google.gson.internal.java.sql.Timestamp)", defaultDateTypeAdapter3.toString());

        // String constructor
        DefaultDateTypeAdapter defaultDateTypeAdapter4 = new DefaultDateTypeAdapter("DefaultDateTypeAdapter(SimpleDateFormat)");
        assertEquals("DefaultDateTypeAdapter(java.util.Date)", defaultDateTypeAdapter4.toString());

        try {
            new DefaultDateTypeAdapter("not a Date class");
            assert false : "Should not be able to create DefaultDateTypeAdapter with non-Date class";
        } catch (IllegalArgumentException e) {
            assertEquals("Date type must be one of java.util.Date, java.sql.Date, or com.google.gson.internal.java.sql.Timestamp but was not a Date class", e.getMessage());
        }

        // int constructor with default Locale.US format
        DefaultDateTypeAdapter defaultDateTypeAdapter5 = new DefaultDateTypeAdapter(Date.class, DateFormat.SHORT);
        assertEquals("DefaultDateTypeAdapter(java.util.Date)", defaultDateTypeAdapter5.toString());

        // int constructor with specified Locale
        DefaultDateTypeAdapter defaultDateTypeAdapter6 = new DefaultDateTypeAdapter(java.sql.Date.class, DateFormat.LONG, Locale.FRENCH);
        assertEquals("DefaultDateTypeAdapter(java.sql.Date)", defaultDateTypeAdapter6.toString());
    }

    @Test
    public void testDefaultDateTypeAdapterWrite() throws IOException {
        DefaultDateTypeAdapter defaultDateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
        JsonWriter jsonWriter = new JsonWriter(System.out);

        Date date = new Date();
        defaultDateTypeAdapter.write(jsonWriter, date);
        jsonWriter.flush();

        System.out.println(jsonWriter.toString()); // prints "2023-03-23T14:36:24.935Z" (current date and time)

        // Test writing null value
        jsonWriter = new JsonWriter(System.out);
        defaultDateTypeAdapter.write(jsonWriter, null);
        jsonWriter.flush();

        System.out.println(jsonWriter.toString()); // prints "null"
    }

    @Test
    public void testDefaultDateTypeAdapterReadDate() throws IOException {
        DefaultDateTypeAdapter defaultDateTypeAdapter = new DefaultDateTypeAdapter(Date.class);

        JsonReader jsonReader = new JsonReader(new StringReader("\"2023-03-23T14:36:24.935Z\""));
        Date date = defaultDateTypeAdapter.read(jsonReader);

        assertNotNull(date);
        assertEquals("Thu Mar 23 14:36:24 GMT 2023", date.toString());
    }

    @Test
    public void testDefaultDateTypeAdapterReadJavaSqlDate() throws IOException, SQLException {
        DefaultDateTypeAdapter defaultDateTypeAdapter = new DefaultDateTypeAdapter(java.sql.Date.class);

        JsonReader jsonReader = new JsonReader(new StringReader("\"2023-03-23\""));
        java.sql.Date date = (java.sql.Date) defaultDateTypeTypeAdapter.read(jsonReader);

        assertNotNull(date);
        assertEquals("Thu Mar 23 00:00:00 GMT 2023", new SimpleDateFormat("E MMM dd HH:mm:ss zzz yyyy").format(date.getTime()));
    }

    @Test
    public void testDefaultDateTypeAdapterReadTimestamp() throws IOException {
        DefaultDateTypeAdapter defaultDateTypeAdapter = new DefaultDateTypeAdapter(Timestamp.class);

        JsonReader jsonReader = new JsonReader(new StringReader("\"2023-03-23T14:36:24.935Z\""));
        Timestamp timestamp = (Timestamp) defaultDateTypeAdapter.read(jsonReader);

        assertNotNull(timestamp);
        assertEquals("Thu Mar 23 14:36:24 GMT 2023", new SimpleDateFormat("E MMM dd HH:mm:ss zzz yyyy").format(timestamp.toDate()));
    }

    @Test
    public void testDefaultDateTypeAdapterReadNull() throws IOException {
        DefaultDateTypeAdapter defaultDateTypeAdapter = new DefaultDateTypeAdapter(Date.class);

        JsonReader jsonReader = new JsonReader(new StringReader("null"));
        Date date = defaultDateTypeAdapter.read(jsonReader);

        assertNull(date);
    }

    @Test
    public void testDefaultDateTypeAdapterParseDate() {
        DefaultDateTypeAdapter defaultDateTypeAdapter = new DefaultDateTypeAdapter(Date.class);

        String dateString = "\"2023-03-23T14:36:24.935Z\"";
        Date date = defaultDateTypeAdapter.deserializeToDate(dateString);

        assertNotNull(date);
        assertEquals("Thu Mar 23 14:36:24 GMT 2023", date.toString());
    }

    @Test
    public void testDefaultDateTypeAdapterParseEmptyString() {
        DefaultDateTypeAdapter defaultDateTypeAdapter = new DefaultDateTypeAdapter(Date.class);

        String emptyString = "\"\"";
        Date date = defaultDateTypeAdapter.deserializeToDate(emptyString);

        assertNull(date);
    }

    @Test
    public void testDefaultDateTypeAdapterParseISO8601() {
        DefaultDateTypeAdapter defaultDateTypeAdapter = new DefaultDateTypeAdapter(Date.class);

        String iso8601DateString = "\"2023-03-23T14:36:24.935Z\"";
        Date date = defaultDateTypeAdapter.deserializeToDate(iso8601DateString);

        assertNotNull(date);
        assertEquals("Thu Mar 23 14:36:24 GMT 2023", date.toString());

        String iso8601DateTimeString = "\"2023-03-23T14:36:24.935+01:00\"";
        Date dateWithTimeZone = defaultDateTypeAdapter.deserializeToDate(iso8601DateTimeString);

        assertNotNull(dateWithTimeZone);
        assertEquals("Fri Mar 24 03:36:24 CET 2023", new SimpleDateFormat("E MMM dd HH:mm:ss zzz yyyy").format(dateWithTimeZone.getTime()));
    }

    @Test
    public void testDefaultDateTypeAdapterParseInvalidDate() {
        DefaultDateTypeAdapter defaultDateTypeAdapter = new DefaultDateTypeAdapter(Date.class);

        String invalidDateString = "\"invalid\"";

        try {
            defaultDateTypeAdapter.deserializeToDate(invalidDateString);
            assert false : "Should throw a JsonSyntaxException for invalid date string";
        } catch (JsonSyntaxException e) {
            // Expected
        }
    }
}