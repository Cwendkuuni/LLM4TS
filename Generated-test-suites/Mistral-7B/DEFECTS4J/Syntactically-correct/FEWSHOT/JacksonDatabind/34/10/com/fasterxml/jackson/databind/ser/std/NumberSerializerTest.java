package com.fasterxml.jackson.databind.ser.std;

import static com.fasterxml.jackson.core.util.TestUtil.createParser;
import static com.fasterxml.jackson.core.util.TestUtil.writeValue;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;

import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Test cases for {@link NumberSerializer} class.
 */
@JacksonStdImpl
@SuppressWarnings("serial")
public class NumberSerializerTest {

    @Test
    public void testSerializeInteger() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Integer.class);
        JsonGenerator generator = createTestGenerator();
        Integer testNumber = 123;
        serializer.serialize(testNumber, generator, new SerializerProvider());
        assertEquals("123", generator.getOutputContent().toString());
    }

    @Test
    public void testSerializeLong() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Long.class);
        JsonGenerator generator = createTestGenerator();
        Long testNumber = 123L;
        serializer.serialize(testNumber, generator, new SerializerProvider());
        assertEquals("123", generator.getOutputContent().toString());
    }

    @Test
    public void testSerializeFloat() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Float.class);
        JsonGenerator generator = createTestGenerator();
        Float testNumber = 123.45F;
        serializer.serialize(testNumber, generator, new SerializerProvider());
        assertEquals("123.45", generator.getOutputContent().toString());
    }

    @Test
    public void testSerializeDouble() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Double.class);
        JsonGenerator generator = createTestGenerator();
        Double testNumber = 123.45;
        serializer.serialize(testNumber, generator, new SerializerProvider());
        assertEquals("123.45", generator.getOutputContent().toString());
    }

    @Test
    public void testSerializeBigInteger() throws IOException {
        NumberSerializer serializer = new NumberSerializer(BigInteger.class);
        JsonGenerator generator = createTestGenerator();
        BigInteger testNumber = BigInteger.valueOf(1234567890123L);
        serializer.serialize(testNumber, generator, new SerializerProvider());
        assertEquals("1234567890123", generator.getOutputContent().toString());
    }

    @Test
    public void testSerializeBigDecimal() throws IOException {
        NumberSerializer serializer = new NumberSerializer(BigDecimal.class);
        JsonGenerator generator = createTestGenerator();
        BigDecimal testNumber = new BigDecimal("123.456");
        serializer.serialize(testNumber, generator, new SerializerProvider());
        assertEquals("123.456", generator.getOutputContent().toString());
    }

    @Test
    public void testSerializeOtherNumberTypes() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonGenerator generator = createTestGenerator();
        Byte testNumberByte = Byte.MIN_VALUE;
        Short testNumberShort = Short.MIN_VALUE;
        serializer.serialize(testNumberByte, generator, new SerializerProvider());
        assertEquals("-128", generator.getOutputContent().toString());
        serializer.serialize(testNumberShort, generator, new SerializerProvider());
        assertEquals("-32768", generator.getOutputContent().toString());

        generator.clearContent();
        serializer.serialize(testNumberByte, generator, new SerializerProvider());
        assertEquals("(byte) -128", generator.getOutputContent().toString());
        serializer.serialize(testNumberShort, generator, new SerializerProvider());
        assertEquals("(short) -32768", generator.getOutputContent().toString());
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonNode schema = serializer.getSchema(new SerializerProvider(), Object.class);
        assertEquals("number", schema.asText());
    }

    @Test
    public void testAcceptJsonFormatVisitorInteger() throws JsonMappingException {
        NumberSerializer serializer = new NumberSerializer(Integer.class);
        JsonFormatVisitorWrapper visitor = createTestVisitor();
        serializer.acceptJsonFormatVisitor(visitor, Object.class.getType());
        assertEquals("number", visitor.getSchemaName());
        assertTrue(visitor.numberFormat.hasDecimalScale());
        assertFalse(visitor.numberFormat.hasLargeNumbers());
        assertEquals(JsonParser.NumberType.INT, visitor.numberFormat.getNumberType());
    }

    @Test
    public void testAcceptJsonFormatVisitorBigInteger() throws JsonMappingException {
        NumberSerializer serializer = new NumberSerializer(BigInteger.class);
        JsonFormatVisitorWrapper visitor = createTestVisitor();
        serializer.acceptJsonFormatVisitor(visitor, Object.class.getType());
        assertEquals("number", visitor.getSchemaName());
        assertTrue(visitor.numberFormat.hasDecimalScale());
        assertTrue(visitor.numberFormat.hasLargeNumbers());
        assertEquals(JsonParser.NumberType.BIG_INTEGER, visitor.numberFormat.getNumberType());
    }

    @Test(expected = JsonMappingException.class)
    public void testAcceptJsonFormatVisitorInvalidType() throws JsonMappingException {
        NumberSerializer serializer = new NumberSerializer(String.class);
        JsonFormatVisitorWrapper visitor = createTestVisitor();
        serializer.acceptJsonFormatVisitor(visitor, Object.class.getType());
    }

    private JsonGenerator createTestGenerator() throws IOException {
        return new JsonFactory().createGenerator(new ByteArrayOutputStream(), null);
    }

    private JsonFormatVisitorWrapper createTestVisitor() {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.getFactory().getSerializerProvider().getDefaultPropertyBasedSerializerProvider().getRootValueSerializer(Object.class.getType())
                .acceptJsonFormatVisitor(new JsonSerializer.StdSerializer<>(Object.class) {}.getClass(), new TypeReference<JsonNode>() {});
    }
}