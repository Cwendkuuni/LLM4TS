package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;
import org.junit.Test;

public class TypeDeserializerBaseTest {

    @Test
    public void testTypeDeserializerBaseConstructor() {
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBaseDummy();
        assertNotNull(typeDeserializer);
    }

    @Test
    public void testForProperty() {
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBaseDummy();
        assertSame(null, typeDeserializer.forProperty(null));
    }

    @Test
    public void testGetTypeInclusion() {
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBaseDummy();
        assertSame(JsonTypeInfo.As.PROPERTY, typeDeserializer.getTypeInclusion());
    }

    @Test
    public void testBaseTypeName() {
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBaseDummy();
        assertEquals("java.lang.Object", typeDeserializer.baseTypeName());
    }

    @Test
    public void testGetPropertyName() {
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBaseDummy();
        assertEquals("", typeDeserializer.getPropertyName());
    }

    @Test
    public void testGetTypeIdResolver() {
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBaseDummy();
        assertNotNull(typeDeserializer.getTypeIdResolver());
    }

    @Test
    public void testGetDefaultImpl() {
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBaseDummy();
        assertNull(typeDeserializer.getDefaultImpl());
    }

    @Test
    public void testToString() {
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBaseDummy();
        assertEquals("[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase; base-type:java.lang.Object; id-resolver: null]", typeDeserializer.toString());
    }

    @Test
    public void testHandleUnknownTypeId() {
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBaseDummy();
        try {
            typeDeserializer._handleUnknownTypeId(null, "unknown_id", null, null);
            fail("Exception should have been thrown");
        } catch (Exception e) {
            assertNotNull(e);
        }
    }

    // Dummy class for testing
    private static class TypeDeserializerBaseDummy extends TypeDeserializerBase {
        private static final long serialVersionUID = 1L;

        protected TypeDeserializerBaseDummy() {
            super(null, null, "", false, null);
        }

        @Override
        public TypeDeserializer forProperty(BeanProperty prop) {
            return null;
        }

        @Override
        public JsonTypeInfo.As getTypeInclusion() {
            return JsonTypeInfo.As.PROPERTY;
        }

        @Override
        protected JavaType _handleUnknownTypeId(DeserializationContext ctxt, String typeId, TypeIdResolver idResolver, JavaType baseType) {
            throw new RuntimeException("Unknown type id");
        }
    }
}