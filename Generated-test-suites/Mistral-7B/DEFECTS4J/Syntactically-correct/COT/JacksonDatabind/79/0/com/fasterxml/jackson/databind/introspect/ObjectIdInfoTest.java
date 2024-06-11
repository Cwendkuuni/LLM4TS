package com.example.junit.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;

import org.junit.Test;

/**
 * ObjectIdInfoTest class to test methods in ObjectIdInfo class
 */
public class ObjectIdInfoTest {

    @Test
    public void testObjectIdInfoConstructorWithPropertyNameAndScopeAndGenerator() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), TestClass.class, TestGenerator.class);
        assertEquals("ObjectIdInfo: propName=testProperty, scope=class com.example.junit.test.TestClass, generatorType=class com.fasterxml.jackson.databind.introspect.TestGenerator, alwaysAsId=false", objectIdInfo.toString());
    }

    @Test
    public void testObjectIdInfoConstructorWithPropertyNameAndScopeAndGeneratorAndAlwaysAsId() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), TestClass.class, TestGenerator.class, true);
        assertEquals("ObjectIdInfo: propName=testProperty, scope=class com.example.junit.test.TestClass, generatorType=class com.fasterxml.jackson.databind.introspect.TestGenerator, alwaysAsId=true", objectIdInfo.toString());
    }

    @Test
    public void testObjectIdInfoConstructorWithPropertyNameAndScopeAndGeneratorAndResolver() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), TestClass.class, TestGenerator.class, TestResolver.class);
        assertEquals("ObjectIdInfo: propName=testProperty, scope=class com.example.junit.test.TestClass, generatorType=class com.fasterxml.jackson.databind.introspect.TestGenerator, alwaysAsId=false, resolverType=class com.example.junit.test.TestResolver", objectIdInfo.toString());
    }

    @Test
    public void testObjectIdInfoConstructorWithStringAndScopeAndGenerator() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo("testProperty", TestClass.class, TestGenerator.class);
        assertEquals("ObjectIdInfo: propName=testProperty, scope=class com.example.junit.test.TestClass, generatorType=class com.fasterxml.jackson.databind.introspect.TestGenerator, alwaysAsId=false", objectIdInfo.toString());
    }

    @Test
    public void testObjectIdInfoConstructorWithStringAndScopeAndGeneratorAndAlwaysAsId() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo("testProperty", TestClass.class, TestGenerator.class, true);
        assertEquals("ObjectIdInfo: propName=testProperty, scope=class com.example.junit.test.TestClass, generatorType=class com.fasterxml.jackson.databind.introspect.TestGenerator, alwaysAsId=true", objectIdInfo.toString());
    }

    @Test
    public void testObjectIdInfoConstructorWithStringAndScopeAndGeneratorAndResolver() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo("testProperty", TestClass.class, TestGenerator.class, TestResolver.class);
        assertEquals("ObjectIdInfo: propName=testProperty, scope=class com.example.junit.test.TestClass, generatorType=class com.fasterxml.jackson.databind.introspect.TestGenerator, alwaysAsId=false, resolverType=class com.example.junit.test.TestResolver", objectIdInfo.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testObjectIdInfoConstructorWithNullPropertyName() {
        new ObjectIdInfo(null, TestClass.class, TestGenerator.class);
    }

    @Test(expected = NullPointerException.class)
    public void testObjectIdInfoConstructorWithNullScope() {
        new ObjectIdInfo(new PropertyName("testProperty"), null, TestGenerator.class);
    }

    @Test(expected = NullPointerException.class)
    public void testObjectIdInfoConstructorWithNullGenerator() {
        new ObjectIdInfo(new PropertyName("testProperty"), TestClass.class, null);
    }

    @Test(expected = NullPointerException.class)
    public void testObjectIdInfoConstructorWithNullGeneratorAndResolver() {
        new ObjectIdInfo(new PropertyName("testProperty"), TestClass.class, null, TestResolver.class);
    }

    @Test
    public void testObjectIdInfoWithAlwaysAsId() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), TestClass.class, TestGenerator.class);
        ObjectIdInfo objectIdInfoWithAlwaysAsId = objectIdInfo.withAlwaysAsId(true);
        assertEquals(objectIdInfoWithAlwaysAsId.getAlwaysAsId(), true);
    }

    @Test
    public void testObjectIdInfoGetters() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), TestClass.class, TestGenerator.class);
        assertEquals(objectIdInfo.getPropertyName().getName(), "testProperty");
        assertEquals(objectIdInfo.getScope().getName(), "TestClass");
        assertEquals(objectIdInfo.getGeneratorType().getName(), "TestGenerator");
        assertEquals(objectIdInfo.getAlwaysAsId(), false);
    }

}

class TestClass {
}

class TestGenerator implements ObjectIdGenerator<Object> {
}

class TestResolver implements ObjectIdResolver {
}