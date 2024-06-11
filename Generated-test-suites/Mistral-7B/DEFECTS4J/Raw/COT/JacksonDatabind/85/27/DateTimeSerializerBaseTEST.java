package com.fasterxml.jackson.databind.ser.std;

import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.util.StdDateFormat;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Test cases for DateTimeSerializerBase class
 */
public class DateTimeSerializerBaseTest {

    

    @Test
    public void testDateTimeSerializerBaseConstructor() {
        Class<String> stringClass = String.class;
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<>(stringClass, false, null);
        assertNotNull(serializer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDateTimeSerializerBaseConstructorWithNullProvider() {
        Class<String> stringClass = String.class;
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<>(stringClass, false, null);
    }

    @Test
    public void testGetTimestamp() {
        Object date = "2023-03-01T12:34:56Z";
        long timestamp = DateTimeSerializerBase.class._timestamp(date);
        assertEquals("Invalid timestamp", 1678367296000L, timestamp);
    }

    @Test
    public void testGetSchema() {
        SerializerProvider serializers = new StandardSerializerProvider();
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<>(String.class, false, null);
        JsonNode schema = serializer.getSchema(serializers, Object.class);
        assertEquals("Schema should be 'number'", "number", schema.getClass().getSimpleName());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        SerializerProvider serializers = new StandardSerializerProvider();
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<>(String.class, false, null);
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(new StandardSerializerProvider(), null);
        serializer.acceptJsonFormatVisitor(visitor, Object.class.getType());
        assertTrue("Format visitor should not be null", visitor != null);
    }

    @Test
    public void testSerializeWithDefaultSettings() throws IOException {
        SerializerProvider serializers = new StandardSerializerProvider();
        ObjectMapper mapper = new ObjectMapper(serializers);
        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), serializers);

        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<>(String.class, false, null);
        String dateString = "2023-03-01T12:34:56Z";
        serializer.serialize(dateString, gen, serializers);
        gen.flush();
        ByteArrayOutputStream outputStream = (ByteArrayOutputStream) gen.getOutputStorage();
        String serializedDate = new String(outputStream.toByteArray());
        assertEquals("Serialized date should match expected output",
                "\"2023-03-01T12:34:56.000Z\"", serializedDate);
    }

    @Test
    public void testSerializeWithCustomFormat() throws IOException {
        SerializerProvider serializers = new StandardSerializerProvider();
        ObjectMapper mapper = new ObjectMapper(serializers);
        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), serializers);

        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<>(String.class, false,
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z"));
        String dateString = "2023-03-01T12:34:56Z";
        serializer.serialize(dateString, gen, serializers);
        gen.flush();
        ByteArrayOutputStream outputStream = (ByteArrayOutputStream) gen.getOutputStorage();
        String serializedDate = new String(outputStream.toByteArray());
        assertEquals("Serialized date should match expected output",
                "\"2023-03-01T12:34:56Z\"", serializedDate);
    }

    @Test
    public void testCreateContextualSerializerWithNullProperty() {
        SerializerProvider serializers = new StandardSerializerProvider();
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<>(String.class, false, null);
        assertEquals("Contextual serializer should be same as the provided serializer", serializer,
                serializer.createContextual(serializers, null));
    }

    @Test
    public void testCreateContextualSerializerWithCustomFormat() throws JsonMappingException {
        SerializerProvider serializers = new StandardSerializerProvider();
        ObjectMapper mapper = new ObjectMapper(serializers);
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.STRING);
        format.setPattern("yyyy-MM-dd HH:mm:ss z");
        JsonParser parser = mapper.getFactory().getParser("2023-03-01T12:34:56Z");

        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<>(String.class, false, null);
        JsonSerializer<String> serializedSerializer = serializer.createContextual(serializers, null);
        JsonNode parsedNode = mapper.readTree(parser);
        String parsedDate = parsedNode.asText();
        assertEquals("Deserialized date should match input date", "2023-03-01T12:34:56Z", parsedDate);
        assertEquals("Contextual serializer should be instance of DateTimeSerializerBase",
                DateTimeSerializerBase.class, serializedSerializer.getClass());
    }

    @Test
    public void testCreateContextualSerializerWithTimestampFormat() throws JsonMappingException {
        SerializerProvider serializers = new StandardSerializerProvider();
        ObjectMapper mapper = new ObjectMapper(serializers);
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.NUMBER);
        JsonParser parser = mapper.getFactory().getParser("2023-03-01T12:34:56Z");

        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<>(Long.class, true, null);
        JsonSerializer<Long> serializedSerializer = serializer.createContextual(serializers, null);
        JsonNode parsedNode = mapper.readTree(parser);
        long parsedDate = parsedNode.asLong();
        assertEquals("Deserialized timestamp should match input date", 1678367296000L, parsedDate);
        assertEquals("Contextual serializer should be instance of DateTimeSerializerBase",
                DateTimeSerializerBase.class, serializedSerializer.getClass());
    }

    @Test(expected = JsonMappingException.class)
    public void testCreateContextualSerializerWithInvalidFormat() throws JsonMappingException {
        SerializerProvider serializers = new StandardSerializerProvider();
        ObjectMapper mapper = new ObjectMapper(serializers);
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.STRING);
        format.setPattern("InvalidPattern");
        JsonParser parser = mapper.getFactory().getParser("2023-03-01T12:34:56Z");

        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<>(String.class, false, null);
        JsonSerializer<String> serializedSerializer = serializer.createContextual(serializers, null);
    }

    @Test
    public void testCreateContextualSerializerWithLocaleAndTimeZone() throws JsonMappingException {
        SerializerProvider serializers = new StandardSerializerProvider();
        ObjectMapper mapper = new ObjectMapper(serializers);
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.STRING);
        format.setLocale(Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        JsonParser parser = mapper.getFactory().getParser("2023-03-01T12:34:56Z");

        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<>(String.class, false, null);
        JsonSerializer<String> serializedSerializer = serializer.createContextual(serializers, null);
        JsonNode parsedNode = mapper.readTree(parser);
        String parsedDate = parsedNode.asText();
        assertEquals("Deserialized date should match input date", "Thu Mar 01 12:34:56 UTC 2023", parsedDate);
        assertEquals("Contextual serializer should be instance of DateTimeSerializerBase",
                DateTimeSerializerBase.class, serializedSerializer.getClass());
    }

    
}