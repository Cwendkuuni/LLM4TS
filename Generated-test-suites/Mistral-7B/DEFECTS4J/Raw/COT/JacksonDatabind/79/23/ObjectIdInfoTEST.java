package com.example.objectidinfo; // replace with your test package if needed

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;

public class ObjectIdInfoTest {

    @Test
    public void testObjectIdInfoConstructorWithAllArguments() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<String> scope = String.class;
        Class<CustomObjectIdGenerator> generator = CustomObjectIdGenerator.class;
        Class<CustomObjectIdResolver> resolver = CustomObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false, resolver);

        assertEquals("testProperty", objectIdInfo.getPropertyName().getName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertEquals(resolver, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoConstructorWithThreeArguments() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<String> scope = String.class;
        Class<CustomObjectIdGenerator> generator = CustomObjectIdGenerator.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false);

        assertEquals("testProperty", objectIdInfo.getPropertyName().getName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertNull(objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoConstructorWithTwoArguments() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<String> scope = String.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, CustomObjectIdGenerator.class);

        assertEquals("testProperty", objectIdInfo.getPropertyName().getName());
        assertEquals(scope, objectIdInfo.getScope());
        assertNull(objectIdInfo.getGeneratorType());
        assertNull(objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoWithAlwaysAsId() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<String> scope = String.class;
        Class<CustomObjectIdGenerator> generator = CustomObjectIdGenerator.class;
        Class<CustomObjectIdResolver> resolver = CustomObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false, resolver);
        ObjectIdInfo objectIdInfoWithAlwaysAsId = objectIdInfo.withAlwaysAsId(true);

        assertNotSame(objectIdInfo, objectIdInfoWithAlwaysAsId);
        assertTrue(objectIdInfoWithAlwaysAsId.getAlwaysAsId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectIdInfoWithNullPropertyName() {
        Class<String> scope = String.class;
        Class<CustomObjectIdGenerator> generator = CustomObjectIdGenerator.class;
        Class<CustomObjectIdResolver> resolver = CustomObjectIdResolver.class;

        new ObjectIdInfo(null, scope, generator, false, resolver);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectIdInfoWithNullScope() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<CustomObjectIdGenerator> generator = CustomObjectIdGenerator.class;
        Class<CustomObjectIdResolver> resolver = CustomObjectIdResolver.class;

        new ObjectIdInfo(propertyName, null, generator, false, resolver);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectIdInfoWithNullGenerator() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<String> scope = String.class;
        Class<CustomObjectIdResolver> resolver = CustomObjectIdResolver.class;

        new ObjectIdInfo(propertyName, scope, null, false, resolver);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectIdInfoWithNullResolver() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<String> scope = String.class;
        Class<CustomObjectIdGenerator> generator = CustomObjectIdGenerator.class;

        new ObjectIdInfo(propertyName, scope, generator, false, null);
    }
}