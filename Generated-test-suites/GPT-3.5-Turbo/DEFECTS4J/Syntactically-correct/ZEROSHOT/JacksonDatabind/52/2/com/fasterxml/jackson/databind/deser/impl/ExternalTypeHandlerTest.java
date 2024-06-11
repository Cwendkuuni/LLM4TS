package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.TokenBuffer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ExternalTypeHandlerTest {
    
    private ExternalTypeHandler externalTypeHandler;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ExtTypedProperty[] properties = new ExtTypedProperty[1];
        HashMap<String, Integer> nameToPropertyIndex = new HashMap<>();
        String[] typeIds = new String[1];
        TokenBuffer[] tokens = new TokenBuffer[1];
        
        externalTypeHandler = new ExternalTypeHandler(properties, nameToPropertyIndex, typeIds, tokens);
    }

    @Test
    public void testStart() {
        ExternalTypeHandler newHandler = externalTypeHandler.start();
        assertNotNull(newHandler);
        assertNotEquals(externalTypeHandler, newHandler);
    }

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        String propName = "type";
        Object bean = new Object();
        
        boolean result = externalTypeHandler.handleTypePropertyValue(p, ctxt, propName, bean);
        
        assertFalse(result);
        verify(p, times(1)).getText();
        verify(bean, times(0)).getClass();
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        String propName = "type";
        Object bean = new Object();
        
        boolean result = externalTypeHandler.handlePropertyValue(p, ctxt, propName, bean);
        
        assertFalse(result);
        verify(p, times(1)).getText();
        verify(p, times(1)).skipChildren();
        verify(bean, times(0)).getClass();
    }

    @Test
    public void testComplete() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object bean = new Object();
        
        Object result = externalTypeHandler.complete(p, ctxt, bean);
        
        assertEquals(bean, result);
        verify(p, times(0)).skipChildren();
        verify(bean, times(0)).getClass();
    }

    @Test
    public void testCompleteWithProperty() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        PropertyValueBuffer buffer = mock(PropertyValueBuffer.class);
        PropertyBasedCreator creator = mock(PropertyBasedCreator.class);
        Object bean = new Object();
        
        Object result = externalTypeHandler.complete(p, ctxt, buffer, creator);
        
        assertNull(result);
        verify(p, times(0)).skipChildren();
        verify(bean, times(0)).getClass();
    }
}