package com.fasterxml.jackson.databind.ser.std;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonIntegerFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonNumberFormatVisitor;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.ShortSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.IntegerSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.IntLikeSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.LongSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.FloatSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.DoubleSerializer;

import org.junit.Test;

public class NumberSerializersTest {

    @Test
    public void testShortSerializer() throws IOException {
        JsonSerializer<Short> serializer = new ShortSerializer();
        Short value = 123;
        
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        serializer.serialize(value, gen, provider);
    }

    @Test
    public void testIntegerSerializer() throws IOException {
        JsonSerializer<Object> serializer = new IntegerSerializer();
        Integer value = 123456;
        
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        serializer.serialize(value, gen, provider);
    }

    @Test
    public void testIntLikeSerializer() throws IOException {
        JsonSerializer<Number> serializer = new IntLikeSerializer();
        Number value = 123456;
        
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        serializer.serialize(value, gen, provider);
    }

    @Test
    public void testLongSerializer() throws IOException {
        JsonSerializer<Object> serializer = new LongSerializer();
        Long value = 1234567890L;
        
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        serializer.serialize(value, gen, provider);
    }

    @Test
    public void testFloatSerializer() throws IOException {
        JsonSerializer<Object> serializer = new FloatSerializer();
        Float value = 123.456f;
        
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        serializer.serialize(value, gen, provider);
    }

    @Test
    public void testDoubleSerializer() throws IOException {
        JsonSerializer<Object> serializer = new DoubleSerializer();
        Double value = 123.456789;
        
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        serializer.serialize(value, gen, provider);
    }
}