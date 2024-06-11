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
import org.junit.*;
import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {

    @Test
    public void testForProperty() {
        BeanProperty prop = null;
        TypeDeserializerBase instance = new TypeDeserializerBase(null, null, null, false, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };
        TypeDeserializer result = instance.forProperty(prop);
        assertNull(result);
    }

    @Test
    public void testGetTypeInclusion() {
        TypeDeserializerBase instance = new TypeDeserializerBase(null, null, null, false, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };
        JsonTypeInfo.As result = instance.getTypeInclusion();
        assertNull(result);
    }

    @Test
    public void testBaseTypeName() {
        TypeDeserializerBase instance = new TypeDeserializerBase(null, null, null, false, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };
        String result = instance.baseTypeName();
        assertNull(result);
    }

    @Test
    public void testGetPropertyName() {
        TypeDeserializerBase instance = new TypeDeserializerBase(null, null, null, false, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };
        String result = instance.getPropertyName();
        assertNull(result);
    }

    @Test
    public void testGetTypeIdResolver() {
        TypeDeserializerBase instance = new TypeDeserializerBase(null, null, null, false, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };
        TypeIdResolver result = instance.getTypeIdResolver();
        assertNull(result);
    }

    @Test
    public void testGetDefaultImpl() {
        TypeDeserializerBase instance = new TypeDeserializerBase(null, null, null, false, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };
        Class<?> result = instance.getDefaultImpl();
        assertNull(result);
    }

    @Test
    public void testToString() {
        TypeDeserializerBase instance = new TypeDeserializerBase(null, null, null, false, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };
        String result = instance.toString();
        assertNotNull(result);
    }

    @Test
    public void testFindDeserializer() throws IOException {
        DeserializationContext ctxt = null;
        String typeId = "";
        TypeDeserializerBase instance = new TypeDeserializerBase(null, null, null, false, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
            @Override
            protected JsonDeserializer<Object> _findDeserializer(DeserializationContext ctxt, String typeId) throws IOException {
                return null;
            }
        };
        JsonDeserializer<Object> expResult = null;
        JsonDeserializer<Object> result = instance._findDeserializer(ctxt, typeId);
        assertEquals(expResult, result);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        DeserializationContext ctxt = null;
        TypeDeserializerBase instance = new TypeDeserializerBase(null, null, null, false, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
            @Override
            protected JsonDeserializer<Object> _findDefaultImplDeserializer(DeserializationContext ctxt) throws IOException {
                return null;
            }
        };
        JsonDeserializer<Object> expResult = null;
        JsonDeserializer<Object> result = instance._findDefaultImplDeserializer(ctxt);
        assertEquals(expResult, result);
    }

    @Test
    public void testDeserializeWithNativeTypeId_JsonParser_DeserializationContext() throws IOException {
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        TypeDeserializerBase instance = new TypeDeserializerBase(null, null, null, false, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
            @Override
            protected Object _deserializeWithNativeTypeId(JsonParser jp, DeserializationContext ctxt, Object typeId) throws IOException {
                return null;
            }
        };
        Object expResult = null;
        Object result = instance._deserializeWithNativeTypeId(jp, ctxt);
        assertEquals(expResult, result);
    }

    @Test
    public void testDeserializeWithNativeTypeId_3args() throws IOException {
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        Object typeId = null;
        TypeDeserializerBase instance = new TypeDeserializerBase(null, null, null, false, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
            @Override
            protected Object _deserializeWithNativeTypeId(JsonParser jp, DeserializationContext ctxt, Object typeId) throws IOException {
                return null;
            }
        };
        Object expResult = null;
        Object result = instance._deserializeWithNativeTypeId(jp, ctxt, typeId);
        assertEquals(expResult, result);
    }

    @Test
    public void testHandleUnknownTypeId() throws IOException {
        DeserializationContext ctxt = null;
        String typeId = "";
        TypeIdResolver idResolver = null;
        JavaType baseType = null;
        TypeDeserializerBase instance = new TypeDeserializerBase(null, null, null, false, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
            @Override
            protected JsonDeserializer<Object> _handleUnknownTypeId(DeserializationContext ctxt, String typeId, TypeIdResolver idResolver, JavaType baseType) throws IOException {
                return null;
            }
        };
        JsonDeserializer<Object> expResult = null;
        JsonDeserializer<Object> result = instance._handleUnknownTypeId(ctxt, typeId, idResolver, baseType);
        assertEquals(expResult, result);
    }
}