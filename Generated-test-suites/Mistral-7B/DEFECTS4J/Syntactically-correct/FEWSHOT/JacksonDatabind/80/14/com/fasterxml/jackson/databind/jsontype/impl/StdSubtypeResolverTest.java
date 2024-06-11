package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.AnnotatedClass;
import com.fasterxml.jackson.databind.util.AnnotatedMember;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.Converter.StdConverter;

public class StdSubtypeResolverTest {

    @Test
    public void testRegisterSubtypes() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        Class<String> baseType = String.class;
        resolver.registerSubtypes(new NamedType(StringBuilder.class));
        resolver.registerSubtypes(new NamedType(StringBuffer.class));
        List<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(new MapperConfig.StandardConfig(), null, baseType);
        assertTrue(subtypes.size() > 0);
        assertTrue(subtypes.contains(new NamedType(StringBuilder.class)));
        assertTrue(subtypes.contains(new NamedType(StringBuffer.class)));
    }

    @Test
    public void testRegisterSubtypesWithBaseType() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        Class<String> baseType = String.class;
        resolver.registerSubtypes(baseType, new NamedType(StringBuilder.class));
        List<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(new MapperConfig.StandardConfig(), null, baseType);
        assertTrue(subtypes.size() > 0);
        assertTrue(subtypes.contains(new NamedType(StringBuilder.class)));
    }

    @Test
    public void testCollectAndResolveSubtypesByClass() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        MapperConfig<?> config = new MapperConfig.StandardConfig();
        config.enable(MapperFeature.AUTO_DETECT_TYPES);
        Class<?> baseType = Object.class;
        List<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, null, baseType);
        assertTrue(subtypes.isEmpty());

        baseType = String.class;
        subtypes = resolver.collectAndResolveSubtypesByClass(config, null, baseType);
        assertTrue(subtypes.size() > 0);
        for (NamedType type : subtypes) {
            assertNotNull(type.getName());
        }

        // Test with custom subtypes registration
        resolver = new StdSubtypeResolver();
        baseType = String.class;
        resolver.registerSubtypes(new NamedType(StringBuilder.class));
        resolver.registerSubtypes(new NamedType(StringBuffer.class));
        subtypes = resolver.collectAndResolveSubtypesByClass(config, null, baseType);
        assertTrue(subtypes.size() > 0);
        assertTrue(subtypes.containsAll(List.of(new NamedType(StringBuilder.class), new NamedType(StringBuffer.class))));

        // Test with null baseType
        subtypes = resolver.collectAndResolveSubtypesByClass(config, null, null);
        assertTrue(subtypes.isEmpty());
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        MapperConfig<?> config = new MapperConfig.StandardConfig();
        config.enable(MapperFeature.AUTO_DETECT_TYPES);
        Class<String> baseType = String.class;
        List<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(config, null, baseType);
        assertTrue(subtypes.isEmpty());

        baseType = String.class;
        resolver.registerSubtypes(new NamedType(StringBuilder.class));
        subtypes = resolver.collectAndResolveSubtypesByTypeId(config, null, baseType);
        assertTrue(subtypes.size() > 0);
        assertTrue(subtypes.contains(new NamedType(StringBuilder.class)));

        baseType = StringBuilder.class;
        subtypes = resolver.collectAndResolveSubtypesByTypeId(config, null, baseType);
        assertTrue(subtypes.isEmpty());

        // Test with null baseType
        subtypes = resolver.collectAndResolveSubtypesByTypeId(config, null, null);
        assertTrue(subtypes.isEmpty());
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdWithCustomSubtypesRegistration() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        MapperConfig<?> config = new MapperConfig.StandardConfig();
        config.enable(MapperFeature.AUTO_DETECT_TYPES);
        Class<String> baseType = String.class;
        resolver.registerSubtypes(new NamedType(StringBuilder.class));
        resolver.registerSubtypes(new NamedType(StringBuffer.class));
        List<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(config, null, baseType);
        assertTrue(subtypes.size() > 0);
        assertTrue(subtypes.containsAll(List.of(new NamedType(StringBuilder.class), new NamedType(StringBuffer.class))));
    }

    @Test
    public void testCombineNamedAndUnnamed() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        MapperConfig<?> config = new MapperConfig.StandardConfig();
        config.enable(MapperFeature.AUTO_DETECT_TYPES);
        Class<?> baseType = Object.class;
        List<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, null, baseType);
        assertTrue(subtypes.isEmpty());

        baseType = String.class;
        subtypes = resolver.collectAndResolveSubtypesByClass(config, null, baseType);
        List<NamedType> combinedSubtypes = resolver._combineNamedAndUnnamed(baseType.getClass(), new HashSet<>(), new LinkedHashMap<>());
        assertTrue(combinedSubtypes.size() > 0);
        for (NamedType type : combinedSubtypes) {
            if (type.getName() == null) {
                assertTrue(ClassUtil.isSubTypeOf(baseType, type.getType()));
            } else {
                assertTrue(type.getName().equals(type.getType().getSimpleName()));
            }
        }
    }
}