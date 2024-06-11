package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

/**
 * Test cases for ExternalTypeHandler class
 */
public class ExternalTypeHandlerTest {

    private JsonParser jsonParser;
    private DeserializationContext context;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() throws IOException {
        objectMapper = new ObjectMapper();
        context = objectMapper.getDeserializationContext();
        jsonParser = objectMapper.getFactory().createParser("[" +
                "{\"name\":\"John\",\"type\":\"Person\"}," +
                "{\"name\":\"Jane\",\"type\":\"Animal\",\"animalType\":\"Cat\"}," +
                "{\"name\":\"Jack\",\"type\":\"Person\"}" +
                "]");
    }

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(
                new ExternalTypeHandler.Builder()
                        .addExternal(new TestProperty("name", Person.class), new PersonTypeDeserializer())
                        .addExternal(new TestProperty("type", String.class), new StringDeserializer())
                        .addExternal(new TestProperty("animalType", Animal.class), new AnimalTypeDeserializer())
                        .build()
        );

        handler.start();

        boolean handled = handler.handleTypePropertyValue(jsonParser, context, "type", null);
        Assert.assertTrue(handled);
        Assert.assertEquals("Person", handler._typeIds[0]);
        Assert.assertNull(handler._tokens[0]);

        handled = handler.handleTypePropertyValue(jsonParser, context, "animalType", null);
        Assert.assertTrue(handled);
        Assert.assertEquals("Cat", handler._typeIds[1]);
        Assert.assertNotNull(handler._tokens[1]);

        handler.complete(jsonParser, context, null);
        Assert.assertNull(handler._typeIds);
        Assert.assertNull(handler._tokens);
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(
                new ExternalTypeHandler.Builder()
                        .addExternal(new TestProperty("name", String.class), null)
                        .addExternal(new TestProperty("type", String.class), null)
                        .addExternal(new TestProperty("animalType", String.class), null)
                        .build()
        );

        handler.start();

        boolean handled = handler.handlePropertyValue(jsonParser, context, "name", null);
        Assert.assertTrue(handled);
        Assert.assertNull(handler._typeIds);
        Assert.assertNotNull(handler._tokens[0]);

