package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitable;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.ResolvableSerializer;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;

public class StdDelegatingSerializerTest {

    @Test
    public void testConvertValue() throws IOException {
        Converter<Object, String> converter = new Converter.Std() {
            @Override
            public String convert(Object value) {
                return value.toString();
            }
        };
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);
        Object obj = new Object() {
            @Override
            public String toString() {
                return "test";
            }
        };
        Assert.assertEquals("test", serializer.convertValue(obj));
    }

    @Test
    public void testSerialize() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Converter<Object, String> converter = new Converter.Std() {
            @Override
            public String convert(Object value) {
                return value.toString();
            }
        };
        JsonSerializer<Object> delegateSerializer = mapper.findValueSerializer(String.class);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, mapper.getTypeFactory().constructType(String.class), delegateSerializer);
        Object obj = new Object() {
            @Override
            public String toString() {
                return "test";
            }
        };
        String result = serializer.serializeToString(obj);
        Assert.assertEquals("\"test\"", result);
    }

    @Test
    public void testResolve() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        Converter<Object, String> converter = new Converter.Std() {
            @Override
            public String convert(Object value) {
                return value.toString();
            }
        };
        JsonSerializer<Object> delegateSerializer = mapper.findValueSerializer(String.class);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, null, delegateSerializer);
        SerializerProvider provider = mapper.getSerializerProvider();
        serializer.resolve(provider);
        Assert.assertNotNull(serializer.getDelegateSerializer());
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        Converter<Object, String> converter = new Converter.Std() {
            @Override
            public String convert(Object value) {
                return value.toString();
            }
        };
        JsonSerializer<Object> delegateSerializer = mapper.findValueSerializer(String.class);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, null, delegateSerializer);
        SerializerProvider provider = mapper.getSerializerProvider();
        JsonSerializer<String> result = serializer.createContextual(provider, null);
        Assert.assertEquals(delegateSerializer, result);
    }

    @Test
    public void testSerializeWithType() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Converter<Object, String> converter = new Converter.Std() {
            @Override
            public String convert(Object value) {
                return value.toString();
            }
        };
        JsonSerializer<Object> delegateSerializer = mapper.findValueSerializer(String.class);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, null, delegateSerializer);
        Object obj = new Object() {
            @Override
            public String toString() {
                return "test";
            }
        };
        TypeSerializer typeSerializer = mapper.getTypeSerializer();
        String result = serializer.serializeWithType(obj, null, null, typeSerializer);
        Assert.assertEquals("\"test\"", result);
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        Converter<Object, String> converter = new Converter.Std() {
            @Override
            public String convert(Object value) {
                return value.toString();
            }
        };
        JsonSerializer<Object> delegateSerializer = mapper.findValueSerializer(String.class);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, null, delegateSerializer);
        Assert.assertNull(serializer.getSchema(null, null));
        if (delegateSerializer instanceof SchemaAware) {
            Assert.assertNotNull(((SchemaAware) delegateSerializer).getSchema(null, null));
        }
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        Converter<Object, String> converter = new Converter.Std() {
            @Override
            public String convert(Object value) {
                return value.toString();
            }
        };
        JsonSerializer<Object> delegateSerializer = mapper.findValueSerializer(String.class);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, null, delegateSerializer);
        JsonFormatVisitorWrapper visitor = null;
        serializer.acceptJsonFormatVisitor(visitor, null);
    }
}