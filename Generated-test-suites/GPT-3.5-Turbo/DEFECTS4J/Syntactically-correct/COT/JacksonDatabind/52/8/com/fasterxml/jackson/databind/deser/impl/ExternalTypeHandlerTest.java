
package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Test;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

import static org.junit.Assert.*;

public class ExternalTypeHandlerTest {

    @Test
    public void testStart() {
        ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
        ExternalTypeHandler result = handler.start();
        assertNotNull(result);
    }

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
        boolean result = handler.handleTypePropertyValue(null, null, null, null);
        assertFalse(result);
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
        boolean result = handler.handlePropertyValue(null, null, null, null);
        assertFalse(result);
    }

    @Test
    public void testComplete() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
        Object result = handler.complete(null, null, null);
        assertNull(result);
    }

    @Test
    public void testCompleteWithCreator() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
        PropertyValueBuffer buffer = new PropertyValueBuffer(null);
        PropertyBasedCreator creator = new PropertyBasedCreator(null, null);
        Object result = handler.complete(null, null, buffer, creator);
        assertNull(result);
    }

    @Test
    public void testExternalTypeHandlerBuilder() {
        ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
        builder.addExternal(null, null);
        ExternalTypeHandler handler = builder.build();
        assertNotNull(handler);
    }

    @Test
    public void testExtTypedProperty() {
        SettableBeanProperty property = new SettableBeanProperty(null, null, null, null, null, null, false, null);
        TypeDeserializer typeDeser = new TypeDeserializer(null, null, null, false, null);
        ExternalTypeHandler.ExtTypedProperty extTypedProperty = new ExternalTypeHandler.ExtTypedProperty(property, typeDeser);
        assertNotNull(extTypedProperty);

        extTypedProperty.linkTypeProperty(property);
        assertNotNull(extTypedProperty.getTypeProperty());
    }
}
