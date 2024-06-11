package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Test;
import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {

    @Test
    public void testBaseTypeName() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseSubclass();
        assertEquals("java.lang.Object", typeDeserializerBase.baseTypeName());
    }

    @Test
    public void testGetPropertyName() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseSubclass();
        assertEquals("type", typeDeserializerBase.getPropertyName());
    }

    @Test
    public void testGetTypeIdResolver() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseSubclass();
        assertNotNull(typeDeserializerBase.getTypeIdResolver());
    }
    
    @Test
    public void testGetDefaultImpl() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseSubclass();
        assertEquals(Void.class, typeDeserializerBase.getDefaultImpl());
    }
    
    @Test
    public void testBaseType() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseSubclass();
        assertNotNull(typeDeserializerBase.baseType());
    }
    
    @Test
    public void testToString() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseSubclass();
        assertNotNull(typeDeserializerBase.toString());
    }

    private class TypeDeserializerBaseSubclass extends TypeDeserializerBase {
        public TypeDeserializerBaseSubclass() {
            super(null, null, "type", true, null);
        }

        @Override
        public TypeDeserializer forProperty(BeanProperty prop) {
            return null;
        }

        @Override
        public JsonTypeInfo.As getTypeInclusion() {
            return JsonTypeInfo.As.EXTERNAL_PROPERTY;
        }
    }
}