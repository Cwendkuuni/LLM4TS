package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
import com.fasterxml.jackson.databind.jsontype.impl.StdSubtypeResolver;
import com.fasterxml.jackson.databind.jsontype.impl.AnnotatedClassResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class StdSubtypeResolverTest {

    /**
     * Test for the registerSubtypes method.
     */
    @Test
    public void testRegisterSubtypes() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        // Test with an empty array
        resolver.registerSubtypes();
        assertTrue(resolver._registeredSubtypes == null);

        // Test with an non-empty array
        NamedType type1 = new NamedType(String.class, "StringSubtype1");
        NamedType type2 = new NamedType(Integer.class, "IntSubtype1");
        resolver.registerSubtypes(type1, type2);
        LinkedHashSet<NamedType> registeredSubtypes = resolver._registeredSubtypes;
        assertEquals(2, registeredSubtypes.size());
        assertTrue(registeredSubtypes.contains(type1));
        assertTrue(registeredSubtypes.contains(type2));

        // Test with an array of classes
        Class<?> clazz1 = String.class;
        Class<?> clazz2 = Integer.class;
        resolver.registerSubtypes(clazz1, clazz2);
        LinkedHashSet<NamedType> registeredSubtypes2 = resolver._registeredSubtypes;
        assertEquals(2, registeredSubtypes2.size());
        NamedType registeredType1 = registeredSubtypes2.iterator().next();
        assertEquals(clazz1, registeredType1.getType());
        assertEquals("StringSubtype1", registeredType1.getName());
        NamedType registeredType2 = registeredSubtypes2.iterator().next();
        assertEquals(clazz2, registeredType2.getType());
        assertNull(registeredType2.getName());
    }

    /**
     * Test for the collectAndResolveSubtypesByClass method.
     */
    @Test
    public void testCollectAndResolveSubtypesByClass() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        // Test with an empty set of registered subtypes
        MapperConfig<Object> config = new MapperConfig.Builder().build();
        AnnotationIntrospector annotationIntrospector = config.getAnnotationIntrospector();
        JavaType baseType = JavaType.construct(String[].class);
        Class<?> rawBase = baseType.getRawClass();
        List<NamedType> expectedSubtypes = Arrays.asList(
                new NamedType(String[].class),
                new NamedType(ArrayList.class),
                new NamedType(LinkedHashSet.class),
                new NamedType(List.class),
                new NamedType(Collection.class)
        );
        List<NamedType> collectedSubtypes = resolver.collectAndResolveSubtypesByClass(config, null, baseType);
        assertEquals(expectedSubtypes.size(), collectedSubtypes.size());
        for (NamedType expectedType : expectedSubtypes) {
            assertTrue(collectedSubtypes.contains(expectedType));
        }

        // Test with a non-empty set of registered subtypes
        resolver.registerSubtypes(Arrays.asList(
                new NamedType(String[].class, "ArraySubtype1"),
                new NamedType(ArrayList.class, "ListSubtype1"),
                new NamedType(LinkedHashSet.class, "SetSubtype1"),
                new NamedType(List.class, "ListSubtype2"),
                new NamedType(Collection.class, "CollectionSubtype1")
        ));
        MapperConfig<Object> config2 = new MapperConfig.Builder().build();
        AnnotationIntrospector annotationIntrospector2 = config2.getAnnotationIntrospector();
        JavaType baseType2 = JavaType.construct(List.class);
        List<NamedType> collectedSubtypes2 = resolver.collectAndResolveSubtypesByClass(config2, null, baseType2);
        List<NamedType> expectedSubtypes2 = Arrays.asList(
                new NamedType(ArrayList.class, "ListSubtype1"),
                new NamedType(LinkedHashSet.class, "SetSubtype1"),
                new NamedType(List.class, "ListSubtype2"),
                new NamedType(Collection.class, "CollectionSubtype1"),
                new NamedType(ArrayList.class, "ArraySubtype1"),
                new NamedType(List.class, "ListSubtype1"),
                new NamedType(LinkedHashSet.class, "SetSubtype1"),
                new NamedType(List.class, "ListSubtype2"),
                new NamedType(Collection.class, "CollectionSubtype1")
        );
        assertEquals(expectedSubtypes2.size(), collectedSubtypes2.size());
        for (NamedType expectedType : expectedSubtypes2) {
            assertTrue(collectedSubtypes2.contains(expectedType));
        }
    }

    /**
     * Test for the collectAndResolveSubtypesByTypeId method.
     */
    @Test
    public void testCollectAndResolveSubtypesByTypeId() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        // Test with no registered subtypes
        MapperConfig<Object> config = new MapperConfig.Builder().build();
        AnnotationIntrospector annotationIntrospector = config.getAnnotationIntrospector();
        JavaType baseType = JavaType.construct(String[].class);
        Map<String, NamedType> expectedSubtypesByName = new LinkedHashMap<>();
        expectedSubtypesByName.put("StringSubtype1", new NamedType(String[].class, "StringSubtype1"));
        expectedSubtypesByName.put("ArraySubtype1", new NamedType(String[].class, "ArraySubtype1"));
        List<NamedType> collectedSubtypes = resolver.collectAndResolveSubtypesByTypeId(config, null, baseType);
        assertEquals(expectedSubtypesByName.size(), collectedSubtypes.size());
        for (NamedType expectedType : expectedSubtypesByName.values()) {
            assertTrue(collectedSubtypes.contains(expectedType));
        }

        // Test with registered subtypes
        resolver.registerSubtypes(Arrays.asList(
                new NamedType(String[].class, "ArraySubtype1"),
                new NamedType(ArrayList.class, "ListSubtype1"),
                new NamedType(LinkedHashSet.class, "SetSubtype1"),
                new NamedType(List.class, "ListSubtype2"),
                new NamedType(Collection.class, "CollectionSubtype1")
        ));
        MapperConfig<Object> config2 = new MapperConfig.Builder().build();
        AnnotationIntrospector annotationIntrospector2 = config2.getAnnotationIntrospector();
        JavaType baseType2 = JavaType.construct(List.class);
        Map<String, NamedType> expectedSubtypesByName2 = new LinkedHashMap<>();
        expectedSubtypesByName2.put("ListSubtype1", new NamedType(ArrayList.class, "ListSubtype1"));
        expectedSubtypesByName2.put("SetSubtype1", new NamedType(LinkedHashSet.class, "SetSubtype1"));
        expectedSubtypesByName2.put("ListSubtype2", new NamedType(List.class, "ListSubtype2"));
        expectedSubtypesByName2.put("CollectionSubtype1", new NamedType(Collection.class, "CollectionSubtype1"));
        expectedSubtypesByName2.put("ArraySubtype1", new NamedType(ArrayList.class, "ArraySubtype1"));
        expectedSubtypesByName2.put("ListSubtype1", new NamedType(ArrayList.class, "ListSubtype1"));
        expectedSubtypesByName2.put("SetSubtype1", new NamedType(LinkedHashSet.class, "SetSubtype1"));
        expectedSubtypesByName2.put("ListSubtype2", new NamedType(List.class, "ListSubtype2"));
        expectedSubtypesByName2.put("CollectionSubtype1", new NamedType(Collection.class, "CollectionSubtype1"));
        List<NamedType> collectedSubtypes2 = resolver.collectAndResolveSubtypesByTypeId(config2, null, baseType2);
        assertEquals(expectedSubtypesByName2.size(), collectedSubtypes2.size());
        for (NamedType expectedType : expectedSubtypesByName2.values()) {
            assertTrue(collectedSubtypes2.contains(expectedType));
        }
    }

    /**
     * Test for the _collectAndResolve method.
     */
    @Test
    public void testCollectAndResolve() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        // Test with an empty registered subtypes set
        Class<?> baseType = String[].class;
        AnnotatedClass annotatedType = AnnotatedClassResolver.resolveWithoutSuperTypes(new MapperConfig.Builder().build(), baseType);
        NamedType namedType = new NamedType(baseType, null);
        resolver._collectAndResolve(annotatedType, namedType, new MapperConfig.Builder().build(), null, Collections.emptyMap());
        assertEquals(1, resolver._registeredSubtypes.size());
        assertTrue(resolver._registeredSubtypes.contains(namedType));

        // Test with registered subtypes
        resolver.registerSubtypes(Arrays.asList(
                new NamedType(String[].class, "ArraySubtype1"),
                new NamedType(ArrayList.class, "ListSubtype1"),
                new NamedType(LinkedHashSet.class, "SetSubtype1"),
                new NamedType(List.class, "ListSubtype2"),
                new NamedType(Collection.class, "CollectionSubtype1")
        ));
        Class<?> baseType2 = List.class;
        AnnotatedClass annotatedType2 = AnnotatedClassResolver.resolveWithoutSuperTypes(new MapperConfig.Builder().build(), baseType2);
        NamedType namedType2 = new NamedType(baseType2, null);
        resolver._collectAndResolve(annotatedType2, namedType2, new MapperConfig.Builder().build(), null, Collections.emptyMap());
        LinkedHashSet<NamedType> registeredSubtypes = resolver._registeredSubtypes;
        assertEquals(6, registeredSubtypes.size());
        assertTrue(registeredSubtypes.contains(new NamedType(ArrayList.class, "ArraySubtype1")));
        assertTrue(registeredSubtypes.contains(new NamedType(ArrayList.class, "ListSubtype1")));
        assertTrue(registeredSubtypes.contains(new NamedType(LinkedHashSet.class, "SetSubtype1")));
        assertTrue(registeredSubtypes.contains(new NamedType(List.class, "ListSubtype2")));
        assertTrue(registeredSubtypes.contains(new NamedType(Collection.class, "CollectionSubtype1")));
        assertTrue(registeredSubtypes.contains(new NamedType(ArrayList.class, "ListSubtype1")));
    }
}