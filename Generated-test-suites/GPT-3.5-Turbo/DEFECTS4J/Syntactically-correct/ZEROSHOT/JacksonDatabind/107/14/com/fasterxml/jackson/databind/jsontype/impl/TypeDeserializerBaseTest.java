package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {

    private TypeDeserializerBase typeDeserializerBase;

    @Before
    public void setUp() {
        typeDeserializerBase = new TypeDeserializerBase(null, null, null, false, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };
    }

    @Test
    public void testBaseTypeName() {
        String baseTypeName = typeDeserializerBase.baseTypeName();
        assertNull(baseTypeName);
    }

    @Test
    public void testGetPropertyName() {
        String propertyName = typeDeserializerBase.getPropertyName();
        assertNull(propertyName);
    }

    @Test
    public void testGetTypeIdResolver() {
        TypeIdResolver idResolver = typeDeserializerBase.getTypeIdResolver();
        assertNull(idResolver);
    }

    @Test
    public void testGetDefaultImpl() {
        Class<?> defaultImpl = typeDeserializerBase.getDefaultImpl();
        assertNull(defaultImpl);
    }

    @Test
    public void testBaseType() {
        JavaType baseType = typeDeserializerBase.baseType();
        assertNull(baseType);
    }

    @Test
    public void testToString() {
        String toString = typeDeserializerBase.toString();
        assertNotNull(toString);
    }
}