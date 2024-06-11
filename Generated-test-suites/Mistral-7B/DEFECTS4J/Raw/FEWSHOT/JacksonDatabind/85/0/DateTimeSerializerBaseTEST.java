package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;
import com.fasterxml.jackson.databind.ser.*;
import com.fasterxml.jackson.databind.util.StdDateFormat;

@SuppressWarnings("serial")
public class DateTimeSerializerBaseTest<T> {
    private static final Class<T> HANDLED_TYPE = Object.class;

    @SuppressWarnings("unchecked")
    private DateTimeSerializerBase<T> createSerializer(boolean useTimestamp, DateFormat customFormat) {
        return new DateTimeSerializerBase<T>() {
            @Override
            protected long _timestamp(T value) {
                throw new UnsupportedOperationException();
            }

            @Override
            public DateTimeSerializerBase<T> withFormat(Boolean formatTimestamp, DateFormat format) {
                return this;
            }

            @Override
            protected boolean _asTimestamp(SerializerProvider serializers) {
                return useTimestamp;
            }

            @Override
            protected void _acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint, boolean asNumber)
                    throws JsonMappingException {
            }

            @Override
            public void serialize(T value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                throw new UnsupportedOperationException();
            }
        }.newInstance(HANDLED_TYPE, useTimestamp, customFormat);
    }

    @Test
    public void testCreateContextualWithDefaultSettings() throws Exception {
        SerializerProvider serializers = newSerializerProvider();
        DateTimeSerializerBase<Object> serializer = new DateTimeSerializerBase<Object>()
                .newInstance(Object.class, false, null);
        JsonSerializer<?> contextualSerializer = serializer.createContextual(serializers, null);
        JsonSerializer<?> expectedSerializer = new DateTimeSerializerBase<Object>()
                .newInstance(Object.class, false, null);
        assertEquals(expectedSerializer.getClass(), contextualSerializer.getClass());
    }

    @Test
    public void testCreateContextualWithUseTimestampTrue() throws Exception {
        SerializerProvider serializers = newSerializerProvider();
        DateTimeSerializerBase<Object> serializer = new DateTimeSerializerBase<Object>()
                .newInstance(Object.class, true, null);
        JsonSerializer<?> contextualSerializer = serializer.createContextual(serializers, null);
        JsonSerializer<?> expectedSerializer = new DateTimeSerializerBase<Object>()
                .newInstance(Object.class, true, null);
        assertEquals(expectedSerializer.getClass(), contextualSerializer.getClass());
    }

    @Test
    public void testCreateContextualWithCustomFormat() throws Exception {
        SimpleDateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        SerializerProvider serializers = newSerializerProvider();
        DateTimeSerializerBase<Object> serializer = new DateTimeSerializerBase<Object>()
                .newInstance(Object.class, false, customFormat);
        JsonSerializer<?> contextualSerializer = serializer.createContextual(serializers, null);
        JsonSerializer<?> expectedSerializer = new DateTimeSerializerBase<Object>()
                .withFormat(false, customFormat)
                .newInstance(Object.class, false, customFormat);
        assertEquals(expectedSerializer.getClass(), contextualSerializer.getClass());
    }

    @Test
    public void testIsEmpty() {
        DateTimeSerializerBase<Object> serializer = new DateTimeSerializerBase<Object>()
                .newInstance(Object.class, false, null);
        assertTrue(serializer.isEmpty(null));
        assertTrue(serializer.isEmpty(serializer, null));
    }

    @Test
    public void testGetSchema() throws Exception {
        SerializerProvider serializers = newSerializerProvider();
        DateTimeSerializerBase<Object> serializer = new DateTimeSerializerBase<Object>()
                .newInstance(Object.class, false, null);
        JsonNode schema = serializer.getSchema(serializers, Object.class);
        assertEquals("string", schema.getClass().getSimpleName());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws Exception {
        SerializerProvider serializers = newSerializerProvider();
        DateTimeSerializerBase<Object> serializer = new DateTimeSerializerBase<Object>()
                .newInstance(Object.class, false, null);
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(serializers, null);
        serializer.acceptJsonFormatVisitor(visitor, Object.class);
    }

    @Test
    public void testSerializeWithDefaultSettings() throws Exception {
        SerializerProvider serializers = newSerializerProvider();
        DateTimeSerializerBase<Object> serializer = new DateTimeSerializerBase<Object>()
                .newInstance(Object.class, false, null);
        ObjectMapper mapper = new ObjectMapper(serializers);
        ObjectNode node = mapper.createObjectNode();
        node.put("date", new java.util.Date());
        JsonGenerator gen = mapper.getFactory()
                .createGenerator(new ByteArrayOutputStream(), null);
        serializer.serialize(node.get("date"), gen, serializers);
        gen.flush();
        ByteArrayOutputStream outputStream = (ByteArrayOutputStream) gen.getOutput();
        String expectedJson = "\"2023-03-31T14:10:29.652Z\"";
        assertEquals(expectedJson, outputStream.toString());
    }

    @Test
    public void testSerializeWithCustomFormat() throws Exception {
        SimpleDateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        SerializerProvider serializers = newSerializerProvider();
        DateTimeSerializerBase<Object> serializer = new DateTimeSerializerBase<Object>()
                .newInstance(Object.class, false, customFormat);
        ObjectMapper mapper = new ObjectMapper(serializers);
        ObjectNode node = mapper.createObjectNode();
        node.put("date", new java.util.Date());
        JsonGenerator gen = mapper.getFactory()
                .createGenerator(new ByteArrayOutputStream(), null);
        serializer.serialize(node.get("date"), gen, serializers);
        gen.flush();
        ByteArrayOutputStream outputStream = (ByteArrayOutputStream) gen.getOutput();
        String expectedJson = "\"2023-03-31 14:10:29+02:00\"";
        assertEquals(expectedJson, outputStream.toString());
    }

    @Test
    public void testSerializeWithUseTimestampTrue() throws Exception {
        SerializerProvider serializers = newSerializerProvider();
        DateTimeSerializerBase<Object> serializer = new DateTimeSerializerBase<Object>()
                .newInstance(Object.class, true, null);
        ObjectMapper mapper = new ObjectMapper(serializers);
        ObjectNode node = mapper.createObjectNode();
        node.put("date", new java.util.Date());
        JsonGenerator gen = mapper.getFactory()
                .createGenerator(new ByteArrayOutputStream(), null);
        serializer.serialize(node.get("date"), gen, serializers);
        gen.flush();
        ByteArrayOutputStream outputStream = (ByteArrayOutputStream) gen.getOutput();
        String expectedJson = "1680121829000";
        assertEquals(expectedJson, outputStream.toString());
    }

    private SerializerProvider newSerializerProvider() {
        return new BaseSerializerProvider() {
            @Override
            public Locale getLocale() {
                return Locale.getDefault();
            }

            @Override
            public TimeZone getTimeZone() {
                return TimeZone.getDefault();
            }
        };
    }
}