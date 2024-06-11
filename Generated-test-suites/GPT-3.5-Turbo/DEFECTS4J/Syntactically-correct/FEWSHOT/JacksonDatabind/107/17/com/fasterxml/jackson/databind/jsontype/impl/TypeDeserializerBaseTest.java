package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
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
    private TypeDeserializerBase deserializer;

    @Before
    public void setUp() {
        JavaType baseType = null;
        TypeIdResolver idRes = null;
        String typePropertyName = "";
        boolean typeIdVisible = false;
        JavaType defaultImpl = null;
        deserializer = new TypeDeserializerBaseImpl(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
    }

    @Test
    public void testForProperty() {
        BeanProperty prop = null;
        TypeDeserializer result = deserializer.forProperty(prop);
        assertNull(result);
    }

    @Test
    public void testGetTypeInclusion() {
        JsonTypeInfo.As result = deserializer.getTypeInclusion();
        assertNull(result);
    }

    @Test
    public void testBaseTypeName() {
        String result = deserializer.baseTypeName();
        assertNull(result);
    }

    @Test
    public void testGetPropertyName() {
        String result = deserializer.getPropertyName();
        assertEquals("", result);
    }

    @Test
    public void testGetTypeIdResolver() {
        TypeIdResolver result = deserializer.getTypeIdResolver();
        assertNull(result);
    }

    @Test
    public void testGetDefaultImpl() {
        Class<?> result = deserializer.getDefaultImpl();
        assertNull(result);
    }

    @Test
    public void testBaseType() {
        JavaType result = deserializer.baseType();
        assertNull(result);
    }

    @Test
    public void testToString() {
        String result = deserializer.toString();
        assertEquals("[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBaseImpl; base-type:null; id-resolver: null]", result);
    }

    @Test
    public void testDeserializeWithNativeTypeId1() throws IOException {
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        Object typeId = null;
        Object result = deserializer._deserializeWithNativeTypeId(jp, ctxt, typeId);
        assertNull(result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeWithNativeTypeId2() throws IOException {
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        Object typeId = "";
        deserializer._deserializeWithNativeTypeId(jp, ctxt, typeId);
    }

    @Test
    public void testDeserializeWithNativeTypeId3() throws IOException {
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        Object typeId = "typeId";
        Object result = deserializer._deserializeWithNativeTypeId(jp, ctxt, typeId);
        assertNull(result);
    }

    @Test
    public void testHandleUnknownTypeId() throws IOException {
        DeserializationContext ctxt = null;
        String typeId = "";
        JavaType result = deserializer._handleUnknownTypeId(ctxt, typeId);
        assertNull(result);
    }

    @Test
    public void testHandleMissingTypeId() throws IOException {
        DeserializationContext ctxt = null;
        String extraDesc = "";
        JavaType result = deserializer._handleMissingTypeId(ctxt, extraDesc);
        assertNull(result);
    }

    private static class TypeDeserializerBaseImpl extends TypeDeserializerBase {
        protected TypeDeserializerBaseImpl(JavaType baseType, TypeIdResolver idRes, String typePropertyName,
                boolean typeIdVisible, JavaType defaultImpl) {
            super(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
        }

        @Override
        public TypeDeserializer forProperty(BeanProperty prop) {
            return null;
        }

        @Override
        public JsonTypeInfo.As getTypeInclusion() {
            return null;
        }
    }
}