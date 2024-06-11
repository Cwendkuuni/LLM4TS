package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.deser.impl.ExternalTypeHandler.ExtTypedProperty;
import com.fasterxml.jackson.databind.deser.impl.ExternalTypeHandler.Builder;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;

public class ExternalTypeHandlerTest {

    private ExternalTypeHandler externalTypeHandler;
    private DeserializationContext deserializationContext;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        externalTypeHandler = new ExternalTypeHandler();
        objectMapper = new ObjectMapper();
        deserializationContext = new DeserializationContext(objectMapper, new JsonFactory());
    }

    @Test
    public void testStart() {
        ExternalTypeHandler newExternalTypeHandler = externalTypeHandler.start();
        assertNotNull(newExternalTypeHandler);
    }

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        String propName = "propName";
        Object bean = new Object();
        JsonParser p = new JsonFactory().createParser("");
        assertFalse(externalTypeHandler.handleTypePropertyValue(p, deserializationContext, propName, bean));
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        String propName = "propName";
        Object bean = new Object();
        JsonParser p = new JsonFactory().createParser("");
        assertFalse(externalTypeHandler.handlePropertyValue(p, deserializationContext, propName, bean));
    }

    @Test
    public void testComplete() throws IOException {
        Object bean = new Object();
        JsonParser p = new JsonFactory().createParser("");
        assertNull(externalTypeHandler.complete(p, deserializationContext, bean));
    }

    @Test
    public void testCompleteWithPropertyValues() throws IOException {
        ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
        SettableBeanProperty property = new SettableBeanProperty();
        TypeDeserializer typeDeser = new TypeDeserializer();
        builder.addExternal(property, typeDeser);
        ExternalTypeHandler externalTypeHandler = builder.build();

        TokenBuffer[] tokens = new TokenBuffer[1];
        String[] typeIds = new String[1];

        Object bean = new Object();
        JsonParser p = new JsonFactory().createParser("");
        builder.build().complete(p, deserializationContext, bean);
    }

    @Test
    public void testCompleteWithPropertyValueBuffer() throws IOException {
        ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
        SettableBeanProperty property = new SettableBeanProperty();
        TypeDeserializer typeDeser = new TypeDeserializer();
        builder.addExternal(property, typeDeser);
        ExternalTypeHandler externalTypeHandler = builder.build();

        PropertyValueBuffer buffer = new PropertyValueBuffer();
        PropertyBasedCreator creator = new PropertyBasedCreator(objectMapper,
                PropertyNamingStrategy.LOWER_CASE);

        Object bean = new Object();
        JsonParser p = new JsonFactory().createParser("");
        builder.build().complete(p, deserializationContext, buffer, creator);
    }

    @Test
    public void testBuilder() {
        ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
        assertNotNull(builder);
    }

    @Test
    public void testExtTypedProperty() {
        SettableBeanProperty property = new SettableBeanProperty();
        TypeDeserializer typeDeser = new TypeDeserializer();
        ExternalTypeHandler.ExtTypedProperty extTypedProperty = new ExternalTypeHandler.ExtTypedProperty(property, typeDeser);
        assertNotNull(extTypedProperty);
    }

}