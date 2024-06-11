package com.example.junit.tests;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;

import org.junit.Test;

/**
 * Test class for ObjectIdInfo
 */
public class ObjectIdInfoTest {

    @Test
    public void testConstructorWithPropertyNameAndScopeAndGenerator() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<String> scope = String.class;
        Class<CustomObjectIdGenerator> generator = CustomObjectIdGenerator.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator);
        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testConstructorWithPropertyNameAndScope() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<String> scope = String.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope);
        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertNull(objectIdInfo.getGeneratorType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testConstructorWithNameAndScopeAndGenerator() {
        String name = "testProperty";
        Class<String> scope = String.class;
        Class<CustomObjectIdGenerator> generator = CustomObjectIdGenerator.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, scope, generator);
        PropertyName propertyName = new PropertyName(name);
        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullName() {
        Class<String> scope = String.class;
        Class<CustomObjectIdGenerator> generator = CustomObjectIdGenerator.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, scope, generator);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullScope() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<CustomObjectIdGenerator> generator = CustomObjectIdGenerator.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, null);
    }

    @Test
    public void testWithAlwaysAsId() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<String> scope = String.class;
        Class<CustomObjectIdGenerator> generator = CustomObjectIdGenerator.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator);
        ObjectIdInfo newObjectIdInfo = objectIdInfo.withAlwaysAsId(true);
        assertTrue(newObjectIdInfo.getAlwaysAsId());
        assertNotSame(objectIdInfo, newObjectIdInfo);
    }

    @Test
    public void testToString() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<String> scope = String.class;
        Class<CustomObjectIdGenerator> generator = CustomObjectIdGenerator.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator);
        String expectedToString = "ObjectIdInfo: propName=testProperty, scope=class java.lang.String, generatorType=class com.example.CustomObjectIdGenerator, alwaysAsId=false";
        assertEquals(expectedToString, objectIdInfo.toString());
    }

    private static class CustomObjectIdGenerator {}

}