package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;

public class TypeDeserializerBaseTest {
    private TypeDeserializerBase typeDeserializerBase;
    
    @Before
    public void setUp() {
        JavaType baseType = null;
        TypeIdResolver idRes = null;
        String typePropertyName = null;
        boolean typeIdVisible = false;
        Class<?> defaultImpl = null;
        typeDeserializerBase = new TypeDeserializerBase(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return JsonTypeInfo.As.EXTERNAL_PROPERTY;
            }
        };
    }

    @Test
    public void testBaseTypeName() {
        String actual = typeDeserializerBase.baseTypeName();
        assertNull(actual);
    }

    @Test
    public void testGetPropertyName() {
        String actual = typeDeserializerBase.getPropertyName();
        assertNull(actual);
    }

    @Test
    public void testGetTypeIdResolver() {
        TypeIdResolver actual = typeDeserializerBase.getTypeIdResolver();
        assertNull(actual);
    }

    @Test
    public void testGetDefaultImpl() {
        Class<?> actual = typeDeserializerBase.getDefaultImpl();
        assertNull(actual);
    }

    @Test
    public void testToString() {
        String actual = typeDeserializerBase.toString();
        String expected = "[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase; base-type:null; id-resolver: null]";
        assertEquals(expected, actual);
    }
}