package com.example.junit.test.objectidinfo;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.databind.PropertyName;

import org.junit.Test;

public class ObjectIdInfoTest {

    @Test
    public void testObjectIdInfoConstructorWithPropertyNameAndScopeAndGenerator() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<String> scope = String.class;
        Class<MyCustomObjectIdGenerator> generator = MyCustomObjectIdGenerator.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator);

        assertEquals("ObjectIdInfo: propName=testProperty, scope=class java.lang.String, generatorType=class com.example.MyCustomObjectIdGenerator, alwaysAsId=false", objectIdInfo.toString());
        assertNotNull(objectIdInfo.getPropertyName());
        assertNotNull(objectIdInfo.getScope());
        assertNotNull(objectIdInfo.getGeneratorType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoConstructorWithPropertyNameAndScopeAndGeneratorAndAlwaysAsId() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<String> scope = String.class;
        Class<MyCustomObjectIdGenerator> generator = MyCustomObjectIdGenerator.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, true);

        assertEquals("ObjectIdInfo: propName=testProperty, scope=class java.lang.String, generatorType=class com.example.MyCustomObjectIdGenerator, alwaysAsId=true", objectIdInfo.toString());
        assertNotNull(objectIdInfo.getPropertyName());
        assertNotNull(objectIdInfo.getScope());
        assertNotNull(objectIdInfo.getGeneratorType());
        assertTrue(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoConstructorWithStringNameAndScopeAndGenerator() {
        String propertyName = "testProperty";
        Class<String> scope = String.class;
        Class<MyCustomObjectIdGenerator> generator = MyCustomObjectIdGenerator.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator);

        assertEquals("ObjectIdInfo: propName=testProperty, scope=class java.lang.String, generatorType=class com.example.MyCustomObjectIdGenerator, alwaysAsId=false", objectIdInfo.toString());
        assertNotNull(objectIdInfo.getPropertyName());
        assertNotNull(objectIdInfo.getScope());
        assertNotNull(objectIdInfo.getGeneratorType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoConstructorWithStringNameAndScopeAndGeneratorAndAlwaysAsId() {
        String propertyName = "testProperty";
        Class<String> scope = String.class;
        Class<MyCustomObjectIdGenerator> generator = MyCustomObjectIdGenerator.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, true);

        assertEquals("ObjectIdInfo: propName=testProperty, scope=class java.lang.String, generatorType=class com.example.MyCustomObjectIdGenerator, alwaysAsId=true", objectIdInfo.toString());
        assertNotNull(objectIdInfo.getPropertyName());
        assertNotNull(objectIdInfo.getScope());
        assertNotNull(objectIdInfo.getGeneratorType());
        assertTrue(objectIdInfo.getAlwaysAsId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectIdInfoConstructorWithNullPropertyName() {
        Class<String> scope = String.class;
        Class<MyCustomObjectIdGenerator> generator = MyCustomObjectIdGenerator.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, scope, generator);

        fail("Expected IllegalArgumentException but got: " + objectIdInfo);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectIdInfoConstructorWithNullScope() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<MyCustomObjectIdGenerator> generator = MyCustomObjectIdGenerator.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, null, generator);

        fail("Expected IllegalArgumentException but got: " + objectIdInfo);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectIdInfoConstructorWithNullGenerator() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<String> scope = String.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, null);

        fail("Expected IllegalArgumentException but got: " + objectIdInfo);
    }

    @Test
    public void testObjectIdInfoWithAlwaysAsId() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<String> scope = String.class;
        Class<MyCustomObjectIdGenerator> generator = MyCustomObjectIdGenerator.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator);
        ObjectIdInfo withAlwaysAsId = objectIdInfo.withAlwaysAsId(true);

        assertNotSame(objectIdInfo, withAlwaysAsId);
        assertEquals("ObjectIdInfo: propName=testProperty, scope=class java.lang.String, generatorType=class com.example.MyCustomObjectIdGenerator, alwaysAsId=true", withAlwaysAsId.toString());
        assertTrue(withAlwaysAsId.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoGetters() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<String> scope = String.class;
        Class<MyCustomObjectIdGenerator> generator = MyCustomObjectIdGenerator.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, true);

        assertEquals("testProperty", objectIdInfo.getPropertyName().getName());
        assertEquals(String.class, objectIdInfo.getScope());
        assertEquals(MyCustomObjectIdGenerator.class, objectIdInfo.getGeneratorType());
        assertTrue(objectIdInfo.getAlwaysAsId());
    }
}