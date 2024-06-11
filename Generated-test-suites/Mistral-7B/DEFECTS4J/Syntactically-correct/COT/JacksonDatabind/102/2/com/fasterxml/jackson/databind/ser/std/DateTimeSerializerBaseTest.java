package com.example.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.StdDateFormat;
import com.fasterxml.jackson.databind.util.StdSerializer;

@SuppressWarnings("resultofmethodcalls") // for mockito
public class DateTimeSerializerBaseTest {

    @Nullable
    private static <T> DateTimeSerializerBase<T> createSerializer(
            Class<T> type, Boolean useTimestamp, @Nullable DateFormat customFormat) {
        return new DateTimeSerializerBase<T>() {
            @Override
            protected DateTimeSerializerBase<T> withFormat(
                    Boolean timestamp, DateFormat customFormat) {
                return this;
            }

            @Override
            public DateTimeSerializerBase<T> instantiate(
                    SerializerProvider serializers, BeanProperty property)
                    throws JsonMappingException {
                return this;
            }

            @Override
            public boolean isEmpty(SerializerProvider serializers, T value) {
                throw new UnsupportedOperationException();
            }

            @Override
            public JsonNode getSchema(SerializerProvider serializers, Type typeHint) {
                throw new UnsupportedOperationException();
            }

            @Override
            public void acceptJsonFormatVisitor(
                    JsonFormatVisitorWrapper visitor, JavaType typeHint)
                    throws JsonMappingException {
                throw new UnsupportedOperationException();
            }

            @Override
            public void serialize(T value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
                assertUnimplemented();
            }

            @Override
            protected boolean _asTimestamp(SerializerProvider serializers) {
                return useTimestamp;
            }

            @Override
            protected void _acceptJsonFormatVisitor(
                    JsonFormatVisitorWrapper visitor, JavaType typeHint, boolean asNumber)
                    throws JsonMappingException {
                // no-op
            }

            @Override
            protected void _serializeAsString(Date value, JsonGenerator g, SerializerProvider provider)
                    throws IOException {
                // no-op
            }

            @Override
            protected long _timestamp(T value) {
                throw new UnsupportedOperationException();
            }

            @Override
            protected void assertUnimplemented() {
                throw new UnsupportedOperationException();
            }
        }.withFormat(useTimestamp, customFormat);
    }

    // Basic test case for constructor
    @org.junit.Test
    public void test_DateTimeSerializerBase_constructor() throws IOException {
        DateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss Z");
        DateTimeSerializerBase<Date> serializer = createSerializer(Date.class, false, customFormat);
        assertNotNull(serializer);
        assertEquals(Date.class, serializer.handledType());
        assertNull(serializer._useTimestamp);
        assertNull(serializer._customFormat);
        assertNull(serializer._reusedCustomFormat);
    }

    // Basic test case for withFormat method
    @org.junit.Test
    public void test_DateTimeSerializerBase_withFormat() throws IOException {
        DateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss Z");
        DateTimeSerializerBase<Date> serializer = createSerializer(Date.class, false, null);
        DateTimeSerializerBase<Date> serialized = serializer.withFormat(false, customFormat);
        assertNotNull(serialized);
        assertEquals(Date.class, serialized.handledType());
        assertNull(serialized._useTimestamp);
        assertEquals(customFormat, serialized._customFormat);
        assertNull(serialized._reusedCustomFormat);
    }

    // Test for constructor with useTimestamp = true
    @org.junit.Test
    public void test_DateTimeSerializerBase_constructor_useTimestampTrue() throws IOException {
        DateTimeSerializerBase<Date> serializer = createSerializer(Date.class, true, null);
        assertEquals(Boolean.TRUE, serializer._useTimestamp);
        assertNull(serializer._customFormat);
        assertNull(serializer._reusedCustomFormat);
    }

    // Test for constructor with customFormat
    @org.junit.Test
    public void test_DateTimeSerializerBase_constructor_customFormat() throws IOException {
        DateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss Z");
        DateTimeSerializerBase<Date> serializer = createSerializer(Date.class, false, customFormat);
        assertEquals(Date.class, serializer.handledType());
        assertNull(serializer._useTimestamp);
        assertEquals(customFormat, serializer._customFormat);
        assertNull(serializer._reusedCustomFormat);
    }

    // Test for isEmpty method
    @org.junit.Test
    public void test_isEmpty() throws IOException {
        DateTimeSerializerBase<Date> serializer = createSerializer(Date.class, false, null);
        assertFalse(serializer.isEmpty(mock(SerializerProvider.class), null));
    }

    // Test for getSchema method
    @org.junit.Test
    public void test_getSchema() throws IOException {
        DateTimeSerializerBase<Date> serializer = createSerializer(Date.class, false, null);
        JsonNode schema = serializer.getSchema(mock(SerializerProvider.class), null);
        assertEquals("number", schema.getClass().getSimpleName());
    }

    // Test for acceptJsonFormatVisitor method
    @org.junit.Test
    public void test_acceptJsonFormatVisitor() throws IOException {
        DateTimeSerializerBase<Date> serializer = createSerializer(Date.class, false, null);
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        serializer.acceptJsonFormatVisitor(visitor, null);
    }

    // Test for serialize method
    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void test_serialize() throws IOException {
        DateTimeSerializerBase<Date> serializer = createSerializer(Date.class, false, null);
        serializer.serialize(null, mock(JsonGenerator.class), mock(SerializerProvider.class));
    }

    // Test for _asTimestamp method
    @org.junit.Test
    public void test_asTimestamp() throws IOException {
        DateTimeSerializerBase<Date> serializer = createSerializer(Date.class, false, null);
        assertFalse(serializer._asTimestamp(mock(SerializerProvider.class)));
    }

    // Test for _acceptJsonFormatVisitor method
    @org.junit.Test
    public void test_acceptJsonFormatVisitor_asNumber() throws JsonMappingException {
        DateTimeSerializerBase<Date> serializer = createSerializer(Date.class, false, null);
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        serializer._acceptJsonFormatVisitor(visitor, null, true);
    }

    // Test for _acceptJsonFormatVisitor method
    @org.junit.Test
    public void test_acceptJsonFormatVisitor_asString() throws JsonMappingException {
        DateTimeSerializerBase<Date> serializer = createSerializer(Date.class, false, null);
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        serializer._acceptJsonFormatVisitor(visitor, null, false);
    }

    // Test for _serializeAsString method
    @org.junit.Test
    public void test_serializeAsString() throws IOException, ParseException {
        DateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss Z");
        DateTimeSerializerBase<Date> serializer = createSerializer(Date.class, false, customFormat);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        when(provider.getDefaultSerializerProvider()).thenReturn(provider);
        Date date = new Date();
        serializer._serializeAsString(date, gen, provider);
        String expected = customFormat.format(date);
        assertEquals(expected, gen.getOutputSize());
    }

    // Test for _timestamp method
    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void test_timestamp() throws IOException {
        DateTimeSerializerBase<Date> serializer = createSerializer(Date.class, false, null);
        serializer._timestamp(null);
    }

    // Test for _reusedCustomFormat
    @org.junit.Test
    public void test_reusedCustomFormat() throws IOException, ParseException {
        DateTimeSerializerBase<Date> serializer = createSerializer(Date.class, false, null);
        DateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss Z");
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        when(provider.getConfig()).thenReturn(new ObjectMapper.DefaultConfig());
        Date date = new Date();
        serializer._serializeAsString(date, gen, provider);
        DateFormat reusedFormat = serializer._reusedCustomFormat.get();
        assertEquals(customFormat, reusedFormat);
    }
}