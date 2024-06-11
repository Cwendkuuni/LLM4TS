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

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
public class TypeDeserializerBaseTest {

    private TypeDeserializerBase typeDeserializerBase;
    
    @Before
    public void setUp() {
        TypeIdResolver idResolver = new TypeIdResolverImpl();
        JavaType baseType = new JavaTypeImpl(Object.class);
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = new JavaTypeImpl(Object.class);
        
        typeDeserializerBase = new TypeDeserializerBaseMock(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
    }
    
    @Test
    public void testForProperty() {
        BeanProperty prop = null;
        
        TypeDeserializer result = typeDeserializerBase.forProperty(prop);
        
        assertNotNull(result);
        assertEquals(TypeDeserializerBaseMock.class, result.getClass());
    }
    
    @Test
    public void testGetTypeInclusion() {
        JsonTypeInfo.As result = typeDeserializerBase.getTypeInclusion();
        
        assertNotNull(result);
        assertEquals(JsonTypeInfo.As.PROPERTY, result);
    }
    
    @Test
    public void testBaseTypeName() {
        String result = typeDeserializerBase.baseTypeName();
        
        assertNotNull(result);
        assertEquals("java.lang.Object", result);
    }
    
    @Test
    public void testGetPropertyName() {
        String result = typeDeserializerBase.getPropertyName();
        
        assertNotNull(result);
        assertEquals("type", result);
    }
    
    @Test
    public void testGetTypeIdResolver() {
        TypeIdResolver result = typeDeserializerBase.getTypeIdResolver();
        
        assertNotNull(result);
        assertEquals(TypeIdResolverImpl.class, result.getClass());
    }
    
    @Test
    public void testGetDefaultImpl() {
        Class<?> result = typeDeserializerBase.getDefaultImpl();
        
        assertNotNull(result);
        assertEquals(Object.class, result);
    }
    
    @Test
    public void testBaseType() {
        JavaType result = typeDeserializerBase.baseType();
        
        assertNotNull(result);
        assertEquals(JavaTypeImpl.class, result.getClass());
    }
    
    @Test
    public void testToString() {
        String result = typeDeserializerBase.toString();
        
        assertNotNull(result);
        assertEquals("[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBaseMock; base-type:com.fasterxml.jackson.databind.jsontype.impl.JavaTypeImpl; id-resolver: com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverImpl]", result);
    }
    
    @Test
    public void testDeserializeWithNativeTypeId() throws IOException {
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        
        Object result = typeDeserializerBase._deserializeWithNativeTypeId(jp, ctxt);
        
        assertNotNull(result);
        assertEquals(Object.class, result.getClass());
    }
    
    @Test
    public void testDeserializeWithNativeTypeIdWithType() throws IOException {
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        Object typeId = "someType";
        
        Object result = typeDeserializerBase._deserializeWithNativeTypeId(jp, ctxt, typeId);
        
        assertNotNull(result);
        assertEquals(Object.class, result.getClass());
    }
    
    @Test
    public void testHandleUnknownTypeId() throws IOException {
        DeserializationContext ctxt = null;
        String typeId = "someType";
        
        JavaType result = typeDeserializerBase._handleUnknownTypeId(ctxt, typeId);
        
        assertNotNull(result);
        assertEquals(JavaTypeImpl.class, result.getClass());
    }
    
    @Test
    public void testHandleMissingTypeId() throws IOException {
        DeserializationContext ctxt = null;
        String extraDesc = "extraDescription";
        
        JavaType result = typeDeserializerBase._handleMissingTypeId(ctxt, extraDesc);
        
        assertNotNull(result);
        assertEquals(JavaTypeImpl.class, result.getClass());
    }
    
    // Mock classes
    
    private class TypeIdResolverImpl implements TypeIdResolver {

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
        public String getDescForKnownTypeIds() {
            return null;
        }

        @Override
        public JsonTypeInfo.Id getMechanism() {
            return null;
        }
        
    }
    
    private class JavaTypeImpl extends JavaType {

        public JavaTypeImpl(Class<?> rawClass) {
            super(rawClass);
        }

        @Override
        public JavaType with(TypeBindings bindings) {
            return null;
        }

    }
    
    private class TypeDeserializerBaseMock extends TypeDeserializerBase {

        public TypeDeserializerBaseMock(JavaType baseType, TypeIdResolver idRes,
                String typePropertyName, boolean typeIdVisible, JavaType defaultImpl) {
            super(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
        }

        @Override
        public TypeDeserializer forProperty(BeanProperty prop) {
            return new TypeDeserializerBaseMock(_baseType, _idResolver, _typePropertyName, _typeIdVisible, _defaultImpl);
        }

        @Override
        public JsonTypeInfo.As getTypeInclusion() {
            return JsonTypeInfo.As.PROPERTY;
        }
        
    }
}