package com.fasterxml.jackson.databind.module;

import java.util.LinkedList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleAbstractTypeResolverTest {

    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);

        assertEquals(LinkedList.class, resolver._mappings.get(new ClassKey(Collection.class)));
        assertEquals(LinkedList.class, resolver._mappings.get(new ClassKey(List.class)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingWithSameClass() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        resolver.addMapping(LinkedList.class, LinkedList.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingWithInvalidSubType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        resolver.addMapping(Collection.class, String.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingWithNonAbstractSuperType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        resolver.addMapping(String.class, LinkedList.class);
    }

    @Test
    public void testFindTypeMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);

        JavaType result1 = resolver.findTypeMapping(null, new JavaType(Collection.class));
        JavaType result2 = resolver.findTypeMapping(null, new JavaType(List.class));
        JavaType result3 = resolver.findTypeMapping(null, new JavaType(LinkedList.class));
        JavaType result4 = resolver.findTypeMapping(null, new JavaType(String.class));

        assertEquals(new JavaType(LinkedList.class), result1);
        assertEquals(new JavaType(LinkedList.class), result2);
        assertEquals(new JavaType(LinkedList.class), result3);
        assertEquals(null, result4);
    }

    @Test
    public void testResolveAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        JavaType result = resolver.resolveAbstractType(null, new JavaType(Collection.class));

        assertEquals(null, result);
    }

}