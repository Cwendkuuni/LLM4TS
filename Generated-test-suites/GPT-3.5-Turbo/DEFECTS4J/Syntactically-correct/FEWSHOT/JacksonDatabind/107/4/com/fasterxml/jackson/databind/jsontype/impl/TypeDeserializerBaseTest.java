package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

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
    
    @Test
    public void testBaseTypeName() {
        // Arrange
        TypeDeserializerBase deserializer = new TypeDeserializerBaseStub(null, null, null, false, null);
        
        // Act
        String result = deserializer.baseTypeName();
        
        // Assert
        assertEquals("java.lang.Object", result);
    }

    @Test
    public void testGetPropertyName() {
        // Arrange
        TypeDeserializerBase deserializer = new TypeDeserializerBaseStub(null, null, "type", false, null);
        
        // Act
        String result = deserializer.getPropertyName();
        
        // Assert
        assertEquals("type", result);
    }

    @Test    
    public void testGetTypeIdResolver() {
        // Arrange
        TypeIdResolver resolver = new TypeIdResolverStub();
        TypeDeserializerBase deserializer = new TypeDeserializerBaseStub(null, resolver, null, false, null);
        
        // Act
        TypeIdResolver result = deserializer.getTypeIdResolver();
        
        // Assert
        assertSame(resolver, result);
    }

    @Test    
    public void testGetDefaultImpl() {
        // Arrange
        JavaType defaultImpl = new JavaTypeStub();
        TypeDeserializerBase deserializer = new TypeDeserializerBaseStub(null, null, null, false, defaultImpl);
        
        // Act
        Class<?> result = deserializer.getDefaultImpl();
        
        // Assert
        assertEquals(Integer.class, result);
    }

    @Test
    public void testBaseType() {
        // Arrange
        JavaType baseType = new JavaTypeStub();
        TypeDeserializerBase deserializer = new TypeDeserializerBaseStub(baseType, null, null, false, null);
        
        // Act
        JavaType result = deserializer.baseType();
        
        // Assert
        assertSame(baseType, result);
    }
}

class TypeDeserializerBaseStub extends TypeDeserializerBase {

    private static final long serialVersionUID = 1L;

    public TypeDeserializerBaseStub(JavaType baseType, TypeIdResolver idRes, String typePropertyName,
            boolean typeIdVisible, JavaType defaultImpl) {
        super(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
    }

    public TypeDeserializerBaseStub(TypeDeserializerBase src, BeanProperty property) {
        super(src, property);
    }

    @Override
    public TypeDeserializer forProperty(BeanProperty prop) {
        return null;
    }
    
    @Override
    public JsonTypeInfo.As getTypeInclusion() {
        return null;
    }
    
    @Override
    protected Object _deserializeWithNativeTypeId(JsonParser jp, DeserializationContext ctxt)
            throws IOException {
        return null;
    }
    
    @Override
    protected Object _deserializeWithNativeTypeId(JsonParser jp, DeserializationContext ctxt, Object typeId)
            throws IOException {
        return null;
    }
    
    @Override
    protected JavaType _handleMissingTypeId(DeserializationContext ctxt, String extraDesc) throws IOException {
        return null;
    }

    @Override
    protected JavaType _handleUnknownTypeId(DeserializationContext ctxt, String typeId) throws IOException {
        return null;
    }
}

class JavaTypeStub extends JavaType {

    private static final long serialVersionUID = 1L;

    public JavaTypeStub() {
        super(Object.class);
    }
}

class TypeIdResolverStub extends TypeIdResolver {

    @Override
    public String idFromValue(Object value) {
        return null;
    }

    @Override
    public String idFromValueAndType(Object value, Class<?> suggestedType) {
        return null;
    }

    @Override
    public String idFromBaseType() {
        return null;
    }

    @Override
    public JavaType typeFromId(DatabindContext context, String id) {
        return null;
    }

    @Override
    public JsonTypeInfo.Id getMechanism() {
        return null;
    }
}