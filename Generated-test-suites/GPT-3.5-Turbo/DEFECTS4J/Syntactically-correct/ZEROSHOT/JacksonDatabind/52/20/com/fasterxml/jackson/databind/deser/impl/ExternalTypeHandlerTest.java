package com.fasterxml.jackson.databind.deser.impl;

import org.junit.*;
import static org.junit.Assert.*;
import java.io.IOException;
import java.util.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class ExternalTypeHandlerTest {

    @Test
    public void testStart() {
        ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
        ExternalTypeHandler newHandler = handler.start();
        assertNotNull(newHandler);
    }

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
        JsonParser p = new JsonFactory().createParser("null");
        DeserializationContext ctxt = null;
        String propName = "prop";
        Object bean = null;
        boolean result = handler.handleTypePropertyValue(p, ctxt, propName, bean);
        assertFalse(result);
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
        JsonParser p = new JsonFactory().createParser("null");
        DeserializationContext ctxt = null;
        String propName = "prop";
        Object bean = null;
        boolean result = handler.handlePropertyValue(p, ctxt, propName, bean);
        assertFalse(result);
    }

    @Test
    public void testComplete() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
        JsonParser p = new JsonFactory().createParser("null");
        DeserializationContext ctxt = null;
        Object bean = null;
        Object result = handler.complete(p, ctxt, bean);
        assertNull(result);
    }

    @Test
    public void testCompleteWithBuffer() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
        JsonParser p = new JsonFactory().createParser("null");
        DeserializationContext ctxt = null;
        PropertyValueBuffer buffer = null;
        PropertyBasedCreator creator = null;
        Object result = handler.complete(p, ctxt, buffer, creator);
        assertNull(result);
    }

    @Test
    public void testDeserialize() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
        JsonParser p = new JsonFactory().createParser("null");
        DeserializationContext ctxt = null;
        int index = 0;
        String typeId = "type";
        Object result = handler._deserialize(p, ctxt, index, typeId);
        assertNull(result);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
        JsonParser p = new JsonFactory().createParser("null");
        DeserializationContext ctxt = null;
        Object bean = null;
        int index = 1;
        String typeId = "type";
        handler._deserializeAndSet(p, ctxt, bean, index, typeId);
    }

    @Test
    public void testAddExternal() {
        ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
        SettableBeanProperty property = null;
        TypeDeserializer typeDeser = null;
        builder.addExternal(property, typeDeser);
    }
}