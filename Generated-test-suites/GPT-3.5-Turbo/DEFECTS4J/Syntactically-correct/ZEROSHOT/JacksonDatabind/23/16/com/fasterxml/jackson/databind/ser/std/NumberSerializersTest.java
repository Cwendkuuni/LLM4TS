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
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonIntegerFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonNumberFormatVisitor;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

public class NumberSerializersTest {

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
        NumberSerializers.addAll(allDeserializers);
        
        Assert.assertTrue(allDeserializers.containsKey(Integer.class.getName()));
        Assert.assertTrue(allDeserializers.containsKey(Integer.TYPE.getName()));
        Assert.assertTrue(allDeserializers.containsKey(Long.class.getName()));
        Assert.assertTrue(allDeserializers.containsKey(Long.TYPE.getName()));
        Assert.assertTrue(allDeserializers.containsKey(Byte.class.getName()));
        Assert.assertTrue(allDeserializers.containsKey(Byte.TYPE.getName()));
        Assert.assertTrue(allDeserializers.containsKey(Short.class.getName()));
        Assert.assertTrue(allDeserializers.containsKey(Short.TYPE.getName()));
        Assert.assertTrue(allDeserializers.containsKey(Float.class.getName()));
        Assert.assertTrue(allDeserializers.containsKey(Float.TYPE.getName()));
        Assert.assertTrue(allDeserializers.containsKey(Double.class.getName()));
        Assert.assertTrue(allDeserializers.containsKey(Double.TYPE.getName()));
    }

    @Test
    public void testShortSerializerSerialize() throws IOException {
        ShortSerializer serializer = new ShortSerializer();

        StubJsonGenerator gen = new StubJsonGenerator();
        SerializerProvider provider = new StubSerializerProvider();

        serializer.serialize((short) 10, gen, provider);

        Assert.assertEquals((short) 10, gen.getLastNumber());
    }

    @Test
    public void testIntegerSerializerSerialize() throws IOException {
        IntegerSerializer serializer = new IntegerSerializer();

        StubJsonGenerator gen = new StubJsonGenerator();
        SerializerProvider provider = new StubSerializerProvider();

        serializer.serialize(10, gen, provider);

        Assert.assertEquals(10, gen.getLastNumber());
    }

    @Test
    public void testIntegerSerializerSerializeWithType() throws IOException {
        IntegerSerializer serializer = new IntegerSerializer();

        StubJsonGenerator gen = new StubJsonGenerator();
        SerializerProvider provider = new StubSerializerProvider();
        TypeSerializer typeSer = new StubTypeSerializer();

        serializer.serializeWithType(10, gen, provider, typeSer);

        Assert.assertEquals(10, gen.getLastNumber());
    }

    @Test
    public void testIntLikeSerializerSerialize() throws IOException {
        IntLikeSerializer serializer = new IntLikeSerializer();

        StubJsonGenerator gen = new StubJsonGenerator();
        SerializerProvider provider = new StubSerializerProvider();

        serializer.serialize(10, gen, provider);

        Assert.assertEquals(10, gen.getLastNumber());
    }

    @Test
    public void testLongSerializerSerialize() throws IOException {
        LongSerializer serializer = new LongSerializer();

        StubJsonGenerator gen = new StubJsonGenerator();
        SerializerProvider provider = new StubSerializerProvider();

        serializer.serialize(10L, gen, provider);

        Assert.assertEquals(10L, gen.getLastNumber());
    }

    @Test
    public void testFloatSerializerSerialize() throws IOException {
        FloatSerializer serializer = new FloatSerializer();

        StubJsonGenerator gen = new StubJsonGenerator();
        SerializerProvider provider = new StubSerializerProvider();

        serializer.serialize(10F, gen, provider);

        Assert.assertEquals(10F, gen.getLastNumber());
    }

    @Test
    public void testDoubleSerializerSerialize() throws IOException {
        DoubleSerializer serializer = new DoubleSerializer();

        StubJsonGenerator gen = new StubJsonGenerator();
        SerializerProvider provider = new StubSerializerProvider();

        serializer.serialize(10D, gen, provider);

        Assert.assertEquals(10D, gen.getLastNumber());
    }

    private static class StubJsonGenerator extends JsonGenerator {
        private Number lastNumber;

        @Override
        public void writeNumber(int v) throws IOException {
            lastNumber = v;
        }

        @Override
        public void writeNumber(long v) throws IOException {
            lastNumber = v;
        }

        @Override
        public void writeNumber(BigInteger v) throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public void writeNumber(double v) throws IOException {
            lastNumber = v;
        }

        @Override
        public void writeNumber(float v) throws IOException {
            lastNumber = v;
        }

        @Override
        public void writeNumber(BigDecimal v) throws IOException {
            throw new UnsupportedOperationException();
        }

        public Number getLastNumber() {
            return lastNumber;
        }
    }

    private static class StubSerializerProvider extends SerializerProvider {
        @Override
        public void setDefaultKeySerializer(JsonSerializer<?> jsonSerializer) {
        }

        @Override
        public void setNullKeySerializer(JsonSerializer<Object> jsonSerializer) {
        }
    }

    private static class StubTypeSerializer extends TypeSerializer {
        @Override
        public TypeIdResolver getTypeIdResolver() {
            return null;
        }

        @Override
        public JsonTypeInfo.As getTypeInclusion() {
            return null;
        }

        @Override
        public JsonTypeInfo.As externalTypeId() {
            return null;
        }

        @Override
        public void writeTypePrefixForScalar(Object o, JsonGenerator jsonGenerator) throws IOException {

        }

        @Override
        public void writeTypePrefixForObject(Object o, JsonGenerator jsonGenerator) throws IOException {

        }

        @Override
        public void writeTypePrefixForArray(Object o, JsonGenerator jsonGenerator) throws IOException {

        }

        @Override
        public void writeTypeSuffixForScalar(Object o, JsonGenerator jsonGenerator) throws IOException {

        }

        @Override
        public void writeTypeSuffixForObject(Object o, JsonGenerator jsonGenerator) throws IOException {

        }

        @Override
        public void writeTypeSuffixForArray(Object o, JsonGenerator jsonGenerator) throws IOException {

        }

        @Override
        public void writeTypePrefixForScalar(Object o, JsonGenerator jsonGenerator, Class<?> aClass) throws IOException {

        }

        @Override
        public void writeTypePrefixForObject(Object o, JsonGenerator jsonGenerator, Class<?> aClass) throws IOException {

        }

        @Override
        public void writeTypePrefixForArray(Object o, JsonGenerator jsonGenerator, Class<?> aClass) throws IOException {

        }

        @Override
        public void writeTypeSuffixForScalar(Object o, JsonGenerator jsonGenerator, Class<?> aClass) throws IOException {

        }

        @Override
        public void writeTypeSuffixForObject(Object o, JsonGenerator jsonGenerator, Class<?> aClass) throws IOException {

        }

        @Override
        public void writeTypeSuffixForArray(Object o, JsonGenerator jsonGenerator, Class<?> aClass) throws IOException {

        }

        @Override
        public void writeCustomTypePrefixForScalar(Object o, JsonGenerator jsonGenerator, String s) throws IOException {

        }

        @Override
        public void writeCustomTypePrefixForObject(Object o, JsonGenerator jsonGenerator, String s) throws IOException {

        }

        @Override
        public void writeCustomTypePrefixForArray(Object o, JsonGenerator jsonGenerator, String s) throws IOException {

        }

        @Override
        public void writeCustomTypeSuffixForScalar(Object o, JsonGenerator jsonGenerator, String s) throws IOException {

        }

        @Override
        public void writeCustomTypeSuffixForObject(Object o, JsonGenerator jsonGenerator, String s) throws IOException {

        }

        @Override
        public void writeCustomTypeSuffixForArray(Object o, JsonGenerator jsonGenerator, String s) throws IOException {

        }

        @Override
        public void writeTypePrefixForScalar(Object o, JsonGenerator jsonGenerator, String s) throws IOException {

        }

        @Override
        public void writeTypePrefixForObject(Object o, JsonGenerator jsonGenerator, String s) throws IOException {

        }

        @Override
        public void writeTypePrefixForArray(Object o, JsonGenerator jsonGenerator, String s) throws IOException {

        }

        @Override
        public void writeTypeSuffixForScalar(Object o, JsonGenerator jsonGenerator, String s) throws IOException {

        }

        @Override
        public void writeTypeSuffixForObject(Object o, JsonGenerator jsonGenerator, String s) throws IOException {

        }

        @Override
        public void writeTypeSuffixForArray(Object o, JsonGenerator jsonGenerator, String s) throws IOException {

        }
    }
}