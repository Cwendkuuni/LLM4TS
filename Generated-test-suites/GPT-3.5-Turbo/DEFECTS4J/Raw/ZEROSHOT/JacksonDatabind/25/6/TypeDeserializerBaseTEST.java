package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Test;
import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {

    @Test
    public void test_baseTypeName() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase() {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };
        String baseTypeName = typeDeserializerBase.baseTypeName();
        assertEquals("com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase", baseTypeName);
    }

    @Test
    public void test_getPropertyName() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase() {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };
        String propertyName = typeDeserializerBase.getPropertyName();
        assertNull(propertyName);
    }

    @Test
    public void test_getTypeIdResolver() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase() {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
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
    public void test_getDefaultImpl() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase() {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
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

}