package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Modifier;
import java.util.*;

/**
 * Test class for StdSubtypeResolver
 */
public class StdSubtypeResolverTest {

    private ObjectMapper objectMapper;
    private AnnotationIntrospector introspector;
    private MapperConfig config;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        introspector = objectMapper.getAnnotationIntrospector();
        config = objectMapper.getConfiguration();
    }

    /**
     * Test method for {@link StdSubtypeResolver#registerSubtypes(Class...)}
     */
    @Test
    public void testRegisterSubtypesByClass() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        Class<?> superclass = Integer.TYPE;
        Class<?> subclass1 = Integer.class;
        Class<?> subclass2 = Long.class;

        resolver.registerSubtypes(subclass1, subclass2);

        List<NamedType> actual = resolver.collectAndResolveSubtypesByClass(config, null, superclass);
        List<NamedType> expected = Arrays.asList(
                new NamedType(subclass1),
                new NamedType(subclass2)
        );
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test method for {@link StdSubtypeResolver#registerSubtypes(NamedType...)}
     */
    @Test
    public void testRegisterSubtypesByName() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        Class<?> superclass = Integer.TYPE;
        Class<?> subclass1 = Integer.class;
        Class<?> subclass2 = Long.class;

        NamedType subtype1 = new NamedType(subclass1, "Integer");
        NamedType subtype2 = new NamedType(subclass2, "Long");

        resolver.registerSubtypes(subtype1, subtype2);

        List<NamedType> actual = resolver.collectAndResolveSubtypesByClass(config, null, superclass);
        List<NamedType> expected = Arrays.asList(
                subtype1,
                subtype2
        );
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test method for {@link StdSubtypeResolver#collectAndResolveSubtypesByClass(MapperConfig, AnnotatedMember, JavaType)}
     */
    @Test
    public void testCollectAndResolveSubtypesByClass() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        Class<?> superclass = Integer.TYPE;
        Class<?> integerClass = Integer.class;
        Class<?> stringClass = String.class;

        resolver.registerSubtypes(integerClass);

        AnnotatedMember member = new AnnotatedMember(integerClass.getField("value"), new JsonTypeInfo.Id());

        List<NamedType> actual = resolver.collectAndResolveSubtypesByClass(config, member, superclass);
        List<NamedType> expected = Arrays.asList(new NamedType(integerClass));
        Assert.assertEquals(expected, actual);

        // Test with a superclass that has no registered subtypes
        resolver = new StdSubtypeResolver();

        Class<?> superclassNoSubtypes = List.class;

        List<NamedType> actualNoSubtypes = resolver.collectAndResolveSubtypesByClass(config, null, superclassNoSubtypes);
        Assert.assertTrue(actualNoSubtypes.isEmpty());

        // Test with a class that has no subtypes
        Class<?> noSubtypesClass = Void.TYPE;

        AnnotatedMember noSubtypesMember = new AnnotatedMember(noSubtypesClass.getDeclaredField("value"), new JsonTypeInfo.Id());

        List<NamedType> actualNoSubtypesClass = resolver.collectAndResolveSubtypesByClass(config, noSubtypesMember, superclassNoSubtypes);
        Assert.assertTrue(actualNoSubtypesClass.isEmpty());

        // Test with a class that has registered subtypes and annotations
        Class<?> superclassWithAnnotations = List.class;
        Class<?> integerListClass = ArrayList.class;
        Class<?> stringListClass = ArrayList.class;

        resolver.registerSubtypes(integerListClass);

        AnnotatedClass annotatedClass = AnnotatedClassResolver.resolveWithoutSuperTypes(config, integerListClass);
        List<JsonTypeInfo.Id> ids = introspector.findAnnotationIds(annotatedClass, JsonTypeInfo.Id.class);

        for (JsonTypeInfo.Id id : ids) {
            AnnotatedMember memberWithAnnotation = annotatedClass.findAnnotatedMember(id.name(), 0);
            List<NamedType> subtypesWithAnnotation = resolver.collectAndResolveSubtypesByClass(config, memberWithAnnotation, superclassWithAnnotations);
            List<NamedType> expectedWithAnnotation = Arrays.asList(new NamedType(integerListClass));
            Assert.assertEquals(expectedWithAnnotation, subtypesWithAnnotation);
        }

        // Test with a class that has registered subtypes and superclass
        Class<?> superclassWithSuperclass = List.class;
        Class<?> integerListClassWithSuperclass = ArrayList.class;
        Class<?> stringListClassWithSuperclass = ArrayList.class;

        resolver.registerSubtypes(integerListClassWithSuperclass);

        AnnotatedClass superclassAnnotatedClass = AnnotatedClassResolver.resolveWithoutSuperTypes(config, superclassWithSuperclass);

        List<NamedType> actualSuperclass = resolver.collectAndResolveSubtypesByClass(config, null, superclassWithSuperclass);
        List<NamedType> expectedSuperclass = Arrays.asList(new NamedType(integerListClassWithSuperclass));
        Assert.assertEquals(expectedSuperclass, actualSuperclass);
    }

    /**
     * Test method for {@link StdSubtypeResolver#collectAndResolveSubtypesByTypeId(MapperConfig, AnnotatedMember, JavaType)}
     */
    @Test
    public void testCollectAndResolveSubtypesByTypeId() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        Class<?> superclass = Integer.TYPE;
        Class<?> integerClass = Integer.class;
        Class<?> stringClass = String.class;

        resolver.registerSubtypes(integerClass);

        AnnotatedMember member = new AnnotatedMember(integerClass.getField("value"), new JsonTypeInfo.Id());

        List<NamedType> actual = resolver.collectAndResolveSubtypesByTypeId(config, member, superclass);
        List<NamedType> expected = Arrays.asList(new NamedType(integerClass));
        Assert.assertEquals(expected, actual);

        // Test with a superclass that has no registered subtypes
        resolver = new StdSubtypeResolver();

        Class<?> superclassNoSubtypes = List.class;

        List<NamedType> actualNoSubtypes = resolver.collectAndResolveSubtypesByTypeId(config, null, superclassNoSubtypes);
        Assert.assertTrue(actualNoSubtypes.isEmpty());

        // Test with a class that has no subtypes
        Class<?> noSubtypesClass = Void.TYPE;

        AnnotatedMember noSubtypesMember = new AnnotatedMember(noSubtypesClass.getDeclaredField("value"), new JsonTypeInfo.Id());

        List<NamedType> actualNoSubtypesClass = resolver.collectAndResolveSubtypesByTypeId(config, noSubtypesMember, superclassNoSubtypes);
        Assert.assertTrue(actualNoSubtypesClass.isEmpty());

        // Test with a class that has registered subtypes and annotations
        Class<?> superclassWithAnnotations = List.class;
        Class<?> integerListClass = ArrayList.class;
        Class<?> stringListClass = ArrayList.class;

        resolver.registerSubtypes(integerListClass);

        AnnotatedClass annotatedClass = AnnotatedClassResolver.resolveWithoutSuperTypes(config, integerListClass);
        List<JsonTypeInfo.Id> ids = introspector.findAnnotationIds(annotatedClass, JsonTypeInfo.Id.class);

        for (JsonTypeInfo.Id id : ids) {
            AnnotatedMember memberWithAnnotation = annotatedClass.findAnnotatedMember(id.name(), 0);
            List<NamedType> subtypesWithAnnotation = resolver.collectAndResolveSubtypesByTypeId(config, memberWithAnnotation, superclassWithAnnotations);
            List<NamedType> expectedWithAnnotation = Arrays.asList(new NamedType(integerListClass));
            Assert.assertEquals(expectedWithAnnotation, subtypesWithAnnotation);
        }

        // Test with a class that has registered subtypes and superclass
        Class<?> superclassWithSuperclass = List.class;
        Class<?> integerListClassWithSuperclass = ArrayList.class;
        Class<?> stringListClassWithSuperclass = ArrayList.class;

        resolver.registerSubtypes(integerListClassWithSuperclass);

        AnnotatedClass superclassAnnotatedClass = AnnotatedClassResolver.resolveWithoutSuperTypes(config, superclassWithSuperclass);

        List<NamedType> actualSuperclass = resolver.collectAndResolveSubtypesByTypeId(config, null, superclassWithSuperclass);
        List<NamedType> expectedSuperclass = Arrays.asList(new NamedType(integerListClassWithSuperclass));
        Assert.assertEquals(expectedSuperclass, actualSuperclass);
    }

    /**
     * Test method for {@link StdSubtypeResolver#collectAndResolveSubtypesByTypeId(MapperConfig, AnnotatedClass)}
     */
    @Test
    public void testCollectAndResolveSubtypesByTypeIdWithClass() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        Class<?> superclass = List.class;
        Class<?> integerListClass = ArrayList.class;

        resolver.registerSubtypes(integerListClass);

        List<NamedType> actual = resolver.collectAndResolveSubtypesByTypeId(config, null, superclass);
        List<NamedType> expected = Arrays.asList(new NamedType(integerListClass));
        Assert.assertEquals(expected, actual);

        // Test with a superclass that has no registered subtypes
        resolver = new StdSubtypeResolver();

        Class<?> superclassNoSubtypes = Void.TYPE;

        List<NamedType> actualNoSubtypes = resolver.collectAndResolveSubtypesByTypeId(config, null, superclassNoSubtypes);
        Assert.assertTrue(actualNoSubtypes.isEmpty());

        // Test with a class that has registered subtypes and annotations
        Class<?> superclassWithAnnotations = List.class;
        Class<?> integerListClassWithAnnotations = ArrayList.class;
        Class<?> stringListClassWithAnnotations = ArrayList.class;

        resolver.registerSubtypes(integerListClassWithAnnotations);

        AnnotatedClass annotatedClass = AnnotatedClassResolver.resolveWithoutSuperTypes(config, integerListClassWithAnnotations);
        List<JsonTypeInfo.Id> ids = introspector.findAnnotationIds(annotatedClass, JsonTypeInfo.Id.class);

        for (JsonTypeInfo.Id id : ids) {
            AnnotatedClass memberWithAnnotation = AnnotatedClassResolver.resolveWithoutSuperTypes(config, integerListClassWithAnnotations);
            List<NamedType> subtypesWithAnnotation = resolver.collectAndResolveSubtypesByTypeId(config, memberWithAnnotation, superclassWithAnnotations);
            List<NamedType> expectedWithAnnotation = Arrays.asList(new NamedType(integerListClassWithAnnotations));
            Assert.assertEquals(expectedWithAnnotation, subtypesWithAnnotation);
        }

        // Test with a class that has registered subtypes and superclass
        Class<?> superclassWithSuperclass = List.class;
        Class<?> integerListClassWithSuperclass = ArrayList.class;
        Class<?> stringListClassWithSuperclass = ArrayList.class;

        resolver.registerSubtypes(integerListClassWithSuperclass);

        AnnotatedClass superclassAnnotatedClass = AnnotatedClassResolver.resolveWithoutSuperTypes(config, superclassWithSuperclass);

        List<NamedType> actualSuperclass = resolver.collectAndResolveSubtypesByTypeId(config, null, superclassWithSuperclass);
        List<NamedType> expectedSuperclass = Arrays.asList(new NamedType(integerListClassWithSuperclass));
        Assert.assertEquals(expectedSuperclass, actualSuperclass);
    }

    /**
     * Test method for {@link StdSubtypeResolver#_collectAndResolve(AnnotatedClass, NamedType, MapperConfig, AnnotationIntrospector, HashMap)}
     */
    @Test(expected = AssertionError.class)
    public void test_collectAndResolveWithDuplicateBaseType() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        Class<?> superclass = List.class;
        Class<?> integerListClass = ArrayList.class;

        resolver.registerSubtypes(integerListClass);

        AnnotatedClass annotatedClass = AnnotatedClassResolver.resolveWithoutSuperTypes(config, integerListClass);
        NamedType namedType = new NamedType(superclass, null);

        resolver._collectAndResolve(annotatedClass, namedType, config, introspector, Collections.emptyMap());
    }

    /**
     * Test method for {@link StdSubtypeResolver#_collectAndResolveByTypeId(AnnotatedClass, NamedType, MapperConfig, Set, Map)}
     */
    @Test(expected = AssertionError.class)
    public void test_collectAndResolveByTypeIdWithDuplicateBaseType() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        Class<?> superclass = List.class;
        Class<?> integerListClass = ArrayList.class;

        resolver.registerSubtypes(integerListClass);

        AnnotatedClass annotatedClass = AnnotatedClassResolver.resolveWithoutSuperTypes(config, integerListClass);
        NamedType namedType = new NamedType(superclass, null);

        Set<Class<?>> typesHandled = new HashSet<>();
        Map<String, NamedType> byName = new LinkedHashMap<>();

        resolver._collectAndResolveByTypeId(annotatedClass, namedType, config, typesHandled, byName);
    }
}