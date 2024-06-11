package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

import java.io.IOException;
import java.util.*;

/**
 * Test class for {@link ExternalTypeHandler}.
 */
public class ExternalTypeHandlerTest {

    /**
     * Test for {@link ExternalTypeHandler#start()} method.
     *
     * @throws Exception if an error occurs during testing
     */
    @Test
    public void testStart() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"typeId1\": 1, \"typeId2\": 2, \"prop1\": \"value1\", \"prop2\": \"value2\"}";
        ExternalTypeHandler handler = mapper.readValue(json, ExternalTypeHandler.class).start();
        assertEquals(2, handler._properties.length);
        assertEquals("typeId1", handler._nameToPropertyIndex.get("typeId1").toString());
        assertEquals("typeId2", handler._nameToPropertyIndex.get("typeId2").toString());
        assertEquals("prop1", handler._nameToPropertyIndex.get("prop1").toString());
        assertEquals("prop2", handler._nameToPropertyIndex.get("prop2").toString());
    }

    /**
     * Test for {@link ExternalTypeHandler#handleTypePropertyValue(JsonParser, DeserializationContext, String, Object)} method.
     *
     * @throws Exception if an error occurs during testing
     */
    @Test
    public void testHandleTypePropertyValue() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"prop\": {\"typeId\": \"StringTypeId\"}, \"value\": \"test\"}";
        ExternalTypeHandler handler = mapper.readValue(json, ExternalTypeHandler.class).start();
        JsonParser parser = mapper.getFactory().createParser(json);
        DeserializationContext context = mapper.getFactory().createContext();
        Object bean = new Object();

        assertTrue(handler.handleTypePropertyValue(parser, context, "prop", bean));
        assertEquals("StringTypeId", handler._typeIds[0]);
        assertNull(handler._tokens[0]);

        parser.nextToken(); // move the parser to the next token
        assertFalse(handler.handleTypePropertyValue(parser, context, "prop", bean));
        assertNull(handler._typeIds[0]);
        assertNull(handler._tokens[0]);
    }

    /**
     * Test for {@link ExternalTypeHandler#handlePropertyValue(JsonParser, DeserializationContext, String, Object)} method.
     *
     * @throws Exception if an error occurs during testing
     */
    @Test
    public void testHandlePropertyValue() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"prop\": \"value\"}";
        ExternalTypeHandler handler = mapper.readValue(json, ExternalTypeHandler.class).start();
        JsonParser parser = mapper.getFactory().createParser(json);
        DeserializationContext context = mapper.getFactory().createContext();
        Object bean = new Object();

        assertTrue(handler.handlePropertyValue(parser, context, "prop", bean));
        assertNull(handler._typeIds[0]);
        assertNotNull(handler._tokens[0]);

        parser.nextToken(); // move the parser to the next token
        assertFalse(handler.handlePropertyValue(parser, context, "prop", bean));
        assertNull(handler._typeIds[0]);
        assertNull(handler._tokens[0]);
    }

    /**
     * Test for {@link ExternalTypeHandler#complete(JsonParser, DeserializationContext, Object)} method.
     *
     * @throws Exception if an error occurs during testing
     */
    @Test
    public void testComplete() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"typeId1\": 1, \"prop1\": \"value1\"}";
        ExternalTypeHandler handler = mapper.readValue(json, ExternalTypeHandler.class).start();
        JsonParser parser = mapper.getFactory().createParser(json);
        DeserializationContext context = mapper.getFactory().createContext();
        Object bean = new Object();

        handler.handleTypePropertyValue(parser, context, "typeId1", bean);
        handler.handlePropertyValue(parser, context, "prop1", bean);
        parser.nextToken(); // move the parser to the next token to close the JSON object

        Object result = handler.complete(parser, context, bean);
        assertNotNull(result);

        // Check if the properties have been set correctly
        Object property1Value = handler.getPropertyValue(bean, "prop1");
        assertEquals("value1", property1Value);
        Object typeId1Value = handler.getPropertyValue(bean, "typeId1");
        assertEquals(1, typeId1Value);
    }

    /**
     * Test for {@link ExternalTypeHandler#complete(JsonParser, DeserializationContext, PropertyValueBuffer, PropertyBasedCreator)} method.
     *
     * @throws Exception if an error occurs during testing
     */
    @Test
    public void testCompleteWithPropertyValueBuffer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"typeId1\": 1, \"prop1\": \"value1\"}";
        ExternalTypeHandler handler = mapper.readValue(json, ExternalTypeHandler.class).start();
        JsonParser parser = mapper.getFactory().createParser(json);
        DeserializationContext context = mapper.getFactory().createContext();
        PropertyValueBuffer buffer = new PropertyValueBuffer();
        PropertyBasedCreator creator = mapper.getTypeFactory().constructValue("com.example.MyClass");

        handler.handleTypePropertyValue(parser, context, "typeId1", null);
        handler.handlePropertyValue(parser, context, "prop1", null);
        parser.nextToken(); // move the parser to the next token to close the JSON object

        handler.complete(parser, context, buffer, creator);
        Object result = creator.build(context, buffer);
        assertNotNull(result);

        // Check if the properties have been set correctly
        Object property1Value = handler.getPropertyValue(result, "prop1");
        assertEquals("value1", property1Value);
        Object typeId1Value = handler.getPropertyValue(result, "typeId1");
        assertEquals(1, typeId1Value);
    }

    /**
     * Test for error scenarios in {@link ExternalTypeHandler#handleTypePropertyValue(JsonParser, DeserializationContext, String, Object)} method.
     *
     * @throws Exception if an error occurs during testing
     */
    @Test(expected = IOException.class)
    public void testHandleTypePropertyValueErrorScenarios() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json1 = "{\"prop\": null, \"value\": \"test\"}"; // null typeId
        String json2 = "{\"prop\": {\"typeId\": null}, \"value\": \"test\"}"; // null typeId property name
        String json3 = "{\"prop\": \"value\", \"typeId\": null}"; // null typeId value
        String json4 = "{\"prop\": \"value\"}"; // missing typeId property
        ExternalTypeHandler handler = mapper.readValue(json1, ExternalTypeHandler.class).start();
        JsonParser parser = mapper.getFactory().createParser(json1);
        DeserializationContext context = mapper.getFactory().createContext();
        Object bean = new Object();

        handler.handleTypePropertyValue(parser, context, "prop", bean);
        handler.handlePropertyValue(parser, context, "prop", bean);
        parser.nextToken(); // move the parser to the next token to close the JSON object

        handler.complete(parser, context, bean);
    }

    /**
     * Test for error scenarios in {@link ExternalTypeHandler#handlePropertyValue(JsonParser, DeserializationContext, String, Object)} method.
     *
     * @throws Exception if an error occurs during testing
     */
    @Test(expected = IOException.class)
    public void testHandlePropertyValueErrorScenarios() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json1 = "{\"prop\": null, \"value\": \"test\"}"; // null property name
        String json2 = "{\"prop\": \"value\", \"typeId\": null}"; // null typeId
        String json3 = "{\"prop\": \"value\", \"typeId\": \"nonExistentTypeId\"}"; // non-existent type id
        String json4 = "{\"prop\": \"value\", \"typeId\": \"StringTypeId\"}"; // non-creator property
        ExternalTypeHandler handler = mapper.readValue(json1, ExternalTypeHandler.class).start();
        JsonParser parser = mapper.getFactory().createParser(json1);
        DeserializationContext context = mapper.getFactory().createContext();
        Object bean = new Object();

        handler.handlePropertyValue(parser, context, null, bean);
        handler.handleTypePropertyValue(parser, context, "prop", bean);
        parser.nextToken(); // move the parser to the next token to close the JSON object

        handler.complete(parser, context, bean);
    }
}