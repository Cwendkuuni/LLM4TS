package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.*;
import org.junit.Test;

public class NumberSerializersTest {

    @Test
    public void testShortSerializer() throws IOException {
        ShortSerializer serializer = new ShortSerializer();
        JsonGenerator gen = new MockJsonGenerator();

        serializer.serialize(Short.MIN_VALUE, gen, new SerializerProvider());
        assertEquals("-Short.MIN_VALUE", gen.getOutputContent().toString());

        serializer.serialize(Short.MAX_VALUE, gen, new SerializerProvider());
        assertEquals("Short.MAX_VALUE", gen.getOutputContent().toString());

        serializer.serialize((short) 0, gen, new SerializerProvider());
        assertEquals("0", gen.getOutputContent().toString());

        serializer.serialize((short) 123, gen, new SerializerProvider());
        assertEquals("123", gen.getOutputContent().toString());
    }

    @Test
    public void testIntegerSerializer() throws IOException {
        IntegerSerializer serializer = new IntegerSerializer();
        JsonGenerator gen = new MockJsonGenerator();

        serializer.serialize((Object) Short.MIN_VALUE, gen, new SerializerProvider());
        assertEquals("-Short.MIN_VALUE", gen.getOutputContent().toString());

        serializer.serialize((Object) Short.MAX_VALUE, gen, new SerializerProvider());
        assertEquals("Short.MAX_VALUE", gen.getOutputContent().toString());

        serializer.serialize((Object) 0, gen, new SerializerProvider());
        assertEquals("0", gen.getOutputContent().toString());

        serializer.serialize((Object) 123, gen, new SerializerProvider());
        assertEquals("123", gen.getOutputContent().toString());

        serializer.serialize((Object) Integer.MIN_VALUE, gen, new SerializerProvider());
        assertEquals("-Integer.MIN_VALUE", gen.getOutputContent().toString());

        serializer.serialize((Object) Integer.MAX_VALUE, gen, new SerializerProvider());
        assertEquals("Integer.MAX_VALUE", gen.getOutputContent().toString());

        serializer.serialize((Object) 1234567890, gen, new SerializerProvider());
        assertEquals("1234567890", gen.getOutputContent().toString());
    }

    @Test
    public void testIntLikeSerializer() throws IOException {
        IntLikeSerializer serializer = new IntLikeSerializer();
        JsonGenerator gen = new MockJsonGenerator();

        serializer.serialize((Number) Short.MIN_VALUE, gen, new SerializerProvider());
        assertEquals("-Short.MIN_VALUE", gen.getOutputContent().toString());

        serializer.serialize((Number) Short.MAX_VALUE, gen, new SerializerProvider());
        assertEquals("Short.MAX_VALUE", gen.getOutputContent().toString());

        serializer.serialize((Number) 0, gen, new SerializerProvider());
        assertEquals("0", gen.getOutputContent().toString());

        serializer.serialize((Number) 123, gen, new SerializerProvider());
        assertEquals("123", gen.getOutputContent().toString());

        serializer.serialize((Number) Long.MIN_VALUE, gen, new SerializerProvider());
        assertEquals("-Long.MIN_VALUE", gen.getOutputContent().toString());

        serializer.serialize((Number) Long.MAX_VALUE, gen, new SerializerProvider());
        assertEquals("Long.MAX_VALUE", gen.getOutputContent().toString());

        serializer.serialize((Number) 1234567890, gen, new SerializerProvider());
        assertEquals("1234567890", gen.getOutputContent().toString());
    }

    @Test
    public void testLongSerializer() throws IOException {
        LongSerializer serializer = new LongSerializer();
        JsonGenerator gen = new MockJsonGenerator();

        serializer.serialize(Long.MIN_VALUE, gen, new SerializerProvider());
        assertEquals("-Long.MIN_VALUE", gen.getOutputContent().toString());

        serializer.serialize(Long.MAX_VALUE, gen, new SerializerProvider());
        assertEquals("Long.MAX_VALUE", gen.getOutputContent().toString());

        serializer.serialize((long) 0, gen, new SerializerProvider());
        assertEquals("0", gen.getOutputContent().toString());

        serializer.serialize((long) 123, gen, new SerializerProvider());
        assertEquals("123", gen.getOutputContent().toString());

        serializer.serialize((long) 1234567890, gen, new SerializerProvider());
        assertEquals("1234567890", gen.getOutputContent().toString());
    }

    @Test
    public void testFloatSerializer() throws IOException {
        FloatSerializer serializer = new FloatSerializer();
        JsonGenerator gen = new MockJsonGenerator();

        serializer.serialize((Object) Float.NaN, gen, new SerializerProvider());
        assertEquals("null", gen.getOutputContent().toString());

        serializer.serialize((Object) Float.POSITIVE_INFINITY, gen, new SerializerProvider());
        assertEquals("null", gen.getOutputContent().toString());

        serializer.serialize((Object) Float.NEGATIVE_INFINITY, gen, new SerializerProvider());
        assertEquals("null", gen.getOutputContent().toString());

        serializer.serialize((Object) 0.0f, gen, new SerializerProvider());
        assertEquals("0", gen.getOutputContent().toString());

        serializer.serialize((Object) 1.23f, gen, new SerializerProvider());
        assertEquals("1.23", gen.getOutputContent().toString());

        serializer.serialize((Object) 123456.789f, gen, new SerializerProvider());
        assertEquals("123456.789", gen.getOutputContent().toString());
    }

    @Test
    public void testDoubleSerializer() throws IOException {
        DoubleSerializer serializer = new DoubleSerializer();
        JsonGenerator gen = new MockJsonGenerator();

        serializer.serialize((Object) Double.NaN, gen, new SerializerProvider());
        assertEquals("null", gen.getOutputContent().toString());

        serializer.serialize((Object) Double.POSITIVE_INFINITY, gen, new SerializerProvider());
        assertEquals("null", gen.getOutputContent().toString());

        serializer.serialize((Object) Double.NEGATIVE_INFINITY, gen, new SerializerProvider());
        assertEquals("null", gen.getOutputContent().toString());

        serializer.serialize((Object) 0.0, gen, new SerializerProvider());
        assertEquals("0", gen.getOutputContent().toString());

        serializer.serialize((Object) 1.23, gen, new SerializerProvider());
        assertEquals("1.23", gen.getOutputContent().toString());

        serializer.serialize((Object) 123456.789, gen, new SerializerProvider());
        assertEquals("123456.789", gen.getOutputContent().toString());
    }
}