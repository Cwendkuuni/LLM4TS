package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.ResolvableSerializer;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Test;

import java.lang.reflect.Type;

public class StdDelegatingSerializerTest {

    @Test
    public void testConstructor1() {
        Converter<?, ?> converter = new Converter<Object, Object>() {
            @Override
            public Object convert(Object value) {
                return null;
            }
        };

        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);

        assertNull(serializer._delegateType);
        assertNull(serializer._delegateSerializer);
    }

    @Test
    public void testConstructor2() {
        Converter<Object, ?> converter = new Converter<Object, Object>() {
            @Override
            public Object convert(Object value) {
                return null;
            }
        };

        JavaType javaType = null;

        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, javaType, null);

        assertEquals(javaType, serializer._delegateType);
        assertNull(serializer._delegateSerializer);
    }

    @Test
    public void testWithDelegate() {
        Converter<Object, ?> converter = new Converter<Object, Object>() {
            @Override
            public Object convert(Object value) {
                return null;
            }
        };

        JavaType javaType = null;

        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, javaType, null);

        StdDelegatingSerializer newSerializer = serializer.withDelegate(converter, javaType, null);

        assertEquals(javaType, newSerializer._delegateType);
        assertNull(newSerializer._delegateSerializer);
    }

    @Test
    public void testResolve() {
        Converter<Object, ?> converter = new Converter<Object, Object>() {
            @Override
            public Object convert(Object value) {
                return null;
            }
        };

        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);

        serializer.resolve(null);
    }

    @Test
    public void testCreateContextual() {
        Converter<Object, ?> converter = new Converter<Object, Object>() {
            @Override
            public Object convert(Object value) {
                return null;
            }
        };

        SerializerProvider provider = null;
        BeanProperty property = null;

        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);

        serializer.createContextual(provider, property);
    }

    @Test
    public void testSerialize() throws IOException {
        Converter<Object, ?> converter = new Converter<Object, Object>() {
            @Override
            public Object convert(Object value) {
                return null;
            }
        };

        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);

        JsonGenerator gen = null;
        SerializerProvider provider = null;

        serializer.serialize(null, gen, provider);
    }

    @Test
    public void testSerializeWithType() throws IOException {
        Converter<Object, ?> converter = new Converter<Object, Object>() {
            @Override
            public Object convert(Object value) {
                return null;
            }
        };

        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);

        JsonGenerator gen = null;
        SerializerProvider provider = null;
        TypeSerializer typeSer = null;

        serializer.serializeWithType(null, gen, provider, typeSer);
    }

    @Test
    public void testIsEmpty1() {
        Converter<Object, ?> converter = new Converter<Object, Object>() {
            @Override
            public Object convert(Object value) {
                return null;
            }
        };

        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);

        Object value = null;

        serializer.isEmpty(value);
    }

    @Test
    public void testIsEmpty2() {
        Converter<Object, ?> converter = new Converter<Object, Object>() {
            @Override
            public Object convert(Object value) {
                return null;
            }
        };

        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);

        SerializerProvider prov = null;
        Object value = null;

        serializer.isEmpty(prov, value);
    }

    @Test
    public void testGetSchema1() throws JsonMappingException {
        Converter<Object, ?> converter = new Converter<Object, Object>() {
            @Override
            public Object convert(Object value) {
                return null;
            }
        };

        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);

        SerializerProvider provider = null;
        Type typeHint = null;

        serializer.getSchema(provider, typeHint);
    }

    @Test
    public void testGetSchema2() throws JsonMappingException {
        Converter<Object, ?> converter = new Converter<Object, Object>() {
            @Override
            public Object convert(Object value) {
                return null;
            }
        };

        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);

        SerializerProvider provider = null;
        Type typeHint = null;
        boolean isOptional = false;

        serializer.getSchema(provider, typeHint, isOptional);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        Converter<Object, ?> converter = new Converter<Object, Object>() {
            @Override
            public Object convert(Object value) {
                return null;
            }
        };

        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);

        JsonFormatVisitorWrapper visitor = null;
        JavaType typeHint = null;

        serializer.acceptJsonFormatVisitor(visitor, typeHint);
    }

    @Test
    public void testConvertValue() {
        Converter<Object, ?> converter = new Converter<Object, Object>() {
            @Override
            public Object convert(Object value) {
                return null;
            }
        };

        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);

        Object value = null;

        serializer.convertValue(value);
    }
}