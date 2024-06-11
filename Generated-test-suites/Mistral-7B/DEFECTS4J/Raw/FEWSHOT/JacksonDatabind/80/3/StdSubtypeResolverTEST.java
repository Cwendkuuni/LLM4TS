package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.lang.reflect.Modifier;
import java.util.*;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.jsontype.impl.*;

public class StdSubtypeResolverTest {

    @Test
    public void testRegisterSubtypes() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        // Test registration with null
        assertNull(resolver._registeredSubtypes);

        // Test registration with empty array
        resolver.registerSubtypes();
        assertEquals(0, resolver._registeredSubtypes.size());

        // Test registration with single element
        NamedType type1 = new NamedType(String.class, "Type1");
        resolver.registerSubtypes(type1);
        assertEquals(1, resolver._registeredSubtypes.size());
        assertEquals(type1, resolver._registeredSubtypes.iterator().next());

        // Test registration with multiple elements
        NamedType type1_1 = new NamedType(Integer.class, "Type1_1");
        NamedType type1_2 = new NamedType(Double.class, "Type1_2");
        resolver.registerSubtypes(type1_1, type1_2);
        assertEquals(3, resolver._registeredSubtypes.size());
        assertTrue(resolver._registeredSubtypes.contains(type1));
        assertTrue(resolver._registeredSubtypes.contains(type1_1));
        assertTrue(resolver._registeredSubtypes.contains(type1_2));
    }

    @Test
    public void testCollectAndResolveSubtypesByClass() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        // Test empty collection
        MapperConfig<?> config = new MapperConfig<>();
        AnnotatedClass baseType = AnnotatedClassResolver.resolveWithoutSuperTypes(config, String.class);
        assertEmpty(resolver.collectAndResolveSubtypesByClass(config, baseType));

        // Test registering subtypes
        NamedType type1 = new NamedType(Integer.class, "Type1");
        resolver.registerSubtypes(type1);

        baseType = AnnotatedClassResolver.resolveWithoutSuperTypes(config, String.class);
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, baseType);
        assertEquals(1, subtypes.size());
        assertEquals(type1, subtypes.iterator().next());

        // Test registering multiple subtypes
        NamedType type2 = new NamedType(Double.class, "Type2");
        resolver.registerSubtypes(type1, type2);

        subtypes = resolver.collectAndResolveSubtypesByClass(config, baseType);
        assertEquals(2, subtypes.size());
        assertTrue(subtypes.contains(type1));
        assertTrue(subtypes.contains(type2));
    }

    @Test
    public void testCollectAndResolveSubtypesByClass_AnnotatedMember() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        // Test empty collection
        MapperConfig<?> config = new MapperConfig<>();
        AnnotatedMember member = AnnotatedMember.construct(String.class, "member");
        JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);
        assertEmpty(resolver.collectAndResolveSubtypesByClass(config, member, baseType));

        // Test registering subtypes
        NamedType type1 = new NamedType(Integer.class, "Type1");
        resolver.registerSubtypes(type1);

        member = AnnotatedMember.construct(String.class, "member");
        baseType = TypeFactory.defaultInstance().constructType(String.class);
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, member, baseType);
        assertEquals(1, subtypes.size());
        assertEquals(type1, subtypes.iterator().next());

        // Test registering multiple subtypes
        NamedType type2 = new NamedType(Double.class, "Type2");
        resolver.registerSubtypes(type1, type2);

        subtypes = resolver.collectAndResolveSubtypesByClass(config, member, baseType);
        assertEquals(2, subtypes.size());
        assertTrue(subtypes.contains(type1));
        assertTrue(subtypes.contains(type2));
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        // Test empty collection
        MapperConfig<?> config = new MapperConfig<>();
        AnnotatedClass baseType = AnnotatedClassResolver.resolveWithoutSuperTypes(config, String.class);
        assertEmpty(resolver.collectAndResolveSubtypesByTypeId(config, baseType));

        // Test registering subtypes
        NamedType type1 = new NamedType(Integer.class, "Type1");
        resolver.registerSubtypes(type1);

        baseType = AnnotatedClassResolver.resolveWithoutSuperTypes(config, String.class);
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(config, baseType);
        assertEquals(1, subtypes.size());
        assertEquals(type1, subtypes.iterator().next());

        // Test registering multiple subtypes
        NamedType type2 = new NamedType(Double.class, "Type2");
        resolver.registerSubtypes(type1, type2);

        subtypes = resolver.collectAndResolveSubtypesByTypeId(config, baseType);
        assertEquals(2, subtypes.size());
        assertTrue(subtypes.contains(type1));
        assertTrue(subtypes.contains(type2));
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId_AnnotatedMember() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        // Test empty collection
        MapperConfig<?> config = new MapperConfig<>();
        AnnotatedMember member = AnnotatedMember.construct(String.class, "member");
        JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);
        assertEmpty(resolver.collectAndResolveSubtypesByTypeId(config, member, baseType));

        // Test registering subtypes
        NamedType type1 = new NamedType(Integer.class, "Type1");
        resolver.registerSubtypes(type1);

        member = AnnotatedMember.construct(String.class, "member");
        baseType = TypeFactory.defaultInstance().constructType(String.class);
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(config, member, baseType);
        assertEquals(1, subtypes.size());
        assertEquals(type1, subtypes.iterator().next());

        // Test registering multiple subtypes
        NamedType type2 = new NamedType(Double.class, "Type2");
        resolver.registerSubtypes(type1, type2);

        subtypes = resolver.collectAndResolveSubtypesByTypeId(config, member, baseType);
        assertEquals(2, subtypes.size());
        assertTrue(subtypes.contains(type1));
        assertTrue(subtypes.contains(type2));
    }
}