package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {

    private ObjectMapper objectMapper;
    private TypeDeserializerBase typeDeserializerBase;

    @Before
    public void setup() throws Exception {
        objectMapper = new ObjectMapper();
        JavaType baseType = objectMapper.getTypeFactory().constructType(TypeDeserializerBase.class);
        TypeIdResolver idResolver = new StdTypeIdResolver();
        String typePropertyName = "typeProperty";
        boolean typeIdVisible = true;
        JavaType defaultImpl = objectMapper.getTypeFactory().constructType(String.class);
        typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
    }

    @Test
    public void testForProperty() throws Exception {
        BeanProperty property = new BeanProperty("testProperty", String.class);
        TypeDeserializer forProperty = typeDeserializerBase.forProperty(property);
        assertEquals(TypeDeserializerBase.class, forProperty.getClass());
        assertEquals(typeDeserializerBase, forProperty);
    }

    @Test
    public void testGetTypeInclusion() throws Exception {
        JsonTypeInfo.As typeInclusion = typeDeserializerBase.getTypeInclusion();
        assertEquals(JsonTypeInfo.As.WRAP_ROOT_VALUE, typeInclusion);
    }

    @Test
    public void testBaseTypeName() throws Exception {
        String baseTypeName = typeDeserializerBase.baseTypeName();
        assertEquals(TypeDeserializerBase.class.getName(), baseTypeName);
    }

    @Test
    public void testPropertyName() throws Exception {
        String propertyName = typeDeserializerBase.getPropertyName();
        assertEquals("", propertyName);
    }

    @Test
    public void testTypeIdResolver() throws Exception {
        TypeIdResolver typeIdResolver = typeDeserializerBase.getTypeIdResolver();
        assertEquals(idResolver.getClass(), typeIdResolver.getClass());
    }

    @Test
    public void testDefaultImpl() throws Exception {
        Class<?> defaultImplClass = typeDeserializerBase.getDefaultImpl();
        assertEquals(String.class, defaultImplClass);
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws Exception {
        String json = "{\"typeProperty\":\"stringType\"}";
        JsonParser jsonParser = objectMapper.getFactory().createParser(json);
        Object deserializedObject = typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, objectMapper.getDeserializationContext());
        assertEquals("stringType", deserializedObject);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeWithUnknownTypeId() throws Exception {
        String json = "{\"typeProperty\":\"unknownTypeId\"}";
        JsonParser jsonParser = objectMapper.getFactory().createParser(json);
        typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, objectMapper.getDeserializationContext());
    }
}