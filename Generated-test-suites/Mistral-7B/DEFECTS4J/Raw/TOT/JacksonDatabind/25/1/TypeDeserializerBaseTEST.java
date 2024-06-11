package com.fasterxml.jackson.databind.tests.jsontype.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializerBase;
import com.fasterxml.jackson.databind.jsontype.impl.StdTypeDeserializer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

public class TypeDeserializerBaseTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testForPropertyMethod() throws IOException, JsonProcessingException {
        JavaType javaType = objectMapper.getTypeFactory().constructType(String.class);
        TypeDeserializerBase typeDeserializerBase = new StdTypeDeserializer(javaType, null, null, null, null, null);
        BeanProperty property = objectMapper.findProperty("testProperty", String.class);
        TypeDeserializerBase forProperty = typeDeserializerBase.forProperty(property);

        Assert.assertNotNull(forProperty);
        Assert.assertEquals(javaType, forProperty.getType());
        Assert.assertEquals(TypeDeserializerBase.class, forProperty.getClass());
        Assert.assertNull(forProperty._property);
    }

    @Test
    public void testGetTypeInclusionMethod() {
        JavaType javaType = objectMapper.getTypeFactory().constructType(String.class);
        TypeDeserializerBase typeDeserializerBase = new StdTypeDeserializer(javaType, null, null, null, null, null);
        Assert.assertNotNull(typeDeserializerBase.getTypeInclusion());
        Assert.assertEquals(JsonTypeInfo.As.WRAP_ROOT_VALUE, typeDeserializerBase.getTypeInclusion());
    }

    @Test
    public void testGetBaseTypeNameMethod() throws IOException {
        JavaType javaType = objectMapper.getTypeFactory().constructType(String.class);
        TypeDeserializerBase typeDeserializerBase = new StdTypeDeserializer(javaType, null, null, null, null, null);
        Assert.assertEquals("java.lang.String", typeDeserializerBase.baseTypeName());
    }

    @Test
    public void testGetPropertyNameMethod() {
        JavaType javaType = objectMapper.getTypeFactory().constructType(String.class);
        TypeDeserializerBase typeDeserializerBase = new StdTypeDeserializer(javaType, null, null, null, null, null);
        Assert.assertEquals("", typeDeserializerBase.getPropertyName());
    }

    @Test
    public void testGetTypeIdResolverMethod() {
        JavaType javaType = objectMapper.getTypeFactory().constructType(String.class);
        TypeDeserializerBase typeDeserializerBase = new StdTypeDeserializer(javaType, null, null, null, null, null);
        Assert.assertNotNull(typeDeserializerBase.getTypeIdResolver());
    }

    @Test
    public void testGetDefaultImplMethod() {
        JavaType javaType = objectMapper.getTypeFactory().constructType(String.class);
        TypeDeserializerBase typeDeserializerBase = new StdTypeDeserializer(javaType, null, null, null, null, null);
        Assert.assertNull(typeDeserializerBase.getDefaultImpl());
    }

    @Test
    public void testToStringMethod() throws IOException {
        JavaType javaType = objectMapper.getTypeFactory().constructType(String.class);
        TypeDeserializerBase typeDeserializerBase = new StdTypeDeserializer(javaType, null, null, null, null, null);
        String toString = typeDeserializerBase.toString();
        Assert.assertNotNull(toString);
        Assert.assertEquals("[com.fasterxml.jackson.databind.jsontype.impl.StdTypeDeserializer; base-type:java.lang.String; id-resolver:null]", toString);
    }

    @Test
    public void testFindDeserializerMethod() throws IOException {
        JavaType javaType = objectMapper.getTypeFactory().constructType(Map.class);
        TypeDeserializerBase typeDeserializerBase = new StdTypeDeserializer(javaType, null, null, null, null, null);
        String typeId = "Map";
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(objectMapper.getDeserializationContext(), typeId);
        Assert.assertNotNull(deserializer);
    }

    @Test(expected = IOException.class)
    public void testFindDeserializerWithInvalidTypeIdMethod() throws IOException {
        JavaType javaType = objectMapper.getTypeFactory().constructType(Map.class);
        TypeDeserializerBase typeDeserializerBase = new StdTypeDeserializer(javaType, null, null, null, null, null);
        String typeId = "InvalidTypeId";
        typeDeserializerBase._findDeserializer(objectMapper.getDeserializationContext(), typeId);
    }
}