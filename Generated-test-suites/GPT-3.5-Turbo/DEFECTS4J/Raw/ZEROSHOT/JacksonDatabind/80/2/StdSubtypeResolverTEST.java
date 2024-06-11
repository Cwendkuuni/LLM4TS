package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedClassResolver;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;

import java.util.*;

public class StdSubtypeResolverTest {

    private StdSubtypeResolver stdSubtypeResolver;

    @Before
    public void setup() {
        stdSubtypeResolver = new StdSubtypeResolver();
    }

    @Test
    public void testRegisterSubtypes() {
        NamedType[] types = new NamedType[] {new NamedType(String.class, "StringType")};
        stdSubtypeResolver.registerSubtypes(types);
        Collection<NamedType> subtypes = stdSubtypeResolver.collectAndResolveSubtypesByClass(
                null, null, null);
        Assert.assertEquals(1, subtypes.size());
        Assert.assertEquals("StringType", subtypes.iterator().next().getName());
    }

    @Test
    public void testRegisterSubtypesWithClasses() {
        Class<?>[] classes = new Class[] {String.class};
        stdSubtypeResolver.registerSubtypes(classes);
        Collection<NamedType> subtypes = stdSubtypeResolver.collectAndResolveSubtypesByClass(
                null, null, null);
        Assert.assertEquals(1, subtypes.size());
        Assert.assertEquals("String", subtypes.iterator().next().getName());
    }

    @Test
    public void testCollectAndResolveSubtypesByClass() {
        NamedType namedType = new NamedType(String.class, "StringType");
        stdSubtypeResolver.registerSubtypes(namedType);
        List<NamedType> subtypes = new ArrayList<>(stdSubtypeResolver.collectAndResolveSubtypesByClass(
                null, null));
        Assert.assertEquals(1, subtypes.size());
        Assert.assertEquals(namedType, subtypes.get(0));
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId() {
        NamedType namedType = new NamedType(String.class, "StringType");
        stdSubtypeResolver.registerSubtypes(namedType);
        List<NamedType> subtypes = new ArrayList<>(stdSubtypeResolver.collectAndResolveSubtypesByTypeId(
                null, null, null));
        Assert.assertEquals(1, subtypes.size());
        Assert.assertEquals(namedType, subtypes.get(0));
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdWithAnnotatedMember() {
        NamedType namedType = new NamedType(String.class, "StringType");
        stdSubtypeResolver.registerSubtypes(namedType);
        List<NamedType> subtypes = new ArrayList<>(stdSubtypeResolver.collectAndResolveSubtypesByTypeId(
                null, new AnnotatedMemberStub(), null));
        Assert.assertEquals(1, subtypes.size());
        Assert.assertEquals(namedType, subtypes.get(0));
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdWithAnnotatedClass() {
        NamedType namedType = new NamedType(String.class, "StringType");
        stdSubtypeResolver.registerSubtypes(namedType);
        List<NamedType> subtypes = new ArrayList<>(stdSubtypeResolver.collectAndResolveSubtypesByTypeId(
                null, new AnnotatedClassStub()));
        Assert.assertEquals(1, subtypes.size());
        Assert.assertEquals(namedType, subtypes.get(0));
    }

    private static class AnnotatedMemberStub implements AnnotatedMember {

        @Override
        public Class<?> getRawType() {
            return null;
        }

        @Override
        public <A extends java.lang.annotation.Annotation> A getAnnotation(Class<A> class_) {
            return null;
        }

        @Override
        public boolean hasAnnotation(Class<?> annotationClass) {
            return false;
        }
    }

    private static class AnnotatedClassStub implements AnnotatedClass {

        @Override
        public Class<?> getRawType() {
            return null;
        }

        @Override
        public Iterable<AnnotatedField> fields() {
            return null;
        }

        @Override
        public Iterable<AnnotatedMethod> memberMethods() {
            return null;
        }

        @Override
        public Iterable<AnnotatedConstructor> constructors() {
            return null;
        }

        @Override
        public Iterable<AnnotatedMethod> staticMethods() {
            return null;
        }

        @Override
        public Iterable<AnnotatedField> fields(boolean forSerialization) {
            return null;
        }

        @Override
        public AnnotatedMethod findMethod(String name, Class<?>[] paramTypes) {
            return null;
        }

        @Override
        public int getModifiers() {
            return 0;
        }

        @Override
        public String getName() {
            return null;
        }

        @Override
        public <A extends java.lang.annotation.Annotation> A getAnnotation(Class<A> class_) {
            return null;
        }

        @Override
        public boolean hasAnnotation(Class<?> annotationClass) {
            return false;
        }
    }
}