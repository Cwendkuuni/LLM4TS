package com.google.gson.internal.bind.util; // Added for ISO8601Utils test

import static com.google.gson.stream.JsonWriter.NULL_VALUE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonReader;
import com.google.gson.JsonSyntaxException;
import com.google.gson.JsonWriter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.stream.JsonParser;

import java.io.StringReader;
import java.io.StringWriter;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

public class DefaultDateTypeAdapterTest {
    @Test
    public void testDefaultConstructor() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter();
        assertNotNull(adapter);
        assertEquals(Date.class, adapter.dateType);
        assertEquals(DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US), adapter.enUsFormat);
        assertEquals(DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT), adapter.localFormat);
    }

    @Test
    public void testCustomDatePatternConstructor() {
        String datePattern = "yyyy-MM-dd HH:mm:ss.SSS";
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, datePattern);
        assertEquals(Date.class, adapter.dateType);
        assertEquals(new SimpleDateFormat(datePattern, Locale.US), adapter.enUsFormat);
        assertEquals(new SimpleDateFormat(datePattern), adapter.localFormat);
    }

    @Test
    public void testDateStyleConstructor() {
        int dateStyle = Calendar.SHORT;
        int timeStyle = Calendar.SHORT;
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(dateStyle, timeStyle);
        assertEquals(Date.class, adapter.dateType);
        assertEquals(DateFormat.getDateTimeInstance(dateStyle, timeStyle, Locale.US), adapter.enUsFormat);
        assertEquals(DateFormat.getDateTimeInstance(dateStyle, timeStyle), adapter.localFormat);
    }

    @Test
    public void testWriteDate() throws IOException {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter();
        Date date = new Date();
        StringWriter out = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(out);
        jsonWriter.setLenient(true);
        adapter.write(jsonWriter, date);
        assertEquals("Mon Feb 27 17:53:46 EDT 2023", out.toString());
    }

    @Test
    public void testWriteTimestamp() throws IOException {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Timestamp.class);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        StringWriter out = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(out);
        adapter.write(jsonWriter, timestamp);
        assertEquals(ISO8601Utils.format(timestamp), out.toString());
    }

    @Test
    public void testWriteSqlDate() throws IOException {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(java.sql.Date.class);
        java.sql.Date sqlDate = new java.sql.Date(System.currentTimeMillis());
        StringWriter out = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(out);
        adapter.write(jsonWriter, sqlDate);
        assertEquals(ISO8601Utils.format(sqlDate), out.toString());
    }

    @Test
    public void testReadDate() throws IOException {
        String jsonDate = "\"2023-02-27T17:53:46.000Z\"";
        Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new DefaultDateTypeAdapter()).create();
        JsonElement jsonElement = gson.fromJson(jsonDate, JsonElement.class);
        JsonReader jsonReader = new JsonReader(new StringReader(jsonDate));
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonParserElement = jsonParser.parse(jsonDate);
        Date date = new DefaultDateTypeAdapter().read(jsonReader);
        assertEquals(jsonElement.getAsJsonPrimitive().getAsString(), date.toString());
        assertEquals(jsonParserElement.getAsJsonPrimitive().getAsString(), date.toString());
    }

    @Test
    public void testReadTimestamp() throws IOException {
        String jsonTimestamp = "\"2023-02-27T17:53:46.000Z\"";
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Timestamp.class, new DefaultDateTypeAdapter(Timestamp.class))
                .create();
        JsonElement jsonElement = gson.fromJson(jsonTimestamp, JsonElement.class);
        JsonReader jsonReader = new JsonReader(new StringReader(jsonTimestamp));
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonParserElement = jsonParser.parse(jsonTimestamp);
        Timestamp timestamp = new DefaultDateTypeAdapter(Timestamp.class).read(jsonReader);
        assertEquals(jsonElement.getAsJsonPrimitive().getAsString(), ISO8601Utils.format(timestamp));
        assertEquals(jsonParserElement.getAsJsonPrimitive().getAsString(), ISO8601Utils.format(timestamp));
    }

    @Test
    public void testReadSqlDate() throws IOException {
        String jsonSqlDate = "\"2023-02-27\"";
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(java.sql.Date.class, new DefaultDateTypeAdapter(java.sql.Date.class))
                .create();
        JsonElement jsonElement = gson.fromJson(jsonSqlDate, JsonElement.class);
        JsonReader jsonReader = new JsonReader(new StringReader(jsonSqlDate));
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonParserElement = jsonParser.parse(jsonSqlDate);
        java.sql.Date sqlDate = new DefaultDateTypeAdapter(java.sql.Date.class).read(jsonReader);
        assertEquals(jsonElement.getAsJsonPrimitive().getAsString(), ISO8601Utils.format(sqlDate.toDate()));
        assertEquals(jsonParserElement.getAsJsonPrimitive().getAsString(), ISO8601Utils.format(sqlDate.toDate()));
    }

    @Test(expected = JsonParseException.class)
    public void testInvalidJsonDate() throws IOException {
        String jsonDate = "\"Mon Feb 27 17:53:46 EDT 2023\""; // invalid format
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DefaultDateTypeAdapter())
                .create();
        JsonReader jsonReader = new JsonReader(new StringReader(jsonDate));
        new DefaultDateTypeAdapter().read(jsonReader);
    }

    @Test(expected = JsonParseException.class)
    public void testInvalidJsonTimestamp() throws IOException {
        String jsonTimestamp = "\"2023-02-27 17:53:46.000Zfoo\""; // invalid format
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Timestamp.class, new DefaultDateTypeAdapter(Timestamp.class))
                .create();
        JsonReader jsonReader = new JsonReader(new StringReader(jsonTimestamp));
        new DefaultDateTypeAdapter(Timestamp.class).read(jsonReader);
    }

    @Test(expected = JsonSyntaxException.class)
    public void testInvalidJsonSqlDate() throws IOException {
        String jsonSqlDate = "\"Mon Feb 27 17:53:46 EDT 2023\""; // invalid format
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(java.sql.Date.class, new DefaultDateTypeAdapter(java.sql.Date.class))
                .create();
        JsonReader jsonReader = new JsonReader(new StringReader(jsonSqlDate));
        new DefaultDateTypeAdapter(java.sql.Date.class).read(jsonReader);
    }
}