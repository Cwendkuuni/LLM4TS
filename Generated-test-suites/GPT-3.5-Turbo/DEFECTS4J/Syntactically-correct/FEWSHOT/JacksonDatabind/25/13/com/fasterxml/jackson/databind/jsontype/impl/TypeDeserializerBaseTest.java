package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.TypeIdResolver;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;

public class TypeDeserializerBaseTest {

    @Test
    public void testBaseTypeName() {
        TypeDeserializerBase deserializer = createDeserializer();
        assertEquals("java.lang.Object", deserializer.baseTypeName());
    }

    @Test
    public void testGetPropertyName() {
        TypeDeserializerBase deserializer = createDeserializer();
        assertEquals("_typePropertyName", deserializer.getPropertyName());
    }

    @Test
    public void testGetTypeIdResolver() {
        TypeDeserializerBase deserializer = createDeserializer();
        assertNotNull(deserializer.getTypeIdResolver());
    }

    @Test
    public void testGetDefaultImpl() {
        TypeDeserializerBase deserializer = createDeserializer();
        assertNull(deserializer.getDefaultImpl());
    }

    protected TypeDeserializerBase createDeserializer() {
        TypeIdResolver typeIdResolver = null; // replace with actual TypeIdResolver implementation
        JavaType baseType = null; // replace with actual base type
        String typePropertyName = "_typePropertyName";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = null;

        return new TypeDeserializerBase(baseType, typeIdResolver, typePropertyName, typeIdVisible, defaultImpl) {

            private static final long serialVersionUID = 1L;

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }

            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return this;
            }

            @Override
            public Object deserializeTypedFromAny(JsonParser jp, DeserializationContext ctxt) throws IOException {
                return null;
            }

            @Override
            public Object deserializeTypedFromArray(JsonParser jp, DeserializationContext ctxt) throws IOException {
                return null;
            }

            @Override
            public Object deserializeTypedFromObject(JsonParser jp, DeserializationContext ctxt) throws IOException {
                return null;
            }

            @Override
            public Object deserializeTypedFromScalar(JsonParser jp, DeserializationContext ctxt) throws IOException {
                return null;
            }

            @Override
            public Object deserializeTypedFromAny(JsonParser jp, DeserializationContext ctxt, JsonDeserializer<?> defaultImpl) throws IOException {
                return null;
            }

            @Override
            public Object deserializeTypedFromArray(JsonParser jp, DeserializationContext ctxt, JsonDeserializer<?> defaultImpl) throws IOException {
                return null;
            }

            @Override
            public Object deserializeTypedFromObject(JsonParser jp, DeserializationContext ctxt, JsonDeserializer<?> defaultImpl) throws IOException {
                return null;
            }

            @Override
            public Object deserializeTypedFromScalar(JsonParser jp, DeserializationContext ctxt, JsonDeserializer<?> defaultImpl) throws IOException {
                return null;
            }

        };
    }
}