package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

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

public class TypeDeserializerBaseTest {
    
    private TypeDeserializerBase typeDeserializer;
    
    @Before
    public void setUp() {
        JavaType baseType = null;
        TypeIdResolver idRes = null;
        String typePropertyName = null;
        boolean typeIdVisible = false;
        Class<?> defaultImpl = null;
        
        typeDeserializer = new TypeDeserializerBaseTest(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
    }
    
    @Test
    public void testBaseTypeName() {
        String expected = null;
        String actual = typeDeserializer.baseTypeName();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetPropertyName() {
        String expected = null;
        String actual = typeDeserializer.getPropertyName();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetTypeIdResolver() {
        TypeIdResolver expected = null;
        TypeIdResolver actual = typeDeserializer.getTypeIdResolver();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetDefaultImpl() {
        Class<?> expected = null;
        Class<?> actual = typeDeserializer.getDefaultImpl();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testToString() {
        String expected = "[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase]";
        String actual = typeDeserializer.toString();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testFindDeserializer() throws IOException {
        DeserializationContext ctxt = null;
        String typeId = null;
        
        JsonDeserializer<Object> expected = null;
        JsonDeserializer<Object> actual = typeDeserializer._findDeserializer(ctxt, typeId);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        DeserializationContext ctxt = null;
        
        JsonDeserializer<Object> expected = null;
        JsonDeserializer<Object> actual = typeDeserializer._findDefaultImplDeserializer(ctxt);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testDeserializeWithNativeTypeId1() throws IOException {
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        
        Object expected = null;
        Object actual = typeDeserializer._deserializeWithNativeTypeId(jp, ctxt);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testDeserializeWithNativeTypeId2() throws IOException {
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        Object typeId = null;
        
        Object expected = null;
        Object actual = typeDeserializer._deserializeWithNativeTypeId(jp, ctxt, typeId);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testHandleUnknownTypeId() throws IOException {
        DeserializationContext ctxt = null;
        String typeId = null;
        TypeIdResolver idResolver = null;
        JavaType baseType = null;
        
        JsonDeserializer<Object> expected = null;
        JsonDeserializer<Object> actual = typeDeserializer._handleUnknownTypeId(ctxt, typeId, idResolver, baseType);
        assertEquals(expected, actual);
    }
    
}