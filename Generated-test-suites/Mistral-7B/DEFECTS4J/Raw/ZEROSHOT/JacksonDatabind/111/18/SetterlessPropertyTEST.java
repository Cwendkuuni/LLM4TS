package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SetterlessPropertyTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @org.junit.Test
    public void testDeserializeAndSet() throws Exception {
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, createTestMethod());
        Object testInstance = new Object();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser = objectMapper.getFactory().createParser("[]");

        property.deserializeAndSet(jsonParser, objectMapper.getDeserializationContext(), testInstance);

        // Verify that the getter method was called and the value was set correctly
        Object value = property.getGetter().invoke(testInstance, new Object[0]);
        assertNotNull(value);
        assertTrue(value instanceof List);

        // Test deserializing a null value
        jsonParser.nextToken(); // consume START_ARRAY token
        jsonParser.nextNull(); // consume null value
        property.deserializeAndSet(jsonParser, objectMapper.getDeserializationContext(), testInstance);
        value = property.getGetter().invoke(testInstance, new Object[0]);
        assertNull(value);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testSet() throws Exception {
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, createTestMethod());
        Object testInstance = new Object();

        property.set(testInstance, new Object());
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturn() throws Exception {
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, createTestMethod());
        Object testInstance = new Object();

        property.setAndReturn(testInstance, new Object());
    }

    @org.junit.Test
    public void testGetAnnotation() throws Exception {
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, createTestMethod());

        assertEquals(MyAnnotation.class, property.getAnnotation(MyAnnotation.class));
        assertEquals(null, property.getAnnotation(UnknownAnnotation.class));
    }

    @org.junit.Test
    public void testFixAccess() throws Exception {
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, createTestMethod());

        property.fixAccess(mapper.getDeserializationConfig());
        AnnotatedMethod annotatedMethod = property.getAnnotated();
        assertEquals(Modifier.PUBLIC, annotatedMethod.getModifiers());
    }

    @org.junit.Test
    public void testDeserializeSetAndReturn() throws Exception {
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, createTestMethod());
        Object testInstance = new Object();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser = objectMapper.getFactory().createParser("[]");

        Object result = property.deserializeSetAndReturn(jsonParser, objectMapper.getDeserializationContext(), testInstance);
        assertEquals(testInstance, result);
    }

    private Method createTestMethod() throws NoSuchMethodException, IllegalAccessException {
        Class<?> testClass = this.getClass();
        return testClass.getMethod("testGetMethod", Object[].class);
    }

    private static class MyAnnotation extends JacksonAnnotation {
    }

    private static class UnknownAnnotation extends JacksonAnnotation {
    }

    private static class TestClass {
        private List<Integer> list;

        public List<Integer> getList() {
            return list;
        }

        public void setList(List<Integer> list) {
            this.list = list;
        }

        @MyAnnotation
        public List<Integer> testGetMethod(List<Integer> list) {
            return list;
        }
    }
}