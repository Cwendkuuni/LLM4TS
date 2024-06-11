package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.*;
import com.fasterxml.jackson.databind.util.StdDateFormat;

@SuppressWarnings("serial")
public class DateTimeSerializerBaseTest {

    static class TestDateTimeSerializer<T> extends DateTimeSerializerBase<T> {
        TestDateTimeSerializer(Class<T> type, Boolean useTimestamp, DateFormat customFormat) {
            super(type, useTimestamp, customFormat);
        }

        @Override
        public DateTimeSerializerBase<T> withFormat(Boolean timestamp, DateFormat customFormat) {
            return new TestDateTimeSerializer<>(handledType(), timestamp, customFormat);
        }
    }

    @Test
    public void testConstructor() {
        Class<String> stringType = String.class;
        Boolean useTimestamp = false;
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        DateTimeSerializerBase<String> serializer = new TestDateTimeSerializer<>(stringType, useTimestamp, customFormat);
        assertEquals(stringType, serializer.handledType());
        assertEquals(useTimestamp, serializer._useTimestamp);
        assertEquals(customFormat, serializer._customFormat);
    }

    @Test
    public void testIsEmpty() {
        DateTimeSerializerBase<Object> serializer = new TestDateTimeSerializer<>(Object.class, false, null);
        assertTrue(serializer.isEmpty(null));
        assertTrue(serializer.isEmpty(null, null));
        assertFalse(serializer.isEmpty(new Object()));
        assertFalse(serializer.isEmpty(new Object(), null));
    }

    @Test
    public void testGetSchema() throws JsonProcessingException {
        DateTimeSerializerBase<Object> serializer = new TestDateTimeSerializer<>(Object.class, false, null);
        JsonNode schema = serializer.getSchema(null, Object.class);
        assertEquals("string", schema.getType());

        serializer = new TestDateTimeSerializer<>(Object.class, true, null);
        schema = serializer.getSchema(null, Object.class);
        assertEquals("number", schema.getType());
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        DateTimeSerializerBase<Object> serializer = new TestDateTimeSerializer<>(Object.class, false, null);
        JsonSerializer<?> result = serializer.createContextual(null, null);
        assertEquals(serializer, result);

        JsonFormat.Value format = new JsonFormat.Value();
        JsonFormat.Shape shape = JsonFormat.Shape.STRING;
        format.setShape(shape);
        format.setPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        format.setLocale(Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));

        JsonSerializer<?> result2 = serializer.createContextual(null, null);
        assertNotSame(serializer, result2);
        JsonFormat format2 = result2.getFormat();
        assertEquals(shape, format2.getShape());
        assertEquals("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", format2.getPattern());
        assertEquals(Locale.US, format2.getLocale());
        assertEquals("UTC", format2.getTimeZone().getID());

        // test null format
        JsonSerializer<?> result3 = serializer.createContextual(null, null);
        assertNotNull(result3);
        JsonFormat format3 = result3.getFormat();
        assertNull(format3);
    }

    @Test
    public void testSerialize() throws IOException, JsonProcessingException {
        DateTimeSerializerBase<Long> serializer = new TestDateTimeSerializer<>(Long.class, false, null);
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
        serializer.serialize(1234567890123L, gen, new DefaultSerializerProvider());
        String json = gen.getOutputBuffer().readAllBytes();
        assertEquals("1234567890123", json);

        serializer = new TestDateTimeSerializer<>(Long.class, true, null);
        gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
        serializer.serialize(1234567890123L, gen, new DefaultSerializerProvider());
        json = gen.getOutputBuffer().readAllBytes();
        assertEquals("1234567890123", json);

        serializer = new TestDateTimeSerializer<>(Long.class, false, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS"));
        gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
        Object date = "2023-03-31 14:59:56.123".parse(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS"));
        serializer.serialize(date, gen, new DefaultSerializerProvider());
        json = gen.getOutputBuffer().readAllBytes();
        assertEquals("\"2023-03-31 14:59:56.123\"", json);

        serializer = new TestDateTimeSerializer<>(Long.class, true, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS"));
        gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
        serializer.serialize(date, gen, new DefaultSerializerProvider());
        json = gen.getOutputBuffer().readAllBytes();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        long time = df.parse("2023-03-31 14:59:56.123").getTime();
        assertEquals(time, Long.parseLong(json));

        serializer = new TestDateTimeSerializer<>(String.class, false, null);
        gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
        serializer.serialize(null, gen, new DefaultSerializerProvider());
        json = gen.getOutputBuffer().readAllBytes();
        assertEquals("null", json);

        serializer = new TestDateTimeSerializer<>(String.class, false, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS"));
        gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
        serializer.serialize(null, gen, new DefaultSerializerProvider());
        json = gen.getOutputBuffer().readAllBytes();
        assertEquals("null", json);
    }

    @Test
    public void testTimestamp() {
        DateTimeSerializerBase<Object> serializer = new TestDateTimeSerializer<>(Object.class, true, null);
        assertTrue(serializer._asTimestamp(null));

        SerializerProvider serializers = new DefaultSerializerProvider();
        assertFalse(serializer._asTimestamp(serializers));

        serializer = new TestDateTimeSerializer<>(Object.class, false, null);
        assertFalse(serializer._asTimestamp(null));
        assertFalse(serializer._asTimestamp(serializers));
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        DateTimeSerializerBase<Object> serializer = new TestDateTimeSerializer<>(Object.class, false, null);
        ObjectMapper mapper = new ObjectMapper();
        JsonFormatVisitorWrapper visitor = mapper.schemaFactory().findIntFormatVisitor(null, null);
        serializer.acceptJsonFormatVisitor(visitor, Object.class);
        JsonFormat format = visitor.getFormat();
        assertEquals(JsonParser.NumberType.LONG, format.getNumberType());
        assertEquals(JsonValueFormat.UTC_MILLISEC, format.getValueFormat());

        visitor = mapper.schemaFactory().findStringFormatVisitor(null, null, null);
        serializer.acceptJsonFormatVisitor(visitor, Object.class);
        format = visitor.getFormat();
        assertEquals(JsonValueFormat.DATE_TIME, format.getValueFormat());

        serializer = new TestDateTimeSerializer<>(Object.class, false, new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
        visitor = mapper.schemaFactory().findStringFormatVisitor(null, null, null);
        serializer.acceptJsonFormatVisitor(visitor, Object.class);
        format = visitor.getFormat();
        assertEquals("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", format.getPattern());
    }
}