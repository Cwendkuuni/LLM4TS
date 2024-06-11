package com.fasterxml.jackson.databind.ser.std;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.HashMap;
import java.util.Map;

public class NumberSerializersTest {

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
        NumberSerializers.addAll(allDeserializers);
        assertEquals(8, allDeserializers.size());
    }

    @Test
    public void testShortSerializerSerialize() throws Exception {
        NumberSerializers.ShortSerializer serializer = new NumberSerializers.ShortSerializer();
        MockJsonGenerator gen = new MockJsonGenerator();
        MockSerializerProvider provider = new MockSerializerProvider();
        serializer.serialize((short) 5, gen, provider);
    }

    @Test
    public void testIntegerSerializerSerialize() throws Exception {
        NumberSerializers.IntegerSerializer serializer = new NumberSerializers.IntegerSerializer(Integer.class);
        MockJsonGenerator gen = new MockJsonGenerator();
        MockSerializerProvider provider = new MockSerializerProvider();
        serializer.serialize(10, gen, provider);
    }

    @Test
    public void testIntegerSerializerSerializeWithType() throws Exception {
        NumberSerializers.IntegerSerializer serializer = new NumberSerializers.IntegerSerializer(Integer.class);
        MockJsonGenerator gen = new MockJsonGenerator();
        MockSerializerProvider provider = new MockSerializerProvider();
        serializer.serializeWithType(10, gen, provider, null);
    }

    @Test
    public void testIntLikeSerializerSerialize() throws Exception {
        NumberSerializers.IntLikeSerializer serializer = new NumberSerializers.IntLikeSerializer();
        MockJsonGenerator gen = new MockJsonGenerator();
        MockSerializerProvider provider = new MockSerializerProvider();
        serializer.serialize(15, gen, provider);
    }

    @Test
    public void testLongSerializerSerialize() throws Exception {
        NumberSerializers.LongSerializer serializer = new NumberSerializers.LongSerializer(Long.class);
        MockJsonGenerator gen = new MockJsonGenerator();
        MockSerializerProvider provider = new MockSerializerProvider();
        serializer.serialize((long) 20, gen, provider);
    }

    @Test
    public void testFloatSerializerSerialize() throws Exception {
        NumberSerializers.FloatSerializer serializer = new NumberSerializers.FloatSerializer();
        MockJsonGenerator gen = new MockJsonGenerator();
        MockSerializerProvider provider = new MockSerializerProvider();
        serializer.serialize((float) 25.5, gen, provider);
    }

    @Test
    public void testDoubleSerializerSerialize() throws Exception {
        NumberSerializers.DoubleSerializer serializer = new NumberSerializers.DoubleSerializer(Double.class);
        MockJsonGenerator gen = new MockJsonGenerator();
        MockSerializerProvider provider = new MockSerializerProvider();
        serializer.serialize((double) 30.5, gen, provider);
    }

    private class MockJsonGenerator extends JsonGenerator {
        public void writeNumber(short value) {}
        public void writeNumber(int value) {}
        public void writeNumber(long value) {}
        public void writeNumber(float value) {}
        public void writeNumber(double value) {}
        public void writeNumber(BigInteger value) {}
        public void writeNumber(BigDecimal value) {}
    }

    private class MockSerializerProvider extends SerializerProvider {
        public JsonGenerator generatorInstance(IOContext ctxt, int stdFeat, ObjectCodec codec, Writer out) { return null; }
        public void releaseGeneratorInstance(JsonGenerator gen) {}
    }
}