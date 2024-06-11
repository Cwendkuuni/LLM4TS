package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Test;
import static org.junit.Assert.*;

public class AsPropertyTypeDeserializerTest {

    @Test
    public void testConstructor1() {
        JavaType bt = new JavaType();
        TypeIdResolver idRes = new TypeIdResolver();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = new JavaType();
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(bt, idRes, typePropertyName, typeIdVisible, defaultImpl);
        assertEquals(bt, deserializer.getBt());
        assertEquals(idRes, deserializer.getIdRes());
        assertEquals(typePropertyName, deserializer.getTypePropertyName());
        assertEquals(typeIdVisible, deserializer.getTypeIdVisible());
        assertEquals(defaultImpl, deserializer.getDefaultImpl());
        assertEquals(As.PROPERTY, deserializer.getTypeInclusion());
    }

    @Test
    public void testConstructor2() {
        JavaType bt = new JavaType();
        TypeIdResolver idRes = new TypeIdResolver();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = new JavaType();
        As inclusion = As.EXTERNAL_PROPERTY;
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(bt, idRes, typePropertyName, typeIdVisible, defaultImpl, inclusion);
        assertEquals(bt, deserializer.getBt());
        assertEquals(idRes, deserializer.getIdRes());
        assertEquals(typePropertyName, deserializer.getTypePropertyName());
        assertEquals(typeIdVisible, deserializer.getTypeIdVisible());
        assertEquals(defaultImpl, deserializer.getDefaultImpl());
        assertEquals(inclusion, deserializer.getTypeInclusion());
    }

    @Test
    public void testForProperty() {
        JavaType bt = new JavaType();
        TypeIdResolver idRes = new TypeIdResolver();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = new JavaType();
        As inclusion = As.EXTERNAL_PROPERTY;
        AsPropertyTypeDeserializer src = new AsPropertyTypeDeserializer(bt, idRes, typePropertyName, typeIdVisible, defaultImpl, inclusion);
        BeanProperty property = new BeanProperty();
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(src, property);
        assertEquals(src.getTypeInclusion(), deserializer.getTypeInclusion());
        assertEquals(property, deserializer.getProperty());
    }

    @Test
    public void testGetTypeInclusion() {
        JavaType bt = new JavaType();
        TypeIdResolver idRes = new TypeIdResolver();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = new JavaType();
        As inclusion = As.EXTERNAL_PROPERTY;
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(bt, idRes, typePropertyName, typeIdVisible, defaultImpl, inclusion);
        assertEquals(inclusion, deserializer.getTypeInclusion());
    }

    @Test
    public void testDeserializeTypedFromObjectWithNativeTypeId() {
        JavaType bt = new JavaType();
        TypeIdResolver idRes = new TypeIdResolver();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = new JavaType();
        As inclusion = As.EXTERNAL_PROPERTY;
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(bt, idRes, typePropertyName, typeIdVisible, defaultImpl, inclusion);
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object result = deserializer.deserializeTypedFromObject(p, ctxt);
        assertNull(result);
    }

    @Test
    public void testDeserializeTypedFromObjectWithStartObjectToken() {
        // TODO: implement test
    }

    @Test
    public void testDeserializeTypedFromObjectWithFieldToken() {
        // TODO: implement test
    }

    @Test
    public void testDeserializeTypedFromObjectWithDefaultImpl() {
        // TODO: implement test
    }

    @Test
    public void testDeserializeTypedFromAnyWithStartArrayToken() {
        // TODO: implement test
    }

    @Test
    public void testDeserializeTypedFromAny() {
        // TODO: implement test
    }
}