package com.fasterxml.jackson.databind.ser.std;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.fasterxml.jackson.core.*;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

/**
 * Unit tests for NumberSerializers class.
 */
public class NumberSerializersTest {

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
        NumberSerializers.addAll(allDeserializers);
        Assert.assertEquals(8, allDeserializers.size());
        Assert.assertNotNull(allDeserializers.get(Integer.class.getName()));
        Assert.assertNotNull(allDeserializers.get(Integer.TYPE.getName()));
        Assert.assertNotNull(allDeserializers.get(Long.class.getName()));
        Assert.assertNotNull(allDeserializers.get(Long.TYPE.getName()));
        Assert.assertNotNull(allDeserializers.get(Byte.class.getName()));
        Assert.assertNotNull(allDeserializers.get(Byte.TYPE.getName()));
        Assert.assertNotNull(allDeserializers.get(Short.class.getName()));
        Assert.assertNotNull(allDeserializers.get(Short.TYPE.getName()));
        Assert.assertNotNull(allDeserializers.get(Double.class.getName()));
        Assert.assertNotNull(allDeserializers.get(Double.TYPE.getName()));
        Assert.assertNotNull(allDeserializers.get(Float.class.getName()));
        Assert.assertNotNull(allDeserializers.get(Float.TYPE.getName()));
    }

    @Test
    public void testShortSerializer() throws IOException {
        NumberSerializers.ShortSerializer serializer = new NumberSerializers.ShortSerializer();
        Short value = 10;
        JsonGenerator generator = new JsonFactory().createGenerator(System.out);
        serializer.serialize(value, generator, new DefaultSerializerProvider.Impl());
    }

    @Test
    public void testIntegerSerializer() throws IOException {
        NumberSerializers.IntegerSerializer serializer = new NumberSerializers.IntegerSerializer(Integer.class);
        Integer value = 10;
        JsonGenerator generator = new JsonFactory().createGenerator(System.out);
        serializer.serialize(value, generator, new DefaultSerializerProvider.Impl());
    }

    @Test
    public void testIntLikeSerializer() throws IOException {
        NumberSerializers.IntLikeSerializer serializer = new NumberSerializers.IntLikeSerializer();
        Number value = 10;
        JsonGenerator generator = new JsonFactory().createGenerator(System.out);
        serializer.serialize(value, generator, new DefaultSerializerProvider.Impl());
    }

    @Test
    public void testLongSerializer() throws IOException {
        NumberSerializers.LongSerializer serializer = new NumberSerializers.LongSerializer(Long.class);
        Long value = 10L;
        JsonGenerator generator = new JsonFactory().createGenerator(System.out);
        serializer.serialize(value, generator, new DefaultSerializerProvider.Impl());
    }

    @Test
    public void testFloatSerializer() throws IOException {
        NumberSerializers.FloatSerializer serializer = new NumberSerializers.FloatSerializer();
        Float value = 10.5f;
        JsonGenerator generator = new JsonFactory().createGenerator(System.out);
        serializer.serialize(value, generator, new DefaultSerializerProvider.Impl());
    }

    @Test
    public void testDoubleSerializer() throws IOException {
        NumberSerializers.DoubleSerializer serializer = new NumberSerializers.DoubleSerializer(Double.class);
        Double value = 10.5;
        JsonGenerator generator = new JsonFactory().createGenerator(System.out);
        serializer.serialize(value, generator, new DefaultSerializerProvider.Impl());
    }
}