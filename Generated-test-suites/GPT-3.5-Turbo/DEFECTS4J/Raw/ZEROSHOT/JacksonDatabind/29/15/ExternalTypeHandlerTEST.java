package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.util.TokenBuffer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ExternalTypeHandlerTest {

    private ExternalTypeHandler.ExtTypedProperty[] properties;
    private HashMap<String, Integer> nameToPropertyIndex;
    private String[] typeIds;
    private TokenBuffer[] tokens;

    @Before
    public void setup() {
        properties = new ExternalTypeHandler.ExtTypedProperty[]{
                new ExternalTypeHandler.ExtTypedProperty(null, null),
                new ExternalTypeHandler.ExtTypedProperty(null, null)
        };

        nameToPropertyIndex = new HashMap<>();
        nameToPropertyIndex.put("prop1", 0);
        nameToPropertyIndex.put("prop2", 1);

        typeIds = new String[]{"type1", "type2"};
        tokens = new TokenBuffer[]{new TokenBuffer(), new TokenBuffer()};
    }

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        String propName = "prop1";
        Object bean = null;

        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(properties, nameToPropertyIndex, typeIds, tokens);
        boolean result = externalTypeHandler.handleTypePropertyValue(jp, ctxt, propName, bean);

        assertTrue(result);
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        String propName = "prop1";
        Object bean = null;

        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(properties, nameToPropertyIndex, typeIds, tokens);
        boolean result = externalTypeHandler.handlePropertyValue(jp, ctxt, propName, bean);

        assertTrue(result);
    }

    @Test
    public void testComplete() throws IOException {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object bean = null;

        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(properties, nameToPropertyIndex, typeIds, tokens);
        Object result = externalTypeHandler.complete(p, ctxt, bean);

        assertNotNull(result);
    }

    @Test
    public void testCompleteWithPropertyValueBufferAndPropertyBasedCreator() throws IOException {
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        ExternalTypeHandler.PropertyValueBuffer buffer = null;
        ExternalTypeHandler.PropertyBasedCreator creator = null;

        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(properties, nameToPropertyIndex, typeIds, tokens);
        Object result = externalTypeHandler.complete(jp, ctxt, buffer, creator);

        assertNotNull(result);
    }

    @Test
    public void testDeserialize() throws IOException {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        int index = 0;
        String typeId = "type1";

        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(properties, nameToPropertyIndex, typeIds, tokens);
        Object result = externalTypeHandler.deserialize(p, ctxt, index, typeId);

        assertNotNull(result);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object bean = null;
        int index = 0;
        String typeId = "type1";

        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(properties, nameToPropertyIndex, typeIds, tokens);
        externalTypeHandler.deserializeAndSet(p, ctxt, bean, index, typeId);
    }
}