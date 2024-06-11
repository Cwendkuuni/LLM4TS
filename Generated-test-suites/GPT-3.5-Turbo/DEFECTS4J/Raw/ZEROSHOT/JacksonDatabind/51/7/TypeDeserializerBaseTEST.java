package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Test;
import static org.junit.Assert.*;

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

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TypeDeserializerBaseTest {

    @Test
    public void testBaseTypeName() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase() {
            @Override
            public JavaType forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };

        String result = typeDeserializerBase.baseTypeName();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetPropertyName() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase() {
            @Override
            public JavaType forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };

        String result = typeDeserializerBase.getPropertyName();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetTypeIdResolver() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase() {
            @Override
            public JavaType forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };

        TypeIdResolver typeIdResolver = typeDeserializerBase.getTypeIdResolver();

        assertNull(typeIdResolver);
    }

    @Test
    public void testGetDefaultImpl() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase() {
            @Override
            public JavaType forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };

        Class<?> defaultImpl = typeDeserializerBase.getDefaultImpl();

        assertNull(defaultImpl);
    }

    @Test
    public void testToString() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase() {
            @Override
            public JavaType forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };

        String result = typeDeserializerBase.toString();

        assertNotNull(result);
        assertTrue(result.contains("base-type"));
        assertTrue(result.contains("id-resolver"));
    }

    @Test
    public void testFindDeserializer() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase() {
            @Override
            public JavaType forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };

        DeserializationContext deserializationContext = new DeserializationContext(null, null, null);

        JsonDeserializer result = typeDeserializerBase._findDeserializer(deserializationContext, "testTypeId");

        assertNull(result);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase() {
            @Override
            public JavaType forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };

        DeserializationContext deserializationContext = new DeserializationContext(null, null, null);

        JsonDeserializer result = typeDeserializerBase._findDefaultImplDeserializer(deserializationContext);

        assertNull(result);
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase() {
            @Override
            public JavaType forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };

        JsonParser jsonParser = new JsonParser(null, null);
        DeserializationContext deserializationContext = new DeserializationContext(null, null, null);

        Object result = typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, deserializationContext);

        assertNull(result);
    }

    @Test
    public void testDeserializeWithNativeTypeId2() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase() {
            @Override
            public JavaType forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };

        JsonParser jsonParser = new JsonParser(null, null);
        DeserializationContext deserializationContext = new DeserializationContext(null, null, null);

        Object result = typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, deserializationContext, "testTypeId");

        assertNull(result);
    }

    @Test
    public void testHandleUnknownTypeId() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase() {
            @Override
            public JavaType forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };

        DeserializationContext deserializationContext = new DeserializationContext(null, null, null);

        JavaType result = typeDeserializerBase._handleUnknownTypeId(deserializationContext, "testTypeId", null, null);

        assertNull(result);
    }
}