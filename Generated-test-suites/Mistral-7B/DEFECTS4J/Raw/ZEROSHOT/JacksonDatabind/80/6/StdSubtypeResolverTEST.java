package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperConfig;
import com.fasterxml.jackson.databind.cfg.MapperConfig.DefaultInitTracker;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.NamedType;
import com.fasterxml.jackson.databind.jsontype.JsonSubType;
import com.fasterxml.jackson.databind.jsontype.StdSubtypeResolver;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.impl.AnnotatedClassResolver;

public class StdSubtypeResolverTest {

    private StdSubtypeResolver resolver;
    private MapperConfig<?> config;

    @Before
    public void setUp() {
        resolver = new StdSubtypeResolver();
        config = new MapperConfig.StandardMapperConfig();
        config.enable(DefaultInitTracker.Builder.standardBuilder());
    }

    @Test
    public void testRegisterSubtypes() {
        Class<?> baseType = Object.class;
        Class<?> subtype1 = Integer.class;
        Class<?> subtype2 = String.class;

        resolver.registerSubtypes(new NamedType(subtype1));
        resolver.registerSubtypes(subtype2);
        resolver.registerSubtypes(new NamedType(subtype1), new NamedType(subtype2));

        Set<NamedType> registeredSubtypes = resolver.getRegisteredSubtypes();
        assertEquals(3, registeredSubtypes.size());
        assertTrue(registeredSubtypes.contains(new NamedType(subtype1)));
        assertTrue(registeredSubtypes.contains(new NamedType(subtype2)));
        assertTrue(registeredSubtypes.contains(new NamedType(subtype1, "default")));
    }

    @Test
    public void testRegisterSubtypesWithNames() {
        Class<?> baseType = Object.class;
        Class<?> subtype1 = Integer.class;
        Class<?> subtype2 = String.class;

        resolver.registerSubtypes(new NamedType(subtype1, "intSubtype"));
        resolver.registerSubtypes(new NamedType(subtype2, "stringSubtype"));

        Set<NamedType> registeredSubtypes = resolver.getRegisteredSubtypes();
        assertEquals(2, registeredSubtypes.size());
        assertTrue(registeredSubtypes.contains(new NamedType(subtype1, "intSubtype")));
        assertTrue(registeredSubtypes.contains(new NamedType(subtype2, "stringSubtype")));
    }

    @Test
    public void testRegisterSubtypesWithSameBaseType() {
        Class<?> baseType = Object.class;
        Class<?> subtype1 = Integer.class;
        Class<?> subtype2 = Integer.class;

        resolver.registerSubtypes(new NamedType(subtype1));
        resolver.registerSubtypes(new NamedType(subtype2));

        Set<NamedType> registeredSubtypes = resolver.getRegisteredSubtypes();
        assertEquals(2, registeredSubtypes.size());
        assertTrue(registeredSubtypes.contains(new NamedType(subtype1)));
        assertTrue(registeredSubtypes.contains(new NamedType(subtype2)));
    }

