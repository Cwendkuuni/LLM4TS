package com.fasterxml.jackson.databind.ser.std; // Make sure to import necessary classes

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

/**
 * Test cases for NumberSerializers class.
 */
public class NumberSerializersTest {

    

    @Test
    public void testIntegerSerializer() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        IntegerSerializer intSerializer = new NumberSerializers.IntegerSerializer();
        JsonGenerator gen = mapper.getFactory().createGenerator();

        Number number = 123;
        intSerializer.serialize(number, gen, null);
        gen.flush();

        JsonParser parser = mapper.getFactory().getParser(mapper.writeValueAsString(number));
        assertEquals(123, parser.getValueAsInt());
    }

    @Test
    public void testIntegerSerializerWithType() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        IntegerSerializer intSerializer = new NumberSerializers.IntegerSerializer();

        ObjectNode node = JsonNodeFactory.instance.objectNode();
        intSerializer.serializeWithType(123, JsonGeneratorFactory.instance.createGenerator(node),
                null, mapper.getTypeSerializer());

        assertEquals("123", node.get("@type").asText());
        assertEquals(123, node.get(0).asInt());
    }

    @Test(expected = JsonMappingException.class)
    public void testIntegerSerializerWithString() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        IntegerSerializer intSerializer = new NumberSerializers.IntegerSerializer();

        ObjectNode node = JsonNodeFactory.instance.objectNode();
        intSerializer.serializeWithType("123", JsonGeneratorFactory.instance.createGenerator(node),
                null, mapper.getTypeSerializer());
    }

    @Test
    public void testIntegerSerializerNegativeNumber() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        IntegerSerializer intSerializer = new NumberSerializers.IntegerSerializer();

        Number number = -123;
        intSerializer.serialize(number, null, null);
        String json = mapper.writeValueAsString(number);
        JsonParser parser = mapper.getFactory().getParser(json);

        assertEquals(-123, parser.getValueAsInt());
    }

    @Test(expected = JsonMappingException.class)
    public void testIntegerSerializerTooLargeNumber() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        IntegerSerializer intSerializer = new NumberSerializers.IntegerSerializer();

        long maxLong = Long.MAX_VALUE;
        intSerializer.serialize(maxLong, null, null);
        String json = mapper.writeValueAsString(maxLong);
        JsonParser parser = mapper.getFactory().getParser(json);
    }

    @Test
    public void testShortSerializer() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ShortSerializer shortSerializer = new NumberSerializers.ShortSerializer();

        Number number = (short) 123;
        shortSerializer.serialize(number, null, null);
        String json = mapper.writeValueAsString(number);
        JsonParser parser = mapper.getFactory().getParser(json);

        assertEquals(123, parser.getValueAsInt());
    }

    @Test
    public void testShortSerializerNegativeNumber() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ShortSerializer shortSerializer = new NumberSerializers.ShortSerializer();

        Number number = (short) -123;
        shortSerializer.serialize(number, null, null);
        String json = mapper.writeValueAsString(number);
        JsonParser parser = mapper.getFactory().getParser(json);

        assertEquals(-123, parser.getValueAsInt());
    }

    @Test
    public void testLongSerializer() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        LongSerializer longSerializer = new NumberSerializers.LongSerializer();

        Number number = 123L;
        longSerializer.serialize(number, null, null);
        String json = mapper.writeValueAsString(number);
        JsonParser parser = mapper.getFactory().getParser(json);

        assertEquals(123, parser.getValueAsLong());
    }

    @Test
    public void testFloatSerializer() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FloatSerializer floatSerializer = new NumberSerializers.FloatSerializer();

        Number number = 123.45f;
        floatSerializer.serialize(number, null, null);
        String json = mapper.writeValueAsString(number);
        JsonParser parser = mapper.getFactory().getParser(json);

        assertEquals(123.45f, parser.getValueAsFloat(), 0.01f);
    }

    @Test
    public void testDoubleSerializer() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        DoubleSerializer doubleSerializer = new NumberSerializers.DoubleSerializer();

        Number number = 123.45;
        doubleSerializer.serialize(number, null, null);
        String json = mapper.writeValueAsString(number);
        JsonParser parser = mapper.getFactory().getParser(json);

        assertEquals(123.45, parser.getValueAsDouble(), 0.01);
    }

    @Test(expected = JsonMappingException.class)
    public void testDoubleSerializerWithInfinity() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        DoubleSerializer doubleSerializer = new NumberSerializers.DoubleSerializer();

        double infinity = Double.POSITIVE_INFINITY;
        doubleSerializer.serialize(infinity, null, null);
        String json = mapper.writeValueAsString(infinity);
        JsonParser parser = mapper.getFactory().getParser(json);
    }

    @Test(expected = JsonMappingException.class)
    public void testDoubleSerializerWithNaN() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        DoubleSerializer doubleSerializer = new NumberSerializers.DoubleSerializer();

        double nan = Double.NaN;
        doubleSerializer.serialize(nan, null, null);
        String json = mapper.writeValueAsString(nan);
        JsonParser parser = mapper.getFactory().getParser(json);
    }

    @Test
    public void testNullSerializer() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        ObjectNode node = JsonNodeFactory.instance.objectNode();
        NumberSerializers.Base<Object> nullSerializer = new NumberSerializers.Base<Object>(null, JsonParser.NumberType.NULL, "null");

        nullSerializer.serializeWithType(null, JsonGeneratorFactory.instance.createGenerator(node), null, mapper.getTypeSerializer());

        assertNull(node.get(0));
        assertEquals("null", node.get("@type").asText());
    }

    @Test
    public void testAddAllMethod() {
        NumberSerializers numberSerializers = new NumberSerializers();
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();

        numberSerializers.addAll(allDeserializers);

        assertEquals(8, allDeserializers.size());
        assertTrue(allDeserializers.containsKey(Integer.class.getName()));
        assertTrue(allDeserializers.containsKey(Integer.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Long.class.getName()));
        assertTrue(allDeserializers.containsKey(Long.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Byte.class.getName()));
        assertTrue(allDeserializers.containsKey(Byte.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Short.class.getName()));
        assertTrue(allDeserializers.containsKey(Short.TYPE.getName()));
    }

    
}