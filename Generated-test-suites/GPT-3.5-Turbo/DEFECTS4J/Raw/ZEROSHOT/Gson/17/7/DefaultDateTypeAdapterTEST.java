package com.google.gson;

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

import static org.junit.Assert.*;

public class DefaultDateTypeAdapterTest {

    @Test
    public void testWrite_nullValue() throws IOException {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        JsonWriter writer = new JsonWriter(null);
        adapter.write(writer, null);
        writer.close();
        assertEquals(JsonToken.NULL, writer.peek());
    }

    @Test
    public void testWrite() throws IOException {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        JsonWriter writer = new JsonWriter(null);
        Date date = new Date(1605916800000L); // November 21, 2020 12:00:00 AM GMT
        adapter.write(writer, date);
        writer.close();
        assertEquals("Nov 21, 2020 12:00:00 AM", writer.toString());
    }

    @Test
    public void testRead_invalidInput() throws IOException {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        JsonReader reader = new JsonReader(null);
        reader.beginObject();
        reader.nextName();
        reader.value("Invalid Date");
        reader.endObject();
        reader.close();
        try {
            adapter.read(reader);
            fail("Expected JsonParseException to be thrown");
        } catch (JsonParseException e) {
            assertEquals("The date should be a string value", e.getMessage());
        }
    }

    @Test
    public void testRead() throws IOException {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        JsonReader reader = new JsonReader(null);
        reader.beginObject();
        reader.nextName();
        reader.value("Nov 21, 2020 12:00:00 AM");
        reader.endObject();
        reader.close();
        Date expectedDate = new Date(1605916800000L); // November 21, 2020 12:00:00 AM GMT
        assertEquals(expectedDate, adapter.read(reader));
    }

    @Test
    public void testDeserializeToDate_localFormat() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        String dateString = "Nov 21, 2020 12:00:00 AM";
        Date expectedDate = new Date(1605916800000L); // November 21, 2020 12:00:00 AM GMT
        assertEquals(expectedDate, adapter.deserializeToDate(dateString));
    }

    @Test
    public void testDeserializeToDate_enUsFormat() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        String dateString = "2020-11-21T00:00:00Z";
        Date expectedDate = new Date(1605916800000L); // November 21, 2020 12:00:00 AM GMT
        assertEquals(expectedDate, adapter.deserializeToDate(dateString));
    }

    @Test
    public void testDeserializeToDate_ISO8601Utils() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        String dateString = "2020-11-21T00:00:00Z";
        Date expectedDate = new Date(1605916800000L); // November 21, 2020 12:00:00 AM GMT
        assertEquals(expectedDate, adapter.deserializeToDate(dateString));
    }

    @Test
    public void testToString() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        assertEquals("DefaultDateTypeAdapter(SimpleDateFormat)", adapter.toString());
    }
}