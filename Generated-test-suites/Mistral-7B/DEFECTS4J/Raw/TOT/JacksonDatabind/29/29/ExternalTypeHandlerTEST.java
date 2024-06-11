package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.PropertyValueBuffer;
import com.fasterxml.jackson.databind.json.JsonFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class ExternalTypeHandlerTest {

    private ObjectMapper mapper;
    private ExternalTypeHandler handler;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
        handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(new PropertyName("prop1"), new MyTypeDeserializer())
                .addExternal(new PropertyName("prop2"), new OtherTypeDeserializer())
                .build());
    }

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        String json = "{\"typeId\":\"MyType\", \"prop1\":\"value1\"}";
        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser parser = jsonFactory.createParser(json);
        DeserializationContext context = mapper.getDeserializationContext();
        Object bean = new Object();

        handler.handleTypePropertyValue(parser, context, "prop1", bean);

        assertEquals("value1", handler.getProperty("prop1").get(bean));
        assertTrue(parser.nextToken() == CompositeStreamParser.Null);
    }

    @Test
    public void testHandlePropertyValue_ScalarValue() throws IOException {
        String json = "{\"prop1\":1}";
        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser parser = jsonFactory.createParser(json);
        DeserializationContext context = mapper.getDeserializationContext();
        Object bean = new Object();

        handler.handlePropertyValue(parser, context, "prop1", bean);

        assertEquals(1, handler.getProperty("prop1").get(bean));
        assertTrue(parser.nextToken() == CompositeStreamParser.Null);
    }

    @Test
    public void testHandlePropertyValue_ComplexValue() throws IOException {
        String json = "{\"prop1\":[1,2,3],\"prop2\":{\"id\":1,\"name\":\"John\"}}";
        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser parser = jsonFactory.createParser(json);
        DeserializationContext context = mapper.getDeserializationContext();
        Object bean = new Object();

        handler.handlePropertyValue(parser, context, "prop1", bean);
        handler.handlePropertyValue(parser, context, "prop2", bean);

        Object prop1 = handler.getProperty("prop1").get(bean);
        List<Object> list = mapper.readValue(parser, TypeFactory.defaultInstance().constructCollectionType(List.class, Number.class));
        assertEquals(3, list.size());
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(3, list.get(2));

        Object prop2 = handler.getProperty("prop2").get(bean);
        Map<String, Object> map = mapper.readValue(parser, TypeFactory.defaultInstance().constructMapType(Map.class, PropertyName.STANDARD_JOBNAME, Number.class));
        assertEquals(1, map.get("id"));
        assertEquals("John", map.get("name"));

        assertTrue(parser.nextToken() == CompositeStreamParser.Null);
    }

    @Test(expected = JsonMappingException.class)
    public void testMissingTypeIdProperty() throws IOException {
        String json = "{\"prop1\":1}";
        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser parser = jsonFactory.createParser(json);
        DeserializationContext context = mapper.getDeserializationContext();
        Object bean = new Object();

        handler.handlePropertyValue(parser, context, "prop1", bean);

        String missingTypeIdProperty = "missingTypeIdProperty";
        handler.handlePropertyValue(parser, context, missingTypeIdProperty, bean);

        fail("Expected JsonMappingException: Missing property 'missingTypeIdProperty' for external type id 'MyType'");
    }

    @Test(expected = JsonMappingException.class)
    public void testMissingPropertyForExternalTypeId() throws IOException {
        String json = "{\"typeId\":\"MyType\"}";
        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser parser = jsonFactory.createParser(json);
        DeserializationContext context = mapper.getDeserializationContext();
        Object bean = new Object();

        handler.handleTypePropertyValue(parser, context, "typeId", bean);

        String missingProperty = "missingProperty";
        handler.handlePropertyValue(parser, context, missingProperty, bean);

        fail("Expected JsonMappingException: Missing property 'missingProperty' for external type id 'MyType'");
    }

    @Test
    public void testComplete() throws IOException {
        String json = "{\"typeId\":\"MyType\", \"prop1\":1, \"prop2\":{\"id\":1,\"name\":\"John\"}}";
        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser parser = jsonFactory.createParser(json);
        DeserializationContext context = mapper.getDeserializationContext();
        Object bean = new Object();

        handler.complete(parser, context, bean);

        Object prop1 = handler.getProperty("prop1").get(bean);
        assertEquals(1, prop1);

        Object prop2 = handler.getProperty("prop2").get(bean);
        Map<String, Object> map = mapper.readValue(parser, TypeFactory.defaultInstance().constructMapType(Map.class, PropertyName.STANDARD_JOBNAME, Number.class));
        assertEquals(1, map.get("id"));
        assertEquals("John", map.get("name"));

        assertTrue(parser.nextToken() == CompositeStreamParser.Null);
    }

    @Test
    public void testComplete_PropertyValueBuffer() throws IOException {
        String json = "{\"typeId\":\"MyType\", \"prop1\":1, \"prop2\":{\"id\":1,\"name\":\"John\"}}";
        JsonFactory jsonFactory = mapper.getFactory();
        PropertyValueBuffer buffer = new PropertyValueBuffer();
        PropertyBasedCreator creator = new PropertyBasedCreator() {
            @Override
            public Object build(DeserializationContext ctxt, PropertyValueBuffer pvb) throws IOException {
                return new Object();
            }
        };

        handler.complete(jsonFactory.createParser(json), context, buffer, creator);
        Object bean = creator.build(context, buffer);

        Object prop1 = handler.getProperty("prop1").get(bean);
        assertEquals(1, prop1);

        Object prop2 = handler.getProperty("prop2").get(bean);
        Map<String, Object> map = mapper.readValue(parser, TypeFactory.defaultInstance().constructMapType(Map.class, PropertyName.STANDARD_JOBNAME, Number.class));
        assertEquals(1, map.get("id"));
        assertEquals("John", map.get("name"));

        assertTrue(parser.nextToken() == CompositeStreamParser.Null);
    }
}

class MyTypeDeserializer extends TypeDeserializer {
    @Override
    public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return 1;
    }
}

class OtherTypeDeserializer extends TypeDeserializer {
    @Override
    public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return mapper.readTree(p);
    }
}

class PropertyBasedCreator {
    // Empty class for testing
}