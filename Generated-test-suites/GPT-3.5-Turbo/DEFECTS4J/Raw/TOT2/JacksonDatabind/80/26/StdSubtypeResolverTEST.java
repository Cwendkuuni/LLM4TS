package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedClassResolver;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.NamedType;

public class StdSubtypeResolverTest {

    @Test
    public void testRegisterSubtypes() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        
        NamedType type1 = new NamedType(Integer.class);
        NamedType type2 = new NamedType(String.class);
        
        resolver.registerSubtypes(type1, type2);
        
        Set<NamedType> registeredSubtypes = resolver._registeredSubtypes;
        
        assertNotNull(registeredSubtypes);
        assertTrue(registeredSubtypes.contains(type1));
        assertTrue(registeredSubtypes.contains(type2));
    }

    @Test
    public void testCollectAndResolveSubtypesByClass() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        MapperConfig<?> config = null;
        AnnotatedMember property = null;
        AnnotatedClass baseType = null;
        
        Collection<NamedType> result = resolver.collectAndResolveSubtypesByClass(config, property, baseType);
        
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        MapperConfig<?> config = null;
        AnnotatedMember property = null;
        AnnotatedClass baseType = null;
        
        Collection<NamedType> result = resolver.collectAndResolveSubtypesByTypeId(config, property, baseType);
        
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testInternalMethods() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        
        AnnotatedClass annotatedType = AnnotatedClassResolver.resolveWithoutSuperTypes(null, Integer.class);
        NamedType namedType = new NamedType(String.class);
        MapperConfig<?> config = null;
        StdSubtypeResolver spyResolver = Mockito.spy(resolver);
        
        HashMap<NamedType, NamedType> collectedSubtypes = new HashMap<>();
        
        spyResolver._collectAndResolve(annotatedType, namedType, config, null, collectedSubtypes);
        
        Collection<NamedType> result = new ArrayList<>(collectedSubtypes.values());
        
        assertFalse(result.isEmpty());
    }

    @Test
    public void testCombineNamedAndUnnamed() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        Class<Integer> rawBase = Integer.class;
        Set<Class<?>> typesHandled = new LinkedHashSet<>();
        Map<String, NamedType> byName = new LinkedHashMap<>();
        
        Collection<NamedType> result = (Collection<NamedType>) resolver._combineNamedAndUnnamed(rawBase, typesHandled, byName);
        
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}