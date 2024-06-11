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
    public void setup() {
        Converter<Object, Object> converter = new Converter<Object, Object>() {
            @Override
            public Object convert(Object value) {
                return value;
            }
        };
        serializer = new StdDelegatingSerializer(converter);
    }

    @Test
    public void test_resolve() {
        SerializerProvider provider = null;
        try {
            serializer.resolve(provider);
        } catch (JsonMappingException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void test_createContextual() {
        SerializerProvider provider = null;
        BeanProperty property = null;
        try {
            JsonSerializer<?> result = serializer.createContextual(provider, property);
            assertNotNull(result);
        } catch (JsonMappingException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void test_serialize() {
        try {
            JsonGenerator gen = null;
            SerializerProvider provider = null;
            serializer.serialize(new Object(), gen, provider);
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void test_serializeWithType() {
        try {
            JsonGenerator gen = null;
            SerializerProvider provider = null;
            TypeSerializer typeSer = null;
            serializer.serializeWithType(new Object(), gen, provider, typeSer);
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void test_isEmpty() {
        Object value = new Object();
        assertFalse(serializer.isEmpty(value));
    }

    @Test
    public void test_isEmptyWithProv() {
        SerializerProvider prov = null;
        Object value = new Object();
        assertFalse(serializer.isEmpty(prov, value));
    }

    @Test
    public void test_getSchemaWithType() {
        SerializerProvider provider = null;
        Type typeHint = null;
        try {
            JsonNode result = serializer.getSchema(provider, typeHint);
            assertNotNull(result);
        } catch (JsonMappingException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void test_getSchemaWithTypeAndOpt() {
        SerializerProvider provider = null;
        Type typeHint = null;
        boolean isOptional = false;
        try {
            JsonNode result = serializer.getSchema(provider, typeHint, isOptional);
            assertNotNull(result);
        } catch (JsonMappingException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void test_acceptJsonFormatVisitor() {
        JsonFormatVisitorWrapper visitor = null;
        JavaType typeHint = null;
        try {
            serializer.acceptJsonFormatVisitor(visitor, typeHint);
        } catch (JsonMappingException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void test_convertValue() {
        Object value = new Object();
        Object result = serializer.convertValue(value);
        assertEquals(value, result);
    }
}