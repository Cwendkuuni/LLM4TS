package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class AsPropertyTypeDeserializerTest {

    private AsPropertyTypeDeserializer deserializer;

    @Before
    public void setUp() {
        deserializer = new AsPropertyTypeDeserializer(null, null, null, false, null);
    }

    @Test
    public void testForProperty() {
        BeanProperty property = new BeanProperty();
        TypeDeserializer result = deserializer.forProperty(property);
        assertEquals(deserializer, result);
    }

    @Test
    public void testGetTypeInclusion() {
        As result = deserializer.getTypeInclusion();
        assertEquals(null, result);
    }

    @Test
    public void testDeserializeTypedFromObject() {
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object result = deserializer.deserializeTypedFromObject(p, ctxt);
        assertEquals(null, result);
    }

    @Test
    public void testDeserializeTypedForId() {
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        TokenBuffer tb = new TokenBuffer(p, ctxt);
        Object result = deserializer._deserializeTypedForId(p, ctxt, tb);
        assertEquals(null, result);
    }

    @Test
    public void testDeserializeTypedUsingDefaultImpl() {
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        TokenBuffer tb = new TokenBuffer(p, ctxt);
        Object result = deserializer._deserializeTypedUsingDefaultImpl(p, ctxt, tb);
        assertEquals(null, result);
    }

    @Test
    public void testDeserializeTypedFromAny() {
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object result = deserializer.deserializeTypedFromAny(p, ctxt);
        assertEquals(null, result);
    }
}