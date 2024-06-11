package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.NamedType;
import com.fasterxml.jackson.databind.jsontype.StdClassIdResolver;
import com.fasterxml.jackson.databind.jsontype.impl.StdSubtypeResolver;

public class StdSubtypeResolverTest {

    private static final java.lang.Class<?> RAW_BASE = Object.class;

    @Test
    public void testRegisterSubtypesByClass() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        Class<?> clazzA = Integer.class;
        NamedType namedTypeA = new NamedType(clazzA, "A");
        Class<?> clazzB = Long.class;
        NamedType namedTypeB = new NamedType(clazzB, "B");

        resolver.registerSubtypes(namedTypeA, namedTypeB);

        Set<NamedType> subtypes = resolver.getRegisteredSubtypes();
        assertEquals(2, subtypes.size());
        assertSame(namedTypeA, subtypes.iterator().next());
        assertSame(namedTypeB, subtypes.iterator().next());
    }

    @Test
    public void testRegisterSubtypesByName() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        Class<?> clazzA = Integer.class;
        String nameA = "A";
        NamedType namedTypeA = new NamedType(clazzA, nameA);
        Class<?> clazzB = Long.class;
        String nameB = "B";
        NamedType namedTypeB = new NamedType(clazzB, nameB);

        resolver.registerSubtypes(namedTypeA, namedTypeB);

        Set<NamedType> subtypes = resolver.getRegisteredSubtypes();
        assertEquals(2, subtypes.size());
        assertSame(namedTypeA, subtypes.iterator().next());
        assertSame(namedTypeB, subtypes.iterator().next());
    }

    @Test
    public void testResolveSubtypesByClass_Empty() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        MapperConfig<?> config = new MapperConfig.Builder().build();
        JavaType baseType = null;
        AnnotatedMember property = null;

        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, property, baseType);
        assertEquals(0, subtypes.size());
    }

    @Test
    public void testResolveSubtypesByClass_Annotated() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        MapperConfig<?> config = new MapperConfig.Builder().build();
        AnnotatedClass type = AnnotatedClass.constructFromClass(Integer.class, config);
        JavaType baseType = JavaType.construct(Integer.class, config);

        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, type, baseType);
        assertEquals(1, subtypes.size());
        assertSame(new NamedType(Integer.class, "INTEGER"), subtypes.iterator().next());
    }

    @Test
    public void testResolveSubtypesByClass_Registered() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        MapperConfig<?> config = new MapperConfig.Builder().build();
        AnnotatedClass type = AnnotatedClass.constructFromClass(Integer.class, config);
        JavaType baseType = JavaType.construct(Integer.class, config);

        Class<?> clazzA = Integer.class;
        String nameA = "A";
        NamedType namedTypeA = new NamedType(clazzA, nameA);
        Class<?> clazzB = Long.class;
        String nameB = "B";
        NamedType namedTypeB = new NamedType(clazzB, nameB);

        resolver.registerSubtypes(namedTypeA, namedTypeB);

        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, type, baseType);
        assertEquals(2, subtypes.size());
        assertSame(namedTypeA, subtypes.iterator().next());
        assertSame(namedTypeB, subtypes.iterator().next());
    }

    @Test
    public void testResolveSubtypesByClass_Multiple() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        MapperConfig<?> config = new MapperConfig.Builder().build();
        AnnotatedClass type = AnnotatedClass.constructFromClass(Integer.class, config);
        JavaType baseType = JavaType.construct(Integer.class, config);

        Class<?> clazzA = Integer.class;
        String nameA = "A";
        NamedType namedTypeA = new NamedType(clazzA, nameA);
        Class<?> clazzB = Long.class;
        String nameB = "B";
        NamedType namedTypeB = new NamedType(clazzB, nameB);
        Class<?> clazzC = Short.class;
        String nameC = "C";
        NamedType namedTypeC = new NamedType(clazzC, nameC);

        resolver.registerSubtypes(namedTypeA, namedTypeB, namedTypeC);

        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, type, baseType);
        assertEquals(3, subtypes.size());
        assertSame(namedTypeA, subtypes.iterator().next());
        assertSame(namedTypeB, subtypes.iterator().next());
        assertSame(namedTypeC, subtypes.iterator().next());
    }

    @Test
    public void testResolveSubtypesByTypeId_Empty() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        MapperConfig<?> config = new MapperConfig.Builder().build();
        AnnotatedMember property = null;
        JavaType baseType = null;

        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(config, property, baseType);
        assertEquals(0, subtypes.size());
    }

    @Test
    public void testResolveSubtypesByTypeId_Annotated() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        MapperConfig<?> config = new MapperConfig.Builder().build();
        config.disable(MapperFeature.AUTO_DETECT_CREATORS);
        AnnotatedMember property = AnnotatedMember.construct(Integer.class, "value", config);
        JavaType baseType = JavaType.construct(Integer[].class, config);

        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(config, property, baseType);
        assertEquals(1, subtypes.size());
        assertSame(new NamedType(Integer[].class, "INT_ARRAY"), subtypes.iterator().next());
    }

    @Test
    public void testResolveSubtypesByTypeId_Registered() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        MapperConfig<?> config = new MapperConfig.Builder().build();
        config.disable(MapperFeature.AUTO_DETECT_CREATORS);
        Class<?> rawBase = Integer.class;

        Map<String, NamedType> byName = new LinkedHashMap<>();
        Set<Class<?>> typesHandled = new HashSet<>();

        // Register subtypes by name
        byName.put("A", new NamedType(Integer.TYPE, "A"));
        byName.put("B", new NamedType(Long.TYPE, "B"));

        resolver._collectAndResolveByTypeId(AnnotatedClass.constructFromClass(Integer.class, config),
                new NamedType(rawBase, null), config, typesHandled, byName);

        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(config, null, null);
        assertEquals(2, subtypes.size());
        assertSame(byName.get("A"), subtypes.iterator().next());
        assertSame(byName.get("B"), subtypes.iterator().next());
    }

    @Test
    public void testResolveSubtypesByTypeId_Multiple() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        MapperConfig<?> config = new MapperConfig.Builder().build();
        config.disable(MapperFeature.AUTO_DETECT_CREATORS);
        Class<?> rawBase = Integer.class;

        Map<String, NamedType> byName = new LinkedHashMap<>();
        Set<Class<?>> typesHandled = new HashSet<>();

        // Register subtypes by name
        byName.put("A", new NamedType(Integer.TYPE, "A"));
        byName.put("B", new NamedType(Long.TYPE, "B"));
        byName.put("C", new NamedType(Short.TYPE, "C"));

        resolver._collectAndResolveByTypeId(AnnotatedClass.constructFromClass(Integer.class, config),
                new NamedType(rawBase, null), config, typesHandled, byName);

        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(config, null, null);
        assertEquals(3, subtypes.size());
        assertSame(byName.get("A"), subtypes.iterator().next());
        assertSame(byName.get("B"), subtypes.iterator().next());
        assertSame(byName.get("C"), subtypes.iterator().next());
    }

    @Test
    public void testResolveSubtypesByTypeId_SubtypesByClass() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        MapperConfig<?> config = new MapperConfig.Builder().build();
        config.disable(MapperFeature.AUTO_DETECT_CREATORS);
        Class<?> rawBase = Integer.class;

        Set<Class<?>> typesHandled = new HashSet<>();

        // Register subtypes by class
        resolver.registerSubtypes(new NamedType(Integer.TYPE, "A"));
        resolver.registerSubtypes(new NamedType(Long.TYPE, "B"));
        resolver.registerSubtypes(new NamedType(Short.TYPE, "C"));

        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(config, null, null);
        assertEquals(3, subtypes.size());
        assertSame(new NamedType(Integer.TYPE, "A"), subtypes.iterator().next());
        assertSame(new NamedType(Long.TYPE, "B"), subtypes.iterator().next());
        assertSame(new NamedType(Short.TYPE, "C"), subtypes.iterator().next());

        // Make sure they were handled
        assertFalse(typesHandled.isEmpty());
        assertEquals(3, typesHandled.size());
        assertTrue(typesHandled.contains(Integer.TYPE));
        assertTrue(typesHandled.contains(Long.TYPE));
        assertTrue(typesHandled.contains(Short.TYPE));
    }

    @Test
    public void testResolveSubtypesByTypeId_SubtypesByName() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        MapperConfig<?> config = new MapperConfig.Builder().build();
        config.disable(MapperFeature.AUTO_DETECT_CREATORS);
        Class<?> rawBase = Integer.class;

        Map<String, NamedType> byName = new LinkedHashMap<>();

        // Register subtypes by name
        byName.put("A", new NamedType(Integer.TYPE, "A"));
        byName.put("B", new NamedType(Long.TYPE, "B"));
        byName.put("C", new NamedType(Short.TYPE, "C"));

        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(config, null, null);
        assertEquals(3, subtypes.size());
        assertSame(byName.get("A"), subtypes.iterator().next());
        assertSame(byName.get("B"), subtypes.iterator().next());
        assertSame(byName.get("C"), subtypes.iterator().next());

        // Make sure they were handled
        assertFalse(subtypes.isEmpty());
        assertEquals(3, subtypes.size());
        assertTrue(subtypes.contains(byName.get("A")));
        assertTrue(subtypes.contains(byName.get("B")));
        assertTrue(subtypes.contains(byName.get("C")));
    }

    @Test
    public void testResolveSubtypesByTypeId_CombineNamedAndUnnamed() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        MapperConfig<?> config = new MapperConfig.Builder().build();
        config.disable(MapperFeature.AUTO_DETECT_CREATORS);
        Class<?> rawBase = Integer.class;

        Map<String, NamedType> byName = new LinkedHashMap<>();

        // Register subtypes by name
        byName.put("A", new NamedType(Integer.TYPE, "A"));
        byName.put("B", new NamedType(Long.TYPE, "B"));

        // Register subtypes without explicit name
        resolver.registerSubtypes(new NamedType(Short.TYPE));

        Collection<NamedType> subtypes = resolver._combineNamedAndUnnamed(rawBase, null, byName);
        assertEquals(3, subtypes.size());
        assertSame(byName.get("A"), subtypes.iterator().next());
        assertSame(byName.get("B"), subtypes.iterator().next());
        assertSame(new NamedType(Short.TYPE), subtypes.iterator().next());
    }
}