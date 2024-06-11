package com.fasterxml.jackson.databind.ser.std;
import org.junit.*;
import static org.junit.Assert.*;

public class NumberSerializersTest {

    @Test
    public void testShortSerializer() {
        NumberSerializers.ShortSerializer serializer = new NumberSerializers.ShortSerializer();
        Short value = Short.valueOf("10");
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);
        SerializerProvider provider = new DefaultSerializerProvider.Impl().createInstance();
        try {
            serializer.serialize(value, generator, provider);
            generator.close();
            assertEquals(writer.toString(), "10");
        } catch (IOException e) {
            fail("An exception occurred during test execution: " + e.getMessage());
        }
    }

    @Test
    public void testIntegerSerializer() {
        NumberSerializers.IntegerSerializer serializer = new NumberSerializers.IntegerSerializer(Integer.class);
        Integer value = Integer.valueOf("20");
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);
        SerializerProvider provider = new DefaultSerializerProvider.Impl().createInstance();
        try {
            serializer.serialize(value, generator, provider);
            generator.close();
            assertEquals(writer.toString(), "20");
        } catch (IOException e) {
            fail("An exception occurred during test execution: " + e.getMessage());
        }
    }

    @Test
    public void testIntLikeSerializer() {
        NumberSerializers.IntLikeSerializer serializer = new NumberSerializers.IntLikeSerializer();
        Number value = 30;
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);
        SerializerProvider provider = new DefaultSerializerProvider.Impl().createInstance();
        try {
            serializer.serialize(value, generator, provider);
            generator.close();
            assertEquals(writer.toString(), "30");
        } catch (IOException e) {
            fail("An exception occurred during test execution: " + e.getMessage());
        }
    }

    @Test
    public void testLongSerializer() {
        NumberSerializers.LongSerializer serializer = new NumberSerializers.LongSerializer(Long.class);
        Long value = Long.valueOf("40");
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);
        SerializerProvider provider = new DefaultSerializerProvider.Impl().createInstance();
        try {
            serializer.serialize(value, generator, provider);
            generator.close();
            assertEquals(writer.toString(), "40");
        } catch (IOException e) {
            fail("An exception occurred during test execution: " + e.getMessage());
        }
    }

    @Test
    public void testFloatSerializer() {
        NumberSerializers.FloatSerializer serializer = new NumberSerializers.FloatSerializer();
        Float value = Float.valueOf("50.0");
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);
        SerializerProvider provider = new DefaultSerializerProvider.Impl().createInstance();
        try {
            serializer.serialize(value, generator, provider);
            generator.close();
            assertEquals(writer.toString(), "50.0");
        } catch (IOException e) {
            fail("An exception occurred during test execution: " + e.getMessage());
        }
    }

    @Test
    public void testDoubleSerializer() {
        NumberSerializers.DoubleSerializer serializer = new NumberSerializers.DoubleSerializer(Double.class);
        Double value = Double.valueOf("60.0");
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);
        SerializerProvider provider = new DefaultSerializerProvider.Impl().createInstance();
        try {
            serializer.serialize(value, generator, provider);
            generator.close();
            assertEquals(writer.toString(), "60.0");
        } catch (IOException e) {
            fail("An exception occurred during test execution: " + e.getMessage());
        }
    }
}