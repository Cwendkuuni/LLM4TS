package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.*;
import com.fasterxml.jackson.databind.ser.std.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

public class NumberSerializersTest {

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new java.util.HashMap<>();
        NumberSerializers.addAll(allDeserializers);

        assertEquals(6, allDeserializers.size());
        assertTrue(allDeserializers.containsKey(Integer.class.getName()));
        assertTrue(allDeserializers.containsKey(Integer.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Long.class.getName()));
        assertTrue(allDeserializers.containsKey(Long.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Byte.class.getName()));
        assertTrue(allDeserializers.containsKey(Byte.TYPE.getName()));
    }

    @Test
    public void testIntegerSerializer() throws Exception {
        JsonSerializer<Object> serializer = NumberSerializers.findValueSerializer(Object.class, new ObjectMapper());
        JsonSerializer<Integer> intSerializer = NumberSerializers.findValueSerializer(Integer.class, new ObjectMapper());

        ObjectWriter writer = new ObjectMapper().writerForArrayOfLength(1);
        JsonNode rootNode = writer.writeValueAsTree(new Integer(123));
        JsonNode node = rootNode.elements().next();

        assertEquals(JsonParser.NumberType.INT, node.getNodeType());
        assertEquals("123", node.asText());

        JsonParser parser = new ObjectMapper().getFactory().createParser(rootNode.toString());
        assertEquals(123, parser.readValueAs(Integer.class));

        JsonGenerator gen = new ObjectMapper().getFactory().createGenerator(new ByteArrayOutputStream(), JsonMapping.StdSerializers.FIND_VALUE);
        intSerializer.serialize(new Integer(456), gen, new ObjectMapper().getSerializerProvider());
        assertEquals("456", gen.getOutputContent().toString());
    }

    @Test
    public void testShortSerializer() throws Exception {
        JsonSerializer<Object> serializer = NumberSerializers.findValueSerializer(Object.class, new ObjectMapper());
        JsonSerializer<Short> shortSerializer = NumberSerializers.findValueSerializer(Short.class, new ObjectMapper());

        ObjectWriter writer = new ObjectMapper().writerForArrayOfLength(1);
        JsonNode rootNode = writer.writeValueAsTree((short) 789);
        JsonNode node = rootNode.elements().next();

        assertEquals(JsonParser.NumberType.INT, node.getNodeType());
        assertEquals("789", node.asText());

        JsonParser parser = new ObjectMapper().getFactory().createParser(rootNode.toString());
        assertEquals((short) 789, parser.readValueAs(Short.class));

        JsonGenerator gen = new ObjectMapper().getFactory().createGenerator(new ByteArrayOutputStream(), JsonMapping.StdSerializers.FIND_VALUE);
        shortSerializer.serialize((short) 1024, gen, new ObjectMapper().getSerializerProvider());
        assertEquals("1024", gen.getOutputContent().toString());
    }

    @Test
    public void testIntLikeSerializer() throws Exception {
        JsonSerializer<Object> serializer = NumberSerializers.findValueSerializer(Object.class, new ObjectMapper());
        JsonSerializer<Number> intLikeSerializer = NumberSerializers.findValueSerializer(Number.class, new ObjectMapper());

        ObjectWriter writer = new ObjectMapper().writerForArrayOfLength(1);
        JsonNode rootNode = writer.writeValueAsTree(12345);
        JsonNode node = rootNode.elements().next();

        assertEquals(JsonParser.NumberType.INT, node.getNodeType());
        assertEquals("12345", node.asText());

        JsonParser parser = new ObjectMapper().getFactory().createParser(rootNode.toString());
        assertEquals(12345, parser.readValueAs(Integer.class));

        JsonGenerator gen = new ObjectMapper().getFactory().createGenerator(new ByteArrayOutputStream(), JsonMapping.StdSerializers.FIND_VALUE);
        intLikeSerializer.serialize(12345, gen, new ObjectMapper().getSerializerProvider());
        assertEquals("12345", gen.getOutputContent().toString());
    }

    @Test
    public void testLongSerializer() throws Exception {
        JsonSerializer<Object> serializer = NumberSerializers.findValueSerializer(Object.class, new ObjectMapper());
        JsonSerializer<Long> longSerializer = NumberSerializers.findValueSerializer(Long.class, new ObjectMapper());

        ObjectWriter writer = new ObjectMapper().writerForArrayOfLength(1);
        JsonNode rootNode = writer.writeValueAsTree(new Long(9876543210L));
        JsonNode node = rootNode.elements().next();

        assertEquals(JsonParser.NumberType.LONG, node.getNodeType());
        assertEquals("9876543210", node.asText());

        JsonParser parser = new ObjectMapper().getFactory().createParser(rootNode.toString());
        assertEquals(9876543210L, parser.readValueAs(Long.class));

        JsonGenerator gen = new ObjectMapper().getFactory().createGenerator(new ByteArrayOutputStream(), JsonMapping.StdSerializers.FIND_VALUE);
        longSerializer.serialize(new Long(123456789012345L), gen, new ObjectMapper().getSerializerProvider());
        assertEquals("123456789012345", gen.getOutputContent().toString());
    }

    @Test
    public void testFloatSerializer() throws Exception {
        JsonSerializer<Object> serializer = NumberSerializers.findValueSerializer(Object.class, new ObjectMapper());
        JsonSerializer<Float> floatSerializer = NumberSerializers.findValueSerializer(Float.class, new ObjectMapper());

        ObjectWriter writer = new ObjectMapper().writerForArrayOfLength(1);
        JsonNode rootNode = writer.writeValueAsTree((float) 1.23456f);
        JsonNode node = rootNode.elements().next();

        assertEquals(JsonParser.NumberType.FLOAT, node.getNodeType());
        assertEquals("1.23456", node.asText());

        JsonParser parser = new ObjectMapper().getFactory().createParser(rootNode.toString());
        assertEquals((float) 1.23456f, parser.readValueAs(Float.class));

        JsonGenerator gen = new ObjectMapper().getFactory().createGenerator(new ByteArrayOutputStream(), JsonMapping.StdSerializers.FIND_VALUE);
        floatSerializer.serialize(1.23456f, gen, new ObjectMapper().getSerializerProvider());
        assertEquals("1.23456", gen.getOutputContent().toString());
    }

    @Test
    public void testDoubleSerializer() throws Exception {
        JsonSerializer<Object> serializer = NumberSerializers.findValueSerializer(Object.class, new ObjectMapper());
        JsonSerializer<Double> doubleSerializer = NumberSerializers.findValueSerializer(Double.class, new ObjectMapper());

        ObjectWriter writer = new ObjectMapper().writerForArrayOfLength(1);
        JsonNode rootNode = writer.writeValueAsTree(new Double(1.23456789));
        JsonNode node = rootNode.elements().next();

        assertEquals(JsonParser.NumberType.DOUBLE, node.getNodeType());
        assertEquals("1.23456789", node.asText());

        JsonParser parser = new ObjectMapper().getFactory().createParser(rootNode.toString());
        assertEquals(1.23456789, parser.readValueAs(Double.class));

        JsonGenerator gen = new ObjectMapper().getFactory().createGenerator(new ByteArrayOutputStream(), JsonMapping.StdSerializers.FIND_VALUE);
        doubleSerializer.serialize(1.23456789, gen, new ObjectMapper().getSerializerProvider());
        assertEquals("1.23456789", gen.getOutputContent().toString());
    }
}