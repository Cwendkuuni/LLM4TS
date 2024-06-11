package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TypeDeserializerBaseTest {

    @Test
    @DisplayName("Test forProperty method")
    public void testForProperty() {
        JavaType baseType = mock(JavaType.class);
        TypeIdResolver idRes = mock(TypeIdResolver.class);
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = mock(JavaType.class);
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBase(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };
        
        BeanProperty prop = mock(BeanProperty.class);
        TypeDeserializer result = typeDeserializer.forProperty(prop);

        assertNull(result);
    }
    
    @Test
    @DisplayName("Test getTypeInclusion method")
    public void testGetTypeInclusion() {
        JavaType baseType = mock(JavaType.class);
        TypeIdResolver idRes = mock(TypeIdResolver.class);
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = mock(JavaType.class);
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBase(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return JsonTypeInfo.As.EXTERNAL_PROPERTY;
            }
        };

        JsonTypeInfo.As result = typeDeserializer.getTypeInclusion();

        assertEquals(JsonTypeInfo.As.EXTERNAL_PROPERTY, result);
    }
    
    @Test
    @DisplayName("Test baseTypeName method")
    public void testBaseTypeName() {
        JavaType baseType = mock(JavaType.class);
        TypeIdResolver idRes = mock(TypeIdResolver.class);
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = mock(JavaType.class);
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBase(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };

        String result = typeDeserializer.baseTypeName();

        assertEquals("java.lang.Object", result);
    }
    
    @Test
    @DisplayName("Test getPropertyName method")
    public void testGetPropertyName() {
        JavaType baseType = mock(JavaType.class);
        TypeIdResolver idRes = mock(TypeIdResolver.class);
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = mock(JavaType.class);
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBase(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };

        String result = typeDeserializer.getPropertyName();

        assertEquals("type", result);
    }
    
    @Test
    @DisplayName("Test getTypeIdResolver method")
    public void testGetTypeIdResolver() {
        JavaType baseType = mock(JavaType.class);
        TypeIdResolver idRes = mock(TypeIdResolver.class);
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = mock(JavaType.class);
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBase(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };

        TypeIdResolver result = typeDeserializer.getTypeIdResolver();

        assertEquals(idRes, result);
    }
    
    @Test
    @DisplayName("Test getDefaultImpl method")
    public void testGetDefaultImpl() {
        JavaType baseType = mock(JavaType.class);
        TypeIdResolver idRes = mock(TypeIdResolver.class);
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = mock(JavaType.class);
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBase(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };

        Class<?> result = typeDeserializer.getDefaultImpl();

        assertEquals(ClassUtil.rawClass(defaultImpl), result);
    }
    
    @Test
    @DisplayName("Test baseType method")
    public void testBaseType() {
        JavaType baseType = mock(JavaType.class);
        TypeIdResolver idRes = mock(TypeIdResolver.class);
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = mock(JavaType.class);
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBase(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }
            
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };
        
        JavaType result = typeDeserializer.baseType();
        
        assertEquals(baseType, result);
    }

}