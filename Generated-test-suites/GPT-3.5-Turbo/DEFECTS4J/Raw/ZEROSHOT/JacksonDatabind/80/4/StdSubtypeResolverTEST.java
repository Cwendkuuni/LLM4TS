package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.jsontype.NamedType;

public class StdSubtypeResolverTest {

    private StdSubtypeResolver subtypeResolver;

    @Before
    public void setup() {
        subtypeResolver = new StdSubtypeResolver();
    }

    @Test
    public void testRegisterSubtypes() {
        NamedType type1 = new NamedType(Object.class);
        NamedType type2 = new NamedType(String.class);

        subtypeResolver.registerSubtypes(type1, type2);

        assertEquals(2, subtypeResolver._registeredSubtypes.size());
        assertTrue(subtypeResolver._registeredSubtypes.contains(type1));
        assertTrue(subtypeResolver._registeredSubtypes.contains(type2));
    }

    @Test
    public void testRegisterSubtypes_multipleCalls() {
        NamedType type1 = new NamedType(Object.class);
        NamedType type2 = new NamedType(String.class);
        NamedType type3 = new NamedType(Integer.class);

        subtypeResolver.registerSubtypes(type1, type2);
        subtypeResolver.registerSubtypes(type3);

        assertEquals(3, subtypeResolver._registeredSubtypes.size());
        assertTrue(subtypeResolver._registeredSubtypes.contains(type1));
        assertTrue(subtypeResolver._registeredSubtypes.contains(type2));
        assertTrue(subtypeResolver._registeredSubtypes.contains(type3));
    }

    // TODO: Add tests for other methods in StdSubtypeResolver class

}