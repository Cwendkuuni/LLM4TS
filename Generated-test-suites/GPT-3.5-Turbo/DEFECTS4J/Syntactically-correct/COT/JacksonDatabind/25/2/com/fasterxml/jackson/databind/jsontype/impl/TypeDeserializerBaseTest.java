package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Test;
import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {

    TypeDeserializerBase typeDeserializer = new TypeDeserializerBase() {
        @Override
        public TypeDeserializer forProperty(BeanProperty prop) {
            return null;
        }

        @Override
        public JsonTypeInfo.As getTypeInclusion() {
            return null;
        }
    };

    @Test
    public void testBaseTypeName() {
        assertEquals("java.JavaType", typeDeserializer.baseTypeName());
    }

    @Test
    public void testGetPropertyName() {
        assertNull(typeDeserializer.getPropertyName());
    }

    @Test
    public void testGetTypeIdResolver() {
        assertNull(typeDeserializer.getTypeIdResolver());
    }

    @Test
    public void testGetDefaultImpl() {
        assertNull(typeDeserializer.getDefaultImpl());
    }

    @Test
    public void testToString() {
        assertEquals("[org.junit.Test; base-type:null; id-resolver: null]", typeDeserializer.toString());
    }
}