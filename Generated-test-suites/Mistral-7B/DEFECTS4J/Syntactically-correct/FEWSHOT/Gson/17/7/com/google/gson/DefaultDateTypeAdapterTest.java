package com.google.gson.internal.bind.util; // Import ISO8601Utils for testing

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.junit.Test;

public class DefaultDateTypeAdapterTest {

    @Test
    public void testDefaultDateTypeAdapterConstructionWithDateClass() {
        DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(Date.class);
        assertEquals(Date.class, dateAdapter.dateType);
        assertEquals(DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US), dateAdapter.enUsFormat);
        assertEquals(DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT), dateAdapter.localFormat);
    }

    @Test
    public void testDefaultDateTypeAdapterConstructionWithTimestampClass() {
        DefaultDateTypeAdapter timestampAdapter = new DefaultDateTypeAdapter(Timestamp.class);
        assertEquals(Timestamp.class, timestampAdapter.dateType);
        assertEquals(DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US), timestampAdapter.enUsFormat);
        assertEquals(DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT), timestampAdapter.localFormat);
    }

    @Test
    public void testDefaultDateTypeAdapterConstructionWithDateClassAndCustomFormat() {
        String datePattern = "dd-MM-yyyy hh:mm a";
        DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(Date.class, datePattern);
        assertEquals(Date.class, dateAdapter.dateType);
        SimpleDateFormat customFormat = new SimpleDateFormat(datePattern, Locale.US);
        assertEquals(customFormat, dateAdapter.enUsFormat);
        assertEquals(customFormat, dateAdapter.localFormat);
    }

    @Test
    public void testDefaultDateTypeAdapterConstructionWithDateClassAndUSStyle() {
        DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(Date.class, DateFormat.US);
        assertEquals(Date.class, dateAdapter.dateType);
        assertEquals(DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.US), dateAdapter.enUsFormat);
        assertEquals(DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT), dateAdapter.localFormat);
    }

    @Test
    public void testDefaultDateTypeAdapterConstructionWithDateClassAndCustomDateStyleAndTimeStyle() {
        int dateStyle = DateFormat.FULL;
        int timeStyle = DateFormat.SHORT;
        DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(Date.class, dateStyle, timeStyle);
        assertEquals(Date.class, dateAdapter.dateType);
        DateFormat customFormat = DateFormat.getDateTimeInstance(dateStyle, timeStyle, Locale.US);
        assertEquals(customFormat, dateAdapter.enUsFormat);
        assertEquals(customFormat, dateAdapter.localFormat);
    }

    @Test
    public void testDefaultDateTypeAdapterWrite() throws IOException {
        Date date = new Date(System.currentTimeMillis());
        DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(Date.class);
        String expectedDateFormat = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US).format(date);

        JsonWriter writer = new JsonWriter(System.out);
        dateAdapter.write(writer, date);
        writer.flush();

        assertEquals(expectedDateFormat, System.out.getBytes().toString());
    }

    @Test
    public void testDefaultDateTypeAdapterReadDateClass() throws IOException {
        DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(Date.class);
        Date date = new Date(System.currentTimeMillis());
        String dateString = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US).format(date);

        JsonReader reader = new JsonReader(new StringReader(dateString));
        Date readDate = dateAdapter.read(reader);
        assertEquals(date, readDate);
    }

    @Test
    public void testDefaultDateTypeAdapterReadTimestampClass() throws IOException {
        DefaultDateTypeAdapter timestampAdapter = new DefaultDateTypeAdapter(Timestamp.class);
        Date date = new Date(System.currentTimeMillis());
        long time = date.getTime();
        String timestampString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(date);

        JsonReader reader = new JsonReader(new StringReader(timestampString));
        Timestamp readTimestamp = timestampAdapter.read(reader);
        assertEquals(new Timestamp(time), readTimestamp);
    }

    @Test
    public void testDefaultDateTypeAdapterReadSqlDateClass() throws IOException {
        DefaultDateTypeAdapter sqlDateAdapter = new DefaultDateTypeAdapter(java.sql.Date.class);
        Date date = new Date(System.currentTimeMillis());
        long time = date.getTime();
        String sqlDateString = new SimpleDateFormat("yyyy-MM-dd").format(date);

        JsonReader reader = new JsonReader(new StringReader(sqlDateString));
        java.sql.Date readSqlDate = sqlDateAdapter.read(reader);
        assertEquals(new java.sql.Date(time), readSqlDate);
    }

    @Test
    public void testDefaultDateTypeAdapterReadInvalidString() throws IOException {
        DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(Date.class);
        String invalidString = "invalid";

        JsonReader reader = new JsonReader(new StringReader(invalidString));
        assertThrows(JsonParseException.class, () -> dateAdapter.read(reader));
    }

    @Test
    public void testDefaultDateTypeAdapterReadNull() throws IOException {
        DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(Date.class);

        JsonReader reader = new JsonReader(new StringReader("null"));
        Date readDate = dateAdapter.read(reader);
        assertNull(readDate);
    }

    @Test
    public void testDefaultDateTypeAdapterSerializationWithISO8601Utils() throws IOException, ParseException {
        DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(Date.class);
        Date date = new Date(System.currentTimeMillis());
        String dateString = ISO8601Utils.ISO8601_DATE_FORMAT.format(date);

        JsonWriter writer = new JsonWriter(System.out);
        dateAdapter.write(writer, date);
        writer.flush();

        JsonReader reader = new JsonReader(new StringReader(dateString));
        Date readDate = dateAdapter.read(reader);
        assertEquals(date, readDate);
    }

    @Test
    public void testDefaultDateTypeAdapterSerializationWithCustomFormat() throws IOException, ParseException {
        String datePattern = "dd-MM-yyyy hh:mm a";
        DefaultDateTypeAdapter dateAdapter = new DefaultDateTypeAdapter(Date.class, datePattern);

        Date date = new Date(System.currentTimeMillis());
        String dateString = new SimpleDateFormat(datePattern, Locale.US).format(date);

        JsonWriter writer = new JsonWriter(System.out);
        dateAdapter.write(writer, date);
        writer.flush();

        JsonReader reader = new JsonReader(new StringReader(dateString));
        Date readDate = dateAdapter.read(reader);
        assertEquals(date, readDate);
    }
}