package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {

    @Mock
    private DeserializationContext context;

    @Mock
    private JsonParser jsonParser;

    @Test
    public void testForProperty() {
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

        BeanProperty property = null;
        TypeDeserializerBase result = typeDeserializerBase.forProperty(property);

        assertNull(result);
    }

    @Test
    public void testBaseTypeName() {
        JavaType javaType = null;
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(javaType, null, null, false, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };

        String result = typeDeserializerBase.baseTypeName();

        assertNull(result);
    }

    @Test
    public void testGetPropertyName() {
        JavaType javaType = null;
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(javaType, null, "TestProperty", false, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };

        String result = typeDeserializerBase.getPropertyName();

        assertEquals("TestProperty", result);
    }

    @Test
    public void testGetTypeIdResolver() {
        TypeIdResolver typeIdResolver = null;
        JavaType javaType = null;
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(javaType, typeIdResolver, null, false, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };

        TypeIdResolver result = typeDeserializerBase.getTypeIdResolver();

        assertNull(result);
    }

    @Test
    public void testGetDefaultImpl() {
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

        Class<?> result = typeDeserializerBase.getDefaultImpl();

        assertNull(result);
    }

    @Test
    public void testToString() {
        JavaType javaType = null;
        TypeIdResolver typeIdResolver = null;
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(javaType, typeIdResolver, null, false, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };

        String result = typeDeserializerBase.toString();

        assertEquals("[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase; base-type:null; id-resolver: null]", result);
    }

    @Test
    public void test_findDeserializer() throws IOException {
        JavaType javaType = null;
        TypeIdResolver typeIdResolver = null;
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(javaType, typeIdResolver, null, false, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };

        DeserializationContext context = null;
        String typeId = null;
        JsonDeserializer<Object> result = typeDeserializerBase._findDeserializer(context, typeId);

        assertNull(result);
    }

    @Test
    public void test_findDefaultImplDeserializer() throws IOException {
        JavaType javaType = null;
        TypeIdResolver typeIdResolver = null;
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(javaType, typeIdResolver, null, false, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };

        DeserializationContext context = null;
        JsonDeserializer<Object> result = typeDeserializerBase._findDefaultImplDeserializer(context);

        assertNotNull(result);
        assertTrue(result instanceof NullifyingDeserializer);
    }

    @Test
    public void test_deserializeWithNativeTypeId() throws IOException {
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

        JsonParser jp = null;
        DeserializationContext ctxt = null;
        Object result = typeDeserializerBase._deserializeWithNativeTypeId(jp, ctxt);

        assertNull(result);
    }

    @Test
    public void test_deserializeWithNativeTypeId_withTypeId() throws IOException {
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

        JsonParser jp = null;
        DeserializationContext ctxt = null;
        Object typeId = null;
        Object result = typeDeserializerBase._deserializeWithNativeTypeId(jp, ctxt, typeId);

        assertNull(result);
    }

    @Test
    public void test_handleUnknownTypeId() throws IOException {
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

        DeserializationContext ctxt = null;
        String typeId = null;
        TypeIdResolver idResolver = null;
        JavaType baseType = null;
        JavaType result = typeDeserializerBase._handleUnknownTypeId(ctxt, typeId, idResolver, baseType);

        assertNull(result);
    }
}