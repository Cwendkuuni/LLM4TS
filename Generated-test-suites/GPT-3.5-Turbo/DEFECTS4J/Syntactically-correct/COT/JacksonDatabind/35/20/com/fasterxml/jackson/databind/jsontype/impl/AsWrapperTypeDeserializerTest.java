
package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class AsWrapperTypeDeserializerTest {
    
    // Test class instantiation
    @Test
    public void testAsWrapperTypeDeserializer() {
        JavaType bt = new JavaType();
        TypeIdResolver idRes = new TypeIdResolver();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = Object.class;
        
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(bt, idRes, typePropertyName, typeIdVisible, defaultImpl);
        
        assertNotNull(deserializer);
    }
    
    // Test forProperty() method
    @Test
    public void testForProperty() {
        JavaType bt = new JavaType();
        TypeIdResolver idRes = new TypeIdResolver();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = Object.class;
        BeanProperty prop = new BeanProperty();
        
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(bt, idRes, typePropertyName, typeIdVisible, defaultImpl);
        TypeDeserializer typeDeserializer = deserializer.forProperty(prop);
        
        assertTrue(typeDeserializer instanceof AsWrapperTypeDeserializer);
        assertNotSame(deserializer, typeDeserializer);
    }
    
    // Test getTypeInclusion()
    @Test
    public void testGetTypeInclusion() {
        JavaType bt = new JavaType();
        TypeIdResolver idRes = new TypeIdResolver();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = Object.class;
        
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(bt, idRes, typePropertyName, typeIdVisible, defaultImpl);
        
        assertEquals(As.WRAPPER_OBJECT, deserializer.getTypeInclusion());
    }
    
    // Test deserializeTypedFromObject()
    @Test
    public void testDeserializeTypedFromObject() throws Exception {
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        
        JavaType bt = new JavaType();
        TypeIdResolver idRes = new TypeIdResolver();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = Object.class;
        
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(bt, idRes, typePropertyName, typeIdVisible, defaultImpl);
        
        Object result = deserializer.deserializeTypedFromObject(jp, ctxt);
        
        assertNotNull(result);
    }
    
    // Test deserializeTypedFromArray()
    @Test
    public void testDeserializeTypedFromArray() throws Exception {
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        
        JavaType bt = new JavaType();
        TypeIdResolver idRes = new TypeIdResolver();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = Object.class;
        
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(bt, idRes, typePropertyName, typeIdVisible, defaultImpl);
        
        Object result = deserializer.deserializeTypedFromArray(jp, ctxt);
        
        assertNotNull(result);
    }
    
    // Test deserializeTypedFromScalar()
    @Test
    public void testDeserializeTypedFromScalar() throws Exception {
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        
        JavaType bt = new JavaType();
        TypeIdResolver idRes = new TypeIdResolver();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = Object.class;
        
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(bt, idRes, typePropertyName, typeIdVisible, defaultImpl);
        
        Object result = deserializer.deserializeTypedFromScalar(jp, ctxt);
        
        assertNotNull(result);
    }
    
    // Test deserializeTypedFromAny()
    @Test
    public void testDeserializeTypedFromAny() throws Exception {
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        
        JavaType bt = new JavaType();
        TypeIdResolver idRes = new TypeIdResolver();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = Object.class;
        
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(bt, idRes, typePropertyName, typeIdVisible, defaultImpl);
        
        Object result = deserializer.deserializeTypedFromAny(jp, ctxt);
        
        assertNotNull(result);
    }
    
    // Test _deserialize()
    @Test
    public void testDeserialize() throws Exception {
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        
        JavaType bt = new JavaType();
        TypeIdResolver idRes = new TypeIdResolver();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = Object.class;
        
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(bt, idRes, typePropertyName, typeIdVisible, defaultImpl);
        
        Object result = deserializer._deserialize(p, ctxt);
        
        assertNotNull(result);
    }
    
}
