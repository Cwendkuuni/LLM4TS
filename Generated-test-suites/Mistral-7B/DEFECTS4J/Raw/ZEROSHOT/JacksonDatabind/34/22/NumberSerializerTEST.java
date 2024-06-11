package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider.Feature;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

/**
 * Test case for {@link NumberSerializer} class.
 */
public class NumberSerializerTest {

    @Test
    public void testSerializeNumber() throws JsonProcessingException, JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        NumberSerializer numberSerializer = NumberSerializer.instance;

        // Test serialization of various Number types
        JsonGenerator gen = mapper.getFactory().createGenerator();
        SerializerProvider provider = mapper.getSerializerProvider();

        // BigDecimal
        BigDecimal bigDecimal = new BigDecimal("123.45");
        numberSerializer.serialize(bigDecimal, gen, provider);
        gen.flush();
        JsonParser parser = mapper.getFactory().createParser(gen.getOutputStream());
        JsonNode node = mapper.readValue(parser, JsonNode.class);
        assertEquals(bigDecimal.toString(), node.asText());

        // BigInteger
        BigInteger bigInteger = new BigInteger("123");
        numberSerializer.serialize(bigInteger, gen, provider);
        gen.flush();
        parser = mapper.getFactory().createParser(gen.getOutputStream());
        node = mapper.readValue(parser, JsonNode.class);
        assertEquals(bigInteger.toString(), node.asText());

        // Integer
        Integer integer = 123;
        numberSerializer.serialize(integer, gen, provider);
        gen.flush();
        parser = mapper.getFactory().createParser(gen.getOutputStream());
        node = mapper.readValue(parser, JsonNode.class);
        assertEquals(integer.toString(), node.asText());

        // Long
        Long longValue = 123L;
        numberSerializer.serialize(longValue, gen, provider);
        gen.flush();
        parser = mapper.getFactory().createParser(gen.getOutputStream());
        node = mapper.readValue(parser, JsonNode.class);
        assertEquals(longValue.toString(), node.asText());

        // Double
        Double doubleValue = 123.45;
        numberSerializer.serialize(doubleValue, gen, provider);
        gen.flush();
        parser = mapper.getFactory().createParser(gen.getOutputStream());
        node = mapper.readValue(parser, JsonNode.class);
        assertEquals(doubleValue.toString(), node.asText());

        // Float
        Float floatValue = 123.45f;
        numberSerializer.serialize(floatValue, gen, provider);
        gen.flush();
        parser = mapper.getFactory().createParser(gen.getOutputStream());
        node = mapper.readValue(parser, JsonNode.class);
        assertEquals(Float.toString(floatValue), node.asText());

        // Byte and Short
        Byte byteValue = (byte) 123;
        Short shortValue = (short) 123;
        numberSerializer.serialize(byteValue, gen, provider);
        gen.flush();
        parser = mapper.getFactory().createParser(gen.getOutputStream());
        node = mapper.readValue(parser, JsonNode.class);
        assertEquals(byteValue.intValue(), node.asInt());

        numberSerializer.serialize(shortValue, gen, provider);
        gen.flush();
        parser = mapper.getFactory().createParser(gen.getOutputStream());
        node = mapper.readValue(parser, JsonNode.class);
        assertEquals(shortValue.intValue(), node.asInt());

        // Unsupported Number types
        Object unsupportedNumber = new Object() {
            @Override
            public Number doubleValue() {
                return null;
            }
        };
        try {
            numberSerializer.serialize(unsupportedNumber, gen, provider);
            fail("Expected JsonGenerationException");
        } catch (JsonGenerationException e) {
            // Expected
        }

        // Test serialization with JsonFormatVisitorWrapper
        JsonFormatVisitorWrapper formatVisitor = mapper.getFactory()
                .constructJsonFormatVisitor(JsonFormatVisitorWrapper.Std.NUMBER_SERIALIZER);
        numberSerializer.acceptJsonFormatVisitor(formatVisitor, null);
        JsonNode schemaNode = formatVisitor.getSchemaNode();
        assertEquals("number", schemaNode.asText());
        assertTrue(schemaNode.get("nullable"), schemaNode);
        assertFalse(schemaNode.get("exclusiveMaximum"), schemaNode);
        assertFalse(schemaNode.get("exclusiveMinimum"), schemaNode);
    }
}