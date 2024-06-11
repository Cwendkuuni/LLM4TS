package com.fasterxml.jackson.databind.jsontype.impl;

import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class TypeDeserializerBaseTest {

    private TypeDeserializerBase typeDeserializerBase;

    @Before
    public void setup() {
        TypeIdResolver idResolver = Mockito.mock(TypeIdResolver.class);
        JavaType baseType = Mockito.mock(JavaType.class);
        BeanProperty property = Mockito.mock(BeanProperty.class);
        JavaType defaultImpl = Mockito.mock(JavaType.class);
        typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, "type", true, defaultImpl) {

            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }

        };
    }

    @Test
    public void testBaseTypeName() {
        String result = typeDeserializerBase.baseTypeName();

        Assert.assertNotNull(result);
    }

    @Test
    public void testGetPropertyName() {
        String result = typeDeserializerBase.getPropertyName();

        Assert.assertEquals("type", result);
    }

    @Test
    public void testGetTypeIdResolver() {
        TypeIdResolver result = typeDeserializerBase.getTypeIdResolver();

        Assert.assertNotNull(result);
    }

    @Test
    public void testGetDefaultImpl() {
        Class<?> result = typeDeserializerBase.getDefaultImpl();

        Assert.assertNull(result);
    }

    @Test
    public void testBaseType() {
        JavaType result = typeDeserializerBase.baseType();

        Assert.assertNotNull(result);
    }

    @Test
    public void testToString() {
        String result = typeDeserializerBase.toString();

        Assert.assertNotNull(result);
    }

    @Test
    public void test_findDeserializer() throws Exception {
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        JsonDeserializer<Object> result = typeDeserializerBase._findDeserializer(ctxt, "typeId");

        Assert.assertNull(result);
    }

    @Test
    public void test_findDefaultImplDeserializer() throws Exception {
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        JsonDeserializer<Object> result = typeDeserializerBase._findDefaultImplDeserializer(ctxt);

        Assert.assertNull(result);
    }

    @Test
    public void test_deserializeWithNativeTypeId() throws Exception {
        JsonParser jp = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        Object result = typeDeserializerBase._deserializeWithNativeTypeId(jp, ctxt);

        Assert.assertNull(result);
    }

    @Test
    public void test_deserializeWithNativeTypeId_1() throws Exception {
        JsonParser jp = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        Object result = typeDeserializerBase._deserializeWithNativeTypeId(jp, ctxt, "typeId");

        Assert.assertNull(result);
    }

    @Test
    public void test_handleUnknownTypeId() throws Exception {
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        String result = typeDeserializerBase._handleUnknownTypeId(ctxt, "typeId").toString();

        Assert.assertNotNull(result);
    }

    @Test
    public void test_handleMissingTypeId() throws Exception {
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        String result = typeDeserializerBase._handleMissingTypeId(ctxt, "extraDesc").toString();

        Assert.assertNotNull(result);
    }

}