package com.google.gson.internal.bind.test.util; // Assuming a package named "com.google.gson.internal.bind.test.util" for the test cases

import static com.google.gson.internal.bind.util.ISO8601Utils.parse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class DefaultDateTypeAdapterTest {

    private static final String SIMPLE_NAME = "DefaultDateTypeAdapter";

    @Test
    public void testDefaultDateTypeAdapter() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        assertEquals(SIMPLE_NAME + "(java.util.Date)", adapter.toString());

        adapter = new DefaultDateTypeAdapter(Timestamp.class);
        assertEquals(SIMPLE_NAME + "(java.sql.Timestamp)", adapter.toString());

        adapter = new DefaultDateTypeAdapter(java.sql.Date.class);
        assertEquals(SIMPLE_NAME + "(java.sql.Date)", adapter.toString());
    }

    @Test
    public void testDefaultDateTypeAdapterWithDateFormat() {
        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US);
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, dateFormat, dateFormat);
        assertEquals(SIMPLE_NAME + "(java.util.Date, " + dateFormat.getClass().getSimpleName() + ")", adapter.toString());
    }

    @Test
    public void testDefaultDateTypeAdapterWithDateFormatAndTimeZone() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, dateFormat, dateFormat);
        assertEquals(SIMPLE_NAME + "(java.util.Date, " + dateFormat.getClass().getSimpleName() + ")", adapter.toString());
    }

    @Test
    public void testWrite() throws IOException {
        Gson gson = new Gson();
        JsonWriter writer = new JsonWriter(System.out);
        Date date = new Date();
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        adapter.write(writer, date);
        writer.flush();
        assertEquals("2023-03-24 14:39:29.555", System.console().readLine());

        writer = new JsonWriter(new OutputStreamWriter(System.out));
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        adapter = new DefaultDateTypeAdapter(Timestamp.class);
        adapter.write(writer, timestamp);
        writer.flush();
        assertEquals("2023-03-24 14:39:29.555Z", System.console().readLine());

        writer = new JsonWriter(new OutputStreamWriter(System.out));
        java.sql.Date sqlDate = new java.sql.Date(System.currentTimeMillis());
        adapter = new DefaultDateTypeAdapter(java.sql.Date.class);
        adapter.write(writer, sqlDate);
        writer.flush();
        assertEquals("2023-03-24", System.console().readLine());
    }

    @Test
    public void testReadDate() {
        String jsonString = "\"2023-03-24\"";
        JsonParser jsonParser = new JsonParser();
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        Date date = adapter.read(jsonParser.parse(jsonString));
        assertEquals("Fri Mar 24 14:39:29 EDT 2023", date.toString());
    }

    @Test
    public void testReadTimestamp() {
        String jsonString = "\"2023-03-24 14:39:29.555Z\"";
        JsonParser jsonParser = new JsonParser();
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Timestamp.class);
        Timestamp timestamp = adapter.read(jsonParser.parse(jsonString));
        assertEquals("Fri Mar 24 14:39:29 EDT 2023", new Timestamp(timestamp.getTime()).toString());
    }

    @Test
    public void testReadSqlDate() {
        String jsonString = "\"2023-03-24\"";
        JsonParser jsonParser = new JsonParser();
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(java.sql.Date.class);
        java.sql.Date sqlDate = adapter.read(jsonParser.parse(jsonString));
        assertEquals("Fri Mar 24 00:00:00 EDT 2023", new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").format(sqlDate));
    }

    @Test(expected = JsonParseException.class)
    public void testReadInvalidJsonDate() {
        String jsonString = "\"Invalid Date\"";
        JsonParser jsonParser = new JsonParser();
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        adapter.read(jsonParser.parse(jsonString));
    }

    @Test(expected = JsonSyntaxException.class)
    public void testReadInvalidJsonString() {
        String jsonString = "\"Invalid_String\"";
        JsonParser jsonParser = new JsonParser();
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        adapter.read(jsonParser.parse(jsonString));
    }

    @Test
    public void testDeserializeToDate() {
        String dateString = "\"2023-03-24\"";
        Date expectedDate = new Date();
        Date actualDate = new DefaultDateTypeAdapter(Date.class).deserializeToDate(dateString);
        assertEquals(expectedDate, actualDate);

        String timestampString = "\"2023-03-24 14:39:29.555Z\"";
        expectedDate = new Date(System.currentTimeMillis() + 16 * 60 * 60 * 1000); // 16 hours ahead of the current time
        actualDate = new DefaultDateTypeAdapter(Date.class).deserializeToDate(timestampString);
        assertEquals(expectedDate, actualDate);

        String sqlDateString = "\"2023-03-24\"";
        java.sql.Date expectedSqlDate = new java.sql.Date(System.currentTimeMillis());
        java.sql.Date actualSqlDate = new DefaultDateTypeAdapter(java.sql.Date.class).deserializeToDate(sqlDateString);
        assertEquals(expectedSqlDate, actualSqlDate);

        // Test with custom DateFormat
        SimpleDateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss zzz");
        expectedDate = customFormat.parse("2023-03-24 14:39:29 EDT");
        actualDate = new DefaultDateTypeAdapter(Date.class, customFormat, customFormat).deserializeToDate("\"2023-03-24 14:39:29 EDT\"");
        assertEquals(expectedDate, actualDate);
    }
}