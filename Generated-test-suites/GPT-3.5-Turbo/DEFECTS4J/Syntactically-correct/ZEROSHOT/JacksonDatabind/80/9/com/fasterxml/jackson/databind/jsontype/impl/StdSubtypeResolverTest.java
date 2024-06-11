package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.*;
import static org.junit.Assert.*;
import java.lang.reflect.Modifier;
import java.util.*;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedClassResolver;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;

public class StdSubtypeResolverTest {

    @Test
    public void testRegisterSubtypes() {
        StdSubtypeResolver stdSubtypeResolver = new StdSubtypeResolver();
        NamedType[] types = new NamedType[1];
        types[0] = new NamedType(Object.class);
        stdSubtypeResolver.registerSubtypes(types);
        assertEquals(1, stdSubtypeResolver._registeredSubtypes.size());
    }

    @Test
    public void testRegisterSubtypesClass() {
        StdSubtypeResolver stdSubtypeResolver = new StdSubtypeResolver();
        Class<?>[] classs = new Class<?>[1];
        classs[0] = Object.class;
        stdSubtypeResolver.registerSubtypes(classs);
        assertEquals(1, stdSubtypeResolver._registeredSubtypes.size());
    }

    @Test
    public void testCollectAndResolveSubtypesByClass() {
        StdSubtypeResolver stdSubtypeResolver = new StdSubtypeResolver();
        MapperConfig<?> config = null;
        AnnotatedMember property = null;
        JavaType baseType = null;
        Collection<NamedType> result = stdSubtypeResolver.collectAndResolveSubtypesByClass(config, property, baseType);
        assertNotNull(result);
    }

    @Test
    public void testCollectAndResolveSubtypesByClass_AnnotatedClass() {
        StdSubtypeResolver stdSubtypeResolver = new StdSubtypeResolver();
        MapperConfig<?> config = null;
        AnnotatedClass type = null;
        Collection<NamedType> result = stdSubtypeResolver.collectAndResolveSubtypesByClass(config, type);
        assertNotNull(result);
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId() {
        StdSubtypeResolver stdSubtypeResolver = new StdSubtypeResolver();
        MapperConfig<?> config = null;
        AnnotatedMember property = null;
        JavaType baseType = null;
        Collection<NamedType> result = stdSubtypeResolver.collectAndResolveSubtypesByTypeId(config, property, baseType);
        assertNotNull(result);
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId_AnnotatedClass() {
        StdSubtypeResolver stdSubtypeResolver = new StdSubtypeResolver();
        MapperConfig<?> config = null;
        AnnotatedClass baseType = null;
        Collection<NamedType> result = stdSubtypeResolver.collectAndResolveSubtypesByTypeId(config, baseType);
        assertNotNull(result);
    }

    @Test
    public void test__collectAndResolve() {
        StdSubtypeResolver stdSubtypeResolver = new StdSubtypeResolver();
        AnnotatedClass annotatedType = null;
        NamedType namedType = null;
        MapperConfig<?> config = null;
        AnnotationIntrospector ai = null;
        HashMap<NamedType, NamedType> collectedSubtypes = new HashMap<NamedType, NamedType>();
        stdSubtypeResolver._collectAndResolve(annotatedType, namedType, config, ai, collectedSubtypes);
        assertEquals(0, collectedSubtypes.size());
    }

    @Test
    public void test__collectAndResolveByTypeId() {
        StdSubtypeResolver stdSubtypeResolver = new StdSubtypeResolver();
        AnnotatedClass annotatedType = null;
        NamedType namedType = null;
        MapperConfig<?> config = null;
        Set<Class<?>> typesHandled = new HashSet<Class<?>>();
        Map<String, NamedType> byName = new LinkedHashMap<String, NamedType>();
        stdSubtypeResolver._collectAndResolveByTypeId(annotatedType, namedType, config, typesHandled, byName);
        assertEquals(0, byName.size());
    }

    @Test
    public void test_combineNamedAndUnnamed() {
        StdSubtypeResolver stdSubtypeResolver = new StdSubtypeResolver();
        Class<?> rawBase = null;
        Set<Class<?>> typesHandled = new HashSet<Class<?>>();
        Map<String, NamedType> byName = new LinkedHashMap<String, NamedType>();
        Collection<NamedType> result = stdSubtypeResolver._combineNamedAndUnnamed(rawBase, typesHandled, byName);
        assertNotNull(result);
    }
}