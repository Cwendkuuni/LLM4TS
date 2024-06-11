package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers;
import com.fasterxml.jackson.databind.util.StdTestDriver;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

public class NumberSerializersTest {

    @SuppressWarnings("unchecked")
    private static final ObjectMapper mapper = new ObjectMapper();

    @org.junit.BeforeClass
    public static void setup() {
        // Initialize all deserializers
        NumberSerializers.addAll(mapper.getTypeFactory().constructMap(String.class, Object.class));
    }

    // Base tests

    @org.junit.Test
    public void testBaseGetSchema() throws JsonMappingException, JsonProcessingException {
        NumberSerializers serializers = new NumberSerializers();
        JsonSerializer<?> serializer = serializers.ShortSerializer.instance;
        JsonNode schema = serializer.getSchema(mapper.getSerializerProvider(), null);
        assertEquals("number", schema.get("type").asText());
    }

    @org.junit.Test
    public void testBaseAcceptJsonFormatVisitor() throws JsonMappingException, JsonProcessingException {
        NumberSerializers serializers = new NumberSerializers();
        JsonSerializer<?> serializer = serializers.ShortSerializer.instance;
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(mapper.getTypeFactory().constructType(Object.class));
        serializer.acceptJsonFormatVisitor(visitor, null);
        assertTrue(visitor.asBoolean(JsonFormat.Feature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT));
    }

    @org.junit.Test
    public void testBaseCreateContextual() throws JsonMappingException, JsonProcessingException {
        NumberSerializers serializers = new NumberSerializers();
        JsonSerializer<?> serializer = serializers.ShortSerializer.instance;
        SerializerProvider provider = mapper.getSerializerProvider();
        BeanProperty property = new BeanProperty(null, "test", null);
        JsonSerializer<?> created = serializer.createContextual(provider, property);
        assertEquals(serializer, created);
    }

    // ShortSerializer tests

    @org.junit.Test
    public void testShortSerializerSerialize() throws JsonMappingException, JsonProcessingException {
        NumberSerializers serializers = new NumberSerializers();
        JsonSerializer<?> serializer = serializers.ShortSerializer.instance;
        Short value = Short.MIN_VALUE;
        String result = mapper.writeValueAsString(value);
        JsonParser parser = mapper.getFactory().createParser(result);
        Short deserialized = mapper.readValue(parser, Short.class);
        assertEquals(value, deserialized);
    }

    @org.junit.Test
    public void testShortSerializerSerializeWithType() throws JsonMappingException, JsonProcessingException {
        NumberSerializers serializers = new NumberSerializers();
        JsonSerializer<?> serializer = serializers.ShortSerializer.instance;
        Short value = Short.MIN_VALUE;
        String result = mapper.writerForType(Short.class).writeValueAsString(value);
        JsonParser parser = mapper.getFactory().createParser(result);
        Object deserialized = mapper.readValue(parser, Map.class).get("@type");
        assertEquals(Short.class, deserialized);
    }

    @org.junit.Test(expected = JsonMappingException.class)
    public void testShortSerializerSerializeInvalidValue() throws JsonMappingException, JsonProcessingException {
        NumberSerializers serializers = new NumberSerializers();
        JsonSerializer<?> serializer = serializers.ShortSerializer.instance;
        Short value = Short.MAX_VALUE + 1;
        serializer.serialize(value, mapper.getSerializerProvider().getEmptyGenerator(), mapper.getSerializerProvider());
    }

    // IntegerSerializer tests

    @org.junit.Test
    public void testIntegerSerializerSerialize() throws JsonMappingException, JsonProcessingException {
        NumberSerializers serializers = new NumberSerializers();
        JsonSerializer<?> serializer = serializers.IntegerSerializer.instance;
        Integer value = Integer.MIN_VALUE;
        String result = mapper.writeValueAsString(value);
        JsonParser parser = mapper.getFactory().createParser(result);
        Integer deserialized = mapper.readValue(parser, Integer.class);
        assertEquals(value, deserialized);
    }

    @org.junit.Test
    public void testIntegerSerializerSerializeWithType() throws JsonMappingException, JsonProcessingException {
        NumberSerializers serializers = new NumberSerializers();
        JsonSerializer<?> serializer = serializers.IntegerSerializer.instance;
        Integer value = Integer.MIN_VALUE;
        String result = mapper.writerForType(Integer.class).writeValueAsString(value);
        JsonParser parser = mapper.getFactory().createParser(result);
        Object deserialized = mapper.readValue(parser, Map.class).get("@type");
        assertEquals(Integer.class, deserialized);
    }

    @org.junit.Test(expected = JsonMappingException.class)
    public void testIntegerSerializerSerializeInvalidValue() throws JsonMappingException, JsonProcessingException {
        NumberSerializers serializers = new NumberSerializers();
        JsonSerializer<?> serializer = serializers.IntegerSerializer.instance;
        Integer value = Integer.MAX_VALUE + 1;
        serializer.serialize(value, mapper.getSerializerProvider().getEmptyGenerator(), mapper.getSerializerProvider());
    }

    // IntLikeSerializer tests

    @org.junit.Test
    public void testIntLikeSerializerSerialize() throws JsonMappingException, JsonProcessingException {
        NumberSerializers serializers = new NumberSerializers();
        JsonSerializer<?> serializer = serializers.IntLikeSerializer.instance;
        Byte value = Byte.MIN_VALUE;
        String result = mapper.writeValueAsString(value);
        JsonParser parser = mapper.getFactory().createParser(result);
        Byte deserialized = mapper.readValue(parser, Byte.class);
        assertEquals(value, deserialized);
    }

