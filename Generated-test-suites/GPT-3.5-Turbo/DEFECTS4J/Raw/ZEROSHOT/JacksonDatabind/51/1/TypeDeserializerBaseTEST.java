package com.fasterxml.jackson.databind.jsontype.impl;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TypeDeserializerBaseTest {

    private TypeDeserializerBase typeDeserializer;

    @Before
    public void setUp() {
        TypeIdResolver idResolver = new TypeIdResolverImpl();
        JavaType baseType = null; // Set the appropriate value
        String typePropertyName = null; // Set the appropriate value
        boolean typeIdVisible = false; // Set the appropriate value
        JavaType defaultImpl = null; // Set the appropriate value

        typeDeserializer = new TypeDeserializerBaseDummy(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
    }

    @Test
    public void testForProperty() {
        BeanProperty property = null; // Set the appropriate value

        TypeDeserializer result = typeDeserializer.forProperty(property);

        Assert.assertNotNull(result);
        Assert.assertEquals(TypeDeserializerBaseDummy.class, result.getClass());
    }

    @Test
    public void testGetTypeInclusion() {
        JsonTypeInfo.As expected = null; // Set the appropriate value

        JsonTypeInfo.As result = typeDeserializer.getTypeInclusion();

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testBaseTypeName() {
        String expected = null; // Set the appropriate value

        String result = typeDeserializer.baseTypeName();

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testGetPropertyName() {
        String expected = null; // Set the appropriate value

        String result = typeDeserializer.getPropertyName();

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testGetTypeIdResolver() {
        TypeIdResolver expected = null; // Set the appropriate value

        TypeIdResolver result = typeDeserializer.getTypeIdResolver();

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testGetDefaultImpl() {
        Class<?> expected = null; // Set the appropriate value

        Class<?> result = typeDeserializer.getDefaultImpl();

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testToString() {
        String expected = null; // Set the appropriate value

        String result = typeDeserializer.toString();

        Assert.assertEquals(expected, result);
    }

    @Test
    public void test_findDeserializer() throws IOException {
        DeserializationContext ctxt = null; // Set the appropriate value
        String typeId = null; // Set the appropriate value

        JsonDeserializer<Object> result = typeDeserializer._findDeserializer(ctxt, typeId);

        Assert.assertNotNull(result);
        // Add assertions for result
    }

    @Test
    public void test_findDefaultImplDeserializer() throws IOException {
        DeserializationContext ctxt = null; // Set the appropriate value

        JsonDeserializer<Object> result = typeDeserializer._findDefaultImplDeserializer(ctxt);

        Assert.assertNotNull(result);
        // Add assertions for result
    }

    @Test
    public void test_deserializeWithNativeTypeId_1() throws IOException {
        JsonParser jp = null; // Set the appropriate value
        DeserializationContext ctxt = null; // Set the appropriate value

        Object result = typeDeserializer._deserializeWithNativeTypeId(jp, ctxt);

        Assert.assertNotNull(result);
        // Add assertions for result
    }

    @Test
    public void test_deserializeWithNativeTypeId_2() throws IOException {
        JsonParser jp = null; // Set the appropriate value
        DeserializationContext ctxt = null; // Set the appropriate value
        Object typeId = null; // Set the appropriate value

        Object result = typeDeserializer._deserializeWithNativeTypeId(jp, ctxt, typeId);

        Assert.assertNotNull(result);
        // Add assertions for result
    }

    @Test
    public void test_handleUnknownTypeId() throws IOException {
        DeserializationContext ctxt = null; // Set the appropriate value
        String typeId = null; // Set the appropriate value
        TypeIdResolver idResolver = null; // Set the appropriate value
        JavaType baseType = null; // Set the appropriate value

        JavaType result = typeDeserializer._handleUnknownTypeId(ctxt, typeId, idResolver, baseType);

        Assert.assertNotNull(result);
        // Add assertions for result
    }
}

class TypeIdResolverImpl implements TypeIdResolver {

    @Override
    public void init(JavaType baseType) {

    }

    @Override
    public String idFromValue(Object value) {
        return null;
    }

    @Override
    public String idFromValueAndType(Object value, Class<?> suggestedType) {
        return null;
    }

    @Override
    public JavaType typeFromId(DatabindContext context, String id) {
        return null;
    }

    @Override
    public JsonTypeInfo.Id getMechanism() {
        return null;
    }
}

class TypeDeserializerBaseDummy extends TypeDeserializerBase {

    protected TypeDeserializerBaseDummy(JavaType baseType, TypeIdResolver idRes, String typePropertyName, boolean typeIdVisible, JavaType defaultImpl) {
        super(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
    }

    @Override
    public TypeDeserializer forProperty(BeanProperty prop) {
        return new TypeDeserializerBaseDummy(_baseType, _idResolver, _typePropertyName, _typeIdVisible, _defaultImpl);
    }

    @Override
    public JsonTypeInfo.As getTypeInclusion() {
        return null;
    }
}