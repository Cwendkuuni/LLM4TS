package com.fasterxml.jackson.databind.module;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class SimpleAbstractTypeResolverTest {

    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);
        assertEquals(resolver._mappings.get(new ClassKey(Collection.class)), LinkedList.class);
        assertEquals(resolver._mappings.get(new ClassKey(List.class)), LinkedList.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingSameClass() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, Collection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingNoSubType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(AbstractTypeResolver.class, LinkedList.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingConcreteClass() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(List.class, ArrayList.class);
    }

    @Test
    public void testFindTypeMappingFound() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        DeserializationConfig config = null; //Assign a valid DeserializationConfig object here
        JavaType type = null; //Assign a valid JavaType object here
        JavaType result = resolver.findTypeMapping(config, type);
        assertNotEquals(result, null);
    }

    @Test
    public void testFindTypeMappingNotFound() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(List.class, LinkedList.class);
        DeserializationConfig config = null; //Assign a valid DeserializationConfig object here
        JavaType type = null; //Assign a valid JavaType object here
        JavaType result = resolver.findTypeMapping(config, type);
        assertEquals(result, null);
    }

    @Test
    public void testResolveAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        DeserializationConfig config = null; //Assign a valid DeserializationConfig object here
        JavaType type = null; //Assign a valid JavaType object here
        JavaType result = resolver.resolveAbstractType(config, type);
        assertEquals(result, null);
    }
}