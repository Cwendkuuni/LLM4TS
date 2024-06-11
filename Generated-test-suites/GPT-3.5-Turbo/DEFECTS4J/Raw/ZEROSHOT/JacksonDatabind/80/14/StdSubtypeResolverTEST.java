
package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Modifier;
import java.util.*;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;

public class StdSubtypeResolverTest {

    private StdSubtypeResolver subtypeResolver;

    @Before
    public void setup() {
        subtypeResolver = new StdSubtypeResolver();
    }

    @Test
    public void testRegisterSubtypes() {
        NamedType[] types = new NamedType[2];
        types[0] = new NamedType(TestClassA.class);
        types[1] = new NamedType(TestClassB.class);

        subtypeResolver.registerSubtypes(types);

        LinkedHashSet<NamedType> expected = new LinkedHashSet<>();
        expected.add(new NamedType(TestClassA.class));
        expected.add(new NamedType(TestClassB.class));

        Assert.assertEquals(expected, subtypeResolver._registeredSubtypes);
    }

    @Test
    public void testRegisterSubtypesWithClasses() {
        Class<?>[] classArray = new Class[]{TestClassA.class, TestClassB.class};

        subtypeResolver.registerSubtypes(classArray);

        LinkedHashSet<NamedType> expected = new LinkedHashSet<>();
        expected.add(new NamedType(TestClassA.class));
        expected.add(new NamedType(TestClassB.class));

        Assert.assertEquals(expected, subtypeResolver._registeredSubtypes);
    }

    @Test
    public void testCollectAndResolveSubtypesByClass() {
        MapperConfig<?> config = null;
        AnnotatedMember property = null;
        JavaType baseType = null;

        Collection<NamedType> result = subtypeResolver.collectAndResolveSubtypesByClass(config, property, baseType);

        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testCollectAndResolveSubtypesByClassWithConfig() {
        MapperConfig<?> config = null;
        AnnotatedClass type = null;

        Collection<NamedType> result = subtypeResolver.collectAndResolveSubtypesByClass(config, type);

        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId() {
        MapperConfig<?> config = null;
        AnnotatedMember property = null;
        JavaType baseType = null;

        Collection<NamedType> result = subtypeResolver.collectAndResolveSubtypesByTypeId(config, property, baseType);

        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdWithConfig() {
        MapperConfig<?> config = null;
        AnnotatedClass baseType = null;

        Collection<NamedType> result = subtypeResolver.collectAndResolveSubtypesByTypeId(config, baseType);

        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testCombineNamedAndUnnamed() {
        Class<?> rawBase = TestClassA.class;
        Set<Class<?>> typesHandled = new HashSet<>();
        Map<String, NamedType> byName = new LinkedHashMap<>();

        typesHandled.add(TestClassB.class);
        byName.put("TestB", new NamedType(TestClassB.class));

        Collection<NamedType> result = subtypeResolver._combineNamedAndUnnamed(rawBase, typesHandled, byName);

        List<NamedType> expected = new ArrayList<>();
        expected.add(new NamedType(TestClassB.class));
        expected.add(new NamedType(TestClassA.class));

        Assert.assertEquals(expected, result);
    }

    private static class TestClassA {
    }

    private static class TestClassB {
    }
}

