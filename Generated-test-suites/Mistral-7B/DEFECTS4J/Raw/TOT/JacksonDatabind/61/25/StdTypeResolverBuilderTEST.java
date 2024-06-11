package com.fasterxml.jackson.databind.jsontype.impl.test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperFactory;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.jsontype.impl.*;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class StdTypeResolverBuilderTest {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
    }

    @Test
    public void testStdTypeResolverBuilder_Default() throws IOException {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        TypeSerializer typeSerializer = builder.buildTypeSerializer(mapper.getSerializationConfig(),
                JavaType.constructRawType(List.class), Collections.emptyList());
        assertNull(typeSerializer);

        TypeDeserializer typeDeserializer = builder.buildTypeDeserializer(mapper.getDeserializationConfig(),
                JavaType.constructRawType(List.class), Collections.emptyList());
        assertNull(typeDeserializer);
    }

    @Test
    public void testStdTypeResolverBuilder_NoTypeInfoBuilder() throws IOException {
        StdTypeResolverBuilder builder = StdTypeResolverBuilder.noTypeInfoBuilder();
        TypeSerializer typeSerializer = builder.buildTypeSerializer(mapper.getSerializationConfig(),
                JavaType.constructRawType(List.class), Collections.emptyList());
        assertNull(typeSerializer);

        TypeDeserializer typeDeserializer = builder.buildTypeDeserializer(mapper.getDeserializationConfig(),
                JavaType.constructRawType(List.class), Collections.emptyList());
        assertNull(typeDeserializer);
    }

    @Test
    public void testStdTypeResolverBuilder_CustomIdResolver() throws IOException {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        TypeIdResolver customIdResolver = new AlwaysStringIdResolver();
        builder.init(JsonTypeInfo.Id.CUSTOM, customIdResolver);

        TypeSerializer typeSerializer = builder.buildTypeSerializer(mapper.getSerializationConfig(),
                JavaType.constructRawType(List.class), Collections.emptyList());
        assertEquals("always", typeSerializer.serializeValue("test"));

        TypeDeserializer typeDeserializer = builder.buildTypeDeserializer(mapper.getDeserializationConfig(),
                JavaType.constructRawType(List.class), Collections.emptyList());
        assertEquals(String.class, typeDeserializer.deserializeValue(new FakeContext(),
                mapper.readTree("['always']"), JavaType.constructRawType(List.class)));
    }

    @Test
    public void testStdTypeResolverBuilder_WRAPPER_ARRAY() throws IOException {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.ANY, null);
        builder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);

        TypeSerializer typeSerializer = builder.buildTypeSerializer(mapper.getSerializationConfig(),
                JavaType.constructRawType(List.class), Collections.emptyList());
        assertNull(typeSerializer);

        TypeDeserializer typeDeserializer = builder.buildTypeDeserializer(mapper.getDeserializationConfig(),
                JavaType.constructRawType(List.class), Collections.emptyList());
        assertEquals(List.class, typeDeserializer.getClass());
        TypeDeserializer deserializer = typeDeserializer.findValueDeserializer(mapper.getTypeFactory()
                .constructType(int[].class));
        assertEquals(Integer[].class, deserializer.getType());

        String json = "[\n" +
                "  [1, 2, 3],\n" +
                "  [4, 5, 6]\n" +
                "]";
        Object result = mapper.readValue(json, typeDeserializer.getType());
        List<Object> list = (List<Object>) result;
        assertEquals(2, list.size());
        assertEquals(Arrays.asList(new Integer[]{1, 2, 3}, new Integer[]{4, 5, 6}), list);
    }

    @Test
    public void testStdTypeResolverBuilder_PROPERTY() throws IOException {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.ANY, null);
        builder.inclusion(JsonTypeInfo.As.PROPERTY);
        builder.typeProperty("type");

        TypeSerializer typeSerializer = builder.buildTypeSerializer(mapper.getSerializationConfig(),
                JavaType.constructRawType(Person.class), Collections.emptyList());
        assertEquals("{ \"type\": \"Person\" }", typeSerializer.serializeValue(new Person()));

        TypeDeserializer typeDeserializer = builder.buildTypeDeserializer(mapper.getDeserializationConfig(),
                JavaType.constructRawType(Person.class), Collections.emptyList());
        assertEquals(Person.class, typeDeserializer.getType());

        String json = "{\"type\": \"Person\"}";
        Object result = mapper.readValue(json, typeDeserializer.getType());
        assertEquals(new Person(), result);
    }

    @Test
    public void testStdTypeResolverBuilder_WRAPPER_OBJECT() throws IOException {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.ANY, null);
        builder.inclusion(JsonTypeInfo.As.WRAPPER_OBJECT);

        TypeSerializer typeSerializer = builder.buildTypeSerializer(mapper.getSerializationConfig(),
                JavaType.constructRawType(Person.class), Collections.emptyList());
        assertEquals("{\"@type\": \"Person\"}", typeSerializer.serializeValue(new Person()));

        TypeDeserializer typeDeserializer = builder.buildTypeDeserializer(mapper.getDeserializationConfig(),
                JavaType.constructRawType(Person.class), Collections.emptyList());
        assertEquals(Person.class, typeDeserializer.getType());

        String json = "{\"@type\": \"Person\"}";
        Object result = mapper.readValue(json, typeDeserializer.getType());
        assertEquals(new Person(), result);
    }

    @Test
    public void testStdTypeResolverBuilder_EXTERNAL_PROPERTY() throws IOException {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.ANY, null);
        builder.inclusion(JsonTypeInfo.As.EXTERNAL_PROPERTY);
        builder.typeProperty("@class");

        TypeSerializer typeSerializer = builder.buildTypeSerializer(mapper.getSerializationConfig(),
                JavaType.constructRawType(Person.class), Collections.emptyList());
        assertEquals("{\"@class\": \"Person\"}", typeSerializer.serializeValue(new Person()));

        TypeDeserializer typeDeserializer = builder.buildTypeDeserializer(mapper.getDeserializationConfig(),
                JavaType.constructRawType(Person.class), Collections.emptyList());
        assertEquals(Person.class, typeDeserializer.getType());

        String json = "{\"@class\": \"Person\"}";
        Object result = mapper.readValue(json, typeDeserializer.getType());
        assertEquals(new Person(), result);
    }

    @Test
    public void testStdTypeResolverBuilder_EXISTING_PROPERTY() throws IOException {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.ANY, null);
        builder.inclusion(JsonTypeInfo.As.EXISTING_PROPERTY);
        builder.typeProperty("type");

        TypeSerializer typeSerializer = builder.buildTypeSerializer(mapper.getSerializationConfig(),
                JavaType.constructRawType(Person.class), Collections.emptyList());
        assertEquals("{\"type\": \"Person\"}", typeSerializer.serializeValue(new Person()));

        TypeDeserializer typeDeserializer = builder.buildTypeDeserializer(mapper.getDeserializationConfig(),
                JavaType.constructRawType(Person.class), Collections.emptyList());
        assertEquals(Person.class, typeDeserializer.getType());

        String json = "{\"type\": \"Person\"}";
        Object result = mapper.readValue(json, typeDeserializer.getType());
        assertEquals(new Person(), result);
    }

    static class Person {
        // empty class for testing
    }

    static class FakeContext extends TypeDeserializer.Context {}

    static class AlwaysStringIdResolver implements TypeIdResolver {
        @Override
        public String idFromValue(Object o) {
            return "always";
        }

        @Override
        public Object valueFromString(String id) {
            return "always";
        }
    }
}