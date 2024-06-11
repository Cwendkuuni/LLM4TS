
package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Test;
import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {

    @Test
    public void testBaseTypeName() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerStub();
        String baseTypeName = typeDeserializerBase.baseTypeName();
        assertEquals("com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerStub", baseTypeName);
    }

    @Test
    public void testGetPropertyName() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerStub();
        String propertyName = typeDeserializerBase.getPropertyName();
        assertEquals("_typePropertyName", propertyName);
    }

    private static class TypeDeserializerStub extends TypeDeserializerBase {

        public TypeDeserializerStub() {
            super(null, null, "_typePropertyName", true, null);
        }

        @Override
        public JsonTypeInfo.As getTypeInclusion() {
            return null;
        }

        @Override
        public TypeDeserializer forProperty(BeanProperty prop) {
            return null;
        }
    }
}
