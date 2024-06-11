package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class AsWrapperTypeDeserializerTest {

    @Test
    public void testAsWrapperTypeDeserializer_constructor() {
        JavaType bt = new JavaType();
        TypeIdResolver idRes = new TypeIdResolver();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = Object.class;

        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(bt, idRes, typePropertyName, typeIdVisible, defaultImpl);

        assertEquals(bt, deserializer.getBaseType());
        assertEquals(idRes, deserializer.getTypeIdResolver());
        assertEquals(typePropertyName, deserializer.getTypePropertyName());
        assertTrue(deserializer.isTypeIdVisible());
        assertEquals(defaultImpl, deserializer.getDefaultImpl());
    }

    @Test
    public void testAsWrapperTypeDeserializer_forProperty() {
        JavaType bt = new JavaType();
        TypeIdResolver idRes = new TypeIdResolver();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = Object.class;
        BeanProperty property = new BeanProperty();

        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(bt, idRes, typePropertyName, typeIdVisible, defaultImpl);
        TypeDeserializer result = deserializer.forProperty(property);

        assertTrue(result instanceof AsWrapperTypeDeserializer);
        assertSame(property, ((AsWrapperTypeDeserializer) result).getProperty());
    }

    @Test
    public void testAsWrapperTypeDeserializer_getTypeInclusion() {
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer();

        assertEquals(As.WRAPPER_OBJECT, deserializer.getTypeInclusion());
    }

    @Test
    public void testAsWrapperTypeDeserializer_deserializeTypedFromObject() {
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();

        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer();
        Object result = deserializer.deserializeTypedFromObject(jp, ctxt);

        assertNotNull(result);
    }

    @Test
    public void testAsWrapperTypeDeserializer_deserializeTypedFromArray() {
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();

        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer();
        Object result = deserializer.deserializeTypedFromArray(jp, ctxt);

        assertNotNull(result);
    }

    @Test
    public void testAsWrapperTypeDeserializer_deserializeTypedFromScalar() {
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();

        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer();
        Object result = deserializer.deserializeTypedFromScalar(jp, ctxt);

        assertNotNull(result);
    }

    @Test
    public void testAsWrapperTypeDeserializer_deserializeTypedFromAny() {
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();

        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer();
        Object result = deserializer.deserializeTypedFromAny(jp, ctxt);

        assertNotNull(result);
    }

    @Test
    public void testAsWrapperTypeDeserializer_deserialize() {
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();

        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer();

        try {
            deserializer.deserialize(p, ctxt);
            fail("Expected an IOException to be thrown");
        } catch (IOException e) {
            assertEquals("Invalid argument(s) passed to deserialize(); expected: JsonParser, DeserializationContext", e.getMessage());
        }
    }
}