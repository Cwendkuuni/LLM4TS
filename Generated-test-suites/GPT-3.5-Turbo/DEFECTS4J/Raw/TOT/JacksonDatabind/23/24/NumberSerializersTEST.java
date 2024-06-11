package com.fasterxml.jackson.databind.ser.std;

import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class NumberSerializersTest {

    @Test
    public void testIntegerSerializer() throws IOException {
        Integer value = 10;

        JsonSerializer<?> serializer = new NumberSerializers.IntegerSerializer();

        // Typical use case
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream());
        serializer.serialize(value, gen, new DefaultSerializerProvider.Impl());
        gen.flush();

        // Verify output
        assertEquals("10", gen.getOutputTarget().toString());

        // Edge case
        value = Integer.MIN_VALUE;
        gen = new JsonFactory().createGenerator(new ByteArrayOutputStream());
        serializer.serialize(value, gen, new DefaultSerializerProvider.Impl());
        gen.flush();

        // Verify output
        assertEquals("-2147483648", gen.getOutputTarget().toString());

        // Error scenario
        value = null;
        try {
            serializer.serialize(value, gen, new DefaultSerializerProvider.Impl());
        } catch (NullPointerException e) {
            // NullPointerException expected
        }
    }

    @Test
    public void testShortSerializer() throws IOException {
        Short value = 100;

        JsonSerializer<?> serializer = new NumberSerializers.ShortSerializer();

        // Typical use case
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream());
        serializer.serialize(value, gen, new DefaultSerializerProvider.Impl());
        gen.flush();

        // Verify output
        assertEquals("100", gen.getOutputTarget().toString());

        // Edge case
        value = Short.MAX_VALUE;
        gen = new JsonFactory().createGenerator(new ByteArrayOutputStream());
        serializer.serialize(value, gen, new DefaultSerializerProvider.Impl());
        gen.flush();

        // Verify output
        assertEquals("32767", gen.getOutputTarget().toString());

        // Error scenario
        value = null;
        try {
            serializer.serialize(value, gen, new DefaultSerializerProvider.Impl());
        } catch (NullPointerException e) {
            // NullPointerException expected
        }
    }

    @Test
    public void testIntLikeSerializer() throws IOException {
        Byte value = 50;

        JsonSerializer<?> serializer = new NumberSerializers.IntLikeSerializer();

        // Typical use case
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream());
        serializer.serialize(value, gen, new DefaultSerializerProvider.Impl());
        gen.flush();

        // Verify output
        assertEquals("50", gen.getOutputTarget().toString());

        // Edge case
        value = Byte.MAX_VALUE;
        gen = new JsonFactory().createGenerator(new ByteArrayOutputStream());
        serializer.serialize(value, gen, new DefaultSerializerProvider.Impl());
        gen.flush();

        // Verify output
        assertEquals("127", gen.getOutputTarget().toString());

        // Error scenario
        value = null;
        try {
            serializer.serialize(value, gen, new DefaultSerializerProvider.Impl());
        } catch (NullPointerException e) {
            // NullPointerException expected
        }
    }

    @Test
    public void testLongSerializer() throws IOException {
        Long value = 1000L;

        JsonSerializer<?> serializer = new NumberSerializers.LongSerializer();

        // Typical use case
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream());
        serializer.serialize(value, gen, new DefaultSerializerProvider.Impl());
        gen.flush();

        // Verify output
        assertEquals("1000", gen.getOutputTarget().toString());

        // Edge case
        value = Long.MIN_VALUE;
        gen = new JsonFactory().createGenerator(new ByteArrayOutputStream());
        serializer.serialize(value, gen, new DefaultSerializerProvider.Impl());
        gen.flush();

        // Verify output
        assertEquals("-9223372036854775808", gen.getOutputTarget().toString());

        // Error scenario
        value = null;
        try {
            serializer.serialize(value, gen, new DefaultSerializerProvider.Impl());
        } catch (NullPointerException e) {
            // NullPointerException expected
        }
    }

    @Test
    public void testFloatSerializer() throws IOException {
        Float value = 3.14f;

        JsonSerializer<?> serializer = new NumberSerializers.FloatSerializer();

        // Typical use case
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream());
        serializer.serialize(value, gen, new DefaultSerializerProvider.Impl());
        gen.flush();

        // Verify output
        assertEquals("3.14", gen.getOutputTarget().toString());

        // Edge case
        value = Float.MAX_VALUE;
        gen = new JsonFactory().createGenerator(new ByteArrayOutputStream());
        serializer.serialize(value, gen, new DefaultSerializerProvider.Impl());
        gen.flush();

        // Verify output
        assertEquals("3.4028235E38", gen.getOutputTarget().toString());

        // Error scenario
        value = null;
        try {
            serializer.serialize(value, gen, new DefaultSerializerProvider.Impl());
        } catch (NullPointerException e) {
            // NullPointerException expected
        }
    }

    @Test
    public void testDoubleSerializer() throws IOException {
        Double value = 1.618;

        JsonSerializer<?> serializer = new NumberSerializers.DoubleSerializer();

        // Typical use case
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream());
        serializer.serialize(value, gen, new DefaultSerializerProvider.Impl());
        gen.flush();

        // Verify output
        assertEquals("1.618", gen.getOutputTarget().toString());

        // Edge case
        value = Double.MIN_VALUE;
        gen = new JsonFactory().createGenerator(new ByteArrayOutputStream());
        serializer.serialize(value, gen, new DefaultSerializerProvider.Impl());
        gen.flush();

        // Verify output
        assertEquals("4.9E-324", gen.getOutputTarget().toString());

        // Error scenario
        value = null;
        try {
            serializer.serialize(value, gen, new DefaultSerializerProvider.Impl());
        } catch (NullPointerException e) {
            // NullPointerException expected
        }
    }

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> serializers = new HashMap<>();

        NumberSerializers.addAll(serializers);

        // Verify the size and contents of the map
        assertEquals(14, serializers.size());
        assertEquals(NumberSerializers.IntegerSerializer.class, serializers.get(Integer.class.getName()).getClass());
        assertEquals(NumberSerializers.IntegerSerializer.class, serializers.get(Integer.TYPE.getName()).getClass());
        assertEquals(NumberSerializers.LongSerializer.class, serializers.get(Long.class.getName()).getClass());
        assertEquals(NumberSerializers.LongSerializer.class, serializers.get(Long.TYPE.getName()).getClass());
        assertEquals(NumberSerializers.IntLikeSerializer.class, serializers.get(Byte.class.getName()).getClass());
        assertEquals(NumberSerializers.IntLikeSerializer.class, serializers.get(Byte.TYPE.getName()).getClass());
        assertEquals(NumberSerializers.ShortSerializer.class, serializers.get(Short.class.getName()).getClass());
        assertEquals(NumberSerializers.ShortSerializer.class, serializers.get(Short.TYPE.getName()).getClass());
        assertEquals(NumberSerializers.FloatSerializer.class, serializers.get(Float.class.getName()).getClass());
        assertEquals(NumberSerializers.FloatSerializer.class, serializers.get(Float.TYPE.getName()).getClass());
        assertEquals(NumberSerializers.DoubleSerializer.class, serializers.get(Double.class.getName()).getClass());
        assertEquals(NumberSerializers.DoubleSerializer.class, serializers.get(Double.TYPE.getName()).getClass());
    }
}