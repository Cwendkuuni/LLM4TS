package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser.NumberType;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.node.JsonNode;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase.SerializationFeature;
import com.fasterxml.jackson.databind.ser.std.StdDateFormat;

public class DateTimeSerializerBaseTest {

    @Test
    public void testDateTimeSerializerBaseConstructor() {
        Class<String> stringClass = String.class;
        Boolean useTimestamp = false;
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss zzz");
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(stringClass, useTimestamp, customFormat);
        assertNotNull(serializer);
    }

    @Test
    public void testWithFormat() {
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, false, null);
        DateTimeSerializerBase<String> newSerializer = serializer.withFormat(true, null);
        assertNotSame(serializer, newSerializer);
    }

    @Test
    public void testCreateContextualSerializerWithTimestamp() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer();
        JsonSerializer<String> serializer = mapper.findValueSerializer(String.class);

        assertEquals(serializer.getClass(), DateTimeSerializerBase.class);
        assertTrue(serializer instanceof DateTimeSerializerBase);
        assertTrue(serializer instanceof ContextualSerializer);
        assertTrue(serializer instanceof StdScalarSerializer);

        JsonFormat.Value format = new JsonFormat.Value().withShape(JsonFormat.Shape.NUMBER);
        JsonSerializer<String> serializedSerializer = serializer.createContextual(mapper.getSerializerProvider(), null);
        assertEquals(serializer.getClass(), serializedSerializer.getClass());
        assertTrue(serializedSerializer instanceof DateTimeSerializerBase);
        assertTrue(serializedSerializer instanceof ContextualSerializer);
        assertTrue(serializedSerializer instanceof StdScalarSerializer);
        assertTrue(serializedSerializer._useTimestamp.booleanValue());
        assertTrue(serializedSerializer._customFormat == null);
    }

    @Test
    public void testCreateContextualSerializerWithCustomFormat() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer();
        JsonSerializer<String> serializer = mapper.findValueSerializer(String.class);

        JsonFormat.Value format = new JsonFormat.Value().withShape(JsonFormat.Shape.STRING)
                .withPattern("yyyy-MM-dd HH:mm:ss zzz")
                .withTimeZone(TimeZone.getTimeZone("UTC"))
                .withLocale(Locale.US);

        JsonSerializer<String> serializedSerializer = serializer.createContextual(mapper.getSerializerProvider(), null);
        assertEquals(serializer.getClass(), serializedSerializer.getClass());
        assertTrue(serializedSerializer instanceof DateTimeSerializerBase);
        assertTrue(serializedSerializer instanceof ContextualSerializer);
        assertTrue(serializedSerializer instanceof StdScalarSerializer);
        assertFalse(serializedSerializer._useTimestamp.booleanValue());
        assertEquals(serializedSerializer._customFormat, format);
    }

    @Test(expected = JsonMappingException.class)
    public void testCreateContextualSerializerNullProperty() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer();
        JsonSerializer<String> serializer = mapper.findValueSerializer(String.class);

        serializer.createContextual(mapper.getSerializerProvider(), null);
    }

    @Test(expected = JsonMappingException.class)
    public void testCreateContextualSerializerNullFormat() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer();
        JsonSerializer<String> serializer = mapper.findValueSerializer(String.class);

        JsonFormat.Value format = null;

        serializer.createContextual(mapper.getSerializerProvider(), null);
    }

    @Test
    public void testIsEmpty() {
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, false, null);
        assertTrue(serializer.isEmpty(null));
        assertTrue(serializer.isEmpty(new String[0]));
        assertTrue(serializer.isEmpty(new Object[0]));
        assertTrue(serializer.isEmpty(new DateTime(0)));
        assertFalse(serializer.isEmpty(new DateTime(System.currentTimeMillis())));
    }

    @Test
    public void testGetSchema() {
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, false, null);
        JsonNode schema = serializer.getSchema(null, String.class);
        assertEquals("string", schema.asText());
    }

    @Test
    public void testAcceptJsonFormatVisitorWithNumberFormat() throws JsonMappingException {
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, false, null);
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(new JsonSerializer.DefaultSerializerProvider()) {
            @Override
            public JsonNode visitIntFormat(JsonFormatVisitorWrapper visitor, JavaType typeHint,
                    JsonParser.NumberType numberType, JsonValueFormat valueFormat) throws JsonMappingException {
                assertTrue(numberType == JsonParser.NumberType.LONG);
                assertTrue(valueFormat == JsonValueFormat.UTC_MILLISEC);
                return null;
            }
        };
        serializer.acceptJsonFormatVisitor(visitor, String.class.getType());
    }

    @Test
    public void testAcceptJsonFormatVisitorWithStringFormat() throws JsonMappingException {
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, false, null);
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(new JsonSerializer.DefaultSerializerProvider()) {
            @Override
            public void visitStringFormat(JsonFormatVisitorWrapper visitor, JavaType typeHint,
                    JsonValueFormat valueFormat) throws JsonMappingException {
                assertEquals(JsonValueFormat.DATE_TIME, valueFormat);
            }
        };
        serializer.acceptJsonFormatVisitor(visitor, String.class.getType());
    }

    @Test
    public void testSerializeNullValue() throws IOException {
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, false, null);
        StringWriter writer = new StringWriter();
        JsonGenerator gen = new JsonFactory(new ObjectMapper.DefaultSerializerProvider()).createGenerator(writer);

        serializer.serialize(null, gen, new ObjectMapper.DefaultSerializerProvider());
        gen.flush();
        String serialized = writer.toString();
        assertTrue(serialized.isEmpty());
    }

    @Test
    public void testSerializeValueWithCustomFormat() throws IOException {
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, false, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss zzz"));
        StringWriter writer = new StringWriter();
        JsonGenerator gen = new JsonFactory(new ObjectMapper.DefaultSerializerProvider()).createGenerator(writer);

        DateTime date = new DateTime(System.currentTimeMillis());
        serializer.serialize(date, gen, new ObjectMapper.DefaultSerializerProvider());
        gen.flush();
        String serialized = writer.toString();
        String expected = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss zzz").format(date.toDate());
        assertEquals(expected, serialized);
    }

    @Test
    public void testSerializeValueWithTimestampFormat() throws IOException {
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, true, null);
        StringWriter writer = new StringWriter();
        JsonGenerator gen = new JsonFactory(new ObjectMapper.DefaultSerializerProvider()).createGenerator(writer);

        DateTime date = new DateTime(System.currentTimeMillis());
        serializer.serialize(date, gen, new ObjectMapper.DefaultSerializerProvider());
        gen.flush();
        String serialized = writer.toString();
        long expected = date.getMillis();
        assertEquals(Long.toString(expected), serialized);
    }

    @Test(expected = JsonGenerationException.class)
    public void testSerializeValueWithNullCustomFormat() throws IOException {
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, false, null);
        try {
            StringWriter writer = new StringWriter();
            JsonGenerator gen = new JsonFactory(new ObjectMapper.DefaultSerializerProvider()).createGenerator(writer);
            DateTime date = new DateTime(System.currentTimeMillis());
            serializer.serialize(date, gen, new ObjectMapper.DefaultSerializerProvider());
            gen.flush();
        } catch (IOException e) {
            throw new JsonGenerationException("Failed to serialize date", e);
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testSerializeValueWithNegativeTimestamp() throws IOException {
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, true, null);
        try {
            StringWriter writer = new StringWriter();
            JsonGenerator gen = new JsonFactory(new ObjectMapper.DefaultSerializerProvider()).createGenerator(writer);
            DateTime date = new DateTime(-1);
            serializer.serialize(date, gen, new ObjectMapper.DefaultSerializerProvider());
            gen.flush();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to serialize negative timestamp", e);
        }
    }
}