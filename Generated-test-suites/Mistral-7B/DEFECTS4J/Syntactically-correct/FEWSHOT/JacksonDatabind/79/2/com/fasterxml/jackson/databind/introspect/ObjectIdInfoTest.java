package com.fasterxml.jackson.databind.introspect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ObjectIdInfoTest {

    @Test
    public void testDefaultConstructor() {
        assertNull(new ObjectIdInfo(null, null, null, false, null));
    }

    @Test
    public void testConstructorWithNameAndScope() {
        PropertyName propertyName = new PropertyName("name");
        Class<String> scope = String.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, null, false, null);
        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertNull(objectIdInfo.getGeneratorType());
        assertNull(objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testConstructorWithNameAndScopeAndGenerator() {
        PropertyName propertyName = new PropertyName("name");
        Class<String> scope = String.class;
        Class<CustomObjectIdGenerator> generator = CustomObjectIdGenerator.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false, null);
        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertNull(objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testConstructorWithNameAndScopeAndGeneratorAndAlwaysAsId() {
        PropertyName propertyName = new PropertyName("name");
        Class<String> scope = String.class;
        Class<CustomObjectIdGenerator> generator = CustomObjectIdGenerator.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, true, null);
        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertNull(objectIdInfo.getResolverType());
        assertTrue(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testConstructorWithNameAndScopeAndGeneratorAndAlwaysAsIdAndResolver() {
        PropertyName propertyName = new PropertyName("name");
        Class<String> scope = String.class;
        Class<CustomObjectIdGenerator> generator = CustomObjectIdGenerator.class;
        Class<CustomObjectIdResolver> resolver = CustomObjectIdResolver.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, true, resolver);
        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertEquals(resolver, objectIdInfo.getResolverType());
        assertTrue(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testWithAlwaysAsId() {
        PropertyName propertyName = new PropertyName("name");
        Class<String> scope = String.class;
        Class<CustomObjectIdGenerator> generator = CustomObjectIdGenerator.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false, null);
        ObjectIdInfo newObjectIdInfo = objectIdInfo.withAlwaysAsId(true);
        assertNotSame(objectIdInfo, newObjectIdInfo);
        assertTrue(newObjectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testToString() {
        PropertyName propertyName = new PropertyName("name");
        Class<String> scope = String.class;
        Class<CustomObjectIdGenerator> generator = CustomObjectIdGenerator.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, true, null);
        String toString = objectIdInfo.toString();
        String expected = "ObjectIdInfo: propName=name, scope=class java.lang.String, generatorType=class com.example.CustomObjectIdGenerator, alwaysAsId=true";
        assertEquals(expected, toString);
    }
}