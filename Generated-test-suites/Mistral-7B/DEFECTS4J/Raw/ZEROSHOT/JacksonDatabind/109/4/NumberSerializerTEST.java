package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.NumberNode;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

/**
 * Test for {@link NumberSerializer} class.
 */
public class NumberSerializerTest {

    /**
     * Test serialization of various number types using default serialization.
     */
    @Test
    public void testDefaultSerialization() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        // Test serialization of various number types
        NumberSerializer serializer = NumberSerializer.instance;
        SerializerProvider provider = mapper.getSerializerProvider();

        Number testNumber[] = {new BigDecimal("123.456"), new BigInteger("789"), 123, 45.6, 78, 9};
        for (Number number : testNumber) {
            JsonGenerator gen = mapper.getFactory().createGenerator();
            serializer.serialize(number, gen, provider);
            gen.flush();

            String expectedJson = "";
            if (number instanceof BigDecimal) {
                expectedJson = "123.456";
            } else if (number instanceof BigInteger) {
                expectedJson = "789";
            } else {
                expectedJson = String.valueOf(number.intValue());
            }
            assertEquals(expectedJson, gen.getOutputContent());
        }
    }

    /**
     * Test serialization of various number types using custom {@link JsonFormat}.
     */
    @Test
    public void testCustomSerialization() throws JsonProcessingException, JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();

        // Test serialization of BigDecimal using custom JsonFormat
        JsonFormat<Number> jsonFormat = new JsonFormat.Builder<Number>()
                .shape(JsonFormat.Shape.STRING)
                .build();

        Number testNumber = new BigDecimal("123.456");
        String expectedJson = testNumber.toString();

        SerializerProvider provider = mapper.getSerializerProvider();
        JsonGenerator gen = mapper.getFactory().createGenerator();

        NumberSerializer serializer = new NumberSerializer(Number.class) {
            @Override
            public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property)
                    throws JsonMappingException {
                return serializer -> serializer.acceptJsonFormatVisitor(
                        new JsonFormatVisitorWrapper(prov.getObjectCodec().getJsonFactory()),
                        property.getType());
            }

            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
                    throws JsonMappingException {
                visitor.visitStringFormat(typeHint, jsonFormat);
            }
        };

        serializer.serialize(testNumber, gen, provider);
        gen.flush();

        assertEquals(expectedJson, gen.getOutputContent());
    }

    /**
     * Test deserialization of various number types.
     */
    @Test
    public void testDeserialization() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        String jsonString =
                "{\"number\":123.456,\"bigInteger\":789,\"int\":123,"
                + "\"double\":45.6,\"float\":78,\"byte\":9}";

        Number number[] = {null, null, null, null, null, null};

        // Test deserialization of various number types
        for (int i = 0; i < number.length; i++) {
            JsonParser parser = mapper.getFactory().createParser(jsonString);
            number[i] = mapper.readValue(parser, Number.class);

            // Verify deserialized value
            if (number[i] instanceof BigDecimal) {
                assertEquals(new BigDecimal("123.456"), number[i]);
            } else if (number[i] instanceof BigInteger) {
                assertEquals(new BigInteger("789"), number[i]);
            } else {
                assertEquals(i + 123, number[i].intValue());
            }
            parser.close();
        }
    }

    /**
     * Test schema generation.
     */
    @Test
    public void testGetSchema() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        JsonNode expectedSchema = mapper.getFactory().createObjectNode();
        expectedSchema.put("type", "number");
        expectedSchema.put("description", "Number");

        Number serializer = NumberSerializer.instance;
        JsonNode schema = serializer.getSchema(mapper.getSerializerProvider(), Number.class);

        // Verify schema matches expected schema
        assertEquals(expectedSchema.toString(), schema.toString());
    }
}