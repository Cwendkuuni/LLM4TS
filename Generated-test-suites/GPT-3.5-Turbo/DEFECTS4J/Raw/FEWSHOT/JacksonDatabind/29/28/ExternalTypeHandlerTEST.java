package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import java.io.IOException;
import java.util.HashMap;
import java.util.ArrayList;

public class ExternalTypeHandlerTest {
    @Test
    public void testHandleTypePropertyValue() throws IOException {
        ExtTypedProperty[] properties = new ExtTypedProperty[2];
        HashMap<String, Integer> nameToPropertyIndex = new HashMap<String, Integer>();
        String[] typeIds = new String[2];
        TokenBuffer[] tokens = new TokenBuffer[2];
        ExternalTypeHandler handler = new ExternalTypeHandler(properties, nameToPropertyIndex, typeIds, tokens);
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        String propName = "";
        Object bean = null;
        boolean result = handler.handleTypePropertyValue(jp, ctxt, propName, bean);
        
        assertFalse(result);
    }
    
    @Test
    public void testHandlePropertyValue() throws IOException {
        ExtTypedProperty[] properties = new ExtTypedProperty[2];
        HashMap<String, Integer> nameToPropertyIndex = new HashMap<String, Integer>();
        String[] typeIds = new String[2];
        TokenBuffer[] tokens = new TokenBuffer[2];
        ExternalTypeHandler handler = new ExternalTypeHandler(properties, nameToPropertyIndex, typeIds, tokens);
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        String propName = "";
        Object bean = null;
        boolean result = handler.handlePropertyValue(jp, ctxt, propName, bean);
        
        assertFalse(result);
    }
    
    @Test
    public void testComplete() throws IOException {
        ExtTypedProperty[] properties = new ExtTypedProperty[2];
        HashMap<String, Integer> nameToPropertyIndex = new HashMap<String, Integer>();
        String[] typeIds = new String[2];
        TokenBuffer[] tokens = new TokenBuffer[2];
        ExternalTypeHandler handler = new ExternalTypeHandler(properties, nameToPropertyIndex, typeIds, tokens);
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object bean = null;
        Object result = handler.complete(p, ctxt, bean);
        
        assertNotNull(result);
    }
    
    @Test
    public void testCompleteWithBuffer() throws IOException {
        ExtTypedProperty[] properties = new ExtTypedProperty[2];
        HashMap<String, Integer> nameToPropertyIndex = new HashMap<String, Integer>();
        String[] typeIds = new String[2];
        TokenBuffer[] tokens = new TokenBuffer[2];
        ExternalTypeHandler handler = new ExternalTypeHandler(properties, nameToPropertyIndex, typeIds, tokens);
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        PropertyValueBuffer buffer = null;
        PropertyBasedCreator creator = null;
        Object result = handler.complete(jp, ctxt, buffer, creator);
        
        assertNotNull(result);
    }
    
    @Test
    public void test_deserialize() throws IOException {
        ExtTypedProperty[] properties = new ExtTypedProperty[2];
        HashMap<String, Integer> nameToPropertyIndex = new HashMap<String, Integer>();
        String[] typeIds = new String[2];
        TokenBuffer[] tokens = new TokenBuffer[2];
        ExternalTypeHandler handler = new ExternalTypeHandler(properties, nameToPropertyIndex, typeIds, tokens);
        JsonParser p = null;
        DeserializationContext ctxt = null;
        int index = 0;
        String typeId = "";
        Object result = handler._deserialize(p, ctxt, index, typeId);
        
        assertNotNull(result);
    }
    
    @Test
    public void test_deserializeAndSet() throws IOException {
        ExtTypedProperty[] properties = new ExtTypedProperty[2];
        HashMap<String, Integer> nameToPropertyIndex = new HashMap<String, Integer>();
        String[] typeIds = new String[2];
        TokenBuffer[] tokens = new TokenBuffer[2];
        ExternalTypeHandler handler = new ExternalTypeHandler(properties, nameToPropertyIndex, typeIds, tokens);
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object bean = null;
        int index = 0;
        String typeId = "";
        handler._deserializeAndSet(p, ctxt, bean, index, typeId);
    }
    
    @Test
    public void testBuilder() {
        ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
        SettableBeanProperty property = null;
        TypeDeserializer typeDeser = null;
        builder.addExternal(property, typeDeser);
        ExternalTypeHandler handler = builder.build();
        
        assertNotNull(handler);
    }
}