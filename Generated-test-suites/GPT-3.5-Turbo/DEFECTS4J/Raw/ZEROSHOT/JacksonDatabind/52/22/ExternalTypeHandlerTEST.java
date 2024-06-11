package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExternalTypeHandlerTest {

    private ExternalTypeHandler externalTypeHandler;

    @Before
    public void setUp() {
        ExternalTypeHandler.ExtTypedProperty[] properties = new ExternalTypeHandler.ExtTypedProperty[1];
        ExternalTypeHandler.ExtTypedProperty prop = new ExternalTypeHandler.ExtTypedProperty(null, null);
        properties[0] = prop;

        java.util.HashMap<String, Integer> nameToPropertyIndex = new java.util.HashMap<String, Integer>();
        nameToPropertyIndex.put("testProp", 0);

        String[] typeIds = new String[1];
        typeIds[0] = "testTypeId";

        com.fasterxml.jackson.databind.util.TokenBuffer[] tokens = new com.fasterxml.jackson.databind.util.TokenBuffer[1];
        tokens[0] = new com.fasterxml.jackson.databind.util.TokenBuffer(null, null);

        externalTypeHandler = new ExternalTypeHandler(properties, nameToPropertyIndex, typeIds, tokens);
    }

    @Test
    public void testHandleTypePropertyValue() throws java.io.IOException {
        com.fasterxml.jackson.core.JsonParser p = null;
        com.fasterxml.jackson.databind.DeserializationContext ctxt = null;
        String propName = "";
        Object bean = null;
        boolean result = externalTypeHandler.handleTypePropertyValue(p, ctxt, propName, bean);
        assertTrue(result);
    }

    @Test
    public void testHandlePropertyValue() throws java.io.IOException {
        com.fasterxml.jackson.core.JsonParser p = null;
        com.fasterxml.jackson.databind.DeserializationContext ctxt = null;
        String propName = "";
        Object bean = null;
        boolean result = externalTypeHandler.handlePropertyValue(p, ctxt, propName, bean);
        assertTrue(result);
    }

    @Test
    public void testComplete() throws java.io.IOException {
        com.fasterxml.jackson.core.JsonParser p = null;
        com.fasterxml.jackson.databind.DeserializationContext ctxt = null;
        Object bean = null;
        Object result = externalTypeHandler.complete(p, ctxt, bean);
        assertNotNull(result);
    }

    @Test
    public void testCompleteWithBuffer() throws java.io.IOException {
        com.fasterxml.jackson.core.JsonParser p = null;
        com.fasterxml.jackson.databind.DeserializationContext ctxt = null;
        com.fasterxml.jackson.databind.deser.impl.PropertyValueBuffer buffer = null;
        com.fasterxml.jackson.databind.deser.impl.PropertyBasedCreator creator = null;
        Object result = externalTypeHandler.complete(p, ctxt, buffer, creator);
        assertNotNull(result);
    }
}