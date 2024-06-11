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

import org.junit.Assert;
import org.junit.Test;

public class StdDelegatingSerializerTest {

    @Test
    public void testSerialize() throws Exception {
        Converter<Object, String> converter = new Converter<Object, String>() {
            @Override
            public String convert(Object value) {
                return value.toString();
            }
        };

        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);
        JsonGenerator generator = null;
        SerializerProvider provider = null;

        // Test null value
        serializer.serialize(null, generator, provider);

        // Test non-null value
        serializer.serialize("test", generator, provider);
    }

    @Test
    public void testSerializeWithType() throws Exception {
        Converter<Object, String> converter = new Converter<Object, String>() {
            @Override
            public String convert(Object value) {
                return value.toString();
            }
        };

        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);
        JsonGenerator generator = null;
        SerializerProvider provider = null;
        TypeSerializer typeSer = null;

        // Test null value
        serializer.serializeWithType(null, generator, provider, typeSer);

        // Test non-null value
        serializer.serializeWithType("test", generator, provider, typeSer);
    }

    @Test
    public void testIsEmpty() throws Exception {
        Converter<Object, String> converter = new Converter<Object, String>() {
            @Override
            public String convert(Object value) {
                return value.toString();
            }
        };

        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);
        SerializerProvider provider = null;

        // Test null value
        boolean isEmpty = serializer.isEmpty(provider, null);
        Assert.assertTrue(isEmpty);

        // Test non-empty value
        isEmpty = serializer.isEmpty(provider, "test");
        Assert.assertFalse(isEmpty);
    }

    @Test
    public void testGetSchema() throws Exception {
        Converter<Object, String> converter = new Converter<Object, String>() {
            @Override
            public String convert(Object value) {
                return value.toString();
            }
        };

        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);
        SerializerProvider provider = null;
        Type typeHint = null;

        // Test without SchemaAware delegate serializer
        JsonNode schema = serializer.getSchema(provider, typeHint);
        Assert.assertNull(schema);
    }

    @Test
    public void testGetSchemaWithOptional() throws Exception {
        Converter<Object, String> converter = new Converter<Object, String>() {
            @Override
            public String convert(Object value) {
                return value.toString();
            }
        };

        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);
        SerializerProvider provider = null;
        Type typeHint = null;
        boolean isOptional = false;

        // Test without SchemaAware delegate serializer
        JsonNode schema = serializer.getSchema(provider, typeHint, isOptional);
        Assert.assertNull(schema);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws Exception {
        Converter<Object, String> converter = new Converter<Object, String>() {
            @Override
            public String convert(Object value) {
                return value.toString();
            }
        };

        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);
        JsonFormatVisitorWrapper visitor = null;
        JavaType typeHint = null;

        // Test without delegate serializer
        try {
            serializer.acceptJsonFormatVisitor(visitor, typeHint);
            Assert.fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected exception
        }
    }

}