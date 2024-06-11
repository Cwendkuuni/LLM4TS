package com.fasterxml.jackson.databind.module;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class SimpleAbstractTypeResolverTest {

    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(AbstractTypeResolver.class, SimpleAbstractTypeResolver.class);
        resolver.addMapping(Object.class, String.class);

        // Test mappings
        ClassKey abstractType = new ClassKey(AbstractTypeResolver.class);
        ClassKey concreteType = new ClassKey(SimpleAbstractTypeResolver.class);
        ClassKey objectType = new ClassKey(Object.class);

        assertEquals(SimpleAbstractTypeResolver.class, resolver._mappings.get(abstractType));
        assertEquals(String.class, resolver._mappings.get(objectType));
        assertNull(resolver._mappings.get(concreteType));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMapping_selfMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(AbstractTypeResolver.class, AbstractTypeResolver.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMapping_notSubtype() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(AbstractTypeResolver.class, String.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMapping_notAbstract() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Object.class, String.class);
    }

    @Test
    public void testFindTypeMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(AbstractTypeResolver.class, SimpleAbstractTypeResolver.class);

        DeserializationConfig config = new DeserializationConfig();
        JavaType type = config.constructType(AbstractTypeResolver.class);

        JavaType result = resolver.findTypeMapping(config, type);
        assertNotNull(result);
        assertEquals(SimpleAbstractTypeResolver.class, result.getRawClass());
    }

    @Test
    public void testFindTypeMapping_notMapped() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        DeserializationConfig config = new DeserializationConfig();
        JavaType type = config.constructType(AbstractTypeResolver.class);

        JavaType result = resolver.findTypeMapping(config, type);
        assertNull(result);
    }

    @Test
    public void testResolveAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        DeserializationConfig config = new DeserializationConfig();
        JavaType type = config.constructType(AbstractTypeResolver.class);

        JavaType result = resolver.resolveAbstractType(config, type);
        assertNull(result);
    }
}