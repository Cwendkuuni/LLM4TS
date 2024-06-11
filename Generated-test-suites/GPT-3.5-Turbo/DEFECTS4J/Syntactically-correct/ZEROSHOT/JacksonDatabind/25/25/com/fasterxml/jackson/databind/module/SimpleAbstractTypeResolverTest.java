package com.fasterxml.jackson.databind.module;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SimpleAbstractTypeResolverTest {

    private SimpleAbstractTypeResolver resolver;

    @Before
    public void setUp() {
        resolver = new SimpleAbstractTypeResolver();
    }

    @Test
    public void testAddMapping() {
        Class<?> superType = Collection.class;
        Class<?> subType = LinkedList.class;

        resolver.addMapping(superType, subType);

        assertTrue(resolver._mappings.containsKey(new ClassKey(superType)));
        assertEquals(subType, resolver._mappings.get(new ClassKey(superType)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingSameClass() {
        Class<?> superType = Collection.class;

        resolver.addMapping(superType, superType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingNonSubType() {
        Class<?> superType = Collection.class;
        Class<?> subType = ArrayList.class;

        resolver.addMapping(superType, subType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingNonAbstractSuperType() {
        Class<?> superType = ArrayList.class;
        Class<?> subType = LinkedList.class;

        resolver.addMapping(superType, subType);
    }

    @Test
    public void testFindTypeMapping() {
        resolver.addMapping(Collection.class, LinkedList.class);

        DeserializationConfig config = null;
        JavaType type = null;

        JavaType result = resolver.findTypeMapping(config, type);

        assertNull(result);
    }

    @Test
    public void testResolveAbstractType() {
        DeserializationConfig config = null;
        JavaType type = null;

        JavaType result = resolver.resolveAbstractType(config, type);

        assertNull(result);
    }
}