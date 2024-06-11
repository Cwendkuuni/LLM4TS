package com.example.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Random;

/**
 * Test class for NumberSerializer.
 */
public class NumberSerializerTest {

    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void testSerializeBigDecimal() throws JsonGenerationException, IOException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        BigDecimal bigDecimal = new BigDecimal("123.456");
        numberSerializer.serialize(bigDecimal, jsonGenerator, null);
        jsonGenerator.flush();
        JsonParser jsonParser = objectMapper.getFactory().createParser(System.in);
        Object readValue = jsonParser.readValueAs(Object.class);
        assertEquals(bigDecimal, readValue);
    }

    @Test
    public void testSerializeBigInteger() throws JsonGenerationException, IOException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        BigInteger bigInteger = new BigInteger("12345678901234567890");
        numberSerializer.serialize(bigInteger, jsonGenerator, null);
        jsonGenerator.flush();
        JsonParser jsonParser = objectMapper.getFactory().createParser(System.in);
        Object readValue = jsonParser.readValueAs(Object.class);
        assertEquals(bigInteger, readValue);
    }

    @Test
    public void testSerializeInteger() throws JsonGenerationException, IOException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        int integer = 123;
        numberSerializer.serialize(integer, jsonGenerator, null);
        jsonGenerator.flush();
        JsonParser jsonParser = objectMapper.getFactory().createParser(System.in);
        Object readValue = jsonParser.readValueAs(Object.class);
        assertEquals(integer, readValue);
    }

    @Test
    public void testSerializeDouble() throws JsonGenerationException, IOException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        double doubleValue = 123.456;
        numberSerializer.serialize(doubleValue, jsonGenerator, null);
        jsonGenerator.flush();
        JsonParser jsonParser = objectMapper.getFactory().createParser(System.in);
        Object readValue = jsonParser.readValueAs(Object.class);
        assertEquals(doubleValue, readValue, 0.001); // allow for small error due to floating point representation
    }

    @Test
    public void testSerializeContextualSerializer() throws JsonGenerationException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        BigDecimal bigDecimal = new BigDecimal("123.456");
        ObjectMapper contextualObjectMapper = new ObjectMapper();
        contextualObjectMapper.addSerializer(BigDecimal.class, new ToStringSerializer());
        JsonGenerator contextualJsonGenerator = contextualObjectMapper.getFactory().createGenerator(System.out);
        NumberSerializer numberSerializer = NumberSerializer.instance;

        // Test that NumberSerializer is used by default
        numberSerializer.serialize(bigDecimal, jsonGenerator, null);
        jsonGenerator.flush();
        JsonParser jsonParser = objectMapper.getFactory().createParser(System.in);
        Object readValue = jsonParser.readValueAs(Object.class);
        assertEquals(bigDecimal, readValue);

        // Test that ToStringSerializer is used when specified
        contextualJsonGenerator.setCodec(contextualObjectMapper.getSerializerProvider());
        numberSerializer.serialize(bigDecimal, contextualJsonGenerator, null);
        contextualJsonGenerator.flush();
        jsonParser = contextualObjectMapper.getFactory().createParser(System.in);
        readValue = jsonParser.readValueAs(Object.class);
        assertEquals("123.456", readValue.toString());
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeUnsupportedNumberType() throws JsonGenerationException, IOException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        Object unsupportedNumberType = new Random().nextObject();
        numberSerializer.serialize(unsupportedNumberType, jsonGenerator, null);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        JsonFormatVisitorWrapper jsonFormatVisitorWrapper = null;
        try {
            jsonFormatVisitorWrapper = numberSerializer.acceptJsonFormatVisitor(null, null);
        } catch (JsonMappingException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
        assertTrue(jsonFormatVisitorWrapper != null);
    }
}