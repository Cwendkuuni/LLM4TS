package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Test;

/**
 * Test case for {@link ExternalTypeHandler} class.
 */
public class ExternalTypeHandlerTest {

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        JsonFactory factory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper(factory);
        String json = "{\"typeProperty\": \"StringType\", \"data\": {}}";
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[]{new ExtTypedProperty(
                new SimpleBeanPropertyDefinition("typeProperty", String.class), new SimpleTypeDeserializer(String.class))},
                new HashMap<>(), new String[0], new TokenBuffer[0]);

        JsonParser jp = factory.createParser(json);

        Object result = handler.start();
        result = handler.handleTypePropertyValue(jp, mapper.getDeserializationContext(), "typeProperty", result);
        assertEquals("StringType", result);
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        JsonFactory factory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper(factory);
        String json = "{\"data\": \"dataValue\"}";
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[]{new ExtTypedProperty(
                new SimpleBeanPropertyDefinition("data", String.class), new SimpleTypeDeserializer(String.class))},
                new HashMap<>(), new String[0], new TokenBuffer[0]);

        JsonParser jp = factory.createParser(json);

        Object result = handler.start();
        result = handler.handlePropertyValue(jp, mapper.getDeserializationContext(), "data", result);
        assertEquals("dataValue", result);
    }

    @Test
    public void testComplete() throws IOException {
        JsonFactory factory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper(factory);
        String json = "{\"typeProperty\": \"StringType\"}";
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[]{new ExtTypedProperty(
                new SimpleBeanPropertyDefinition("typeProperty", String.class), new SimpleTypeDeserializer(String.class))},
                new HashMap<>(), new String[0], new TokenBuffer[0]);

        JsonParser jp = factory.createParser(json);
        Object result = handler.start();
        result = handler.handleTypePropertyValue(jp, mapper.getDeserializationContext(), "typeProperty", result);

        result = handler.complete(jp, mapper.getDeserializationContext(), null);
        assertNotNull(result);
    }

    @Test(expected = JsonMappingException.class)
    public void testMissingPropertyForExternalTypeId() throws IOException {
        JsonFactory factory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper(factory);
        String json = "{\"typeProperty\": \"StringType\"}";
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[]{new ExtTypedProperty(
                new SimpleBeanPropertyDefinition("data", String.class), new SimpleTypeDeserializer(String.class))},
                new HashMap<>(), new String[0], new TokenBuffer[0]);

        JsonParser jp = factory.createParser(json);
        Object result = handler.start();
        handler.handlePropertyValue(jp, mapper.getDeserializationContext(), "typeProperty", result);
        handler.handlePropertyValue(jp, mapper.getDeserializationContext(), "missingProperty", result);
        handler.complete(jp, mapper.getDeserializationContext(), null);
    }

    @Test(expected = JsonMappingException.class)
    public void testMissingExternalTypeId() throws IOException {
        JsonFactory factory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper(factory);
        String json = "{\"data\": \"dataValue\"}";
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[]{new ExtTypedProperty(
                new SimpleBeanPropertyDefinition("typeProperty", String.class), new SimpleTypeDeserializer(String.class))},
                new HashMap<>(), new String[0], new TokenBuffer[0]);

        JsonParser jp = factory.createParser(json);
        Object result = handler.start();
        handler.handlePropertyValue(jp, mapper.getDeserializationContext(), "typeProperty", result);
        handler.handlePropertyValue(jp, mapper.getDeserializationContext(), "data", result);
        handler.complete(jp, mapper.getDeserializationContext(), null);

        json = "{\"data\": \"dataValue\"}";
        result = handler.start();
        handler.handlePropertyValue(jp, mapper.getDeserializationContext(), "typeProperty", result);
        handler.handlePropertyValue(jp, mapper.getDeserializationContext(), "data", result);
        handler.handleTypePropertyValue(jp, mapper.getDeserializationContext(), "missingTypeProperty", result);
        handler.complete(jp, mapper.getDeserializationContext(), null);
    }

    @Test
    public void testCompleteWithPropertyBasedCreator() throws IOException {
        JsonFactory factory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper(factory);
        String json = "{\"typeProperty\": \"StringType\"}";
        ExternalTypeHandler handler = new ExternalTypeHandler(new Builder()
                .addExternal(new SimpleBeanPropertyDefinition("typeProperty", String.class),
                        new SimpleTypeDeserializer(String.class))
                .addExternal(new SimpleBeanPropertyDefinition("data", String.class),
                        new PropertyBasedCreator() {
                            @Override
                            public Object createUsingGuid(DeserializationContext ctxt, PropertyName propertyName,
                                                          JsonParser p) throws IOException {
                                return p.getText();
                            }
                        })
                .build());

        JsonParser jp = factory.createParser(json);
        Object result = handler.start();
        result = handler.handleTypePropertyValue(jp, mapper.getDeserializationContext(), "typeProperty", result);

        result = handler.complete(jp, mapper.getDeserializationContext(), null, null);
        assertEquals("StringType", result);

        json = "{\"data\": \"dataValue\"}";
        result = handler.start();
        result = handler.handlePropertyValue(jp, mapper.getDeserializationContext(), "typeProperty", result);
        result = handler.handlePropertyValue(jp, mapper.getDeserializationContext(), "data", result);

        result = handler.complete(jp, mapper.getDeserializationContext(), null, null);
        assertEquals("dataValue", result);
    }
}