package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {

    private TypeDeserializerBase typeDeserializerBase;

    @Before
    public void setUp(){
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
    public void testBaseTypeName(){
        String expected = "java.util.Map";
        String actual = typeDeserializerBase.baseTypeName();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetPropertyName(){
        String expected = "";
        String actual = typeDeserializerBase.getPropertyName();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetTypeIdResolver(){
        assertNull(typeDeserializerBase.getTypeIdResolver());
    }

    @Test
    public void testGetDefaultImpl(){
        assertNull(typeDeserializerBase.getDefaultImpl());
    }

    @Test
    public void testToString(){
        String expected = "[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase; base-type:null; id-resolver: null]";
        String actual = typeDeserializerBase.toString();
        assertEquals(expected, actual);
    }
}