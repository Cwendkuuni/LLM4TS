package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.lang.reflect.Modifier;
import java.util.*;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.cfg.MapperFactory;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.jsontype.impl.*;

public class StdSubtypeResolverTest {

    @Test
    public void testRegisterSubtypes() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        // Test registration with null
        resolver.registerSubtypes(null);
        assertEquals(0, resolver._registeredSubtypes.size());

        // Test registration with empty array
        resolver.registerSubtypes(new NamedType[0]);
        assertEquals(0, resolver._registeredSubtypes.size());

        // Test registration with single element
        NamedType type1 = new NamedType(String.class, "StringType1");
        resolver.registerSubtypes(type1);
        assertEquals(1, resolver._registeredSubtypes.size());
        assertTrue(resolver._registeredSubtypes.contains(type1));

        // Test registration with multiple elements
        NamedType type1 = new NamedType(String.class, "StringType1");
        NamedType type2 = new NamedType(Integer.class, "IntType1");
        resolver.registerSubtypes(type1, type2);
        assertEquals(2, resolver._registeredSubtypes.size());
        assertTrue(resolver._registeredSubtypes.contains(type1));
        assertTrue(resolver._registeredSubtypes.contains(type2));
    }

    @Test
    public void testCollectAndResolveSubtypesByClass() {
        MapperFactory mapperFactory = new MapperFactory();
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        // Test registration with empty set
        resolver.registerSubtypes();
        AnnotatedClass ac = AnnotatedClassResolver.resolveWithoutSuperTypes(mapperFactory, String.class);
        assertEquals(0, resolver.collectAndResolveSubtypesByClass(mapperFactory, ac).size());

        // Test registration with single element
        NamedType type1 = new NamedType(String.class, "StringType1");
        resolver.registerSubtypes(type1);
        ac = AnnotatedClassResolver.resolveWithoutSuperTypes(mapperFactory, String.class);
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(mapperFactory, ac);
        assertEquals(1, subtypes.size());
        assertTrue(subtypes.contains(type1));

        // Test registration with multiple elements
        NamedType type1 = new NamedType(String.class, "StringType1");
        NamedType type2 = new NamedType(Integer.class, "IntType1");
        resolver.registerSubtypes(type1, type2);
        ac = AnnotatedClassResolver.resolveWithoutSuperTypes(mapperFactory, String.class);
        subtypes = resolver.collectAndResolveSubtypesByClass(mapperFactory, ac);
        assertEquals(2, subtypes.size());
        assertTrue(subtypes.contains(type1));
        assertTrue(subtypes.contains(type2));

        // Test registration with type annotation
        @JsonTypeName("StringType2")
        class StringClassWithAnnotation {
        }
        resolver.registerSubtypes(StringClassWithAnnotation.class);
        ac = AnnotatedClassResolver.resolveWithoutSuperTypes(mapperFactory, StringClassWithAnnotation.class);
        subtypes = resolver.collectAndResolveSubtypesByClass(mapperFactory, ac);
        assertEquals(1, subtypes.size());
        assertTrue(subtypes.iterator().next().getName().equals("StringType2"));
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId() {
        MapperFactory mapperFactory = new MapperFactory();
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        // Test registration with empty set
        resolver.registerSubtypes();
        AnnotatedClass ac = AnnotatedClassResolver.resolveWithoutSuperTypes(mapperFactory, String.class);
        assertEquals(0, resolver.collectAndResolveSubtypesByTypeId(mapperFactory, ac).size());

        // Test registration with single element
        NamedType type1 = new NamedType(String.class, "StringType1");
        resolver.registerSubtypes(type1);
        ac = AnnotatedClassResolver.resolveWithoutSuperTypes(mapperFactory, String.class);
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(mapperFactory, ac);
        assertEquals(1, subtypes.size());
        assertTrue(subtypes.contains(type1));

        // Test registration with multiple elements
        NamedType type1 = new NamedType(String.class, "StringType1");
        NamedType type2 = new NamedType(Integer.class, "IntType1");
        resolver.registerSubtypes(type1, type2);
        ac = AnnotatedClassResolver.resolveWithoutSuperTypes(mapperFactory, String.class);
        subtypes = resolver.collectAndResolveSubtypesByTypeId(mapperFactory, ac);
        assertEquals(2, subtypes.size());
        assertTrue(subtypes.contains(type1));
        assertTrue(subtypes.contains(type2));

        // Test registration with type annotation
        @JsonTypeName("StringType2")
        class StringClassWithAnnotation {
        }
        resolver.registerSubtypes(StringClassWithAnnotation.class);
        ac = AnnotatedClassResolver.resolveWithoutSuperTypes(mapperFactory, StringClassWithAnnotation.class);
        subtypes = resolver.collectAndResolveSubtypesByTypeId(mapperFactory, ac);
        assertEquals(1, subtypes.size());
        assertTrue(subtypes.iterator().next().getName().equals("StringType2"));
    }
}