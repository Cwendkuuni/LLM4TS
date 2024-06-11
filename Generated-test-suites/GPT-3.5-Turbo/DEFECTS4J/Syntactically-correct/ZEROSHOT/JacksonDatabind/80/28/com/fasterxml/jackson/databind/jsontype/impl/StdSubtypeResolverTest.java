package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
import org.junit.Before;
import org.junit.Test;
import java.lang.reflect.Modifier;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class StdSubtypeResolverTest {

    private StdSubtypeResolver resolver;
    private MapperConfig<?> config;
    private AnnotatedMember property;
    private JavaType baseType;

    @Before
    public void setup() {
        resolver = new StdSubtypeResolver();
        config = new ObjectMapper().getDeserializationConfig();
        property = null;
        baseType = null;
    }

    @Test
    public void testRegisterSubtypes() {
        NamedType namedType1 = new NamedType(String.class);
        NamedType namedType2 = new NamedType(Integer.class);
        resolver.registerSubtypes(namedType1, namedType2);
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, property, baseType);
        assertEquals(2, subtypes.size());
    }

    @Test
    public void testRegisterSubtypesByClasses() {
        Class<?> class1 = String.class;
        Class<?> class2 = Integer.class;
        resolver.registerSubtypes(class1, class2);
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, property, baseType);
        assertEquals(2, subtypes.size());
    }

    @Test
    public void testCollectAndResolveSubtypesByClass() {
        AnnotatedClass annotatedClass = AnnotatedClassResolver.resolveWithoutSuperTypes(config, String.class);
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, annotatedClass);
        assertEquals(0, subtypes.size());
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId() {
        AnnotatedClass annotatedClass = AnnotatedClassResolver.resolveWithoutSuperTypes(config, String.class);
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(config, property, baseType);
        assertEquals(0, subtypes.size());
    }

    @Test
    public void testInternalMethods_CollectAndResolve() {
        AnnotatedClass annotatedClass = AnnotatedClassResolver.resolveWithoutSuperTypes(config, String.class);
        NamedType namedType = new NamedType(String.class);
        HashMap<NamedType, NamedType> collectedSubtypes = new HashMap<>();
        resolver._collectAndResolve(annotatedClass, namedType, config, config.getAnnotationIntrospector(), collectedSubtypes);
        assertEquals(0, collectedSubtypes.size());

        annotatedClass = AnnotatedClassResolver.resolveWithoutSuperTypes(config, Integer.class);
        namedType = new NamedType(Integer.class);
        collectedSubtypes = new HashMap<>();
        resolver._collectAndResolve(annotatedClass, namedType, config, config.getAnnotationIntrospector(), collectedSubtypes);
        assertEquals(0, collectedSubtypes.size());
    }

    @Test
    public void testInternalMethods_CollectAndResolveByTypeId() {
        AnnotatedClass annotatedClass = AnnotatedClassResolver.resolveWithoutSuperTypes(config, String.class);
        NamedType namedType = new NamedType(String.class);
        Set<Class<?>> typesHandled = new HashSet<>();
        Map<String, NamedType> byName = new LinkedHashMap<>();
        resolver._collectAndResolveByTypeId(annotatedClass, namedType, config, typesHandled, byName);
        assertEquals(0, typesHandled.size());
        assertEquals(0, byName.size());

        annotatedClass = AnnotatedClassResolver.resolveWithoutSuperTypes(config, Integer.class);
        namedType = new NamedType(Integer.class);
        typesHandled = new HashSet<>();
        byName = new LinkedHashMap<>();
        resolver._collectAndResolveByTypeId(annotatedClass, namedType, config, typesHandled, byName);
        assertEquals(0, typesHandled.size());
        assertEquals(0, byName.size());
    }

    @Test
    public void testInternalMethods_CombineNamedAndUnnamed() {
        Class<?> rawBase = String.class;
        Set<Class<?>> typesHandled = new HashSet<>();
        Map<String, NamedType> byName = new LinkedHashMap<>();
        Collection<NamedType> combined = resolver._combineNamedAndUnnamed(rawBase, typesHandled, byName);
        assertEquals(0, combined.size());

        rawBase = Integer.class;
        typesHandled = new HashSet<>();
        byName = new LinkedHashMap<>();
        combined = resolver._combineNamedAndUnnamed(rawBase, typesHandled, byName);
        assertEquals(0, combined.size());
    }
}