package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers.LongDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class DateTimeSerializerBaseTest {

    private JsonSerializerProvider serializers;
    private JsonParser parser;
    private JsonGenerator gen;
    private JsonNodeFactory nodeFactory;

    @Before
    public void setUp() throws Exception {
        serializers = new JsonSerializerProvider();
        parser = mock(JsonParser.class);
        gen = mock(JsonGenerator.class);
        nodeFactory = JsonNodeFactory.instance;
    }

    @Test
    public void testConstructor() {
        Class<Long> type = Long.class;
        Boolean useTimestamp = false;
        DateFormat customFormat = null;

        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<>(type, useTimestamp, customFormat);

        assertEquals(Long.class, serializer.handledType());
        assertNull(serializer._useTimestamp);
        assertNull(serializer._customFormat);
    }

    @Test
    public void testWithFormat_useTimestamp() {
        Class<Long> type = Long.class;
        Boolean useTimestamp = true;
        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<>(type, useTimestamp, null);

        DateTimeSerializerBase<Long> newSerializer = serializer.withFormat(useTimestamp, null);

        assertTrue(newSerializer instanceof DateTimeSerializerBase);
        assertTrue(newSerializer._useTimestamp);
        assertNull(newSerializer._customFormat);
    }

    @Test
    public void testWithFormat_customFormat() {
        Class<Long> type = Long.class;
        Boolean useTimestamp = false;
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<>(type, useTimestamp, customFormat);

        DateTimeSerializerBase<Long> newSerializer = serializer.withFormat(useTimestamp, null);
        DateTimeSerializerBase<Long> customSerializer = serializer.withFormat(useTimestamp, customFormat);

        assertTrue(newSerializer instanceof DateTimeSerializerBase);
        assertFalse(newSerializer._useTimestamp);
        assertNull(newSerializer._customFormat);

        assertTrue(customSerializer instanceof DateTimeSerializerBase);
        assertFalse(customSerializer._useTimestamp);
        assertEquals(customFormat, customSerializer._customFormat);
    }

    @Test
    public void testCreateContextual_default() throws JsonProcessingException {
        Class<Long> type = Long.class;
        JsonSerializerProvider serializers = mock(JsonSerializerProvider.class);
        AnnotatedClass ac = mock(AnnotatedClass.class);
        when(ac.getMember()).thenReturn(null);
        JsonFormat.Value format = mock(JsonFormat.Value.class);

        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<>(type, null, null);

        JsonSerializer<?> result = serializer.createContextual(serializers, ac);
        assertEquals(serializer, result);
    }

    @Test
    public void testCreateContextual_numericFormat() throws JsonProcessingException {
        Class<Long> type = Long.class;
        JsonSerializerProvider serializers = mock(JsonSerializerProvider.class);
        AnnotatedClass ac = mock(AnnotatedClass.class);
        when(ac.getMember()).thenReturn(null);
        JsonFormat.Value format = mock(JsonFormat.Value.class);
        JsonFormat.Shape shape = JsonFormat.Shape.NUMBER;
        when(format.getShape()).thenReturn(shape);

        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<>(type, null, null);

        JsonSerializer<?> result = serializer.createContextual(serializers, ac);
        assertTrue(result instanceof DateTimeSerializerBase);
        assertTrue(((DateTimeSerializerBase) result)._useTimestamp);
        assertNull(((DateTimeSerializerBase) result)._customFormat);
    }

    @Test
    public void testCreateContextual_stringFormat() throws JsonProcessingException {
        Class<Long> type = Long.class;
        JsonSerializerProvider serializers = mock(JsonSerializerProvider.class);
        AnnotatedClass ac = mock(AnnotatedClass.class);
        when(ac.getMember()).thenReturn(null);
        JsonFormat.Value format = mock(JsonFormat.Value.class);
        JsonFormat.Shape shape = JsonFormat.Shape.STRING;
        when(format.getShape()).thenReturn(shape);
        TimeZone tz = mock(TimeZone.class);
        String pattern = "yyyy-MM-dd HH:mm:ss";
        Locale loc = Locale.US;
        SimpleDateFormat df = new SimpleDateFormat(pattern, loc);
        df.setTimeZone(tz);
        when(format.getTimeZone()).thenReturn(tz);
        when(format.getPattern()).thenReturn(pattern);
        when(format.getLocale()).thenReturn(loc);

        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<>(type, null, null);

        JsonSerializer<?> result = serializer.createContextual(serializers, ac);
        assertTrue(result instanceof DateTimeSerializerBase);
        assertFalse(result._useTimestamp);
        assertEquals(df, result._customFormat);
    }

    @Test
    public void testSerialize_useTimestamp() throws IOException {
        Class<Long> type = Long.class;
        Long value = new Long(System.currentTimeMillis());

        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<>(type, true, null);

        serializeAndCheck(serializer, value);
    }

    @Test
    public void testSerialize_customFormat() throws IOException {
        Calendar calendar = new GregorianCalendar(2019, 1, 1, 1, 1, 1);
        Long value = calendar.getTimeInMillis();
        TimeZone tz = TimeZone.getTimeZone("UTC");
        Locale locale = Locale.US;
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", locale);
        customFormat.setTimeZone(tz);

        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<>(Long.class, false, customFormat);

        serializeAndCheck(serializer, value);
    }

    @Test(expected = JsonMappingException.class)
    public void testSerialize_invalidCustomFormat() throws IOException {
        Class<Long> type = Long.class;
        Long value = new Long(System.currentTimeMillis());

        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<>(type, false, null);

        serializeAndCheckInvalid("invalid", serializer, value);
    }

    @Test(expected = JsonMappingException.class)
    public void testSerialize_customFormat_null() throws IOException {
        Class<Long> type = Long.class;
        Long value = new Long(System.currentTimeMillis());

        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<>(type, false, null);

        serializeAndCheckInvalid("null", serializer, value);
    }

    private void serializeAndCheck(DateTimeSerializerBase<Long> serializer, Long value)
            throws IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider serializers = mock(SerializerProvider.class);
        when(serializers.getOutputFactory()).thenReturn(nodeFactory);

        ArgumentCaptor<Long> argCaptor = ArgumentCaptor.forClass(Long.class);

        serializer.serialize(value, gen, serializers);

        verify(gen).writeNumber(argCaptor.capture(arg));
        Long serializedValue = argCaptor.getValue();
        assertEquals(value.longValue(), serializedValue.longValue());
    }

    private void serializeAndCheckInvalid(String errorMessage, DateTimeSerializerBase<Long> serializer, Long value)
            throws IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider serializers = mock(SerializerProvider.class);
        when(serializers.getOutputFactory()).thenReturn(nodeFactory);

        try {
            serializer.serialize(value, gen, serializers);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals(errorMessage, e.getMessage());
        }
    }

}