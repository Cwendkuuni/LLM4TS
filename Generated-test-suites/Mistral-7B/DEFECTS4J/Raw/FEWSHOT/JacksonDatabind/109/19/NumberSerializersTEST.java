package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.Base;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.IntLikeSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.ShortSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.IntegerSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.LongSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.FloatSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.DoubleSerializer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.util.Map;

public class NumberSerializersTest {

    @Test
    public void testShortSerializer() {
        ShortSerializer shortSerializer = new ShortSerializer();
        assertEquals(Short.parseShort("123"), shortSerializer.deserialize(shortSerializer.serialize(Short.parseShort("123")), null));
    }

    @Test
    public void testIntegerSerializer() {
        IntegerSerializer integerSerializer = new IntegerSerializer(Integer.class);
        assertEquals(Integer.parseInt("456"), integerSerializer.deserialize(integerSerializer.serialize(Integer.parseInt("456")), null));
    }

    @Test
    public void testIntLikeSerializer() {
        IntLikeSerializer intLikeSerializer = new IntLikeSerializer();
        assertEquals(Integer.parseInt("789"), intLikeSerializer.deserialize(intLikeSerializer.serialize(new BigInteger("789")), null));
    }

    @Test
    public void testLongSerializer() {
        LongSerializer longSerializer = new LongSerializer(Long.class);
        assertEquals(Long.parseLong("123456789"), longSerializer.deserialize(longSerializer.serialize(Long.parseLong("123456789")), null));
    }

    @Test
    public void testFloatSerializer() {
        FloatSerializer floatSerializer = new FloatSerializer();
        assertEquals(3.14159265f, floatSerializer.deserialize(floatSerializer.serialize(3.14159265f), null), 0.001f);
    }

    @Test
    public void testDoubleSerializer() {
        DoubleSerializer doubleSerializer = new DoubleSerializer(Double.class);
        assertEquals(1.234567891234d, doubleSerializer.deserialize(doubleSerializer.serialize(1.234567891234d), null), 0.000000001);
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        Base<Object> shortSerializer = new ShortSerializer();
        JsonFormat format = new JsonFormat();
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(format, null);
        shortSerializer.acceptJsonFormatVisitor(visitor, null);
        assertEquals("number", visitor.getFormatType());
        assertEquals(JsonFormat.Feature.WRITE_BIGDECIMAL_AS_PLAIN, format.getFeatures().get(JsonFormat.Feature.WRITE_BIGDECIMAL_AS_PLAIN));
        assertEquals(JsonFormat.Feature.WRITE_INT_AS_STRING, format.getFeatures().get(JsonFormat.Feature.WRITE_INT_AS_STRING));

        Base<Object> intLikeSerializer = new IntLikeSerializer();
        format = new JsonFormat();
        visitor = new JsonFormatVisitorWrapper(format, null);
        intLikeSerializer.acceptJsonFormatVisitor(visitor, null);
        assertEquals("number", visitor.getFormatType());
        assertEquals(JsonFormat.Feature.WRITE_BIGDECIMAL_AS_PLAIN, format.getFeatures().get(JsonFormat.Feature.WRITE_BIGDECIMAL_AS_PLAIN));
        assertEquals(JsonFormat.Feature.WRITE_INT_AS_STRING, format.getFeatures().get(JsonFormat.Feature.WRITE_INT_AS_STRING));

        Base<Object> doubleSerializer = new DoubleSerializer(Double.class);
        format = new JsonFormat();
        visitor = new JsonFormatVisitorWrapper(format, null);
        doubleSerializer.acceptJsonFormatVisitor(visitor, null);
        assertEquals("number", visitor.getFormatType());
        assertEquals(JsonFormat.Feature.WRITE_BIGDECIMAL_AS_PLAIN, format.getFeatures().get(JsonFormat.Feature.WRITE_BIGDECIMAL_AS_PLAIN));
        assertNull(format.getFeatures().get(JsonFormat.Feature.WRITE_INT_AS_STRING));
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        JsonSerializer<?> serializer = new IntegerSerializer(Integer.class).createContextual(null, null);
        assertEquals(IntegerSerializer.class, serializer.getClass());

        serializer = new DoubleSerializer(Double.class).createContextual(null, null);
        assertEquals(DoubleSerializer.class, serializer.getClass());

        serializer = new IntLikeSerializer().createContextual(null, null);
        assertEquals(IntLikeSerializer.class, serializer.getClass());

        serializer = new ShortSerializer().createContextual(null, null);
        assertEquals(ShortSerializer.class, serializer.getClass());

        serializer = new LongSerializer(Long.class).createContextual(null, null);
        assertEquals(LongSerializer.class, serializer.getClass());

        serializer = new FloatSerializer().createContextual(null, null);
        assertEquals(FloatSerializer.class, serializer.getClass());
    }

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new java.util.HashMap<>();
        NumberSerializers.addAll(allDeserializers);
        assertEquals(NumberSerializers.IntegerSerializer.class, allDeserializers.get(Integer.class.getName()));
        assertEquals(NumberSerializers.LongSerializer.class, allDeserializers.get(Long.class.getName()));
        assertEquals(NumberSerializers.IntegerSerializer.class, allDeserializers.get(Integer.TYPE.getName()));
        assertEquals(NumberSerializers.LongSerializer.class, allDeserializers.get(Long.TYPE.getName()));
        assertEquals(NumberSerializers.IntLikeSerializer.class, allDeserializers.get(Byte.class.getName()));
        assertEquals(NumberSerializers.IntLikeSerializer.class, allDeserializers.get(Byte.TYPE.getName()));
        assertEquals(NumberSerializers.ShortSerializer.class, allDeserializers.get(Short.class.getName()));
        assertEquals(NumberSerializers.ShortSerializer.class, allDeserializers.get(Short.TYPE.getName()));
        assertEquals(NumberSerializers.FloatSerializer.class, allDeserializers.get(Float.class.getName()));
        assertEquals(NumberSerializers.FloatSerializer.class, allDeserializers.get(Float.TYPE.getName()));
        assertEquals(NumberSerializers.DoubleSerializer.class, allDeserializers.get(Double.class.getName()));
        assertEquals(NumberSerializers.DoubleSerializer.class, allDeserializers.get(Double.TYPE.getName()));
    }
}