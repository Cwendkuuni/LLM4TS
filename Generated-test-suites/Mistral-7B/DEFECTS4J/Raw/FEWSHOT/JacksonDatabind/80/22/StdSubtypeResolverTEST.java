package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;

public class StdSubtypeResolverTest {

    @Test
    public void testRegisterSubtypesByClass() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        Class<?> baseType = Integer.class;
        Class<?> subtype1 = Long.class;
        Class<?> subtype2 = String.class;

        resolver.registerSubtypes(subtype1, subtype2);

        AnnotationIntrospector ai = null; // mock or use real instance
        MapperConfig<?> config = null; // mock or use real instance
        AnnotatedClass baseClass = AnnotatedClassResolver.resolveWithoutSuperTypes(config, baseType);

        List<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, baseClass);

        assertEquals(2, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(subtype1, null)));
        assertTrue(subtypes.contains(new NamedType(subtype2, null)));
    }

    @Test
    public void testRegisterSubtypesByName() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        Class<?> baseType = Integer.class;
        String subtypeName1 = "LongType";
        String subtypeName2 = "StringType";

        resolver.registerSubtypes(new NamedType(Long.class, subtypeName1));
        resolver.registerSubtypes(new NamedType(String.class, subtypeName2));

        AnnotationIntrospector ai = null; // mock or use real instance
        MapperConfig<?> config = null; // mock or use real instance
        AnnotatedClass baseClass = AnnotatedClassResolver.resolveWithoutSuperTypes(config, baseType);

        List<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, baseClass);

        assertEquals(2, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(Long.class, subtypeName1)));
        assertTrue(subtypes.contains(new NamedType(String.class, subtypeName2)));
    }

    @Test
    public void testCollectSubtypesByClassWithRegisteredSubtypes() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        Class<?> baseType = Integer.class;
        Class<?> subtype1 = Long.class;
        Class<?> subtype2 = String.class;

        resolver.registerSubtypes(subtype1, subtype2);

        AnnotationIntrospector ai = null; // mock or use real instance
        MapperConfig<?> config = null; // mock or use real instance
        AnnotatedClass baseClass = AnnotatedClassResolver.resolveWithoutSuperTypes(config, baseType);

        List<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, baseClass);

        assertEquals(2, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(subtype1, null)));
        assertTrue(subtypes.contains(new NamedType(subtype2, null)));
    }

    @Test
    public void testCollectSubtypesByClassWithAnnotatedSubtypes() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        Class<?> baseType = Integer.class;
        Class<?> subtype1 = Long.class;
        Class<?> subtype2 = String.class;

        JavaType type1 = null; // mock or use real instance
        JavaType type2 = null; // mock or use real instance
        AnnotatedMember property = null; // mock or use real instance

        AnnotationIntrospector ai = null; // mock or use real instance
        MapperConfig<?> config = null; // mock or use real instance
        AnnotatedClass baseClass = AnnotatedClassResolver.resolveWithoutSuperTypes(config, baseType);

        // Register subtypes using annotations on the base class
        AnnotatedClass subtypeClass1 = AnnotatedClassResolver.resolveWithoutSuperTypes(config, subtype1);
        JsonTypeInfo annotations1 = new JsonTypeInfo();
        annotations1.use(JsonTypeInfo.Id.CLASS);
        AnnotatedMember property1 = AnnotatedMember.wrap(subtypeClass1, "memberName");
        annotations1.propertyOrder(property1);

        AnnotatedClass subtypeClass2 = AnnotatedClassResolver.resolveWithoutSuperTypes(config, subtype2);
        JsonTypeInfo annotations2 = new JsonTypeInfo();
        annotations2.use(JsonTypeInfo.Id.NAME);
        annotations2.propertyOrder(AnnotatedMember.wrap(subtypeClass2, "memberName"));

        ai.findSubtypes(baseClass).add(new NamedType(subtypeClass1, annotations1));
        ai.findSubtypes(baseClass).add(new NamedType(subtypeClass2, annotations2));

        List<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, baseClass);

        assertEquals(2, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(subtype1, "memberName")));
        assertTrue(subtypes.contains(new NamedType(subtype2, "memberName")));
    }

    @Test
    public void testCollectSubtypesByClassWithAnnotatedSubtypesAndRegisteredSubtypes() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        Class<?> baseType = Integer.class;
        Class<?> subtype1 = Long.class;
        Class<?> subtype2 = String.class;

        resolver.registerSubtypes(subtype1, subtype2);

        AnnotationIntrospector ai = null; // mock or use real instance
        MapperConfig<?> config = null; // mock or use real instance
        AnnotatedClass baseClass = AnnotatedClassResolver.resolveWithoutSuperTypes(config, baseType);

        // Register subtypes using annotations on the base class
        Class<?> registeredSubtype1 = Float.class;
        Class<?> registeredSubtype2 = Double.class;

        AnnotatedClass registeredSubtypeClass1 = AnnotatedClassResolver.resolveWithoutSuperTypes(config, registeredSubtype1);
        JsonTypeInfo annotations1 = new JsonTypeInfo();
        annotations1.use(JsonTypeInfo.Id.CLASS);
        AnnotatedMember property1 = AnnotatedMember.wrap(registeredSubtypeClass1, "memberName");
        annotations1.propertyOrder(property1);

        AnnotatedClass registeredSubtypeClass2 = AnnotatedClassResolver.resolveWithoutSuperTypes(config, registeredSubtype2);
        JsonTypeInfo annotations2 = new JsonTypeInfo();
        annotations2.use(JsonTypeInfo.Id.NAME);
        AnnotatedMember property2 = AnnotatedMember.wrap(registeredSubtypeClass2, "memberName");
        annotations2.propertyOrder(property2);

        ai.findSubtypes(baseClass).add(new NamedType(registeredSubtypeClass1, annotations1));
        ai.findSubtypes(baseClass).add(new NamedType(registeredSubtypeClass2, annotations2));

        List<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, baseClass);

        assertEquals(4, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(subtype1, null)));
        assertTrue(subtypes.contains(new NamedType(subtype2, null)));
        assertTrue(subtypes.contains(new NamedType(registeredSubtype1, "memberName")));
        assertTrue(subtypes.contains(new NamedType(registeredSubtype2, "memberName")));
    }

    @Test
    public void testCollectSubtypesByName() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        Class<?> baseType = Integer.class;
        String subtypeName1 = "LongType";
        String subtypeName2 = "StringType";

        AnnotationIntrospector ai = null; // mock or use real instance
        MapperConfig<?> config = null; // mock or use real instance
        AnnotatedClass baseClass = AnnotatedClassResolver.resolveWithoutSuperTypes(config, baseType);

        // Register subtypes using annotations on the base class
        AnnotatedClass subtypeClass1 = AnnotatedClassResolver.resolveWithoutSuperTypes(config, Long.class);
        JsonTypeInfo annotations1 = new JsonTypeInfo();
        annotations1.use(JsonTypeInfo.Id.NAME);
        annotations1.propertyOrder(AnnotatedMember.wrap(subtypeClass1, "memberName"));
        ai.findSubtypes(baseClass).add(new NamedType(subtypeClass1, annotations1));

        AnnotatedClass subtypeClass2 = AnnotatedClassResolver.resolveWithoutSuperTypes(config, String.class);
        JsonTypeInfo annotations2 = new JsonTypeInfo();
        annotations2.use(JsonTypeInfo.Id.NAME);
        annotations2.propertyOrder(AnnotatedMember.wrap(subtypeClass2, "memberName"));
        ai.findSubtypes(baseClass).add(new NamedType(subtypeClass2, annotations2));

        List<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, baseClass);

        assertEquals(2, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(Long.class, subtypeName1)));
        assertTrue(subtypes.contains(new NamedType(String.class, subtypeName2)));
    }

    @Test
    public void testCollectSubtypesByNameWithRegisteredSubtypes() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        Class<?> baseType = Integer.class;
        String subtypeName1 = "LongType";
        String subtypeName2 = "StringType";

        resolver.registerSubtypes(Long.class, String.class);

        AnnotationIntrospector ai = null; // mock or use real instance
        MapperConfig<?> config = null; // mock or use real instance
        AnnotatedClass baseClass = AnnotatedClassResolver.resolveWithoutSuperTypes(config, baseType);

        // Register subtypes using annotations on the base class
        AnnotatedClass subtypeClass1 = AnnotatedClassResolver.resolveWithoutSuperTypes(config, Long.class);
        JsonTypeInfo annotations1 = new JsonTypeInfo();
        annotations1.use(JsonTypeInfo.Id.NAME);
        annotations1.propertyOrder(AnnotatedMember.wrap(subtypeClass1, "memberName"));
        ai.findSubtypes(baseClass).add(new NamedType(subtypeClass1, annotations1));

        AnnotatedClass subtypeClass2 = AnnotatedClassResolver.resolveWithoutSuperTypes(config, String.class);
        JsonTypeInfo annotations2 = new JsonTypeInfo();
        annotations2.use(JsonTypeInfo.Id.NAME);
        annotations2.propertyOrder(AnnotatedMember.wrap(subtypeClass2, "memberName"));
        ai.findSubtypes(baseClass).add(new NamedType(subtypeClass2, annotations2));

        List<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, baseClass);

        assertEquals(3, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(Long.class, subtypeName1)));
        assertTrue(subtypes.contains(new NamedType(String.class, subtypeName2)));
        assertTrue(subtypes.contains(new NamedType(Long.class, null)));
        assertTrue(subtypes.contains(new NamedType(String.class, null)));
    }
}