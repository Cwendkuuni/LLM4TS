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
import org.junit.Test;
import org.junit.Before;

import static org.junit.Assert.*;

public class StdDelegatingSerializerTest {

    private StdDelegatingSerializer serializer;

    @Before
    public void setUp() {
        serializer = new StdDelegatingSerializer(new TestConverter());
    }

    @Test
    public void testResolve() throws JsonMappingException {
        SerializerProvider provider = new DefaultSerializerProvider.Impl();
        serializer.resolve(provider);
        assertNotNull(serializer.getDelegatee());
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        SerializerProvider provider = new DefaultSerializerProvider.Impl();
        BeanProperty property = new BeanProperty.Std(null, null, null, null, null, null, null, null, null);
        JsonSerializer<?> result = serializer.createContextual(provider, property);
        assertNotNull(result);
    }

    @Test
    public void testSerialize() throws IOException {
        Object value = new Object();
        JsonGenerator gen = null;
        SerializerProvider provider = new DefaultSerializerProvider.Impl();
        serializer.serialize(value, gen, provider);
        // verify the result
    }

    @Test
    public void testSerializeWithType() throws IOException {
        Object value = new Object();
        JsonGenerator gen = null;
        SerializerProvider provider = new DefaultSerializerProvider.Impl();
        TypeSerializer typeSer = null;
        serializer.serializeWithType(value, gen, provider, typeSer);
        // verify the result
    }

    @Test
    public void testIsEmpty() {
        Object value = new Object();
        assertTrue(serializer.isEmpty(value));
    }

    @Test
    public void testIsEmptyWithProvider() {
        SerializerProvider prov = new DefaultSerializerProvider.Impl();
        Object value = new Object();
        assertTrue(serializer.isEmpty(prov, value));
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        SerializerProvider provider = new DefaultSerializerProvider.Impl();
        Type typeHint = null;
        JsonNode result = serializer.getSchema(provider, typeHint);
        // verify the result
    }

    @Test
    public void testGetSchemaWithOptional() throws JsonMappingException {
        SerializerProvider provider = new DefaultSerializerProvider.Impl();
        Type typeHint = null;
        boolean isOptional = false;
        JsonNode result = serializer.getSchema(provider, typeHint, isOptional);
        // verify the result
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = null;
        serializer.acceptJsonFormatVisitor(visitor, null);
        // verify the result
    }

    private class TestConverter implements Converter<Object, Object> {

        @Override
        public Object convert(Object value) {
            return null;
        }

        @Override
        public JavaType getInputType(TypeFactory typeFactory) {
            return null;
        }

        @Override
        public JavaType getOutputType(TypeFactory typeFactory) {
            return null;
        }
    }
}