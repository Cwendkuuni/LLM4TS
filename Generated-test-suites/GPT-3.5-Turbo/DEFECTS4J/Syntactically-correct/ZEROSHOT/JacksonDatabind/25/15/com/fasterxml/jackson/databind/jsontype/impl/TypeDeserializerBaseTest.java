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
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {
    
    private TypeDeserializerBase typeDeserializerBase;
    private JavaType baseType;
    private TypeIdResolver idResolver;
    private String typePropertyName;
    private boolean typeIdVisible;
    private Class<?> defaultImpl;
    
    @Before
    public void setUp() {
        baseType = new JavaType();
        idResolver = new TypeIdResolver();
        typePropertyName = "type";
        typeIdVisible = true;
        defaultImpl = null;
        
        typeDeserializerBase = new TypeDeserializerBaseImpl(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
    }
    
    @Test
    public void testForProperty() {
        BeanProperty property = new BeanProperty();
        TypeDeserializer result = typeDeserializerBase.forProperty(property);
        assertNotNull(result);
    }
    
    @Test
    public void testGetTypeInclusion() {
        JsonTypeInfo.As result = typeDeserializerBase.getTypeInclusion();
        assertNotNull(result);
    }
    
   @Test
    public void testBaseTypeName() {
        String result = typeDeserializerBase.baseTypeName();
        assertNotNull(result);
    }
    
    @Test
    public void testGetPropertyName() {
        String result = typeDeserializerBase.getPropertyName();
        assertNotNull(result);
    }
    
    @Test
    public void testGetTypeIdResolver() {
        TypeIdResolver result = typeDeserializerBase.getTypeIdResolver();
        assertNotNull(result);
    }
    
    @Test
    public void testGetDefaultImpl() {
        Class<?> result = typeDeserializerBase.getDefaultImpl();
        assertNull(result);
    }
    
    @Test
    public void testToString() {
        String result = typeDeserializerBase.toString();
        assertNotNull(result);
    }
    
    @Test
    public void testFindDeserializer() throws IOException {
        DeserializationContext ctxt = new DeserializationContext();
        String typeId = "typeId";
        JsonDeserializer<Object> result = typeDeserializerBase._findDeserializer(ctxt, typeId);
        assertNotNull(result);
    }
    
    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        DeserializationContext ctxt = new DeserializationContext();
        JsonDeserializer<Object> result = typeDeserializerBase._findDefaultImplDeserializer(ctxt);
        assertNull(result);
    }
    
    @Test
    public void testDeserializeWithNativeTypeId1() throws IOException {
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object result = typeDeserializerBase._deserializeWithNativeTypeId(jp, ctxt);
        assertNotNull(result);
    }
    
    @Test
    public void testDeserializeWithNativeTypeId2() throws IOException {
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object typeId = new Object();
        Object result = typeDeserializerBase._deserializeWithNativeTypeId(jp, ctxt, typeId);
        assertNotNull(result);
    }
    
    @Test
    public void testHandleUnknownTypeId() throws IOException {
        DeserializationContext ctxt = new DeserializationContext();
        String typeId = "typeId";
        TypeIdResolver idResolver = new TypeIdResolver();
        JavaType baseType = new JavaType();
        
        try {
            typeDeserializerBase._handleUnknownTypeId(ctxt, typeId, idResolver, baseType);
            fail("Expected exception to be thrown");
        } catch (IOException ex) {
            String message = ex.getMessage();
            assertNotNull(message);
        }
    }
}