package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitable;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.ResolvableSerializer;
import com.fasterxml.jackson.databind.util.Converter;

import java.io.IOException;
import java.lang.reflect.Type;

import static org.junit.Assert.*;

public class StdDelegatingSerializerTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testStdDelegatingSerializer() {
        Converter<Object, String> converter = new Converter.Std() {
            @Override
            public String convert(Object o) {
                return o.toString();
            }
        };

        StdDelegatingSerializer serializer1 = new StdDelegatingSerializer(converter);
        assertNull(serializer1.getDelegateType());
        assertNull(serializer1.getDelegateSerializer());

        String delegateTypeStr = "java.lang.String";
        JavaType delegateType = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(delegateTypeStr);
        JsonSerializer<Object> delegateSerializer = ObjectMapper.getDefaultInstance().findValueSerializer(delegateType);

        StdDelegatingSerializer serializer2 = new StdDelegatingSerializer(converter, delegateType, delegateSerializer);
        assertEquals(delegateType, serializer2.getDelegateType());
        assertEquals(delegateSerializer, serializer2.getDelegateSerializer());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testResolve() throws JsonMappingException {
        Converter<Object, String> converter = new Converter.Std() {
            @Override
            public String convert(Object o) {
                return o.toString();
            }
        };

        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);
        SerializerProvider provider = new SerializerProvider();

        serializer.resolve(provider);
        assertNotNull(serializer.getDelegateSerializer());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testCreateContextual() throws JsonMappingException {
        Converter<Object, String> converter = new Converter.Std() {
            @Override
            public String convert(Object o) {
                return o.toString();
            }
        };

        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);
        SerializerProvider provider = new SerializerProvider();

        JsonSerializer<Object> delegateSerializer = ObjectMapper.getDefaultInstance().findValueSerializer(String.class);
        Object result = serializer.createContextual(provider, new StubProperty("name"));
        assertEquals(delegateSerializer, result);
    }

    @Test
    public void testSerialize() throws IOException {
        Converter<Object, String> converter = new Converter.Std() {
            @Override
            public String convert(Object o) {
                return o.toString();
            }
        };

        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);
        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        JsonGenerator gen = mapper.getFactory().createGenerator();
        SerializerProvider provider = new SerializerProvider();

        Object testValue = new Object() {
            @Override
            public String toString() {
                return "testValue";
            }
        };

        serializer.serialize(testValue, gen, provider);
        assertEquals("testValue", gen.getOutputBuffer().toString());
    }

    @Test
    public void testSerializeWithType() throws IOException {
        Converter<Object, String> converter = new Converter.Std() {
            @Override
            public String convert(Object o) {
                return o.toString();
            }
        };

        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);
        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        JsonGenerator gen = mapper.getFactory().createGenerator();
        SerializerProvider provider = new SerializerProvider();
        TypeSerializer typeSerializer = mapper.getTypeSerializer();

        Object testValue = new Object() {
            @Override
            public String toString() {
                return "testValue";
            }
        };

        serializer.serializeWithType(testValue, gen, provider, typeSerializer);
        assertEquals("\"testValue\"", gen.getOutputBuffer().toString());
    }

    @Test
    public void testIsEmpty() throws IOException {
        Converter<Object, String> converter = new Converter.Std() {
            @Override
            public String convert(Object o) {
                return o.toString();
            }
        };

        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);
        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        SerializerProvider provider = new SerializerProvider();

        Object testValue = new Object() {
            @Override
            public boolean isEmpty() {
                return true;
            }
        };

        assertTrue(serializer.isEmpty(testValue));

        Object testValue2 = new Object();
        assertFalse(serializer.isEmpty(testValue2));
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        Converter<Object, String> converter = new Converter.Std() {
            @Override
            public String convert(Object o) {
                return o.toString();
            }
        };

        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);

        SchemaFactory schemaFactory = ObjectMapper.getDefaultInstance().getPolymorphicSchemaFactory();
        ObjectMapper mapper = ObjectMapper.getDefaultInstance();

        JsonNode schema = serializer.getSchema(mapper.getSerializerProvider(), String.class.getType());
        assertNotNull(schema);
        assertTrue(schema.isTextual());
        assertEquals("", schema.asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        Converter<Object, String> converter = new Converter.Std() {
            @Override
            public String convert(Object o) {
                return o.toString();
            }
        };

        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);

        JsonFormatVisitorVisitor visitor = new JsonFormatVisitorWrapper(ObjectMapper.getDefaultInstance().getDefaultPrettyPrinter());
        serializer.acceptJsonFormatVisitor(visitor, null);
        assertTrue(visitor.isEnumLike());
    }
}