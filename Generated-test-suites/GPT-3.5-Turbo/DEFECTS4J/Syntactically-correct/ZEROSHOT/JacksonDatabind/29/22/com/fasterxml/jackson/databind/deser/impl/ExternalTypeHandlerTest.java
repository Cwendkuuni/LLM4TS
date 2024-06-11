package com.fasterxml.jackson.databind.deser.impl;

import org.junit.*;
import static org.junit.Assert.*;
import org.mockito.Mockito;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import java.io.IOException;
import java.util.HashMap;

public class ExternalTypeHandlerTest {

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        ExternalTypeHandler.ExtTypedProperty prop = new ExternalTypeHandler.ExtTypedProperty(null, null);
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(null, null, null, null);
        JsonParser jp = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        String propName = "propName";
        Object bean = null;
        boolean result = externalTypeHandler.handleTypePropertyValue(jp, ctxt, propName, bean);
        assertFalse(result);
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        ExternalTypeHandler.ExtTypedProperty prop = new ExternalTypeHandler.ExtTypedProperty(null, null);
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(null, null, null, null);
        JsonParser jp = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        String propName = "propName";
        Object bean = null;
        boolean result = externalTypeHandler.handlePropertyValue(jp, ctxt, propName, bean);
        assertFalse(result);
    }

    @Test
    public void testComplete() throws IOException {
        ExternalTypeHandler.ExtTypedProperty prop = new ExternalTypeHandler.ExtTypedProperty(null, null);
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(null, null, null, null);
        JsonParser p = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        Object bean = null;
        Object result = externalTypeHandler.complete(p, ctxt, bean);
        assertNotNull(result);
    }

    @Test
    public void testComplete_PropertyValueBuffer() throws IOException {
        ExternalTypeHandler.ExtTypedProperty prop = new ExternalTypeHandler.ExtTypedProperty(null, null);
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(null, null, null, null);
        JsonParser jp = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        PropertyValueBuffer buffer = Mockito.mock(PropertyValueBuffer.class);
        PropertyBasedCreator creator = Mockito.mock(PropertyBasedCreator.class);
        Object result = externalTypeHandler.complete(jp, ctxt, buffer, creator);
        assertNotNull(result);
    }

    @Test
    public void test_deserialize() throws IOException {
        ExternalTypeHandler.ExtTypedProperty prop = new ExternalTypeHandler.ExtTypedProperty(null, null);
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(null, null, null, null);
        JsonParser p = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        int index = 0;
        String typeId = "typeId";
        Object result = externalTypeHandler._deserialize(p, ctxt, index, typeId);
        assertNotNull(result);
    }

    @Test
    public void test_deserializeAndSet() throws IOException {
        ExternalTypeHandler.ExtTypedProperty prop = new ExternalTypeHandler.ExtTypedProperty(null, null);
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(null, null, null, null);
        JsonParser p = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        Object bean = null;
        int index = 0;
        String typeId = "typeId";
        externalTypeHandler._deserializeAndSet(p, ctxt, bean, index, typeId);
    }

    @Test
    public void testBuilder() {
        ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
        SettableBeanProperty property = Mockito.mock(SettableBeanProperty.class);
        TypeDeserializer typeDeser = Mockito.mock(TypeDeserializer.class);
        builder.addExternal(property, typeDeser);
        ExternalTypeHandler externalTypeHandler = builder.build();
        assertNotNull(externalTypeHandler);
    }

    @Test
    public void testExtTypedProperty() {
        SettableBeanProperty property = Mockito.mock(SettableBeanProperty.class);
        TypeDeserializer typeDeser = Mockito.mock(TypeDeserializer.class);
        ExternalTypeHandler.ExtTypedProperty extTypedProperty = new ExternalTypeHandler.ExtTypedProperty(property, typeDeser);
        assertNotNull(extTypedProperty);
    }


}