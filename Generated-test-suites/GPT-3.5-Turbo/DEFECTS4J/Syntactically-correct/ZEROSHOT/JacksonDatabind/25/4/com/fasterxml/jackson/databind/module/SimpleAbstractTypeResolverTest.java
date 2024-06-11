package com.fasterxml.jackson.databind.module;

import org.junit.*;
import static org.junit.Assert.*;

public class SimpleAbstractTypeResolverTest {

    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        Class<?> superType = Collection.class;
        Class<?> subType = LinkedList.class;

        resolver.addMapping(superType, subType);

        assertEquals(subType, resolver._mappings.get(new ClassKey(superType)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMapping_sameClasses() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        Class<?> superType = Collection.class;

        resolver.addMapping(superType, superType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMapping_notSubType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        Class<?> superType = List.class;
        Class<?> subType = LinkedList.class;

        resolver.addMapping(superType, subType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMapping_notAbstract() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        Class<?> superType = LinkedList.class;
        Class<?> subType = LinkedList.class;

        resolver.addMapping(superType, subType);
    }

    @Test
    public void testFindTypeMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        Class<?> superType = Collection.class;
        Class<?> subType = LinkedList.class;
        resolver.addMapping(superType, subType);

        DeserializationConfig config = null; // initialize with appropriate values
        JavaType type = null; // initialize with appropriate values

        JavaType result = resolver.findTypeMapping(config, type);

        assertEquals(type.narrowBy(subType), result);
    }

    @Test
    public void testResolveAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        DeserializationConfig config = null; // initialize with appropriate values
        JavaType type = null; // initialize with appropriate values

        JavaType result = resolver.resolveAbstractType(config, type);

        assertNull(result);
    }

}