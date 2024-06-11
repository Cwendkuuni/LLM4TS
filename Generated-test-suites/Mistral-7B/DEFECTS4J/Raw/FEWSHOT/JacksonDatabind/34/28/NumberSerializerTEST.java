package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser.NumberType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapperWrapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.TestUtils;

import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Test cases for {@link NumberSerializer}.
 */
public class NumberSerializerTest {
    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testSerialize() throws JsonGenerationException, JsonMappingException, IOException {
        final SerializerProvider provider = new SerializerProvider() {
            @Override
            public JsonGenerator getGeneratorForDefaultTyping(JsonGenerator g) {
                return g;
            }

            @Override
            public JsonParser getParserForDefaultTyping(JsonParser p) {
                return p;
            }
        };

        NumberSerializer serializer = NumberSerializer.instance;

        // Test for BigDecimal
        BigDecimal bd = new BigDecimal("123.456");
        byte[] bytes = mapper.writerWithRootValueSerializer(NumberSerializer.class).writeValueAsBytes(bd);
        JsonParser parser = mapper.getFactory().createParser(TestUtils.createTestInput(bytes));
        Number deserialized = mapper.readerForUpdating(Number.class).readValue(parser);
        assertEquals(bd, deserialized);

        // Test for BigInteger
        BigInteger bi = BigInteger.valueOf(12345);
        bytes = mapper.writerWithRootValueSerializer(NumberSerializer.class).writeValueAsBytes(bi);
        parser = mapper.getFactory().createParser(TestUtils.createTestInput(bytes));
        deserialized = mapper.readerForUpdating(Number.class).readValue(parser);
        assertEquals(bi, deserialized);

        // Test for int
        int i = 123;
        bytes = mapper.writerWithRootValueSerializer(NumberSerializer.class).writeValueAsBytes(i);
        parser = mapper.getFactory().createParser(TestUtils.createTestInput(bytes));
        deserialized = mapper.readerForUpdating(Number.class).readValue(parser);
        assertEquals(i, deserialized.intValue());

        // Test for long
        long l = 12345;
        bytes = mapper.writerWithRootValueSerializer(NumberSerializer.class).writeValueAsBytes(l);
        parser = mapper.getFactory().createParser(TestUtils.createTestInput(bytes));
        deserialized = mapper.readerForUpdating(Number.class).readValue(parser);
        assertEquals(l, deserialized.longValue());

        // Test for double
        double d = 123.456;
        bytes = mapper.writerWithRootValueSerializer(NumberSerializer.class).writeValueAsBytes(d);
        parser = mapper.getFactory().createParser(TestUtils.createTestInput(bytes));
        deserialized = mapper.readerForUpdating(Number.class).readValue(parser);
        assertEquals(d, deserialized.doubleValue(), 0.001);

        // Test for float
        float f = 123.456f;
        bytes = mapper.writerWithRootValueSerializer(NumberSerializer.class).writeValueAsBytes(f);
        parser = mapper.getFactory().createParser(TestUtils.createTestInput(bytes));
        deserialized = mapper.readerForUpdating(Number.class).readValue(parser);
        assertEquals(f, deserialized.floatValue(), 0.001f);

        // Test for byte
        byte b = (byte) 127;
        bytes = mapper.writerWithRootValueSerializer(NumberSerializer.class).writeValueAsBytes(b);
        parser = mapper.getFactory().createParser(TestUtils.createTestInput(bytes));
        deserialized = mapper.readerForUpdating(Number.class).readValue(parser);
        assertEquals(b, deserialized.byteValue());

        // Test for short
        short s = (short) 123;
        bytes = mapper.writerWithRootValueSerializer(NumberSerializer.class).writeValueAsBytes(s);
        parser = mapper.getFactory().createParser(TestUtils.createTestInput(bytes));
        deserialized = mapper.readerForUpdating(Number.class).readValue(parser);
        assertEquals(s, deserialized.shortValue());

        // Test for non-supported number types
        BigInteger bi2 = BigInteger.valueOf(Long.MAX_VALUE.longValue() + 1);
        bytes = mapper.writerWithRootValueSerializer(NumberSerializer.class).writeValueAsBytes(bi2);
        parser = mapper.getFactory().createParser(TestUtils.createTestInput(bytes));
        deserialized = mapper.readerForUpdating(Number.class).readValue(parser);
        assertNull(deserialized);
    }

    @Test
    public void testSchema() throws JsonMappingException, JsonProcessingException {
        JsonMapperWrapper mapperWrapper = new JsonMapperWrapper(mapper);
        NumberSerializer serializer = NumberSerializer.instance;

        // Test for int
        assertEquals("number", serializer.getSchema(new SerializerProvider(), TypeFactory.defaultInstance().constructType(Integer.class)));

        // Test for long
        assertEquals("number", serializer.getSchema(new SerializerProvider(), TypeFactory.defaultInstance().constructType(Long.class)));

        // Test for BigInteger
        assertEquals("number", serializer.getSchema(new SerializerProvider(), TypeFactory.defaultInstance().constructType(BigInteger.class)));

        // Test for BigDecimal
        assertEquals("number", serializer.getSchema(new SerializerProvider(), TypeFactory.defaultInstance().constructType(BigDecimal.class)));

        // Test for float
        assertEquals("number", serializer.getSchema(new SerializerProvider(), TypeFactory.defaultInstance().constructType(Float.class)));

        // Test for double
        assertEquals("number", serializer.getSchema(new SerializerProvider(), TypeFactory.defaultInstance().constructType(Double.class)));

        // Test for non-number types
        assertNull(serializer.getSchema(new SerializerProvider(), TypeFactory.defaultInstance().constructType(String.class)));
        assertNull(serializer.getSchema(new SerializerProvider(), TypeFactory.defaultInstance().constructType(List.class)));
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        NumberSerializer serializer = NumberSerializer.instance;

        JsonFormatVisitorWrapper visitor = mapper.schemaFactory().findIntFormat(null, null, null, null, null);
        serializer.acceptJsonFormatVisitor(visitor, TypeFactory.defaultInstance().constructType(Integer.class));

        visitor = mapper.schemaFactory().findNumberFormat(null, JsonParser.NumberType.BIG_INTEGER, null, null, null);
        serializer.acceptJsonFormatVisitor(visitor, TypeFactory.defaultInstance().constructType(BigInteger.class));

        // Test for unsupported number type
        Class<?> h = NumberSerializer.class.getSuperclass();
        visitor = mapper.schemaFactory().findNumberFormat(null, JsonParser.NumberType.BIG_DECIMAL, null, null, null);
        try {
            serializer.acceptJsonFormatVisitor(visitor, TypeFactory.defaultInstance().constructType(h));
            assert false : "Should not reach this point.";
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}