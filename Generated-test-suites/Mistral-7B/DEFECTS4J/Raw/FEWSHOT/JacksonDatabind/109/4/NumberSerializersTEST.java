package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

@SuppressWarnings("unchecked")
public class NumberSerializersTest {

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new java.util.LinkedHashMap<>();
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

    @Test
    public void testShortSerializer() throws JsonProcessingException {
        JsonSerializer<Short> shortSerializer = new ShortSerializer();
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);

        shortSerializer.serialize(Short.MIN_VALUE, gen, new JsonFactory());
        shortSerializer.serialize(Short.MAX_VALUE, gen, new JsonFactory());
        shortSerializer.serialize((short) 0, gen, new JsonFactory());

        gen.close();
    }

    @Test
    public void testIntegerSerializer() throws JsonProcessingException {
        JsonSerializer<Integer> integerSerializer = new IntegerSerializer(Integer.class);
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);

        integerSerializer.serialize(Integer.MIN_VALUE, gen, new JsonFactory());
        integerSerializer.serialize(Integer.MAX_VALUE, gen, new JsonFactory());
        integerSerializer.serialize(0, gen, new JsonFactory());

        gen.close();
    }

    @Test
    public void testIntLikeSerializer() throws JsonProcessingException {
        JsonSerializer<Number> intLikeSerializer = IntLikeSerializer.instance;
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);

        intLikeSerializer.serialize(Byte.MIN_VALUE, gen, new JsonFactory());
        intLikeSerializer.serialize(Byte.MAX_VALUE, gen, new JsonFactory());
        intLikeSerializer.serialize((byte) 0, gen, new JsonFactory());

        intLikeSerializer.serialize(Short.MIN_VALUE, gen, new JsonFactory());
        intLikeSerializer.serialize(Short.MAX_VALUE, gen, new JsonFactory());
        intLikeSerializer.serialize((short) 0, gen, new JsonFactory());

        intLikeSerializer.serialize(Integer.MIN_VALUE, gen, new JsonFactory());
        intLikeSerializer.serialize(Integer.MAX_VALUE, gen, new JsonFactory());
        intLikeSerializer.serialize(0, gen, new JsonFactory());

        intLikeSerializer.serialize(Long.MIN_VALUE, gen, new JsonFactory());
        intLikeSerializer.serialize(Long.MAX_VALUE, gen, new JsonFactory());
        intLikeSerializer.serialize(0L, gen, new JsonFactory());

        gen.close();
    }

    @Test
    public void testLongSerializer() throws JsonProcessingException {
        JsonSerializer<Long> longSerializer = new LongSerializer(Long.class);
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);

        longSerializer.serialize(Long.MIN_VALUE, gen, new JsonFactory());
        longSerializer.serialize(Long.MAX_VALUE, gen, new JsonFactory());
        longSerializer.serialize(0L, gen, new JsonFactory());

        gen.close();
    }

    @Test
    public void testFloatSerializer() throws JsonProcessingException {
        JsonSerializer<Float> floatSerializer = FloatSerializer.instance;
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);

        floatSerializer.serialize(Float.NEGATIVE_INFINITY, gen, new JsonFactory());
        floatSerializer.serialize(Float.POSITIVE_INFINITY, gen, new JsonFactory());
        floatSerializer.serialize(0f, gen, new JsonFactory());

        gen.close();
    }

    @Test
    public void testDoubleSerializer() throws JsonProcessingException {
        JsonSerializer<Double> doubleSerializer = new DoubleSerializer(Double.class);
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);

        doubleSerializer.serialize(Double.MIN_VALUE, gen, new JsonFactory());
        doubleSerializer.serialize(Double.MAX_VALUE, gen, new JsonFactory());
        doubleSerializer.serialize(0.0, gen, new JsonFactory());

        gen.close();
    }
}