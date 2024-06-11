package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;
import org.junit.Test;

public class ExternalTypeHandlerTest {
    @Test
    public void testHandleTypePropertyValue() {
        ExternalTypeHandler.ExtTypedProperty property = new ExternalTypeHandler.ExtTypedProperty(property, typeDeser);
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(properties, nameToPropertyIndex, typeIds, tokens);
        boolean result = externalTypeHandler.handleTypePropertyValue(jp, ctxt, propName, bean);
        assertEquals(true, result);
    }

    @Test
    public void testHandlePropertyValue() {
        ExternalTypeHandler.ExtTypedProperty property = new ExternalTypeHandler.ExtTypedProperty(property, typeDeser);
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(properties, nameToPropertyIndex, typeIds, tokens);
        boolean result = externalTypeHandler.handlePropertyValue(jp, ctxt, propName, bean);
        assertEquals(true, result);
    }

    @Test
    public void testComplete() {
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(properties, nameToPropertyIndex, typeIds, tokens);
        Object result = externalTypeHandler.complete(p, ctxt, bean);
        assertNotNull(result);
    }

    @Test
    public void testCompleteWithBuffer() {
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(properties, nameToPropertyIndex, typeIds, tokens);
        Object result = externalTypeHandler.complete(jp, ctxt, buffer, creator);
        assertNotNull(result);
    }
}