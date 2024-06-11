package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.util.*;

import org.junit.Test;

public class ExternalTypeHandlerTest {

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        ObjectNode root = mapper.createObjectNode();
        root.put("prop1", "type1");
        root.put("prop2", "type2");

        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser jp = mapper.getFactory().createParser(root);
        ExtTypedProperty[] properties = handler.start()._properties;
        Integer index1 = _nameToPropertyIndex.get("prop1");
        Integer index2 = _nameToPropertyIndex.get("prop2");

        handler.handleTypePropertyValue(jp, ctxt, "prop1", null);
        assertEquals("type1", handler._typeIds[index1]);
        assertEquals(null, handler._tokens[index1]);

        handler.handleTypePropertyValue(jp, ctxt, "prop2", null);
        assertEquals("type2", handler._typeIds[index2]);
        assertEquals(null, handler._tokens[index2]);

        jp.nextToken(); // consume root end
        handler.complete(jp, ctxt, null);
        assertEquals(2, handler._properties.length);
        assertEquals("type1", handler._typeIds[0]);
        assertEquals("type2", handler._typeIds[1]);
    }

    @Test
    public void testHandlePropertyValueWithTypeId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        ObjectNode root = mapper.createObjectNode();
        root.set("prop1", JsonNodeFactory.valueNode("type1"));
        root.set("prop2", JsonNodeFactory.valueNode("type2"));

        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser jp = mapper.getFactory().createParser(root);
        ExtTypedProperty[] properties = handler.start()._properties;
        Integer index1 = _nameToPropertyIndex.get("prop1");
        Integer index2 = _nameToPropertyIndex.get("prop2");

        handler.handlePropertyValue(jp, ctxt, "prop1", null);
        assertEquals("type1", handler._typeIds[index1]);
        assertEquals(null, handler._tokens[index1]);

        handler.handlePropertyValue(jp, ctxt, "prop2", null);
        assertEquals("type2", handler._typeIds[index2]);
        assertEquals(null, handler._tokens[index2]);

        jp.nextToken(); // consume root end
        handler.complete(jp, ctxt, null);
        assertEquals(2, handler._properties.length);
        assertEquals("type1", handler._typeIds[0]);
        assertEquals("type2", handler._typeIds[1]);
    }

    @Test
    public void testHandlePropertyValueWithBufferedToken() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        ObjectNode root = mapper.createObjectNode();
        root.set("prop1", JsonNodeFactory.valueNode("type1"));
        root.set("prop2", JsonNodeFactory.valueNode("type2"));

        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser jp = mapper.getFactory().createParser(root);
        ExtTypedProperty[] properties = handler.start()._properties;
        Integer index1 = _nameToPropertyIndex.get("prop1");
        Integer index2 = _nameToPropertyIndex.get("prop2");

        handler.handlePropertyValue(jp, ctxt, "prop1", null);
        assertEquals(null, handler._typeIds[index1]);
        assertNotNull(handler._tokens[index1]);

        handler.handlePropertyValue(jp, ctxt, "prop2", null);
        assertEquals(null, handler._typeIds[index2]);
        assertNotNull(handler._tokens[index2]);

        jp.nextToken(); // consume root end
        handler.complete(jp, ctxt, null);
        assertEquals(2, handler._properties.length);
        assertEquals(null, handler._typeIds[0]);
        assertEquals(null, handler._typeIds[1]);
    }

    @Test
    public void testHandlePropertyValueWithNullTypeIdAndNullValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        ObjectNode root = mapper.createObjectNode();

        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser jp = mapper.getFactory().createParser(root);
        handler.handlePropertyValue(jp, ctxt, "prop1", null);
        handler.handlePropertyValue(jp, ctxt, "prop1", null);
        handler.complete(jp, ctxt, null);

        assertEquals(0, handler._properties.length);
        assertNull(handler._typeIds);
        assertNull(handler._tokens);
    }

    @Test
    public void testHandlePropertyValueWithNullTypeIdAndNonEmptyValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        ObjectNode root = mapper.createObjectNode();
        root.set("prop1", JsonNodeFactory.textValue("value1"));

        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser jp = mapper.getFactory().createParser(root);
        handler.handlePropertyValue(jp, ctxt, "prop1", null);
        handler.handlePropertyValue(jp, ctxt, "prop1", null);
        handler.complete(jp, ctxt, null);

        assertEquals(1, handler._properties.length);
        assertEquals("value1", handler._typeIds[0]);
        assertNull(handler._tokens[0]);
    }

    @Test
    public void testHandlePropertyValueWithNonEmptyTypeIdAndNullValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        ObjectNode root = mapper.createObjectNode();
        root.set("typeId1", "type1");

        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser jp = mapper.getFactory().createParser(root);
        handler.handlePropertyValue(jp, ctxt, "typeId1", null);
        handler.handlePropertyValue(jp, ctxt, "typeId1", null);
        handler.complete(jp, ctxt, null);

        assertEquals(1, handler._properties.length);
        assertEquals("type1", handler._typeIds[0]);
        assertNull(handler._tokens[0]);
    }

    @Test
    public void testHandlePropertyValueWithNonEmptyTypeIdAndNonEmptyValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        ObjectNode root = mapper.createObjectNode();
        root.set("typeId1", "type1");
        root.set("prop1", JsonNodeFactory.textValue("value1"));

        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser jp = mapper.getFactory().createParser(root);
        handler.handlePropertyValue(jp, ctxt, "typeId1", null);
        handler.handlePropertyValue(jp, ctxt, "prop1", null);
        handler.complete(jp, ctxt, null);

        assertEquals(1, handler._properties.length);
        assertEquals("value1", handler._typeIds[0]);
        assertNull(handler._tokens[0]);
    }

    @Test
    public void testCompleteWithNullTypeIdAndNullValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        ObjectNode root = mapper.createObjectNode();

        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object bean = handler.complete(mapper.getFactory().getCarFactory(), ctxt, null);

        assertEquals(0, handler._properties.length);
        assertNull(handler._typeIds);
        assertNull(handler._tokens);
        assertNotNull(bean);
    }

    @Test
    public void testCompleteWithNullTypeIdAndNonEmptyValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        ObjectNode root = mapper.createObjectNode();
        root.set("prop1", JsonNodeFactory.textValue("value1"));

        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object bean = handler.complete(mapper.getFactory().getCarFactory(), ctxt, null);

        assertEquals(1, handler._properties.length);
        assertEquals("value1", handler._typeIds[0]);
        assertNull(handler._tokens[0]);
        assertNotNull(bean);
    }

    @Test
    public void testCompleteWithNonEmptyTypeIdAndNullValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        ObjectNode root = mapper.createObjectNode();
        root.set("typeId1", "type1");

        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object bean = handler.complete(mapper.getFactory().getCarFactory(), ctxt, null);

        assertEquals(1, handler._properties.length);
        assertEquals("type1", handler._typeIds[0]);
        assertNull(handler._tokens[0]);
        assertNotNull(bean);
    }

    @Test
    public void testCompleteWithNonEmptyTypeIdAndNonEmptyValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        ObjectNode root = mapper.createObjectNode();
        root.set("typeId1", "type1");
        root.set("prop1", JsonNodeFactory.textValue("value1"));

        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object bean = handler.complete(mapper.getFactory().getCarFactory(), ctxt, null);

        assertEquals(1, handler._properties.length);
        assertEquals("value1", handler._typeIds[0]);
        assertNull(handler._tokens[0]);
        assertNotNull(bean);
    }

    @Test
    public void testCompleteWithBufferedValuesAndNullCreator() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        ObjectNode root = mapper.createObjectNode();
        root.set("prop1", JsonNodeFactory.valueNode("type1"));
        root.set("prop2", JsonNodeFactory.valueNode("type2"));

        PropertyValueBuffer buffer = new PropertyValueBuffer();
        ObjectNode creator = mapper.createObjectNode();

        handler.handlePropertyValue(mapper.getFactory().getParserForObject(root), mapper.getDeserializationContext(), "prop1", null);
        handler.handlePropertyValue(mapper.getFactory().getParserForObject(root), mapper.getDeserializationContext(), "prop2", null);

        Object bean = handler.complete(mapper.getFactory().getParserForObject(root), mapper.getDeserializationContext(), buffer, null);

        assertEquals(2, handler._properties.length);
        assertEquals("type1", handler._typeIds[0]);
        assertEquals("type2", handler._typeIds[1]);
        assertNull(handler._tokens[0]);
        assertNull(handler._tokens[1]);
        assertNotNull(bean);
        assertEquals("type1", buffer.getValueTypeName(0));
        assertEquals("type2", buffer.getValueTypeName(1));
    }

    @Test
    public void testCompleteWithBufferedValuesAndCreator() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        ObjectNode root = mapper.createObjectNode();
        root.set("prop1", JsonNodeFactory.valueNode("type1"));
        root.set("prop2", JsonNodeFactory.valueNode("type2"));

        PropertyValueBuffer buffer = new PropertyValueBuffer();
        ObjectNode creator = mapper.createObjectNode();
        creator.set(" creatorMethod", JsonNodeFactory.constructorArgValue("arg1"));

        handler.handlePropertyValue(mapper.getFactory().getParserForObject(root), mapper.getDeserializationContext(), "prop1", null);
        handler.handlePropertyValue(mapper.getFactory().getParserForObject(root), mapper.getDeserializationContext(), "prop2", null);

        Object bean = handler.complete(mapper.getFactory().getParserForObject(root), mapper.getDeserializationContext(), buffer, creator);

        assertEquals(2, handler._properties.length);
        assertEquals("type1", handler._typeIds[0]);
        assertEquals("type2", handler._typeIds[1]);
        assertNull(handler._tokens[0]);
        assertNull(handler._tokens[1]);
        assertNotNull(bean);
        assertEquals("type1", buffer.getValueTypeName(0));
        assertEquals("type2", buffer.getValueTypeName(1));
        assertNotNull(creator.get(" creatorMethod").get("arg1"));
    }

    private static Map<String, Integer> _nameToPropertyIndex = new HashMap<>();

}