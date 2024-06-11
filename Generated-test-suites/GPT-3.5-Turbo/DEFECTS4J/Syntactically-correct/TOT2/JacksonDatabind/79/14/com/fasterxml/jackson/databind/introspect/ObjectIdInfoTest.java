package com.fasterxml.jackson.databind.introspect;

import static org.junit.Assert.*;

import org.junit.Test;

public class ObjectIdInfoTest {

    @Test
    public void testConstructorWithResolver() {
        // Test constructor with resolver
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("test"), String.class, SimpleObjectIdGenerator.class, SimpleObjectIdResolver.class);
        assertEquals("test", objectIdInfo.getPropertyName().getSimpleName());
        assertEquals(String.class, objectIdInfo.getScope());
        assertEquals(SimpleObjectIdGenerator.class, objectIdInfo.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testConstructorWithoutResolver() {
        // Test constructor without resolver
        ObjectIdInfo objectIdInfo = new ObjectIdInfo("test", String.class, SimpleObjectIdGenerator.class);
        assertEquals("test", objectIdInfo.getPropertyName().getSimpleName());
        assertEquals(String.class, objectIdInfo.getScope());
        assertEquals(SimpleObjectIdGenerator.class, objectIdInfo.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testWithAlwaysAsId() {
        // Test withAlwaysAsId method
        ObjectIdInfo objectIdInfo = new ObjectIdInfo("test", String.class, SimpleObjectIdGenerator.class);
        assertFalse(objectIdInfo.getAlwaysAsId());
        objectIdInfo = objectIdInfo.withAlwaysAsId(true);
        assertTrue(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testToString() {
        // Test toString method
        ObjectIdInfo objectIdInfo = new ObjectIdInfo("test", String.class, SimpleObjectIdGenerator.class);
        assertEquals("ObjectIdInfo: propName=test, scope=java.lang.String, generatorType=com.fasterxml.jackson.annotation.SimpleObjectIdGenerator, alwaysAsId=false", objectIdInfo.toString());
    }
}