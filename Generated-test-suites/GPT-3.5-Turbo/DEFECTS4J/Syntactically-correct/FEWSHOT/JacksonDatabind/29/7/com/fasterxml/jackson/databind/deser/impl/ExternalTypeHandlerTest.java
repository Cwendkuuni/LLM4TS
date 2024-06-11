package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import java.io.IOException;
import java.util.*;

public class ExternalTypeHandlerTest {

    @Test
    public void testStart() {
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(null, null, null, null);
        ExternalTypeHandler result = externalTypeHandler.start();
        assertNotNull(result);
    }

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(null, null, null, null);
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        String propName = null;
        Object bean = null;
        boolean result = externalTypeHandler.handleTypePropertyValue(jp, ctxt, propName, bean);
        assertFalse(result);
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(null, null, null, null);
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        String propName = null;
        Object bean = null;
        boolean result = externalTypeHandler.handlePropertyValue(jp, ctxt, propName, bean);
        assertFalse(result);
    }

    @Test
    public void testComplete() throws IOException {
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(null, null, null, null);
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object bean = null;
        Object result = externalTypeHandler.complete(p, ctxt, bean);
        assertNull(result);
    }

    @Test
    public void testComplete2() throws IOException {
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(null, null, null, null);
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        PropertyValueBuffer buffer = null;
        PropertyBasedCreator creator = null;
        Object result = externalTypeHandler.complete(jp, ctxt, buffer, creator);
        assertNull(result);
    }
    
    @Test
    public void testDeserialize() throws IOException {
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(null, null, null, null);
        JsonParser p = null;
        DeserializationContext ctxt = null;
        int index = 0;
        String typeId = null;
        Object result = externalTypeHandler._deserialize(p, ctxt, index, typeId);
        assertNull(result);
    }
    
    @Test
    public void testDeserializeAndSet() throws IOException {
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(null, null, null, null);
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object bean = null;
        int index = 0;
        String typeId = null;
        externalTypeHandler._deserializeAndSet(p, ctxt, bean, index, typeId);
    }
    
    @Test
    public void testBuilderAddExternal() {
        ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
        SettableBeanProperty property = null;
        TypeDeserializer typeDeser = null;
        builder.addExternal(property, typeDeser);
    }
    
    @Test
    public void testBuilderBuild() {
        ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
        ExternalTypeHandler result = builder.build();
        assertNotNull(result);
    }
    
}