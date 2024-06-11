package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

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
        assertEquals("java.util.Map", typeDeserializerBase.baseTypeName());
    }

    @Test
    public void testGetPropertyName() {
        assertNull(typeDeserializerBase.getPropertyName());
    }

    @Test
    public void testGetTypeIdResolver() {
        assertNull(typeDeserializerBase.getTypeIdResolver());
    }

    @Test
    public void testGetDefaultImpl() {
        assertNull(typeDeserializerBase.getDefaultImpl());
    }

    @Test
    public void testToString() {
        assertEquals("[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase; base-type:null; id-resolver: null]", typeDeserializerBase.toString());
    }
}