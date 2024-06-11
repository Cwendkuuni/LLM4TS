package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class StdSubtypeResolverTest {

    @Test
    public void testRegisterSubtypes() {
        NamedType[] namedTypes = {new NamedType(String.class)};
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        resolver.registerSubtypes(namedTypes);

        assertEquals(1, resolver._registeredSubtypes.size());
        assertEquals(String.class, resolver._registeredSubtypes.toArray()[0].getType());
    }

    @Test
    public void testCollectAndResolveSubtypesByClass() {
        MapperConfig<?> config = null;
        AnnotatedMember property = null;
        JavaType baseType = null;
        
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        Collection<NamedType> result = resolver.collectAndResolveSubtypesByClass(config, property, baseType);

        assertEquals(0, result.size());
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId() {
        MapperConfig<?> config = null;
        AnnotatedMember property = null;
        JavaType baseType = null;

        StdSubtypeResolver resolver = new StdSubtypeResolver();
        Collection<NamedType> result = resolver.collectAndResolveSubtypesByTypeId(config, property, baseType);

        assertEquals(0, result.size());
    }
}