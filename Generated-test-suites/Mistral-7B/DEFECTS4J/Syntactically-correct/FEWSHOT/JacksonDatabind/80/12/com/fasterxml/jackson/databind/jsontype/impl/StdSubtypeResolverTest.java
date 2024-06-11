package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.lang.reflect.Modifier;
import java.util.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.jsontype.impl.*;

public class StdSubtypeResolverTest {

    @Test
    public void testRegisterSubtypes() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        Class<?> baseType = Object.class;
        Class<?> subtype1 = String.class;
        Class<?> subtype2 = Integer.class;

        resolver.registerSubtypes(new NamedType(subtype1));
        resolver.registerSubtypes(new NamedType(subtype2));

        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(null, null, baseType);
        assertEquals(2, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(subtype1)));
        assertTrue(subtypes.contains(new NamedType(subtype2)));

        resolver.registerSubtypes(new NamedType(subtype1, "StringSubtype"));
        subtypes = resolver.collectAndResolveSubtypesByClass(null, null, baseType);
        assertEquals(2, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(subtype1, "StringSubtype")));
        assertTrue(subtypes.contains(new NamedType(subtype2)));
    }

    @Test
    public void testRegisterSubtypesWithAnnotations() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        Class<?> baseType = Object.class;
        Class<?> subtype1 = String.class;
        Class<?> subtype2 = Integer.class;

        subtype1 = AnnotatedClass.wrap(String[].class);
        subtype1.getAnnotationMembers().add(new JsonTypeName("StringSubtype"));

        resolver.registerSubtypes(subtype1.getRawType());

        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(null, null, baseType);
        assertEquals(2, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(subtype1, "StringSubtype")));
        assertTrue(subtypes.contains(new NamedType(subtype2)));
    }

    @Test
    public void testCollectAndResolveSubtypesByClass() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        Class<?> baseType = Object.class;
        Class<?> subtype1 = String.class;
        Class<?> subtype2 = Integer.class;

        resolver.registerSubtypes(new NamedType(subtype1));
        resolver.registerSubtypes(new NamedType(subtype2));

        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(null, null, baseType);
        assertEquals(2, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(subtype1)));
        assertTrue(subtypes.contains(new NamedType(subtype2)));

        subtypes = resolver.collectAndResolveSubtypesByClass(null, null, String[].class);
        assertEquals(1, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(subtype1, "StringSubtype")));
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        Class<?> baseType = Object.class;
        Class<?> subtype1 = String.class;
        Class<?> subtype2 = Integer.class;

        resolver.registerSubtypes(new NamedType(subtype1));
        resolver.registerSubtypes(new NamedType(subtype2));

        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(null, null, baseType);
        assertEquals(2, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(subtype1)));
        assertTrue(subtypes.contains(new NamedType(subtype2)));

        subtypes = resolver.collectAndResolveSubtypesByTypeId(null, null, String[].class);
        assertEquals(1, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(subtype1, "StringSubtype")));
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdWithAnnotations() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        Class<?> baseType = Object.class;
        Class<?> subtype1 = String.class;
        Class<?> subtype2 = Integer.class;

        subtype1 = AnnotatedClass.wrap(String[].class);
        subtype1.getAnnotationMembers().add(new JsonTypeName("StringSubtype"));

        resolver.registerSubtypes(subtype1.getRawType());

        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(null, null, baseType);
        assertEquals(2, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(subtype1, "StringSubtype")));
        assertTrue(subtypes.contains(new NamedType(subtype2)));

        subtypes = resolver.collectAndResolveSubtypesByTypeId(null, null, String[].class);
        assertEquals(1, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(subtype1, "StringSubtype")));
    }

    @Test
    public void testCombineNamedAndUnnamed() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        Class<?> baseType = Object.class;
        Class<?> subtype1 = String.class;
        Class<?> subtype2 = Integer.class;
        Class<?> subtype3 = List.class;
        Class<?> subtype4 = ArrayList.class;

        resolver.registerSubtypes(new NamedType(subtype1));
        resolver.registerSubtypes(new NamedType(subtype2));
        resolver.registerSubtypes(subtype3);
        resolver.registerSubtypes(subtype4);

        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(null, null, baseType);
        assertEquals(7, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(subtype1)));
        assertTrue(subtypes.contains(new NamedType(subtype2)));
        assertTrue(subtypes.contains(new NamedType(subtype3)));
        assertTrue(subtypes.contains(new NamedType(subtype4)));
        assertTrue(subtypes.contains(new NamedType(String[].class)));
        assertTrue(subtypes.contains(new NamedType(ArrayList.class)));
        assertTrue(subtypes.contains(new NamedType(List.class)));

        subtypes = resolver.collectAndResolveSubtypesByTypeId(null, null, baseType);
        assertEquals(10, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(subtype1)));
        assertTrue(subtypes.contains(new NamedType(subtype2)));
        assertTrue(subtypes.contains(new NamedType(subtype3)));
        assertTrue(subtypes.contains(new NamedType(subtype4)));
        assertTrue(subtypes.contains(new NamedType(String[].class, "StringArray")));
        assertTrue(subtypes.contains(new NamedType(ArrayList.class, "ArrayList")));
        assertTrue(subtypes.contains(new NamedType(List.class, "List")));
        assertTrue(subtypes.contains(new NamedType(Object[].class)));
        assertTrue(subtypes.contains(new NamedType(Object.class)));
    }
}