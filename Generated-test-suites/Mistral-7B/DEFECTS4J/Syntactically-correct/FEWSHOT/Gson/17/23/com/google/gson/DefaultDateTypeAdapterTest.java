package com.google.gson.internal.serializer; // Assuming the test case class is defined in this package for better organization

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.junit.Test;

public class DefaultDateTypeAdapterTest {

    @Test
    public void testDefaultDateTypeAdapterConstruction() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        assertEquals(Date.class, adapter.getType());
        assertEquals(DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US), adapter.getEnUsFormat());
        assertEquals(DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT), adapter.getLocalFormat());

        adapter = new DefaultDateTypeAdapter(null);
        assertNull(adapter);

        adapter = new DefaultDateTypeAdapter(Timestamp.class);
        assertEquals(Timestamp.class, adapter.getType());
        assertEquals(DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US), adapter.getEnUsFormat());
        assertEquals(DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT), adapter.getLocalFormat());

        adapter = new DefaultDateTypeAdapter(java.sql.Date.class);
        assertEquals(java.sql.Date.class, adapter.getType());
        assertEquals(DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US), adapter.getEnUsFormat());
        assertEquals(DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT), adapter.getLocalFormat());
    }

    @Test
    public void testWrite() throws IOException {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        JsonWriter writer = new JsonWriter(new JsonWriter.OfOutputStream(output));
        adapter.write(writer, new Date(1234567890123L));
        String expectedJson = "2009-12-31T15:16:17.890Z"; // The output format might vary
        assertEquals(expectedJson, output.toString());
    }

    @Test
    public void testReadDate() throws IOException, ParseException {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);

        String jsonDate = "2022-03-31T12:59:59.999Z";
        JsonReader reader = new JsonReader(new InputStreamReader(new ByteArrayInputStream(jsonDate.getBytes()), "UTF-8"));
        Date date = adapter.read(reader);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        Date parsedDate = formatter.parse(jsonDate);
        assertEquals(parsedDate.getTime(), date.getTime());
    }

    @Test
    public void testReadTimestamp() throws IOException, ParseException {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Timestamp.class);

        String jsonTimestamp = "2022-03-31T12:59:59.999Z";
        JsonReader reader = new JsonReader(new InputStreamReader(new ByteArrayInputStream(jsonTimestamp.getBytes()), "UTF-8"));
        Timestamp timestamp = adapter.read(reader);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        Date parsedDate = formatter.parse(jsonTimestamp);
        assertEquals(parsedDate.getTime(), timestamp.getTime());
    }

    @Test
    public void testReadSqlDate() throws IOException, ParseException {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(java.sql.Date.class);

        String jsonSqlDate = "2022-03-31";
        JsonReader reader = new JsonReader(new InputStreamReader(new ByteArrayInputStream(jsonSqlDate.getBytes()), "UTF-8"));
        java.sql.Date sqlDate = (java.sql.Date) adapter.read(reader);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Date parsedDate = formatter.parse(jsonSqlDate);
        assertEquals(parsedDate.getTime(), sqlDate.getTime());
    }

    @Test
    public void testNullValue() throws IOException {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        JsonWriter writer = new JsonWriter(new JsonWriter.OfOutputStream(output));
        adapter.write(writer, null);
        String expectedJson = "null";
        assertEquals(expectedJson, output.toString());

        JsonReader reader = new JsonReader(new InputStreamReader(new ByteArrayInputStream(expectedJson.getBytes()), "UTF-8"));
        Date date = adapter.read(reader);
        assertNull(date);
    }

    @Test
    public void testDeserializeToDate() throws IOException, ParseException {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);

        String dateString1 = "2022-03-31T12:59:59.999Z";
        String dateString2 = "2022-03-31";
        String dateString3 = "2022-01-31"; // Invalid format

        Date date1 = adapter.deserializeToDate(dateString1);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        Date parsedDate1 = formatter.parse(dateString1);
        assertEquals(parsedDate1.getTime(), date1.getTime());

        Date date2 = adapter.deserializeToDate(dateString2);
        SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Date parsedDate2 = formatter2.parse(dateString2);
        assertEquals(parsedDate2.getTime(), date2.getTime());

        Date date3 = adapter.deserializeToDate(dateString3);
        assertNull(date3);
    }

    @Test
    public void testTypeMismatch() throws IOException {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);

        String jsonTimestamp = "2022-03-31T12:59:59.999Z";
        JsonReader reader = new JsonReader(new InputStreamReader(new ByteArrayInputStream(jsonTimestamp.getBytes()), "UTF-8"));

        try {
            Timestamp timestamp = (Timestamp) adapter.read(reader);
            fail("Expected JsonParseException");
        } catch (JsonParseException e) {
            // Expected
        }
    }
}