package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

public class NumberSerializersTest {

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new java.util.HashMap<>();
        NumberSerializers.addAll(allDeserializers);
        assertEquals(13, allDeserializers.size());
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
    public void testBaseClass() throws JsonProcessingException {
        JsonSerializer<?> shortSerializer = NumberSerializers.ShortSerializer.instance;
        Base<Short> shortSerializerBase = (Base<Short>) shortSerializer;
        assertEquals(JsonParser.NumberType.INT, shortSerializerBase._numberType);
        assertEquals("number", shortSerializerBase._schemaType);
        assertTrue(shortSerializerBase._isInt);

        JsonSerializer<?> intSerializer = NumberSerializers.IntegerSerializer.instance;
        Base<Object> intSerializerBase = (Base<Object>) intSerializer;
        assertEquals(Integer.class, intSerializerBase.getSerializableType());
        assertEquals(JsonParser.NumberType.INT, intSerializerBase._numberType);
        assertEquals("integer", intSerializerBase._schemaType);
        assertTrue(intSerializerBase._isInt);

        JsonSerializer<?> intLikeSerializer = NumberSerializers.IntLikeSerializer.instance;
        Base<Number> intLikeSerializerBase = (Base<Number>) intLikeSerializer;
        assertEquals(Number.class, intLikeSerializerBase.getSerializableType());
        assertEquals(JsonParser.NumberType.INT, intLikeSerializerBase._numberType);
        assertEquals("integer", intLikeSerializerBase._schemaType);
        assertTrue(intLikeSerializerBase._isInt);
    }

    @Test
    public void testShortSerializer() throws JsonProcessingException {
        Short serializableValue = Short.MIN_VALUE;
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), JsonMapping.getDefaultSerializerProvider());
        NumberSerializers.ShortSerializer.instance.serialize(serializableValue, gen, null);
        JsonParser parser = new JsonFactory().createParser(new ByteArrayInputStream(gen.getOutputStream().toByteArray()));
        assertEquals(Short.MIN_VALUE, parser.readValueAs(Short.class));
    }

    @Test
    public void testIntegerSerializer() throws JsonProcessingException {
        Integer serializableValue = Integer.MIN_VALUE;
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), JsonMapping.getDefaultSerializerProvider());
        NumberSerializers.IntegerSerializer.instance.serialize(serializableValue, gen, null);
        JsonParser parser = new JsonFactory().createParser(new ByteArrayInputStream(gen.getOutputStream().toByteArray()));
        assertEquals(Integer.MIN_VALUE, parser.readValueAs(Integer.class));
    }

    @Test
    public void testIntLikeSerializer() throws JsonProcessingException {
        Byte serializableValue = Byte.MIN_VALUE;
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), JsonMapping.getDefaultSerializerProvider());
        NumberSerializers.IntLikeSerializer.instance.serialize(serializableValue, gen, null);
        JsonParser parser = new JsonFactory().createParser(new ByteArrayInputStream(gen.getOutputStream().toByteArray()));
        assertEquals(Byte.MIN_VALUE, parser.readValueAs(Byte.class));
    }

    @Test
    public void testLongSerializer() throws JsonProcessingException {
        Long serializableValue = Long.MIN_VALUE;
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), JsonMapping.getDefaultSerializerProvider());
        NumberSerializers.LongSerializer.instance.serialize(serializableValue, gen, null);
        JsonParser parser = new JsonFactory().createParser(new ByteArrayInputStream(gen.getOutputStream().toByteArray()));
        assertEquals(Long.MIN_VALUE, parser.readValueAs(Long.class));
    }

    @Test
    public void testFloatSerializer() throws JsonProcessingException {
        Float serializableValue = Float.MIN_VALUE;
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), JsonMapping.getDefaultSerializerProvider());
        NumberSerializers.FloatSerializer.instance.serialize(serializableValue, gen, null);
        JsonParser parser = new JsonFactory().createParser(new ByteArrayInputStream(gen.getOutputStream().toByteArray()));
        assertEquals(Float.MIN_VALUE, parser.readValueAs(Float.class));
    }

    @Test
    public void testDoubleSerializer() throws JsonProcessingException {
        Double serializableValue = Double.MIN_VALUE;
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), JsonMapping.getDefaultSerializerProvider());
        NumberSerializers.DoubleSerializer.instance.serialize(serializableValue, gen, null);
        JsonParser parser = new JsonFactory().createParser(new ByteArrayInputStream(gen.getOutputStream().toByteArray()));
        assertEquals(Double.MIN_VALUE, parser.readValueAs(Double.class));
    }
}