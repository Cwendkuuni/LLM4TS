package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

/**
 * Test class for ExternalTypeHandler.
 */
public class ExternalTypeHandlerTest {

    private ExternalTypeHandler handler;
    private JsonParser parser;
    private ObjectNode node;

    @Before
    public void setup() throws IOException {
        parser = new ObjectMapper().getFactory().createParser("{\"name\": \"John\", \"age\": 25, \"type\": \"Person\"}");
        handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(new SettableBeanProperty("name", String.class), null)
                .addExternal(new SettableBeanProperty("age", Number.class, NumberDeserializers.INTEGER), NumberDeserializers.create0_1IntDeserializer(parser.getConfig()))
                .addExternal(new SettableBeanProperty("type", String.class), StringDeserializer.instance)
                .build());
    }

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        handler.handleTypePropertyValue(parser, new TestContext(), "type", null);
        Assert.assertNotNull(handler._typeIds);
        Assert.assertEquals("Person", handler._typeIds[0]);
    }

    @Test
    public void testHandleTypePropertyValueWithBean() throws IOException {
        Object node = new ObjectMapper().readValue(parser, Map.class);
        handler.handleTypePropertyValue(parser, new TestContext(), "type", node);
        Assert.assertNull(handler._typeIds);
        Assert.assertNotNull(handler._tokens[0]);
        TokenBuffer tokenBuffer = handler._tokens[0];
        Assert.assertEquals(JsonToken.START_OBJECT, tokenBuffer.currentToken().asInt());
        JsonParser parsedTokenBuffer = tokenBuffer.asParser(parser);
        Object type = StringDeserializer.instance.deserialize(parsedTokenBuffer, new TestContext(), null);
        Assert.assertEquals("Person", type);
    }

    @Test(expected = IOException.class)
    public void testHandleTypePropertyValueWithMissingTypePropertyName() throws IOException {
        handler.handleTypePropertyValue(parser, new TestContext(), "unknown", null);
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        handler.handlePropertyValue(parser, new TestContext(), "name", null);
        Assert.assertEquals("John", handler._properties[0].getProperty().get(null));

        node = JsonMapping.nodeFactory.objectNode();
        node.put("name", "Mike");
        handler.handlePropertyValue(parser, new TestContext(), "name", node);
        Assert.assertEquals("Mike", handler._properties[0].getProperty().get(null));
    }

    @Test(expected = IOException.class)
    public void testHandlePropertyValueWithMissingPropertyName() throws IOException {
        handler.handlePropertyValue(parser, new TestContext(), "unknown", null);
    }

    @Test
    public void testComplete() throws IOException {
        handler.complete(parser, new TestContext(), null);
        Object result = parser.readValueAs(Person.class);
        Assert.assertEquals("John", result.getClass().getField("name").get(result));
        Assert.assertEquals(25, result.getClass().getField("age").get(result));
        Assert.assertEquals("Person", result.getClass().getField("type").get(result));
    }

    @Test
    public void testCompleteWithPropertyValueBuffer() throws IOException {
        PropertyValueBuffer buffer = new PropertyValueBuffer();
        handler.complete(parser, new TestContext(), buffer, null);
        Object result = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(buffer.asStartArray());
        Assert.assertEquals("\"name\":\"John\",\"age\":25,\"type\":\"Person\"", result);

        Object parsedResult = new ObjectMapper().readValue(result, Map.class);
        Object node = new ObjectMapper().readValue(parser, Map.class);
        parser.seek(parser.getPointer());
        handler.complete(parser, new TestContext(), buffer, null);
        Object result2 = handler.start().complete(parser, new TestContext(), null);
        Assert.assertEquals(result, result2);
    }

    @Test
    public void testCompleteWithMissingTypeId() throws IOException {
        handler.handlePropertyValue(parser, new TestContext(), "type", null);
        handler.handlePropertyValue(parser, new TestContext(), "name", null);
        handler.handlePropertyValue(parser, new TestContext(), "age", null);
        handler.handlePropertyValue(parser, new TestContext(), "type", null);
        handler.handlePropertyValue(parser, new TestContext(), "age", null);
        handler.handlePropertyValue(parser, new TestContext(), "name", null);
        handler.handlePropertyValue(parser, new TestContext(), "age", null);
        handler.handlePropertyValue(parser, new TestContext(), "name", null);

        handler.complete(parser, new TestContext(), null);
        Object result = parser.readValueAs(Person.class);
        Assert.assertEquals("John", result.getClass().getField("name").get(result));
        Assert.assertEquals(25, result.getClass().getField("age").get(result));
        Assert.assertEquals(null, result.getClass().getField("type").get(result));
    }

    private static class TestContext extends DeserializationContext {

        @Override
        public BeanDescription getBeanDescription() {
            return new BeanDescription();
        }

        @Override
        public Object findValueHolderWithType(Class<?> type) {
            return null;
        }

        @Override
        public void reportTypeMismatch(Class<?> expected, JavaType actual) throws IOException {
            throw new IOException("Type mismatch");
        }

        @Override
        public void reportMappingException(String msg) throws IOException {
            throw new IOException("Mapping exception: " + msg);
        }

        @Override
        public void reportMappingException(String msg, Throwable t) throws IOException {
            throw new IOException("Mapping exception: " + msg, t);
        }

        @Override
        public void handleUnknownProperty(String name, Object beanOrText, JsonParser p) throws IOException {
            throw new IOException("Unknown property: " + name);
        }

        @Override
        public void handleMissingProperty(String name, Object beanOrText, JsonParser p) throws IOException {
            throw new IOException("Missing property: " + name);
        }

        @Override
        public void handleMissingValue(Object beanOrText, String propertyName, JavaType typeHint, JsonParser p) throws IOException {
            throw new IOException("Missing value: " + propertyName);
        }

        @Override
        public void handleMissingCreator(SettableBeanProperty prop, Object beanOrText, JsonParser p) throws IOException {
            throw new IOException("Missing creator: " + prop.getName());
        }
    }

    private static class Person {
        private String name;
        private Integer age;
        private String type;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}