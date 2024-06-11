package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class StdSubtypeResolverTest {

    @Test
    public void testRegisterSubtypes() {
        NamedType[] types = {new NamedType(Object.class), new NamedType(String.class)};
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        resolver.registerSubtypes(types);
        Collection<NamedType> subtypes = resolver.getRegisteredSubtypes();
        assertEquals(types.length, subtypes.size());
        for (NamedType type : types) {
            assertTrue(subtypes.contains(type));
        }
    }

    @Test
    public void testRegisterSubtypesWithClasses() {
        Class<?>[] classes = {Object.class, String.class};
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        resolver.registerSubtypes(classes);
        Collection<NamedType> subtypes = resolver.getRegisteredSubtypes();
        assertEquals(classes.length, subtypes.size());
        for (Class<?> cls : classes) {
            assertTrue(subtypes.contains(new NamedType(cls)));
        }
    }

    @Test
    public void testCollectAndResolveSubtypesByClass() {
        // TODO: Implement test cases
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId() {
        // TODO: Implement test cases
    }
}