package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer.None;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

/**
 * Test cases for ExternalTypeHandler class
 */
public class ExternalTypeHandlerTest {

    private ExternalTypeHandler handler;
    private ObjectReader reader;

    @Before
    public void setup() throws IOException {
        JsonFactory factory = new JsonFactory();
        DeserializationContext context = new DeserializationContext(factory);
        TypeDeserializer typeDeserializer = new None.Instance();
        handler = new ExternalTypeHandler.Builder()
                .addExternal(new PropertyName("name"), typeDeserializer)
                .addExternal(new PropertyName("type"), typeDeserializer)
                .build();
        reader = factory.readValueAs(Object[].class, context.getFactory().getValueTypeDeserializer());
    }

    @Test
    public void testHandleTypePropertyValue() throws IOException, JsonProcessingException {
        String json = "{\"name\":\"John\",\"type\":\"Person\"}";
        JsonParser parser = handler.start().handleParser(new JsonFactory().createParser(json));
        Object result = reader.readValue(parser);
        assertArrayEquals(new Object[]{"John", "Person"}, result);

        parser = handler.start().handleParser(new JsonFactory().createParser(json.replace("Person", "Animal")));
        result = reader.readValue(parser);
        assertArrayEquals(new Object[]{"John", null}, result);

        parser = handler.start().handleParser(new JsonFactory().createParser(json.replace("Person", "")));
        result = reader.readValue(parser);
        assertArrayEquals(new Object[]{"John", null}, result);

        parser = handler.start().handleParser(new JsonFactory().createParser(json.replace("name", "type")));
        result = reader.readValue(parser);
        assertArrayEquals(new Object[]{null, "Person"}, result);

        parser = handler.start().handleParser(new JsonFactory().createParser(json.replace("name", "")));
        result = reader.readValue(parser);
        assertArrayEquals(new Object[]{null, null}, result);
    }

    @Test
    public void testHandlePropertyValue() throws IOException, JsonProcessingException {
        String json = "{\"name\":\"John\",\"type\":\"Person\"}";
        JsonParser parser = handler.start().handleParser(new JsonFactory().createParser(json));
        parser.nextToken(); // skip "[{"

        Object result = handler.handlePropertyValue(parser, null, "name", null);
        assertEquals("name", result);

        result = handler.handlePropertyValue(parser, null, "type", null);
        assertEquals("type", result);

        parser.nextToken(); // skip "]"
        parser.nextToken(); // skip ":"
        parser.nextToken(); // skip "Person"

        result = handler.handlePropertyValue(parser, null, "name", new Object());
        assertEquals("John", result);

        result = handler.handlePropertyValue(parser, null, "type", new Object());
        assertEquals("Person", result);

        parser.nextToken(); // skip ","

        result = handler.handlePropertyValue(parser, null, "name", null);
        assertNull(result);

        result = handler.handlePropertyValue(parser, null, "type", null);
        assertNull(result);
    }

    @Test(expected = IOException.class)
    public void testCompleteMissingProperty() throws IOException {
        String json = "{\"type\":\"Person\"}";
        JsonParser parser = handler.start().handleParser(new JsonFactory().createParser(json));
        handler.complete(parser, null, null);
    }

    @Test(expected = IOException.class)
    public void testCompleteMissingTypeId() throws IOException {
        String json = "{\"name\":\"John\"}";
        JsonParser parser = handler.start().handleParser(new JsonFactory().createParser(json));
        handler.complete(parser, null, null);
    }

    @Test(expected = IOException.class)
    public void testCompleteMissingToken() throws IOException {
        String json = "{\"name\":\"John\",\"type\":\"Person\"}";
        JsonParser parser = handler.start().handleParser(new JsonFactory().createParser(json));
        parser.nextToken(); // skip "[{"
        parser.nextToken(); // skip "name":
        parser.nextToken(); // skip "John"
        parser.nextToken(); // skip ","
        parser.nextToken(); // skip "type":
        parser.nextToken(); // skip "Person"
        parser.nextToken(); // skip "]"
        parser.nextToken(); // skip ":"
        parser.nextToken(); // skip "}"
        handler.complete(parser, null, null);
    }

    @Test
    public void testComplete() throws IOException, JsonProcessingException {
        String json = "{\"name\":\"John\",\"type\":\"Person\"}";
        JsonParser parser = handler.start().handleParser(new JsonFactory().createParser(json));
        Object result = handler.complete(parser, null, null);
        assertArrayEquals(new Object[]{"John", "Person"}, result);
    }

    @Test
    public void testCompleteWithCreator() throws IOException, JsonProcessingException {
        JsonDeserializer<String> stringDeserializer = new JsonDeserializer<String>() {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt)
                    throws IOException, JsonProcessingException {
                String value = p.getValueAsString();
                return value.toUpperCase();
            }
        };
        String json = "[{\"name\":\"John\",\"type\":\"Person\",\"typeId\":\"PersonType\"}, {\"name\":\"Jane\",\"type\":\"Person\",\"typeId\":\"PersonType\"}]";

        JsonParser parser = handler.start().handleParser(new JsonFactory().createParser(json));
        Object result = handler.complete(parser, null, null, new PropertyValueBuffer(), stringDeserializer);
        assertArrayEquals(new Object[]{"John", "PersonType"}, result[0]);
        assertArrayEquals(new Object[]{"Jane", "PersonType"}, result[1]);
    }
}