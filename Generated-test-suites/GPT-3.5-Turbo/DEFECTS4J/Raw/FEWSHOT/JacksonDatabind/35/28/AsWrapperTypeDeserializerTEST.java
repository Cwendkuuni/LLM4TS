package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.node.BooleanNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class AsWrapperTypeDeserializerTest {
    private AsWrapperTypeDeserializer deserializer;

    @Before
    public void setUp() {
        JavaType bt = null;
        TypeIdResolver idRes = null;
        String typePropertyName = null;
        boolean typeIdVisible = false;
        Class<?> defaultImpl = null;
        deserializer = new AsWrapperTypeDeserializer(bt, idRes, typePropertyName, typeIdVisible, defaultImpl);
    }

    @Test
    public void testDeserializeTypedFromObject() throws IOException {
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        
        try {
            deserializer.deserializeTypedFromObject(jp, ctxt);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            assertTrue(e instanceof JsonParseException);
            assertEquals("need JSON Object to contain As.WRAPPER_OBJECT type information for class null", e.getMessage());
        }
    }

    @Test
    public void testDeserializeTypedFromArray() throws IOException {
        JsonParser jp = null;
        DeserializationContext ctxt = null;

        try {
            deserializer.deserializeTypedFromArray(jp, ctxt);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            assertTrue(e instanceof JsonParseException);
            assertEquals("need JSON Object to contain As.WRAPPER_OBJECT type information for class null", e.getMessage());
        }
    }

    @Test
    public void testDeserializeTypedFromScalar() throws IOException {
        JsonParser jp = null;
        DeserializationContext ctxt = null;

        try {
            deserializer.deserializeTypedFromScalar(jp, ctxt);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            assertTrue(e instanceof JsonParseException);
            assertEquals("need JSON Object to contain As.WRAPPER_OBJECT type information for class null", e.getMessage());
        }
    }

    @Test
    public void testDeserializeTypedFromAny() throws IOException {
        JsonParser jp = null;
        DeserializationContext ctxt = null;

        try {
            deserializer.deserializeTypedFromAny(jp, ctxt);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            assertTrue(e instanceof JsonParseException);
            assertEquals("need JSON Object to contain As.WRAPPER_OBJECT type information for class null", e.getMessage());
        }
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws IOException {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object typeId = null;

        try {
            deserializer.deserializeWithNativeTypeId(p, ctxt, typeId);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            assertTrue(e instanceof JsonParseException);
            assertEquals("need JSON Object to contain As.WRAPPER_OBJECT type information for class null", e.getMessage());
        }
    }

    @Test
    public void testFindDeserializer() {
        DeserializationContext ctxt = null;
        String typeId = null;

        try {
            deserializer._findDeserializer(ctxt, typeId);
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            assertEquals("No type id resolver configured for type identifier '" + typeId + "'", e.getMessage());
        }
    }
    
    @Test
    public void testBaseTypeName() {
        assertNull(deserializer.baseTypeName());
    }
}