        handler.complete(jsonParser, context, null);
        Assert.assertNull(handler._typeIds);
        Assert.assertNull(handler._tokens);
    }

    @Test
    public void testHandleTypePropertyValueMissingProperty() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(
                new ExternalTypeHandler.Builder()
                        .addExternal(new TestProperty("name", Person.class), new PersonTypeDeserializer())
                        .addExternal(new TestProperty("type", String.class), new StringDeserializer())
                        .addExternal(new TestProperty("unknown", String.class), null)
                        .build()
        );

        handler.start();

        boolean handled = handler.handleTypePropertyValue(jsonParser, context, "unknown", null);
        Assert.assertFalse(handled);

        handler.complete(jsonParser, context, null);
        Assert.assertNull(handler._typeIds);
        Assert.assertNull(handler._tokens);
    }

    @Test
    public void testHandlePropertyValueMissingType() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(
                new ExternalTypeHandler.Builder()
                        .addExternal(new TestProperty("name", String.class), null)
                        .addExternal(new TestProperty("type", String.class), null)
                        .addExternal(new TestProperty("animalType", String.class), null)
                        .addExternal(new TestProperty("unknown", String.class), null)
                        .build()
        );

        handler.start();

        boolean handled = handler.handlePropertyValue(jsonParser, context, "name", null);
        Assert.assertTrue(handled);
        Assert.assertNull(handler._typeIds);
        Assert.assertNotNull(handler._tokens[0]);

        handled = handler.handlePropertyValue(jsonParser, context, "unknown", null);
        Assert.assertFalse(handled);

        handler.complete(jsonParser, context, null);
        Assert.assertNull(handler._typeIds);
        Assert.assertNull(handler._tokens);
    }

    @Test
    public void testHandlePropertyValueMissingBoth() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(
                new ExternalTypeHandler.Builder()
                        .addExternal(new TestProperty("name", String.class), null)
                        .addExternal(new TestProperty("type", String.class), null)
                        .addExternal(new TestProperty("animalType", String.class), null)
                        .build()
        );

        handler.start();

        boolean handled = handler.handlePropertyValue(jsonParser, context, "name", null);
        Assert.assertTrue(handled);
        Assert.assertNull(handler._typeIds);
        Assert.assertNotNull(handler._tokens[0]);

        handled = handler.handlePropertyValue(jsonParser, context, "type", null);
        Assert.assertFalse(handled);

        handled = handler.handlePropertyValue(jsonParser, context, "animalType", null);
        Assert.assertFalse(handled);

        handler.complete(jsonParser, context, null);
        Assert.assertNull(handler._typeIds);
        Assert.assertNull(handler._tokens);
    }

    @Test
    public void testCompleteWithNulls() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(
                new ExternalTypeHandler.Builder()
                        .addExternal(new TestProperty("name", String.class), null)
                        .addExternal(new TestProperty("type", String.class), null)
                        .addExternal(new TestProperty("animalType", String.class), null)
                        .build()
        );

        handler.start();

        handler.handlePropertyValue(jsonParser, context, "name", null);
        handler.handlePropertyValue(jsonParser, context, "type", null);
        handler.handlePropertyValue(jsonParser, context, "animalType", null);

        Object result = handler.complete(jsonParser, context, null);
        Assert.assertNotNull(result);

        handler.complete(jsonParser, context, null);
        Assert.assertNull(handler._typeIds);
        Assert.assertNull(handler._tokens);
    }

    @Test
    public void testCompleteWithInvalidTypeId() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(
                new ExternalTypeHandler.Builder()
                        .addExternal(new TestProperty("name", String.class), null)
                        .addExternal(new TestProperty("type", String.class), null)
                        .addExternal(new TestProperty("animalType", String.class), null)
                        .build()
        );

        handler.start();

        handler.handlePropertyValue(jsonParser, context, "name", null);
        handler.handlePropertyValue(jsonParser, context, "type", null);
        handler.handlePropertyValue(jsonParser, context, "animalType", null);

        handler._typeIds[2] = "InvalidTypeId";
        Object result = handler.complete(jsonParser, context, null);
        Assert.assertTrue(result instanceof MappingException);

        handler.complete(jsonParser, context, null);
        Assert.assertNull(handler._typeIds);
        Assert.assertNull(handler._tokens);
    }

    @Test
    public void testCompleteWithEmptyArray() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(
                new ExternalTypeHandler.Builder()
                        .addExternal(new TestProperty("name", String.class), null)
                        .addExternal(new TestProperty("type", String.class), null)
                        .addExternal(new TestProperty("animalType", String.class), null)
                        .build()
        );

        handler.start();

        handler.handlePropertyValue(jsonParser, context, "name", null);
        handler.handlePropertyValue(jsonParser, context, "type", null);
        handler.handlePropertyValue(jsonParser, context, "animalType", null);

        JsonParser emptyArrayParser = objectMapper.getFactory().createParser("[]");
        Object result = handler.complete(emptyArrayParser, context, null);
        Assert.assertNotNull(result);

        handler.complete(jsonParser, context, null);
        Assert.assertNull(handler._typeIds);
        Assert.assertNull(handler._tokens);
    }

    @Test
    public void testCompleteWithCreator() throws IOException {
        TestCreator creator = new TestCreator();

        ExternalTypeHandler handler = new ExternalTypeHandler(
                new ExternalTypeHandler.Builder()
                        .addExternal(new TestProperty("name", String.class), null)
                        .addExternal(new TestProperty("type", String.class), null)
                        .addExternal(new TestProperty("animalType", String.class), null)
                        .build()
        );

        handler.start();

        handler.handlePropertyValue(jsonParser, context, "name", null);
        handler.handlePropertyValue(jsonParser, context, "type", null);
        handler.handlePropertyValue(jsonParser, context, "animalType", null);

        handler.handlePropertyValue(jsonParser, context, "animalType", null);
        handler.handlePropertyValue(jsonParser, context, "animalType", null);

        Object result = handler.complete(jsonParser, context, null, creator);
        Assert.assertNotNull(result);
        Assert.assertEquals(3, result.getClass().getDeclaredFields().length);

        handler.complete(jsonParser, context, null, creator);
        Assert.assertNull(handler._typeIds);
        Assert.assertNull(handler._tokens);
    }

    private static class TestProperty {
        private final String name;
        private final Class<?> clazz;

        public TestProperty(String name, Class<?> clazz) {
            this.name = name;
            this.clazz = clazz;
        }
    }

    private static class Person {
    }

    private static class PersonTypeDeserializer extends StdTypeDeserializer {
        public PersonTypeDeserializer() {
            this(null);
        }

        public PersonTypeDeserializer(Class<?> vc) {
            super(Person.class, vc);
        }

        @Override
        public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer vd)
                throws IOException {
            return new Person();
        }
    }

    private static class StringDeserializer extends StdScalarDeserializer {
        public StringDeserializer() {
            this(null);
        }

        public StringDeserializer(Class<?> vc) {
            super(String.class, vc);
        }

        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return p.getValueAsString();
        }
    }

    private static class Animal {
    }

    private static class AnimalTypeDeserializer extends StdTypeDeserializer {
        public AnimalTypeDeserializer() {
            this(null);
        }

        public AnimalTypeDeserializer(Class<?> vc) {
            super(Animal.class, vc);
        }

        @Override
        public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer vd)
                throws IOException {
            return new Animal();
        }
    }

    private static class TestCreator extends BaseJsonDeserializer<Object> {
        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            Map<String, Object> properties = new HashMap<>();
            while (p.nextToken() != null && !p.isExpectedStartArrayToken()) {
                properties.put(p.getCurrentName(), p.getValueAsString());
                p.nextToken();
            }
            p.nextToken();
            Object result = new Object[3];
            for (int i = 0; i < 3; ++i) {
                result[i] = properties.get(String.valueOf(i));
            }
            return result;
        }
    }
}