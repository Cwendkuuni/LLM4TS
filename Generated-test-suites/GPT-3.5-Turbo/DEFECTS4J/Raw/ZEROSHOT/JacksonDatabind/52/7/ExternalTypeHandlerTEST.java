package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Test;
import static org.mockito.Mockito.*;

public class ExternalTypeHandlerTest {

    @Test
    public void testStart() {
        ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
        ExternalTypeHandler result = handler.start();
    }

    @Test
    public void testHandleTypePropertyValue() throws Exception {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object bean = new Object();

        ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
        boolean result = handler.handleTypePropertyValue(p, ctxt, "propName", bean);

        verify(p, times(1)).getText();
    }

    @Test
    public void testHandlePropertyValue() throws Exception {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object bean = new Object();

        ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
        boolean result = handler.handlePropertyValue(p, ctxt, "propName", bean);

        verify(p, times(1)).getText();
    }

    @Test
    public void testComplete() throws Exception {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object bean = new Object();

        ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
        Object result = handler.complete(p, ctxt, bean);
    }

    @Test
    public void testCompleteWithBuffer() throws Exception {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        PropertyValueBuffer buffer = mock(PropertyValueBuffer.class);
        PropertyBasedCreator creator = mock(PropertyBasedCreator.class);

        ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
        Object result = handler.complete(p, ctxt, buffer, creator);
    }

    @Test
    public void testDeserialize() throws Exception {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
        Object result = handler._deserialize(p, ctxt, 0, "typeId");
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object bean = new Object();

        ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
        handler._deserializeAndSet(p, ctxt, bean, 0, "typeId");
    }

    @Test
    public void testBuilderAddExternal() {
        SettableBeanProperty property = mock(SettableBeanProperty.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);

        ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
        builder.addExternal(property, typeDeser);
    }
}