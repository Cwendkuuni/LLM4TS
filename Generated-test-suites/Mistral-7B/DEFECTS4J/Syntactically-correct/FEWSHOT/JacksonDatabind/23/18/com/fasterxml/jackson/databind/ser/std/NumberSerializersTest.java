package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNumberNode;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.Base;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.IntegerSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.LongSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.ShortSerializer;

public class NumberSerializersTest {

    @Test
    public void testShortSerializer() throws JsonProcessingException {
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser("{ \"num\": 123 }");
        Base deserializer = NumberDeserializers.findShortValueDeserializer(Short.class, parser.getCurrentName(), parser.getCodec());
        Short value = deserializer.deserialize(parser, null);
        assertEquals((short) 123, value);
    }

    @Test
    public void testIntegerSerializer() throws JsonProcessingException {
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser("{ \"num\": 123 }");
        Base deserializer = NumberDeserializers.findIntLikeValueDeserializer(Integer.class, parser.getCurrentName(), parser.getCodec());
        Integer value = deserializer.deserialize(parser, null);
        assertEquals(123, value.intValue());
    }

    @Test
    public void testIntLikeSerializer() throws JsonProcessingException {
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser("{ \"num\": 123 }");
        Base deserializer = NumberDeserializers.findIntLikeValueDeserializer(Number.class, parser.getCurrentName(), parser.getCodec());
        Number value = deserializer.deserialize(parser, null);
        assertEquals(123, value.intValue());
    }

    @Test
    public void testLongSerializer() throws JsonProcessingException {
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser("{ \"num\": 1234567890 }");
        Base deserializer = NumberDeserializers.findLongValueDeserializer(Long.class, parser.getCurrentName(), parser.getCodec());
        Long value = deserializer.deserialize(parser, null);
        assertEquals(1234567890L, value);
    }

    @Test
    public void testFloatSerializer() throws JsonProcessingException {
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser("{ \"num\": 123.45 }");
        Base deserializer = NumberSerializers.FloatSerializer.instance;
        Float value = deserializer.deserialize(parser, null);
        assertEquals(123.45f, value);
    }

    @Test
    public void testDoubleSerializer() throws JsonProcessingException {
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser("{ \"num\": 123.45 }");
        Base deserializer = NumberSerializers.DoubleSerializer.instance;
        Double value = deserializer.deserialize(parser, null);
        assertEquals(123.45, value);
    }

    @Test
    public void testSerializeNumber() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Short num = (short) 123;
        JsonNode root = mapper.writeValueAsTree(num);
        JsonNode numberNode = root.path("num");
        assertEquals(JsonNumberNode.class, numberNode.getClass());
        assertEquals(123, numberNode.asInt());

        Integer intNum = 123;
        root = mapper.writeValueAsTree(intNum);
        numberNode = root.path("num");
        assertEquals(JsonNumberNode.class, numberNode.getClass());
        assertEquals(123, numberNode.asInt());

        Number numObj = 123;
        root = mapper.writeValueAsTree(numObj);
        numberNode = root.path("num");
        assertEquals(JsonNumberNode.class, numberNode.getClass());
        assertEquals(123, numberNode.asInt());

        Long longNum = 1234567890L;
        root = mapper.writeValueAsTree(longNum);
        numberNode = root.path("num");
        assertEquals(JsonNumberNode.class, numberNode.getClass());
        assertEquals(1234567890, numberNode.asLong());

        Float floatNum = 123.45f;
        root = mapper.writeValueAsTree(floatNum);
        numberNode = root.path("num");
        assertEquals(JsonNumberNode.class, numberNode.getClass());
        assertEquals(123.45f, floatNum);

        Double doubleNum = 123.45;
        root = mapper.writeValueAsTree(doubleNum);
        numberNode = root.path("num");
        assertEquals(JsonNumberNode.class, numberNode.getClass());
        assertEquals(123.45, doubleNum);
    }

    @Test
    public void testSerializeNumberWithType() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Short num = (short) 123;
        JsonNode root = mapper.writeValueAsTree(num);
        JsonNode numberNode = root.path("num");
        assertEquals(JsonNumberNode.class, numberNode.getClass());
        assertEquals("number", numberNode.getNodeType().getJavaClass().getSimpleName());

        Integer intNum = 123;
        root = mapper.writeValueAsTree(intNum);
        numberNode = root.path("num");
        assertEquals(JsonNumberNode.class, numberNode.getClass());
        assertEquals("number", numberNode.getNodeType().getJavaClass().getSimpleName());

        Number numObj = 123;
        root = mapper.writeValueAsTree(numObj);
        numberNode = root.path("num");
        assertEquals(JsonNumberNode.class, numberNode.getClass());
        assertEquals("number", numberNode.getNodeType().getJavaClass().getSimpleName());

        Long longNum = 1234567890L;
        root = mapper.writeValueAsTree(longNum);
        numberNode = root.path("num");
        assertEquals(JsonNumberNode.class, numberNode.getClass());
        assertEquals("number", numberNode.getNodeType().getJavaClass().getSimpleName());

        Float floatNum = 123.45f;
        root = mapper.writeValueAsTree(floatNum);
        numberNode = root.path("num");
        assertEquals(JsonNumberNode.class, numberNode.getClass());
        assertEquals("number", numberNode.getNodeType().getJavaClass().getSimpleName());

        Double doubleNum = 123.45;
        root = mapper.writeValueAsTree(doubleNum);
        numberNode = root.path("num");
        assertEquals(JsonNumberNode.class, numberNode.getClass());
        assertEquals("number", numberNode.getNodeType().getJavaClass().getSimpleName());
    }

    @Test
    public void testNull() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Short num = null;
        JsonNode root = mapper.writeValueAsTree(num);
        JsonNode numberNode = root.path("num");
        assertNull(numberNode);

        Integer intNum = null;
        root = mapper.writeValueAsTree(intNum);
        numberNode = root.path("num");
        assertNull(numberNode);

        Number numObj = null;
        root = mapper.writeValueAsTree(numObj);
        numberNode = root.path("num");
        assertNull(numberNode);

        Long longNum = null;
        root = mapper.writeValueAsTree(longNum);
        numberNode = root.path("num");
        assertNull(numberNode);

        Float floatNum = null;
        root = mapper.writeValueAsTree(floatNum);
        numberNode = root.path("num");
        assertNull(numberNode);

        Double doubleNum = null;
        root = mapper.writeValueAsTree(doubleNum);
        numberNode = root.path("num");
        assertNull(numberNode);
    }
}