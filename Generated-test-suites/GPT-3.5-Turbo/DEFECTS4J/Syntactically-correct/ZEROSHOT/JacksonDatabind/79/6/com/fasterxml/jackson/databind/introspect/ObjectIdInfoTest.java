package com.fasterxml.jackson.databind.introspect;

import org.junit.*;
import static org.junit.Assert.*;

public class ObjectIdInfoTest {

    // Test constructor
    @Test
    public void testConstructor() {
        PropertyName name = new PropertyName("testProperty");
        Class<?> scope = TestClass.class;
        Class<?> gen = ObjectIdGenerator.class;
        Class<?> resolver = ObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, scope, gen, resolver);

        assertEquals(name, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(gen, objectIdInfo.getGeneratorType());
        assertEquals(resolver, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    // Test deprecated constructor
    @Test
    public void testDeprecatedConstructor() {
        PropertyName name = new PropertyName("testProperty");
        Class<?> scope = TestClass.class;
        Class<?> gen = ObjectIdGenerator.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, scope, gen);

        assertEquals(name, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(gen, objectIdInfo.getGeneratorType());
        assertNull(objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    // Test deprecated constructor
    @Test
    public void testDeprecatedStringConstructor() {
        String name = "testProperty";
        Class<?> scope = TestClass.class;
        Class<?> gen = ObjectIdGenerator.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, scope, gen);

        assertEquals(new PropertyName(name), objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(gen, objectIdInfo.getGeneratorType());
        assertNull(objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    // Test protected constructor
    @Test
    public void testProtectedConstructor() {
        PropertyName name = new PropertyName("testProperty");
        Class<?> scope = TestClass.class;
        Class<?> gen = ObjectIdGenerator.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, scope, gen, true);

        assertEquals(name, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(gen, objectIdInfo.getGeneratorType());
        assertNull(objectIdInfo.getResolverType());
        assertTrue(objectIdInfo.getAlwaysAsId());
    }

    // Test withAlwaysAsId method
    @Test
    public void testWithAlwaysAsId() {
        PropertyName name = new PropertyName("testProperty");
        Class<?> scope = TestClass.class;
        Class<?> gen = ObjectIdGenerator.class;
        Class<?> resolver = ObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, scope, gen, resolver);
        objectIdInfo = objectIdInfo.withAlwaysAsId(true);

        assertEquals(name, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(gen, objectIdInfo.getGeneratorType());
        assertEquals(resolver, objectIdInfo.getResolverType());
        assertTrue(objectIdInfo.getAlwaysAsId());
    }
    
    // Test toString method
    @Test
    public void testToString() {
        PropertyName name = new PropertyName("testProperty");
        Class<?> scope = TestClass.class;
        Class<?> gen = ObjectIdGenerator.class;
        
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, scope, gen);
        
        String expected = "ObjectIdInfo: propName=testProperty, scope=com.fasterxml.jackson.databind.introspect.TestClass, generatorType=class com.fasterxml.jackson.annotation.ObjectIdGenerator, alwaysAsId=false";
        
        assertEquals(expected, objectIdInfo.toString());
    }
    
    // Test getters
    @Test
    public void testGetters() {
        PropertyName name = new PropertyName("testProperty");
        Class<?> scope = TestClass.class;
        Class<?> gen = ObjectIdGenerator.class;
        Class<?> resolver = ObjectIdResolver.class;
        
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, scope, gen, resolver);
        
        assertEquals(name, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(gen, objectIdInfo.getGeneratorType());
        assertEquals(resolver, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }
    
    public static class TestClass {
        
    }
}