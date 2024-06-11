package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;

import java.util.*;

public class StdSubtypeResolverTest {
    private StdSubtypeResolver resolver;

    @Before
    public void setUp() {
        resolver = new StdSubtypeResolver();
    }

    @Test
    public void testRegisterSubtypes() {
        NamedType[] types = new NamedType[]{
            new NamedType(Subtype1.class),
            new NamedType(Subtype2.class)
        };

        resolver.registerSubtypes(types);

        Collection<NamedType> registeredSubtypes = resolver._registeredSubtypes;
        Assert.assertEquals(2, registeredSubtypes.size());
        for (NamedType type : types) {
            Assert.assertTrue(registeredSubtypes.contains(type));
        }
    }

    @Test
    public void testRegisterSubtypesWithClasses() {
        resolver.registerSubtypes(Subtype1.class, Subtype2.class);

        Collection<NamedType> registeredSubtypes = resolver._registeredSubtypes;
        Assert.assertEquals(2, registeredSubtypes.size());
        Assert.assertTrue(registeredSubtypes.contains(new NamedType(Subtype1.class)));
        Assert.assertTrue(registeredSubtypes.contains(new NamedType(Subtype2.class)));
    }

    @Test
    public void testCollectAndResolveSubtypesByClass() {
        MapperConfig<?> config = new MapperConfig<Object>() {
            @Override
            public AnnotationIntrospector getAnnotationIntrospector() {
                return null;
            }
        };
        AnnotatedMember property = null;
        JavaType baseType = null;

        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, property, baseType);

        Assert.assertNotNull(subtypes);
        Assert.assertEquals(0, subtypes.size());
    }

    @Test
    public void testCollectAndResolveSubtypesByClassWithConfigAndType() {
        MapperConfig<?> config = new MapperConfig<Object>() {
            @Override
            public AnnotationIntrospector getAnnotationIntrospector() {
                return null;
            }
        };
        AnnotatedMember property = null;
        JavaType baseType = null;

        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, property, baseType);

        Assert.assertNotNull(subtypes);
        Assert.assertEquals(0, subtypes.size());
    }

    @Test
    public void testCollectAndResolveSubtypesByClassWithConfigAndAnnotatedType() {
        MapperConfig<?> config = new MapperConfig<Object>() {
            @Override
            public AnnotationIntrospector getAnnotationIntrospector() {
                return null;
            }
        };
        AnnotatedClass type = null;

        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, type);

        Assert.assertNotNull(subtypes);
        Assert.assertEquals(0, subtypes.size());
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId() {
        MapperConfig<?> config = new MapperConfig<Object>() {
            @Override
            public AnnotationIntrospector getAnnotationIntrospector() {
                return null;
            }
        };
        AnnotatedMember property = null;
        JavaType baseType = null;

        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(config, property, baseType);

        Assert.assertNotNull(subtypes);
        Assert.assertEquals(0, subtypes.size());
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdWithConfigAndType() {
        MapperConfig<?> config = new MapperConfig<Object>() {
            @Override
            public AnnotationIntrospector getAnnotationIntrospector() {
                return null;
            }
        };
        AnnotatedMember property = null;
        JavaType baseType = null;

        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(config, property, baseType);

        Assert.assertNotNull(subtypes);
        Assert.assertEquals(0, subtypes.size());
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdWithConfigAndAnnotatedClass() {
        MapperConfig<?> config = new MapperConfig<Object>() {
            @Override
            public AnnotationIntrospector getAnnotationIntrospector() {
                return null;
            }
        };
        AnnotatedClass baseType = null;

        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(config, baseType);

        Assert.assertNotNull(subtypes);
        Assert.assertEquals(0, subtypes.size());
    }

    @Test
    public void testInternalMethods() {
        AnnotatedClass annotatedType = null;
        NamedType namedType = null;
        MapperConfig<?> config = new MapperConfig<Object>() {
            @Override
            public AnnotationIntrospector getAnnotationIntrospector() {
                return null;
            }
        };
        AnnotationIntrospector ai = null;
        HashMap<NamedType, NamedType> collectedSubtypes = new HashMap<>();

        resolver._collectAndResolve(annotatedType, namedType, config, ai, collectedSubtypes);
        Assert.assertEquals(0, collectedSubtypes.size());

        collectorSubtypesByTypeId(annotatedType, namedType, config, new HashSet<>(), new LinkedHashMap<>());
        Assert.assertEquals(0, collectedSubtypes.size());
    }

    private void collectorSubtypesByTypeId(AnnotatedClass annotatedType, NamedType namedType, MapperConfig<?> config,
            Set<Class<?>> typesHandled, Map<String, NamedType> byName) {
        if (!namedType.hasName()) {
            String name = config.getAnnotationIntrospector().findTypeName(annotatedType);
            if (name != null) {
                namedType = new NamedType(namedType.getType(), name);
            }
        }
        if (namedType.hasName()) {
            byName.put(namedType.getName(), namedType);
        }

        if (typesHandled.add(namedType.getType())) {
            Collection<NamedType> st = config.getAnnotationIntrospector().findSubtypes(annotatedType);
            if (st != null && !st.isEmpty()) {
                for (NamedType subtype : st) {
                    AnnotatedClass subtypeClass = AnnotatedClassResolver.resolveWithoutSuperTypes(config,
                            subtype.getType());
                    collectorSubtypesByTypeId(subtypeClass, subtype, config, typesHandled, byName);
                }
            }
        }
    }

    private static class Subtype1 {}
    private static class Subtype2 {}
}