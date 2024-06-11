package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.*;

import java.util.*;

public class StdSubtypeResolverTest {

    @Test
    public void testRegisterSubtypes() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        Class<?> baseType = Object.class;
        Class<?> subtype1 = String.class;
        Class<?> subtype2 = Integer.class;

        resolver.registerSubtypes(subtype1, subtype2);

        AnnotationIntrospector ai = createMockAnnotationIntrospector();
        MapperConfig<?> config = createMockMapperConfig();

        AnnotatedClass baseClass = AnnotatedClassResolver.resolveWithoutSuperTypes(config, baseType);
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, baseClass);

        assertEquals(2, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(subtype1, Id.CLASS)));
        assertTrue(subtypes.contains(new NamedType(subtype2, Id.CLASS)));
    }

    @Test
    public void testRegisterSubtypesClassArray() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        Class<?> baseType = Object.class;
        Class<?> subtype1 = String.class;
        Class<?> subtype2 = Integer.class;

        resolver.registerSubtypes(subtype1, subtype2);

        AnnotationIntrospector ai = createMockAnnotationIntrospector();
        MapperConfig<?> config = createMockMapperConfig();

        AnnotatedClass baseClass = AnnotatedClassResolver.resolveWithoutSuperTypes(config, baseType);
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, baseClass);

        assertEquals(2, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(subtype1, Id.CLASS)));
        assertTrue(subtypes.contains(new NamedType(subtype2, Id.CLASS)));
    }

    @Test
    public void testCollectAndResolveSubtypesByClass() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        Class<?> baseType = Object.class;
        Class<?> subtype1 = String.class;
        Class<?> subtype2 = Integer.class;

        // register subtypes for testing
        resolver.registerSubtypes(subtype1, subtype2);

        AnnotationIntrospector ai = createMockAnnotationIntrospector();
        MapperConfig<?> config = createMockMapperConfig();

        AnnotatedClass baseClass = AnnotatedClassResolver.resolveWithoutSuperTypes(config, baseType);
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, baseClass);

        assertEquals(2, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(subtype1, Id.CLASS)));
        assertTrue(subtypes.contains(new NamedType(subtype2, Id.CLASS)));

        // test with empty collection
        Collection<NamedType> emptySubtypes = Collections.emptyList();
        Collection<NamedType> subtypes2 = resolver.collectAndResolveSubtypesByClass(config, baseClass, emptySubtypes);
        assertTrue(subtypes2.isEmpty());
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        Class<?> baseType = Object.class;
        Class<?> subtype1 = String.class;
        Class<?> subtype2 = Integer.class;

        // register subtypes for testing
        resolver.registerSubtypes(subtype1, subtype2);

        AnnotationIntrospector ai = createMockAnnotationIntrospector();
        MapperConfig<?> config = createMockMapperConfig();

        AnnotatedMember member = createMockAnnotatedMember();
        JavaType baseTypeJavaType = createMockJavaType(baseType);

        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(config, member, baseTypeJavaType);

        assertEquals(2, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(subtype1, Id.CLASS)));
        assertTrue(subtypes.contains(new NamedType(subtype2, Id.CLASS)));

        // test with empty collection
        Collection<NamedType> emptySubtypes = Collections.emptyList();
        Collection<NamedType> subtypes2 = resolver.collectAndResolveSubtypesByTypeId(config, member, baseTypeJavaType, emptySubtypes);
        assertTrue(subtypes2.isEmpty());
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdAnnotatedClass() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        Class<?> baseType = Object.class;
        Class<?> subtype1 = String.class;
        Class<?> subtype2 = Integer.class;

        // register subtypes for testing
        resolver.registerSubtypes(subtype1, subtype2);

        AnnotationIntrospector ai = createMockAnnotationIntrospector();
        MapperConfig<?> config = createMockMapperConfig();

        AnnotatedClass baseClass = createMockAnnotatedClass(baseType);

        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(config, baseClass);

        assertEquals(2, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(subtype1, Id.CLASS)));
        assertTrue(subtypes.contains(new NamedType(subtype2, Id.CLASS)));

        // test with empty collection
        Collection<NamedType> emptySubtypes = Collections.emptyList();
        Collection<NamedType> subtypes2 = resolver.collectAndResolveSubtypesByTypeId(config, baseClass, emptySubtypes);
        assertTrue(subtypes2.isEmpty());
    }

    private AnnotatedMember createMockAnnotatedMember() {
        return null; // Mockito.mock(AnnotatedMember.class);
    }

    private AnnotatedClass createMockAnnotatedClass(Class<?> type) {
        return null; // Mockito.mock(AnnotatedClass.class);
    }

    private JavaType createMockJavaType(Class<?> type) {
        return null; // Mockito.mock(JavaType.class);
    }

    private MapperConfig<?> createMockMapperConfig() {
        return null; // Mockito.mock(MapperConfig.class);
    }

    private AnnotationIntrospector createMockAnnotationIntrospector() {
        return null; // Mockito.mock(AnnotationIntrospector.class);
    }
}