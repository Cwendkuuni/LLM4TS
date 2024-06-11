package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.IntegerSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.ShortSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.IntLikeSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.LongSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.FloatSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.DoubleSerializer;

import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class NumberSerializersTest {

    @Test
    public void testShortSerialize() {
        ShortSerializer serializer = new ShortSerializer();
        Short value = 10;
        JsonGenerator generator = null;
        SerializerProvider provider = null;

        try {
            serializer.serialize(value, generator, provider);
        } catch (IOException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testIntegerSerialize() {
        IntegerSerializer serializer = new IntegerSerializer(Integer.class);
        Integer value = 10;
        JsonGenerator generator = null;
        SerializerProvider provider = null;

        try {
            serializer.serialize(value, generator, provider);
        } catch (IOException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testIntLikeSerialize() {
        IntLikeSerializer serializer = IntLikeSerializer.instance;
        Number value = 10;
        JsonGenerator generator = null;
        SerializerProvider provider = null;

        try {
            serializer.serialize(value, generator, provider);
        } catch (IOException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testLongSerialize() {
        LongSerializer serializer = new LongSerializer(Long.class);
        Long value = 10L;
        JsonGenerator generator = null;
        SerializerProvider provider = null;

        try {
            serializer.serialize(value, generator, provider);
        } catch (IOException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testFloatSerialize() {
        FloatSerializer serializer = FloatSerializer.instance;
        Float value = 10.0F;
        JsonGenerator generator = null;
        SerializerProvider provider = null;

        try {
            serializer.serialize(value, generator, provider);
        } catch (IOException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testDoubleSerialize() {
        DoubleSerializer serializer = new DoubleSerializer(Double.class);
        Double value = 10.0;
        JsonGenerator generator = null;
        SerializerProvider provider = null;

        try {
            serializer.serialize(value, generator, provider);
        } catch (IOException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testBaseGetSchema() {
        NumberSerializers.Base base = new NumberSerializers.Base(Integer.class, JsonParser.NumberType.INT, "integer");
        SerializerProvider provider = null;
        JavaType typeHint = null;

        JsonNode result = base.getSchema(provider, typeHint);
        assertNotNull(result);
    }

    @Test
    public void testBaseAcceptJsonFormatVisitor() {
        NumberSerializers.Base base = new NumberSerializers.Base(Integer.class, JsonParser.NumberType.INT, "integer");
        JsonFormatVisitorWrapper visitor = null;
        JavaType typeHint = null;

        try {
            base.acceptJsonFormatVisitor(visitor, typeHint);
        } catch (JsonMappingException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testBaseCreateContextual() {
        NumberSerializers.Base base = new NumberSerializers.Base(Integer.class, JsonParser.NumberType.INT, "number");
        SerializerProvider prov = null;
        BeanProperty property = null;

        try {
            base.createContextual(prov, property);
        } catch (JsonMappingException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testNumberSerializersAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
        NumberSerializers.addAll(allDeserializers);

        assertEquals(8, allDeserializers.size());
    }

}