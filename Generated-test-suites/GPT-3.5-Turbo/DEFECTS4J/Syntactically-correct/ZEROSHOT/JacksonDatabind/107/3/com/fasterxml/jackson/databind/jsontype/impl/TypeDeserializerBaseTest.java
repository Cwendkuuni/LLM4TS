package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Test;
import org.junit.Assert;
import org.junit.Before;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.deserialization.DeserializationFeature;
import com.fasterxml.jackson.databind.util.ClassUtil;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TypeDeserializerBaseTest {

    TypeDeserializerBase typeDeserializerBase;

    @Before
    public void setup() {
        JavaType baseType = new JavaType();
        TypeIdResolver idRes = new TypeIdResolver();
        String typePropertyName = "testPropertyName";
        boolean typeIdVisible = true;
        JavaType defaultImpl = new JavaType();
        typeDeserializerBase = new TypeDeserializerBase(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
    }

    @Test
    public void testForProperty() {

        BeanProperty prop = new BeanProperty();
        TypeDeserializerBase result = typeDeserializerBase.forProperty(prop);

        Assert.assertEquals(prop, result._property);
    }

    @Test
    public void testGetTypeInclusion() {

        TypeDeserializerBase result = typeDeserializerBase;

        Assert.assertEquals(JsonTypeInfo.As.PROPERTY, result.getTypeInclusion());
    }

    @Test
    public void testBaseTypeName() {

        TypeDeserializerBase result = typeDeserializerBase;

        Assert.assertEquals("TestTypeName", result.baseTypeName());
    }

    @Test
    public void testGetPropertyName() {

        TypeDeserializerBase result = typeDeserializerBase;

        Assert.assertEquals("TestPropertyName", result.getPropertyName());
    }

    @Test
    public void testGetTypeIdResolver() {

        TypeDeserializerBase result = typeDeserializerBase;

        Assert.assertEquals(new TypeIdResolver(), result.getTypeIdResolver());
    }

    @Test
    public void testGetDefaultImpl() {

        TypeDeserializerBase result = typeDeserializerBase;

        Assert.assertEquals(Void.TYPE, result.getDefaultImpl());
    }

    @Test
    public void testBaseType() {

        TypeDeserializerBase result = typeDeserializerBase;

        Assert.assertEquals(new JavaType(), result.baseType());
    }

    @Test
    public void testToString() {

        TypeDeserializerBase result = typeDeserializerBase;

        Assert.assertEquals("[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase; base-type:TestBaseType; id-resolver: TestIdResolver]", result.toString());
    }

    @Test
    public void test_findDeserializer() throws IOException {

        DeserializationContext ctxt = new DeserializationContext();
        String typeId = "TestId";
        JsonDeserializer<Object> result = typeDeserializerBase._findDeserializer(ctxt, typeId);

        Assert.assertEquals(new JsonDeserializer<Object>(), result);
    }

    @Test
    public void test_findDefaultImplDeserializer() throws IOException {

        DeserializationContext ctxt = new DeserializationContext();
        JsonDeserializer<Object> result = typeDeserializerBase._findDefaultImplDeserializer(ctxt);

        Assert.assertEquals(new JsonDeserializer<Object>(), result);
    }

    @Test
    public void test_deserializeWithNativeTypeId() throws IOException {

        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object typeId = "TestId";
        Object result = typeDeserializerBase._deserializeWithNativeTypeId(jp, ctxt, typeId);

        Assert.assertEquals(new Object(), result);
    }

    @Test
    public void test_handleUnknownTypeId() throws IOException {

        DeserializationContext ctxt = new DeserializationContext();
        String typeId = "TestId";
        JavaType result = typeDeserializerBase._handleUnknownTypeId(ctxt, typeId);

        Assert.assertEquals(new JavaType(), result);
    }

    @Test
    public void test_handleMissingTypeId() throws IOException {

        DeserializationContext ctxt = new DeserializationContext();
        String extraDesc = "TestDesc";
        JavaType result = typeDeserializerBase._handleMissingTypeId(ctxt, extraDesc);

        Assert.assertEquals(new JavaType(), result);
    }

}