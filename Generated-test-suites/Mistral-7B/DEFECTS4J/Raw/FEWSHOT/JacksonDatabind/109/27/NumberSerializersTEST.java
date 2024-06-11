package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.*;

import org.junit.Test;

public class NumberSerializersTest {

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new java.util.HashMap<>();
        NumberSerializers.addAll(allDeserializers);

        assertEquals(Integer.class.getName(), allDeserializers.get(Integer.class.getName()));
        assertEquals(Integer.TYPE.getName(), allDeserializers.get(Integer.TYPE.getName()));
        assertEquals(Long.class.getName(), allDeserializers.get(Long.class.getName()));
        assertEquals(Long.TYPE.getName(), allDeserializers.get(Long.TYPE.getName()));
        assertEquals(Byte.class.getName(), allDeserializers.get(Byte.class.getName()));
        assertEquals(Byte.TYPE.getName(), allDeserializers.get(Byte.TYPE.getName()));
        assertEquals(Short.class.getName(), allDeserializers.get(Short.class.getName()));
        assertEquals(Short.TYPE.getName(), allDeserializers.get(Short.TYPE.getName()));
        assertEquals(Double.class.getName(), allDeserializers.get(Double.class.getName()));
        assertEquals(Double.TYPE.getName(), allDeserializers.get(Double.TYPE.getName()));
        assertEquals(Float.class.getName(), allDeserializers.get(Float.class.getName()));
        assertEquals(Float.TYPE.getName(), allDeserializers.get(Float.TYPE.getName()));
    }

    @Test
    public void testShortSerializer() {
        ShortSerializer shortSerializer = new ShortSerializer();
        JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());

        shortSerializer.serialize(Short.valueOf("127"), gen, new SerializerProvider());
        assertEquals("127", gen.getOutputText());

        shortSerializer.serialize(Short.MIN_VALUE, gen, new SerializerProvider());
        assertEquals("-128", gen.getOutputText());

        shortSerializer.serialize((short) 0, gen, new SerializerProvider());
        assertEquals("0", gen.getOutputText());
    }

    @Test
    public void testIntegerSerializer() {
        IntegerSerializer integerSerializer = new IntegerSerializer(Integer.class);
        JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());

        integerSerializer.serialize(Integer.valueOf("2147483647"), gen, new SerializerProvider());
        assertEquals("2147483647", gen.getOutputText());

        integerSerializer.serialize(Integer.MIN_VALUE, gen, new SerializerProvider());
        assertEquals("-2147483648", gen.getOutputText());

        integerSerializer.serialize((Integer) null, gen, new SerializerProvider());
        assertEquals(null, gen.getOutputText());
    }

    @Test
    public void testIntLikeSerializer() {
        IntLikeSerializer intLikeSerializer = new IntLikeSerializer();
        JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());

        intLikeSerializer.serialize(Byte.valueOf("127"), gen, new SerializerProvider());
        assertEquals("127", gen.getOutputText());

        intLikeSerializer.serialize(Short.MIN_VALUE, gen, new SerializerProvider());
        assertEquals("-128", gen.getOutputText());

        intLikeSerializer.serialize((Number) null, gen, new SerializerProvider());
        assertEquals(null, gen.getOutputText());
    }

    @Test
    public void testLongSerializer() {
        LongSerializer longSerializer = new LongSerializer(Long.class);
        JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());

        longSerializer.serialize(Long.valueOf("9223372036854775807L"), gen, new SerializerProvider());
        assertEquals("9223372036854775807", gen.getOutputText());

        longSerializer.serialize(Long.MIN_VALUE, gen, new SerializerProvider());
        assertEquals("-9223372036854775808", gen.getOutputText());

        longSerializer.serialize((Long) null, gen, new SerializerProvider());
        assertEquals(null, gen.getOutputText());
    }

    @Test
    public void testFloatSerializer() {
        JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());

        FloatSerializer floatSerializer = new FloatSerializer();
        floatSerializer.serialize(Float.valueOf("3.14159265358979323846"), gen, new SerializerProvider());
        assertEquals("3.141592653589793", gen.getOutputText());

        floatSerializer.serialize(Float.NaN, gen, new SerializerProvider());
        assertEquals("NaN", gen.getOutputText());

        floatSerializer.serialize(Float.NEGATIVE_INFINITY, gen, new SerializerProvider());
        assertEquals("-Infinity", gen.getOutputText());

        floatSerializer.serialize((Float) null, gen, new SerializerProvider());
        assertEquals(null, gen.getOutputText());
    }

    @Test
    public void testDoubleSerializer() {
        JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());

        DoubleSerializer doubleSerializer = new DoubleSerializer(Double.class);
        doubleSerializer.serialize(Double.valueOf("6.02214076e+23"), gen, new SerializerProvider());
        assertEquals("6.02214076e+23", gen.getOutputText());

        doubleSerializer.serialize(Double.NaN, gen, new SerializerProvider());
        assertEquals("NaN", gen.getOutputText());

        doubleSerializer.serialize(Double.NEGATIVE_INFINITY, gen, new SerializerProvider());
        assertEquals("-Infinity", gen.getOutputText());

        doubleSerializer.serialize((Double) null, gen, new SerializerProvider());
        assertEquals(null, gen.getOutputText());
    }
}