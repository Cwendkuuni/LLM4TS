package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class ExternalTypeHandlerTest {

    private ExternalTypeHandler externalTypeHandler;

    @Before
    public void setUp() {
        ExtTypedProperty[] properties = createProperties();
        HashMap<String, Integer> nameToPropertyIndex = createNameToPropertyIndex();
        String[] typeIds = createTypeIds();
        TokenBuffer[] tokens = createTokens();

        externalTypeHandler = new ExternalTypeHandler(properties, nameToPropertyIndex, typeIds, tokens);
    }

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        String propName = "";
        Object bean = null;

        boolean result = externalTypeHandler.handleTypePropertyValue(jp, ctxt, propName, bean);

        assertTrue(result);
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        String propName = "";
        Object bean = null;

        boolean result = externalTypeHandler.handlePropertyValue(jp, ctxt, propName, bean);

        assertTrue(result);
    }

    @Test
    public void testComplete() throws IOException {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object bean = null;

        Object result = externalTypeHandler.complete(p, ctxt, bean);

        assertNotNull(result);
    }

    @Test
    public void testCompleteWithBuffer() throws IOException {
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        PropertyValueBuffer buffer = null;
        PropertyBasedCreator creator = null;

        Object result = externalTypeHandler.complete(jp, ctxt, buffer, creator);

        assertNotNull(result);
    }

    private ExtTypedProperty[] createProperties() {
        return new ExtTypedProperty[2];
    }

    private HashMap<String, Integer> createNameToPropertyIndex() {
        return new HashMap<String, Integer>();
    }

    private String[] createTypeIds() {
        return new String[2];
    }

    private TokenBuffer[] createTokens() {
        return new TokenBuffer[2];
    }

}