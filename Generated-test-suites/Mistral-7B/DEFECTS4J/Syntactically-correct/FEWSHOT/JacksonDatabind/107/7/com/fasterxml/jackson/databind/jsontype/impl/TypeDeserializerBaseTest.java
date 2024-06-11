package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.*;

public class TypeDeserializerBaseTest {

    @Test
    public void testHandleMissingTypeId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DefaultTypingResolverBuilder builder = new DefaultTypingResolverBuilder();
        TypeDeserializerBase typeDeserializerBase = new AbstractMapTypeDeserializer(
                mapper.getTypeFactory().constructMapType(Map.class, String.class, Number.class),
                builder, null, null, null);

        String json = "{\"foo\": 1}";
        TypeDeserializer deserializer = mapper.readValue(json, TypeDeserializer.class);

        assertEquals(TypeDeserializerBase.class, deserializer.getClass());
        assertEquals(TypeDeserializerBase.class, typeDeserializerBase.getClass());

        try {
            typeDeserializerBase.handleMissingTypeId(mapper.getDeserializationContext(), "MissingTypeId");
            fail();
        } catch (JsonMappingException e) {
            assertEquals("Missing type id 'MissingTypeId' for class [simple type, class java.util.Map$Entry<java.lang.String, java.lang.Number>]", e.getMessage());
        }
    }

    @Test
    public void testFindDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(String[].class, new StringArrayDeserializer());
        mapper.registerModule(module);

        TypeDeserializerBase typeDeserializerBase = new ArrayTypeDeserializer(
                mapper.getTypeFactory().constructTypeArrayType(String[].class),
                mapper.getTypeFactory(), null, null, null);

        String json = "[\"foo\", \"bar\"]";
        TypeDeserializer deserializer = mapper.readValue(json, TypeDeserializer.class);

        assertEquals(TypeDeserializerBase.class, deserializer.getClass());
        assertEquals(TypeDeserializerBase.class, typeDeserializerBase.getClass());

        JsonDeserializer<Object> deser = typeDeserializerBase.findDeserializer(mapper.getDeserializationContext(), "com.fasterxml.jackson.core.JsonParser");
        assertNotNull(deser);
        assertEquals(StringArrayDeserializer.class, deser.getClass());
    }

    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        TypeDeserializerBase typeDeserializerBase = new AbstractMapTypeDeserializer(
                mapper.getTypeFactory().constructMapType(Map.class, String.class, Number.class),
                null, null, null, null);

        String json = "{}";
        TypeDeserializer deserializer = mapper.readValue(json, TypeDeserializer.class);

        assertEquals(TypeDeserializerBase.class, deserializer.getClass());
        assertEquals(TypeDeserializerBase.class, typeDeserializerBase.getClass());

        JsonDeserializer<Object> deser = typeDeserializerBase.findDefaultImplDeserializer(mapper.getDeserializationContext());
        assertNull(deser);
    }

    @Test
    public void testHandleUnknownTypeId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DefaultTypingResolverBuilder builder = new DefaultTypingResolverBuilder();
        TypeDeserializerBase typeDeserializerBase = new AbstractMapTypeDeserializer(
                mapper.getTypeFactory().constructMapType(Map.class, String.class, Number.class),
                builder, null, null, null);

        String json = "{\"foo\": 1}";
        TypeDeserializer deserializer = mapper.readValue(json, TypeDeserializer.class);

        assertEquals(TypeDeserializerBase.class, deserializer.getClass());
        assertEquals(TypeDeserializerBase.class, typeDeserializerBase.getClass());

        try {
            typeDeserializerBase.handleUnknownTypeId(mapper.getDeserializationContext(), "UnknownTypeId");
            fail();
        } catch (JsonMappingException e) {
            assertEquals("Unknown type id 'UnknownTypeId' for class [simple type, class java.util.Map$Entry<java.lang.String, java.lang.Number>]", e.getMessage());
        }
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DefaultTypingResolverBuilder builder = new DefaultTypingResolverBuilder();
        MapTypeDeserializer mapTypeDeserializer = new MapTypeDeserializer(mapper.getTypeFactory(), builder, null, null, null);

        String json = "{\"foo\": { \"bar\": 1 } }";
        Map<String, Number> result = mapper.readValue(json, Map.class);

        Map<String, Number> expected = new HashMap<>();
        expected.put("bar", 1);

        assertEquals(expected, result);

        TypeDeserializer deserializer = mapper.readValue(json, TypeDeserializer.class);

        assertEquals(MapTypeDeserializer.class, deserializer.getClass());

        Object deserializedObject = deserializer.deserializeWithNativeTypeId(mapper.getFactory().createParser(json), mapper.getDeserializationContext());

        assertEquals(expected, deserializedObject);
    }
}