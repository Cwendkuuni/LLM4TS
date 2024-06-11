package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

import static org.junit.Assert.*;

public class StdSubtypeResolverTest {

    private StdSubtypeResolver subtypeResolver;

    @Before
    public void setUp() {
        subtypeResolver = new StdSubtypeResolver();
    }

    @Test
    public void testRegisterSubtypes() {
        NamedType[] types = {new NamedType(A.class), new NamedType(B.class)};
        subtypeResolver.registerSubtypes(types);

        LinkedHashSet<NamedType> expected = new LinkedHashSet<>();
        expected.add(new NamedType(A.class));
        expected.add(new NamedType(B.class));

        assertEquals(expected, subtypeResolver._registeredSubtypes);
    }

    @Test
    public void testRegisterSubtypesWithClasses() {
        subtypeResolver.registerSubtypes(A.class, B.class);

        LinkedHashSet<NamedType> expected = new LinkedHashSet<>();
        expected.add(new NamedType(A.class));
        expected.add(new NamedType(B.class));

        assertEquals(expected, subtypeResolver._registeredSubtypes);
    }

    @Test
    public void testCollectAndResolveSubtypesByClassWithConfigPropertyAndBaseType() {
        MapperConfig mapperConfig = new MapperConfig();
        JavaType baseType = new JavaType(A.class);

        Collection<NamedType> actual = subtypeResolver.collectAndResolveSubtypesByClass(mapperConfig, null, baseType);

        LinkedHashSet<NamedType> expected = new LinkedHashSet<>();
        expected.add(new NamedType(A.class));

        assertEquals(expected, actual);
    }

    @Test
    public void testCollectAndResolveSubtypesByClassWithConfigAndAnnotatedMember() {
        MapperConfig mapperConfig = new MapperConfig();
        AnnotatedMember property = new AnnotatedMember();
        JavaType baseType = new JavaType(A.class);

        Collection<NamedType> actual = subtypeResolver.collectAndResolveSubtypesByClass(mapperConfig, property, baseType);

        LinkedHashSet<NamedType> expected = new LinkedHashSet<>();
        expected.add(new NamedType(A.class));

        assertEquals(expected, actual);
    }

    @Test
    public void testCollectAndResolveSubtypesByClassWithConfigAndAnnotatedClass() {
        MapperConfig mapperConfig = new MapperConfig();
        AnnotatedClass type = new AnnotatedClass();

        Collection<NamedType> actual = subtypeResolver.collectAndResolveSubtypesByClass(mapperConfig, type);

        LinkedHashSet<NamedType> expected = new LinkedHashSet<>();
        expected.add(new NamedType(type.getRawType()));

        assertEquals(expected, actual);
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdWithConfigPropertyAndBaseType() {
        MapperConfig mapperConfig = new MapperConfig();
        JavaType baseType = new JavaType(A.class);
        AnnotatedMember property = new AnnotatedMember();

        Collection<NamedType> actual = subtypeResolver.collectAndResolveSubtypesByTypeId(mapperConfig, property, baseType);

        LinkedHashSet<NamedType> expected = new LinkedHashSet<>();
        expected.add(new NamedType(A.class));

        assertEquals(expected, actual);
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdWithConfigAndAnnotatedClass() {
        MapperConfig mapperConfig = new MapperConfig();
        AnnotatedClass baseType = new AnnotatedClass();

        Collection<NamedType> actual = subtypeResolver.collectAndResolveSubtypesByTypeId(mapperConfig, baseType);

        LinkedHashSet<NamedType> expected = new LinkedHashSet<>();
        expected.add(new NamedType(baseType.getRawType()));

        assertEquals(expected, actual);
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdWithConfigAndAnnotatedClassAndRegisterSubtypes() {
        MapperConfig mapperConfig = new MapperConfig();
        AnnotatedClass baseType = new AnnotatedClass();

        subtypeResolver.registerSubtypes(A.class, B.class);

        Collection<NamedType> actual = subtypeResolver.collectAndResolveSubtypesByTypeId(mapperConfig, baseType);

        LinkedHashSet<NamedType> expected = new LinkedHashSet<>();
        expected.add(new NamedType(A.class));
        expected.add(new NamedType(B.class));

        assertEquals(expected, actual);
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdWithConfigAndAnnotatedClassAndBaseTypeAndRegisterSubtypes() {
        MapperConfig mapperConfig = new MapperConfig();
        AnnotatedClass baseType = new AnnotatedClass();
        JavaType rawBase = new JavaType(C.class);

        subtypeResolver.registerSubtypes(A.class, B.class);

        Collection<NamedType> actual = subtypeResolver.collectAndResolveSubtypesByTypeId(mapperConfig, baseType);

        LinkedHashSet<NamedType> expected = new LinkedHashSet<>();
        expected.add(new NamedType(A.class));
        expected.add(new NamedType(B.class));

        assertEquals(expected, actual);
    }

    private static class A {}
    private static class B {}
    private static class C {}
}