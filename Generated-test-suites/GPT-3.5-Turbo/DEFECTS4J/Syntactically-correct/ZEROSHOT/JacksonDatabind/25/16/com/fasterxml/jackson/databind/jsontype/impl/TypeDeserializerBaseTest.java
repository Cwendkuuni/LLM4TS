package com.fasterxml.jackson.databind.jsontype.impl;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.core.JsonParser;

import org.junit.Test;
import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {
    
    @Test
    public void testBaseTypeName() {
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBase(null, null, null, false, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }
            
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };
        
        String baseTypeName = typeDeserializer.baseTypeName();
        
        assertNull(baseTypeName);
    }
    
    @Test
    public void testGetPropertyName() {
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBase(null, null, "type", false, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }
            
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };
        
        String propertyName = typeDeserializer.getPropertyName();
        
        assertEquals("type", propertyName);
    }
    
    @Test
    public void testGetTypeIdResolver() {
        TypeIdResolver resolver = new TypeIdResolver() {
            @Override
            public void init(JavaType baseType) {
                
            }
            
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
        };
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBase(null, resolver, null, false, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }
            
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };
        
        TypeIdResolver typeIdResolver = typeDeserializer.getTypeIdResolver();
        
        assertNotNull(typeIdResolver);
    }
    
    @Test
    public void testGetDefaultImpl() {
        JavaType defaultImpl = TypeFactory.unknownType();
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBase(null, null, null, false, defaultImpl) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }
            
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };
        
        assertNotNull(typeDeserializer.getDefaultImpl());
    }
    
    @Test
    public void testToString() {
        JavaType baseType = TypeFactory.unknownType();
        TypeIdResolver resolver = new TypeIdResolver() {
            @Override
            public void init(JavaType baseType) {
                
            }
            
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
        };
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBase(baseType, resolver, "type", false, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }
            
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };
        
        String toString = typeDeserializer.toString();
        
        assertNotNull(toString);
    }
    
    @Test
    public void testFindDeserializer() throws IOException {
        DeserializationContext context = new DeserializationContext(null, null, null);
        String typeId = "type";
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBase(null, null, null, false, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }
            
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };
        
        JsonDeserializer<Object> deserializer = typeDeserializer._findDeserializer(context, typeId);
        
        assertNotNull(deserializer);
    }
    
    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        DeserializationContext context = new DeserializationContext(null, null, null);
        JavaType defaultImpl = TypeFactory.unknownType();
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBase(null, null, null, false, defaultImpl) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }
            
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };
        
        JsonDeserializer<Object> deserializer = typeDeserializer._findDefaultImplDeserializer(context);
        
        assertNull(deserializer);
    }
    
    @Test(expected = IOException.class)
    public void testDeserializeWithNativeTypeIdWithNoTypeId() throws IOException {
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBase(null, null, null, false, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }
            
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };
        JsonParser parser = null;
        DeserializationContext context = null;
        
        Object deserializedObject = typeDeserializer._deserializeWithNativeTypeId(parser, context);
    }
    
    @Test(expected = IOException.class)
    public void testDeserializeWithNativeTypeIdWithTypeId() throws IOException {
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBase(null, null, null, false, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }
            
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };
        JsonParser parser = null;
        DeserializationContext context = null;
        Object typeId = null;
        
        Object deserializedObject = typeDeserializer._deserializeWithNativeTypeId(parser, context, typeId);
    }
    
    @Test(expected = IOException.class)
    public void testHandleUnknownTypeId() throws IOException {
        DeserializationContext context = null;
        String typeId = null;
        TypeIdResolver idResolver = null;
        JavaType baseType = TypeFactory.unknownType();
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBase(null, null, null, false, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }
            
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };
        
        JsonDeserializer<Object> deserializer = typeDeserializer._handleUnknownTypeId(context, typeId, idResolver, baseType);
    }
}