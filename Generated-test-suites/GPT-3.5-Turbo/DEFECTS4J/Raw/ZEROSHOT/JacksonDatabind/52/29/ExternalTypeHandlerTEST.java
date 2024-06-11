package com.fasterxml.jackson.databind.deser.impl;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class ExternalTypeHandlerTest {

    private ExternalTypeHandler externalTypeHandler;

    @Mock
    private TokenBuffer tokenBuffer;

    @Mock
    private JsonProperty prop;

    @Mock
    private SettableBeanProperty settableBeanProperty;

    @Mock
    private TypeDeserializer typeDeserializer;

    @Before
    public void setup() {
        ExternalTypeHandler.ExtTypedProperty[] properties = new ExternalTypeHandler.ExtTypedProperty[] {};
        HashMap<String, Integer> nameToPropertyIndex = new HashMap<String, Integer>();
        String[] typeIds = new String[] {};
        TokenBuffer[] tokens = new TokenBuffer[] {};

        externalTypeHandler = new ExternalTypeHandler(properties, nameToPropertyIndex, typeIds, tokens);
    }

    @Test
    public void testStart() {
        ExternalTypeHandler result = externalTypeHandler.start();
        assertNotNull(result);
    }

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        String propName = "test";
        Object bean = new Object();

        boolean result = externalTypeHandler.handleTypePropertyValue(p, ctxt, propName, bean);
        assertFalse(result);
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        String propName = "test";
        Object bean = new Object();

        boolean result = externalTypeHandler.handlePropertyValue(p, ctxt, propName, bean);
        assertFalse(result);
    }

    @Test
    public void testComplete() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object bean = new Object();

        Object result = externalTypeHandler.complete(p, ctxt, bean);
        assertNotNull(result);
    }

    @Test
    public void testCompleteWithBuffer() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        PropertyValueBuffer buffer = mock(PropertyValueBuffer.class);
        PropertyBasedCreator creator = mock(PropertyBasedCreator.class);

        Object result = externalTypeHandler.complete(p, ctxt, buffer, creator);
        assertNotNull(result);
    }

    @Test
    public void testDeserialize() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        int index = 0;
        String typeId = "test";

        Object result = externalTypeHandler._deserialize(p, ctxt, index, typeId);
        assertNotNull(result);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object bean = new Object();
        int index = 0;
        String typeId = "test";

        externalTypeHandler._deserializeAndSet(p, ctxt, bean, index, typeId);
    }

    @Test
    public void testBuilderAddExternal() {
        // TODO: implement this test
    }

    @Test
    public void testBuilderBuild() {
        // TODO: implement this test
    }
}