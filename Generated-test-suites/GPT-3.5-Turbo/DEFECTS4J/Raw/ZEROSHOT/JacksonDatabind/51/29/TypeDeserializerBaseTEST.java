package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

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

    @Test
    public void testBaseTypeName() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, false, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };

        assertEquals("java.lang.Object", typeDeserializerBase.baseTypeName());
    }
    
    @Test
    public void testGetPropertyName() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, "type", true, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };
        
        assertEquals("type", typeDeserializerBase.getPropertyName());
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
            public JavaType typeFromId(DatabindContext context, String id) {
                return null;
            }

            @Override
            public JsonTypeInfo.Id getMechanism() {
                return null;
            }
        };
        
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, resolver, null, false, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };
        
        assertEquals(resolver, typeDeserializerBase.getTypeIdResolver());
    }
    
    @Test
    public void testGetDefaultImpl() {
        JavaType defaultImpl = new JavaType(null, null, null, null, null);
        
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(defaultImpl, null, null, false, defaultImpl) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };
        
        assertEquals(JavaType.class, typeDeserializerBase.getDefaultImpl());
    }
    
    @Test
    public void testToString() {
        JavaType baseType = new JavaType(null, null, null, null, null);
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
            public JavaType typeFromId(DatabindContext context, String id) {
                return null;
            }

            @Override
            public JsonTypeInfo.Id getMechanism() {
                return null;
            }
        };
        
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, resolver, null, false, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };
        
        String expected = "[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase; base-type:null; id-resolver: com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBaseTest$1]";
        assertEquals(expected, typeDeserializerBase.toString());
    }
    
    @Test
    public void testDeserializeWithNativeTypeId() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, false, null) {

            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
            
            protected Object _deserializeWithNativeTypeId(JsonParser jp, DeserializationContext ctxt, Object typeId)
                throws IOException
            {
                return "mocked";
            }
        };
        
        assertEquals("mocked", typeDeserializerBase._deserializeWithNativeTypeId(null, null));
    }
    
    @Test
    public void testHandleUnknownTypeId() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, false, null) {

            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
            
            protected JavaType _handleUnknownTypeId(DeserializationContext ctxt, String typeId,
                    TypeIdResolver idResolver, JavaType baseType)
                throws IOException
            {
                return null;
            }
        };
        
        assertNull(typeDeserializerBase._handleUnknownTypeId(null, null, null, null));
    }
}