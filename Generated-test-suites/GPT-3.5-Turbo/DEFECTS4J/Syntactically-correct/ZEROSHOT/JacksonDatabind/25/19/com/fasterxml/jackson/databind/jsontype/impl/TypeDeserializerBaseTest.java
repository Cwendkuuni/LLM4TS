package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class TypeDeserializerBaseTest {

    private TypeDeserializerBase typeDeserializerBase;

    @Before
    public void setUp() {
        JavaType baseType = null;
        TypeIdResolver idRes = null;
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = null;
        typeDeserializerBase = new TypeDeserializerBase(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl) {
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
        String expectedResult = "java.lang.NullPointerException";
        String actualResult = typeDeserializerBase.baseTypeName();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testGetPropertyName() {
        String expectedResult = "type";
        String actualResult = typeDeserializerBase.getPropertyName();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testGetTypeIdResolver() {
        TypeIdResolver expectedResult = null;
        TypeIdResolver actualResult = typeDeserializerBase.getTypeIdResolver();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testGetDefaultImpl() {
        Class<?> expectedResult = null;
        Class<?> actualResult = typeDeserializerBase.getDefaultImpl();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testToString() {
        String expectedResult = "[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase; base-type:null; id-resolver: null]";
        String actualResult = typeDeserializerBase.toString();
        assertEquals(expectedResult, actualResult);
    }
    
    @Test(expected = NullPointerException.class)
    public void test_findDeserializer_null() throws IOException {
        DeserializationContext ctxt = null;
        String typeId = null;
        typeDeserializerBase._findDeserializer(ctxt, typeId);
    }
    
    @Test
    public void test_findDeserializer() throws IOException {
        DeserializationContext ctxt = null;
        String typeId = "";
        JsonDeserializer<Object> result = typeDeserializerBase._findDeserializer(ctxt, typeId);
        assertNull(result);
    }

    @Test
    public void test_findDefaultImplDeserializer() throws IOException {
        DeserializationContext ctxt = null;
        JsonDeserializer<Object> expectedResult = NullifyingDeserializer.instance;
        JsonDeserializer<Object> actualResult = typeDeserializerBase._findDefaultImplDeserializer(ctxt);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void test_deserializeWithNativeTypeId_1() throws IOException {
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        Object typeId = null;
        typeDeserializerBase._deserializeWithNativeTypeId(jp, ctxt, typeId);
    }

    @Test(expected = IOException.class)
    public void test_deserializeWithNativeTypeId_2() throws IOException {
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        typeDeserializerBase._deserializeWithNativeTypeId(jp, ctxt);
    }

    @Test
    public void test_handleUnknownTypeId() throws IOException {
        DeserializationContext ctxt = null;
        String typeId = "";
        TypeIdResolver idResolver = null;
        JavaType baseType = null;
        JsonDeserializer<Object> expectedResult = null;
        JsonDeserializer<Object> actualResult = typeDeserializerBase._handleUnknownTypeId(ctxt, typeId, idResolver, baseType);
        assertEquals(expectedResult, actualResult);
    }
}