package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TypeDeserializerBaseTest {
    private DeserializationContext context;
    private TypeDeserializerBase typeDeserializer;

    @Before
    public void setUp() throws JsonMappingException {
        context = new TestContext();
        JavaType baseType = context.getTypeFactory().constructType(Integer.class);
        TypeIdResolver idResolver = context.getTypeIdResolver();
        typeDeserializer = new TypeDeserializerBase(baseType, idResolver, "typePropertyName", true, null);
    }

    @Test
    public void testForProperty() throws JsonProcessingException {
        BeanProperty property = context.createPropertyWriter("propertyName").getBeanProperty();
        TypeDeserializer forProperty = typeDeserializer.forProperty(property);
        Assert.assertNotNull(forProperty);
    }

    @Test
    public void testGetTypeInclusion() {
        JsonTypeInfo.As typeInclusion = typeDeserializer.getTypeInclusion();
        Assert.assertEquals(JsonTypeInfo.As.WRAP_IN_VALUE, typeInclusion);
    }

    @Test
    public void testBaseTypeName() {
        String baseTypeName = typeDeserializer.baseTypeName();
        Assert.assertEquals("class java.lang.Integer", baseTypeName);
    }

    @Test
    public void testGetPropertyName() {
        String propertyName = typeDeserializer.getPropertyName();
        Assert.assertEquals("typePropertyName", propertyName);
    }

    @Test
    public void testGetTypeIdResolver() {
        TypeIdResolver typeIdResolver = typeDeserializer.getTypeIdResolver();
        Assert.assertNotNull(typeIdResolver);
    }

    @Test
    public void testGetDefaultImpl() throws JsonMappingException {
        Class<?> defaultImpl = typeDeserializer.getDefaultImpl();
        Assert.assertNull(defaultImpl);
    }

    @Test
    public void testFindDeserializer() throws JsonMappingException {
        String typeId = "com.example.MyClass";
        JsonDeserializer<Object> deserializer = typeDeserializer._findDeserializer(context, typeId);
        Assert.assertNull(deserializer);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws JsonMappingException {
        JsonDeserializer<Object> defaultImplDeserializer = typeDeserializer._findDefaultImplDeserializer(context);
        Assert.assertNull(defaultImplDeserializer);
    }

    @Test(expected = JsonMappingException.class)
    public void testHandleUnknownTypeId() throws JsonMappingException {
        String typeId = "unknownTypeId";
        JavaType type = typeDeserializer._handleUnknownTypeId(context, typeId);
        Assert.fail("Should have thrown JsonMappingException");
    }

    @Test
    public void testHandleMissingTypeId() throws JsonMappingException {
        String extraDesc = "missing type id";
        JavaType type = typeDeserializer._handleMissingTypeId(context, extraDesc);
        Assert.assertNull(type);
    }
}

class TestContext extends MinimalJacksonContext {
    @Override
    public boolean canDeserialize(Class<?> clazz) {
        return true;
    }
}