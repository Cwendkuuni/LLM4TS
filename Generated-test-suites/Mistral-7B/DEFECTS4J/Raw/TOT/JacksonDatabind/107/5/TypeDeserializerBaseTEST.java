package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {

    private ObjectMapper mapper;

    @Before
    public void setUp() {
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void testTypeDeserializerBaseForProperty() throws JsonProcessingException {
        JavaType baseType = mapper.getTypeFactory().constructType(TypeDeserializerBase.class);
        TypeIdResolver idResolver = mapper.getTypeIdResolver();
        String typePropertyName = "typeProperty";
        boolean typeIdVisible = true;
        JavaType defaultImpl = mapper.getTypeFactory().constructType(String.class);
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        BeanProperty property = mapper.findProperty("typeProperty", String.class);
        TypeDeserializer forProperty = typeDeserializerBase.forProperty(property);
        assertEquals(TypeDeserializerBase.class, forProperty.getClass());
    }

    @Test
    public void testGetTypeInclusion() throws JsonProcessingException {
        JavaType baseType = mapper.getTypeFactory().constructType(TypeDeserializerBase.class);
        TypeIdResolver idResolver = mapper.getTypeIdResolver();
        String typePropertyName = "typeProperty";
        boolean typeIdVisible = true;
        JavaType defaultImpl = mapper.getTypeFactory().constructType(String.class);
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        JsonTypeInfo.As typeInclusion = typeDeserializerBase.getTypeInclusion();
        assertEquals(JsonTypeInfo.As.WRAP_ONLY, typeInclusion);
    }

    @Test
    public void testBaseTypeName() throws JsonProcessingException {
        JavaType baseType = mapper.getTypeFactory().constructType(TypeDeserializerBase.class);
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseBase(baseType, null, null, false, null);
        String baseTypeName = typeDeserializerBase.baseTypeName();
        assertEquals(TypeDeserializerBase.class.getName(), baseTypeName);
    }

    @Test
    public void testPropertyName() throws JsonProcessingException {
        JavaType baseType = mapper.getTypeFactory().constructType(TypeDeserializerBase.class);
        TypeIdResolver idResolver = mapper.getTypeIdResolver();
        String typePropertyName = "typeProperty";
        boolean typeIdVisible = true;
        JavaType defaultImpl = mapper.getTypeFactory().constructType(String.class);
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        String propertyName = typeDeserializerBase.getPropertyName();
        assertEquals(typePropertyName, propertyName);
    }

    @Test
    public void testFindDeserializer() throws JsonProcessingException {
        JavaType baseType = mapper.getTypeFactory().constructType(TypeDeserializerBase.class);
        TypeIdResolver idResolver = mapper.getTypeIdResolver();
        String typePropertyName = "typeProperty";
        boolean typeIdVisible = true;
        JavaType defaultImpl = mapper.getTypeFactory().constructType(String.class);
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        DeserializationContext context = mapper.getDeserializationContext();
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(context, "StringDeserializer");
        assertNotNull(deserializer);
        assertEquals("StringDeserializer", deserializer.getClass().getName());
    }

    @Test
    public void testFindDefaultImplDeserializer() throws JsonProcessingException {
        JavaType baseType = mapper.getTypeFactory().constructType(TypeDeserializerBase.class);
        TypeIdResolver idResolver = mapper.getTypeIdResolver();
        String typePropertyName = "typeProperty";
        boolean typeIdVisible = true;
        JavaType defaultImpl = mapper.getTypeFactory().constructType(String.class);
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        DeserializationContext context = mapper.getDeserializationContext();
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDefaultImplDeserializer(context);
        assertNull(deserializer);
    }

    @Test
    public void testHandleUnknownTypeId() throws JsonProcessingException {
        JavaType baseType = mapper.getTypeFactory().constructType(TypeDeserializerBase.class);
        TypeIdResolver idResolver = mapper.getTypeIdResolver();
        String typePropertyName = "typeProperty";
        boolean typeIdVisible = true;
        JavaType defaultImpl = mapper.getTypeFactory().constructType(String.class);
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        DeserializationContext context = mapper.getDeserializationContext();
        context.handleUnknownTypeId(baseType, null);
        fail("Expected JsonMappingException");
    }

    @Test
    public void testHandleMissingTypeId() throws JsonProcessingException {
        JavaType baseType = mapper.getTypeFactory().constructType(TypeDeserializerBase.class);
        TypeIdResolver idResolver = mapper.getTypeIdResolver();
        String typePropertyName = "typeProperty";
        boolean typeIdVisible = true;
        JavaType defaultImpl = mapper.getTypeFactory().constructType(String.class);
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        DeserializationContext context = mapper.getDeserializationContext();
        context.handleMissingTypeId(baseType, "MissingTypeId");
        fail("Expected JsonMappingException");
    }
}