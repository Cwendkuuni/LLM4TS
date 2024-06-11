package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

import static org.junit.Assert.*;
import org.junit.Test;

public class ExternalTypeHandlerTest {

    @Test
    public void testHandleTypePropertyValue() {
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler.Builder().build();
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        String propName = "";
        Object bean = null;
        boolean result = externalTypeHandler.handleTypePropertyValue(jp, ctxt, propName, bean);
        assertFalse(result);
    }

    @Test
    public void testHandlePropertyValue() {
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler.Builder().build();
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        String propName = "";
        Object bean = null;
        boolean result = externalTypeHandler.handlePropertyValue(jp, ctxt, propName, bean);
        assertFalse(result);
    }

    @Test
    public void testComplete() {
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler.Builder().build();
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object bean = null;
        Object result = externalTypeHandler.complete(p, ctxt, bean);
        assertNull(result);
    }

    @Test
    public void testComplete2() {
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler.Builder().build();
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        PropertyValueBuffer buffer = null;
        PropertyBasedCreator creator = null;
        Object result = externalTypeHandler.complete(jp, ctxt, buffer, creator);
        assertNull(result);
    }

    @Test
    public void testDeserialize() {
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler.Builder().build();
        JsonParser p = null;
        DeserializationContext ctxt = null;
        int index = 0;
        String typeId = "";
        try {
            Object result = externalTypeHandler._deserialize(p, ctxt, index, typeId);
            fail("Expected an IOException to be thrown");
        } catch (IOException e) {
            String expectedMessage = "Required field 'p.nextToken()' was not present";
            assertEquals(expectedMessage, e.getMessage());
        }
    }

    @Test
    public void testDeserializeAndSet() {
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler.Builder().build();
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object bean = null;
        int index = 0;
        String typeId = "";
        try {
            externalTypeHandler._deserializeAndSet(p, ctxt, bean, index, typeId);
            fail("Expected an IOException to be thrown");
        } catch (IOException e) {
            String expectedMessage = "Required field 'p.nextToken()' was not present";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}