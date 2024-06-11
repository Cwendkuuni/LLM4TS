package com.fasterxml.jackson.databind.deser.impl;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExternalTypeHandlerTest {

    private ExternalTypeHandler externalTypeHandler;

    @Before
    public void setup() {
        ExternalTypeHandler.ExtTypedProperty property = new ExternalTypeHandler.ExtTypedProperty(mock(SettableBeanProperty.class), mock(TypeDeserializer.class));
        HashMap<String, Integer> nameToPropertyIndex = new HashMap<>();
        nameToPropertyIndex.put("testProperty", 0);
        String[] typeIds = new String[1];
        TokenBuffer[] tokens = new TokenBuffer[1];

        externalTypeHandler = new ExternalTypeHandler(new ExternalTypeHandler.ExtTypedProperty[]{property}, nameToPropertyIndex, typeIds, tokens);
    }

    @Test
    public void testStart() {
        ExternalTypeHandler handler = externalTypeHandler.start();
        assertNotNull(handler);
        assertNotSame(externalTypeHandler, handler);
    }

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        String propName = "testProperty";
        Object bean = new Object();

        boolean handled = externalTypeHandler.handleTypePropertyValue(p, ctxt, propName, bean);

        assertFalse(handled);
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        String propName = "testProperty";
        Object bean = new Object();

        boolean handled = externalTypeHandler.handlePropertyValue(p, ctxt, propName, bean);

        assertFalse(handled);
    }

    @Test
    public void testComplete() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object bean = new Object();

        Object result = externalTypeHandler.complete(p, ctxt, bean);

        assertSame(bean, result);
    }

    @Test
    public void testCompleteWithPropertyValueBuffer() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        PropertyValueBuffer buffer = mock(PropertyValueBuffer.class);
        PropertyBasedCreator creator = mock(PropertyBasedCreator.class);

        Object result = externalTypeHandler.complete(p, ctxt, buffer, creator);

        assertNotNull(result);
    }

    @Test
    public void testDeserialize() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        int index = 0;
        String typeId = "testType";

        Object result = externalTypeHandler._deserialize(p, ctxt, index, typeId);

        assertNull(result);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object bean = new Object();
        int index = 0;
        String typeId = "testType";

        externalTypeHandler._deserializeAndSet(p, ctxt, bean, index, typeId);
    }

    @Test
    public void testBuilderAddExternal() {
        SettableBeanProperty property = mock(SettableBeanProperty.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);

        ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
        builder.addExternal(property, typeDeser);
    }

    @Test
    public void testBuilderBuild() {
        ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
        ExternalTypeHandler handler = builder.build();

        assertNotNull(handler);
    }
}