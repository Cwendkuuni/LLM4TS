package com.fasterxml.jackson.databind.ser.std.test;

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
import static org.junit.Assert.*;

public class StdDelegatingSerializerTest {

    private final Converter<Object, ?> converter = new Converter<Object, Object>() {
        @Override
        public Object convert(Object value) {
            // perform conversion
            return null;
        }
    };

    @Test
    public void testResolve() {
        SerializerProvider provider = new DefaultSerializerProvider();
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);
        try {
            serializer.resolve(provider);
        } catch (JsonMappingException e) {
            fail("Should not throw an exception");
        }
    }

    @Test
    public void testCreateContextual() {
        SerializerProvider provider = new DefaultSerializerProvider();
        BeanProperty property = new BeanProperty() {
            @Override
            public String getName() {
                return "property";
            }

            @Override
            public PropertyMetadata getMetadata() {
                return null;
            }

            @Override
            public AnnotatedMember getMember() {
                return null;
            }

            @Override
            public JavaType getType() {
                return null;
            }

            @Override
            public PropertyName getWrapperName() {
                return null;
            }

            @Override
            public AnnotatedMember getPrimaryMember() {
                return null;
            }

            @Override
            public Class<?>[] findViews() {
                return new Class[0];
            }
        };
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);
        try {
            JsonSerializer<?> result = serializer.createContextual(provider, property);
            assertNull(result);
        } catch (JsonMappingException e) {
            fail("Should not throw an exception");
        }
    }

    @Test
    public void testSerialize() throws IOException {
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        SerializerProvider provider = new DefaultSerializerProvider();
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);
        try {
            serializer.serialize(null, gen, provider);
        } catch (IOException e) {
            fail("Should not throw an exception");
        }
    }

    @Test
    public void testSerializeWithType() throws IOException {
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        SerializerProvider provider = new DefaultSerializerProvider();
        TypeSerializer typeSer = new DefaultTypeSerializer();
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);
        try {
            serializer.serializeWithType(null, gen, provider, typeSer);
        } catch (IOException e) {
            fail("Should not throw an exception");
        }
    }

    @Test
    public void testIsEmpty_1() {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);
        assertFalse(serializer.isEmpty(null));
    }

    @Test
    public void testIsEmpty_2() {
        SerializerProvider provider = new DefaultSerializerProvider();
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);
        assertFalse(serializer.isEmpty(provider, null));
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        SerializerProvider provider = new DefaultSerializerProvider();
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);
        try {
            JsonNode schema = serializer.getSchema(provider, null);
            assertNull(schema);
        } catch (JsonMappingException e) {
            fail("Should not throw an exception");
        }
    }

    @Test
    public void testGetSchemaWithIsOptional() throws JsonMappingException {
        SerializerProvider provider = new DefaultSerializerProvider();
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);
        try {
            JsonNode schema = serializer.getSchema(provider, null, true);
            assertNull(schema);
        } catch (JsonMappingException e) {
            fail("Should not throw an exception");
        }
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = new DefaultJsonFormatVisitorWrapper();
        JavaType typeHint = null;
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);
        try {
            serializer.acceptJsonFormatVisitor(visitor, typeHint);
        } catch (JsonMappingException e) {
            fail("Should not throw an exception");
        }
    }

    @Test
    public void testConvertValue() {
        Object value = null;
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);
        Object result = serializer.convertValue(value);
        assertNull(result);
    }

    @Test
    public void testGetDelegatee() {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);
        JsonSerializer<?> delegatee = serializer.getDelegatee();
        assertNull(delegatee);
    }

    @Test
    public void testWithDelegate() {
        Converter<Object, ?> converter = new Converter<Object, Object>() {
            @Override
            public Object convert(Object value) {
                // perform conversion
                return null;
            }
        };
        JavaType delegateType = null;
        JsonSerializer<?> delegateSerializer = new DefaultJsonSerializer();
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);
        StdDelegatingSerializer result = serializer.withDelegate(converter, delegateType, delegateSerializer);
        assertEquals(serializer.getClass(), result.getClass());
    }

}