    @org.junit.Test
    public void testIntLikeSerializerSerializeWithType() throws JsonMappingException, JsonProcessingException {
        NumberSerializers serializers = new NumberSerializers();
        JsonSerializer<?> serializer = serializers.IntLikeSerializer.instance;
        Byte value = Byte.MIN_VALUE;
        String result = mapper.writerForType(Byte.class).writeValueAsString(value);
        JsonParser parser = mapper.getFactory().createParser(result);
        Object deserialized = mapper.readValue(parser, Map.class).get("@type");
        assertEquals(Byte.class, deserialized);
    }

    @org.junit.Test(expected = JsonMappingException.class)
    public void testIntLikeSerializerSerializeInvalidValue() throws JsonMappingException, JsonProcessingException {
        NumberSerializers serializers = new NumberSerializers();
        JsonSerializer<?> serializer = serializers.IntLikeSerializer.instance;
        Byte value = Byte.MAX_VALUE + 1;
        serializer.serialize(value, mapper.getSerializerProvider().getEmptyGenerator(), mapper.getSerializerProvider());
    }

    // LongSerializer tests

    @org.junit.Test
    public void testLongSerializerSerialize() throws JsonMappingException, JsonProcessingException {
        NumberSerializers serializers = new NumberSerializers();
        JsonSerializer<?> serializer = serializers.LongSerializer.instance;
        Long value = Long.MIN_VALUE;
        String result = mapper.writeValueAsString(value);
        JsonParser parser = mapper.getFactory().createParser(result);
        Long deserialized = mapper.readValue(parser, Long.class);
        assertEquals(value, deserialized);
    }

    @org.junit.Test
    public void testLongSerializerSerializeWithType() throws JsonMappingException, JsonProcessingException {
        NumberSerializers serializers = new NumberSerializers();
        JsonSerializer<?> serializer = serializers.LongSerializer.instance;
        Long value = Long.MIN_VALUE;
        String result = mapper.writerForType(Long.class).writeValueAsString(value);
        JsonParser parser = mapper.getFactory().createParser(result);
        Object deserialized = mapper.readValue(parser, Map.class).get("@type");
        assertEquals(Long.class, deserialized);
    }

    @org.junit.Test(expected = JsonMappingException.class)
    public void testLongSerializerSerializeInvalidValue() throws JsonMappingException, JsonProcessingException {
        NumberSerializers serializers = new NumberSerializers();
        JsonSerializer<?> serializer = serializers.LongSerializer.instance;
        Long value = Long.MAX_VALUE + 1;
        serializer.serialize(value, mapper.getSerializerProvider().getEmptyGenerator(), mapper.getSerializerProvider());
    }

    // FloatSerializer tests

    @org.junit.Test
    public void testFloatSerializerSerialize() throws JsonMappingException, JsonProcessingException {
        NumberSerializers serializers = new NumberSerializers();
        JsonSerializer<?> serializer = serializers.FloatSerializer.instance;
        Float value = Float.MIN_VALUE;
        String result = mapper.writeValueAsString(value);
        JsonParser parser = mapper.getFactory().createParser(result);
        Float deserialized = mapper.readValue(parser, Float.class);
        assertEquals(value, deserialized);
    }

    @org.junit.Test
    public void testFloatSerializerSerializeWithType() throws JsonMappingException, JsonProcessingException {
        NumberSerializers serializers = new NumberSerializers();
        JsonSerializer<?> serializer = serializers.FloatSerializer.instance;
        Float value = Float.MIN_VALUE;
        String result = mapper.writerForType(Float.class).writeValueAsString(value);
        JsonParser parser = mapper.getFactory().createParser(result);
        Object deserialized = mapper.readValue(parser, Map.class).get("@type");
        assertEquals(Float.class, deserialized);
    }

    @org.junit.Test(expected = JsonMappingException.class)
    public void testFloatSerializerSerializeInvalidValue() throws JsonMappingException, JsonProcessingException {
        NumberSerializers serializers = new NumberSerializers();
        JsonSerializer<?> serializer = serializers.FloatSerializer.instance;
        Float value = Float.MAX_VALUE + 1f;
        serializer.serialize(value, mapper.getSerializerProvider().getEmptyGenerator(), mapper.getSerializerProvider());
    }

    // DoubleSerializer tests

    @org.junit.Test
    public void testDoubleSerializerSerialize() throws JsonMappingException, JsonProcessingException {
        NumberSerializers serializers = new NumberSerializers();
        JsonSerializer<?> serializer = serializers.DoubleSerializer.instance;
        Double value = Double.MIN_VALUE;
        String result = mapper.writeValueAsString(value);
        JsonParser parser = mapper.getFactory().createParser(result);
        Double deserialized = mapper.readValue(parser, Double.class);
        assertEquals(value, deserialized);
    }

    @org.junit.Test
    public void testDoubleSerializerSerializeWithType() throws JsonMappingException, JsonProcessingException {
        NumberSerializers serializers = new NumberSerializers();
        JsonSerializer<?> serializer = serializers.DoubleSerializer.instance;
        Double value = Double.MIN_VALUE;
        String result = mapper.writerForType(Double.class).writeValueAsString(value);
        JsonParser parser = mapper.getFactory().createParser(result);
        Object deserialized = mapper.readValue(parser, Map.class).get("@type");
        assertEquals(Double.class, deserialized);
    }

    @org.junit.Test(expected = JsonMappingException.class)
    public void testDoubleSerializerSerializeInvalidValue() throws JsonMappingException, JsonProcessingException {
        NumberSerializers serializers = new NumberSerializers();
        JsonSerializer<?> serializer = serializers.DoubleSerializer.instance;
        Double value = Double.MAX_VALUE + 1;
        serializer.serialize(value, mapper.getSerializerProvider().getEmptyGenerator(), mapper.getSerializerProvider());
    }
}