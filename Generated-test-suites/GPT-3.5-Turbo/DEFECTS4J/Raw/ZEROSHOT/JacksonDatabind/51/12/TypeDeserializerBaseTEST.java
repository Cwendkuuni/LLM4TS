package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class TypeDeserializerBaseTest {

    private TypeDeserializerBase typeDeserializer;

    public void setUp() {
        JavaType baseType = null;
        TypeIdResolver idResolver = null;
        String typePropertyName = "";
        boolean typeIdVisible = true;
        JavaType defaultImpl = null;
        typeDeserializer = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl) {
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

    //@Test
    public void testBaseTypeName() {
        assertEquals("com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase",
                typeDeserializer.baseTypeName());
    }

    //@Test
    public void testGetPropertyName() {
        assertEquals("", typeDeserializer.getPropertyName());
    }

    //@Test
    public void testGetTypeIdResolver() {
        assertNull(typeDeserializer.getTypeIdResolver());
    }

    //@Test
    public void testGetDefaultImpl() {
        assertNull(typeDeserializer.getDefaultImpl());
    }

    //@Test
    public void testToString() {
        assertEquals("[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase; base-type:null; id-resolver: null]",
                typeDeserializer.toString());
    }

    //@Test
    public void testFindDeserializer() throws IOException {
        DeserializationContext ctxt = null;
        String typeId = null;
        JsonDeserializer<Object> deser = typeDeserializer._findDeserializer(ctxt, typeId);
        assertNull(deser);
    }

    //@Test
    public void testFindDefaultImplDeserializer() throws IOException {
        DeserializationContext ctxt = null;
        JsonDeserializer<Object> deser = typeDeserializer._findDefaultImplDeserializer(ctxt);
        assertNull(deser);
    }

    //@Test
    public void testDeserializeWithNativeTypeId1() throws IOException {
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        Object result = typeDeserializer._deserializeWithNativeTypeId(jp, ctxt);
        assertNull(result);
    }

    //@Test
    public void testDeserializeWithNativeTypeId2() throws IOException {
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        Object typeId = null;
        Object result = typeDeserializer._deserializeWithNativeTypeId(jp, ctxt, typeId);
        assertNull(result);
    }

    //@Test
    public void testHandleUnknownTypeId() throws IOException {
        DeserializationContext ctxt = null;
        String typeId = null;
        TypeIdResolver idResolver = null;
        JavaType baseType = null;
        JavaType result = typeDeserializer._handleUnknownTypeId(ctxt, typeId, idResolver, baseType);
        assertNull(result);
    }

}