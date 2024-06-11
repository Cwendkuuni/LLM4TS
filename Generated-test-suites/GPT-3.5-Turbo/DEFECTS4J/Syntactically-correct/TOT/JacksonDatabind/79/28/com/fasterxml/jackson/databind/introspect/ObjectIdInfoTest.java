package com.fasterxml.jackson.databind.introspect;

import org.junit.Test;

import static org.junit.Assert.*;

public class ObjectIdInfoTest {

    @Test
    public void testConstructorWithPropertyNameAndScopeAndGeneratorAndResolver() {
        PropertyName propertyName = new PropertyName("name");
        Class<?> scope = Object.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = ObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, resolver);

        assertNotNull(objectIdInfo);
        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertEquals(resolver, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testConstructorWithPropertyNameAndScopeAndGenerator() {
        PropertyName propertyName = new PropertyName("name");
        Class<?> scope = Object.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator);

        assertNotNull(objectIdInfo);
        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertNull(objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testConstructorWithStringNameAndScopeAndGenerator() {
        String propertyName = "name";
        Class<?> scope = Object.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator);

        assertNotNull(objectIdInfo);
        assertEquals(new PropertyName(propertyName), objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertNull(objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testConstructorWithPropertyNameAndScopeAndGeneratorAndAlwaysAsId() {
        PropertyName propertyName = new PropertyName("name");
        Class<?> scope = Object.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        boolean alwaysAsId = true;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, alwaysAsId);

        assertNotNull(objectIdInfo);
        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertNull(objectIdInfo.getResolverType());
        assertTrue(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testWithAlwaysAsId() {
        PropertyName propertyName = new PropertyName("name");
        Class<?> scope = Object.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        boolean alwaysAsId = true;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, alwaysAsId);
        assertEquals(alwaysAsId, objectIdInfo.getAlwaysAsId());

        ObjectIdInfo updatedObjectIdInfo = objectIdInfo.withAlwaysAsId(false);
        assertFalse(updatedObjectIdInfo.getAlwaysAsId());
        assertNotSame(objectIdInfo, updatedObjectIdInfo);
    }

    @Test
    public void testToString() {
        PropertyName propertyName = new PropertyName("name");
        Class<?> scope = Object.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        boolean alwaysAsId = true;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, alwaysAsId);

        String expectedString = "ObjectIdInfo: propName=name, scope=java.lang.Object, generatorType=com.fasterxml.jackson.annotation.ObjectIdGenerator, alwaysAsId=true";
        assertEquals(expectedString, objectIdInfo.toString());
    }

}