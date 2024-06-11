package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;
import org.junit.*;
import org.mockito.*;

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

public class NumberSerializersTest {

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = Mockito.mock(Map.class);

        NumberSerializers.addAll(allDeserializers);

        Mockito.verify(allDeserializers, Mockito.times(1)).put(Integer.class.getName(), IntegerSerializer.instance);
        Mockito.verify(allDeserializers, Mockito.times(1)).put(Integer.TYPE.getName(), IntegerSerializer.instance);
        Mockito.verify(allDeserializers, Mockito.times(1)).put(Long.class.getName(), LongSerializer.instance);
        Mockito.verify(allDeserializers, Mockito.times(1)).put(Long.TYPE.getName(), LongSerializer.instance);
        Mockito.verify(allDeserializers, Mockito.times(1)).put(Byte.class.getName(), IntLikeSerializer.instance);
        Mockito.verify(allDeserializers, Mockito.times(1)).put(Byte.TYPE.getName(), IntLikeSerializer.instance);
        Mockito.verify(allDeserializers, Mockito.times(1)).put(Short.class.getName(), ShortSerializer.instance);
        Mockito.verify(allDeserializers, Mockito.times(1)).put(Short.TYPE.getName(), ShortSerializer.instance);
        Mockito.verify(allDeserializers, Mockito.times(1)).put(Float.class.getName(), FloatSerializer.instance);
        Mockito.verify(allDeserializers, Mockito.times(1)).put(Float.TYPE.getName(), FloatSerializer.instance);
        Mockito.verify(allDeserializers, Mockito.times(1)).put(Double.class.getName(), DoubleSerializer.instance);
        Mockito.verify(allDeserializers, Mockito.times(1)).put(Double.TYPE.getName(), DoubleSerializer.instance);
    }

    @JacksonStdImpl
    public final static class ShortSerializerTest extends NumberSerializers.Base<Short>
    {
        public ShortSerializerTest() { super(Short.class, JsonParser.NumberType.INT, "number"); }

        @Override
        public void serialize(Short value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeNumber(value.shortValue());
        }
    }

    @Test
    public void testSerialize_short() throws IOException {
        Short value = 100;
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);

        ShortSerializerTest serializer = new ShortSerializerTest();
        serializer.serialize(value, gen, provider);

        Mockito.verify(gen, Mockito.times(1)).writeNumber(value.shortValue());
    }

    @JacksonStdImpl
    public final static class IntegerSerializerTest extends NumberSerializers.Base<Object>
    {
        public IntegerSerializerTest() { super(Integer.class, JsonParser.NumberType.INT ,"integer"); }

        @Override
        public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeNumber(((Integer) value).intValue());
        }

        @Override
        public void serializeWithType(Object value, JsonGenerator gen,
                SerializerProvider provider, TypeSerializer typeSer) throws IOException {
            serialize(value, gen, provider);
        }
    }

    @Test
    public void testSerialize_integer() throws IOException {
        Integer value = 100;
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);

        IntegerSerializerTest serializer = new IntegerSerializerTest();
        serializer.serialize(value, gen, provider);

        Mockito.verify(gen, Mockito.times(1)).writeNumber(value.intValue());
    }

    @Test
    public void testSerializeWithType_integer() throws IOException {

        Integer value = 100;
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        TypeSerializer typeSer = Mockito.mock(TypeSerializer.class);

        IntegerSerializerTest serializer = new IntegerSerializerTest();
        serializer.serializeWithType(value, gen, provider, typeSer);

        Mockito.verify(serializer, Mockito.times(1)).serialize(value, gen, provider);
    }

    @JacksonStdImpl
    public final static class IntLikeSerializerTest extends NumberSerializers.Base<Number>
    {
        public IntLikeSerializerTest() {
            super(Number.class, JsonParser.NumberType.INT, "integer");
        }

        @Override
        public void serialize(Number value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeNumber(value.intValue());
        }
    }

    @Test
    public void testSerialize_intlike() throws IOException {
        IntLikeSerializerTest serializer = new IntLikeSerializerTest();
        Number value = 100.5;
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);

        serializer.serialize(value, gen, provider);

        Mockito.verify(gen, Mockito.times(1)).writeNumber(value.intValue());
    }

    @JacksonStdImpl
    public final static class LongSerializerTest extends NumberSerializers.Base<Object>
    {

        public LongSerializerTest() { super(Long.class, JsonParser.NumberType.LONG, "number"); }

        @Override
        public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeNumber(((Long) value).longValue());
        }
    }

    @Test
    public void testSerialize_long() throws IOException {
        Long value = 100L;
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);

        LongSerializerTest serializer = new LongSerializerTest();
        serializer.serialize(value, gen, provider);

        Mockito.verify(gen, Mockito.times(1)).writeNumber(value.longValue());
    }

    @JacksonStdImpl
    public final static class FloatSerializerTest extends NumberSerializers.Base<Object>
    {

        public FloatSerializerTest() { super(Float.class, JsonParser.NumberType.FLOAT, "number"); }

        @Override
        public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeNumber(((Float) value).floatValue());
        }
    }

    @Test
    public void testSerialize_float() throws IOException {
        Float value = 100.5f;
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);

        FloatSerializerTest serializer = new FloatSerializerTest();
        serializer.serialize(value, gen, provider);

        Mockito.verify(gen, Mockito.times(1)).writeNumber(value.floatValue());
    }

    @JacksonStdImpl
    public final static class DoubleSerializerTest extends NumberSerializers.Base<Object>
    {

        public DoubleSerializerTest() { super(Double.class, JsonParser.NumberType.DOUBLE, "number"); }

        @Override
        public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeNumber(((Double) value).doubleValue());
        }

        @Override
        public void serializeWithType(Object value, JsonGenerator gen,
                SerializerProvider provider, TypeSerializer typeSer) throws IOException {
            serialize(value, gen, provider);
        }
    }

    @Test
    public void testSerialize_double() throws IOException {
        Double value = 100.5;
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);

        DoubleSerializerTest serializer = new DoubleSerializerTest();
        serializer.serialize(value, gen, provider);

        Mockito.verify(gen, Mockito.times(1)).writeNumber(value.doubleValue());
    }

    @Test
    public void testSerializeWithType_double() throws IOException {
        Double value = 100.5;
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        TypeSerializer typeSer = Mockito.mock(TypeSerializer.class);

        DoubleSerializerTest serializer = new DoubleSerializerTest();
        serializer.serializeWithType(value, gen, provider, typeSer);

        Mockito.verify(serializer, Mockito.times(1)).serialize(value, gen, provider);
    }

    @Test
    public void testShortSerializerConstructor() {
        ShortSerializerTest serializer = new ShortSerializerTest();
        assertEquals(Short.class, serializer.handledType());
        assertEquals(JsonParser.NumberType.INT, serializer._numberType);
        assertEquals("number", serializer._schemaType);
    }

    @Test
    public void testIntegerSerializerConstructor() {
        IntegerSerializerTest serializer = new IntegerSerializerTest();
        assertEquals(Integer.class, serializer.handledType());
        assertEquals(JsonParser.NumberType.INT, serializer._numberType);
        assertEquals("integer", serializer._schemaType);
    }

    @Test
    public void testIntLikeSerializerConstructor() {
        IntLikeSerializerTest serializer = new IntLikeSerializerTest();
        assertEquals(Number.class, serializer.handledType());
        assertEquals(JsonParser.NumberType.INT, serializer._numberType);
        assertEquals("integer", serializer._schemaType);
    }

    @Test
    public void testLongSerializerConstructor() {
        LongSerializerTest serializer = new LongSerializerTest();
        assertEquals(Long.class, serializer.handledType());
        assertEquals(JsonParser.NumberType.LONG, serializer._numberType);
        assertEquals("number", serializer._schemaType);
    }

    @Test
    public void testFloatSerializerConstructor() {
        FloatSerializerTest serializer = new FloatSerializerTest();
        assertEquals(Float.class, serializer.handledType());
        assertEquals(JsonParser.NumberType.FLOAT, serializer._numberType);
        assertEquals("number", serializer._schemaType);
    }

    @Test
    public void testDoubleSerializerConstructor() {
        DoubleSerializerTest serializer = new DoubleSerializerTest();
        assertEquals(Double.class, serializer.handledType());
        assertEquals(JsonParser.NumberType.DOUBLE, serializer._numberType);
        assertEquals("number", serializer._schemaType);
    }
}