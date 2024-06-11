package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Test;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class ExternalTypeHandlerTest {

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(null, null, null, null);
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        String propName = "";
        Object bean = new Object();
        boolean result = externalTypeHandler.handleTypePropertyValue(jp, ctxt, propName, bean);
        // Add assertions
    }
    
    @Test
    public void testHandlePropertyValue() throws IOException {
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(null, null, null, null);
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        String propName = "";
        Object bean = new Object();
        boolean result = externalTypeHandler.handlePropertyValue(jp, ctxt, propName, bean);
        // Add assertions
    }
    
    @Test
    public void testComplete() throws IOException {
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(null, null, null, null);
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object bean = new Object();
        Object result = externalTypeHandler.complete(p, ctxt, bean);
        // Add assertions
    }
    
    @Test
    public void testCompleteWithPropertyValueBuffer() throws IOException {
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(null, null, null, null);
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        PropertyValueBuffer buffer = null;
        PropertyBasedCreator creator = null;
        Object result = externalTypeHandler.complete(jp, ctxt, buffer, creator);
        // Add assertions
    }
    
    @Test
    public void testDeserialize() throws IOException {
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(null, null, null, null);
        JsonParser p = null;
        DeserializationContext ctxt = null;
        int index = 0;
        String typeId = "";
        Object result = externalTypeHandler._deserialize(p, ctxt, index, typeId);
        // Add assertions
    }
    
    @Test
    public void testDeserializeAndSet() throws IOException {
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(null, null, null, null);
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object bean = new Object();
        int index = 0;
        String typeId = "";
        externalTypeHandler._deserializeAndSet(p, ctxt, bean, index, typeId);
        // Add assertions
    }
    
    @Test
    public void testBuilder() {
        ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
        SettableBeanProperty property = null;
        TypeDeserializer typeDeser = null;
        builder.addExternal(property, typeDeser);
        ExternalTypeHandler externalTypeHandler = builder.build();
        // Add assertions
    }
}