package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class StdSubtypeResolverTest {

    @Test
    public void testEmptyRegistration() throws Exception {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        MapperConfig config = new MapperConfig();
        JavaType baseType = JavaType.constructUnTypedType(String.class);
        List<NamedType> result = resolver.collectAndResolveSubtypesByClass(config, null, baseType);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testRegistrationByClass() throws Exception {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        MapperConfig config = new MapperConfig();
        JavaType baseType = JavaType.constructUnTypedType(String.class);

        // Register empty set
        resolver.registerSubtypes();
        List<NamedType> result1 = resolver.collectAndResolveSubtypesByClass(config, null, baseType);
        assertTrue(result1.isEmpty());

        // Register concrete types
        resolver.registerSubtypes(new NamedType(String[].class), new NamedType(Integer[].class));
        List<NamedType> result2 = resolver.collectAndResolveSubtypesByClass(config, null, baseType);
        List<NamedType> expected = Arrays.asList(
                new NamedType(String[].class), new NamedType(Integer[].class));
        assertEquals(expected, result2);

        // Register abstract type
        resolver.registerSubtypes(new NamedType(List.class));
        List<NamedType> result3 = resolver.collectAndResolveSubtypesByClass(config, null, baseType);
        List<NamedType> expected3 = Arrays.asList(
                new NamedType(String[].class), new NamedType(Integer[].class), new NamedType(List.class));
        assertEquals(expected3, result3);

        // Register overlapping types
        resolver.registerSubtypes(new NamedType(List.class), new NamedType(Integer[].class));
        List<NamedType> result4 = resolver.collectAndResolveSubtypesByClass(config, null, baseType);
        List<NamedType> expected4 = Arrays.asList(
                new NamedType(String[].class), new NamedType(List.class), new NamedType(Integer[].class));
        assertEquals(expected4, result4);
    }

    @Test
    public void testRegistrationByName() throws Exception {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        MapperConfig config = new MapperConfig();
        JavaType baseType = JavaType.constructUnTypedType(String.class);

        // Register empty set
        resolver.registerSubtypes();
        List<NamedType> result1 = resolver.collectAndResolveSubtypesByClass(config, null, baseType);
        assertTrue(result1.isEmpty());

        // Register concrete types by class name
        resolver.registerSubtypes("java.util.List", "java.lang.Integer[]");
        List<NamedType> result2 = resolver.collectAndResolveSubtypesByClass(config, null, baseType);
        List<NamedType> expected = Arrays.asList(
                new NamedType(List.class), new NamedType(Integer[].class));
        assertEquals(expected, result2);

        // Register abstract type by name
        resolver.registerSubtypes("java.util.List");
        List<NamedType> result3 = resolver.collectAndResolveSubtypesByClass(config, null, baseType);
        List<NamedType> expected3 = Arrays.asList(
                new NamedType(List.class), new NamedType(String[].class), new NamedType(Integer[].class));
        assertEquals(expected3, result3);

        // Register overlapping types by name
        resolver.registerSubtypes("java.util.List", "java.lang.Integer[]");
        List<NamedType> result4 = resolver.collectAndResolveSubtypesByClass(config, null, baseType);
        List<NamedType> expected4 = Arrays.asList(
                new NamedType(List.class), new NamedType(Integer[].class), new NamedType(String[].class));
        assertEquals(expected4, result4);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegistrationWithNullTypes() throws Exception {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        resolver.registerSubtypes(null);
    }

    @Test
    public void testRegistrationWithDuplicateTypes() throws Exception {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        MapperConfig config = new MapperConfig();
        JavaType baseType = JavaType.constructUnTypedType(String.class);

        // Register duplicate types
        resolver.registerSubtypes(new NamedType(Integer[].class), new NamedType(Integer[].class));
        List<NamedType> result = resolver.collectAndResolveSubtypesByClass(config, null, baseType);
        List<NamedType> expected = Arrays.asList(
                new NamedType(Integer[].class));
        assertEquals(expected, result);
    }

    @Test
    public void testEmptyCollection() throws Exception {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        MapperConfig config = new MapperConfig();
        JavaType baseType = JavaType.constructUnTypedType(String.class);

        List<NamedType> result = resolver.collectAndResolveSubtypesByClass(config, null, baseType);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testEmptyCollectionWithAnnotation() throws Exception {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        MapperConfig config = new MapperConfig();
        JavaType baseType = JavaType.constructUnTypedType(String.class);

        AnnotatedMember property = new AnnotatedMember(new AnnotatedClass(String.class), "property");
        List<NamedType> result = resolver.collectAndResolveSubtypesByClass(config, property, baseType);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testSingleSubtypeAnnotation() throws Exception {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        MapperConfig config = new MapperConfig();
        JavaType baseType = JavaType.constructUnTypedType(String.class);

        AnnotatedMember property = new AnnotatedMember(new AnnotatedClass(String.class), "property");
        resolver.registerSubtypes(new NamedType(Integer.class));
        List<NamedType> result = resolver.collectAndResolveSubtypesByClass(config, property, baseType);
        List<NamedType> expected = Arrays.asList(new NamedType(Integer.class));
        assertEquals(expected, result);
    }

    @Test
    public void testMultipleSubtypesAnnotation() throws Exception {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        MapperConfig config = new MapperConfig();
        JavaType baseType = JavaType.constructUnTypedType(String.class);

        AnnotatedMember property = new AnnotatedMember(new AnnotatedClass(String.class), "property");
        resolver.registerSubtypes(new NamedType(Integer.class), new NamedType(Double.class));
        List<NamedType> result = resolver.collectAndResolveSubtypesByClass(config, property, baseType);
        List<NamedType> expected = Arrays.asList(new NamedType(Integer.class), new NamedType(Double.class));
        assertEquals(expected, result);
    }

    @Test
    public void testEmptyCollectionWithAnnotationAndRegisteredTypes() throws Exception {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        MapperConfig config = new MapperConfig();
        JavaType baseType = JavaType.constructUnTypedType(String.class);

        AnnotatedMember property = new AnnotatedMember(new AnnotatedClass(String.class), "property");
        resolver.registerSubtypes(new NamedType(Integer.class), new NamedType(Double.class));
        List<NamedType> result = resolver.collectAndResolveSubtypesByClass(config, property, baseType);
        List<NamedType> expected = Arrays.asList(new NamedType(Integer.class), new NamedType(Double.class));
        assertEquals(expected, result);
    }

    @Test
    public void testEmptyCollectionWithAnnotationAndRegisteredTypesByName() throws Exception {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        MapperConfig config = new MapperConfig();
        JavaType baseType = JavaType.constructUnTypedType(String.class);

        AnnotatedMember property = new AnnotatedMember(new AnnotatedClass(String.class), "property");
        resolver.registerSubtypes("java.lang.Integer", "java.lang.Double");
        List<NamedType> result = resolver.collectAndResolveSubtypesByClass(config, property, baseType);
        List<NamedType> expected = Arrays.asList(new NamedType(Integer.class), new NamedType(Double.class));
        assertEquals(expected, result);
    }

    @Test
    public void testEmptyCollectionWithAnnotationAndConflictingRegisteredTypes() throws Exception {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        MapperConfig config = new MapperConfig();
        JavaType baseType = JavaType.constructUnTypedType(String.class);

        AnnotatedMember property = new AnnotatedMember(new AnnotatedClass(String.class), "property");
        resolver.registerSubtypes("java.lang.Integer", "java.lang.Double");
        resolver.registerSubtypes("java.lang.Double", "java.lang.Integer");
        List<NamedType> result = resolver.collectAndResolveSubtypesByClass(config, property, baseType);
        List<NamedType> expected = Collections.emptyList();
        assertEquals(expected, result);
    }

    @Test
    public void testEmptyCollectionWithAnnotationAndConflictingRegisteredTypesByName() throws Exception {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        MapperConfig config = new MapperConfig();
        JavaType baseType = JavaType.constructUnTypedType(String.class);

        AnnotatedMember property = new AnnotatedMember(new AnnotatedClass(String.class), "property");
        resolver.registerSubtypes("java.lang.Integer", "java.lang.Double");
        resolver.registerSubtypes("java.lang.Double", "java.lang.Integer");
        List<NamedType> result = resolver.collectAndResolveSubtypesByClass(config, property, baseType);
        List<NamedType> expected = Collections.emptyList();
        assertEquals(expected, result);
    }
}