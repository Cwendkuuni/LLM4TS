package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.PropertyValue.BufferingPropertySettable;
import com.fasterxml.jackson.databind.PropertyValueBuffer;


public class ExternalTypeHandlerTest {

    private ExternalTypeHandler handler;
    private ExtTypedProperty[] properties;
    private HashMap<String, Integer> nameToPropertyIndex;
    private String[] typeIds;
    private TokenBuffer[] tokens;

    @Mock
    JsonParser p;
    @Mock
    DeserializationContext ctxt;
    @Mock
    Object bean;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        properties = new ExtTypedProperty[2];
        nameToPropertyIndex = new HashMap<String, Integer>();
        typeIds = new String[]{"typeId1", "typeId2"};
        tokens = new TokenBuffer[2];
        handler = new ExternalTypeHandler(properties, nameToPropertyIndex, typeIds, tokens);
    }

    @Test
    public void testStart() {
        ExternalTypeHandler result = handler.start();
        assertNotNull(result);
        assertNotSame(handler, result);
        assertEquals(handler._properties, result._properties);
        assertEquals(handler._nameToPropertyIndex, result._nameToPropertyIndex);
        assertEquals(handler._typeIds, result._typeIds);
        assertEquals(handler._tokens, result._tokens);
    }

    @Test
    public void testHandleTypePropertyValue_Positive() throws IOException {
        properties[0] = new ExtTypedProperty(null, null);
        nameToPropertyIndex.put("propName", 0);
        boolean result = handler.handleTypePropertyValue(p, ctxt, "propName", bean);
        assertTrue(result);
        // Verify that typeIds and tokens have been set correctly
        assertNull(handler._typeIds[0]);
        assertNull(handler._tokens[0]);
    }

    @Test
    public void testHandleTypePropertyValue_Negative() throws IOException {
        boolean result = handler.handleTypePropertyValue(p, ctxt, "propName", bean);
        assertFalse(result);
    }

    @Test
    public void testHandlePropertyValue_Positive() throws IOException {
        properties[0] = new ExtTypedProperty(null, null);
        nameToPropertyIndex.put("propName", 0);
        boolean result = handler.handlePropertyValue(p, ctxt, "propName", bean);
        assertTrue(result);
        // Verify that typeIds and tokens have been set correctly
        assertNull(handler._typeIds[0]);
        assertNotNull(handler._tokens[0]);
    }

    @Test
    public void testHandlePropertyValue_Negative() throws IOException {
        boolean result = handler.handlePropertyValue(p, ctxt, "propName", bean);
        assertFalse(result);
    }

    @Test
    public void testComplete() throws IOException {
        properties[0] = new ExtTypedProperty(null, null);
        properties[1] = new ExtTypedProperty(null, null);
        nameToPropertyIndex.put("propName1", 0);
        nameToPropertyIndex.put("propName2", 1);
        handler.complete(p, ctxt, bean);
        // Verify that _deserializeAndSet is called for each property
    }

    @Test
    public void testCompleteWithPropertyValueBuffer() throws IOException {
        properties[0] = new ExtTypedProperty(null, null);
        properties[1] = new ExtTypedProperty(null, null);
        nameToPropertyIndex.put("propName1", 0);
        nameToPropertyIndex.put("propName2", 1);
        PropertyValueBuffer buffer = new PropertyValueBuffer();
        PropertyBasedCreator creator = new PropertyBasedCreator();
        creator.build(ctxt, buffer);
        handler.complete(p, ctxt, buffer, creator);
        // Verify that _deserialize is called for each property
    }

    @Test
    public void testDeserialize() throws IOException {
        properties[0] = new ExtTypedProperty(null, null);
        handler._tokens[0] = new TokenBuffer(null, null);
        String typeId = "typeId";
        Object result = handler._deserialize(p, ctxt, 0, typeId);
        assertNotNull(result);
        // Verify that appropriate methods are called
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        properties[0] = new ExtTypedProperty(null, null);
        handler._tokens[0] = new TokenBuffer(null, null);
        String typeId = "typeId";
        handler._deserializeAndSet(p, ctxt, bean, 0, typeId);
        // Verify that appropriate methods are called
    }
}