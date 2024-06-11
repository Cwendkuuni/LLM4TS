package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
import com.fasterxml.jackson.databind.jsontype.impl.StdSubtypeResolver;

public class StdSubtypeResolverTest {

    private static final String TEST_PACKAGE = "testpackage.";
    private static final String TEST_CLASS_SUFFIX = "Test";

    private AnnotationIntrospector _ai;
    private MapperConfig<?> _config;
    private StdSubtypeResolver _resolver;

    @Before
    public void setUp() {
        _ai = new AnnotationIntrospector();
        _config = new MapperConfig(new AnnotationIntrospector());
        _resolver = new StdSubtypeResolver();
    }

    @Test
    public void testRegisterSubtypes() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        Class<?>[] classTypes = { String.class, Integer.class, Long.class };
        resolver.registerSubtypes(classTypes);
        Set<NamedType> registeredSubtypes = resolver.getRegisteredSubtypes();
        assertEquals(3, registeredSubtypes.size());
        for (NamedType subtype : registeredSubtypes) {
            assertTrue(subtype.hasName());
            assertEquals(subtype.getType().getName(), subtype.getName());
        }

        resolver.registerSubtypes(new NamedType(String[].class));
        registeredSubtypes = resolver.getRegisteredSubtypes();
        assertEquals(4, registeredSubtypes.size());
        NamedType arrayType = null;
        for (NamedType subtype : registeredSubtypes) {
            if (subtype.getType().isArray()) {
                arrayType = subtype;
                break;
            }
        }
        assertNotNull(arrayType);
        assertEquals(String[].class, arrayType.getType());
    }

    @Test
    public void testCollectAndResolveSubtypesByClass() {
        _resolver.registerSubtypes(new NamedType(String.class), new NamedType(Integer.class),
                new NamedType(Long.class));
        AnnotatedClass ac = AnnotatedClassResolver.resolveWithoutSuperTypes(_config, String.class);
        Collection<NamedType> subtypes = _resolver.collectAndResolveSubtypesByClass(_config, ac);
        assertEquals(3, subtypes.size());
        for (NamedType subtype : subtypes) {
            assertTrue(subtype.hasName());
            assertEquals(subtype.getType().getName(), subtype.getName());
        }

        String[] arrayString = new String[]{"test1", "test2"};
        ac = AnnotatedClassResolver.resolveWithoutSuperTypes(_config, arrayString.getClass());
        subtypes = _resolver.collectAndResolveSubtypesByClass(_config, ac);
        assertEquals(1, subtypes.size());
        NamedType arrayType = null;
        for (NamedType subtype : subtypes) {
            if (subtype.getType().isArray()) {
                arrayType = subtype;
                break;
            }
        }
        assertNotNull(arrayType);
        assertEquals(String[].class, arrayType.getType());
    }

    @Test
    public void testCollectAndResolveSubtypesByClassWithNullBaseType() {
        _resolver.registerSubtypes(new NamedType(String.class), new NamedType(Integer.class),
                new NamedType(Long.class));
        AnnotatedMember property = null;
        Collection<NamedType> subtypes = _resolver
                .collectAndResolveSubtypesByClass(_config, property, null);
        // no subtypes should be returned when baseType is null
        assertTrue(Collections.isEmpty(subtypes));
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId() {
        _resolver.registerSubtypes(new NamedType(String.class), new NamedType(Integer.class),
                new NamedType(Long.class));
        AnnotatedClass ac = AnnotatedClassResolver.resolveWithoutSuperTypes(_config, String.class);
        Collection<NamedType> subtypes = _resolver.collectAndResolveSubtypesByTypeId(_config, ac);
        assertEquals(3, subtypes.size());
        for (NamedType subtype : subtypes) {
            assertTrue(subtype.hasName());
            assertEquals(subtype.getType().getName(), subtype.getName());
        }

        AnnotatedClass arrayClass = AnnotatedClassResolver.resolveWithoutSuperTypes(_config,
                String[].class);
        subtypes = _resolver.collectAndResolveSubtypesByTypeId(_config, arrayClass);
        assertEquals(1, subtypes.size());
        NamedType arrayType = null;
        for (NamedType subtype : subtypes) {
            if (subtype.getType().isArray()) {
                arrayType = subtype;
                break;
            }
        }
        assertNotNull(arrayType);
        assertEquals(String[].class, arrayType.getType());
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdWithNullBaseType() {
        _resolver.registerSubtypes(new NamedType(String.class), new NamedType(Integer.class),
                new NamedType(Long.class));
        Collection<NamedType> subtypes = _resolver
                .collectAndResolveSubtypesByTypeId(_config, null);
        // no subtypes should be returned when baseType is null
        assertTrue(Collections.isEmpty(subtypes));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCollectAndResolveSubtypesByTypeIdWithEmptyList() {
        _resolver.registerSubtypes(new NamedType(String.class), new NamedType(Integer.class),
                new NamedType(Long.class));
        Collection<NamedType> subtypes = _resolver
                .collectAndResolveSubtypesByTypeId(_config, new ArrayList<>());
        // should throw IllegalArgumentException when the list is empty
        fail();
    }

    @Test
    public void testCombineNamedAndUnnamed() {
        _resolver.registerSubtypes(new NamedType(String.class));
        _resolver.registerSubtypes(new NamedType(Integer.class), new NamedType(Long.class));
        List<NamedType> subtypes = _resolver.collectAndResolveSubtypesByClass(_config,
                AnnotatedClassResolver.resolveWithoutSuperTypes(_config, Object.class));
        List<NamedType> result = _resolver._combineNamedAndUnnamed(Object.class, new HashSet<Class<?>>(),
                new LinkedHashMap<String, NamedType>());
        assertEquals(3, result.size());
        for (NamedType subtype : result) {
            if (subtype.hasName()) {
                assertTrue(subtype.getType().isAssignableFrom(String.class));
            } else {
                assertTrue(subtype.getType().isAssignableFrom(Object.class));
            }
        }
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdWithEmptySet() {
        _resolver.registerSubtypes(new NamedType(String.class));
        _resolver.registerSubtypes(new NamedType(Integer.class), new NamedType(Long.class));
        List<NamedType> subtypes = _resolver.collectAndResolveSubtypesByTypeId(_config, null);
        List<NamedType> result = _resolver._combineNamedAndUnnamed(Object.class, new HashSet<Class<?>>(),
                new LinkedHashMap<String, NamedType>());
        assertEquals(3, result.size());
        for (NamedType subtype : result) {
            if (subtype.hasName()) {
                assertTrue(subtype.getType().isAssignableFrom(String.class));
            } else {
                assertTrue(subtype.getType().isAssignableFrom(Object.class));
            }
        }
    }
}