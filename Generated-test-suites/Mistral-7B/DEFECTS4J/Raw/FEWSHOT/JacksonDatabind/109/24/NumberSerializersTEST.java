package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

public class NumberSerializersTest {

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new java.util.HashMap<>();
        NumberSerializers.addAll(allDeserializers);
        assertNotNull(allDeserializers.get(Integer.class.getName()));
        assertNotNull(allDeserializers.get(Integer.TYPE.getName()));
        assertNotNull(allDeserializers.get(Long.class.getName()));
        assertNotNull(allDeserializers.get(Long.TYPE.getName()));
        assertNotNull(allDeserializers.get(Byte.class.getName()));
        assertNotNull(allDeserializers.get(Byte.TYPE.getName()));
        assertNotNull(allDeserializers.get(Short.class.getName()));
        assertNotNull(allDeserializers.get(Short.TYPE.getName()));
        assertNotNull(allDeserializers.get(Double.class.getName()));
        assertNotNull(allDeserializers.get(Double.TYPE.getName()));
        assertNotNull(allDeserializers.get(Float.class.getName()));
        assertNotNull(allDeserializers.get(Float.TYPE.getName()));
    }

    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    static class TestObject {
        private Integer intValue;
        private Long longValue;
        private Float floatValue;
        private Double doubleValue;

        public Integer getIntValue() {
            return intValue;
        }

        public void setIntValue(Integer intValue) {
            this.intValue = intValue;
        }

        public Long getLongValue() {
            return longValue;
        }

        public void setLongValue(Long longValue) {
            this.longValue = longValue;
        }

        public Float getFloatValue() {
            return floatValue;
        }

        public void setFloatValue(Float floatValue) {
            this.floatValue = floatValue;
        }

        public Double getDoubleValue() {
            return doubleValue;
        }

        public void setDoubleValue(Double doubleValue) {
            this.doubleValue = doubleValue;
        }
    }

    @Test
    public void testSerializeInteger() throws JsonProcessingException {
        JsonSerializer<Integer> integerSerializer = NumberSerializers.findValueSerializer(Integer.class, null);
        TestObject testObject = new TestObject();
        testObject.setIntValue(123);

        ObjectWriter objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();
        String json = objectWriter.writeValueAsString(testObject);
        JsonParser jsonParser = new ObjectMapper().getFactory().createParser(json);
        JsonNode rootNode = jsonParser.readValueAsTree();
        JsonNode intNode = rootNode.get("intValue");

        assertEquals(123, intNode.asInt());
    }

    @Test
    public void testSerializeLong() throws JsonProcessingException {
        JsonSerializer<Long> longSerializer = NumberSerializers.findValueSerializer(Long.class, null);
        TestObject testObject = new TestObject();
        testObject.setLongValue(1234567890L);

        ObjectWriter objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();
        String json = objectWriter.writeValueAsString(testObject);
        JsonParser jsonParser = new ObjectMapper().getFactory().createParser(json);
        JsonNode rootNode = jsonParser.readValueAsTree();
        JsonNode longNode = rootNode.get("longValue");

        assertEquals(1234567890, longNode.asLong());
    }

    @Test
    public void testSerializeFloat() throws JsonProcessingException {
        JsonSerializer<Float> floatSerializer = NumberSerializers.findValueSerializer(Float.class, null);
        TestObject testObject = new TestObject();
        testObject.setFloatValue(123.45f);

        ObjectWriter objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();
        String json = objectWriter.writeValueAsString(testObject);
        JsonParser jsonParser = new ObjectMapper().getFactory().createParser(json);
        JsonNode rootNode = jsonParser.readValueAsTree();
        JsonNode floatNode = rootNode.get("floatValue");

        assertEquals(123.45f, floatNode.numberValue().floatValue(), 0.001);
    }

    @Test
    public void testSerializeDouble() throws JsonProcessingException {
        JsonSerializer<Double> doubleSerializer = NumberSerializers.findValueSerializer(Double.class, null);
        TestObject testObject = new TestObject();
        testObject.setDoubleValue(123.456789);

        ObjectWriter objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();
        String json = objectWriter.writeValueAsString(testObject);
        JsonParser jsonParser = new ObjectMapper().getFactory().createParser(json);
        JsonNode rootNode = jsonParser.readValueAsTree();
        JsonNode doubleNode = rootNode.get("doubleValue");

        assertEquals(123.456789, doubleNode.numberValue().doubleValue(), 0.001);
    }

    @Test
    public void testSerializeShort() throws JsonProcessingException {
        JsonSerializer<Short> shortSerializer = NumberSerializers.findValueSerializer(Short.class, null);
        TestObject testObject = new TestObject();
        testObject.setIntValue((short) 123);

        ObjectWriter objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();
        String json = objectWriter.writeValueAsString(testObject);
        JsonParser jsonParser = new ObjectMapper().getFactory().createParser(json);
        JsonNode rootNode = jsonParser.readValueAsTree();
        JsonNode shortNode = rootNode.get("intValue");

        assertEquals((short) 123, shortNode.asInt());
    }

    @Test
    public void testSerializeByte() throws JsonProcessingException {
        JsonSerializer<Byte> byteSerializer = NumberSerializers.findValueSerializer(Byte.class, null);
        TestObject testObject = new TestObject();
        testObject.setIntValue((byte) 123);

        ObjectWriter objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();
        String json = objectWriter.writeValueAsString(testObject);
        JsonParser jsonParser = new ObjectMapper().getFactory().createParser(json);
        JsonNode rootNode = jsonParser.readValueAsTree();
        JsonNode byteNode = rootNode.get("intValue");

        assertEquals((byte) 123, byteNode.asInt());
    }

    @Test
    public void testSerializeNull() throws JsonProcessingException {
        TestObject testObject = new TestObject();
        ObjectWriter objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();
        String json = objectWriter.writeValueAsString(testObject);
        JsonParser jsonParser = new ObjectMapper().getFactory().createParser(json);
        JsonNode intNode = jsonParser.readValueAsTree().get("intValue");
        JsonNode longNode = jsonParser.readValueAsTree().get("longValue");
        JsonNode floatNode = jsonParser.readValueAsTree().get("floatValue");
        JsonNode doubleNode = jsonParser.readValueAsTree().get("doubleValue");

        assertNull(intNode);
        assertNull(longNode);
        assertNull(floatNode);
        assertNull(doubleNode);
    }
}