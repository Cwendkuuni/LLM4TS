package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedClassResolver;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.lang.reflect.Modifier;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(Parameterized.class)
public class StdSubtypeResolverTest {

    private StdSubtypeResolver subtypeResolver;

    @Before
    public void setup() {
        subtypeResolver = new StdSubtypeResolver();
    }

    // *** Collect and Resolve Subtypes by Class (Serialization) ***

    @Test
    public void testCollectAndResolveSubtypesByClass() {
        MapperConfig<?> config = mock(MapperConfig.class);
        AnnotatedMember property = mock(AnnotatedMember.class);
        JavaType baseType = mock(JavaType.class);

        subtypeResolver.registerSubtypes(NamedTypeTestClassA.class,
                NamedTypeTestClassB.class);

        Collection<NamedType> resolvedSubtypes = subtypeResolver.collectAndResolveSubtypesByClass(config,
                property, baseType);

        assertEquals(2, resolvedSubtypes.size());
        verify(config, times(1)).getAnnotationIntrospector();
    }

    // *** Collect and Resolve Subtypes by Class (Deserialization) ***

    @Test
    public void testCollectAndResolveSubtypesByTypeId() {
        MapperConfig<?> config = mock(MapperConfig.class);
        AnnotatedMember property = mock(AnnotatedMember.class);
        JavaType baseType = mock(JavaType.class);

        subtypeResolver.registerSubtypes(NamedTypeTestClassA.class,
                NamedTypeTestClassB.class);

        Collection<NamedType> resolvedSubtypes = subtypeResolver.collectAndResolveSubtypesByTypeId(config,
                property, baseType);

        assertEquals(2, resolvedSubtypes.size());
        verify(config, times(1)).getAnnotationIntrospector();
    }

    @SuppressWarnings("unused")
    private static class NamedTypeTestClassA {
    }

    @SuppressWarnings("unused")
    private static class NamedTypeTestClassB {
    }
}