package com.example.junit.test.objectidinfo;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.reflections.Reflections;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.databind.PropertyName;

@RunWith(JUnit4.class)
public class ObjectIdInfoTest {
    private static final String TEST_PACKAGE = "com.fasterxml.jackson.databind.introspect.";

    @Test
    public void testObjectIdInfoConstructor() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<String> scope = String.class;
        Class<MyObjectIdGenerator> generator = MyObjectIdGenerator.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator);
        assertNotNull(objectIdInfo);
        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertFalse(objectIdInfo.getAlwaysAsId());

        ObjectIdInfo objectIdInfoWithAlwaysAsId = new ObjectIdInfo(propertyName, scope, generator, true);
        assertNotSame(objectIdInfo, objectIdInfoWithAlwaysAsId);
        assertTrue(objectIdInfoWithAlwaysAsId.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoNullArgs() {
        try {
            new ObjectIdInfo(null, String.class, MyObjectIdGenerator.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            new ObjectIdInfo(new PropertyName(null), String.class, MyObjectIdGenerator.class);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        try {
            new ObjectIdInfo(new PropertyName("testProperty"), null, MyObjectIdGenerator.class);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        try {
            new ObjectIdInfo(new PropertyName("testProperty"), String.class, null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        try {
            new ObjectIdInfo(new PropertyName("testProperty"), String.class, null, true, null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectIdInfoInvalidGenerator() {
        new ObjectIdInfo(new PropertyName("testProperty"), String.class, InvalidObjectIdGenerator.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectIdInfoInvalidResolver() {
        new ObjectIdInfo(new PropertyName("testProperty"), String.class, MyObjectIdGenerator.class, true, InvalidObjectIdResolver.class);
    }

    @Test
    public void testObjectIdInfoWithAlwaysAsId() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<String> scope = String.class;
        Class<MyObjectIdGenerator> generator = MyObjectIdGenerator.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator);
        assertFalse(objectIdInfo.getAlwaysAsId());

        ObjectIdInfo objectIdInfoWithAlwaysAsId = objectIdInfo.withAlwaysAsId(true);
        assertTrue(objectIdInfoWithAlwaysAsId.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoToString() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<String> scope = String.class;
        Class<MyObjectIdGenerator> generator = MyObjectIdGenerator.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator);
        String actual = objectIdInfo.toString();
        String expected = "ObjectIdInfo: propName=testProperty, scope=class java.lang.String, generatorType=class com.example.MyObjectIdGenerator, alwaysAsId=false";
        assertEquals(expected, actual);
    }

    private static class MyObjectIdGenerator implements ObjectIdGenerator<Object> {
        @Override
        public Object generateId(SerializerProvider serializerProvider, BeanProperty beanProperty) {
            return null;
        }
    }

    private static class MyObjectIdResolver implements ObjectIdResolver {
        @Override
        public ObjectId idFromString(String id) {
            return null;
        }

        @Override
        public String idToString(ObjectId id) {
            return null;
        }
    }

    private static class InvalidObjectIdGenerator implements ObjectIdGenerator<Object> {
        @Override
        public Object generateId(SerializerProvider serializerProvider, BeanProperty beanProperty) {
            throw new RuntimeException("Invalid generator");
        }
    }

    private static class InvalidObjectIdResolver implements ObjectIdResolver {
        @Override
        public ObjectId idFromString(String id) {
            throw new RuntimeException("Invalid resolver");
        }

        @Override
        public String idToString(ObjectId id) {
            throw new RuntimeException("Invalid resolver");
        }
    }

    @Test
    public void testReflectionUtils() {
        Reflections reflections = new Reflections(TEST_PACKAGE);
        Class<?> objectIdInfoClass = reflections.get(ObjectIdInfo.class);

        assertEquals(ObjectIdInfo.class, objectIdInfoClass);
    }
}