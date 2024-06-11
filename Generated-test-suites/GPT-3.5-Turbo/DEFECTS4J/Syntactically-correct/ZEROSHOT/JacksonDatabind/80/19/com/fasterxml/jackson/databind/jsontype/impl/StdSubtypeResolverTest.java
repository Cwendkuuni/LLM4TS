package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedClassResolver;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Modifier;
import java.util.*;

public class StdSubtypeResolverTest {

    private StdSubtypeResolver subtypeResolver;

    @Before
    public void setUp() {
        subtypeResolver = new StdSubtypeResolver();
    }

    @Test
    public void testRegisterSubtypes() {
        NamedType namedType = new NamedType(Object.class);
        subtypeResolver.registerSubtypes(namedType);

        Assert.assertNotNull(subtypeResolver._registeredSubtypes);
        Assert.assertEquals(1, subtypeResolver._registeredSubtypes.size());
    }

    @Test
    public void testRegisterSubtypeClasses() {
        Class<?>[] classes = {Object.class};
        subtypeResolver.registerSubtypes(classes);

        Assert.assertNotNull(subtypeResolver._registeredSubtypes);
        Assert.assertEquals(1, subtypeResolver._registeredSubtypes.size());
    }

    @Test
    public void testCollectAndResolveSubtypesByClassWithBaseType() {
        MapperConfig<?> config = null;
        AnnotatedMember property = null;
        JavaType baseType = null;

        Collection<NamedType> namedTypes = subtypeResolver.collectAndResolveSubtypesByClass(config, property, baseType);

        Assert.assertNotNull(namedTypes);
        Assert.assertTrue(namedTypes.isEmpty());
    }

    @Test
    public void testCollectAndResolveSubtypesByClassWithType() {
        MapperConfig<?> config = null;
        AnnotatedClass type = null;

        Collection<NamedType> namedTypes = subtypeResolver.collectAndResolveSubtypesByClass(config, type);

        Assert.assertNotNull(namedTypes);
        Assert.assertTrue(namedTypes.isEmpty());
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdWithBaseType() {
        MapperConfig<?> config = null;
        AnnotatedMember property = null;
        JavaType baseType = null;

        Collection<NamedType> namedTypes = subtypeResolver.collectAndResolveSubtypesByTypeId(config, property, baseType);

        Assert.assertNotNull(namedTypes);
        Assert.assertTrue(namedTypes.isEmpty());
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdWithType() {
        MapperConfig<?> config = null;
        AnnotatedClass type = null;

        Collection<NamedType> namedTypes = subtypeResolver.collectAndResolveSubtypesByTypeId(config, type);

        Assert.assertNotNull(namedTypes);
        Assert.assertTrue(namedTypes.isEmpty());
    }
}