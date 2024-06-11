package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.*;

public class NumberSerializersTest {
    @Test
    public void testBaseGetSchema() {
        Base<Integer> base = new IntegerSerializer(Integer.class);
        SerializerProvider provider = new SerializerProvider();

        JsonNode schema = base.getSchema(provider, null);
        
        assertNotNull(schema);
        assertEquals("integer", schema.get("type").asText());
    }

    @Test
    public void testBaseAcceptJsonFormatVisitor() {
        Base<Integer> base = new IntegerSerializer(Integer.class);
        SerializerProvider provider = new SerializerProvider();
        JsonFormatVisitorWrapper visitor = null;
        JavaType typeHint = null;

        try {
            base.acceptJsonFormatVisitor(visitor, typeHint);
            assertTrue(true);
        } catch (Exception e) {
            fail("Exception occurred");
        }
    }

    @Test
    public void testBaseCreateContextual() {
        Base<Integer> base = new IntegerSerializer(Integer.class);
        SerializerProvider prov = new SerializerProvider();
        BeanProperty property = null;

        try {
            JsonSerializer<?> result = base.createContextual(prov, property);
            assertNotNull(result);
            assertEquals(base, result);
        } catch (Exception e) {
            fail("Exception occurred");
        }
    }

    @Test
    public void testShortSerialize() {
        ShortSerializer serializer = new ShortSerializer();
        JsonGenerator gen = null;
        SerializerProvider provider = new SerializerProvider();
        int value = 5;

        try {
            serializer.serialize(value, gen, provider);
            assertTrue(true);
        } catch (Exception e) {
            fail("Exception occurred");
        }
    }

    @Test
    public void testIntegerSerialize() {
        IntegerSerializer serializer = new IntegerSerializer(Integer.class);
        JsonGenerator gen = null;
        SerializerProvider provider = new SerializerProvider();
        int value = 10;

        try {
            serializer.serialize(value, gen, provider);
            assertTrue(true);
        } catch (Exception e) {
            fail("Exception occurred");
        }
    }

    @Test
    public void testIntegerSerializeWithType() {
        IntegerSerializer serializer = new IntegerSerializer(Integer.class);
        JsonGenerator gen = null;
        SerializerProvider provider = new SerializerProvider();
        TypeSerializer typeSer = null;
        int value = 15;

        try {
            serializer.serializeWithType(value, gen, provider, typeSer);
            assertTrue(true);
        } catch (Exception e) {
            fail("Exception occurred");
        }
    }

    @Test
    public void testIntLikeSerialize() {
        IntLikeSerializer serializer = new IntLikeSerializer();
        JsonGenerator gen = null;
        SerializerProvider provider = new SerializerProvider();
        Number value = 20;

        try {
            serializer.serialize(value, gen, provider);
            assertTrue(true);
        } catch (Exception e) {
            fail("Exception occurred");
        }
    }

    @Test
    public void testLongSerialize() {
        LongSerializer serializer = new LongSerializer(Long.class);
        JsonGenerator gen = null;
        SerializerProvider provider = new SerializerProvider();
        long value = 25L;

        try {
            serializer.serialize(value, gen, provider);
            assertTrue(true);
        } catch (Exception e) {
            fail("Exception occurred");
        }
    }

    @Test
    public void testFloatSerialize() {
        FloatSerializer serializer = new FloatSerializer();
        JsonGenerator gen = null;
        SerializerProvider provider = new SerializerProvider();
        float value = 3.14f;

        try {
            serializer.serialize(value, gen, provider);
            assertTrue(true);
        } catch (Exception e) {
            fail("Exception occurred");
        }
    }

    @Test
    public void testDoubleSerialize() {
        DoubleSerializer serializer = new DoubleSerializer(Double.class);
        JsonGenerator gen = null;
        SerializerProvider provider = new SerializerProvider();
        double value = 2.71828;

        try {
            serializer.serialize(value, gen, provider);
            assertTrue(true);
        } catch (Exception e) {
            fail("Exception occurred");
        }
    }
}