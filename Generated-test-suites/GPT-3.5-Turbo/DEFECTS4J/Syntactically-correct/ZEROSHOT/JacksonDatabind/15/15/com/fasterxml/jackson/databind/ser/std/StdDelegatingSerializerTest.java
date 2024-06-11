package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitable;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.ResolvableSerializer;
import com.fasterxml.jackson.databind.util.Converter;

import java.io.IOException;
import java.lang.reflect.Type;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdDelegatingSerializerTest {

    private StdDelegatingSerializer serializer;

    @Before
    public void setUp() throws Exception {
        Converter<Object, Object> converter = new Converter<Object, Object>() {
            @Override
            public Object convert(Object value) {
                return value;
            }
        };
        serializer = new StdDelegatingSerializer(converter);
    }

    @Test
    public void testSerialize() throws Exception {
        JsonGenerator gen = null;
        SerializerProvider provider = null;

        // Test 1
        Object value1 = null;
        serializer.serialize(value1, gen, provider);

        // Test 2
        Object value2 = new Object();
        serializer.serialize(value2, gen, provider);
    }

    @Test
    public void testSerializeWithType() throws Exception {
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        TypeSerializer typeSer = null;

        // Test 1
        Object value1 = null;
        serializer.serializeWithType(value1, gen, provider, typeSer);

        // Test 2
        Object value2 = new Object();
        serializer.serializeWithType(value2, gen, provider, typeSer);
    }

    @Test
    public void testIsEmpty() throws Exception {
        Object value = null;

        // Test 1
        boolean result1 = serializer.isEmpty(value);
        assertTrue(result1);

        // Test 2
        Object value2 = new Object();
        boolean result2 = serializer.isEmpty(value2);
        assertFalse(result2);
    }

    @Test
    public void testIsEmptyWithProv() throws Exception {
        SerializerProvider prov = null;
        Object value = null;

        // Test 1
        boolean result1 = serializer.isEmpty(prov, value);
        assertTrue(result1);

        // Test 2
        Object value2 = new Object();
        boolean result2 = serializer.isEmpty(prov, value2);
        assertFalse(result2);
    }

    @Test
    public void testGetSchema() throws Exception {
        SerializerProvider provider = null;
        Type typeHint = null;

        // Test 1
        JsonNode result1 = serializer.getSchema(provider, typeHint);

        // Test 2
        boolean isOptional = false;
        JsonNode result2 = serializer.getSchema(provider, typeHint, isOptional);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws Exception {
        JsonFormatVisitorWrapper visitor = null;
        JavaType typeHint = null;

        // Test 1
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
    }

    @Test
    public void testConvertValue() throws Exception {
        Object value = new Object();

        // Test 1
        Object result1 = serializer.convertValue(value);
        assertEquals(value, result1);
    }
}