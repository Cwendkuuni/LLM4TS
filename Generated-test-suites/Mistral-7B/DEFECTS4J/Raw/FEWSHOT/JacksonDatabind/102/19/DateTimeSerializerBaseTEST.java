package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest<T> {

    @SuppressWarnings("unchecked")
    private static final Class<DateTimeSerializerBase<T>> TYPE = (Class<DateTimeSerializerBase<T>>) (Class<?>) DateTimeSerializerBase.class;

    @Test
    public void testCreateContextual() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DateTimeSerializerBase<Date> serializer = mapper.findValueSerializer(Date.class);

        // Test default case
        JsonSerializer<Date> defaultSerializer = serializer.createContextual(null, null);
        assertEquals(TYPE, defaultSerializer.getClass());
        assertFalse(defaultSerializer._useTimestamp.booleanValue());
        assertNull(defaultSerializer._customFormat);

        // Test with custom format
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.STRING);
        format.setPattern("yyyy-MM-dd HH:mm:ss.SSS Z");
        JsonSerializer<Date> customSerializer = serializer.createContextual(null, null, null, format);
        assertTrue(customSerializer instanceof DateTimeSerializerBase);
        assertTrue(customSerializer._useTimestamp.booleanValue() == false);
        assertEquals(format.getPattern(), ((DateTimeSerializerBase) customSerializer)._customFormat.toPattern());

        // Test with timestamp
        JsonSerializer<Date> timestampSerializer = serializer.createContextual(null, null, true, null);
        assertTrue(timestampSerializer instanceof DateTimeSerializerBase);
        assertTrue(timestampSerializer._useTimestamp.booleanValue() == true);
        assertNull(timestampSerializer._customFormat);

        // Test with custom format and locale
        format.setLocale(Locale.US);
        JsonSerializer<Date> customWithLocaleSerializer = serializer.createContextual(null, null, null, format);
        assertTrue(customWithLocaleSerializer instanceof DateTimeSerializerBase);
        assertTrue(customWithLocaleSerializer._useTimestamp.booleanValue() == false);
        assertEquals(format.getLocale(), ((DateTimeSerializerBase) customWithLocaleSerializer)._customFormat.getLocale());
        assertEquals(format.getPattern(), ((DateTimeSerializerBase) customWithLocaleSerializer)._customFormat.toPattern());

        // Test with custom format, locale and timezone
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        format.setLocale(Locale.US);
        format.setTimeZone(timeZone);
        JsonSerializer<Date> customWithLocaleAndTimeZoneSerializer = serializer.createContextual(null, null, null, format);
        assertTrue(customWithLocaleAndTimeZoneSerializer instanceof DateTimeSerializerBase);
        assertTrue(customWithLocaleAndTimeZoneSerializer._useTimestamp.booleanValue() == false);
        assertEquals(format.getLocale(), ((DateTimeSerializerBase) customWithLocaleAndTimeZoneSerializer)._customFormat.getLocale());
        assertEquals(format.getPattern(), ((DateTimeSerializerBase) customWithLocaleAndTimeZoneSerializer)._customFormat.toPattern());
        assertEquals(timeZone, ((DateTimeSerializerBase) customWithLocaleAndTimeZoneSerializer)._customFormat.getTimeZone());
    }

    @Test
    public void testSerialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        // Test default case
        JsonSerializer<Date> defaultSerializer = mapper.findValueSerializer(Date.class);
        Date date = new Date();
        String output = defaultSerializer.serializeValueAsString(date, new JsonGenerator());
        assertEquals("\"2023-03-10T14:23:56.000Z\"", output);

        // Test with custom format
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.STRING);
        format.setPattern("yyyy-MM-dd HH:mm:ss.SSS");
        JsonSerializer<Date> customSerializer = mapper.findValueSerializer(Date.class, null, format);
        String outputCustom = customSerializer.serializeValueAsString(date, new JsonGenerator());
        assertEquals("\"2023-03-10 14:23:56.000\"", outputCustom);

        // Test with timestamp
        JsonSerializer<Date> timestampSerializer = mapper.findValueSerializer(Date.class, true, null);
        long timestamp = date.getTime();
        String outputTimestamp = timestampSerializer.serializeValueAsString(date, new JsonGenerator());
        assertEquals("2023-03-10T14:23:56.000Z", outputTimestamp);
    }

    @Test
    public void testSerializationWithLocale() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        // Test with custom format and locale
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.STRING);
        format.setPattern("yyyy-MM-dd HH:mm:ss.SSS");
        format.setLocale(Locale.US);
        JsonSerializer<Date> customWithLocaleSerializer = mapper.findValueSerializer(Date.class, null, format);
        Date date = new Date();
        String outputCustomWithLocale = customWithLocaleSerializer.serializeValueAsString(date, new JsonGenerator());
        assertEquals("\"2023-03-10 14:23:56.000\"", outputCustomWithLocale);
    }

    @Test
    public void testSerializationWithLocaleAndTimezone() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        // Test with custom format, locale, and timezone
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.STRING);
        format.setPattern("yyyy-MM-dd HH:mm:ss.SSS Z");
        format.setLocale(Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        JsonSerializer<Date> customWithLocaleAndTimeZoneSerializer = mapper.findValueSerializer(Date.class, null, format);
        Date date = new Date();
        String outputCustomWithLocaleAndTimeZone = customWithLocaleAndTimeZoneSerializer.serializeValueAsString(date, new JsonGenerator());
        assertEquals("\"2023-03-10 14:23:56.000Z\"", outputCustomWithLocaleAndTimeZone);
    }
}