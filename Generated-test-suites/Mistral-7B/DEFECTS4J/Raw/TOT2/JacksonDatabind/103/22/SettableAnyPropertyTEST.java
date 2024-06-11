package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableAnyProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SettableAnyPropertyTest {

    private Object testObject;
    private SettableAnyProperty property;

    @Before
    public void setup() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        this.testObject = new Object();
        this.property = new SettableAnyProperty(
                new BeanProperty(new AnnotatedField(TestClass.class, "testProperty")),
                new AnnotatedField(TestClass.class, "testProperty"),
                JsonTypeFactory.instance.constructSpecializedType(TestClass.class, "map"),
                null,
                null,
                null);
    }

    @Test
    public void testSetValue() throws Exception {
        JsonParser parser = mapper.getFactory().createParser("[]");
        property.deserializeAndSet(parser, mapper.getDeserializationContext(), testObject, "testProperty");
        Map<Object, Object> map = (Map<Object, Object>) property.getProperty().get(testObject);
        assertEquals("expected null value", null, map.get("key1"));
        parser = mapper.getFactory().createParser("{\"key1\":1, \"key2\":2}");
        property.deserializeAndSet(parser, mapper.getDeserializationContext(), testObject, "testProperty");
        map = (Map<Object, Object>) property.getProperty().get(testObject);
        assertEquals("expected value 1 for key1", 1, map.get("key1"));
        assertEquals("expected value 2 for key2", 2, map.get("key2"));
    }

    @Test
    public void testEmptyKeyDeserializer() throws Exception {
        // Create a SettableAnyProperty instance with empty keyDeserializer
        property = new SettableAnyProperty(
                new BeanProperty(new AnnotatedField(TestClass.class, "testProperty")),
                new AnnotatedField(TestClass.class, "testProperty"),
                JsonTypeFactory.instance.constructRawType(Map.class),
                null,
                null,
                null);

        JsonParser parser = mapper.getFactory().createParser("{\"key\":\"value\"}");
        try {
            property.deserializeAndSet(parser, mapper.getDeserializationContext(), testObject, "testProperty");
            fail("Expected IllegalArgumentException, keyDeserializer is null");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testNullValueDeserializer() throws Exception {
        property = new SettableAnyProperty(
                new BeanProperty(new AnnotatedField(TestClass.class, "testProperty")),
                new AnnotatedField(TestClass.class, "testProperty"),
                JsonTypeFactory.instance.constructRawType(Map.class),
                null,
                null,
                null);

        property.withValueDeserializer(null);

        JsonParser parser = mapper.getFactory().createParser("1");
        try {
            property.deserializeAndSet(parser, mapper.getDeserializationContext(), testObject, "testProperty");
            fail("Expected IllegalArgumentException, valueDeserializer is null");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testValueDeserializationFailure() throws Exception {
        JsonParser parser = mapper.getFactory().createParser("invalidJson");
        try {
            property.deserializeAndSet(parser, mapper.getDeserializationContext(), testObject, "testProperty");
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testSetNullValue() throws Exception {
        property.deserializeAndSet(mapper.getFactory().createParser("null"), mapper.getDeserializationContext(), testObject, "testProperty");
        Map<Object, Object> map = (Map<Object, Object>) property.getProperty().get(testObject);
        assertNull("expected null value", map.get("key1"));
    }

    static class TestClass {
        Map<Object, Object> testProperty;
    }
}