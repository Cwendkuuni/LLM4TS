package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import java.io.IOException;

public class TypeDeserializerBaseTest {

    @Test
    public void testConstructor() {
        JavaType baseType = new JavaType();
        TypeIdResolver idRes = new TypeIdResolver();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = new JavaType();
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
        assertEquals(baseType, typeDeserializerBase._baseType);
        assertEquals(idRes, typeDeserializerBase._idResolver);
        assertEquals(typePropertyName, typeDeserializerBase._typePropertyName);
        assertEquals(typeIdVisible, typeDeserializerBase._typeIdVisible);
        assertEquals(defaultImpl, typeDeserializerBase._defaultImpl);
        assertNull(typeDeserializerBase._property);
    }

    @Test
    public void testConstructorWithProperty() {
        TypeDeserializerBase src = new TypeDeserializerBase();
        BeanProperty property = new BeanProperty();
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(src, property);
        assertEquals(src._baseType, typeDeserializerBase._baseType);
        assertEquals(src._idResolver, typeDeserializerBase._idResolver);
        assertEquals(src._typePropertyName, typeDeserializerBase._typePropertyName);
        assertEquals(src._typeIdVisible, typeDeserializerBase._typeIdVisible);
        assertEquals(src._deserializers, typeDeserializerBase._deserializers);
        assertEquals(src._defaultImpl, typeDeserializerBase._defaultImpl);
        assertEquals(src._defaultImplDeserializer, typeDeserializerBase._defaultImplDeserializer);
        assertEquals(property, typeDeserializerBase._property);
    }

    @Test
    public void testForProperty() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase();
        BeanProperty prop = new BeanProperty();
        TypeDeserializer typeDeserializer = typeDeserializerBase.forProperty(prop);
        assertNotNull(typeDeserializer);
    }

    @Test
    public void testGetTypeInclusion() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase();
        assertEquals(null, typeDeserializerBase.getTypeInclusion());
    }

    @Test
    public void testBaseTypeName() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase();
        assertEquals("java.lang.Object", typeDeserializerBase.baseTypeName());
    }

    @Test
    public void testGetPropertyName() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase();
        assertEquals(null, typeDeserializerBase.getPropertyName());
    }

    @Test
    public void testGetTypeIdResolver() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase();
        assertNull(typeDeserializerBase.getTypeIdResolver());
    }

    @Test
    public void testGetDefaultImpl() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase();
        assertEquals(Object.class, typeDeserializerBase.getDefaultImpl());
    }

    @Test
    public void testBaseType() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase();
        assertNotNull(typeDeserializerBase.baseType());
    }

    @Test
    public void testToString() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase();
        assertNotNull(typeDeserializerBase.toString());
    }

    @Test
    public void test_findDeserializer() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase();
        DeserializationContext ctxt = new DeserializationContext();
        JsonParser jp = new JsonParser();
        String typeId = "Test";
        JsonDeserializer<Object> deser = typeDeserializerBase._findDeserializer(ctxt, typeId);
        assertNull(deser);
    }

    @Test
    public void test_findDefaultImplDeserializer() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase();
        DeserializationContext ctxt = new DeserializationContext();
        JsonDeserializer<Object> deser = typeDeserializerBase._findDefaultImplDeserializer(ctxt);
        assertNotNull(deser);
    }

    @Test
    public void test_handleUnknownTypeId() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase();
        DeserializationContext ctxt = new DeserializationContext();
        String typeId = "Test";
        JavaType actual = typeDeserializerBase._handleUnknownTypeId(ctxt, typeId);
        assertNull(actual);
    }

    @Test
    public void test_handleMissingTypeId() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase();
        DeserializationContext ctxt = new DeserializationContext();
        String extraDesc = "Extra Description";
        JavaType actual = typeDeserializerBase._handleMissingTypeId(ctxt, extraDesc);
        assertNull(actual);
    }
}