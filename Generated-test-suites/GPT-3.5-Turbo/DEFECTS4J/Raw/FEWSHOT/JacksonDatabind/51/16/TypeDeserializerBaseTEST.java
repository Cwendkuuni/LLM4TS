package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TypeDeserializerBaseTest {
    @Test
    public void testBaseTypeName() {
        TypeDeserializerBase deserializer = new TypeDeserializerBase() {
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }
        };

        assertEquals("java.lang.Object", deserializer.baseTypeName());
    }

    @Test
    public void testGetPropertyName() {
        TypeDeserializerBase deserializer = new TypeDeserializerBase() {
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }
        };

        assertEquals("", deserializer.getPropertyName());
    }

    @Test
    public void testGetTypeIdResolver() {
        TypeDeserializerBase deserializer = new TypeDeserializerBase() {
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }
        };

        assertNull(deserializer.getTypeIdResolver());
    }

    @Test
    public void testGetDefaultImpl() {
        TypeDeserializerBase deserializer = new TypeDeserializerBase() {
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }
        };

        assertNull(deserializer.getDefaultImpl());
    }
}