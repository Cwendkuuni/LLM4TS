package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperConfig;
import com.fasterxml.jackson.databind.cfg.MapperConfig.DefaultInitTracker;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
import com.fasterxml.jackson.databind.jsontype.impl.StdSubtypeResolver;

/**
 * Test class for StdSubtypeResolver.
 */
public class StdSubtypeResolverTest {

    private static final Class<?> BASE_CLASS = Object.class;
    private static final Class<?> SUBTYPE_1 = String.class;
    private static final Class<?> SUBTYPE_2 = Integer.class;

    @Test
    public void testRegisterSubtypes() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        // Test registering subtypes using classes
        resolver.registerSubtypes(new Class<?>[] { SUBTYPE_1, SUBTYPE_2 });
        Set<NamedType> registeredSubtypes = resolver.getRegisteredSubtypes();
        List<Class<?>> registeredClasses = new ArrayList<>();
        for (NamedType namedType : registeredSubtypes) {
            registeredClasses.add(namedType.getType());
        }
        assertEquals(2, registeredClasses.size());
        assertTrue(registeredClasses.contains(SUBTYPE_1));
        assertTrue(registeredClasses.contains(SUBTYPE_2));
        // Test registering subtypes using NamedType
        NamedType namedType1 = new NamedType(SUBTYPE_1, "StringSubtype");
        NamedType namedType2 = new NamedType(SUBTYPE_2, "IntegerSubtype");
        resolver.registerSubtypes(namedType1, namedType2);
        registeredSubtypes = resolver.getRegisteredSubtypes();
        assertEquals(2, registeredSubtypes.size());
        assertTrue(registeredSubtypes.contains(namedType1));
        assertTrue(registeredSubtypes.contains(namedType2));
    }

    @Test
    public void testCollectAndResolveSubtypesByClassSerialization() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        MapperConfig<Object> config = new MapperConfig.DefaultConfig();
        AnnotationIntrospector ai = config.getAnnotationIntrospector();
        JavaType baseType = JavaType.construct(BASE_CLASS);
        // Test empty collection
        List<NamedType> result = resolver.collectAndResolveSubtypesByClass(config, null, baseType);
        assertEquals(0, result.size());
        // Test no registered subtypes
        resolver = new StdSubtypeResolver();
        result = resolver.collectAndResolveSubtypesByClass(config, null, baseType);
        assertEquals(0, result.size());
        // Test registered subtypes using classes
        resolver.registerSubtypes(new Class<?>[] { SUBTYPE_1, SUBTYPE_2 });
        result = resolver.collectAndResolveSubtypesByClass(config, null, baseType);
        assertEquals(2, result.size());
        assertTrue(result.get(0).getType().isAssignableFrom(BASE_CLASS));
        assertTrue(result.get(0).getClass().getName().startsWith("com.fasterxml.jackson.databind.jsontype.impl.StdSubtypeResolver$NamedType"));
        assertTrue(result.get(0).getName() != null);
        assertTrue(result.get(1).getType().isAssignableFrom(BASE_CLASS));
        assertTrue(result.get(1).getClass().getName().startsWith("com.fasterxml.jackson.databind.jsontype.impl.StdSubtypeResolver$NamedType"));
        assertTrue(result.get(1).getName() != null);
        // Test registered subtypes using NamedType
        NamedType namedType1 = new NamedType(SUBTYPE_1, "StringSubtype");
        NamedType namedType2 = new NamedType(SUBTYPE_2, "IntegerSubtype");
        resolver.registerSubtypes(namedType1, namedType2);
        result = resolver.collectAndResolveSubtypesByClass(config, null, baseType);
        assertEquals(2, result.size());
        assertTrue(result.get(0).getType().isAssignableFrom(BASE_CLASS));
        assertTrue(result.get(0).getClass().getName().startsWith("com.fasterxml.jackson.databind.jsontype.impl.StdSubtypeResolver$NamedType"));
        assertTrue(result.get(0).getName().equals("StringSubtype"));
        assertTrue(result.get(1).getType().isAssignableFrom(BASE_CLASS));
        assertTrue(result.get(1).getClass().getName().startsWith("com.fasterxml.jackson.databind.jsontype.impl.StdSubtypeResolver$NamedType"));
        assertTrue(result.get(1).getName().equals("IntegerSubtype"));
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdSerialization() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        MapperConfig<Object> config = new MapperConfig.DefaultConfig();
        AnnotationIntrospector ai = config.getAnnotationIntrospector();
        JavaType baseType = JavaType.construct(BASE_CLASS);
        // Test empty collection
        List<NamedType> result = resolver.collectAndResolveSubtypesByTypeId(config, null, baseType);
        assertEquals(0, result.size());
        // Test no registered subtypes
        result = resolver.collectAndResolveSubtypesByTypeId(config, null, baseType);
        assertEquals(0, result.size());
        // Test registered subtypes using classes
        resolver.registerSubtypes(new Class<?>[] { SUBTYPE_1, SUBTYPE_2 });
        result = resolver.collectAndResolveSubtypesByTypeId(config, null, baseType);
        assertEquals(2, result.size());
        assertTrue(result.get(0).getType().isAssignableFrom(BASE_CLASS));
        assertTrue(result.get(0).getClass().getName().startsWith("com.fasterxml.jackson.databind.jsontype.impl.StdSubtypeResolver$NamedType"));
        assertTrue(result.get(0).getName() != null);
        assertTrue(result.get(1).getType().isAssignableFrom(BASE_CLASS));
        assertTrue(result.get(1).getClass().getName().startsWith("com.fasterxml.jackson.databind.jsontype.impl.StdSubtypeResolver$NamedType"));
        assertTrue(result.get(1).getName() != null);
        // Test registered subtypes using NamedType
        NamedType namedType1 = new NamedType(SUBTYPE_1, "StringSubtype");
        NamedType namedType2 = new NamedType(SUBTYPE_2, "IntegerSubtype");
        resolver.registerSubtypes(namedType1, namedType2);
        result = resolver.collectAndResolveSubtypesByTypeId(config, null, baseType);
        assertEquals(2, result.size());
        assertTrue(result.get(0).getType().isAssignableFrom(BASE_CLASS));
        assertTrue(result.get(0).getClass().getName().startsWith("com.fasterxml.jackson.databind.jsontype.impl.StdSubtypeResolver$NamedType"));
        assertTrue(result.get(0).getName().equals("StringSubtype"));
        assertTrue(result.get(1).getType().isAssignableFrom(BASE_CLASS));
        assertTrue(result.get(1).getClass().getName().startsWith("com.fasterxml.jackson.databind.jsontype.impl.StdSubtypeResolver$NamedType"));
        assertTrue(result.get(1).getName().equals("IntegerSubtype"));
    }

    @Test
    public void testCollectAndResolveSubtypesByClassDeserialization() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        MapperConfig<Object> config = new MapperConfig.DefaultConfig();
        JavaType baseType = JavaType.construct(BASE_CLASS);
        // Test empty collection
        List<NamedType> result = resolver.collectAndResolveSubtypesByClass(config, null, baseType);
        assertEquals(0, result.size());
        // Test no registered subtypes
        result = resolver.collectAndResolveSubtypesByClass(config, null, baseType);
        assertEquals(0, result.size());
        // Test registered subtypes using classes
        resolver.registerSubtypes(new Class<?>[] { SUBTYPE_1, SUBTYPE_2 });
        result = resolver.collectAndResolveSubtypesByClass(config, null, baseType);
        assertEquals(2, result.size());
        assertTrue(result.get(0).getType().isAssignableFrom(BASE_CLASS));
        assertTrue(result.get(0).getClass().getName().startsWith("com.fasterxml.jackson.databind.jsontype.impl.StdSubtypeResolver$NamedType"));
        assertTrue(result.get(0).getName() != null);
        assertTrue(result.get(1).getType().isAssignableFrom(BASE_CLASS));
        assertTrue(result.get(1).getClass().getName().startsWith("com.fasterxml.jackson.databind.jsontype.impl.StdSubtypeResolver$NamedType"));
        assertTrue(result.get(1).getName() != null);
        // Test registered subtypes using NamedType
        NamedType namedType1 = new NamedType(SUBTYPE_1, "StringSubtype");
        NamedType namedType2 = new NamedType(SUBTYPE_2, "IntegerSubtype");
        resolver.registerSubtypes(namedType1, namedType2);
        result = resolver.collectAndResolveSubtypesByClass(config, null, baseType);
        assertEquals(2, result.size());
        assertTrue(result.get(0).getType().isAssignableFrom(BASE_CLASS));
        assertTrue(result.get(0).getClass().getName().startsWith("com.fasterxml.jackson.databind.jsontype.impl.StdSubtypeResolver$NamedType"));
        assertTrue(result.get(0).getName().equals("StringSubtype"));
        assertTrue(result.get(1).getType().isAssignableFrom(BASE_CLASS));
        assertTrue(result.get(1).getClass().getName().startsWith("com.fasterxml.jackson.databind.jsontype.impl.StdSubtypeResolver$NamedType"));
        assertTrue(result.get(1).getName().equals("IntegerSubtype"));
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdDeserialization() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        MapperConfig<Object> config = new MapperConfig.DefaultConfig();
        JavaType baseType = JavaType.construct(BASE_CLASS);
        // Test empty collection
        List<NamedType> result = resolver.collectAndResolveSubtypesByTypeId(config, null, baseType);
        assertEquals(0, result.size());
        // Test no registered subtypes
        result = resolver.collectAndResolveSubtypesByTypeId(config, null, baseType);
        assertEquals(0, result.size());
        // Test registered subtypes using classes
        resolver.registerSubtypes(new Class<?>[] { SUBTYPE_1, SUBTYPE_2 });
        result = resolver.collectAndResolveSubtypesByTypeId(config, null, baseType);
        assertEquals(2, result.size());
        assertTrue(result.get(0).getType().isAssignableFrom(BASE_CLASS));
        assertTrue(result.get(0).getClass().getName().startsWith("com.fasterxml.jackson.databind.jsontype.impl.StdSubtypeResolver$NamedType"));
        assertTrue(result.get(0).getName() != null);
        assertTrue(result.get(1).getType().isAssignableFrom(BASE_CLASS));
        assertTrue(result.get(1).getClass().getName().startsWith("com.fasterxml.jackson.databind.jsontype.impl.StdSubtypeResolver$NamedType"));
        assertTrue(result.get(1).getName() != null);
        // Test registered subtypes using NamedType
        NamedType namedType1 = new NamedType(SUBTYPE_1, "StringSubtype");
        NamedType namedType2 = new NamedType(SUBTYPE_2, "IntegerSubtype");
        resolver.registerSubtypes(namedType1, namedType2);
        result = resolver.collectAndResolveSubtypesByTypeId(config, null, baseType);
        assertEquals(2, result.size());
        assertTrue(result.get(0).getType().isAssignableFrom(BASE_CLASS));
        assertTrue(result.get(0).getClass().getName().startsWith("com.fasterxml.jackson.databind.jsontype.impl.StdSubtypeResolver$NamedType"));
        assertTrue(result.get(0).getName().equals("StringSubtype"));
        assertTrue(result.get(1).getType().isAssignableFrom(BASE_CLASS));
        assertTrue(result.get(1).getClass().getName().startsWith("com.fasterxml.jackson.databind.jsontype.impl.StdSubtypeResolver$NamedType"));
        assertTrue(result.get(1).getName().equals("IntegerSubtype"));
    }

}