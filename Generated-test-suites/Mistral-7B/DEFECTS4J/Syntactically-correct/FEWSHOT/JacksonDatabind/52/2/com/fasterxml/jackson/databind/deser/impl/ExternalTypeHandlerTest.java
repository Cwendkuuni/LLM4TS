package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class ExternalTypeHandlerTest {

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        ExternalTypeHandler handler = createTestHandler();
        JsonParser p = createTestParser();
        DeserializationContext ctxt = createTestContext();

        handler.start();

        String propName = "prop1";
        String typeId = "Type1";
        handler.handleTypePropertyValue(p, ctxt, propName, null);
        assertEquals(typeId, handler._typeIds[handler._nameToPropertyIndex.get(propName).intValue()]);

        // Test null bean
        handler.handleTypePropertyValue(p, ctxt, propName, null);
        assertEquals(typeId, handler._typeIds[handler._nameToPropertyIndex.get(propName).intValue()]);

        Object bean = new Object();
        handler.handleTypePropertyValue(p, ctxt, propName, bean);
        assertEquals(typeId, handler._typeIds[handler._nameToPropertyIndex.get(propName).intValue()]);
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        ExternalTypeHandler handler = createTestHandler();
        JsonParser p = createTestParser();
        DeserializationContext ctxt = createTestContext();

        handler.start();

        String propName = "prop1";
        String typeId = "Type1";
        handler.handlePropertyValue(p, ctxt, propName, null);
        assertNull(handler._typeIds[handler._nameToPropertyIndex.get(propName).intValue()]);

        Object bean = new Object();
        handler.handlePropertyValue(p, ctxt, propName, bean);
        assertEquals(typeId, handler._typeIds[handler._nameToPropertyIndex.get(propName).intValue()]);

        String json = "{\"prop1\":\"Type1\"}";
        p.readValueAsString(json);
        assertEquals(typeId, handler._typeIds[handler._nameToPropertyIndex.get(propName).intValue()]);
    }

    @Test
    public void testComplete() throws IOException {
        ExternalTypeHandler handler = createTestHandler();
        JsonParser p = createTestParser();
        DeserializationContext ctxt = createTestContext();
        Object bean = new Object();

        handler.start();

        String propName1 = "prop1";
        String typeId1 = "Type1";
        handler.handlePropertyValue(p, ctxt, propName1, null);
        handler._tokens[handler._nameToPropertyIndex.get(propName1).intValue()] = null;

        String propName2 = "prop2";
        String typeId2 = "Type2";
        handler.handlePropertyValue(p, ctxt, propName2, bean);

        handler.complete(p, ctxt, bean);

        assertEquals(typeId1, handler._typeIds[handler._nameToPropertyIndex.get(propName1).intValue()]);
        assertEquals(typeId2, handler._typeIds[handler._nameToPropertyIndex.get(propName2).intValue()]);
    }

    @Test
    public void testCompleteWithPropertyValueBuffer() throws IOException {
        ExternalTypeHandler handler = createTestHandler();
        JsonParser p = createTestParser();
        DeserializationContext ctxt = createTestContext();
        PropertyValueBuffer buffer = new PropertyValueBuffer();
        PropertyBasedCreator creator = mock(PropertyBasedCreator.class);

        handler.start();

        String propName1 = "prop1";
        String typeId1 = "Type1";
        handler.handlePropertyValue(p, ctxt, propName1, null);
        handler._tokens[handler._nameToPropertyIndex.get(propName1).intValue()] = null;

        String propName2 = "prop2";
        String typeId2 = "Type2";
        handler.handlePropertyValue(p, ctxt, propName2, bean);

        handler.complete(p, ctxt, buffer, creator);

        Object result = creator.build(ctxt, buffer);

        assertEquals(typeId1, handler._typeIds[handler._nameToPropertyIndex.get(propName1).intValue()]);
        assertEquals(typeId2, handler._typeIds[handler._nameToPropertyIndex.get(propName2).intValue()]);
        assertEquals(result.getClass(), Object.class);
    }

    @Test
    public void testCompleteWithNullTypeId() throws IOException {
        ExternalTypeHandler handler = createTestHandler();
        JsonParser p = createTestParser();
        DeserializationContext ctxt = createTestContext();
        Object bean = new Object();

        handler.start();

        String propName = "prop1";
        handler.handlePropertyValue(p, ctxt, propName, null);
        handler._tokens[handler._nameToPropertyIndex.get(propName).intValue()] = null;

        handler.complete(p, ctxt, bean);

        // No type id should be set
        assertNull(handler._typeIds[handler._nameToPropertyIndex.get(propName).intValue()]);
    }

    @Test
    public void testCompleteWithMissingProperty() throws IOException {
        ExternalTypeHandler handler = createTestHandler();
        JsonParser p = createTestParser();
        DeserializationContext ctxt = createTestContext();
        Object bean = new Object();

        handler.start();

        String propName = "prop1";
        handler.handleTypePropertyValue(p, ctxt, propName, bean);
        handler._typeIds[handler._nameToPropertyIndex.get(propName).intValue()] = null;

        handler.complete(p, ctxt, bean);

        ctxt.expectedException(MappingException.class);
        handler.complete(p, ctxt, bean);
    }

    private ExternalTypeHandler createTestHandler() {
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder());
        handler.addProperty("prop1", new SimpleTypeDeserializer(String.class));
        handler.addProperty("prop2", new SimpleTypeDeserializer(Integer.class));
        return handler;
    }

    private JsonParser createTestParser() {
        return new TestJsonParser();
    }

    private DeserializationContext createTestContext() {
        return new TestDeserializationContext();
    }
}

class TestJsonParser extends JsonParserBase {
    @Override
    public void nextToken() throws IOException {
        // Empty implementation
    }

    @Override
    public void close() throws IOException {
        // Empty implementation
    }
}

class TestDeserializationContext extends DeserializationContextBase {
    @Override
    public void reportMappingFailure(MappingException me) {
        // Empty implementation
    }

    @Override
    public void reportMappingException(String msg) {
        // Empty implementation
    }
}