package com.fasterxml.jackson.databind.ser.std;

import org.junit.Assert;
import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicReference;

public class DateTimeSerializerBaseTest {
    @Test
    public void testConstructor() {
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, true, null) {
            @Override
            public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(String value) {
                return 0;
            }

            @Override
            public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }

        };

        assertEquals(String.class, serializer.handledType());
        assertEquals(true, serializer._useTimestamp);
        assertNull(serializer._customFormat);
        assertNull(serializer._reusedCustomFormat);
    }

    @Test
    public void testWithFormat() {
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, true, null) {
            @Override
            public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(String value) {
                return 0;
            }

            @Override
            public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }

        };

        DateTimeSerializerBase<String> newSerializer = serializer.withFormat(false, new SimpleDateFormat("yyyy-MM-dd"));
        assertEquals(false, newSerializer._useTimestamp);
        assertNotNull(newSerializer._customFormat);
        assertNull(newSerializer._reusedCustomFormat);
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, true, null) {
            @Override
            public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(String value) {
                return 0;
            }

            @Override
            public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }

        };
        SerializerProvider provider = new SerializerProvider();

        assertNull(serializer.createContextual(provider, null));
    }

    @Test
    public void testIsEmpty() {
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, true, null) {
            @Override
            public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(String value) {
                return 0;
            }

            @Override
            public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }

        };

        SerializerProvider provider = new SerializerProvider();
        String value = "test";
        assertFalse(serializer.isEmpty(provider, value));
    }

    @Test
    public void testGetSchema() {
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, true, null) {
            @Override
            public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(String value) {
                return 0;
            }

            @Override
            public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }

        };
        SerializerProvider provider = new SerializerProvider();
        String value = "test";

        JsonNode node = serializer.getSchema(provider, null);
        assertNotNull(node);
        assertEquals("string", node.get("type").asText());
        assertTrue(node.get("required").asBoolean());
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, true, null) {
            @Override
            public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(String value) {
                return 0;
            }

            @Override
            public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }

        };
        SerializerProvider provider = new SerializerProvider();
        JavaType typeHint = null;
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JsonValueFormat jsonValueFormat = mock(JsonValueFormat.class);

        when(visitor.getProvider()).thenReturn(provider);
        when(jsonValueFormat.isNumeric()).thenReturn(true);
        when(visitor.expectNumberFormat(typeHint)).thenReturn(jsonValueFormat);
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        verify(visitor, times(1)).expectNumberFormat(typeHint);
        verify(visitor, times(0)).expectStringFormat(typeHint, jsonValueFormat);
    }

    @Test
    public void testSerializeAsString() throws IOException {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        SerializerProvider provider = new SerializerProvider();

        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, true, null) {
            @Override
            public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
                return this;
            }

            @Override
            protected long _timestamp(String value) {
                return 0;
            }

            @Override
            public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }

        };

        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(timeZone);
        String expected = dateFormat.format(now);
        JsonGenerator gen = mock(JsonGenerator.class);

        serializer._serializeAsString(now, gen, provider);
        verify(gen, times(1)).writeString(expected);
    }
}