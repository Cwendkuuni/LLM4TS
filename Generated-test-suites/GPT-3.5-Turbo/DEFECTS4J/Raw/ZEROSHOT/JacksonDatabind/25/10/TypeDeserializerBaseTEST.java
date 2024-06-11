package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {

    private TypeDeserializerBase typeDeserializerBase;

    @Before
    public void setUp() {
        typeDeserializerBase = new TypeDeserializerBase() {
            @Override
            public org.codehaus.jackson.annotate.JsonTypeInfo.As getTypeInclusion() {
                return null;
            }

            @Override
            public TypeDeserializer forProperty(org.codehaus.jackson.map.BeanProperty prop) {
                return null;
            }

            @Override
            protected org.codehaus.jackson.map.JsonDeserializer<Object> _findDeserializer(org.codehaus.jackson.map.DeserializationContext ctxt, String typeId) throws IOException {
                return null;
            }

            @Override
            protected org.codehaus.jackson.map.JsonDeserializer<Object> _findDefaultImplDeserializer(org.codehaus.jackson.map.DeserializationContext ctxt) throws IOException {
                return null;
            }

            @Override
            protected Object _deserializeWithNativeTypeId(org.codehaus.jackson.JsonParser jp, org.codehaus.jackson.map.DeserializationContext ctxt) throws IOException {
                return null;
            }

            @Override
            protected JsonDeserializer<Object> _handleUnknownTypeId(org.codehaus.jackson.map.DeserializationContext ctxt, String typeId, org.codehaus.jackson.map.jsontype.TypeIdResolver idResolver, org.codehaus.jackson.type.JavaType baseType) throws IOException {
                return null;
            }
        };
    }

    @Test
    public void testBaseTypeName() {
        assertEquals("java.lang.Object", typeDeserializerBase.baseTypeName());
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
        String expected = "[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBaseTest; base-type:null; id-resolver: null]";
        assertEquals(expected, typeDeserializerBase.toString());
    }
}