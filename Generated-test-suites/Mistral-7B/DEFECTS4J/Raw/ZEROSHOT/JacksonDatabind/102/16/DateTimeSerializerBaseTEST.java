package com.example.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest {

    @SuppressWarnings("unchecked")
    private static final Class<DateTimeSerializerBase<Date>> TYPE = (Class<DateTimeSerializerBase<Date>>) (Class<?>) DateTimeSerializerBase.class;

    private static final DateFormat DATE_FORMAT_INSTANCE = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private static class TestDate {}

    @org.junit.Test
    public void testDateTimeSerializerBaseCreation() {
        JsonSerializer<Date> dateSerializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
        assertFalse(dateSerializer._useTimestamp.booleanValue());
        assertNull(dateSerializer._customFormat);
        assertNull(dateSerializer._reusedCustomFormat);

        JsonSerializer<Date> customFormatSerializer = new DateTimeSerializerBase<Date>(Date.class, false, DATE_FORMAT_INSTANCE);
        assertFalse(customFormatSerializer._useTimestamp.booleanValue());
        assertSame(DATE_FORMAT_INSTANCE, customFormatSerializer._customFormat);
        assertNull(customFormatSerializer._reusedCustomFormat);

        JsonSerializer<Date> useTimestampSerializer = new DateTimeSerializerBase<Date>(Date.class, true, null);
        assertTrue(useTimestampSerializer._useTimestamp.booleanValue());
        assertNull(useTimestampSerializer._customFormat);
        assertNull(useTimestampSerializer._reusedCustomFormat);
    }

    @org.junit.Test
    public void testDateTimeSerializerBaseWithFormatSuccess() {
        JsonSerializer<Date> dateSerializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
        JsonSerializer<Date> customFormatSerializer = dateSerializer.withFormat(false, DATE_FORMAT_INSTANCE);
        assertSame(DATE_FORMAT_INSTANCE, customFormatSerializer._customFormat);
        assertNull(customFormatSerializer._reusedCustomFormat);
    }

    @org.junit.Test
    public void testDateTimeSerializerBaseWithFormatError() {
        JsonSerializer<Date> dateSerializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
        try {
            dateSerializer.withFormat(false, null);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @org.junit.Test
    public void testDateTimeSerializerBaseCreateContextualSuccess() {
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<Date> serializer = mapper.findValueSerIALizer(TestDate.class, null);
        JsonFormat.Value jsonFormat = JsonFormat.value().shape(JsonFormat.Shape.STRING).pattern("yyyy-MM-dd HH:mm:ss");
        JsonSerializer<Date> customSerializer = serializer.withResolvedFormat(JsonFormat.value(jsonFormat));

        assertEquals(DATE_FORMAT_INSTANCE.getClass(), customSerializer._customFormat.getClass());
    }

    @org.junit.Test
    public void testDateTimeSerializerBaseSerializeToJson() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer();
        JsonSerializer<Date> serializer = mapper.findValueSerIALizer(TestDate.class, null);
        JsonGenerator generator = writer.getJsonGenerator();

        Date date = new Date();

        serializer.serialize(date, generator, null);
        generator.flush();

        String json = writer.close();
        assertEquals("\"2023-03-31 15:32:31.000\"", json);

        JsonSerializer<Date> customSerializer = serializer.withFormat(false, DATE_FORMAT_INSTANCE);
        serializer = null; // discard original
        generator = writer.getJsonGenerator();

        customSerializer.serialize(date, generator, null);
        generator.flush();

        json = writer.close();
        assertEquals("\"2023-03-31 15:32:31.000\"", json);
    }

    @org.junit.Test
    public void testDateTimeSerializerBaseSerializeToJsonWithFormat() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer();
        JsonSerializer<Date> serializer = mapper.findValueSerIALizer(TestDate.class, null);
        JsonGenerator generator = writer.getJsonGenerator();

        Date date = new Date();

        JsonFormat.Value jsonFormat = JsonFormat.value().shape(JsonFormat.Shape.STRING).pattern("yyyy-MM-dd HH:mm:ss");

        JsonSerializer<Date> customSerializer = serializer.withFormat(false, jsonFormat);
        serializer = null; // discard original
        generator = writer.getJsonGenerator();

        customSerializer.serialize(date, generator, null);
        generator.flush();

        String json = writer.close();
        assertEquals("\"2023-03-31 15:32:31.000\"", json);
    }

    @org.junit.Test
    public void testDateTimeSerializerBaseSerializeToJsonWithTimestamp() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer();
        JsonSerializer<Date> serializer = mapper.findValueSerIALizer(TestDate.class, null);
        JsonGenerator generator = writer.getJsonGenerator();

        Date date = new Date();

        JsonSerializer<Date> timestampSerializer = serializer.withFormat(true, null);
        serializer = null; // discard original
        generator = writer.getJsonGenerator();

        timestampSerializer.serialize(date, generator, null);
        generator.flush();

        String json = writer.close();
        assertEquals("1679849391000", json);
    }

    @org.junit.Test
    public void testDateTimeSerializerBaseGetSchema() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<Date> serializer = mapper.findValueSerIALizer(TestDate.class, null);

        SchemaNode schemaNode = serializer.getSchema(mock(SerializerProvider.class), null);

        assertEquals("number", schemaNode.getType());
    }

    @org.junit.Test
    public void testDateTimeSerializerBaseAcceptJsonFormatVisitor() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<Date> serializer = mapper.findValueSerIALizer(TestDate.class, null);
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        when(visitor.getProvider()).thenReturn(mock(SerializerProvider.class));

        serializer.acceptJsonFormatVisitor(visitor, null);

        verify(visitor).visitIntFormat(any(JsonFormatVisitorWrapper.class), eq(null), any(JsonParser.NumberType.class), anyInt());
    }

    @org.junit.Test
    public void testDateTimeSerializerBaseAcceptJsonFormatVisitorWithFormat() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<Date> serializer = mapper.findValueSerIALizer(TestDate.class, null);
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        when(visitor.getProvider()).thenReturn(mock(SerializerProvider.class));

        JsonFormat.Value jsonFormat = JsonFormat.value().shape(JsonFormat.Shape.STRING).pattern("yyyy-MM-dd HH:mm:ss");
        serializer.acceptJsonFormatVisitor(visitor, null, true);

        verify(visitor).visitStringFormat(any(JsonFormatVisitorWrapper.class), eq(null), eq("DATE_TIME"));
    }
}