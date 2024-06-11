package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Test;
import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {

    @Test
    public void testForProperty() {
        BeanProperty property = null;
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
        TypeDeserializer result = instance.forProperty(property);
        assertNull(result);
    }

    @Test
    public void testGetPropertyName() {
        TypeDeserializerBase instance = new TypeDeserializerBase(null, null, "type", false, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };
        String expResult = "type";
        String result = instance.getPropertyName();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetTypeIdResolver() {
        TypeIdResolver idResolver = null;
        TypeDeserializerBase instance = new TypeDeserializerBase(null, idResolver, null, false, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };
        TypeIdResolver expResult = idResolver;
        TypeIdResolver result = instance.getTypeIdResolver();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetDefaultImpl() {
        Class<?> defaultImpl = null;
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
        Class<?> expResult = null;
        Class<?> result = instance.getDefaultImpl();
        assertEquals(expResult, result);
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
        String expResult = "[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase; base-type:null; id-resolver: null]";
        String result = instance.toString();
        assertEquals(expResult, result);
    }

    @Test
    public void test_findDeserializer() throws Exception {
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
        };
        JsonDeserializer<Object> expResult = null;
        JsonDeserializer<Object> result = instance._findDeserializer(ctxt, typeId);
        assertEquals(expResult, result);
    }

    @Test
    public void test_findDefaultImplDeserializer() throws Exception {
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
        };
        JsonDeserializer<Object> expResult = null;
        JsonDeserializer<Object> result = instance._findDefaultImplDeserializer(ctxt);
        assertEquals(expResult, result);
    }

    @Test
    public void test_deserializeWithNativeTypeId() throws Exception {
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
                return super._deserializeWithNativeTypeId(jp, ctxt, typeId); //To change body of generated methods, choose Tools | Templates.
            }
        };
        Object expResult = null;
        Object result = instance._deserializeWithNativeTypeId(jp, ctxt);
        assertEquals(expResult, result);
    }

    @Test
    public void test_handleUnknownTypeId() throws Exception {
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
                return super._handleUnknownTypeId(ctxt, typeId, idResolver, baseType); //To change body of generated methods, choose Tools | Templates.
            }
        };
        JsonDeserializer<Object> expResult = null;
        JsonDeserializer<Object> result = instance._handleUnknownTypeId(ctxt, typeId, idResolver, baseType);
        assertEquals(expResult, result);
    }
}