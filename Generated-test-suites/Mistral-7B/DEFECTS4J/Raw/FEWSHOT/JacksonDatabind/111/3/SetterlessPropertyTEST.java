package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.SetterlessProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SetterlessPropertyTest {

    private static final Object testInstance = new Object() {
        private Object testProperty;
    };

    private static final Method testGetMethod;

    static {
        try {
            testGetMethod = testInstance.getClass().getMethod("getTestProperty");
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Test property getter not found", e);
        }
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        ObjectType testType = ObjectType.constructUnsafe(testInstance.getClass());
        SetterlessProperty property = new SetterlessProperty(
                new BeanPropertyDefinition(
                        "testProperty",
                        testType,
                        null,
                        null,
                        null,
                        new AnnotatedMethod(testGetMethod, testInstance.getClass())),
                testType,
                null,
                Annotations.empty(),
                new AnnotatedMethod(testGetMethod, testInstance.getClass()));

        JsonParser parser = createParser("{\"testProperty\": [1, 2, 3]}");
        property.deserializeAndSet(parser, new DeserializationContext(null, null), testInstance);

        Object testProperty = testInstance.getClass().getMethod("getTestProperty").invoke(testInstance);
        assertEquals(Arrays.asList(1, 2, 3), testProperty);
    }

    @Test
    public void testGetAnnotation() {
        SetterlessProperty property = new SetterlessProperty(
                new BeanPropertyDefinition("testProperty", null, null, null, null, null),
                ObjectType.constructUnsafe(Object.class),
                null,
                Annotations.empty(),
                new AnnotatedMethod(testGetMethod, testInstance.getClass()));

        assertNull(property.getAnnotation(MyAnnotation.class));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() throws Exception {
        SetterlessProperty property = new SetterlessProperty(
                new BeanPropertyDefinition("testProperty", null, null, null, null, null),
                ObjectType.constructUnsafe(Object.class),
                null,
                Annotations.empty(),
                new AnnotatedMethod(testGetMethod, testInstance.getClass()));

        property.set(testInstance, new Object());
    }

    @Test
    public void testSetterlessPropertyWithValueDeserializer() throws Exception {
        ObjectType listType = ObjectType.constructUnsafe(List.class);
        JsonDeserializer<List> listDeserializer = new ArrayDeserializer(List.class);

        SetterlessProperty property = new SetterlessProperty(
                new BeanPropertyDefinition(
                        "testProperty",
                        listType,
                        null,
                        null,
                        null,
                        new AnnotatedMethod(testGetMethod, testInstance.getClass())),
                listType,
                listDeserializer,
                Annotations.empty(),
                new AnnotatedMethod(testGetMethod, testInstance.getClass()));

        JsonParser parser = createParser("{\"testProperty\": [1, 2, 3]}");
        TestDeserializer deserializer = new TestDeserializer();
        property.setValueDeserializer(deserializer);
        property.deserializeAndSet(parser, new DeserializationContext(null, null), testInstance);

        Object testProperty = testInstance.getClass().getMethod("getTestProperty").invoke(testInstance);
        assertEquals(Arrays.asList(deserializer.deserializes, deserializer.deserializes2), testProperty);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetterlessPropertyWithNullProvider() throws Exception {
        SetterlessProperty property = new SetterlessProperty(
                new BeanPropertyDefinition("testProperty", null, null, null, null, null),
                ObjectType.constructUnsafe(Object.class),
                null,
                new NullValueProvider.None(),
                new AnnotatedMethod(testGetMethod, testInstance.getClass()));

        property.set(testInstance, null);
    }

    @Test
    public void testFixAccess() throws Exception {
        SetterlessProperty property = new SetterlessProperty(
                new BeanPropertyDefinition("testProperty", null, null, null, null, null),
                ObjectType.constructUnsafe(Object.class),
                null,
                Annotations.empty(),
                new AnnotatedMethod(testGetMethod, testInstance.getClass()));

        property.fixAccess(new DeserializationConfig.Builder().build());
        assertTrue(testGetMethod.isAccessible());
    }

    private JsonParser createParser(String content) throws IOException {
        JsonFactory factory = new JsonFactory();
        return factory.createParser(new StringReader(content));
    }

    private static class MyAnnotation {}

    private static class TestDeserializer extends StdDeserializer<List<Object>> {
        Object deserializes = new Object();
        Object deserializes2 = new Object();

        @Override
        public List<Object> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            deserializes = ctxt.readValueAs(Object.class);
            deserializes2 = ctxt.readValueAs(Object.class);
            return Arrays.asList(deserializes, deserializes2);
        }
    }
}