    @Test
    public void testCollectAndResolveSubtypesByClass() {
        Class<?> baseType = Object.class;
        Class<?> subtype1 = Integer.class;
        Class<?> subtype2 = String.class;
        Class<?> subtype3 = Float.class;

        // register subtypes
        resolver.registerSubtypes(new NamedType(subtype1));
        resolver.registerSubtypes(new NamedType(subtype2));
        resolver.registerSubtypes(new NamedType(subtype3));

        AnnotatedClass annotatedType = AnnotatedClassResolver.resolveWithoutSuperTypes(config, baseType);

        // test with empty
        List<NamedType> result1 = resolver.collectAndResolveSubtypesByClass(config, null, null);
        assertEquals(0, result1.size());

        // test with base type
        List<NamedType> result2 = resolver.collectAndResolveSubtypesByClass(config, annotatedType, null);
        assertEquals(3, result2.size());
        assertTrue(result2.contains(new NamedType(subtype1)));
        assertTrue(result2.contains(new NamedType(subtype2)));
        assertTrue(result2.contains(new NamedType(subtype3)));

        // test with property
        AnnotatedMember property = null; // to be implemented
        JavaType baseTypeProperty = null; // to be implemented

        List<NamedType> result3 = resolver.collectAndResolveSubtypesByClass(config, property, baseTypeProperty);
        assertEquals(3, result3.size());
        assertTrue(result3.contains(new NamedType(subtype1)));
        assertTrue(result3.contains(new NamedType(subtype2)));
        assertTrue(result3.contains(new NamedType(subtype3)));
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId() {
        Class<?> baseType = Object.class;
        Class<?> subtype1 = Integer.class;
        Class<?> subtype2 = String.class;
        Class<?> subtype3 = Float.class;

        // register subtypes
        resolver.registerSubtypes(new NamedType(subtype1));
        resolver.registerSubtypes(new NamedType(subtype2));
        resolver.registerSubtypes(new NamedType(subtype3));

        AnnotatedClass baseTypeAnnotated = AnnotatedClassResolver.resolveWithoutSuperTypes(config, baseType);

        // test with empty
        List<NamedType> result1 = resolver.collectAndResolveSubtypesByTypeId(config, null, null);
        assertEquals(0, result1.size());

        // test with base type
        List<NamedType> result2 = resolver.collectAndResolveSubtypesByTypeId(config, baseTypeAnnotated, null);
        assertEquals(3, result2.size());
        assertTrue(result2.contains(new NamedType(subtype1)));
        assertTrue(result2.contains(new NamedType(subtype2)));
        assertTrue(result2.contains(new NamedType(subtype3)));

        // test with property
        AnnotatedMember property = null; // to be implemented
        JavaType baseTypeProperty = null; // to be implemented

        List<NamedType> result3 = resolver.collectAndResolveSubtypesByTypeId(config, property, baseTypeProperty);
        assertEquals(3, result3.size());
        assertTrue(result3.contains(new NamedType(subtype1)));
        assertTrue(result3.contains(new NamedType(subtype2)));
        assertTrue(result3.contains(new NamedType(subtype3)));
    }

    @Test
    public void testCollectAndResolveSubtypesWithJsonTypeInfo() {
        Class<?> baseType = MyBaseType.class;
        Class<?> subtype1 = MySubtype1.class;
        Class<?> subtype2 = MySubtype2.class;

        resolver.registerSubtypes(new NamedType(subtype1));
        resolver.registerSubtypes(new NamedType(subtype2));

        AnnotatedClass baseTypeAnnotated = AnnotatedClassResolver.resolveWithoutSuperTypes(config, baseType);

        List<NamedType> result = resolver.collectAndResolveSubtypesByTypeId(config, baseTypeAnnotated, null);
        assertEquals(2, result.size());
        assertTrue(result.contains(new NamedType(subtype1, "Subtype1")));
        assertTrue(result.contains(new NamedType(subtype2, "Subtype2")));
    }

    @Test
    public void testCollectAndResolveSubtypesWithJsonTypeInfoAndName() {
        Class<?> baseType = MyBaseType.class;
        Class<?> subtype1 = MySubtype1.class;
        Class<?> subtype2 = MySubtype2.class;

        resolver.registerSubtypes(new NamedType(subtype1, "Subtype1Name"));
        resolver.registerSubtypes(new NamedType(subtype2, "Subtype2Name"));

        AnnotatedClass baseTypeAnnotated = AnnotatedClassResolver.resolveWithoutSuperTypes(config, baseType);

        List<NamedType> result = resolver.collectAndResolveSubtypesByTypeId(config, baseTypeAnnotated, null);
        assertEquals(2, result.size());
        assertTrue(result.contains(new NamedType(subtype1, "Subtype1Name")));
        assertTrue(result.contains(new NamedType(subtype2, "Subtype2Name")));
    }

    @Test
    public void testCollectAndResolveSubtypesWithJsonTypeInfoAndMultipleBaseTypes() {
        Class<?> baseType1 = MyBaseType1.class;
        Class<?> baseType2 = MyBaseType2.class;
        Class<?> subtype = MySubType.class;

        resolver.registerSubtypes(new NamedType(subtype, "SubtypeName"));

        AnnotatedClass baseType1Annotated = AnnotatedClassResolver.resolveWithoutSuperTypes(config, baseType1);
        AnnotatedClass baseType2Annotated = AnnotatedClassResolver.resolveWithoutSuperTypes(config, baseType2);

        List<NamedType> result = resolver.collectAndResolveSubtypesByTypeId(config, null, new JavaType[] { baseType1Annotated, baseType2Annotated });
        assertEquals(1, result.size());
        assertTrue(result.contains(new NamedType(subtype, "SubtypeName")));
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = As.PROPERTY, property = "type")
    static class MyBaseType {
        String type;
    }

    @JsonSubTypes({
            @JsonSubTypes.Type(value = MySubtype1.class, name = "Subtype1"),
            @JsonSubTypes.Type(value = MySubtype2.class, name = "Subtype2")
    })
    static class MySubtype1 {
        // empty
    }

    @JsonTypeName("Subtype2Name")
    static class MySubtype2 {
        // empty
    }
}