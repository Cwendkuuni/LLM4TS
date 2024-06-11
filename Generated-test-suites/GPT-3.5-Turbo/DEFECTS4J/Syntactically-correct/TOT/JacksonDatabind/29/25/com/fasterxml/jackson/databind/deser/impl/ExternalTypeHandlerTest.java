package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ExternalTypeHandlerTest {
    
    @Test
    public void testHandleTypePropertyValue() throws IOException {
        ExternalTypeHandler.ExtTypedProperty property = new ExternalTypeHandler.ExtTypedProperty(null, null);
        property.setTypePropertyName("type");

        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(null, null, null, null);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonFactory jsonFactory = new JsonFactory();

        String jsonString = "{\"type\":\"book\"}";

        JsonParser jp = jsonFactory.createParser(jsonString);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();

        boolean result = externalTypeHandler.handleTypePropertyValue(jp, ctxt, "type", null);
        assertTrue(result);
    }

    @Test
    public void testHandleTypePropertyValue_NoMatchingProperty() {
        ExternalTypeHandler.ExtTypedProperty property = new ExternalTypeHandler.ExtTypedProperty(null, null);
        property.setTypePropertyName("type");

        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(null, null, null, null);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonFactory jsonFactory = new JsonFactory();

        String jsonString = "{\"type\":\"book\"}";

        JsonParser jp = jsonFactory.createParser(jsonString);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();

        boolean result = externalTypeHandler.handleTypePropertyValue(jp, ctxt, "name", null);
        assertFalse(result);
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        ExternalTypeHandler.ExtTypedProperty property = new ExternalTypeHandler.ExtTypedProperty(null, null);
        property.setTypePropertyName("type");

        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(null, null, null, null);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonFactory jsonFactory = new JsonFactory();

        String jsonString = "{\"name\":\"book\"}";

        JsonParser jp = jsonFactory.createParser(jsonString);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();

        boolean result = externalTypeHandler.handlePropertyValue(jp, ctxt, "name", null);
        assertTrue(result);
    }

    @Test
    public void testHandlePropertyValue_NoMatchingProperty() {
        ExternalTypeHandler.ExtTypedProperty property = new ExternalTypeHandler.ExtTypedProperty(null, null);
        property.setTypePropertyName("type");

        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(null, null, null, null);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonFactory jsonFactory = new JsonFactory();

        String jsonString = "{\"type\":\"book\"}";

        JsonParser jp = jsonFactory.createParser(jsonString);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();

        boolean result = externalTypeHandler.handlePropertyValue(jp, ctxt, "name", null);
        assertFalse(result);
    }

    @Test
    public void testComplete() throws IOException {
        ExternalTypeHandler.ExtTypedProperty property = new ExternalTypeHandler.ExtTypedProperty(null, null);
        property.setTypePropertyName("type");

        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(null, null, null, null);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonFactory jsonFactory = new JsonFactory();

        String jsonString = "{\"type\":\"book\"}";

        JsonParser jp = jsonFactory.createParser(jsonString);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();

        Object result = externalTypeHandler.complete(jp, ctxt, null);
        assertNull(result);
    }

    @Test
    public void testComplete_MissingTypeProperty() throws IOException {
        ExternalTypeHandler.ExtTypedProperty property = new ExternalTypeHandler.ExtTypedProperty(null, null);
        property.setTypePropertyName("type");

        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(null, null, null, null);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonFactory jsonFactory = new JsonFactory();

        String jsonString = "{\"name\":\"book\"}";

        JsonParser jp = jsonFactory.createParser(jsonString);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();

        try {
            externalTypeHandler.complete(jp, ctxt, null);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            assertEquals("Missing property 'name' for external type id 'type'", e.getMessage());
        }
    }

    @Test
    public void testComplete_MissingTypeValue() throws IOException {
        ExternalTypeHandler.ExtTypedProperty property = new ExternalTypeHandler.ExtTypedProperty(null, null);
        property.setTypePropertyName("type");

        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(null, null, null, null);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonFactory jsonFactory = new JsonFactory();

        String jsonString = "{\"type\":null}";

        JsonParser jp = jsonFactory.createParser(jsonString);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();

        try {
            externalTypeHandler.complete(jp, ctxt, null);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            assertEquals("Missing property 'type' for external type id 'type'", e.getMessage());
        }
    }

}