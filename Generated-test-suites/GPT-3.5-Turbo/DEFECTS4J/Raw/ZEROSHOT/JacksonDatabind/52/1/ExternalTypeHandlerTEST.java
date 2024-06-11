package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.util.HashMap;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;
 
public class ExternalTypeHandlerTest {
 
    @Test
    public void testHandleTypePropertyValue() throws IOException {
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(null, new HashMap<String, Integer>(), null, null);
        JsonParser p = null;
        DeserializationContext ctxt = null;
        String propName = null;
        Object bean = null;
        assertFalse(externalTypeHandler.handleTypePropertyValue(p, ctxt, propName, bean));
    }
 
    @Test
    public void testHandlePropertyValue() throws IOException {
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(null, new HashMap<String, Integer>(), null, null);
        JsonParser p = null;
        DeserializationContext ctxt = null;
        String propName = null;
        Object bean = null;
        assertFalse(externalTypeHandler.handlePropertyValue(p, ctxt, propName, bean));
    }
 
    @Test
    public void testComplete() throws IOException {
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(null, new HashMap<String, Integer>(), null, null);
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object bean = null;
        assertNull(externalTypeHandler.complete(p, ctxt, bean));
    }
 
    @Test
    public void testCompleteWithBufferAndCreator() throws IOException {
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(null, new HashMap<String, Integer>(), null, null);
        JsonParser p = null;
        DeserializationContext ctxt = null;
        PropertyValueBuffer buffer = null;
        PropertyBasedCreator creator = null;
        assertNull(externalTypeHandler.complete(p, ctxt, buffer, creator));
    }
 
    @Test
    public void testDeserialize() throws IOException {
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(null, new HashMap<String, Integer>(), null, null);
        JsonParser p = null;
        DeserializationContext ctxt = null;
        int index = 0;
        String typeId = null;
        assertNull(externalTypeHandler._deserialize(p, ctxt, index, typeId));
    }
 
    @Test
    public void testDeserializeAndSet() throws IOException {
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(null, new HashMap<String, Integer>(), null, null);
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object bean = null;
        int index = 0;
        String typeId = null;
        externalTypeHandler._deserializeAndSet(p, ctxt, bean, index, typeId);
    }
 
    @Test
    public void testBuilderAddExternal() throws IOException {
        ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
        SettableBeanProperty property = null;
        TypeDeserializer typeDeser = null;
        builder.addExternal(property, typeDeser);
    }
}