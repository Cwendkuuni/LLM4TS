package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.DeserializationFeature.*;
import com.fasterxml.jackson.databind.JsonTypeInfo;

public class TypeDeserializerBaseTest {
    
    private TypeDeserializerBase typeDeserializerBase;
    
    @Before
    public void setup() {
        typeDeserializerBase = new TypeDeserializerBase() {
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
        assertEquals("com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase", typeDeserializerBase.baseTypeName());
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
        String expected = "[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase; base-type:null; id-resolver: null]";
        assertEquals(expected, typeDeserializerBase.toString());
    }
    
    @Test
    public void testDeserializeWithNativeTypeId() throws IOException {
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        assertNull(typeDeserializerBase._deserializeWithNativeTypeId(jp, ctxt));
    }
    
    @Test
    public void testHandleUnknownTypeId() throws IOException {
        DeserializationContext ctxt = null;
        String typeId = null;
        TypeIdResolver idResolver = null;
        JavaType baseType = null;
        assertNull(typeDeserializerBase._handleUnknownTypeId(ctxt, typeId, idResolver, baseType));
    }
}