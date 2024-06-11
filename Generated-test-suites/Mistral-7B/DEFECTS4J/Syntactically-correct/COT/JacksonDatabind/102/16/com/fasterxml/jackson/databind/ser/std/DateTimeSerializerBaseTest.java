package com.example.junit;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;

public class DateTimeSerializerBaseTest {

    @Test
    public void testConstructor() {
        // Test constructor with null argument, expected exception
        try {
            new DateTimeSerializerBase<Date>(null, null, null);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // expected
        }

        // Test constructor with valid arguments
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<>(Date.class, false, customFormat);
        assertEquals(Date.class, serializer.handledType());
        assertNull(serializer._useTimestamp);
        assertNotNull(serializer._customFormat);
        assertEquals(customFormat, serializer._customFormat);
        assertNull(serializer._reusedCustomFormat);
    }

    @Test
    public void testWithFormat() {
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<>(Date.class, false, null);
        DateTimeSerializerBase<Date> withFormat = serializer.withFormat(false, customFormat);
        assertNotSame(serializer, withFormat);
        assertEquals(customFormat, withFormat._customFormat);
        assertNull(withFormat._reusedCustomFormat);

        // Test withFormat with null customFormat, expected exception
        try {
            serializer.withFormat(false, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Test withFormat with useTimestamp true, expected exception
        try {
            serializer.withFormat(true, customFormat);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testIsEmpty() {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<>(Date.class, false, null);
        assertFalse(serializer.isEmpty(null, null));

        Date date = new Date(System.currentTimeMillis());
        assertFalse(serializer.isEmpty(null, date));

        long timestamp = 0L;
        Date emptyDate = new Date(timestamp);
        assertTrue(serializer.isEmpty(null, emptyDate));
    }

    @Test
    public void testGetSchema() {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<>(Date.class, false, null);
        JsonNode schema = serializer.getSchema(null, null);
        assertEquals("string", schema.getClass().getSimpleName());
        assertTrue(schema.isTextual());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<>(Date.class, false, null);
        JsonFormatVisitorWrapper visitor = null; // null argument, expected exception
        try {
            serializer.acceptJsonFormatVisitor(visitor, null);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // expected
        }

        visitor = new JsonFormatVisitorWrapper(null, null, null);
        serializer.acceptJsonFormatVisitor(visitor, null);
        assertTrue(visitor.hasSerializableValue());
        assertFalse(visitor.hasSerializableContent());
        assertFalse(visitor.hasCustomSerializer());
        assertEquals("string", visitor.getFormatType());
        assertFalse(visitor.hasFormatShape());
    }

    @Test
    public void testSerialize() throws JsonGenerationException,
            JsonMappingException, IOException, ParseException {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<>(Date.class, false, null);

        // Test serialization with default format
        Date date = new Date(System.currentTimeMillis());
        JsonGenerator gen = null; // null argument, expected exception
        try {
            serializer.serialize(date, gen, null);
            fail("Expected JsonGenerationException");
        } catch (JsonGenerationException e) {
            // expected
        }

        SerializerProvider provider = new SerializerProvider();
        provider.setNullValueSerializer(new StandardSerializer(null));
        provider.setFeatureEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        gen = provider.getJsonGenerator();
        gen.setDateFormatInstance(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

        String expected = "\"2023-03-01 12:00:00\"";
        String result = serializer.serialize(date, gen, provider).getContentAsString();
        assertEquals(expected, result);

        // Test serialization with custom format
        DateFormat customFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        serializer = new DateTimeSerializerBase<>(Date.class, false, customFormat);
        result = serializer.serialize(date, gen, provider).getContentAsString();
        assertEquals("\"20230301120000\"", result);

        // Test serialization with timestamp
        serializer = new DateTimeSerializerBase<>(Date.class, true, null);
        result = serializer.serialize(date, gen, provider).getContentAsString();
        assertEquals("2023-03-01T12:00:00.000Z", result);
    }

    @Test
    public void test_acceptJsonFormatVisitorWithProvider() throws JsonMappingException {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<>(Date.class, false, null);
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(null, null, null);
        SerializerProvider provider = new SerializerProvider();
        provider.setNullValueSerializer(new StandardSerializer(null));
        provider.setFeatureEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        serializer.acceptJsonFormatVisitor(visitor, null, provider);
        assertTrue(visitor.hasSerializableValue());
        assertFalse(visitor.hasSerializableContent());
        assertFalse(visitor.hasCustomSerializer());
        assertEquals("number", visitor.getFormatType());
        assertFalse(visitor.hasFormatShape());

        // Test acceptJsonFormatVisitor with null provider, expected exception
        try {
            serializer.acceptJsonFormatVisitor(visitor, null, null);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // expected
        }
    }

    @Test
    public void test_serializeAsString() throws JsonGenerationException, IOException, ParseException {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<>(Date.class, false, null);

        // Test serialization with default format
        Date date = new Date(System.currentTimeMillis());
        String result = serializer._serializeAsString(date, null, null);
        assertEquals("\"Fri, 03 Mar 2023 12:00:00 GMT\"", result);

        // Test serialization with custom format
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
        serializer = new DateTimeSerializerBase<>(Date.class, false, customFormat);
        result = serializer._serializeAsString(date, null, null);
        assertEquals("\"2023-03-01T12:00:00Z\"", result);
    }
}