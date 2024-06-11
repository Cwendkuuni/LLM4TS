package com.google.gson.internal.util; // Assuming ISO8601Utils is in this package

import static org.junit.Assert.*;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.JsonReader;
import com.google.gson.JsonToken;
import com.google.gson.JsonWriter;
import com.google.gson.stream.JsonParser;

public class DefaultDateTypeAdapterTest {

    @Test
    public void testDefaultConstructor() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        assertEquals(Date.class, adapter.dateType);
        assertEquals(DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US), adapter.enUsFormat);
        assertEquals(DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT), adapter.localFormat);
    }

    @Test
    public void testStringConstructor() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter("DefaultDateTypeAdapter", "yyyy-MM-dd HH:mm:ss");
        assertEquals("DefaultDateTypeAdapter", adapter.getClass().getSimpleName());
        assertEquals("yyyy-MM-dd HH:mm:ss", adapter.enUsFormat.toPattern());
        assertEquals("yyyy-MM-dd HH:mm:ss", adapter.localFormat.toPattern());
    }

    @Test
    public void testIntConstructor() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, DateFormat.ABBREVIATED);
        assertEquals(Date.class, adapter.dateType);
        assertEquals(DateFormat.getDateInstance(DateFormat.ABBREVIATED, Locale.US), adapter.enUsFormat);
        assertEquals(DateFormat.getDateInstance(DateFormat.ABBREVIATED), adapter.localFormat);
    }

    @Test
    public void testIntIntConstructor() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, DateFormat.ABBREVIATED, DateFormat.SHORT);
        assertEquals(Date.class, adapter.dateType);
        assertEquals(DateFormat.getDateTimeInstance(DateFormat.ABBREVIATED, DateFormat.SHORT, Locale.US), adapter.enUsFormat);
        assertEquals(DateFormat.getDateTimeInstance(DateFormat.ABBREVIATED, DateFormat.SHORT), adapter.localFormat);
    }

    @Test
    public void testWrite() throws IOException {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        JsonWriter writer = new JsonWriter(System.out);
        adapter.write(writer, new Date(System.currentTimeMillis()));
        writer.flush();
        System.out.flush();
    }

    @Test
    public void testReadDate() throws IOException {
        JsonReader reader = new JsonReader(new JsonParser().parse("{\"date\":\"2022-12-31 12:00:00\"}")).asJsonReader();
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        Date date = adapter.read(reader);
        assertEquals(new Date(System.currentTimeMillis()), date);
    }

    @Test
    public void testReadTimestamp() throws IOException {
        JsonReader reader = new JsonReader(new JsonParser().parse("{\"timestamp\":\"2022-12-31T12:00:00.000Z\"}")).asJsonReader();
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Timestamp.class);
        Timestamp timestamp = adapter.read(reader);
        assertEquals(new Timestamp(System.currentTimeMillis()), timestamp);
    }

    @Test
    public void testReadSqlDate() throws IOException {
        JsonReader reader = new JsonReader(new JsonParser().parse("{\"sqlDate\":\"2022-12-31\"}")).asJsonReader();
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(java.sql.Date.class);
        java.sql.Date sqlDate = adapter.read(reader);
        assertEquals(new java.sql.Date(System.currentTimeMillis()), sqlDate);
    }

    @Test(expected = JsonIOException.class)
    public void testReadInvalidJson() throws IOException {
        JsonReader reader = new JsonReader(new JsonParser().parse("{\"date\":\"abc\"}")).asJsonReader();
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        adapter.read(reader);
    }

    @Test(expected = JsonIOException.class)
    public void testReadNullJson() throws IOException {
        JsonReader reader = new JsonReader(new JsonParser().parse("{\"date\":null}")).asJsonReader();
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        adapter.read(reader);
    }

    @Test(expected = JsonSyntaxException.class)
    public void testReadEmptyJson() throws IOException {
        JsonReader reader = new JsonReader(new JsonParser().parse("{}")).asJsonReader();
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        adapter.read(reader);
    }

    @Test(expected = JsonParseException.class)
    public void testReadInvalidDateFormat() throws IOException {
        JsonReader reader = new JsonReader(new JsonParser().parse("{\"date\":\"31-12-2022 12:00:00\"}")).asJsonReader();
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        adapter.read(reader);
    }
}