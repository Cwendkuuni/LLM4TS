package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.*;
import com.fasterxml.jackson.databind.util.StdConverter;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class NumberSerializersTest {

    private Map<String, JsonSerializer<?>> deserializers;

    @Before
    public void setup() {
        deserializers = new HashMap<>();
        NumberSerializers.addAll(deserializers);
    }

    @Test
    public void testAddAll() {
        assertNotNull(deserializers);
        assertEquals(7, deserializers.size());
    }

    @Test
    public void testShortSerializer() {
        ShortSerializer serializer = (ShortSerializer) deserializers.get(Short.class.getName());
        assertNotNull(serializer);
        assertEquals(Short.class, serializer.handledType());
        assertEquals(JsonParser.NumberType.INT, serializer._numberType);
        assertEquals("number", serializer._schemaType);
    }

    @Test
    public void testShortSerializerSerialize() throws IOException {
        ShortSerializer serializer = (ShortSerializer) deserializers.get(Short.class.getName());
        assertNotNull(serializer);
        JsonGenerator gen = new MappingJsonFactory().createGenerator(System.out);
        SerializerProvider provider = new DefaultSerializerProvider.Impl();
        serializer.serialize((short) 5, gen, provider);
    }

    @Test
    public void testIntegerSerializer() {
        IntegerSerializer serializer = (IntegerSerializer) deserializers.get(Integer.class.getName());
        assertNotNull(serializer);
        assertEquals(Integer.class, serializer.handledType());
        assertEquals(JsonParser.NumberType.INT, serializer._numberType);
        assertEquals("integer", serializer._schemaType);
    }

    @Test
    public void testIntegerSerializerSerialize() throws IOException {
        IntegerSerializer serializer = (IntegerSerializer) deserializers.get(Integer.class.getName());
        assertNotNull(serializer);
        JsonGenerator gen = new MappingJsonFactory().createGenerator(System.out);
        SerializerProvider provider = new DefaultSerializerProvider.Impl();
        serializer.serialize(10, gen, provider);
    }

    @Test
    public void testIntLikeSerializer() {
        IntLikeSerializer serializer = (IntLikeSerializer) deserializers.get(Number.class.getName());
        assertNotNull(serializer);
        assertEquals(Number.class, serializer.handledType());
        assertEquals(JsonParser.NumberType.INT, serializer._numberType);
        assertEquals("integer", serializer._schemaType);
    }

    @Test
    public void testIntLikeSerializerSerialize() throws IOException {
        IntLikeSerializer serializer = (IntLikeSerializer) deserializers.get(Number.class.getName());
        assertNotNull(serializer);
        JsonGenerator gen = new MappingJsonFactory().createGenerator(System.out);
        SerializerProvider provider = new DefaultSerializerProvider.Impl();
        serializer.serialize(15, gen, provider);
    }

    @Test
    public void testLongSerializer() {
        LongSerializer serializer = (LongSerializer) deserializers.get(Long.class.getName());
        assertNotNull(serializer);
        assertEquals(Long.class, serializer.handledType());
        assertEquals(JsonParser.NumberType.LONG, serializer._numberType);
        assertEquals("number", serializer._schemaType);
    }

    @Test
    public void testLongSerializerSerialize() throws IOException {
        LongSerializer serializer = (LongSerializer) deserializers.get(Long.class.getName());
        assertNotNull(serializer);
        JsonGenerator gen = new MappingJsonFactory().createGenerator(System.out);
        SerializerProvider provider = new DefaultSerializerProvider.Impl();
        serializer.serialize(20L, gen, provider);
    }

    @Test
    public void testFloatSerializer() {
        FloatSerializer serializer = (FloatSerializer) deserializers.get(Float.class.getName());
        assertNotNull(serializer);
        assertEquals(Float.class, serializer.handledType());
        assertEquals(JsonParser.NumberType.FLOAT, serializer._numberType);
        assertEquals("number", serializer._schemaType);
    }

    @Test
    public void testFloatSerializerSerialize() throws IOException {
        FloatSerializer serializer = (FloatSerializer) deserializers.get(Float.class.getName());
        assertNotNull(serializer);
        JsonGenerator gen = new MappingJsonFactory().createGenerator(System.out);
        SerializerProvider provider = new DefaultSerializerProvider.Impl();
        serializer.serialize(10.5f, gen, provider);
    }

    @Test
    public void testDoubleSerializer() {
        DoubleSerializer serializer = (DoubleSerializer) deserializers.get(Double.class.getName());
        assertNotNull(serializer);
        assertEquals(Double.class, serializer.handledType());
        assertEquals(JsonParser.NumberType.DOUBLE, serializer._numberType);
        assertEquals("number", serializer._schemaType);
    }

    @Test
    public void testDoubleSerializerSerialize() throws IOException {
        DoubleSerializer serializer = (DoubleSerializer) deserializers.get(Double.class.getName());
        assertNotNull(serializer);
        JsonGenerator gen = new MappingJsonFactory().createGenerator(System.out);
        SerializerProvider provider = new DefaultSerializerProvider.Impl();
        serializer.serialize(20.5d, gen, provider);
    }
}