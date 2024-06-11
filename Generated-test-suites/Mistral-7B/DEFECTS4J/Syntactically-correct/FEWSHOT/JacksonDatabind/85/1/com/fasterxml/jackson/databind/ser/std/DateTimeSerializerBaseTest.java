package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;
import org.junit.Test;

public class DateTimeSerializerBaseTest<T> {

    @SuppressWarnings("unchecked")
    static DateTimeSerializerBase<Long> getLongSerializer(Boolean useTimestamp, DateFormat customFormat) {
        return new DateTimeSerializerBase<Long>(Long.class, useTimestamp, customFormat) {
            @Override
            protected long _timestamp(Long value) {
                return value;
            }
        };
    }

    @Test
    public void testDefaultConstructor() {
        DateTimeSerializerBase<Long> serializer = getLongSerializer(null, null);
        assertEquals(JsonSerializer.NullValue.EMPTY_STRING, serializer.getSchema(null, null));
        assertTrue(serializer.isEmpty(null));
        assertTrue(serializer.isEmpty(null, null));
    }

    @Test
    public void testUseTimestampConstructor() {
        DateTimeSerializerBase<Long> serializer = getLongSerializer(true, null);
        assertEquals("number", serializer.getSchema(null, null).getClass().getSimpleName());
        assertFalse(serializer.isEmpty(null));
        assertFalse(serializer.isEmpty(null, null));
    }

    @Test
    public void testCustomFormatConstructor() {
        SimpleDateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateTimeSerializerBase<Long> serializer = getLongSerializer(false, customFormat);
        assertEquals("string", serializer.getSchema(null, null).getClass().getSimpleName());
        assertFalse(serializer.isEmpty(null));
        assertFalse(serializer.isEmpty(null, null));
    }

    @Test
    public void testCreateContextualWithNullProperty() {
        DateTimeSerializerBase<Long> serializer = getLongSerializer(false, null);
        assertEquals(serializer, serializer.createContextual(null, null));
    }

    @Test
    public void testCreateContextualWithFormatOverrides() {
        SimpleDateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.STRING);
        format.setLocale(Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        format.setPattern("yyyy-MM-dd HH:mm:ss");
        JsonSerializer<Long> result = new DateTimeSerializerBase<Long>(Long.class, false, null) {
            @Override
            public DateTimeSerializerBase<T> withFormat(Boolean timestamp, DateFormat customFormat) {
                return this;
            }
        }.createContextual(null, null);
        assertTrue(result instanceof DateTimeSerializerBase);
        assertEquals(customFormat, result.customFormat());
    }

    @Test
    public void testCreateContextualWithDefaultFormat() {
        DateTimeSerializerBase<Long> serializer = getLongSerializer(false, null);
        JsonSerializer<Long> result = serializer.createContextual(null, null);
        assertTrue(result instanceof DateTimeSerializerBase);
        assertNull(result.customFormat());
    }

    @Test
    public void testCreateContextualWithNumericFormat() {
        JsonSerializer<Long> result = getLongSerializer(true, null).createContextual(null, null);
        assertTrue(result instanceof DateTimeSerializerBase);
        assertNull(result.customFormat());
    }

    @Test
    public void testSerializeWithNullValue() throws IOException {
        DateTimeSerializerBase<Long> serializer = getLongSerializer(false, null);
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), 1024);
        serializer.serialize(null, gen, null);
        assertEquals("null", gen.getOutputContent().readFully());
    }

    @Test
    public void testSerializeWithTimestamp() throws IOException {
        DateTimeSerializerBase<Long> serializer = getLongSerializer(true, null);
        Long value = 1633040221000L;
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), 1024);
        serializer.serialize(value, gen, null);
        assertEquals("1633040221000", gen.getOutputContent().readFully());
    }

    @Test
    public void testSerializeWithCustomFormat() throws IOException {
        SimpleDateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateTimeSerializerBase<Long> serializer = getLongSerializer(false, customFormat);
        Long value = new Date(1633040221000L).getTime();
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), 1024);
        serializer.serialize(value, gen, null);
        assertEquals("2021-08-12 10:34:41", new String(gen.getOutputContent().readAllBytes()));
    }

    @Test
    public void testDeserializeWithNullValue() throws IOException {
        DateTimeSerializerBase<Long> serializer = getLongTimestampSerializer();
        JsonParser parser = new JsonFactory().createParser(new ByteArrayInputStream("null".getBytes()));
        Long value = serializer.deserialize(parser, Long.class, null);
        assertEquals(Long.MIN_VALUE, value);
    }

    @Test
    public void testDeserializeWithTimestamp() throws IOException {
        DateTimeSerializerBase<Long> serializer = getLongTimestampSerializer();
        JsonParser parser = new JsonFactory().createParser(new ByteArrayInputStream("1633040221000".getBytes()));
        Long value = serializer.deserialize(parser, Long.class, null);
        assertEquals(1633040221000L, value);
    }

    @Test
    public void testDeserializeWithCustomFormat() throws IOException {
        SimpleDateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateTimeSerializerBase<Long> serializer = getLongCustomFormatSerializer(customFormat);
        JsonParser parser = new JsonFactory().createParser(new ByteArrayInputStream("2021-08-12 10:34:41".getBytes()));
        Long value = serializer.deserialize(parser, Long.class, null);
        assertEquals(1633040221000L, value);
    }

    private DateTimeSerializerBase<Long> getLongTimestampSerializer() {
        return new DateTimeSerializerBase<Long>(Long.class, true, null);
    }

    private DateTimeSerializerBase<Long> getLongCustomFormatSerializer(DateFormat customFormat) {
        return new DateTimeSerializerBase<Long>(Long.class, false, customFormat);
    }
}