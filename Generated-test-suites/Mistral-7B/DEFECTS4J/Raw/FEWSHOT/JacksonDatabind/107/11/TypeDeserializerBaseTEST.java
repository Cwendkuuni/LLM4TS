package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ConcurrentMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class TypeDeserializerBaseTest {

    @Test
    public void testFindDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        TypeDeserializerBase typeDeserializerBase = createTestTypeDeserializerBase();
        String typeId = "com.example.MyClass";

        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(ctxt, typeId);
        assertNotNull(deserializer);

        // Test with null typeId
        deserializer = typeDeserializerBase._findDeserializer(ctxt, null);
        assertNull(deserializer);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeseralizationContext();
        TypeDeserializerBase typeDeserializerBase = createTestTypeDeserializerBase();

        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDefaultImplDeserializer(ctxt);
        assertNotNull(deserializer);

        // Test with null context
        deserializer = typeDeserializerBase._findDefaultImplDeserializer(null);
        assertNull(deserializer);
    }

    @Test
    public void testHandleUnknownTypeId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        TypeDeserializerBase typeDeserializerBase = createTestTypeDeserializerBase();
        String typeId = "unknownTypeId";

        JavaType javaType = typeDeserializerBase._handleUnknownTypeId(ctxt, typeId);
        assertNotNull(javaType);
    }

    @Test
    public void testHandleMissingTypeId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        TypeDeserializerBase typeDeserializerBase = createTestTypeDeserializerBase();

        JavaType javaType = typeDeserializerBase._handleMissingTypeId(ctxt, "MissingTypeId");
        assertNotNull(javaType);
    }

    private TypeDeserializerBase createTestTypeDeserializerBase() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(TypeDeserializerBase.class);
        TypeIdResolver idResolver = new SimpleTypeIdResolver();
        String typePropertyName = "typeProperty";
        boolean typeIdVisible = true;
        JavaType defaultImpl = TypeFactory.defaultInstance().constructType(String.class);

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        return typeDeserializerBase;
    }
}