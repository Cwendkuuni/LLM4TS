package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.LongNode;
import com.fasterxml.jackson.databind.node.NumberNode;
import com.fasterxml.jackson.databind.node.TextNode;

public class NumberSerializersTest {

    @Test
    public void testSerializeDeserializeForShortType() throws JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        ShortSerializer serializer = NumberSerializers.ShortSerializer.instance;

        // Serialize and deserialize
        NumberNode shortNode = mapper.valueToTree(Short.MIN_VALUE);
        Short deserializedShort = serializer.deserialize(shortNode.asInt(), null);
        assertEquals(Short.MIN_VALUE, deserializedShort);

        NumberNode shortNode2 = mapper.valueToTree(Short.MAX_VALUE);
        Short deserializedShort2 = serializer.deserialize(shortNode2.asInt(), null);
        assertEquals(Short.MAX_VALUE, deserializedShort2);
    }

    @Test
    public void testSerializeDeserializeForIntegerType() throws JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        IntegerSerializer serializer = NumberSerializers.IntegerSerializer.instance;

        // Serialize and deserialize
        NumberNode intNode = mapper.valueToTree(Integer.MIN_VALUE);
        Integer deserializedInt = serializer.deserialize(intNode.asInt(), null);
        assertEquals(Integer.MIN_VALUE, deserializedInt);

        NumberNode intNode2 = mapper.valueToTree(Integer.MAX_VALUE);
        Integer deserializedInt2 = serializer.deserialize(intNode2.asInt(), null);
        assertEquals(Integer.MAX_VALUE, deserializedInt2);
    }

    @Test
    public void testSerializeDeserializeForLongType() throws JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        LongSerializer serializer = NumberSerializers.LongSerializer.instance;

        // Serialize and deserialize
        NumberNode longNode = mapper.valueToTree(Long.MIN_VALUE);
        Long deserializedLong = serializer.deserialize(longNode.asLongValueExact(), null);
        assertEquals(Long.MIN_VALUE, deserializedLong);

        NumberNode longNode2 = mapper.valueToTree(Long.MAX_VALUE);
        Long deserializedLong2 = serializer.deserialize(longNode2.asLongValueExact(), null);
        assertEquals(Long.MAX_VALUE, deserializedLong2);
    }

    @Test
    public void testSerializeDeserializeForFloatType() throws JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        FloatSerializer serializer = NumberSerializers.FloatSerializer.instance;

        // Serialize and deserialize
        TextNode floatTextNode = mapper.valueToTree(Float.NaN);
        Float deserializedFloat = serializer.deserialize(floatTextNode.asText(), null);
        assertEquals(Float.NaN, deserializedFloat);

        NumberNode floatNode = mapper.valueToTree(Float.MIN_VALUE);
        Float deserializedFloat2 = serializer.deserialize(floatNode.asFloatValue(), null);
        assertEquals(Float.MIN_VALUE, deserializedFloat2);
    }

    @Test
    public void testSerializeDeserializeForDoubleType() throws JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        DoubleSerializer serializer = NumberSerializers.DoubleSerializer.instance;

        // Serialize and deserialize
        TextNode doubleTextNode = mapper.valueToTree(Double.NaN);
        Double deserializedDouble = serializer.deserialize(doubleTextNode.asText(), null);
        assertEquals(Double.NaN, deserializedDouble);

        NumberNode doubleNode = mapper.valueToTree(Double.MIN_VALUE);
        Double deserializedDouble2 = serializer.deserialize(doubleNode.asDoubleValue(), null);
        assertEquals(Double.MIN_VALUE, deserializedDouble2);
    }
}