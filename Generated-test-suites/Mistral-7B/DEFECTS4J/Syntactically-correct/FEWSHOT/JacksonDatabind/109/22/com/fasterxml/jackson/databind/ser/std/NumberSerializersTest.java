package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser.NumberType;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.*;

import org.junit.Test;

public class NumberSerializersTest {

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new java.util.HashMap<>();
        NumberSerializers.addAll(allDeserializers);

        assertEquals(7, allDeserializers.size());
        assertTrue(allDeserializers.containsKey(Integer.class.getName()));
        assertTrue(allDeserializers.containsKey(Integer.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Long.class.getName()));
        assertTrue(allDeserializers.containsKey(Long.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Byte.class.getName()));
        assertTrue(allDeserializers.containsKey(Byte.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Short.class.getName()));
        assertTrue(allDeserializers.containsKey(Short.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Double.class.getName()));
        assertTrue(allDeserializers.containsKey(Double.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Float.class.getName()));
        assertTrue(allDeserializers.containsKey(Float.TYPE.getName()));
    }

    @Test
    public void testShortSerializer() throws JsonProcessingException {
        JsonSerializer<Short> shortSerializer = NumberSerializers.ShortSerializer.instance;

        Short testValue = Short.parseShort("123");
        String jsonString = "123";

        JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
        shortSerializer.serialize(testValue, gen, new ObjectMapper().getSerializerProvider());
        assertEquals(jsonString, gen.getOutputContent());

        Short deserializedValue = null;
        JsonParser parser = new JsonFactory().createParser(new StringReader(jsonString));
        deserializedValue = (Short) shortSerializer.deserialize(parser, Short.TYPE);
        assertEquals(testValue, deserializedValue);
    }

    @Test
    public void testIntegerSerializer() throws JsonProcessingException {
        JsonSerializer<Integer> intSerializer = NumberSerializers.IntegerSerializer.instance;

        Integer testValue = Integer.parseInt("123");
        String jsonString = "123";

        JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
        intSerializer.serialize(testValue, gen, new ObjectMapper().getSerializerProvider());
        assertEquals(jsonString, gen.getOutputContent());

        Integer deserializedValue = null;
        JsonParser parser = new JsonFactory().createParser(new StringReader(jsonString));
        deserializedValue = (Integer) intSerializer.deserialize(parser, Integer.TYPE);
        assertEquals(testValue, deserializedValue);
    }

    @Test
    public void testIntLikeSerializer() throws JsonProcessingException {
        JsonSerializer<Number> intLikeSerializer = NumberSerializers.IntLikeSerializer.instance;

        Number testValue = Byte.parseByte("123");
        String jsonString = "123";

        JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
        intLikeSerializer.serialize(testValue, gen, new ObjectMapper().getSerializerProvider());
        assertEquals(jsonString, gen.getOutputContent());

        Number deserializedValue = null;
        JsonParser parser = new JsonFactory().createParser(new StringReader(jsonString));
        deserializedValue = intLikeSerializer.deserialize(parser, Number.class);
        assertEquals(testValue, deserializedValue);
    }

    @Test
    public void testLongSerializer() throws JsonProcessingException {
        JsonSerializer<Long> longSerializer = NumberSerializers.LongSerializer.instance;

        Long testValue = Long.parseLong("1234567890");
        String jsonString = "1234567890";

        JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
        longSerializer.serialize(testValue, gen, new ObjectMapper().getSerializerProvider());
        assertEquals(jsonString, gen.getOutputContent());

        Long deserializedValue = null;
        JsonParser parser = new JsonFactory().createParser(new StringReader(jsonString));
        deserializedValue = (Long) longSerializer.deserialize(parser, Long.TYPE);
        assertEquals(testValue, deserializedValue);
    }

    @Test
    public void testFloatSerializer() throws JsonProcessingException {
        JsonSerializer<Float> floatSerializer = NumberSerializers.FloatSerializer.instance;

        Float testValue = Float.parseFloat("123.45");
        String jsonString = "123.45";

        JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
        floatSerializer.serialize(testValue, gen, new ObjectMapper().getSerializerProvider());
        assertEquals(jsonString, gen.getOutputContent());

        Float deserializedValue = null;
        JsonParser parser = new JsonFactory().createParser(new StringReader(jsonString));
        deserializedValue = (Float) floatSerializer.deserialize(parser, Float.TYPE);
        assertEquals(testValue, deserializedValue);
    }

    @Test
    public void testDoubleSerializer() throws JsonProcessingException {
        JsonSerializer<Double> doubleSerializer = NumberSerializers.DoubleSerializer.instance;

        Double testValue = Double.parseDouble("123.456");
        String jsonString = "123.456";

        JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
        doubleSerializer.serialize(testValue, gen, new ObjectMapper().getSerializerProvider());
        assertEquals(jsonString, gen.getOutputContent());

        Double deserializedValue = null;
        JsonParser parser = new JsonFactory().createParser(new StringReader(jsonString));
        deserializedValue = (Double) doubleSerializer.deserialize(parser, Double.TYPE);
        assertEquals(testValue, deserializedValue);
    }
}