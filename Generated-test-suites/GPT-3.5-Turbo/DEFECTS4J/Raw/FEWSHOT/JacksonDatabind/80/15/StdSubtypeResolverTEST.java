package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class StdSubtypeResolverTest {
    @Test
    public void testRegisterSubtypes() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        
        // Test with single NamedType
        NamedType namedType1 = new NamedType(String.class);
        resolver.registerSubtypes(namedType1);
        assertEquals(1, resolver._registeredSubtypes.size());
        assertEquals(namedType1, resolver._registeredSubtypes.iterator().next());

        // Test with multiple NamedType
        NamedType namedType2 = new NamedType(Integer.class);
        NamedType namedType3 = new NamedType(Double.class);
        resolver.registerSubtypes(namedType2, namedType3);
        assertEquals(3, resolver._registeredSubtypes.size());
        assert(resolver._registeredSubtypes.contains(namedType2));
        assert(resolver._registeredSubtypes.contains(namedType3));
    }

    @Test
    public void testCollectAndResolveSubtypesByClass() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        
        // Test with no registered subtypes
        Collection<NamedType> result1 = resolver.collectAndResolveSubtypesByClass(null, null);
        assertNull(result1);
        
        // Test when base type is null
        NamedType namedType1 = new NamedType(String.class);
        resolver.registerSubtypes(namedType1);
        Collection<NamedType> result2 = resolver.collectAndResolveSubtypesByClass(null, null);
        assertEquals(1, result2.size());
        assertEquals(namedType1, result2.iterator().next());
        
        // Test when base type is not null
        AnnotatedClass annotatedClass = new AnnotatedClass(String.class, null, null);
        Collection<NamedType> result3 = resolver.collectAndResolveSubtypesByClass(null, annotatedClass);
        assertEquals(1, result3.size());
        assertEquals(namedType1, result3.iterator().next());
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        
        // Test with no registered subtypes
        Collection<NamedType> result1 = resolver.collectAndResolveSubtypesByTypeId(null, null);
        assertNull(result1);
        
        // Test when base type is null
        NamedType namedType1 = new NamedType(String.class);
        resolver.registerSubtypes(namedType1);
        Collection<NamedType> result2 = resolver.collectAndResolveSubtypesByTypeId(null, null);
        assertEquals(1, result2.size());
        assertEquals(namedType1, result2.iterator().next());
        
        // Test when base type is not null
        AnnotatedClass annotatedClass = new AnnotatedClass(String.class, null, null);
        Collection<NamedType> result3 = resolver.collectAndResolveSubtypesByTypeId(null, annotatedClass);
        assertEquals(1, result3.size());
        assertEquals(namedType1, result3.iterator().next());
    }
}