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

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperFactoryMapperConfig;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
import com.fasterxml.jackson.databind.jsontype.impl.StdSubtypeResolver;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class StdSubtypeResolverTest {

    private static final Class<?> TEST_BASE_CLASS = TestBaseClass.class;
    private static final Class<?> TEST_SUBTYPE_CLASS_1 = TestSubtypeClass1.class;
    private static final Class<?> TEST_SUBTYPE_CLASS_2 = TestSubtypeClass2.class;
    private static final Class<?> TEST_ANNOTATED_SUBTYPE_CLASS = TestAnnotatedSubtypeClass.class;

    private static final AnnotationIntrospector introspector = new AnnotationIntrospector();
    private static final MapperConfig config = new MapperFactoryMapperConfig();
    private static final TypeFactory typeFactory = config.getTypeFactory();

    @Test
    public void testRegisterSubtypesByClass() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        StdSubtypeResolver expected = new StdSubtypeResolver();
        expected.registerSubtypes(TEST_SUBTYPE_CLASS_1, TEST_SUBTYPE_CLASS_2);
        resolver.registerSubtypes(TEST_SUBTYPE_CLASS_1, TEST_SUBTYPE_CLASS_2);
        assertSame(expected, resolver);
    }

    @Test
    public void testRegisterSubtypesByAnnotation() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        StdSubtypeResolver expected = new StdSubtypeResolver();
        expected.registerSubtypes(AnnotatedClass.fromClass(TEST_ANNOTATED_SUBTYPE_CLASS));
        resolver.registerSubtypes(TEST_ANNOTATED_SUBTYPE_CLASS);
        assertSame(expected, resolver);
    }

    @Test
    public void testCollectAndResolveSubtypesByClass() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        JavaType baseType = typeFactory.constructType(TEST_BASE_CLASS);

        AnnotatedMember property = createMockAnnotatedMember("testProperty");
        List<NamedType> result = resolver.collectAndResolveSubtypesByClass(config, property, baseType);

        Map<Class<?>, NamedType> expected = new HashMap<>();
        AnnotatedClass testSubtypeClass1Annotated = AnnotatedClassResolver.resolveWithoutSuperTypes(config, TEST_SUBTYPE_CLASS_1);
        expected.put(TEST_SUBTYPE_CLASS_1, new NamedType(TEST_SUBTYPE_CLASS_1, introspector.findTypeName(testSubtypeClass1Annotated)));
        AnnotatedClass testSubtypeClass2Annotated = AnnotatedClassResolver.resolveWithoutSuperTypes(config, TEST_SUBTYPE_CLASS_2);
        expected.put(TEST_SUBTYPE_CLASS_2, new NamedType(TEST_SUBTYPE_CLASS_2, introspector.findTypeName(testSubtypeClass2Annotated)));
        AnnotatedClass testAnnotatedSubtypeClassAnnotated = AnnotatedClassResolver.resolveWithoutSuperTypes(config, TEST_ANNOTATED_SUBTYPE_CLASS);
        expected.put(TEST_ANNOTATED_SUBTYPE_CLASS, new NamedType(TEST_ANNOTATED_SUBTYPE_CLASS, introspector.findTypeName(testAnnotatedSubtypeClassAnnotated)));

        assertEquals(expected.size(), result.size());
        for (NamedType namedType : result) {
            assertTrue(expected.containsKey(namedType.getType()));
        }
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        JavaType baseType = typeFactory.constructType(TEST_BASE_CLASS);

        List<NamedType> result = resolver.collectAndResolveSubtypesByTypeId(config, null, baseType);

        Map<Class<?>, NamedType> expected = new HashMap<>();
        expected.put(TEST_SUBTYPE_CLASS_1, new NamedType(TEST_SUBTYPE_CLASS_1));
        expected.put(TEST_SUBTYPE_CLASS_2, new NamedType(TEST_SUBTYPE_CLASS_2));
        expected.put(TEST_ANNOTATED_SUBTYPE_CLASS, new NamedType(TEST_ANNOTATED_SUBTYPE_CLASS));

        assertEquals(expected.size(), result.size());
        for (NamedType namedType : result) {
            assertTrue(expected.containsKey(namedType.getType()));
        }
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdWithName() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        JavaType baseType = typeFactory.constructType(TEST_BASE_CLASS);

        List<NamedType> result = resolver.collectAndResolveSubtypesByTypeId(config, null, baseType);

        Map<String, NamedType> expectedByName = new LinkedHashMap<>();
        expectedByName.put("subtype1", new NamedType(TEST_SUBTYPE_CLASS_1, "subtype1"));
        expectedByName.put("subtype2", new NamedType(TEST_SUBTYPE_CLASS_2, "subtype2"));
        expectedByName.put("annotatedSubtype", new NamedType(TEST_ANNOTATED_SUBTYPE_CLASS, "annotatedSubtype"));

        assertEquals(expectedByName.size(), result.size());
        for (NamedType namedType : result) {
            assertTrue(expectedByName.containsKey(namedType.getName()));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCollectAndResolveSubtypesByTypeIdNullBaseType() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        resolver.collectAndResolveSubtypesByTypeId(config, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCollectAndResolveSubtypesByTypeIdNullProperty() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        JavaType baseType = typeFactory.constructType(TEST_BASE_CLASS);
        resolver.collectAndResolveSubtypesByTypeId(config, null, baseType);
    }

    private AnnotatedMember createMockAnnotatedMember(String name) {
        AnnotatedMember member = mock(AnnotatedMember.class);
        when(member.getName()).thenReturn(name);
        return member;
    }
}