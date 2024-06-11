package com.fasterxml.jackson.databind.ser.std;

import org.junit.Test;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;
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
        Map<String, JsonSerializer<?>> allDeserializers = new Map<String, JsonSerializer<?>>();
        NumberSerializers.addAll(allDeserializers);
        assert ( allDeserializers.get(Integer.class.getName()).equals(new IntegerSerializer()) );
        assert ( allDeserializers.get(Integer.TYPE.getName()).equals(new IntegerSerializer()) );
        assert ( allDeserializers.get(Long.class.getName()).equals(LongSerializer.instance) );
        assert ( allDeserializers.get(Long.TYPE.getName()).equals(LongSerializer.instance) );
        assert ( allDeserializers.get(Byte.class.getName()).equals(IntLikeSerializer.instance) );
        assert ( allDeserializers.get(Byte.TYPE.getName()).equals(IntLikeSerializer.instance) );
        assert ( allDeserializers.get(Short.class.getName()).equals(ShortSerializer.instance) );
        assert ( allDeserializers.get(Short.TYPE.getName()).equals(ShortSerializer.instance) );
        assert ( allDeserializers.get(Float.class.getName()).equals(FloatSerializer.instance) );
        assert ( allDeserializers.get(Float.TYPE.getName()).equals(FloatSerializer.instance) );
        assert ( allDeserializers.get(Double.class.getName()).equals(DoubleSerializer.instance) );
        assert ( allDeserializers.get(Double.TYPE.getName()).equals(DoubleSerializer.instance) );
    }

    @Test
    public void testShortSerializer() {
        ShortSerializer shortSerializer = new ShortSerializer();
        assert ( shortSerializer.handledType().equals(Short.class) );
        assert ( shortSerializer.getNumberType().equals(JsonParser.NumberType.INT) );
        assert ( shortSerializer.getSchema(null, null).equals(NumberSerializers.createSchemaNode("number", true)) );
    }

    @Test
    public void testShortSerializerSerialize() {
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        int expected = 1;
        Short value = new Short(expected);
        ShortSerializer shortSerializer = new ShortSerializer();
        try {
            shortSerializer.serialize(value, gen, provider);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testIntegerSerializer() {
        IntegerSerializer integerSerializer = new IntegerSerializer();
        assert ( integerSerializer.handledType().equals(Integer.class) );
        assert ( integerSerializer.getNumberType().equals(JsonParser.NumberType.INT) );
        assert ( integerSerializer.getSchema(null, null).equals(NumberSerializers.createSchemaNode("integer", true)) );
    }

    @Test
    public void testIntegerSerializerSerialize() {
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        int expected = 1;
        Integer value = new Integer(expected);
        IntegerSerializer integerSerializer = new IntegerSerializer();
        try {
            integerSerializer.serialize(value, gen, provider);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testIntegerSerializerSerializeWithType() {
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        TypeSerializer typeSer = null;
        int expected = 1;
        Integer value = new Integer(expected);
        IntegerSerializer integerSerializer = new IntegerSerializer();
        try {
            integerSerializer.serializeWithType(value, gen, provider, typeSer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testIntLikeSerializer() {
        IntLikeSerializer intLikeSerializer = new IntLikeSerializer();
        assert ( intLikeSerializer.handledType().equals(Number.class) );
        assert ( intLikeSerializer.getNumberType().equals(JsonParser.NumberType.INT) );
        assert ( intLikeSerializer.getSchema(null, null).equals(NumberSerializers.createSchemaNode("integer", true)) );
    }

    @Test
    public void testIntLikeSerializerSerialize() {
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        int expected = 1;
        Number value = new Number(expected);
        IntLikeSerializer intLikeSerializer = new IntLikeSerializer();
        try {
            intLikeSerializer.serialize(value, gen, provider);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLongSerializer() {
        LongSerializer longSerializer = new LongSerializer();
        assert ( longSerializer.handledType().equals(Long.class) );
        assert ( longSerializer.getNumberType().equals(JsonParser.NumberType.LONG) );
        assert ( longSerializer.getSchema(null, null).equals(NumberSerializers.createSchemaNode("number", true)) );
    }

    @Test
    public void testLongSerializerSerialize() {
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        long expected = 1L;
        Long value = new Long(expected);
        LongSerializer longSerializer = new LongSerializer();
        try {
            longSerializer.serialize(value, gen, provider);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFloatSerializer() {
        FloatSerializer floatSerializer = new FloatSerializer();
        assert ( floatSerializer.handledType().equals(Float.class) );
        assert ( floatSerializer.getNumberType().equals(JsonParser.NumberType.FLOAT) );
        assert ( floatSerializer.getSchema(null, null).equals(NumberSerializers.createSchemaNode("number", true)) );
    }

    @Test
    public void testFloatSerializerSerialize() {
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        float expected = 1f;
        Float value = new Float(expected);
        FloatSerializer floatSerializer = new FloatSerializer();
        try {
            floatSerializer.serialize(value, gen, provider);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDoubleSerializer() {
        DoubleSerializer doubleSerializer = new DoubleSerializer();
        assert ( doubleSerializer.handledType().equals(Double.class) );
        assert ( doubleSerializer.getNumberType().equals(JsonParser.NumberType.DOUBLE) );
        assert ( doubleSerializer.getSchema(null, null).equals(NumberSerializers.createSchemaNode("number", true)) );
    }

    @Test
    public void testDoubleSerializerSerialize() {
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        double expected = 1.0d;
        Double value = new Double(expected);
        DoubleSerializer doubleSerializer = new DoubleSerializer();
        try {
            doubleSerializer.serialize(value, gen, provider);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDoubleSerializerSerializeWithType() {
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        TypeSerializer typeSer = null;
        double expected = 1.0d;
        Double value = new Double(expected);
        DoubleSerializer doubleSerializer = new DoubleSerializer();
        try {
            doubleSerializer.serializeWithType(value, gen, provider, typeSer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}