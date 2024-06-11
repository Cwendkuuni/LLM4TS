package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase.Boolean;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase.DateFormat;

public class DateTimeSerializerBaseTest<T> {

    @SuppressWarnings("unchecked")
    public static class TestDateTimeSerializerBase extends DateTimeSerializerBase<Long> {

        public TestDateTimeSerializerBase(Boolean useTimestamp, DateFormat customFormat) {
            super(Long.TYPE, useTimestamp, customFormat);
        }

        @Override
        public DateTimeSerializerBase<Long> withFormat(Boolean timestamp, DateFormat customFormat) {
            return new TestDateTimeSerializerBase(timestamp, customFormat);
        }

        @Override
        protected long _timestamp(Long value) {
            return value;
        }

        @Override
        public void serialize(Long value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            // implementation not needed for testing
        }
    }

    @Test
    public void testCreateContextualWithNullProperty() {
        JsonParser parser = new JsonParser();
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider serializers = mapper.getSerializerProvider();

        DateTimeSerializerBase<Long> serializer = new TestDateTimeSerializerBase(null, null);

        JsonSerializer<Long> result = serializer.createContextual(serializers, null);
        assertEquals(serializer, result);
    }

    @Test
    public void testCreateContextualWithJsonFormatAnnotation() {
        JsonParser parser = new JsonParser();
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider serializers = mapper.getSerializerProvider();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS z");
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Locale locale = Locale.US;
        TimeZone timeZone = TimeZone.getTimeZone("UTC");

        JsonFormat jsonFormat = new JsonFormat();
        jsonFormat.shape(JsonFormat.Shape.STRING);
        jsonFormat.timeZone(timeZone);
        jsonFormat.locale(locale);
        jsonFormat.pattern("yyyy-MM-dd HH:mm:ss.SSS z");

        Annotated property = mock(Annotated.class);
        when(property.getMember()).thenReturn(null);
        when(property.getValue()).thenReturn(jsonFormat);

        DateTimeSerializerBase<Long> serializer = new TestDateTimeSerializerBase(true, null);

        JsonSerializer<Long> result = serializer.createContextual(serializers, property);
        assertTrue(result instanceof TestDateTimeSerializerBase);
        assertTrue(((TestDateTimeSerializerBase) result)._useTimestamp);
        assertNull(((TestDateTimeSerializerBase) result)._customFormat);
    }

    @Test
    public void testCreateContextualWithJsonFormatAnnotationWithCustomFormat() {
        JsonParser parser = new JsonParser();
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider serializers = mapper.getSerializerProvider();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS z");
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Locale locale = Locale.US;
        TimeZone timeZone = TimeZone.getTimeZone("UTC");

        JsonFormat jsonFormat = new JsonFormat();
        jsonFormat.shape(JsonFormat.Shape.STRING);
        jsonFormat.timeZone(timeZone);
        jsonFormat.locale(locale);
        jsonFormat.pattern("yyyy-MM-dd HH:mm:ss.SSS z");
        jsonFormat.format(customFormat);

        Annotated property = mock(Annotated.class);
        when(property.getMember()).thenReturn(null);
        when(property.getValue()).thenReturn(jsonFormat);

        DateTimeSerializerBase<Long> serializer = new TestDateTimeSerializerBase(false, customFormat);

        JsonSerializer<Long> result = serializer.createContextual(serializers, property);
        assertTrue(result instanceof TestDateTimeSerializerBase);
        assertFalse(((TestDateTimeSerializerBase) result)._useTimestamp);
        assertEquals(customFormat, ((TestDateTimeSerializerBase) result)._customFormat);
    }

    @Test
    public void testJsonFormatVisitor() throws Exception {
        JsonParser parser = new JsonParser();
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider serializers = mapper.getSerializerProvider();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS z");
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Locale locale = Locale.US;
        TimeZone timeZone = TimeZone.getTimeZone("UTC");

        JsonFormat jsonFormat = new JsonFormat();
        jsonFormat.shape(JsonFormat.Shape.STRING);
        jsonFormat.timeZone(timeZone);
        jsonFormat.locale(locale);
        jsonFormat.pattern("yyyy-MM-dd HH:mm:ss.SSS z");
        jsonFormat.format(customFormat);

        Annotated property = mock(Annotated.class);
        when(property.getMember()).thenReturn(null);
        when(property.getValue()).thenReturn(jsonFormat);

        DateTimeSerializerBase<Long> serializer = new TestDateTimeSerializerBase(false, customFormat);

        JsonFormatVisitorWrapper visitor = mapper.schemaFactory().getJsonFormatVisitor(serializers, Long.TYPE);
        serializer.acceptJsonFormatVisitor(visitor, Long.TYPE);

        String expected = "{\"type\":\"string\",\"format\":{\"type\":\"string\",\"pattern\":\"^\\[(?:[\\\"\\/]|[^\\]])*(?:\\[|\\])(?:\\/)?(?:[\\\"\\/]|[^\\]])*$\",\"patternProperties\":{},\"properties\":{},\"title\":null,\"description\":null,\"xml\":{}}";
        assertEquals(expected, visitor.getFormatAsString());
    }

    @Test
    public void testSerializeWithTimestamp() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider serializers = mapper.getSerializerProvider();

        JsonParser parser = mapper.getFactory().createParser("1970-01-01T00:00:00.000Z");
        Annotated property = parser.getCurrentName();

        DateTimeSerializerBase<Long> serializer = new TestDateTimeSerializerBase(true, null);
        JsonNode result = mapper.readerForTree(serializer).readValue(parser);
        assertEquals(19700101000000000L, result.asLong());
    }

    @Test
    public void testSerializeWithCustomFormat() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider serializers = mapper.getSerializerProvider();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS z");
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Locale locale = Locale.US;
        TimeZone timeZone = TimeZone.getTimeZone("UTC");

        JsonFormat jsonFormat = new JsonFormat();
        jsonFormat.shape(JsonFormat.Shape.STRING);
        jsonFormat.timeZone(timeZone);
        jsonFormat.locale(locale);
        jsonFormat.pattern("yyyy-MM-dd HH:mm:ss.SSS z");
        jsonFormat.format(customFormat);

        Annotated property = mock(Annotated.class);
        when(property.getMember()).thenReturn(null);
        when(property.getValue()).thenReturn(jsonFormat);

        DateTimeSerializerBase<Long> serializer = new TestDateTimeSerializerBase(false, customFormat);

        JsonNode result = mapper.readerForTree(serializer).readValue(new ObjectReader(mapper.getFactory())
                .readValue(mapper.getFactory().createParser("1970-01-01T00:00:00.000Z")));
        assertEquals("1970-01-01T00:00:00.000Z", result.asText());
    }
}