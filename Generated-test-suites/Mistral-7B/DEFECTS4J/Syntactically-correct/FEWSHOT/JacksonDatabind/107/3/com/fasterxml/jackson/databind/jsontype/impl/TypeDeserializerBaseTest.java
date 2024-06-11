package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class TypeDeserializerBaseTest {
    @Test
    public void testFindDeserializerWithKnownTypeId() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializerBase typeDeserializerBase = mapper.getTypeFactory().constructType(TypeDeserializerBase.class);

        JsonDeserializer<Object> deserializer = typeDeserializerBase.findDeserializer(mapper.getDeserializationContext(), "com.test.TypeA");
        assertNotNull(deserializer);
    }

    @Test
    public void testFindDeserializerWithUnknownTypeId() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializerBase typeDeserializerBase = mapper.getTypeFactory().constructType(TypeDeserializerBase.class);

        try {
            JsonDeserializer<Object> deserializer = typeDeserializerBase.findDeserializer(mapper.getDeserializationContext(), "com.test.UnknownType");
            fail("Exception expected");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testFindDefaultImplDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializerBase typeDeserializerBase = mapper.getTypeFactory().constructType(TypeDeserializerBase.class);

        JsonDeserializer<Object> deserializer = typeDeserializerBase.findDefaultImplDeserializer(mapper.getDeserializationContext());
        assertNotNull(deserializer);
    }

    @Test
    public void testFindDefaultImplDeserializerNull() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializerBase typeDeserializerBase = mapper.getTypeFactory().constructType(TypeDeserializerBase.class);

        JsonDeserializer<Object> deserializer = typeDeserializerBase.findDefaultImplDeserializer(mapper.getDeserializationContext());
        assertNull(deserializer);
    }

    @Test
    public void testHandleUnknownTypeId() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializerBase typeDeserializerBase = mapper.getTypeFactory().constructType(TypeDeserializerBase.class);

        TypeIdResolver typeIdResolver = mapper.getTypeFactory();
        JavaType baseType = mapper.getTypeFactory().constructType(String.class);

        JavaType typeId = typeIdResolver.typeFromId(mapper.getDeserializationContext(), "com.test.UnknownType");
        assertNull(typeId);

        try {
            JavaType resolvedType = typeDeserializerBase.handleUnknownTypeId(mapper.getDeserializationContext(), "com.test.UnknownType");
            fail("Exception expected");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testHandleMissingTypeId() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializerBase typeDeserializerBase = mapper.getTypeFactory().constructType(TypeDeserializerBase.class);

        TypeIdResolver typeIdResolver = mapper.getTypeFactory();
        JavaType baseType = mapper.getTypeFactory().constructType(String.class);

        JavaType typeId = mapper.getTypeFactory().constructType(String.class);

        try {
            JavaType resolvedType = typeDeserializerBase.handleMissingTypeId(mapper.getDeserializationContext(), "MissingTypeId");
            fail("Exception expected");
        } catch (JsonMappingException e) {
            // Expected
        }

        assertEquals(baseType, resolvedType);
    }
}