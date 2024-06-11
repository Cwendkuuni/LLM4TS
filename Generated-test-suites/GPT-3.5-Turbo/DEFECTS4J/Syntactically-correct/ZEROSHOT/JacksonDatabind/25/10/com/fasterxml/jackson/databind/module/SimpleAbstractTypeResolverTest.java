package com.fasterxml.jackson.databind.module;

import org.junit.*;
import static org.junit.Assert.*;

public class SimpleAbstractTypeResolverTest {

    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);

        // verify mapping is added correctly
        assertEquals(LinkedList.class, resolver._mappings.get(new ClassKey(Collection.class)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingWithSameClass() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, Collection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingWithInvalidSubType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, String.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingWithNonAbstractSuperType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(ArrayList.class, LinkedList.class);
    }

    @Test
    public void testFindTypeMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);

        // create a JavaType with Collection as the raw class
        JavaType collectionType = new JavaType(Collection.class);
        JavaType resolvedType = resolver.findTypeMapping(null, collectionType);

        // verify resolved type is correct
        assertEquals(LinkedList.class, resolvedType.getRawClass());
    }

    @Test
    public void testResolveAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        // create a JavaType with Collection as the raw class
        JavaType collectionType = new JavaType(Collection.class);
        JavaType resolvedType = resolver.resolveAbstractType(null, collectionType);

        // verify resolved type is null
        assertNull(resolvedType);
    }

}