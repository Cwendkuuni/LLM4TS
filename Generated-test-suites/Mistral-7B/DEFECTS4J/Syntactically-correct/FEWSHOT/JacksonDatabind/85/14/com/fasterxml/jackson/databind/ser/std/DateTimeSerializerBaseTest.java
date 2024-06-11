package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

@SuppressWarnings("serial")
public class DateTimeSerializerBaseTest<T> {

    @SuppressWarnings("unchecked")
    private static final DateTimeSerializerBase<Long> DATE_TIME_SERIALIZER = new DateTimeSerializerBase<Long>(Long.TYPE, false, null);
    @SuppressWarnings("unchecked")
    private static final DateTimeSerializerBase<Long> TIMESTAMP_SERIALIZER = new DateTimeSerializerBase<Long>(Long.TYPE, true, null);

    @SuppressWarnings("unchecked")
    private static final DateTimeSerializerBase<String> DATE_STRING_SERIALIZER = new DateTimeSerializerBase<String>(String.class, false, null);
    @SuppressWarnings("unchecked")
    private static final DateTimeSerializerBase<String> DATE_TIME_STRING_SERIALIZER = new DateTimeSerializerBase<String>(String.class, false, new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));

    @Test
    public void testDateTimeSerializerBase() {
        assertNotNull(DATE_TIME_SERIALIZER);
        assertNotNull(TIMESTAMP_SERIALIZER);
        assertNotNull(DATE_STRING_SERIALIZER);
        assertNotNull(DATE_TIME_STRING_SERIALIZER);
    }

    @Test
    public void testDateTimeSerializerBaseWithFormat() {
        DateTimeSerializerBase<Long> dateTimeSerializerWithFormat = DATE_TIME_SERIALIZER.withFormat(false, new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"));
        assertNotNull(dateTimeSerializerWithFormat);

        DateTimeSerializerBase<Long> timestampSerializerWithFormat = TIMESTAMP_SERIALIZER.withFormat(false, new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"));
        assertNotNull(timestampSerializerWithFormat);

        assertEquals(DATE_TIME_STRING_SERIALIZER, dateTimeSerializerWithFormat);
        assertEquals(TIMESTAMP_SERIALIZER, timestampSerializerWithFormat);

        DateTimeSerializerBase<Long> dateTimeSerializerWithTimestamp = DATE_TIME_SERIALIZER.withFormat(true, null);
        assertNotNull(dateTimeSerializerWithTimestamp);

        DateTimeSerializerBase<Long> timestampSerializerWithTimestamp = TIMESTAMP_SERIALIZER.withFormat(false, null);
        assertNotNull(timestampSerializerWithTimestamp);

        assertNotEquals(DATE_TIME_STRING_SERIALIZER, dateTimeSerializerWithTimestamp);
        assertNotEquals(TIMESTAMP_SERIALIZER, timestampSerializerWithTimestamp);
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        JsonFormat.Value jsonFormat = new JsonFormat.Value();
        JsonFormat.Shape shape = JsonFormat.Shape.STRING;

        // Test with null JsonParser
        JsonParser nullJsonParser = null;
        Object contextualSerializer = DATE_TIME_SERIALIZER.createContextual(null, null);
        assertNotNull(contextualSerializer);
        assertEquals(DATE_TIME_SERIALIZER, contextualSerializer);

        // Test with JsonParser and no format
        JsonParser jsonParser = null;
        jsonFormat.shape(shape);
        contextualSerializer = DATE_TIME_SERIALIZER.createContextual(new SerializerProvider(), null);
        assertNotNull(contextualSerializer);
        assertEquals(DATE_TIME_SERIALIZER, contextualSerializer);

        // Test with JsonParser and custom format (String pattern)
        jsonParser = new TestJsonParser();
        jsonFormat.shape(shape).pattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        contextualSerializer = DATE_TIME_SERIALIZER.createContextual(new SerializerProvider(), null);
        assertNotNull(contextualSerializer);
        assertEquals(DATE_TIME_STRING_SERIALIZER, contextualSerializer);

        // Test with JsonParser and custom format (DateFormat)
        jsonFormat.shape(shape).timeZone(TimeZone.getTimeZone("UTC")).locale(Locale.US);
        contextualSerializer = DATE_TIME_SERIALIZER.createContextual(new SerializerProvider(), null);
        assertNotNull(contextualSerializer);
        assertEquals(DATE_TIME_STRING_SERIALIZER, contextualSerializer);

        // Test with JsonParser and custom format (JsonFormat.Value)
        jsonParser = new TestJsonParser();
        jsonFormat.shape(shape).pattern("yyyy-MM-dd'T'HH:mm:ss'Z'").timeZone(TimeZone.getTimeZone("UTC")).locale(Locale.US);
        contextualSerializer = DATE_TIME_SERIALIZER.createContextual(new SerializerProvider(), null);
        assertNotNull(contextualSerializer);
        assertEquals(DATE_TIME_STRING_SERIALIZER, contextualSerializer);

        // Test with JsonParser and timestamp format
        jsonFormat.shape(JsonFormat.Shape.NUMBER);
        contextualSerializer = DATE_TIME_SERIALIZER.createContextual(new SerializerProvider(), null);
        assertNotNull(contextualSerializer);
        assertEquals(TIMESTAMP_SERIALIZER, contextualSerializer);

        // Test with JsonParser and timestamp format with useTimestamp flag
        jsonFormat.shape(JsonFormat.Shape.NUMBER).useTimestamp(true);
        contextualSerializer = DATE_TIME_SERIALIZER.createContextual(new SerializerProvider(), null);
        assertNotNull(contextualSerializer);
        assertEquals(TIMESTAMP_SERIALIZER, contextualSerializer);

        // Test with JsonParser and null format
        jsonFormat = null;
        contextualSerializer = DATE_TIME_SERIALIZER.createContextual(new SerializerProvider(), null);
        assertNotNull(contextualSerializer);
        assertEquals(DATE_TIME_SERIALIZER, contextualSerializer);
    }
}

class TestJsonParser extends JsonParser {
    @Override
    public JsonParser nextToken() throws IOException {
        return this;
    }
}