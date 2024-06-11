package com.example.numberserializer;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.*;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Test class for NumberSerializer
 */
public class NumberSerializerTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testSerializeNumber() throws JsonProcessingException, JsonMappingException, IOException {
        NumberSerializer serializer = NumberSerializer.instance;

        // Test serialization for various Number types
        Number number = 1;
        assertNull(serializer.getSchema(mapper.getSerializerProvider(), Number.class)); // Test for null schema
        NumberSerializer testSerializer = (NumberSerializer) serializer;
        JsonGenerator generator = mapper.getFactory().createGenerator();
        serializer.serialize(number, generator, mapper.getSerializerProvider());
        generator.flush();
        assertEquals("1", generator.getOutputBuffer().toString());

        number = BigDecimal.valueOf(2.5);
        assertNull(serializer.getSchema(mapper.getSerializerProvider(), Number.class)); // Test for null schema
        serializer.serialize(number, generator, mapper.getSerializerProvider());
        generator.flush();
        assertEquals("2.5", generator.getOutputBuffer().toString());

        number = BigInteger.valueOf(3);
        assertNull(serializer.getSchema(mapper.getSerializerProvider(), Number.class)); // Test for null schema
        serializer.serialize(number, generator, mapper.getSerializerProvider());
        generator.flush();
        assertEquals("3", generator.getOutputBuffer().toString());

        number = 4L;
        assertNull(serializer.getSchema(mapper.getSerializerProvider(), Number.class)); // Test for null schema
        serializer.serialize(number, generator, mapper.getSerializerProvider());
        generator.flush();
        assertEquals("4", generator.getOutputBuffer().toString());

        number = 5.5F;
        assertNull(serializer.getSchema(mapper.getSerializerProvider(), Number.class)); // Test for null schema
        serializer.serialize(number, generator, mapper.getSerializerProvider());
        generator.flush();
        assertEquals("5.5", generator.getOutputBuffer().toString());

        number = 6.0;
        assertNull(serializer.getSchema(mapper.getSerializerProvider(), Number.class)); // Test for null schema
        serializer.serialize(number, generator, mapper.getSerializerProvider());
        generator.flush();
        assertEquals("6", generator.getOutputBuffer().toString());

        number = (byte) 7;
        assertNull(serializer.getSchema(mapper.getSerializerProvider(), Number.class)); // Test for null schema
        serializer.serialize(number, generator, mapper.getSerializerProvider());
        generator.flush();
        assertEquals("7", generator.getOutputBuffer().toString());

        number = (short) 8;
        assertNull(serializer.getSchema(mapper.getSerializerProvider(), Number.class)); // Test for null schema
        serializer.serialize(number, generator, mapper.getSerializerProvider());
        generator.flush();
        assertEquals("8", generator.getOutputBuffer().toString());

        number = new Double("9.9");
        assertNull(serializer.getSchema(mapper.getSerializerProvider(), Number.class)); // Test for null schema
        serializer.serialize(number, generator, mapper.getSerializerProvider());
        generator.flush();
        assertEquals("9.9", generator.getOutputBuffer().toString());

        // Test serialization for null input
        Number nullNumber = null;
        serializer.serialize(nullNumber, generator, mapper.getSerializerProvider());
        generator.flush();
        assertTrue(generator.getOutputBuffer().toString().isEmpty());

        // Test serialization for BigDecimal with large values
        BigDecimal largeDecimal = new BigDecimal("1234567890123456789012345678901234567890");
        assertNull(serializer.getSchema(mapper.getSerializerProvider(), Number.class)); // Test for null schema
        serializer.serialize(largeDecimal, generator, mapper.getSerializerProvider());
        generator.flush();
        assertEquals("1234567890123456789012345678901234567890", generator.getOutputBuffer().toString());

        // Test serialization for BigInteger with large values
        BigInteger largeInteger = BigInteger.valueOf(Long.MAX_VALUE);
        assertNull(serializer.getSchema(mapper.getSerializerProvider(), Number.class)); // Test for null schema
        serializer.serialize(largeInteger, generator, mapper.getSerializerProvider());
        generator.flush();
        assertEquals(Long.toString(Long.MAX_VALUE), generator.getOutputBuffer().toString());
    }

    @Test(expected = JsonGenerationException.class)
    public void testSerializeInvalidNumber() throws JsonProcessingException, JsonMappingException, IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator generator = mapper.getFactory().createGenerator();
        Object obj = new Object();
        serializer.serialize(obj, generator, mapper.getSerializerProvider());
    }

    @Test(expected = JsonMappingException.class)
    public void testGetSchemaInvalidTypeHint() throws JsonProcessingException, JsonMappingException, IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        TypeFactory typeFactory = mapper.getTypeFactory();
        JsonFormatVisitorWrapper visitor = mapper.getFactory().getPolymorphicSerializer
                (JsonSerializer.None.class, typeFactory.constructType(String[].class));
        serializer.acceptJsonFormatVisitor(visitor, typeFactory.constructType(String[].class));
    }
}