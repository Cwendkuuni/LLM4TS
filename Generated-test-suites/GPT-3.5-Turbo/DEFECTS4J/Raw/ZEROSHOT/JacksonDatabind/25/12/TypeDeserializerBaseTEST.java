package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {

    private TypeDeserializerBase typeDeserializer;

    @Before
    public void setup() {
        typeDeserializer = new TypeDeserializerBase(null, null, null, false, null) {
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
        String expected = "java.lang.Object";
        String actual = typeDeserializer.baseTypeName();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetPropertyName() {
        String expected = null;
        String actual = typeDeserializer.getPropertyName();
        assertEquals(expected, actual);
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
        String expected = "[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase; base-type:null; id-resolver: null]";
        String actual = typeDeserializer.toString();
        assertEquals(expected, actual);
    }
}