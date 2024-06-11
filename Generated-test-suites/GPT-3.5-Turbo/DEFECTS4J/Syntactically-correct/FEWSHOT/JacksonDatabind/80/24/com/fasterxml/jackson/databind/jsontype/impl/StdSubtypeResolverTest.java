package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class StdSubtypeResolverTest {
    @Test
    public void testRegisterSubtypes() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        NamedType[] types = {new NamedType(java.lang.String.class)};
        resolver.registerSubtypes(types);
        assertEquals(1, resolver._registeredSubtypes.size());
        assertEquals(java.lang.String.class, resolver._registeredSubtypes.toArray()[0].getType());
    }

    @Test
    public void testRegisterSubtypesClass() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        resolver.registerSubtypes(java.lang.String.class);
        assertEquals(1, resolver._registeredSubtypes.size());
        assertEquals(java.lang.String.class, resolver._registeredSubtypes.toArray()[0].getType());
    }

    @Test
    public void testCollectAndResolveSubtypesByClass() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        MapperConfig<?> config = null;
        AnnotatedMember property = null;
        JavaType baseType = null;
        assertEquals(0, resolver.collectAndResolveSubtypesByClass(config, property, baseType).size());
    }

    @Test
    public void testCollectAndResolveSubtypesByClassAnnotatedClass() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        MapperConfig<?> config = null;
        AnnotatedClass type = null;
        assertEquals(0, resolver.collectAndResolveSubtypesByClass(config, type).size());
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        MapperConfig<?> config = null;
        AnnotatedMember property = null;
        JavaType baseType = null;
        assertEquals(0, resolver.collectAndResolveSubtypesByTypeId(config, property, baseType).size());
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdAnnotatedClass() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        MapperConfig<?> config = null;
        AnnotatedClass baseType = null;
        assertEquals(0, resolver.collectAndResolveSubtypesByTypeId(config, baseType).size());
    }
}