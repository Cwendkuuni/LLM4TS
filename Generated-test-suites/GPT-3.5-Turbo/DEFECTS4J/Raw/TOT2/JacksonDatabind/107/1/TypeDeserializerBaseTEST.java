package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;
import org.junit.Test;

public class TypeDeserializerBaseTest {

    @Test
    public void testBaseTypeName() {
        TypeDeserializerBase base = new TypeDeserializerBaseImpl();
        assertEquals("com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase", base.baseTypeName());
    }

    @Test
    public void testGetPropertyName() {
        TypeDeserializerBase base = new TypeDeserializerBaseImpl();
        assertEquals("_typePropertyName", base.getPropertyName());
    }

    @Test
    public void testGetTypeIdResolver() {
        TypeDeserializerBase base = new TypeDeserializerBaseImpl();
        assertNotNull(base.getTypeIdResolver());
    }

    @Test
    public void testGetDefaultImpl() {
        TypeDeserializerBase base = new TypeDeserializerBaseImpl();
        assertNull(base.getDefaultImpl());
    }

    @Test
    public void testBaseType() {
        TypeDeserializerBase base = new TypeDeserializerBaseImpl();
        assertNotNull(base.baseType());
    }

    @Test
    public void testToString() {
        TypeDeserializerBase base = new TypeDeserializerBaseImpl();
        assertNotNull(base.toString());
    }

    private static class TypeDeserializerBaseImpl extends TypeDeserializerBase {
        public TypeDeserializerBaseImpl() {
            super(null, null, "_typePropertyName", true, null);
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