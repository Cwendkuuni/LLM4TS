package com.fasterxml.jackson.databind.deser.impl;

import java.io.IOException;
import java.util.HashMap;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.util.TokenBuffer;

import org.junit.Test;

import static org.junit.Assert.*;

public class ExternalTypeHandlerTest {
    
    @Test
    public void testHandleTypePropertyValue() throws IOException {
        ExternalTypeHandler.ExtTypedProperty[] properties = new ExternalTypeHandler.ExtTypedProperty[1];
        HashMap<String, Integer> nameToPropertyIndex = new HashMap<String, Integer>();
        String[] typeIds = new String[1];
        TokenBuffer[] tokens = new TokenBuffer[1];
        
        ExternalTypeHandler handler = new ExternalTypeHandler(properties, nameToPropertyIndex, typeIds, tokens);
        
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        String propName = "testProp";
        Object bean = null;
        
        boolean result = handler.handleTypePropertyValue(jp, ctxt, propName, bean);
        
        assertFalse(result);
    }
    
    @Test
    public void testHandlePropertyValue() throws IOException {
        ExternalTypeHandler.ExtTypedProperty[] properties = new ExternalTypeHandler.ExtTypedProperty[1];
        HashMap<String, Integer> nameToPropertyIndex = new HashMap<String, Integer>();
        String[] typeIds = new String[1];
        TokenBuffer[] tokens = new TokenBuffer[1];
        
        ExternalTypeHandler handler = new ExternalTypeHandler(properties, nameToPropertyIndex, typeIds, tokens);
        
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        String propName = "testProp";
        Object bean = null;
        
        boolean result = handler.handlePropertyValue(jp, ctxt, propName, bean);
        
        assertFalse(result);
    }
    
    @Test
    public void testComplete() throws IOException {
        ExternalTypeHandler.ExtTypedProperty[] properties = new ExternalTypeHandler.ExtTypedProperty[1];
        HashMap<String, Integer> nameToPropertyIndex = new HashMap<String, Integer>();
        String[] typeIds = new String[1];
        TokenBuffer[] tokens = new TokenBuffer[1];
        
        ExternalTypeHandler handler = new ExternalTypeHandler(properties, nameToPropertyIndex, typeIds, tokens);
        
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object bean = null;
        
        Object result = handler.complete(p, ctxt, bean);
        
        assertNull(result);
    }
    
    @Test
    public void testCompleteWithBuffer() throws IOException {
        ExternalTypeHandler.ExtTypedProperty[] properties = new ExternalTypeHandler.ExtTypedProperty[1];
        HashMap<String, Integer> nameToPropertyIndex = new HashMap<String, Integer>();
        String[] typeIds = new String[1];
        TokenBuffer[] tokens = new TokenBuffer[1];
        
        ExternalTypeHandler handler = new ExternalTypeHandler(properties, nameToPropertyIndex, typeIds, tokens);
        
        JsonParser p = null;
        DeserializationContext ctxt = null;
        ExternalTypeHandler.PropertyValueBuffer buffer = null;
        ExternalTypeHandler.PropertyBasedCreator creator = null;
        
        Object result = handler.complete(p, ctxt, buffer, creator);
        
        assertNull(result);
    }
    
    @Test
    public void testDeserialize() throws IOException {
        ExternalTypeHandler.ExtTypedProperty[] properties = new ExternalTypeHandler.ExtTypedProperty[1];
        HashMap<String, Integer> nameToPropertyIndex = new HashMap<String, Integer>();
        String[] typeIds = new String[1];
        TokenBuffer[] tokens = new TokenBuffer[1];
        
        ExternalTypeHandler handler = new ExternalTypeHandler(properties, nameToPropertyIndex, typeIds, tokens);
        
        JsonParser p = null;
        DeserializationContext ctxt = null;
        int index = 0;
        String typeId = "testType";
        
        Object result = handler._deserialize(p, ctxt, index, typeId);
        
        assertNull(result);
    }
    
    @Test
    public void testDeserializeAndSet() throws IOException {
        ExternalTypeHandler.ExtTypedProperty[] properties = new ExternalTypeHandler.ExtTypedProperty[1];
        HashMap<String, Integer> nameToPropertyIndex = new HashMap<String, Integer>();
        String[] typeIds = new String[1];
        TokenBuffer[] tokens = new TokenBuffer[1];
        
        ExternalTypeHandler handler = new ExternalTypeHandler(properties, nameToPropertyIndex, typeIds, tokens);
        
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object bean = null;
        int index = 0;
        String typeId = "testType";
        
        handler._deserializeAndSet(p, ctxt, bean, index, typeId);
        
    }
    
}