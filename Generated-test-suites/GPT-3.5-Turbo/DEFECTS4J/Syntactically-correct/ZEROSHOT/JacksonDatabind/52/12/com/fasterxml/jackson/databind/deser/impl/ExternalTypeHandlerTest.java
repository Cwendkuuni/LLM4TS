package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class ExternalTypeHandlerTest {

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        JsonParser p = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(null, null, null, null);
        String propName = "prop";
        Object bean = new Object();

        boolean result = externalTypeHandler.handleTypePropertyValue(p, ctxt, propName, bean);

        assertEquals(false, result);
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        JsonParser p = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(null, null, null, null);
        String propName = "prop";
        Object bean = new Object();

        boolean result = externalTypeHandler.handlePropertyValue(p, ctxt, propName, bean);

        assertEquals(false, result);
    }

    @Test
    public void testComplete() throws IOException {
        JsonParser p = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(null, null, null, null);

        Object result = externalTypeHandler.complete(p, ctxt, new Object());

        assertEquals(null, result);
    }

    @Test
    public void testCompleteWithBuffer() throws IOException {
        JsonParser p = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(null, null, null, null);
        PropertyValueBuffer buffer = new PropertyValueBuffer(null, null);
        PropertyBasedCreator creator = Mockito.mock(PropertyBasedCreator.class);

        Object result = externalTypeHandler.complete(p, ctxt, buffer, creator);

        assertEquals(null, result);
    }

    @Test
    public void testBuilder() {
        ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
        SettableBeanProperty property = Mockito.mock(SettableBeanProperty.class);
        TypeDeserializer typeDeser = Mockito.mock(TypeDeserializer.class);

        builder.addExternal(property, typeDeser);

        ExternalTypeHandler handler = builder.build();

        assertEquals(ExternalTypeHandler.class, handler.getClass());
    }
}