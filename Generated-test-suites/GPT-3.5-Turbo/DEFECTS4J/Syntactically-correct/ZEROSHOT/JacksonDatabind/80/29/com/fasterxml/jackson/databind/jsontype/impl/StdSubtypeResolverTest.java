package com.fasterxml.jackson.databind.jsontype.impl;

import java.lang.reflect.Modifier;
import java.util.*;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
import org.junit.Test;

import static org.junit.Assert.*;

public class StdSubtypeResolverTest {

    @Test
    public void testRegisterSubtypes() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        NamedType[] types = {new NamedType(Integer.class)};
        resolver.registerSubtypes(types);
        Collection<NamedType> collectedTypes = resolver.collectAndResolveSubtypesByTypeId(null, null, null);
        assertFalse(collectedTypes.isEmpty());
        assertTrue(collectedTypes.containsAll(Arrays.asList(types)));
    }

    @Test
    public void testRegisterSubtypesClass() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        Class<?>[] classes = {Integer.class};
        resolver.registerSubtypes(classes);
        Collection<NamedType> collectedTypes = resolver.collectAndResolveSubtypesByTypeId(null, null, null);
        assertFalse(collectedTypes.isEmpty());
        assertEquals(collectedTypes.size(), classes.length);
        for (Class<?> cls : classes) {
            assertTrue(collectedTypes.contains(new NamedType(cls)));
        }
    }

    @Test
    public void testCollectAndResolveSubtypesByClass() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        Collection<NamedType> collectedTypes = resolver.collectAndResolveSubtypesByClass(null, null);
        assertTrue(collectedTypes.isEmpty());
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        Collection<NamedType> collectedTypes = resolver.collectAndResolveSubtypesByTypeId(null, null, null);
        assertTrue(collectedTypes.isEmpty());
    }

    @Test
    public void testInternalMethods() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        AnnotatedClass annotatedType = new AnnotatedClassResolver().resolve(null, null);
        NamedType namedType = new NamedType(Object.class);
        HashMap<NamedType, NamedType> collectedSubtypes = new HashMap<>();
        resolver._collectAndResolve(annotatedType, namedType, null, null, collectedSubtypes);
        assertFalse(collectedSubtypes.isEmpty());
        assertTrue(collectedSubtypes.containsValue(namedType));

        collectedSubtypes.clear();
        resolver._collectAndResolveByTypeId(annotatedType, namedType, null, new HashSet<>(), new LinkedHashMap<>());
        assertFalse(collectedSubtypes.isEmpty());
        assertTrue(collectedSubtypes.containsValue(namedType));

        Collection<NamedType> combinedTypes = resolver._combineNamedAndUnnamed(Object.class, new HashSet<>(), new LinkedHashMap<>());
        assertTrue(combinedTypes.isEmpty());
    }
}