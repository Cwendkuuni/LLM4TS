package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class ExternalTypeHandlerTest {

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        ExternalTypeHandler.ExtTypedProperty extTypedProperty = new ExternalTypeHandler.ExtTypedProperty(
                new SettableBeanProperty(null, null, null, null),
                null);
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(new ExternalTypeHandler.ExtTypedProperty[] {extTypedProperty},
                new HashMap<String, Integer>(), new String[] {null}, new TokenBuffer[] {null});

        JsonParser parser = null;
        DeserializationContext ctxt = null;
        String propName = "";
        Object bean = null;

        boolean result = externalTypeHandler.handleTypePropertyValue(parser, ctxt, propName, bean);
        assertFalse(result);
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        ExternalTypeHandler.ExtTypedProperty extTypedProperty = new ExternalTypeHandler.ExtTypedProperty(
                new SettableBeanProperty(null, null, null, null),
                null);
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(new ExternalTypeHandler.ExtTypedProperty[] {extTypedProperty},
                new HashMap<String, Integer>(), new String[] {null}, new TokenBuffer[] {null});

        JsonParser parser = null;
        DeserializationContext ctxt = null;
        String propName = "";
        Object bean = null;

        boolean result = externalTypeHandler.handlePropertyValue(parser, ctxt, propName, bean);
        assertFalse(result);
    }

    @Test
    public void testComplete() throws IOException {
        ExternalTypeHandler.ExtTypedProperty extTypedProperty = new ExternalTypeHandler.ExtTypedProperty(
                new SettableBeanProperty(null, null, null, null),
                null);
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(new ExternalTypeHandler.ExtTypedProperty[] {extTypedProperty},
                new HashMap<String, Integer>(), new String[] {null}, new TokenBuffer[] {null});

        JsonParser parser = null;
        DeserializationContext ctxt = null;
        Object bean = null;

        Object result = externalTypeHandler.complete(parser, ctxt, bean);
        assertEquals(bean, result);
    }

    @Test
    public void testCompletePropertyBasedCreator() throws IOException {
        ExternalTypeHandler.ExtTypedProperty extTypedProperty = new ExternalTypeHandler.ExtTypedProperty(
                new SettableBeanProperty(null, null, null, null),
                null);
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(new ExternalTypeHandler.ExtTypedProperty[] {extTypedProperty},
                new HashMap<String, Integer>(), new String[] {null}, new TokenBuffer[] {null});

        JsonParser parser = null;
        DeserializationContext ctxt = null;
        PropertyValueBuffer buffer = null;
        PropertyBasedCreator creator = null;

        Object result = externalTypeHandler.complete(parser, ctxt, buffer, creator);
        assertNull(result);
    }

    @Test
    public void testBuilderAddExternal() {
        ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();

        SettableBeanProperty property = null;
        SettableBeanProperty.TypeDeserializer typeDeser = null;
        builder.addExternal(property, typeDeser);
    }
}