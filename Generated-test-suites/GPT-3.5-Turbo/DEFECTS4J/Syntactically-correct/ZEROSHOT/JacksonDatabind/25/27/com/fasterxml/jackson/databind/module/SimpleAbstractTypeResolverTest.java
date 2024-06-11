package com.fasterxml.jackson.databind.module;

import org.junit.Assert;
import org.junit.Test;

public class SimpleAbstractTypeResolverTest {

    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        // Add a mapping from Collection to LinkedList
        resolver.addMapping(Collection.class, LinkedList.class);

        // Verify that the mapping is added correctly
        Class<?> map = resolver._mappings.get(new ClassKey(Collection.class));
        Assert.assertEquals(LinkedList.class, map);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingToItself() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        // Try to add a mapping from Collection to Collection (itself)
        resolver.addMapping(Collection.class, Collection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingToNonSubtype() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        // Try to add a mapping from Collection to ArrayList (not a subtype)
        resolver.addMapping(Collection.class, ArrayList.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingWithNonAbstractSuperType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        // Try to add a mapping from LinkedList to ArrayList (super type is not abstract)
        resolver.addMapping(LinkedList.class, ArrayList.class);
    }

    @Test
    public void testFindTypeMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        // Add a mapping from Collection to LinkedList
        resolver.addMapping(Collection.class, LinkedList.class);

        // Create a JavaType instance for Collection<String>
        TypeFactory typeFactory = TypeFactory.defaultInstance();
        JavaType type = typeFactory.constructCollectionType(Collection.class, String.class);

        // Find the mapping for the type
        JavaType resolvedType = resolver.findTypeMapping(null, type);

        // Verify that LinkedList is returned as the resolved type
        Assert.assertEquals(typeFactory.constructCollectionType(LinkedList.class, String.class), resolvedType);
    }

    @Test
    public void testResolveAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        // Create a JavaType instance for Collection<String>
        TypeFactory typeFactory = TypeFactory.defaultInstance();
        JavaType type = typeFactory.constructCollectionType(Collection.class, String.class);

        // Resolve the abstract type
        JavaType resolvedType = resolver.resolveAbstractType(null, type);

        // Verify that null is returned as the resolved type
        Assert.assertNull(resolvedType);
    }
}