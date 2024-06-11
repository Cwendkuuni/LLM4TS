package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedClassResolver;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;

public class StdSubtypeResolverTest {

    private StdSubtypeResolver subtypeResolver;
    private MapperConfig<?> config;
    private AnnotationIntrospector ai;

    @Before
    public void setUp() {
        subtypeResolver = new StdSubtypeResolver();
    }

    @Test
    public void testRegisterSubtypes() {
        NamedType type1 = new NamedType(String.class);
        NamedType type2 = new NamedType(Integer.class);
        subtypeResolver.registerSubtypes(type1, type2);

        LinkedHashSet<NamedType> registeredSubtypes = subtypeResolver.getRegisteredSubtypes();
        assertTrue(registeredSubtypes.contains(type1));
        assertTrue(registeredSubtypes.contains(type2));
    }

    @Test
    public void testRegisterSubtypesWithClass() {
        subtypeResolver.registerSubtypes(String.class, Integer.class);

        LinkedHashSet<NamedType> registeredSubtypes = subtypeResolver.getRegisteredSubtypes();
        assertTrue(registeredSubtypes.contains(new NamedType(String.class)));
        assertTrue(registeredSubtypes.contains(new NamedType(Integer.class)));
    }

    @Test
    public void testCollectAndResolveSubtypesByClass() {
        MapperConfig<?> config = null;
        AnnotatedMember property = null;
        JavaType baseType = null;

        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(config, property, baseType);

        assertNotNull(subtypes);
        assertTrue(subtypes.isEmpty());
    }

    @Test
    public void testCollectAndResolveSubtypesByClassWithType() {
        MapperConfig<?> config = null;
        AnnotatedClass type = null;

        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(config, type);

        assertNotNull(subtypes);
        assertTrue(subtypes.isEmpty());
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId() {
        MapperConfig<?> config = null;
        AnnotatedMember property = null;
        JavaType baseType = null;

        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByTypeId(config, property, baseType);

        assertNotNull(subtypes);
        assertTrue(subtypes.isEmpty());
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdWithType() {
        MapperConfig<?> config = null;
        AnnotatedClass baseType = null;

        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByTypeId(config, baseType);

        assertNotNull(subtypes);
        assertTrue(subtypes.isEmpty());
    }

    @Test
    public void testCollectAndResolve() {
        AnnotatedClass annotatedType = null;
        NamedType namedType = null;
        MapperConfig<?> config = null;
        AnnotationIntrospector ai = null;
        HashMap<NamedType, NamedType> collectedSubtypes = new HashMap<NamedType, NamedType>();

        subtypeResolver._collectAndResolve(annotatedType, namedType, config, ai, collectedSubtypes);

        assertTrue(collectedSubtypes.isEmpty());
    }

    @Test
    public void testCollectAndResolveByTypeId() {
        AnnotatedClass annotatedType = null;
        NamedType namedType = null;
        MapperConfig<?> config = null;
        Set<Class<?>> typesHandled = new HashSet<Class<?>>();
        Map<String, NamedType> byName = new LinkedHashMap<String, NamedType>();

        subtypeResolver._collectAndResolveByTypeId(annotatedType, namedType, config, typesHandled, byName);

        assertTrue(byName.isEmpty());
    }

    @Test
    public void testCombineNamedAndUnnamed() {
        Class<?> rawBase = null;
        Set<Class<?>> typesHandled = new HashSet<Class<?>>();
        Map<String,NamedType> byName = new LinkedHashMap<String,NamedType>();

        Collection<NamedType> result = subtypeResolver._combineNamedAndUnnamed(rawBase, typesHandled, byName);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

}