
package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TypeDeserializerBaseTest {

    TypeDeserializerBase typeDeserializer;

    @Before
    public void setUp() {
        typeDeserializer = new TypeDeserializerBase(null, null, null, false, null) {
            private static final long serialVersionUID = 1L;

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
        String baseTypeName = typeDeserializer.baseTypeName();
        assertNotNull(baseTypeName);
        assertEquals("java.lang.Object", baseTypeName);
    }

    @Test
    public void testGetPropertyName() {
        String propertyName = typeDeserializer.getPropertyName();
        assertNotNull(propertyName);
        assertEquals("", propertyName);
    }

    @Test
    public void testGetTypeIdResolver() {
        TypeIdResolver resolver = typeDeserializer.getTypeIdResolver();
        assertNull(resolver);
    }

    @Test
    public void testGetDefaultImpl() {
        Class<?> defaultImpl = typeDeserializer.getDefaultImpl();
        assertNull(defaultImpl);
    }

    @Test
    public void testToString() {
        String expected = "[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase; base-type:null; id-resolver: null]";
        assertEquals(expected, typeDeserializer.toString());
    }
}

