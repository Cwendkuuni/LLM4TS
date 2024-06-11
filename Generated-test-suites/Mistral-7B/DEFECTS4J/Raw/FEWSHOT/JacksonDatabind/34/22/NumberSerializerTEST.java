package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser.NumberType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapperFactory;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonNumberFormatVisitor;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

/**
 * Test cases for NumberSerializer
 */
public class NumberSerializerTest {

    private final ObjectMapper mapper = JsonMapperFactory.getMapper();

    @Test
    public void testSerializeNumber() throws IOException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        SerializerProvider provider = mapper.getSerializerProvider();

        // test serialization for different types of Number
        BigDecimal bigDecimal = new BigDecimal("123.45");
        BigInteger bigInteger = new BigInteger("12345678901234567890");
        Double doubleValue = 123.45d;
        Float floatValue = 123.45f;
        Integer intValue = 123;
        Long longValue = 12345678901234567890L;
        Short shortValue = 123;
        Byte byteValue = 123;

        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator();
        numberSerializer.serialize(bigDecimal, jsonGenerator, provider);
        jsonGenerator.flush();
        assertEquals(jsonGenerator.getOutputData().toString(), "123.45");

        numberSerializer.serialize(bigInteger, jsonGenerator, provider);
        jsonGenerator.flush();
        assertEquals(jsonGenerator.getOutputData().toString(), "12345678901234567890");

        jsonGenerator = mapper.getFactory().createGenerator();
        numberSerializer.serialize(doubleValue, jsonGenerator, provider);
        jsonGenerator.flush();
        assertEquals(jsonGenerator.getOutputData().toString(), "123.45");

        jsonGenerator = mapper.getFactory().createGenerator();
        numberSerializer.serialize(floatValue, jsonGenerator, provider);
        jsonGenerator.flush();
        assertEquals(jsonGenerator.getOutputData().toString(), "123.45");

        jsonGenerator = mapper.getFactory().createGenerator();
        numberSerializer.serialize(intValue, jsonGenerator, provider);
        jsonGenerator.flush();
        assertEquals(jsonGenerator.getOutputData().toString(), "123");

        jsonGenerator = mapper.getFactory().createGenerator();
        numberSerializer.serialize(longValue, jsonGenerator, provider);
        jsonGenerator.flush();
        assertEquals(jsonGenerator.getOutputData().toString(), "12345678901234567890");

        jsonGenerator = mapper.getFactory().createGenerator();
        numberSerializer.serialize(shortValue, jsonGenerator, provider);
        jsonGenerator.flush();
        assertEquals(jsonGenerator.getOutputData().toString(), "123");

        jsonGenerator = mapper.getFactory().createGenerator();
        numberSerializer.serialize(byteValue, jsonGenerator, provider);
        jsonGenerator.flush();
        assertEquals(jsonGenerator.getOutputData().toString(), "123");

        // test serialization for null
        jsonGenerator = mapper.getFactory().createGenerator();
        numberSerializer.serialize(null, jsonGenerator, provider);
        jsonGenerator.flush();
        assertNull(jsonGenerator.getOutputData().toString());
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        NumberSerializer numberSerializer = NumberSerializer.instance;

        assertEquals(numberSerializer.getSchema(mapper.getSerializerProvider(), Number.class), mapper.getTypeFactory().constructSchema("number"));
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        NumberSerializer numberSerializer = NumberSerializer.instance;

        JsonNumberFormatVisitor visitor = mapper.getFactory().createNumberFormatVisitor(null, null, null, null, null, null, null, NumberType.BIG_INTEGER);
        numberSerializer.acceptJsonFormatVisitor(visitor, Number.class);
    }
}