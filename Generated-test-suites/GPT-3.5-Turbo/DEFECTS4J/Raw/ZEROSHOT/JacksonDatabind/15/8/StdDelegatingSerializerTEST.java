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
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;

import static org.junit.Assert.*;

public class StdDelegatingSerializerTest {

    private StdDelegatingSerializer serializer;

    @Before
    public void setup() {
        Converter<Object, Object> converter = null;
        JavaType delegateType = null;
        JsonSerializer<Object> delegateSerializer = null;
        serializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer);
    }

    @Test
    public void testResolve() {
        try {
            SerializerProvider provider = null;
            serializer.resolve(provider);
        } catch (JsonMappingException e) {
            e.printStackTrace();
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testCreateContextual() {
        try {
            SerializerProvider provider = null;
            BeanProperty property = null;
            JsonSerializer<?> result = serializer.createContextual(provider, property);
            assertNotNull(result);
        } catch (JsonMappingException e) {
            e.printStackTrace();
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testSerialize() {
        try {
            Object value = null;
            JsonGenerator gen = null;
            SerializerProvider provider = null;
            serializer.serialize(value, gen, provider);
        } catch (IOException e) {
            e.printStackTrace();
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testSerializeWithType() {
        try {
            Object value = null;
            JsonGenerator gen = null;
            SerializerProvider provider = null;
            TypeSerializer typeSer = null;
            serializer.serializeWithType(value, gen, provider, typeSer);
        } catch (IOException e) {
            e.printStackTrace();
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    @Deprecated
    public void testIsEmpty() {
        Object value = null;
        assertTrue(serializer.isEmpty(value));
    }

    @Test
    public void testIsEmpty2() {
        SerializerProvider prov = null;
        Object value = null;
        assertTrue(serializer.isEmpty(prov, value));
    }

    @Test
    public void testGetSchema() {
        try {
            SerializerProvider provider = null;
            Type typeHint = null;
            JsonNode result = serializer.getSchema(provider, typeHint);
            assertNull(result);
        } catch (JsonMappingException e) {
            e.printStackTrace();
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetSchema2() {
        try {
            SerializerProvider provider = null;
            Type typeHint = null;
            boolean isOptional = false;
            JsonNode result = serializer.getSchema(provider, typeHint, isOptional);
            assertNull(result);
        } catch (JsonMappingException e) {
            e.printStackTrace();
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        try {
            JsonFormatVisitorWrapper visitor = null;
            JavaType typeHint = null;
            serializer.acceptJsonFormatVisitor(visitor, typeHint);
        } catch (JsonMappingException e) {
            e.printStackTrace();
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testConvertValue() {
        Object value = null;
        Object result = serializer.convertValue(value);
        assertNull(result);
    }

    @Test
    public void testGetConverter() {
        Converter<Object, ?> result = serializer.getConverter();
        assertNull(result);
    }

    @Test
    public void testGetDelegatee() {
        JsonSerializer<?> result = serializer.getDelegatee();
        assertNull(result);
    }
}