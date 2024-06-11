package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.Base64Variants;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        ObjectMapper.Setup setup = new ObjectMapper.Setup()
                .withSerializationFeature(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .withDeserializationFeature(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE)
                .withDeserializationFeature(DeserializationFeature.FAIL_ON_UNPARSED_VALUE)
                .withDeserializationFeature(DeserializationFeature.READ_UNKNOWN_ENUMS_AS_NULL)
                .withDeserializationFeature(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES)
                .withDeserializationFeature(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)
                .withDeserializationFeature(DeserializationFeature.READ_NULL_FOR_EMPTY_JavaType)
                .withDeserializationFeature(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT)
                .withDeserializationFeature(DeserializationFeature.UNWRAP_ROOT_VALUE)
                .withDeserializationFeature(DeserializationFeature.THROW_EXCEPTIONS)
                .withDeserializationFeature(DeserializationFeature.GENERIC_TYPES_AS_FACTORIES)
                .withDeserializationFeature(DeserializationFeature.BIND_SPECIAL_FORMATS)
                .withDeserializationFeature(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setDefaultTyping(DefaultTyping.NON_FINAL);
        mapper.configure(setup);
        this.objectMapper = mapper;
    }

    @Test
    public void testBaseTypeName() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new AbstractMapTypeDeserializer(null, null, "_typePropertyName", true, String.class) {};
        String baseTypeName = typeDeserializerBase.baseTypeName();
        assertEquals("java.lang.String", baseTypeName);
    }

    @Test
    public void testToString() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new AbstractMapTypeDeserializer(null, null, "_typePropertyName", true, String.class) {};
        String toString = typeDeserializerBase.toString();
        assertTrue(toString.contains("[TypeDeserializerBase; base-type:java.lang.String; id-resolver:null]"));
    }

    @Test
    public void testGetPropertyName() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new AbstractMapTypeDeserializer(null, null, "_typePropertyName", true, String.class) {};
        String propertyName = typeDeserializerBase.getPropertyName();
        assertEquals("_typePropertyName", propertyName);
    }

    @Test
    public void testGetTypeIdResolver() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new AbstractMapTypeDeserializer(null, null, "_typePropertyName", true, String.class) {};
        TypeIdResolver typeIdResolver = typeDeserializerBase.getTypeIdResolver();
        assertNotNull(typeIdResolver);
    }

    @Test
    public void testGetDefaultImpl() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new AbstractMapTypeDeserializer(null, null, "_typePropertyName", true, String.class) {};
        Class<?> defaultImpl = typeDeserializerBase.getDefaultImpl();
        assertNull(defaultImpl);
    }

    @Test
    public void testFindDeserializer() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new AbstractMapTypeDeserializer(null, null, "_typePropertyName", true, String.class) {};
        JsonDeserializer<Object> jsonDeserializer = typeDeserializerBase._findDeserializer(objectMapper.getDeserializationContext(), "StringDeserializer");
        assertNotNull(jsonDeserializer);
        assertInstanceOf(FromStringDeserializer.class, jsonDeserializer);
    }

    @Test(expected = JsonMappingException.class)
    public void testFindDeserializerWithUnknownTypeId() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new AbstractMapTypeDeserializer(null, null, "_typePropertyName", true, String.class) {};
        JsonDeserializer<Object> jsonDeserializer = typeDeserializerBase._findDeserializer(objectMapper.getDeserializationContext(), "UnknownTypeId");
        fail("Unexpectedly found deserializer for unknown type id");
    }

    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new AbstractMapTypeDeserializer(null, null, "_typePropertyName", true, String.class) {};
        JsonDeserializer<Object> jsonDeserializer = typeDeserializerBase._findDefaultImplDeserializer(objectMapper.getDeserializationContext());
        assertNull(jsonDeserializer);
    }

    @Test
    public void testFindDefaultImplDeserializerWithDefaultImpl() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new AbstractMapTypeDeserializer(null, null, "_typePropertyName", true, HashMap.class) {};
        JsonDeserializer<Object> jsonDeserializer = typeDeserializerBase._findDefaultImplDeserializer(objectMapper.getDeserializationContext());
        assertNotNull(jsonDeserializer);
        assertInstanceOf(MapDeserializer.class, jsonDeserializer);
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new AbstractMapTypeDeserializer(null, null, "_typePropertyName", true, String.class) {};
        JsonParser jsonParser = new JsonFactory().createParser("\"StringTypeId\"");
        Object obj = typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, objectMapper.getDeserializationContext());
        assertEquals("StringTypeId", obj);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeWithUnknownTypeId() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new AbstractMapTypeDeserializer(null, null, "_typePropertyName", true, String.class) {};
        JsonParser jsonParser = new JsonFactory().createParser("\"UnknownTypeId\"");
        Object obj = typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, objectMapper.getDeserializationContext());
        fail("Unexpectedly deserialized object for unknown type id");
    }

    @Test
    public void testHandleUnknownTypeId() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new AbstractMapTypeDeserializer(null, null, "_typePropertyName", true, String.class) {};
        try {
            Object obj = typeDeserializerBase._handleUnknownTypeId(objectMapper.getDeserializationContext(), "UnknownTypeId", null, null);
            fail("Unexpectedly deserialized object for unknown type id");
        } catch (JsonMappingException e) {
            assertEquals("Unknown type id: UnknownTypeId", e.getMessage());
        }
    }
}