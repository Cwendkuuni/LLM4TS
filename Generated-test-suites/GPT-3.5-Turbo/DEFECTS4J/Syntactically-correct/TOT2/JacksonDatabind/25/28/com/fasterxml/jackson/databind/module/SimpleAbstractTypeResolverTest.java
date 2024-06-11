package com.fasterxml.jackson.databind.module;

import static org.junit.Assert.*;
import org.junit.Test;

public class SimpleAbstractTypeResolverTest {

    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        // Add additional test cases as needed
    }

    @Test
    public void testFindTypeMapping() {
        // Add test cases to check the functionality of findTypeMapping method
    }

    @Test
    public void testResolveAbstractType() {
        // Add test cases to check the functionality of resolveAbstractType method
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingSelf() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, Collection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingNotSubtype() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(List.class, HashMap.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingNotAbstract() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(List.class, ArrayList.class);
    }
}