package com.fasterxml.jackson.databind.jsontype.impl;

import static com.fasterxml.jackson.core.util.TestUtils.convertObjectToText;
import static org.junit.Assert.*;

import java.lang.reflect.Modifier;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.*;

public class StdSubtypeResolverTest {

    @Test
    public void testRegisterSubtypes() throws Exception {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        Class<?> baseType = Object.class;
        Class<?> subtype1 = String.class;
        Class<?> subtype2 = Integer.class;

        resolver.registerSubtypes(new NamedType(subtype1));
        resolver.registerSubtypes(new NamedType(subtype2));

        MapperConfig<?> config = new MapperConfig.Builder().build();
        AnnotatedClass ac = AnnotatedClassResolver.resolveWithoutSuperTypes(config, subtype1);
        AnnotatedClass baseAc = AnnotatedClassResolver.resolveWithoutSuperTypes(config, baseType);

        List<NamedType> actualSubtypes = resolver.collectAndResolveSubtypesByClass(config, baseAc);

        List<NamedType> expectedSubtypes = Arrays.asList(
                new NamedType(subtype1),
                new NamedType(subtype2)
        );
        assertEquals(expectedSubtypes, actualSubtypes);
    }

    @Test
    public void testRegisterSubtypesAnnotation() throws Exception {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        Class<?> baseType = Object.class;
        Class<?> subtype1 = String.class;
        Class<?> subtype2 = Integer.class;

        SubtypeResolver.BeanPropertyAnnotation introspector = new SubtypeResolver.BeanPropertyAnnotation();
        introspector.addClassAnnotations(subtype1, new JsonTypeInfo.As(JsonTypeInfo.Id.CLASS, true));
        introspector.setPropertyAnnotation(subtype1, "foo", new JsonTypeInfo.As(subtype1, true));
        introspector.addClassAnnotations(subtype2, new JsonTypeInfo.As(JsonTypeInfo.Id.CLASS, true));
        introspector.setPropertyAnnotation(subtype2, "bar", new JsonTypeName("IntegerSubtype"));

        MapperConfig<?> config = new MapperConfig.Builder().build();
        config.setAnnotationIntrospector(introspector);

        resolver.registerSubtypes(subtype1);
        resolver.registerSubtypes(subtype2);

        List<NamedType> actualSubtypes = resolver.collectAndResolveSubtypesByClass(config, baseAc);

        List<NamedType> expectedSubtypes = Arrays.asList(
                new NamedType(subtype1, "foo"),
                new NamedType(subtype2, "bar")
        );
        assertEquals(expectedSubtypes, actualSubtypes);
    }

    @Test
    public void testRegisterSubtypesByClass() throws Exception {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        Class<?> baseType = Object.class;
        Class<?> subtype1 = String.class;
        Class<?> subtype2 = Integer.class;

        resolver.registerSubtypes(baseType, subtype1);
        resolver.registerSubtypes(baseType, subtype2);

        MapperConfig<?> config = new MapperConfig.Builder().build();
        AnnotatedClass ac = AnnotatedClassResolver.resolveWithoutSuperTypes(config, baseType);

        List<NamedType> actualSubtypes = resolver.collectAndResolveSubtypesByClass(config, ac);

        List<NamedType> expectedSubtypes = Arrays.asList(
                new NamedType(subtype1),
                new NamedType(subtype2)
        );
        assertEquals(expectedSubtypes, actualSubtypes);
    }

