package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.BeanProperty;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.StdDateFormat;
import com.fasterxml.jackson.databind.util.StdDateFormat.DateFormatType;
import com.fasterxml.jackson.databind.util.TestBase;

public class DateTimeSerializerBaseTest extends TestBase {

    private static final long TEST_TIMESTAMP = 1633046721000L;
    private static final String TEST_DATE_STRING = "2021-12-31T23:59:59Z";

    @SuppressWarnings("unchecked")
    private static <T> DateTimeSerializerBase<T> createSerializer(Class<T> type, Boolean useTimestamp, DateFormat customFormat) {
        return new DateTimeSerializerBase<T>() {
            @Override
            protected long _timestamp(T value) {
                return (useTimestamp != null && useTimestamp.booleanValue()) ? TEST_TIMESTAMP : super._timestamp(value);
            }

            @Override
            protected void _acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint, boolean asNumber) throws JsonMappingException {
                super._acceptJsonFormatVisitor(visitor, typeHint, asNumber);
            }
        }.withFormat(useTimestamp, customFormat);
    }

    @org.junit.Test
    public void testCreateContextual() throws JsonProcessingException, JsonMappingException {
        JsonSerializer<Object> serializer = createSerializer(Object.class, null, null);
        BeanProperty property = mock(BeanProperty.class);
        JsonFormat.Value format = mock(JsonFormat.Value.class);

        when(property.getMember()).thenReturn(new Object() {});
        when(format.getShape()).thenReturn(JsonFormat.Shape.STRING);
        when(format.getTimeZone()).thenReturn(TimeZone.getDefault());
        when(format.getLocale()).thenReturn(Locale.getDefault());
        when(format.hasPattern()).thenReturn(true);
        when(format.getPattern()).thenReturn("yyyy-MM-dd'T'HH:mm:ss'Z'");

        JsonFormat.Value result = serializer.createContextual(mock(SerializerProvider.class), property);
        SimpleDateFormat expected = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        assertEquals(expected, result._value);
        assertEquals(DateFormatType.DATE_TIME, result._serializerProvider.getConfig().getAnnotationIntrospector().findFormat(property).getShape());
        assertTrue(result instanceof DateTimeSerializerBase);
    }

    @org.junit.Test
    public void testCreateContextual_timestamp() throws JsonProcessingException, JsonMappingException {
        JsonSerializer<Object> serializer = createSerializer(Object.class, Boolean.TRUE, null);
        BeanProperty property = mock(BeanProperty.class);

        JsonFormat.Value result = serializer.createContextual(mock(SerializerProvider.class), property);
        assertSame(serializer, result);
    }

    @org.junit.Test
    public void testCreateContextual_defaultFormat() throws JsonProcessingException, JsonMappingException {
        JsonSerializer<Object> serializer = createSerializer(Object.class, null, null);
        BeanProperty property = mock(BeanProperty.class);

        JsonFormat.Value result = serializer.createContextual(mock(SerializerProvider.class), property);
        assertNull(result);
    }

    @org.junit.Test
    public void testSerialize() throws IOException {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        JsonSerializer<Date> serializer = createSerializer(Date.class, null, format);
        Date date = new Date(TEST_TIMESTAMP);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        JsonGenerator generator = factory.createGenerator(output);
        serializer.serialize(date, generator, null);
        generator.close();

        byte[] actual = output.toByteArray();
        byte[] expected = TEST_DATE_STRING.getBytes();
        assertArrayEquals(expected, actual);
    }

    @org.junit.Test
    public void testSerialize_timestamp() throws IOException {
        JsonSerializer<Long> serializer = createSerializer(Long.class, Boolean.TRUE, null);
        long value = TEST_TIMESTAMP;

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        JsonGenerator generator = factory.createGenerator(output);
        serializer.serialize(value, generator, null);
        generator.close();

        byte[] actual = output.toByteArray();
        assertEquals("1633046721000", new String(actual));
    }

    @org.junit.Test
    public void testSerialize_customFormat() throws IOException {
        DateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        JsonSerializer<Date> serializer = createSerializer(Date.class, null, format);
        Date date = new Date(TEST_TIMESTAMP);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        JsonGenerator generator = factory.createGenerator(output);
        serializer.serialize(date, generator, null);
        generator.close();

        byte[] actual = output.toByteArray();
        String expected = "31-12-2021 23:59:59";
        assertEquals(expected, new String(actual));
    }

    @org.junit.Test
    public void testSerialize_empty() throws IOException {
        JsonSerializer<Date> serializer = createSerializer(Date.class, null, null);
        Date date = null;

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        JsonGenerator generator = factory.createGenerator(output);
        serializer.serialize(date, generator, null);
        generator.close();

        byte[] actual = output.toByteArray();
        byte[] expected = "null".getBytes();
        assertArrayEquals(expected, actual);
    }

    @org.junit.Test
    public void testSerialize_useTimestamp() throws IOException {
        JsonSerializer<Long> serializer = createSerializer(Long.class, Boolean.TRUE, null);
        long value = TEST_TIMESTAMP;

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        JsonGenerator generator = factory.createGenerator(output);
        serializer.serialize(value, generator, null);
        generator.close();

        byte[] actual = output.toByteArray();
        assertEquals("1633046721000", new String(actual));
    }

    @org.junit.Test
    public void testSerialize_emptyWithCustomFormat() throws IOException {
        JsonSerializer<Date> serializer = createSerializer(Date.class, null, new SimpleDateFormat("yyyy-MM-dd"));
        Date date = null;

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        JsonGenerator generator = factory.createGenerator(output);
        serializer.serialize(date, generator, null);
        generator.close();

        byte[] actual = output.toByteArray();
        byte[] expected = "null".getBytes();
        assertArrayEquals(expected, actual);
    }

    @org.junit.Test
    public void testIsEmpty() throws IOException {
        JsonSerializer<Date> serializer = createSerializer(Date.class, null, null);
        Date date = null;

        assertTrue(serializer.isEmpty(date));
        assertTrue(serializer.isEmpty(null));
    }

    @org.junit.Test
    public void testIsEmpty_useTimestamp() throws IOException {
        JsonSerializer<Long> serializer = createSerializer(Long.class, Boolean.TRUE, null);
        long value = TEST_TIMESTAMP;

        assertFalse(serializer.isEmpty(value));
        assertFalse(serializer.isEmpty(null));
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testIsEmpty_nullSerializerProvider() throws IOException {
        JsonSerializer<Long> serializer = createSerializer(Long.class, Boolean.TRUE, null);
        long value = TEST_TIMESTAMP;

        serializer.isEmpty(value, null);
    }
}