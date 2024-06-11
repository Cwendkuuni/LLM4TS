package com.fasterxml.jackson.databind.ser.std;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class NumberSerializersTest {

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
        NumberSerializers.addAll(allDeserializers);

        Assert.assertEquals(11, allDeserializers.size());
        Assert.assertTrue(allDeserializers.containsKey(Integer.class.getName()));
        Assert.assertTrue(allDeserializers.containsKey(Integer.TYPE.getName()));
        Assert.assertTrue(allDeserializers.containsKey(Long.class.getName()));
        Assert.assertTrue(allDeserializers.containsKey(Long.TYPE.getName()));
        Assert.assertTrue(allDeserializers.containsKey(Byte.class.getName()));
        Assert.assertTrue(allDeserializers.containsKey(Byte.TYPE.getName()));
        Assert.assertTrue(allDeserializers.containsKey(Short.class.getName()));
        Assert.assertTrue(allDeserializers.containsKey(Short.TYPE.getName()));
        Assert.assertTrue(allDeserializers.containsKey(Double.class.getName()));
        Assert.assertTrue(allDeserializers.containsKey(Double.TYPE.getName()));
        Assert.assertTrue(allDeserializers.containsKey(Float.class.getName()));
        Assert.assertTrue(allDeserializers.containsKey(Float.TYPE.getName()));
    }

    @Test
    public void testShortSerializerSerialize() throws IOException {
        NumberSerializers.ShortSerializer serializer = new NumberSerializers.ShortSerializer();
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        JsonNode jsonNode = new ObjectMapper().createObjectNode();

        serializer.serialize((short) 100, new ObjectMapper().getJsonFactory().createGenerator(System.out), provider);
        Assert.assertEquals(jsonNode, serializer.getSchema(provider, null));
    }

    @Test
    public void testShortSerializer() throws IOException {
        NumberSerializers.ShortSerializer serializer = new NumberSerializers.ShortSerializer();

        Assert.assertEquals("number", serializer._schemaType);
        Assert.assertEquals(JsonParser.NumberType.INT, serializer._numberType);
        Assert.assertTrue(serializer._isInt);
    }

    @Test
    public void testIntegerSerializerSerialize() throws IOException {
        NumberSerializers.IntegerSerializer serializer = new NumberSerializers.IntegerSerializer(Integer.class);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        JsonNode jsonNode = new ObjectMapper().createObjectNode();

        serializer.serialize(100, new ObjectMapper().getJsonFactory().createGenerator(System.out), provider);
        Assert.assertEquals(jsonNode, serializer.getSchema(provider, null));

        serializer.serializeWithType(200, new ObjectMapper().getJsonFactory().createGenerator(System.out), provider, null);
    }

    @Test
    public void testIntegerSerializer() throws IOException {
        NumberSerializers.IntegerSerializer serializer = new NumberSerializers.IntegerSerializer(Integer.class);

        Assert.assertEquals("integer", serializer._schemaType);
        Assert.assertEquals(JsonParser.NumberType.INT, serializer._numberType);
        Assert.assertTrue(serializer._isInt);
    }

    @Test
    public void testIntLikeSerializerSerialize() throws IOException {
        NumberSerializers.IntLikeSerializer serializer = new NumberSerializers.IntLikeSerializer();
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        JsonNode jsonNode = new ObjectMapper().createObjectNode();

        serializer.serialize(100, new ObjectMapper().getJsonFactory().createGenerator(System.out), provider);
        Assert.assertEquals(jsonNode, serializer.getSchema(provider, null));
    }

    @Test
    public void testIntLikeSerializer() throws IOException {
        NumberSerializers.IntLikeSerializer serializer = new NumberSerializers.IntLikeSerializer();

        Assert.assertEquals("integer", serializer._schemaType);
        Assert.assertEquals(JsonParser.NumberType.INT, serializer._numberType);
        Assert.assertTrue(serializer._isInt);
    }

    @Test
    public void testLongSerializerSerialize() throws IOException {
        NumberSerializers.LongSerializer serializer = new NumberSerializers.LongSerializer(Long.class);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        JsonNode jsonNode = new ObjectMapper().createObjectNode();

        serializer.serialize(100L, new ObjectMapper().getJsonFactory().createGenerator(System.out), provider);
        Assert.assertEquals(jsonNode, serializer.getSchema(provider, null));
    }

    @Test
    public void testLongSerializer() throws IOException {
        NumberSerializers.LongSerializer serializer = new NumberSerializers.LongSerializer(Long.class);

        Assert.assertEquals("number", serializer._schemaType);
        Assert.assertEquals(JsonParser.NumberType.LONG, serializer._numberType);
        Assert.assertTrue(serializer._isInt);
    }

    @Test
    public void testFloatSerializerSerialize() throws IOException {
        NumberSerializers.FloatSerializer serializer = new NumberSerializers.FloatSerializer();
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        JsonNode jsonNode = new ObjectMapper().createObjectNode();

        serializer.serialize(100f, new ObjectMapper().getJsonFactory().createGenerator(System.out), provider);
        Assert.assertEquals(jsonNode, serializer.getSchema(provider, null));
    }

    @Test
    public void testFloatSerializer() throws IOException {
        NumberSerializers.FloatSerializer serializer = new NumberSerializers.FloatSerializer();

        Assert.assertEquals("number", serializer._schemaType);
        Assert.assertEquals(JsonParser.NumberType.FLOAT, serializer._numberType);
        Assert.assertFalse(serializer._isInt);
    }

    @Test
    public void testDoubleSerializerSerialize() throws IOException {
        NumberSerializers.DoubleSerializer serializer = new NumberSerializers.DoubleSerializer(Double.class);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        JsonNode jsonNode = new ObjectMapper().createObjectNode();

        serializer.serialize(100.0, new ObjectMapper().getJsonFactory().createGenerator(System.out), provider);
        Assert.assertEquals(jsonNode, serializer.getSchema(provider, null));

        serializer.serializeWithType(200.0, new ObjectMapper().getJsonFactory().createGenerator(System.out), provider, null);
    }

    @Test
    public void testDoubleSerializer() throws IOException {
        NumberSerializers.DoubleSerializer serializer = new NumberSerializers.DoubleSerializer(Double.class);

        Assert.assertEquals("number", serializer._schemaType);
        Assert.assertEquals(JsonParser.NumberType.DOUBLE, serializer._numberType);
        Assert.assertFalse(serializer._isInt);
    }
}