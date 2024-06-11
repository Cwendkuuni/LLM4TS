package com.fasterxml.jackson.databind.ser.std;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonIntegerFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonNumberFormatVisitor;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

public class NumberSerializersTest {

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
        NumberSerializers.addAll(allDeserializers);

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
    public void testShortSerializer() throws IOException {
        JsonSerializer<Short> serializer = new NumberSerializers.ShortSerializer();
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);

        serializer.serialize((short) 123, gen, null);

        gen.flush();
    }

    @Test
    public void testIntegerSerializer() throws IOException {
        JsonSerializer<Object> serializer = new NumberSerializers.IntegerSerializer();
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);

        serializer.serialize(123, gen, null);

        gen.flush();
    }

    @Test
    public void testIntLikeSerializer() throws IOException {
        JsonSerializer<Number> serializer = new NumberSerializers.IntLikeSerializer();
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);

        serializer.serialize(123, gen, null);

        gen.flush();
    }

    @Test
    public void testLongSerializer() throws IOException {
        JsonSerializer<Object> serializer = new NumberSerializers.LongSerializer();
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);

        serializer.serialize(123L, gen, null);

        gen.flush();
    }

    @Test
    public void testFloatSerializer() throws IOException {
        JsonSerializer<Object> serializer = new NumberSerializers.FloatSerializer();
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);

        serializer.serialize(123.0f, gen, null);

        gen.flush();
    }

    @Test
    public void testDoubleSerializer() throws IOException {
        JsonSerializer<Object> serializer = new NumberSerializers.DoubleSerializer();
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);

        serializer.serialize(123.0, gen, null);

        gen.flush();
    }
}