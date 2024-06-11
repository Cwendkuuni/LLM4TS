package com.fasterxml.jackson.databind.introspect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ObjectIdInfoTest {

    @Test
    public void testObjectIdInfoConstructionPropertyNameScopeGenerator() {
        PropertyName propertyName = new PropertyName("name");
        Class<String> scope = String.class;
        Class<CustomObjectIdGenerator> generator = CustomObjectIdGenerator.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator);
        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertNull(objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoConstructionPropertyNameScopeGeneratorAlwaysAsId() {
        PropertyName propertyName = new PropertyName("name");
        Class<String> scope = String.class;
        Class<CustomObjectIdGenerator> generator = CustomObjectIdGenerator.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, true);
        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertNull(objectIdInfo.getResolverType());
        assertTrue(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoConstructionPropertyNameScopeGeneratorResolver() {
        PropertyName propertyName = new PropertyName("name");
        Class<String> scope = String.class;
        Class<CustomObjectIdGenerator> generator = CustomObjectIdGenerator.class;
        Class<CustomObjectIdResolver> resolver = CustomObjectIdResolver.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false, resolver);
        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertEquals(resolver, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoConstructionPropertyNameScopeGeneratorAlwaysAsIdResolver() {
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
    public void testObjectIdInfoWithAlwaysAsId() {
        PropertyName propertyName = new PropertyName("name");
        Class<String> scope = String.class;
        Class<CustomObjectIdGenerator> generator = CustomObjectIdGenerator.class;
        Class<CustomObjectIdResolver> resolver = CustomObjectIdResolver.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false, resolver);
        ObjectIdInfo newObjectIdInfo = objectIdInfo.withAlwaysAsId(true);
        assertNotSame(objectIdInfo, newObjectIdInfo);
        assertEquals(propertyName, newObjectIdInfo.getPropertyName());
        assertEquals(scope, newObjectIdInfo.getScope());
        assertEquals(generator, newObjectIdInfo.getGeneratorType());
        assertEquals(resolver, newObjectIdInfo.getResolverType());
        assertTrue(newObjectIdInfo.getAlwaysAsId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectIdInfoWithAlwaysAsIdInvalidArgument() {
        PropertyName propertyName = new PropertyName("name");
        Class<String> scope = String.class;
        Class<CustomObjectIdGenerator> generator = CustomObjectIdGenerator.class;
        Class<CustomObjectIdResolver> resolver = CustomObjectIdResolver.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false, resolver);
        objectIdInfo.withAlwaysAsId(-1);
    }
}