package com.fasterxml.jackson.databind.introspect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ObjectIdInfoTest {

    @Test
    public void testObjectIdInfoConstructionWithPropertyNameAndScope() {
        PropertyName propertyName = new PropertyName("name");
        Class<String> scope = String.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, null, null);

        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertNotNull(objectIdInfo.getScope());
        assertNull(objectIdInfo.getGeneratorType());
        assertNull(objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoConstructionWithPropertyNameAndScopeAndGenerator() {
        PropertyName propertyName = new PropertyName("name");
        Class<String> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, null);

        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertNotNull(objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertNull(objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoConstructionWithStringNameAndScope() {
        String name = "name";
        Class<String> scope = String.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, scope, null, false);

        PropertyName propertyName = objectIdInfo.getPropertyName();
        assertEquals(name, propertyName.getName());
        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertNotNull(objectIdInfo.getScope());
        assertNull(objectIdInfo.getGeneratorType());
        assertNull(objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoConstructionWithStringNameAndScopeAndGeneratorAndAlwaysAsId() {
        String name = "name";
        Class<String> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, scope, generator, true);

        PropertyName propertyName = objectIdInfo.getPropertyName();
        assertEquals(name, propertyName.getName());
        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertNotNull(objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertNull(objectIdInfo.getResolverType());
        assertTrue(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoConstructionWithPropertyNameAndScopeAndGeneratorAndAlwaysAsId() {
        PropertyName propertyName = new PropertyName("name");
        Class<String> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, true);

        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertNotNull(objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertNull(objectIdInfo.getResolverType());
        assertTrue(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoConstructionWithPropertyNameAndScopeAndGeneratorAndResolver() {
        PropertyName propertyName = new PropertyName("name");
        Class<String> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = SimpleObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false, resolver);

        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertNotNull(objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertEquals(resolver, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoConstructionWithPropertyNameAndScopeAndGeneratorAndAlwaysAsIdAndResolver() {
        PropertyName propertyName = new PropertyName("name");
        Class<String> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = SimpleObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, true, resolver);

        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertNotNull(objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertEquals(resolver, objectIdInfo.getResolverType());
        assertTrue(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoWithAlwaysAsId() {
        PropertyName propertyName = new PropertyName("name");
        Class<String> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = SimpleObjectIdResolver.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false, resolver);

        ObjectIdInfo objectIdInfoWithAlwaysAsId = objectIdInfo.withAlwaysAsId(true);

        assertNotSame(objectIdInfo, objectIdInfoWithAlwaysAsId);
        assertTrue(objectIdInfoWithAlwaysAsId.getAlwaysAsId());
    }
}