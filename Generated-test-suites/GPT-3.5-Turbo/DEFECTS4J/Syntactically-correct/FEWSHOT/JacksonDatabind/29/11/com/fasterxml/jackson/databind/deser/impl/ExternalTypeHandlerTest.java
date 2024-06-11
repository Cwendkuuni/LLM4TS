package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonParser;
import com.fasterxml.jackson.databind.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.util.TokenBuffer;


public class ExternalTypeHandlerTest {
    
    @Test
    public void testHandleTypePropertyValue() throws IOException {
        ExternalTypeHandler extTypeHandler = new ExternalTypeHandler(null, null, null, null);
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        String propName = "";
        Object bean = null;
        boolean result = extTypeHandler.handleTypePropertyValue(jp, ctxt, propName, bean);
        assertFalse(result);
        
        propName = "type";
        result = extTypeHandler.handleTypePropertyValue(jp, ctxt, propName, bean);
        assertFalse(result);
    }
    
    @Test
    public void testHandlePropertyValue() throws IOException {
        ExternalTypeHandler extTypeHandler = new ExternalTypeHandler(null, null, null, null);
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        String propName = "";
        Object bean = null;
        boolean result = extTypeHandler.handlePropertyValue(jp, ctxt, propName, bean);
        assertFalse(result);
        
        propName = "type";
        result = extTypeHandler.handlePropertyValue(jp, ctxt, propName, bean);
        assertFalse(result);
    }
    
    @Test
    public void testComplete() throws IOException {
        ExternalTypeHandler extTypeHandler = new ExternalTypeHandler(null, null, null, null);
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object bean = null;
        Object result = extTypeHandler.complete(p, ctxt, bean);
        assertNull(result);
    }
    
    @Test
    public void testCompleteWithBuffer() throws IOException {
        ExternalTypeHandler extTypeHandler = new ExternalTypeHandler(null, null, null, null);
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        PropertyValueBuffer buffer = null;
        PropertyBasedCreator creator = null;
        Object result = extTypeHandler.complete(jp, ctxt, buffer, creator);
        assertNull(result);
    }
    
    @Test
    public void testDeserialize() throws IOException {
        ExternalTypeHandler extTypeHandler = new ExternalTypeHandler(null, null, null, null);
        JsonParser p = null;
        DeserializationContext ctxt = null;
        int index = 0;
        String typeId = "";
        Object result = extTypeHandler._deserialize(p, ctxt, index, typeId);
        assertNull(result);
    }
    
    @Test
    public void testDeserializeAndSet() throws IOException {
        ExternalTypeHandler extTypeHandler = new ExternalTypeHandler(null, null, null, null);
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object bean = null;
        int index = 0;
        String typeId = "";
        extTypeHandler._deserializeAndSet(p, ctxt, bean, index, typeId);
    }
    
    @Test
    public void testBuilder() {
        ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
        SettableBeanProperty property = null;
        TypeDeserializer typeDeser = null;
        builder.addExternal(property, typeDeser);
        ExternalTypeHandler extTypeHandler = builder.build();
        assertNotNull(extTypeHandler);
    }

}