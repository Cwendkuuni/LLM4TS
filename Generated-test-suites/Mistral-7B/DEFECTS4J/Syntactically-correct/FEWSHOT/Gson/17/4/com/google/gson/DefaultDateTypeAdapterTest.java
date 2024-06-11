package com.google.gson.internal.bind.util.test.typeadapter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class DefaultDateTypeAdapterTest {

    @Test
    public void testDefaultDateTypeAdapter() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        assertEquals(Date.class, adapter.getType());
    }

    @Test
    public void testDefaultDateTypeAdapterWithDatePattern() {
        String datePattern = "dd-MM-yyyy HH:mm:ss.SSSSSS";
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, datePattern);
        assertEquals(Date.class, adapter.getType());
        assertEquals(datePattern, adapter.getDateFormat(Locale.US).toPattern());
    }

    @Test
    public void testDefaultDateTypeAdapterWithDateStyle() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, Calendar.DATE, Calendar.HOUR_OF_DAY);
        assertEquals(Date.class, adapter.getType());
        assertSame(DateFormat.getDateInstance(Calendar.DATE, Locale.US), adapter.localFormat);
        assertSame(DateFormat.getDateInstance(Calendar.DATE, Locale.US), adapter.enUsFormat);
    }

    @Test
    public void testDefaultDateTypeAdapterWithDateStyleAndTimeStyle() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, Calendar.DATE, Calendar.HOUR_OF_DAY, Calendar.MINUTE, Calendar.SECOND);
        assertEquals(Date.class, adapter.getType());
        assertSame(DateFormat.getDateTimeInstance(Calendar.DATE, Calendar.HOUR_OF_DAY, Calendar.MINUTE, Calendar.SECOND, Locale.US), adapter.localFormat);
        assertSame(DateFormat.getDateTimeInstance(Calendar.DATE, Calendar.HOUR_OF_DAY, Calendar.MINUTE, Calendar.SECOND), adapter.enUsFormat);
    }

    @Test
    public void testDefaultDateTypeAdapterWrite() throws IOException {
        Gson gson = new Gson();
        JsonWriter writer = new JsonWriter(System.out);
        Date date = new Date();
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        adapter.write(writer, date);
        writer.flush();
        JsonElement json = JsonParser.parseString(System.in).getAsJsonObject();
        assertEquals("\"2023-03-22 15:23:55.678\"", json.get("date").getAsJsonPrimitive().getAsString());
    }

    @Test
    public void testDefaultDateTypeAdapterReadDate() throws IOException {
        String jsonString = "{\"date\":\"2023-03-22 15:23:55.678\"}";
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        Date date = adapter.read(new JsonParser().parse(jsonString).getAsJsonObject().get("date").getAsJsonPrimitive().getAsString());
        assertEquals(new Date(1679397135678L), date);
    }

    @Test
    public void testDefaultDateTypeAdapterReadTimestamp() throws IOException {
        String jsonString = "{\"timestamp\":\"2023-03-22 15:23:55.678000\"}";
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Timestamp.class);
        Timestamp timestamp = (Timestamp) adapter.read(new JsonParser().parse(jsonString).getAsJsonObject().get("timestamp").getAsJsonPrimitive().getAsString());
        assertEquals(new Timestamp(1679397135678L), timestamp);
    }

    @Test
    public void testDefaultDateTypeAdapterReadSqlDate() throws IOException {
        String jsonString = "{\"sqlDate\":\"2023-03-22\"}";
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(java.sql.Date.class);
        java.sql.Date sqlDate = (java.sql.Date) adapter.read(new JsonParser().parse(jsonString).getAsJsonObject().get("sqlDate").getAsJsonPrimitive().getAsString());
        assertEquals(new java.sql.Date(1679303200000L), sqlDate);
    }

    @Test(expected = AssertionError.class)
    public void testDefaultDateTypeAdapterInvalidType() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Integer.class, DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT));
    }

    @Test
    public void testDefaultDateTypeAdapterParseExceptions() throws IOException, ParseException {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT));
        String dateString = "2023-03-22 15:23:55.678";
        Date date = adapter.deserializetoDate(dateString);
        assertEquals(new Date(1679397135678L), date);

        String invalidDateString = "2023-03-22 15:23:55.678X";
        try {
            adapter.deserializetoDate(invalidDateString);
            fail();
        } catch (JsonSyntaxException e) {
            // expected
        }

        String dateWithTimeZoneString = "2023-03-22 15:23:55.678Z";
        Date dateWithTimeZone = adapter.deserializetoDate(dateWithTimeZoneString);
        assertEquals(new Date(1679397135678L), dateWithTimeZone);

        String dateWithOffsetString = "2023-03-22 15:23:55.678+01:00";
        Date dateWithOffset = adapter.deserializetoDate(dateWithOffsetString);
        assertEquals(new Date(1679400735678L), dateWithOffset);
    }
}