package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
import org.junit.Test;
import java.util.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class StdSubtypeResolverTest {
    @Test
    public void testRegisterSubtypesNamedTypeArray() {
        StdSubtypeResolver subtypeResolver = new StdSubtypeResolver();
        NamedType[] namedTypes = {new NamedType(String.class, "string"), new NamedType(Integer.class, "integer"), new NamedType(Boolean.class, "bool")};
        subtypeResolver.registerSubtypes(namedTypes);

        Collection<NamedType> result = subtypeResolver._registeredSubtypes;
        assertEquals(3, result.size());
        for (NamedType namedType : namedTypes) {
            assertEquals(namedType, result.stream().filter(n -> n.getName().equals(namedType.getName())).findFirst().get());
        }
    }

    @Test
    public void testRegisterSubtypesClassArray() {
        StdSubtypeResolver subtypeResolver = new StdSubtypeResolver();
        Class[] classes = {String.class, Integer.class, Boolean.class};
        subtypeResolver.registerSubtypes(classes);

        Collection<NamedType> result = subtypeResolver._registeredSubtypes;
        assertEquals(3, result.size());
        for (Class clazz : classes) {
            assertEquals(clazz, result.stream().filter(n -> n.getType().equals(clazz)).findFirst().get().getType());
        }
    }

    @Test
    public void testCollectAndResolveSubtypesByClassAnnotatedMemberJavaType() {
        StdSubtypeResolver subtypeResolver = new StdSubtypeResolver();
        subtypeResolver.registerSubtypes(String.class, Integer.class, Boolean.class);

        MapperConfig config = null;
        AnnotatedMember annotatedMember = null;
        JavaType javaType = null;

        Collection<NamedType> result = subtypeResolver.collectAndResolveSubtypesByClass(config, annotatedMember, javaType);

        assertEquals(3, result.size());
        for (NamedType namedType : subtypeResolver._registeredSubtypes) {
            assertEquals(namedType, result.stream().filter(n -> n.getType().equals(namedType.getType())).findFirst().get());
        }
    }

    @Test
    public void testCollectAndResolveSubtypesByClassMapperConfigAnnotatedClass() {
        StdSubtypeResolver subtypeResolver = new StdSubtypeResolver();
        subtypeResolver.registerSubtypes(String.class, Integer.class, Boolean.class);

        MapperConfig config = null;
        AnnotatedClass annotatedClass = null;

        Collection<NamedType> result = subtypeResolver.collectAndResolveSubtypesByClass(config, annotatedClass);

        assertEquals(3, result.size());
        for (NamedType namedType : subtypeResolver._registeredSubtypes) {
            assertEquals(namedType, result.stream().filter(n -> n.getType().equals(namedType.getType())).findFirst().get());
        }
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdAnnotatedMemberJavaType() {
        StdSubtypeResolver subtypeResolver = new StdSubtypeResolver();
        subtypeResolver.registerSubtypes(String.class, Integer.class, Boolean.class);

        MapperConfig config = null;
        AnnotatedMember annotatedMember = null;
        JavaType javaType = null;

        Collection<NamedType> result = subtypeResolver.collectAndResolveSubtypesByTypeId(config, annotatedMember, javaType);

        assertEquals(3, result.size());
        for (NamedType namedType : subtypeResolver._registeredSubtypes) {
            assertEquals(namedType, result.stream().filter(n -> n.getType().equals(namedType.getType())).findFirst().get());
        }
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdMapperConfigAnnotatedClass() {
        StdSubtypeResolver subtypeResolver = new StdSubtypeResolver();
        subtypeResolver.registerSubtypes(String.class, Integer.class, Boolean.class);

        MapperConfig config = null;
        AnnotatedClass annotatedClass = null;

        Collection<NamedType> result = subtypeResolver.collectAndResolveSubtypesByTypeId(config, annotatedClass);

        assertEquals(3, result.size());
        for (NamedType namedType : subtypeResolver._registeredSubtypes) {
            assertEquals(namedType, result.stream().filter(n -> n.getType().equals(namedType.getType())).findFirst().get());
        }
    }
}