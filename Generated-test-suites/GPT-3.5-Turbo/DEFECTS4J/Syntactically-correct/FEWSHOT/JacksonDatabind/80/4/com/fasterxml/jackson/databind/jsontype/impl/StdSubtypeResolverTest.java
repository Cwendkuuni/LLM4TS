package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedClassResolver;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class StdSubtypeResolverTest {

    private MapperConfig config;
    private AnnotationIntrospector ai;

    @Before
    public void setUp() {
        config = new MapperConfig() {
            @Override
            public AnnotationIntrospector getAnnotationIntrospector() {
                return ai;
            }
        };
        ai = new AnnotationIntrospector() {
            @Override
            public Collection<NamedType> findSubtypes(AnnotatedClass ac) {
                List<NamedType> subtypes = new ArrayList<>();
                switch(ac.getName()) {
                    case "com.fasterxml.jackson.databind.jsontype.impl.StdSubtypeResolver":
                        subtypes.add(new NamedType(StdSubtypeResolver.class, "StdSubtypeResolver"));
                        subtypes.add(new NamedType(StdSubtypeResolverTest.class, "StdSubtypeResolverTest"));
                        break;
                    case "com.fasterxml.jackson.databind.jsontype.impl.StdSubtypeResolverTest":
                        subtypes.add(new NamedType(StdSubtypeResolver.class, "StdSubtypeResolver"));
                        break;
                }
                return subtypes;
            }

            @Override
            public String findTypeName(AnnotatedClass annotatedClass) {
                return null;
            }
        };
    }

    @Test
    public void testRegisterSubtypes() {
        StdSubtypeResolver subtypeResolver = new StdSubtypeResolver();
        subtypeResolver.registerSubtypes(new NamedType(ArrayList.class));
        subtypeResolver.registerSubtypes(new NamedType(List.class));

        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(config, null, null);

        assertEquals(3, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(ArrayList.class)));
        assertTrue(subtypes.contains(new NamedType(List.class)));
    }

    @Test
    public void testCollectAndResolveSubtypesByClass() {
        StdSubtypeResolver subtypeResolver = new StdSubtypeResolver();
        subtypeResolver.registerSubtypes(new NamedType(StdSubtypeResolver.class));
        subtypeResolver.registerSubtypes(new NamedType(StdSubtypeResolverTest.class));

        AnnotatedClass annotatedClass = AnnotatedClassResolver.resolveWithoutSuperTypes(config, StdSubtypeResolver.class);
        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(config, annotatedClass);

        assertEquals(1, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(StdSubtypeResolverTest.class)));
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId() {
        StdSubtypeResolver subtypeResolver = new StdSubtypeResolver();
        subtypeResolver.registerSubtypes(new NamedType(StdSubtypeResolver.class, "StdSubtypeResolver"));
        subtypeResolver.registerSubtypes(new NamedType(StdSubtypeResolverTest.class, "StdSubtypeResolverTest"));

        AnnotatedClass annotatedClass = AnnotatedClassResolver.resolveWithoutSuperTypes(config, StdSubtypeResolver.class);
        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByTypeId(config, null, null);

        assertEquals(2, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(StdSubtypeResolver.class, "StdSubtypeResolver")));
        assertTrue(subtypes.contains(new NamedType(StdSubtypeResolverTest.class, "StdSubtypeResolverTest")));
    }
}