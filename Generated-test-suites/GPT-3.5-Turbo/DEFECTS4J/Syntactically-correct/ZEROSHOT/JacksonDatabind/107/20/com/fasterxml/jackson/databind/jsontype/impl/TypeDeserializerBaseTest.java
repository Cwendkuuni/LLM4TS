package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Test;
import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {

    @Test
    public void testBaseTypeName() {
        TypeDeserializerBase deserializer = new TypeDeserializerBaseTest.TestDeserializer();
        String baseTypeName = deserializer.baseTypeName();
        assertEquals("com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBaseTest", baseTypeName);
    }

    @Test
    public void testGetPropertyName() {
        TypeDeserializerBase deserializer = new TypeDeserializerBaseTest.TestDeserializer();
        String propertyName = deserializer.getPropertyName();
        assertEquals("_typePropertyName", propertyName);
    }

    @Test
    public void testGetTypeIdResolver() {
        TypeDeserializerBase deserializer = new TypeDeserializerBaseTest.TestDeserializer();
        TypeIdResolver idResolver = deserializer.getTypeIdResolver();
        assertNotNull(idResolver);
    }

    @Test
    public void testGetDefaultImpl() {
        TypeDeserializerBase deserializer = new TypeDeserializerBaseTest.TestDeserializer();
        Class<?> defaultImpl = deserializer.getDefaultImpl();
        assertEquals(Object.class, defaultImpl);
    }

    @Test
    public void testBaseType() {
        TypeDeserializerBase deserializer = new TypeDeserializerBaseTest.TestDeserializer();
        JavaType baseType = deserializer.baseType();
        assertNotNull(baseType);
    }

    @Test
    public void testToString() {
        TypeDeserializerBase deserializer = new TypeDeserializerBaseTest.TestDeserializer();
        String description = deserializer.toString();
        assertNotNull(description);
    }
    
    private static class TestDeserializer extends TypeDeserializerBase {

        public TestDeserializer() {
            super(null, null, "_typePropertyName", false, null);
        }

        @Override
        public TypeDeserializer forProperty(BeanProperty prop) {
            return null;
        }

        @Override
        public JsonTypeInfo.As getTypeInclusion() {
            return null;
        }
    }
}