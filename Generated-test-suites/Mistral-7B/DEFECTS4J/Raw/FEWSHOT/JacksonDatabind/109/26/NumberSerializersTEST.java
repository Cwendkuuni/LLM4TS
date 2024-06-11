package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.*;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.Base;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.IntegerSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.IntLikeSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.LongSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.ShortSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.FloatSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.DoubleSerializer;

import org.junit.Test;

public class NumberSerializersTest {

    @Test
    public void testIntegerSerializer() throws JsonProcessingException {
        Integer value = 123;
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), JsonMapping.StandardSerializers.getFactory(null));
        NumberSerializers.IntegerSerializer serializer = new NumberSerializers.IntegerSerializer(Integer.class);
        serializer.serialize(value, gen, null);
        assertEquals("123", gen.getOutputContent().toString());
    }

    @Test
    public void testIntLikeSerializer() throws JsonProcessingException {
        Integer value = 123;
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), JsonMapping.StandardSerializers.getFactory(null));
        NumberSerializers.IntLikeSerializer serializer = new NumberSerializers.IntLikeSerializer();
        serializer.serialize(value, gen, null);
        assertEquals("123", gen.getOutputContent().toString());
    }

    @Test
    public void testShortSerializer() throws JsonProcessingException {
        Short value = 123;
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), JsonMapping.StandardSerializers.getFactory(null));
        NumberSerializers.ShortSerializer serializer = NumberSerializers.ShortSerializer.instance;
        serializer.serialize(value, gen, null);
        assertEquals("123", gen.getOutputContent().toString());
    }

    @Test
    public void testLongSerializer() throws JsonProcessingException {
        Long value = 123L;
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), JsonMapping.StandardSerializers.getFactory(null));
        NumberSerializers.LongSerializer serializer = new NumberSerializers.LongSerializer(Long.class);
        serializer.serialize(value, gen, null);
        assertEquals("123", gen.getOutputContent().toString());
    }

    @Test
    public void testFloatSerializer() throws JsonProcessingException {
        Float value = 123.45f;
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), JsonMapping.StandardSerializers.getFactory(null));
        NumberSerializers.FloatSerializer serializer = NumberSerializers.FloatSerializer.instance;
        serializer.serialize(value, gen, null);
        assertEquals("123.45", gen.getOutputContent().toString());
    }

    @Test
    public void testDoubleSerializer() throws JsonProcessingException {
        Double value = 123.45;
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), JsonMapping.StandardSerializers.getFactory(null));
        NumberSerializers.DoubleSerializer serializer = new NumberSerializers.DoubleSerializer(Double.class);
        serializer.serialize(value, gen, null);
        assertEquals("123.45", gen.getOutputContent().toString());
    }

    @Test
    public void testIntegerSerializerWithType() throws JsonProcessingException {
        Integer value = 123;
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), JsonMapping.StandardSerializers.getFactory(null));
        NumberSerializers.IntegerSerializer serializer = new NumberSerializers.IntegerSerializer(Integer.class);
        TypeSerializer typeSerializer = JsonMapping.StandardSerializers.findValueSerializer(Integer.TYPE, null);
        serializer.serializeWithType(value, gen, null, typeSerializer);
        assertEquals("123", gen.getOutputContent().toString());
    }

    @Test
    public void testIntLikeSerializerWithType() throws JsonProcessingException {
        Integer value = 123;
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), JsonMapping.StandardSerializers.getFactory(null));
        NumberSerializers.IntLikeSerializer serializer = new NumberSerializers.IntLikeSerializer();
        TypeSerializer typeSerializer = JsonMapping.StandardSerializers.findValueSerializer(Number.class, null);
        serializer.serializeWithType(value, gen, null, typeSerializer);
        assertEquals("123", gen.getOutputContent().toString());
    }

    @Test
    public void testShortSerializerWithType() throws JsonProcessingException {
        Short value = 123;
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), JsonMapping.StandardSerializers.getFactory(null));
        NumberSerializers.ShortSerializer serializer = NumberSerializers.ShortSerializer.instance;
        TypeSerializer typeSerializer = JsonMapping.StandardSerializers.findValueSerializer(Short.class, null);
        serializer.serializeWithType(value, gen, null, typeSerializer);
        assertEquals("123", gen.getOutputContent().toString());
    }

    @Test
    public void testLongSerializerWithType() throws JsonProcessingException {
        Long value = 123L;
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), JsonMapping.StandardSerializers.getFactory(null));
        NumberSerializers.LongSerializer serializer = new NumberSerializers.LongSerializer(Long.class);
        TypeSerializer typeSerializer = JsonMapping.StandardSerializers.findValueSerializer(Long.TYPE, null);
        serializer.serializeWithType(value, gen, null, typeSerializer);
        assertEquals("123", gen.getOutputContent().toString());
    }

    @Test
    public void testFloatSerializerWithType() throws JsonProcessingException {
        Float value = 123.45f;
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), JsonMapping.StandardSerializers.getFactory(null));
        NumberSerializers.FloatSerializer serializer = NumberSerializers.FloatSerializer.instance;
        TypeSerializer typeSerializer = JsonMapping.StandardSerializers.findValueSerializer(Float.TYPE, null);
        serializer.serializeWithType(value, gen, null, typeSerializer);
        assertEquals("123.45", gen.getOutputContent().toString());
    }

    @Test
    public void testDoubleSerializerWithType() throws JsonProcessingException {
        Double value = 123.45;
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), JsonMapping.StandardSerializers.getFactory(null));
        NumberSerializers.DoubleSerializer serializer = new NumberSerializers.DoubleSerializer(Double.class);
        TypeSerializer typeSerializer = JsonMapping.StandardSerializers.findValueSerializer(Double.TYPE, null);
        serializer.serializeWithType(value, gen, null, typeSerializer);
        assertEquals("123.45", gen.getOutputContent().toString());
    }

}