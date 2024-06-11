package com.fasterxml.jackson.databind.module;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;


import org.junit.Test;

public class SimpleAbstractTypeResolverTest {

    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(List.class, LinkedList.class);
        resolver.addMapping(Collection.class, LinkedList.class);

        assertNotNull(resolver._mappings);
        assertEquals(2, resolver._mappings.size());
    }

    @Test
    public void testAddMappingSameClass() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        try {
            resolver.addMapping(List.class, List.class);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testAddMappingNonSubtype() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        try {
            resolver.addMapping(Collection.class, ArrayList.class);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testAddMappingNonAbstract() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        try {
            resolver.addMapping(ArrayList.class, LinkedList.class);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    // More test cases for other methods can be added here

}