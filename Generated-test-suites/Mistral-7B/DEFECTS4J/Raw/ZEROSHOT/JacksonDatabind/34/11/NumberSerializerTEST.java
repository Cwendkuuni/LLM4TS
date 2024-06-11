package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;

/**
 * Test class for NumberSerializer.java
 */
public class NumberSerializerTest {

    private final ObjectMapper objectMapper = new JsonMapper();

    @Test
    public void testSerializeBigDecimal() throws JsonGenerationException, IOException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(new StringBuilder(), null);
        Number value = new BigDecimal("123.45");

        numberSerializer.serialize(value, jsonGenerator, new SerializerProvider());

        assertNotNull(jsonGenerator.getOutputBuffer());
        assertEquals("123.45", jsonGenerator.getOutputBuffer().toString());
    }

    @Test
    public void testSerializeBigInteger() throws JsonGenerationException, IOException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(new StringBuilder(), null);
        Number value = new BigInteger("12345678901234567890");

        numberSerializer.serialize(value, jsonGenerator, new SerializerProvider());

        assertNotNull(jsonGenerator.getOutputBuffer());
        assertEquals("12345678901234567890", jsonGenerator.getOutputBuffer().toString());
    }

    @Test
    public void testSerializeDifferentTypes() throws JsonGenerationException, IOException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(new StringBuilder(), null);

        Number valueInt = 123;
        Number valueLong = 123L;
        Number valueDouble = 123.45;
        Number valueFloat = 123.45f;
        Number valueByte = (byte) 123;
        Number valueShort = (short) 123;
        Number valueBigDecimal = new BigDecimal("123.45");
        Number valueBigInteger = new BigInteger("12345678901234567890");

        numberSerializer.serialize(valueInt, jsonGenerator, new SerializerProvider());
        assertEquals(Integer.toString(valueInt.intValue()), jsonGenerator.getOutputBuffer().toString());

        numberSerializer.serialize(valueLong, jsonGenerator, new SerializerProvider());
        assertEquals(Long.toString(valueLong.longValue()), jsonGenerator.getOutputBuffer().toString());

        numberSerializer.serialize(valueDouble, jsonGenerator, new SerializerProvider());
        assertEquals(Double.toString(valueDouble.doubleValue()), jsonGenerator.getOutputBuffer().toString());

        numberSerializer.serialize(valueFloat, jsonGenerator, new SerializerProvider());
        assertEquals(Float.toString(valueFloat.floatValue()), jsonGenerator.getOutputBuffer().toString());

        numberSerializer.serialize(valueByte, jsonGenerator, new SerializerProvider());
        assertEquals(Integer.toString(valueByte.intValue()), jsonGenerator.getOutputBuffer().toString());

        numberSerializer.serialize(valueShort, jsonGenerator, new SerializerProvider());
        assertEquals(Integer.toString(valueShort.intValue()), jsonGenerator.getOutputBuffer().toString());

        numberSerializer.serialize(valueBigDecimal, jsonGenerator, new SerializerProvider());
        assertNotNull(jsonGenerator.getOutputBuffer());
        assertEquals(valueBigDecimal.toString(), jsonGenerator.getOutputBuffer().toString());

        numberSerializer.serialize(valueBigInteger, jsonGenerator, new SerializerProvider());
        assertNotNull(jsonGenerator.getOutputBuffer());
        assertEquals(valueBigInteger.toString(), jsonGenerator.getOutputBuffer().toString());
    }

    @Test
    public void testDeserialize() throws JsonMappingException, IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser(new TextNode("123.45").asText());
        ObjectMapper objectMapper = new JsonMapper();
        Number deserializedNumber = objectMapper.readValue(jsonParser, Number.class);

        assertEquals(new BigDecimal("123.45"), deserializedNumber);
    }

    @Test
    public void testDeserializeBigInteger() throws JsonMappingException, IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser(new TextNode("12345678901234567890").asText());
        ObjectMapper objectMapper = new JsonMapper();
        Number deserializedNumber = objectMapper.readValue(jsonParser, Number.class);

        assertEquals(new BigInteger("12345678901234567890"), deserializedNumber);
    }

    @Test
    public void testDeserializeDifferentTypes() throws JsonMappingException, IOException {
        ObjectMapper objectMapper = new JsonMapper();
        JsonParser jsonParser = objectMapper.getFactory().createParser(new TextNode("123").asText());

        Number deserializedNumber = objectMapper.readValue(jsonParser, Number.class);
        assertEquals(123, deserializedNumber.intValue());

        jsonParser = objectMapper.getFactory().createParser(new TextNode("123L").asText());
        deserializedNumber = objectMapper.readValue(jsonParser, Number.class);
        assertEquals(123L, deserializedNumber.longValue());

        jsonParser = objectMapper.getFactory().createParser(new TextNode("123.45").asText());
        deserializedNumber = objectMapper.readValue(jsonParser, Number.class);
        assertEquals(123.45, deserializedNumber.doubleValue(), 0.001);

        jsonParser = objectMapper.getFactory().createParser(new TextNode("123.45f").asText());
        deserializedNumber = objectMapper.readValue(jsonParser, Number.class);
        assertEquals(123.45f, deserializedNumber.floatValue(), 0.001);

        jsonParser = objectMapper.getFactory().createParser(new TextNode("123").asText());
        deserializedNumber = objectMapper.readValue(jsonParser, Number.class);
        assertEquals((byte) 123, deserializedNumber.byteValue());

        jsonParser = objectMapper.getFactory().createParser(new TextNode("123S").asText());
        deserializedNumber = objectMapper.readValue(jsonParser, Number.class);
        assertEquals((short) 123, deserializedNumber.shortValue());

        jsonParser = objectMapper.getFactory().createParser(new TextNode("123.45B").asText());
        deserializedNumber = objectMapper.readValue(jsonParser, Number.class);
        BigDecimal bigDecimal = new BigDecimal("123.45");
        assertEquals(bigDecimal, deserializedNumber);

        jsonParser = objectMapper.getFactory().createParser(new TextNode("12345678901234567890B").asText());
        deserializedNumber = objectMapper.readValue(jsonParser, Number.class);
        BigInteger bigInteger = new BigInteger("12345678901234567890");
        assertEquals(bigInteger, deserializedNumber);
    }

    @Test
    public void testGetSchema() throws JsonMappingException, IOException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        JsonNode schemaNode = numberSerializer.getSchema(objectMapper.getSerializerProvider(), Number.class);

        assertEquals("number", schemaNode.asText());
    }
}