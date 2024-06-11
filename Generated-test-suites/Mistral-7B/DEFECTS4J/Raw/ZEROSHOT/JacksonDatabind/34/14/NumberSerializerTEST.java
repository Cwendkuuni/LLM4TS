package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser.NumberType;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.node.JsonNode;

/**
 * Test class for NumberSerializer
 */
public class NumberSerializerTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testSerialize() throws JsonGenerationException, IOException {
        NumberSerializer numberSerializer = NumberSerializer.instance;

        // Test serialization for various Number types
        BigDecimal bigDecimal = new BigDecimal("123.45");
        BigInteger bigInteger = BigInteger.valueOf(456);
        Integer integer = 1;
        Long longNumber = 2L;
        Double doubleValue = 3.14;
        Float floatValue = 5.6F;
        Byte byteValue = 7;
        Short shortValue = 8;

        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(System.out);
        numberSerializer.serialize(bigDecimal, jsonGenerator, mapper.getSerializerProvider());
        numberSerializer.serialize(bigInteger, jsonGenerator, mapper.getSerializerProvider());
        numberSerializer.serialize(integer, jsonGenerator, mapper.getSerializerProvider());
        numberSerializer.serialize(longNumber, jsonGenerator, mapper.getSerializerProvider());
        numberSerializer.serialize(doubleValue, jsonGenerator, mapper.getSerializerProvider());
        numberSerializer.serialize(floatValue, jsonGenerator, mapper.getSerializerProvider());
        numberSerializer.serialize(byteValue, jsonGenerator, mapper.getSerializerProvider());
        numberSerializer.serialize(shortValue, jsonGenerator, mapper.getSerializerProvider());

        jsonGenerator.close();
        // Verify the output is as expected
        String expectedOutput = "{\"123.45\"}" +
                                "{\"456\"}" +
                                "1" +
                                "2" +
                                "3.14" +
                                "5.6" +
                                "7" +
                                "8";
        String actualOutput = mapper.readTree(System.in).asText();
        assertEquals(expectedOutput, actualOutput);

        // Test serialization for null Number
        JsonGenerator jsonGeneratorNull = mapper.getFactory().createGenerator(System.out);
        numberSerializer.serialize(null, jsonGeneratorNull, mapper.getSerializerProvider());
        jsonGeneratorNull.close();

        String actualOutputNull = mapper.readTree(System.in).asText();
        assertEquals("null", actualOutputNull);
    }

    @Test
    public void testGetSchema() throws JsonProcessingException {
        NumberSerializer numberSerializer = NumberSerializer.instance;

        JsonNode schema = numberSerializer.getSchema(mapper.getSerializerProvider(), Number.class);
        String schemaType = schema.path("type").asText();
        assertTrue(schemaType.equals("number") || schemaType.equals("integer"));
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        NumberSerializer numberSerializer = NumberSerializer.instance;

        ObjectMapper mapperForTest = new ObjectMapper();
        JsonFormatVisitorWrapper jsonFormatVisitorWrapper = mapperForTest.getSerializationConfig()
                                                                            .getDefaultObjectMapperConfig()
                                                                            .getFactory()
                                                                            .constructJsonFormatVisitor(null, null, null);

        // Test for int type
        if (numberSerializer._isInt) {
            numberSerializer.acceptJsonFormatVisitor(jsonFormatVisitorWrapper, Number.class);
            NumberFormat numberFormatInt = jsonFormatVisitorWrapper.getFormatForSerializingFields();
            JsonParser.NumberType numberTypeInt = numberFormatInt.getNumberType();
            assertEquals(JsonParser.NumberType.INT, numberTypeInt);
        }

        // Test for BigDecimal type
        else {
            numberSerializer.acceptJsonFormatVisitor(jsonFormatVisitorWrapper, BigDecimal.class);
            NumberFormat numberFormatBigDecimal = jsonFormatVisitorWrapper.getFormatForSerializingFields();
            JsonParser.NumberType numberTypeBigDecimal = numberFormatBigDecimal.getNumberType();
            assertEquals(JsonParser.NumberType.BIG_DECIMAL, numberTypeBigDecimal);
        }
    }
}