    @Test
    public void testCollectAndResolveSubtypesByClass() throws Exception {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        Class<?> baseType = Object.class;
        Class<?> subtype1 = String.class;
        Class<?> subtype2 = Integer.class;

        MapperConfig<?> config = new MapperConfig.Builder().build();
        resolver.registerSubtypes(new NamedType(subtype1));
        resolver.registerSubtypes(new NamedType(subtype2));

        AnnotatedClass ac = AnnotatedClassResolver.resolveWithoutSuperTypes(config, baseType);

        List<NamedType> actualSubtypes = resolver.collectAndResolveSubtypesByClass(config, ac);

        List<NamedType> expectedSubtypes = Arrays.asList(
                new NamedType(subtype1),
                new NamedType(subtype2)
        );
        assertEquals(expectedSubtypes, actualSubtypes);
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId_EmptyCollection() throws Exception {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        Class<?> baseType = Object.class;

        MapperConfig<?> config = new MapperConfig.Builder().build();

        List<NamedType> actualSubtypes = resolver.collectAndResolveSubtypesByTypeId(config, null, null);

        assertTrue(actualSubtypes.isEmpty());
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId_EmptyCollectionAnnotatedClass() throws Exception {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        Class<?> baseType = Object.class;

        MapperConfig<?> config = new MapperConfig.Builder().build();
        AnnotatedClass ac = AnnotatedClassResolver.resolveWithoutSuperTypes(config, baseType);

        List<NamedType> actualSubtypes = resolver.collectAndResolveSubtypesByTypeId(config, ac, null);

        assertTrue(actualSubtypes.isEmpty());
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId() throws Exception {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        Class<?> baseType = Object.class;
        Class<?> subtype1 = String.class;
        Class<?> subtype2 = Integer.class;

        MapperConfig<?> config = new MapperConfig.Builder().build();
        resolver.registerSubtypes(new NamedType(subtype1));
        resolver.registerSubtypes(new NamedType(subtype2));

        AnnotatedClass ac = AnnotatedClassResolver.resolveWithoutSuperTypes(config, baseType);

        List<NamedType> actualSubtypes = resolver.collectAndResolveSubtypesByTypeId(config, ac, null);

        List<NamedType> expectedSubtypes = Arrays.asList(
                new NamedType(subtype1),
                new NamedType(subtype2)
        );
        assertEquals(expectedSubtypes, actualSubtypes);
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId_Annotation() throws Exception {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        Class<?> baseType = Object.class;
        Class<?> subtype1 = String.class;
        Class<?> subtype2 = Integer.class;

        SubtypeResolver.BeanPropertyAnnotation introspector = new SubtypeResolver.BeanPropertyAnnotation();
        introspector.addClassAnnotations(subtype1, new JsonTypeInfo.As(JsonTypeInfo.Id.CLASS, true));
        introspector.setPropertyAnnotation(subtype1, "foo", new JsonTypeInfo.As(subtype1, true));
        introspector.addClassAnnotations(subtype2, new JsonTypeInfo.As(JsonTypeInfo.Id.CLASS, true));
        introspector.setPropertyAnnotation(subtype2, "bar", new JsonTypeName("IntegerSubtype"));

        MapperConfig<?> config = new MapperConfig.Builder().build();
        config.setAnnotationIntrospector(introspector);

        AnnotatedClass ac = AnnotatedClassResolver.resolveWithoutSuperTypes(config, baseType);

        List<NamedType> actualSubtypes = resolver.collectAndResolveSubtypesByTypeId(config, ac, null);

        List<NamedType> expectedSubtypes = Arrays.asList(
                new NamedType(subtype1, "foo"),
                new NamedType(subtype2, "bar")
        );
        assertEquals(expectedSubtypes, actualSubtypes);
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId_RegisteredAndAnnotated() throws Exception {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        Class<?> baseType = Object.class;
        Class<?> subtype1 = String.class;
        Class<?> subtype2 = Integer.class;

        SubtypeResolver.BeanPropertyAnnotation introspector = new SubtypeResolver.BeanPropertyAnnotation();
        introspector.addClassAnnotations(subtype1, new JsonTypeInfo.As(JsonTypeInfo.Id.CLASS, true));
        introspector.setPropertyAnnotation(subtype1, "foo", new JsonTypeInfo.As(subtype1, true));
        introspector.addClassAnnotations(subtype2, new JsonTypeInfo.As(JsonTypeInfo.Id.CLASS, true));
        introspector.setPropertyAnnotation(subtype2, "bar", new JsonTypeName("IntegerSubtype"));

        MapperConfig<?> config = new MapperConfig.Builder().build();
        config.setAnnotationIntrospector(introspector);
        resolver.registerSubtypes(subtype1);
        resolver.registerSubtypes(subtype2);

        AnnotatedClass ac = AnnotatedClassResolver.resolveWithoutSuperTypes(config, baseType);

        List<NamedType> actualSubtypes = resolver.collectAndResolveSubtypesByTypeId(config, ac, null);

        List<NamedType> expectedSubtypes = Arrays.asList(
                new NamedType(subtype1, "foo"),
                new NamedType(subtype2, "bar")
        );
        assertEquals(expectedSubtypes, actualSubtypes);
    }
}