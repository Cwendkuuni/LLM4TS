package com.google.gson;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class DefaultDateTypeAdapterTest {

    @Test
    public void testDefaultDateTypeAdapter() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        assertEquals("DefaultDateTypeAdapter(Date)", adapter.toString());
        assertNotNull(adapter);
    }

    @Test
    public void testDefaultDateTypeAdapterWithUsFormat() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter("", Date.class);
        assertEquals("DefaultDateTypeAdapter(Date, SimpleDateFormat[en_US, format=\"dd/MM/yyyy hh:mm aa\"])", adapter.toString());
        assertNotNull(adapter);
    }

    @Test
    public void testDefaultDateTypeAdapterWithCustomFormat() {
        String datePattern = "dd-MM-yyyy HH:mm:ss";
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(datePattern, Date.class);
        assertEquals("DefaultDateTypeAdapter(Date, SimpleDateFormat[, format=" + datePattern + "])", adapter.toString());
        assertNotNull(adapter);
    }

    @Test
    public void testDefaultDateTypeAdapterWithDateStyles() {
        int dateStyle = DateFormat.SHORT;
        int timeStyle = DateFormat.SHORT;
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(dateStyle, timeStyle, Date.class);
        assertEquals("DefaultDateTypeAdapter(Date, DateFormat[DateFormat.SHORT, DateFormat.SHORT], SimpleDateFormat[, format=\"dd-MM-yy hh:mm a\"])", adapter.toString());
        assertNotNull(adapter);
    }

    @Test
    public void testDefaultDateTypeAdapterWithDateStylesAndCustomFormat() {
        int dateStyle = DateFormat.SHORT;
        int timeStyle = DateFormat.SHORT;
        String datePattern = "dd-MM-yyyy HH:mm:ss";
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(dateStyle, timeStyle, datePattern, Date.class);
        assertEquals("DefaultDateTypeAdapter(Date, DateFormat[DateFormat.SHORT, DateFormat.SHORT], SimpleDateFormat[, format=" + datePattern + "])", adapter.toString());
        assertNotNull(adapter);
    }

    @Test
    public void testWriteDate() throws IOException {
        Date date = new Date();
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);

        String expectedJson = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a").format(date);
        JsonWriter writer = new JsonWriter(System.out);
        adapter.write(writer, date);
        writer.flush();

        assertEquals(expectedJson, writer.toString());
    }

    @Test
    public void testWriteTimestamp() throws IOException {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        DefaultDateTypeAdapter<Timestamp> adapter = new DefaultDateTypeAdapter<>(Timestamp.class);

        String expectedJson = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(timestamp);
        JsonWriter writer = new JsonWriter(System.out);
        adapter.write(writer, timestamp);
        writer.flush();

        assertEquals(expectedJson, writer.toString());
    }

    @Test
    public void testWriteSqlDate() throws IOException {
        java.sql.Date sqlDate = new java.sql.Date(System.currentTimeMillis());
        DefaultDateTypeAdapter<java.sql.Date> adapter = new DefaultDateTypeAdapter<>(java.sql.Date.class);

        String expectedJson = new SimpleDateFormat("yyyy-MM-dd").format(sqlDate);
        JsonWriter writer = new JsonWriter(System.out);
        adapter.write(writer, sqlDate);
        writer.flush();

        assertEquals(expectedJson, writer.toString());
    }

    @Test
    public void testReadDate() throws IOException {
        String jsonDate = "\"2023-03-15 12:30:45\"";
        JsonElement jsonElement = new JsonParser().parse(jsonDate);
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);

        Date expectedDate = new Date(1678832545000L);
        Date date = adapter.read(new JsonReader(new StringReader(jsonDate)));
        assertEquals(expectedDate, date);
    }

    @Test
    public void testReadTimestamp() throws IOException, ParseException {
        String jsonTimestamp = "\"2023-03-15T12:30:45.123Z\"";
        JsonElement jsonElement = new JsonParser().parse(jsonTimestamp);
        DefaultDateTypeAdapter<Timestamp> adapter = new DefaultDateTypeAdapter<>(Timestamp.class);

        Timestamp expectedTimestamp = new Timestamp(1678832545123L);
        Date date = adapter.read(new JsonReader(new StringReader(jsonTimestamp)));
        assertEquals(expectedTimestamp, new Timestamp(date.getTime()));
    }

    @Test
    public void testReadSqlDate() throws IOException, ParseException {
        String jsonSqlDate = "\"2023-03-15\"";
        JsonElement jsonElement = new JsonParser().parse(jsonSqlDate);
        DefaultDateTypeAdapter<java.sql.Date> adapter = new DefaultDateTypeAdapter<>(java.sql.Date.class);

        java.sql.Date expectedSqlDate = new java.sql.Date(1678832545000L);
        Date date = adapter.read(new JsonReader(new StringReader(jsonSqlDate)));
        assertEquals(expectedSqlDate, new Date(date.getTime()));
    }

    @Test(expected = JsonParseException.class)
    public void testReadInvalidJsonString() throws IOException {
        String jsonString = "\"InvalidJsonString\"";
        JsonElement jsonElement = new JsonParser().parse(jsonString);
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);

        adapter.read(new JsonReader(new StringReader(jsonString)));
    }

    @Test(expected = JsonSyntaxException.class)
    public void testReadInvalidDateFormat() throws IOException, ParseException {
        String jsonDate = "\"2023-03-15/12:30:45\"";
        JsonElement jsonElement = new JsonParser().parse(jsonDate);
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);

        adapter.read(new JsonReader(new StringReader(jsonDate)));
    }

    @Test(expected = AssertionError.class)
    public void testReadInvalidDateType() throws IOException, ParseException {
        String jsonString = "\"InvalidJsonString\"";
        JsonElement jsonElement = new JsonParser().parse(jsonString);
        DefaultDateTypeAdapter<String> adapter = new DefaultDateTypeAdapter<>(String.class);

        adapter.read(new JsonReader(new StringReader(jsonString)));
    }

    @Test
    public void testReadNullJsonString() throws IOException {
        String jsonString = "null";
        JsonElement jsonElement = new JsonParser().parse(jsonString);
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);

        assertNull(adapter.read(new JsonReader(new StringReader(jsonString))));
    }
}