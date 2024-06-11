package com.fasterxml.jackson.databind.introspect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ObjectIdInfoTest {

    @Test
    public void testObjectIdInfoConstruction() {
        PropertyName propertyName = new PropertyName("name");
        Class<String> scope = String.class;
        Class<CustomObjectIdGenerator> generator = CustomObjectIdGenerator.class;
        Class<CustomObjectIdResolver> resolver = CustomObjectIdResolver.class;

        ObjectIdInfo objIdInfo = new ObjectIdInfo(propertyName, scope, generator, false, resolver);
        assertNotNull(objIdInfo);
        assertEquals(propertyName, objIdInfo.getPropertyName());
        assertEquals(scope, objIdInfo.getScope());
        assertEquals(generator, objIdInfo.getGeneratorType());
        assertEquals(resolver, objIdInfo.getResolverType());
        assertFalse(objIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoWithAlwaysAsId() {
        PropertyName propertyName = new PropertyName("name");
        Class<String> scope = String.class;
        Class<CustomObjectIdGenerator> generator = CustomObjectIdGenerator.class;
        Class<CustomObjectIdResolver> resolver = CustomObjectIdResolver.class;

        ObjectIdInfo objIdInfo = new ObjectIdInfo(propertyName, scope, generator, true, resolver);
        assertNotNull(objIdInfo);
        assertEquals(propertyName, objIdInfo.getPropertyName());
        assertEquals(scope, objIdInfo.getScope());
        assertEquals(generator, objIdInfo.getGeneratorType());
        assertEquals(resolver, objIdInfo.getResolverType());
        assertTrue(objIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoWithNullPropertyName() {
        assertNull(new ObjectIdInfo(null, String.class, CustomObjectIdGenerator.class, false, SimpleObjectIdResolver.class));
    }

    @Test
    public void testObjectIdInfoWithNullScope() {
        PropertyName propertyName = new PropertyName("name");
        assertNull(new ObjectIdInfo(propertyName, null, CustomObjectIdGenerator.class, false, SimpleObjectIdResolver.class));
    }

    @Test
    public void testObjectIdInfoWithNullGenerator() {
        PropertyName propertyName = new PropertyName("name");
        Class<String> scope = String.class;
        assertNull(new ObjectIdInfo(propertyName, scope, null, false, SimpleObjectIdResolver.class));
    }

    @Test
    public void testObjectIdInfoWithNullResolver() {
        PropertyName propertyName = new PropertyName("name");
        Class<String> scope = String.class;
        Class<CustomObjectIdGenerator> generator = CustomObjectIdGenerator.class;
        assertNull(new ObjectIdInfo(propertyName, scope, generator, false, null));
    }

    @Test
    public void testObjectIdInfoWithDefaultResolver() {
        PropertyName propertyName = new PropertyName("name");
        Class<String> scope = String.class;
        Class<CustomObjectIdGenerator> generator = CustomObjectIdGenerator.class;

        ObjectIdInfo objIdInfo = new ObjectIdInfo(propertyName, scope, generator, false, null);
        assertNotNull(objIdInfo);
        assertEquals(propertyName, objIdInfo.getPropertyName());
        assertEquals(scope, objIdInfo.getScope());
        assertEquals(generator, objIdInfo.getGeneratorType());
        assertNotNull(objIdInfo.getResolverType());
        assertEquals(SimpleObjectIdResolver.class, objIdInfo.getResolverType());
        assertFalse(objIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoWithCustomResolver() {
        PropertyName propertyName = new PropertyName("name");
        Class<String> scope = String.class;
        Class<CustomObjectIdGenerator> generator = CustomObjectIdGenerator.class;
        Class<CustomObjectIdResolver> resolver = CustomObjectIdResolver.class;

        ObjectIdInfo objIdInfo = new ObjectIdInfo(propertyName, scope, generator, false, resolver);
        assertNotNull(objIdInfo);
        assertEquals(propertyName, objIdInfo.getPropertyName());
        assertEquals(scope, objIdInfo.getScope());
        assertEquals(generator, objIdInfo.getGeneratorType());
        assertEquals(resolver, objIdInfo.getResolverType());
        assertFalse(objIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoWithDefaultGenerator() {
        PropertyName propertyName = new PropertyName("name");
        Class<String> scope = String.class;

        ObjectIdInfo objIdInfo = new ObjectIdInfo(propertyName, scope, null, false, SimpleObjectIdResolver.class);
        assertNotNull(objIdInfo);
        assertEquals(propertyName, objIdInfo.getPropertyName());
        assertEquals(scope, objIdInfo.getScope());
        assertNull(objIdInfo.getGeneratorType());
        assertNotNull(objIdInfo.getResolverType());
        assertEquals(SimpleObjectIdResolver.class, objIdInfo.getResolverType());
        assertFalse(objIdInfo.getAlwaysAsId());
    }

    @Test
    public void testToString() {
        PropertyName propertyName = new PropertyName("name");
        Class<String> scope = String.class;
        Class<CustomObjectIdGenerator> generator = CustomObjectIdGenerator.class;
        Class<CustomObjectIdResolver> resolver = CustomObjectIdResolver.class;

        ObjectIdInfo objIdInfo = new ObjectIdInfo(propertyName, scope, generator, false, resolver);

        String toString = objIdInfo.toString();
        assertTrue(toString.contains("propName=" + propertyName.getId()));
        assertTrue(toString.contains("scope=" + scope.getName()));
        assertTrue(toString.contains("generatorType=" + generator.getName()));
        assertTrue(toString.contains("alwaysAsId=" + objIdInfo.getAlwaysAsId()));
    }
}