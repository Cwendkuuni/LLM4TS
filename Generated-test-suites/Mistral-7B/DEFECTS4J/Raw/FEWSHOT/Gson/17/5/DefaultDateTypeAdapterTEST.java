package com.google.gson.internal.tests; // assuming a tests package exists for internal classes

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class DefaultDateTypeAdapterTest {

    private static final String DATE_PATTERN = "dd MMM yyyy HH:mm:ss z";
    private static final String ISO_DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSZZ";
    private static final String VALID_DATE_STRING = "15 Mar 2022 10:30:00 EDT";
    private static final String VALID_ISO_DATE_STRING = "2022-03-15T10:30:00.000-04:00";
    private static final String INVALID_DATE_STRING = "abc";
    private static final String INVALID_ISO_DATE_STRING = "2022-33-15T10:30:00.000-04:00";
    private static final Date VALID_DATE = new Date(1646852800000L);
    private static final Date VALID_ISO_DATE = new Date(1646852800000L - 10800000); // subtract 3 hours to test the parsing of the timezone
    private static final Timestamp VALID_TIMESTAMP = new Timestamp(1646852800000L);
    private static final Timestamp VALID_ISO_TIMESTAMP = new Timestamp(1646852830000L); // add 30 seconds to test the parsing of the timezone
    private static final java.sql.Date VALID_SQL_DATE = new java.sql.Date(1646852800000L);

    @Test
    public void testDefaultDateTypeAdapter() {
        // default constructor
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        assertEquals("DefaultDateTypeAdapter(java.util.Date)", adapter.toString());

        // constructor with DateFormat
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN, Locale.US);
        DefaultDateTypeAdapter adapterWithUSDateFormat = new DefaultDateTypeAdapter(Date.class, dateFormat, dateFormat);
        assertEquals("DefaultDateTypeAdapter(java.util.Date, java.text.SimpleDateFormat)", adapterWithUSDateFormat.toString());

        // constructor with int dateStyle and int timeStyle
        DefaultDateTypeAdapter adapterWithDateStyle = new DefaultDateTypeAdapter(Date.class, Calendar.DATE, Calendar.HOUR_OF_DAY);
        assertEquals("DefaultDateTypeAdapter(java.util.Date)", adapterWithDateStyle.toString());

        // constructor with Class<? extends Date> and DateFormat
        DefaultDateTypeAdapter adapterWithCustomDateFormat = new DefaultDateTypeAdapter(Timestamp.class, dateFormat, dateFormat);
        assertEquals("DefaultDateTypeAdapter(java.sql.Timestamp)", adapterWithCustomDateFormat.toString());

        // constructor with Class<? extends Date> and int dateStyle and int timeStyle
        DefaultDateTypeAdapter adapterWithCustomDateStyle = new DefaultDateTypeAdapter(java.sql.Date.class, Calendar.DATE, Calendar.DATE);
        assertEquals("DefaultDateTypeAdapter(java.sql.Date)", adapterWithCustomDateStyle.toString());

        testWriteAndRead(adapter, VALID_DATE_STRING, VALID_DATE);
        testWriteAndRead(adapterWithUSDateFormat, VALID_DATE_STRING, VALID_DATE);
        testWriteAndRead(adapterWithDateStyle, VALID_DATE_STRING, VALID_DATE);
        testWriteAndRead(adapterWithCustomDateFormat, VALID_DATE_STRING, VALID_DATE);
        testWriteAndRead(adapterWithCustomDateFormat, VALID_ISO_DATE_STRING, VALID_ISO_DATE);
        testWriteAndRead(adapterWithCustomDateFormat, VALID_ISO_DATE_STRING, VALID_ISO_TIMESTAMP);
        testWriteAndRead(adapterWithCustomDateFormat, VALID_ISO_DATE_STRING, VALID_SQL_DATE);

        testWriteAndRead(adapter, INVALID_DATE_STRING, null);
        testWriteAndRead(adapterWithUSDateFormat, INVALID_DATE_STRING, null);
        testWriteAndRead(adapterWithDateStyle, INVALID_DATE_STRING, null);
        testWriteAndRead(adapterWithCustomDateFormat, INVALID_DATE_STRING, null);
        testWriteAndRead(adapterWithCustomDateFormat, INVALID_ISO_DATE_STRING, null);
    }

    private void testWriteAndRead(DefaultDateTypeAdapter adapter, String dateString, Date date) {
        String json = new Gson().toJson(date);
        JsonElement jsonElement = JsonParser.parseString(json);
        Date deserializedDate = adapter.read(new JsonReader(new StringReader(jsonElement.getAsString())));
        assertEquals(date, deserializedDate);
        JsonWriter jsonWriter = new JsonWriter(new StringWriter());
        adapter.write(jsonWriter, date);
        JsonElement jsonElementFromString = JsonParser.parseString(jsonWriter.toString());
        assertEquals(date, JsonParser.parseString(jsonElementFromString.getAsJsonPrimitive().getAsString()).getAsJsonPrimitive().getAsString());
    }
}