package com.yourpackagename.test;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.annotation.JsonFormat;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;

public class DateTimeSerializerBaseTest {

    private DateTimeSerializerBase<Long> longDateTimeSerializer;
    private DateTimeSerializerBase<String> stringDateTimeSerializer;

    @Before
    public void setup() {
        longDateTimeSerializer = new LongDateTimeSerializer(false, null);
        stringDateTimeSerializer = new StringDateTimeSerializer(false, null);
    }

    @Test
    public void testConstructor() {
        assertNotNull(longDateTimeSerializer);
        assertNotNull(stringDateTimeSerializer);
    }

    @Test
    public void testWithFormat() {
        DateTimeSerializerBase<Long> serializer1 = new LongDateTimeSerializer(true, null);
        DateTimeSerializerBase<Long> serializer2 = serializer1.withFormat(false, new SimpleDateFormat("dd-MM-yyyy"));
        assertNotSame(serializer1, serializer2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullSerializerProvider() {
        new LongDateTimeSerializer(false, null).createContextual(null, null);
    }

    @Test
    public void testCreateContextualNullProperty() {
        DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        DateTimeSerializerBase<String> serializer = new StringDateTimeSerializer(false, format);
        assertEquals(serializer, serializer.createContextual(null, null));
    }

    @Test
    public void testCreateContextualWithFormat() {
        DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        DateTimeSerializerBase<String> serializer = new StringDateTimeSerializer(false, format);
        DateTimeSerializerBase<String> contextualSerializer = serializer.createContextual(new ObjectMapper(), null);
        assertNotSame(serializer, contextualSerializer);
        assertEquals(format, contextualSerializer._customFormat);
    }

    @Test
    public void testIsEmptyNull() {
        assertTrue(longDateTimeSerializer.isEmpty(null));
    }

    @Test
    public void testIsEmptyTimestampZero() {
        Long timestamp = 0L;
        assertTrue(longDateTimeSerializer.isEmpty(timestamp));
    }

    @Test
    public void testIsEmptyFalse() {
        Long timestamp = 1234567890123L;
        assertFalse(longDateTimeSerializer.isEmpty(timestamp));
    }

    @Test
    public void testGetSchema() {
        JsonNode schema = longDateTimeSerializer.getSchema(null, handledType());
        assertEquals("number", schema.toString());
    }

    @Test
    public void testAcceptJsonFormatVisitorNull() {
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(new StandardSerializableSerializer(String.class));
        longDateTimeSerializer.acceptJsonFormatVisitor(visitor, javaTypeOf(Long.TYPE));
    }

    @Test
    public void testAcceptJsonFormatVisitorAsNumber() {
        ObjectMapper mapper = new ObjectMapper();
        JsonFormatVisitorWrapper visitor = mapper.getSerializationConfig().getIntFormatVisitor(JavaType.valueOf(String.class));
        longDateTimeSerializer.acceptJsonFormatVisitor(visitor, javaTypeOf(Long.TYPE));
        assertTrue(visitor.hasFormat());
    }

    @Test
    public void testAcceptJsonFormatVisitorAsString() {
        ObjectMapper mapper = new ObjectMapper();
        JsonFormatVisitorWrapper visitor = mapper.getSerializationConfig().getStringFormatVisitor(JavaType.valueOf(String.class));
        stringDateTimeSerializer.acceptJsonFormatVisitor(visitor, javaTypeOf(String.class));
        assertTrue(visitor.hasFormat());
    }

    @Test
    public void testSerializeNull() throws IOException {
        String input = null;
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
        longDateTimeSerializer.serialize(null, gen, new DefaultSerializerProvider());
        JsonParser parser = new JsonFactory().createParser(new ByteArrayInputStream(gen.getOutputBuffer().toByteArray()));
        JsonNode result = parser.readValueAsTree();
        assertEquals("null", result.toString());
    }

    @Test
    public void testSerializeLong() throws IOException {
        Long value = 1234567890123L;
        String expectedResult = "1234567890123";
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
        longDateTimeSerializer.serialize(value, gen, new DefaultSerializerProvider());
        JsonParser parser = new JsonFactory().createParser(new ByteArrayInputStream(gen.getOutputBuffer().toByteArray()));
        JsonNode result = parser.readValueAsTree();
        assertEquals(expectedResult, result.toString());
    }

    @Test
    public void testSerializeLongWithFormat() throws IOException {
        Long value = 1234567890123L;
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String expectedResult = "\"2023-03-15T10:15:20Z\"";
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
        DateTimeSerializerBase<Long> serializer = longDateTimeSerializer.withFormat(false, format);
        serializer.serialize(value, gen, new DefaultSerializerProvider());
        JsonParser parser = new JsonFactory().createParser(new ByteArrayInputStream(gen.getOutputBuffer().toByteArray()));
        JsonNode result = parser.readValueAsTree();
        assertEquals(expectedResult, result.toString());
    }

    @Test
    public void testSerializeString() throws IOException {
        String value = "2023-03-15";
        String expectedResult = "\"2023-03-15\"";
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
        stringDateTimeSerializer.serialize(value, gen, new DefaultSerializerProvider());
        JsonParser parser = new JsonFactory().createParser(new ByteArrayInputStream(gen.getOutputBuffer().toByteArray()));
        JsonNode result = parser.readValueAsTree();
        assertEquals(expectedResult, result.toString());
    }

    @Test
    public void testSerializeStringWithFormat() throws IOException {
        String value = "2023-03-15";
        DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        String expectedResult = "\"15-03-2023\"";
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
        DateTimeSerializerBase<String> serializer = stringDateTimeSerializer.withFormat(false, format);
        serializer.serialize(value, gen, new DefaultSerializerProvider());
        JsonParser parser = new JsonFactory().createParser(new ByteArrayInputStream(gen.getOutputBuffer().toByteArray()));
        JsonNode result = parser.readValueAsTree();
        assertEquals(expectedResult, result.toString());
    }

    private static JavaType handledType() {
        return new TypeFactory().constructType(Long.class);
    }

    private static JavaType javaTypeOf(Class<?> clazz) {
        return new TypeFactory().constructType(clazz);
    }

    @JsonAutoDetect
    private static class TestDate {
    }

}

public class LongDateTimeSerializer extends DateTimeSerializerBase<Long> {

    // ... the original code for LongDateTimeSerializer

}

public class StringDateTimeSerializer extends DateTimeSerializerBase<String> {

    // ... the original code for StringDateTimeSerializer

}