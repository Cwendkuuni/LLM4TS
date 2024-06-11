package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import org.junit.Test;

public class NumberSerializersTest {

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new java.util.HashMap<>();
        NumberSerializers.addAll(allDeserializers);
        assertEquals(10, allDeserializers.size());
        assertTrue(allDeserializers.containsKey(Integer.class.getName()));
        assertTrue(allDeserializers.containsKey(Integer.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Long.class.getName()));
        assertTrue(allDeserializers.containsKey(Long.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Byte.class.getName()));
        assertTrue(allDeserializers.containsKey(Byte.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Short.class.getName()));
        assertTrue(allDeserializers.containsKey(Short.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Float.class.getName()));
        assertTrue(allDeserializers.containsKey(Float.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Double.class.getName()));
        assertTrue(allDeserializers.containsKey(Double.TYPE.getName()));
    }

    @Test
    public void testIntegerSerializer() {
        JsonSerializer<Integer> intSerializer = NumberSerializers.IntegerSerializer.instance;
        assertEquals(Integer.class, intSerializer.getParameterizedType(null).getComponentType());
        assertEquals(JsonParser.NumberType.INT, intSerializer._numberType);
        assertEquals("integer", intSerializer._schemaType);
        assertTrue(intSerializer instanceof ContextualSerializer);
    }

    @Test
    public void testIntegerSerializerSerialize() throws JsonProcessingException {
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new JsonMapping.DefaultSerializerProvider());
        IntegerSerializer intSerializer = NumberSerializers.IntegerSerializer.instance;
        intSerializer.serialize(1, gen, new JsonSerializer.DefaultSerializers.BaseSerializerProvider());
        String result = gen.getOutputContent();
        assertEquals("1", result);
    }

    @Test
    public void testIntegerSerializerSerializeWithType() throws JsonProcessingException {
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new JsonMapping.DefaultSerializerProvider());
        IntegerSerializer intSerializer = NumberSerializers.IntegerSerializer.instance;
        intSerializer.serializeWithType(1, gen, new JsonSerializer.DefaultSerializers.BaseSerializerProvider(), new TypeSerializer());
        String result = gen.getOutputContent();
        assertEquals("1", result);
    }

    @Test
    public void testIntLikeSerializer() {
        JsonSerializer<Number> intLikeSerializer = NumberSerializers.IntLikeSerializer.instance;
        assertEquals(Number.class, intLikeSerializer.getParameterizedType(null).getComponentType());
        assertEquals(JsonParser.NumberType.INT, intLikeSerializer._numberType);
        assertEquals("integer", intLikeSerializer._schemaType);
        assertTrue(intLikeSerializer instanceof ContextualSerializer);
    }

    @Test
    public void testIntLikeSerializerSerialize() throws JsonProcessingException {
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new JsonMapping.DefaultSerializerProvider());
        IntLikeSerializer intLikeSerializer = NumberSerializers.IntLikeSerializer.instance;
        intLikeSerializer.serialize(1, gen, new JsonSerializer.DefaultSerializers.BaseSerializerProvider());
        String result = gen.getOutputContent();
        assertEquals("1", result);
    }

    @Test
    public void testIntLikeSerializerSerializeWithType() throws JsonProcessingException {
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new JsonMapping.DefaultSerializerProvider());
        IntLikeSerializer intLikeSerializer = NumberSerializers.IntLikeSerializer.instance;
        intLikeSerializer.serializeWithType(1, gen, new JsonSerializer.DefaultSerializers.BaseSerializerProvider(), new TypeSerializer());
        String result = gen.getOutputContent();
        assertEquals("1", result);
    }

    @Test
    public void testLongSerializer() {
        JsonSerializer<Object> longSerializer = NumberSerializers.LongSerializer.instance;
        assertNull(longSerializer.getParameterizedType(null));
        assertEquals(JsonParser.NumberType.LONG, longSerializer._numberType);
        assertEquals("number", longSerializer._schemaType);
        assertTrue(longSerializer instanceof ContextualSerializer);
    }

    @Test
    public void testLongSerializerSerialize() throws JsonProcessingException {
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new JsonMapping.DefaultSerializerProvider());
        LongSerializer longSerializer = NumberSerializers.LongSerializer.instance;
        longSerializer.serialize(1L, gen, new JsonSerializer.DefaultSerializers.BaseSerializerProvider());
        String result = gen.getOutputContent();
        assertEquals("1", result);
    }

    @Test
    public void testLongSerializerSerializeWithType() throws JsonProcessingException {
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new JsonMapping.DefaultSerializerProvider());
        LongSerializer longSerializer = NumberSerializers.LongSerializer.instance;
        longSerializer.serializeWithType(1L, gen, new JsonSerializer.DefaultSerializers.BaseSerializerProvider(), new TypeSerializer());
        String result = gen.getOutputContent();
        assertEquals("1", result);
    }

    @Test
    public void testShortSerializer() {
        JsonSerializer<Short> shortSerializer = NumberSerializers.ShortSerializer.instance;
        assertEquals(Short.class, shortSerializer.getParameterizedType(null).getComponentType());
        assertEquals(JsonParser.NumberType.INT, shortSerializer._numberType);
        assertEquals("number", shortSerializer._schemaType);
        assertTrue(shortSerializer instanceof ContextualSerializer);
    }

    @Test
    public void testShortSerializerSerialize() throws JsonProcessingException {
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new JsonMapping.DefaultSerializerProvider());
        ShortSerializer shortSerializer = NumberSerializers.ShortSerializer.instance;
        shortSerializer.serialize(1, gen, new JsonSerializer.DefaultSerializers.BaseSerializerProvider());
        String result = gen.getOutputContent();
        assertEquals("1", result);
    }

    @Test
    public void testShortSerializerSerializeWithType() throws JsonProcessingException {
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new JsonMapping.DefaultSerializerProvider());
        ShortSerializer shortSerializer = NumberSerializers.ShortSerializer.instance;
        shortSerializer.serializeWithType(1, gen, new JsonSerializer.DefaultSerializers.BaseSerializerProvider(), new TypeSerializer());
        String result = gen.getOutputContent();
        assertEquals("1", result);
    }

    @Test
    public void testFloatSerializer() {
        JsonSerializer<Object> floatSerializer = NumberSerializers.FloatSerializer.instance;
        assertNull(floatSerializer.getParameterizedType(null));
        assertEquals(JsonParser.NumberType.FLOAT, floatSerializer._numberType);
        assertEquals("number", floatSerializer._schemaType);
        assertTrue(floatSerializer instanceof ContextualSerializer);
    }

    @Test
    public void testFloatSerializerSerialize() throws JsonProcessingException {
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new JsonMapping.DefaultSerializerProvider());
        FloatSerializer floatSerializer = NumberSerializers.FloatSerializer.instance;
        floatSerializer.serialize(1.0f, gen, new JsonSerializer.DefaultSerializers.BaseSerializerProvider());
        String result = gen.getOutputContent();
        assertEquals("1.0", result);
    }

    @Test
    public void testFloatSerializerSerializeWithType() throws JsonProcessingException {
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new JsonMapping.DefaultSerializerProvider());
        FloatSerializer floatSerializer = NumberSerializers.FloatSerializer.instance;
        floatSerializer.serializeWithType(1.0f, gen, new JsonSerializer.DefaultSerializers.BaseSerializerProvider(), new TypeSerializer());
        String result = gen.getOutputContent();
        assertEquals("1.0", result);
    }

    @Test
    public void testDoubleSerializer() {
        JsonSerializer<Object> doubleSerializer = NumberSerializers.DoubleSerializer.instance;
        assertNull(doubleSerializer.getParameterizedType(null));
        assertEquals(JsonParser.NumberType.DOUBLE, doubleSerializer._numberType);
        assertEquals("number", doubleSerializer._schemaType);
        assertTrue(doubleSerializer instanceof ContextualSerializer);
    }

    @Test
    public void testDoubleSerializerSerialize() throws JsonProcessingException {
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new JsonMapping.DefaultSerializerProvider());
        DoubleSerializer doubleSerializer = NumberSerializers.DoubleSerializer.instance;
        doubleSerializer.serialize(1.0, gen, new JsonSerializer.DefaultSerializers.BaseSerializerProvider());
        String result = gen.getOutputContent();
        assertEquals("1.0", result);
    }

    @Test
    public void testDoubleSerializerSerializeWithType() throws JsonProcessingException {
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new JsonMapping.DefaultSerializerProvider());
        DoubleSerializer doubleSerializer = NumberSerializers.DoubleSerializer.instance;
        doubleSerializer.serializeWithType(1.0, gen, new JsonSerializer.DefaultSerializers.BaseSerializerProvider(), new TypeSerializer());
        String result = gen.getOutputContent();
        assertEquals("1.0", result);
    }
}