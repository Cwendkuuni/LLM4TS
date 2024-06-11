package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class ExternalTypeHandlerTest {

    @SuppressWarnings("unchecked")
    private static ExternalTypeHandler createHandler(ObjectNode node) throws IOException {
        ObjectReader reader = new ObjectMapper().readerFor(ExternalTypeHandler.class);
        return reader.readValue(node);
    }

    @SuppressWarnings("unchecked")
    private static ObjectNode createNodeWithProperties(String... propertyNames) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        for (String name : propertyNames) {
            node.put(name, null);
        }
        return node;
    }

    @org.junit.Test
    public void testHandleTypePropertyValue() throws IOException {
        ObjectNode node = createNodeWithProperties("prop1", "prop2");
        ExternalTypeHandler handler = createHandler(node);

        JsonParser parser = new ObjectMapper().getFactory().createParser(node.toString());

        DeserializationContext context = mock(DeserializationContext.class);
        Object bean = new Object();

        handler.handleTypePropertyValue(parser, context, "prop1", bean);

        assertEquals("prop1", parser.getCurrentName());
        assertTrue(parser.nextToken() == JsonToken.END_OBJECT);
    }

    @org.junit.Test
    public void testHandlePropertyValue() throws IOException {
        ObjectNode node = createNodeWithProperties("prop1", "prop2");
        ExternalTypeHandler handler = createHandler(node);

        JsonParser parser = new ObjectMapper().getFactory().createParser(node.toString());

        DeserializationContext context = mock(DeserializationContext.class);
        Object bean = new Object();

        handler.handlePropertyValue(parser, context, "prop1", bean);

        assertEquals("prop1", parser.getCurrentName());
        assertTrue(parser.nextToken() == JsonToken.START_ARRAY);
        assertEquals("typeId1", parser.getText());
        assertTrue(parser.nextToken() == JsonToken.END_ARRAY);
        assertTrue(parser.nextToken() == JsonToken.END_OBJECT);
    }

    @org.junit.Test
    public void testHandleTypePropertyValueWithTypeIdProperty() throws IOException {
        ObjectNode node = createNodeWithProperties("prop1", "prop2");
        node.put("typeIdProp", "typeIdProperty");

        ExternalTypeHandler handler = createHandler(node);

        JsonParser parser = new ObjectMapper().getFactory().createParser(node.toString());

        DeserializationContext context = mock(DeserializationContext.class);
        Object bean = new Object();

        handler.handleTypePropertyValue(parser, context, "prop1", bean);

        assertEquals("prop1", parser.getCurrentName());
        assertEquals("typeIdProperty", parser.nextTextValue());
        assertTrue(parser.nextToken() == JsonToken.END_OBJECT);
    }

    @org.junit.Test
    public void testHandlePropertyValueWithTypeIdProperty() throws IOException {
        ObjectNode node = createNodeWithProperties("prop1", "prop2");
        node.put("typeIdProp", "typeIdProperty");

        ExternalTypeHandler handler = createHandler(node);

        JsonParser parser = new ObjectMapper().getFactory().createParser(node.toString());

        DeserializationContext context = mock(DeserializationContext.class);
        Object bean = new Object();

        handler.handlePropertyValue(parser, context, "prop1", bean);

        assertEquals("prop1", parser.getCurrentName());
        assertTrue(parser.nextToken() == JsonToken.START_ARRAY);
        assertEquals("typeIdProperty", parser.getText());
        assertTrue(parser.nextToken() == JsonToken.END_ARRAY);
        assertTrue(parser.nextToken() == JsonToken.END_OBJECT);
    }

    @org.junit.Test
    public void testCompleteWithNoMissingProperties() throws IOException {
        ObjectNode node = createNodeWithProperties("prop1", "prop2");

        ExternalTypeHandler handler = createHandler(node);

        JsonParser parser = new ObjectMapper().getFactory().createParser(node.toString());

        DeserializationContext context = mock(DeserializationContext.class);
        Object bean = new Object();

        handler.complete(parser, context, bean);

        assertTrue(parser.nextToken() == JsonToken.END_OBJECT);
    }

    @org.junit.Test(expected = MappingException.class)
    public void testCompleteWithMissingProperty() throws IOException {
        ObjectNode node = createNodeWithProperties("prop1");

        ExternalTypeHandler handler = createHandler(node);

        JsonParser parser = new ObjectMapper().getFactory().createParser(node.toString());

        DeserializationContext context = mock(DeserializationContext.class);
        Object bean = new Object();

        handler.complete(parser, context, bean);

        fail("Expected MappingException");
    }

    @org.junit.Test(expected = MappingException.class)
    public void testCompleteWithMissingTypeId() throws IOException {
        ObjectNode node = createNodeWithProperties("prop1", "typeIdProp");

        ExternalTypeHandler handler = createHandler(node);

        JsonParser parser = new ObjectMapper().getFactory().createParser(node.toString());

        DeserializationContext context = mock(DeserializationContext.class);
        Object bean = new Object();

        handler.complete(parser, context, bean);

        fail("Expected MappingException");
    }

    @org.junit.Test
    public void testCompleteWithNaturalType() throws IOException {
        ObjectNode node = createNodeWithProperties("prop1", "typeIdProp");
        node.put("prop1", "value1");

        ExternalTypeHandler handler = createHandler(node);

        JsonParser parser = new ObjectMapper().getFactory().createParser(node.toString());

        DeserializationContext context = mock(DeserializationContext.class);
        Object bean = new Object();

        handler.complete(parser, context, bean);

        assertEquals("value1", handler.getProperty("prop1").get(bean));

        assertTrue(parser.nextToken() == JsonToken.END_OBJECT);
    }

    @org.junit.Test
    public void testCompleteWithCreator() throws IOException {
        ObjectNode node = createNodeWithProperties("creatorProp", "prop1", "prop2");
        node.put("typeIdProp", "typeIdType");

        JsonDeserializer<Creator> creatorDeserializer = mock(JsonDeserializer.class);
        when(creatorDeserializer.createContextual(mock(DeserializationContext.class))).thenReturn(new Creator());

        ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
        builder.addExternal(new SimplePOJOProperty(), creatorDeserializer);
        builder.addExternal(new SimplePOJOProperty("prop1"), null);
        builder.addExternal(new SimplePOJOProperty("prop2"), null);

        ExternalTypeHandler handler = builder.build();

        ObjectNode node2 = createNodeWithProperties("prop1", "prop2", "creatorProp");
        node2.put("typeIdProp", "typeIdType");
        node2.put("prop1", "value1");

        JsonParser parser = new ObjectMapper().getFactory().createParser(node2.toString());

        DeserializationContext context = mock(DeserializationContext.class);
        PropertyValueBuffer buffer = mock(PropertyValueBuffer.class);

        handler.complete(parser, context, buffer, creatorDeserializer);

        assertEquals("value1", handler.getProperty("prop1").get(buffer.getObject()));

        Object creator = creatorDeserializer.deserialize(parser, context);
        assertEquals(creator, buffer.getObject());

        assertTrue(parser.nextToken() == JsonToken.END_OBJECT);
    }
}

class Creator {
}

class SimplePOJOProperty {
    final String name;

    SimplePOJOProperty(String name) {
        this.name = name;
    }

    SimplePOJOProperty() {
        this("name");
    }

    String getName() {
        return name;
    }

    SettableBeanProperty getProperty() {
        return new SimpleSettableBeanProperty(this);
    }
}

class SimpleSettableBeanProperty extends SettableBeanProperty {
    SimplePOJOProperty simpleProperty;

    SimpleSettableBeanProperty(SimplePOJOProperty simpleProperty) {
        super(SimplePOJO.class, simpleProperty.getName());
        this.simpleProperty = simpleProperty;
    }

    @Override
    public Object getValue(Object bean) {
        return simpleProperty.getValue(bean);
    }

    @Override
    public void setValue(Object bean, Object value) {
        simpleProperty.setValue(bean, value);
    }

    Object getValue(Object bean) {
        return simpleProperty.getValue(bean);
    }

    void setValue(Object bean, Object value) {
        simpleProperty.setValue(bean, value);
    }
}

class SimplePOJO {
}