package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class ExternalTypeHandlerTest {

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        String json = "{\"prop1\":\"StringTypeId1\", \"prop2\":null}";
        JsonParser jp = mapper.getFactory().createParser(json);

        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object bean = new Object();

        assertTrue(handler.handleTypePropertyValue(jp, ctxt, "prop1", bean));
        assertEquals("StringTypeId1", handler._typeIds[0]);

        assertNull(handler._typeIds[1]);

        assertNull(handler.handleTypePropertyValue(jp, ctxt, "prop2", bean));
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        String json = "{\"prop1\":null, \"prop2\":\"IntTypeId\"}";
        JsonParser jp = mapper.getFactory().createParser(json);

        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object bean = new Object();

        assertFalse(handler.handleTypePropertyValue(jp, ctxt, "prop1", bean));
        assertNull(handler._typeIds[0]);

        assertTrue(handler.handlePropertyValue(jp, ctxt, "prop2", bean));
        assertEquals("IntTypeId", handler._typeIds[1]);
    }

    @Test
    public void testComplete() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        String json = "{\"prop1\":null, \"prop2\":\"IntTypeId\"}";
        JsonParser jp = mapper.getFactory().createParser(json);

        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object bean = new Object();

        handler.handlePropertyValue(jp, ctxt, "prop2", bean);
        handler.handlePropertyValue(jp, ctxt, "prop1", bean);

        handler.complete(jp, ctxt, bean);

        assertEquals("IntTypeId", handler._typeIds[0]);
        assertEquals("IntTypeId", handler._typeIds[1]);
        assertNull(handler._typeIds[2]);
    }

    @Test
    public void testCompleteWithNullTypeId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        String json = "{\"prop1\":null, \"prop2\":null}";
        JsonParser jp = mapper.getFactory().createParser(json);

        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object bean = new Object();

        handler.handlePropertyValue(jp, ctxt, "prop2", bean);
        handler.handlePropertyValue(jp, ctxt, "prop1", bean);

        handler.complete(jp, ctxt, bean);

        assertNull(handler._typeIds[0]);
        assertNull(handler._typeIds[1]);
    }

    @Test
    public void testCompleteWithMissingTypeId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        String json = "{\"prop1\":\"StringTypeId\", \"prop2\":null}";
        JsonParser jp = mapper.getFactory().createParser(json);

        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object bean = new Object();

        handler.handleTypePropertyValue(jp, ctxt, "prop1", bean);

        try {
            handler.complete(jp, ctxt, bean);
            fail();
        } catch (MappingException e) {
            assertEquals("Missing property 'prop2' for external type id 'StringTypeId'", e.getMessage());
        }
    }

    @Test
    public void testCompleteWithMissingProperty() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        String json = "{\"prop1\":\"StringTypeId\", \"prop3\":null}";
        JsonParser jp = mapper.getFactory().createParser(json);

        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object bean = new Object();

        try {
            handler.complete(jp, ctxt, bean);
            fail();
        } catch (MappingException e) {
            assertEquals("Missing property 'prop2' for external type id 'StringTypeId'", e.getMessage());
        }
    }

    @Test
    public void testHandleTypePropertyValueWithNestedObject() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        String json = "{\"obj\":{\"prop1\":\"StringTypeId1\"}}";
        JsonParser jp = mapper.getFactory().createParser(json);

        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object bean = new Object();

        assertTrue(handler.handleTypePropertyValue(jp, ctxt, "obj.prop1", bean));
        assertEquals("StringTypeId1", handler._typeIds[0]);
    }

    @Test
    public void testHandlePropertyValueWithNestedObject() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        String json = "{\"obj\":{}}";
        JsonParser jp = mapper.getFactory().createParser(json);

        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object bean = new Object();

        JsonToken expectedToken = JsonToken.START_OBJECT;
        JsonToken actualToken;

        while ((actualToken = jp.nextToken()) != null) {
            if (actualToken.equals(expectedToken)) {
                handler.handlePropertyValue(jp, ctxt, "obj", bean);
                break;
            }
        }

        assertEquals("obj", handler.getPropertyNameOfCurrentIndex());
        assertNull(handler._typeIds[0]);

        String typeId = jp.getText();
        handler.handleTypePropertyValue(jp, ctxt, "obj.typeId", bean);

        assertEquals(typeId, handler._typeIds[0]);
    }

    private String getPropertyNameOfCurrentIndex() {
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        return handler._properties[handler.getPropertyIndex()].getProperty().getName();
    }
}