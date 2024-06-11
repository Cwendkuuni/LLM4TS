package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.lang.reflect.Modifier;
import java.util.*;

import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.core.type.ClassType;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.cfg.MapperConfig.DefaultConfig;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
import com.fasterxml.jackson.databind.jsontype.impl.StdSubtypeResolver;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector.FindTypeByName;
import com.fasterxml.jackson.databind.introspect.ClassIntrospector;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class StdSubtypeResolverTest {

    private static ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
        mapper.configure(MapperConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void testRegisterSubtypesByClass() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        Class<?> baseType = Integer.class;
        Class<?> subtype1 = Long.class;
        Class<?> subtype2 = Short.class;

        resolver.registerSubtypes(subtype1, subtype2);

        JavaType baseTypeType = mapper.getTypeFactory().constructType(baseType);

        List<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(null, null, baseTypeType);

        assertEquals(2, subtypes.size());
        assertEquals(subtype1, subtypes.get(0).getType());
        assertEquals(subtype2, subtypes.get(1).getType());
    }

    @Test
    public void testRegisterSubtypesByAnnotation() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        Class<?> baseType = Integer.class;
        Class<?> subtype1 = Long.class;
        Class<?> subtype2 = Short.class;

        Class<?> testClass = Mockito.spy(new TestClass());

        ClassIntrospector introspector = mapper.getClassIntrospector();
        PropertyName propertyName = PropertyName.construct("property");
        AnnotatedMember property = introspector.findMember("property", testClass);

        Mockito.doReturn(subtype1).when(testClass).getType(Mockito.eq(propertyName.getShortName()));
        Mockito.doReturn(subtype2).when(testClass).getType(Mockito.eq("testSubtype2"));

        resolver.registerSubtypes(testClass);

        JavaType baseTypeType = mapper.getTypeFactory().constructType(baseType);

        List<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(null, null, baseTypeType);

        assertEquals(2, subtypes.size());
        assertEquals(subtype1, subtypes.get(0).getType());
        assertEquals(subtype2, subtypes.get(1).getType());
    }

    @Test
    public void testCollectSubtypesByClassId() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        Class<?> baseType = Integer.class;
        Class<?> subtype1 = Long.class;
        Class<?> subtype2 = Short.class;

        Class<?> testClass = Mockito.spy(new TestClass());

        ClassIntrospector introspector = mapper.getClassIntrospector();
        PropertyName propertyName = PropertyName.construct("property");
        AnnotatedMember property = introspector.findMember("property", testClass);

        Mockito.doReturn(subtype1).when(testClass).getType(Mockito.eq(propertyName.getShortName()));
        Mockito.doReturn(subtype2).when(testClass).getType(Mockito.eq("testSubtype2"));

        resolver.registerSubtypes(testClass);

        JavaType baseTypeType = mapper.getTypeFactory().constructType(baseType);

        List<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(null, null, baseTypeType);

        assertEquals(2, subtypes.size());
        assertEquals(subtype1, subtypes.get(0).getType());
        assertEquals(subtype2, subtypes.get(1).getType());
    }

    @Test
    public void testCollectSubtypesByClassIdWithExplicitRegistration() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        Class<?> baseType = Integer.class;
        Class<?> subtype1 = Long.class;
        Class<?> subtype2 = Short.class;

        Class<?> testClass = Mockito.spy(new TestClass());

        ClassIntrospector introspector = mapper.getClassIntrospector();
        PropertyName propertyName = PropertyName.construct("property");
        AnnotatedMember property = introspector.findMember("property", testClass);

        Mockito.doReturn(subtype1).when(testClass).getType(Mockito.eq(propertyName.getShortName()));
        Mockito.doReturn(subtype2).when(testClass).getType(Mockito.eq("testSubtype2"));

        resolver.registerSubtypes(testClass);

        JavaType baseTypeType = mapper.getTypeFactory().constructType(baseType);

        resolver.registerSubtypes(subtype1);

        List<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(null, null, baseTypeType);

        assertEquals(3, subtypes.size());
        assertEquals(subtype1, subtypes.get(0).getType());
        assertEquals(subtype2, subtypes.get(1).getType());
        assertTrue(subtypes.get(2).isUnknown());
    }

    @Test
    public void testCollectSubtypesByClassIdWithConflictingRegistration() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        Class<?> baseType = Integer.class;
        Class<?> subtype1 = Long.class;
        Class<?> subtype2 = Short.class;

        Class<?> testClass = Mockito.spy(new TestClass());

        ClassIntrospector introspector = mapper.getClassIntrospector();
        PropertyName propertyName = PropertyName.construct("property");
        AnnotatedMember property = introspector.findMember("property", testClass);

        Mockito.doReturn(subtype1).when(testClass).getType(Mockito.eq(propertyName.getShortName()));
        Mockito.doReturn(subtype2).when(testClass).getType(Mockito.eq("testSubtype2"));

        resolver.registerSubtypes(testClass);

        JavaType baseTypeType = mapper.getTypeFactory().constructType(baseType);

        resolver.registerSubtypes(subtype1);

        Mockito.doThrow(IllegalArgumentException.class).when(testClass).getType(Mockito.eq(subtype1.getName()));

        List<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(null, null, baseTypeType);

        assertEquals(3, subtypes.size());
        assertEquals(subtype1, subtypes.get(0).getType());
        assertEquals(subtype2, subtypes.get(1).getType());
        assertTrue(subtypes.get(2).isUnknown());
    }

    static class TestClass {
        @JsonTypeInfo(use = Id.NAME, include = As.EXTERNAL_PROPERTY, property = "type")
        private Object property;

        @JsonTypeName("testSubtype1")
        public Long getTestSubtype1() {
            return null;
        }

        @JsonTypeName("testSubtype2")
        public Short getTestSubtype2() {
            return null;
        }
    }
}

class FindTypeByName implements AnnotationIntrospector.FindTypeByName {
    @Override
    public Class<?> findTypeByName(String name, JavaType baseType) {
        return mapper.getTypeFactory().constructClass(name);
    }
}