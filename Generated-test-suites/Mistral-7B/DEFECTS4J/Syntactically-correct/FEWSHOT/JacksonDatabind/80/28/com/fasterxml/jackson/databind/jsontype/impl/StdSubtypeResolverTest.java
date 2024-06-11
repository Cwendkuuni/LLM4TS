package com.fasterxml.jackson.databind.jsontype.impl;

import static com.fasterxml.jackson.core.util.TestUtils.configureMapper;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonTypeNameSeparator;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.NameBasedTypeResolverBase;

public class StdSubtypeResolverTest {

    @Test
    public void testRegisterSubtypes() {
        MapperConfig<?> config = configureMapper(newMapperConfig());
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        // Test registration with null argument
        resolver.registerSubtypes(null);
        assertNull(resolver._registeredSubtypes);

        // Test registration with empty array
        resolver.registerSubtypes(new NamedType[0]);
        assertNull(resolver._registeredSubtypes);

        // Test registration with valid arguments
        resolver.registerSubtypes(new NamedType[]{ new NamedType(String[].class, "StringArray") });
        Set<NamedType> registeredSubtypes = resolver._registeredSubtypes;
        assertEquals(1, registeredSubtypes.size());
        assertEquals("StringArray", registeredSubtypes.iterator().next().getName());

        resolver.registerSubtypes(new NamedType[]{ new NamedType(Integer[].class, "IntArray"),
                new NamedType(Character[].class, "CharArray") });
        assertEquals(3, registeredSubtypes.size());
        List<NamedType> registeredTypes = new ArrayList<>(registeredSubtypes);
        assertEquals("IntArray", registeredTypes.get(1).getName());
        assertEquals("CharArray", registeredTypes.get(2).getName());
    }

    @Test
    public void testCollectAndResolveSubtypesByClass() {
        MapperConfig<?> config = configureMapper(newMapperConfig());
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        resolver.registerSubtypes(new NamedType[]{ new NamedType(String[].class, "StringArray") });

        // Test empty collection
        Collection<NamedType> collected = resolver.collectAndResolveSubtypesByClass(config, null, null);
        assertNull(collected);

        // Test null baseType
        collected = resolver.collectAndResolveSubtypesByClass(config, null, null);
        assertNull(collected);

        // Test null property
        collected = resolver.collectAndResolveSubtypesByClass(config, null, new JavaType(String[].class));
        assertNull(collected);

        // Test baseType that is not a collection
        collected = resolver.collectAndResolveSubtypesByClass(config, null, new JavaType(Integer.class));
        assertNull(collected);

        // Test baseType that is a collection with registered subtypes
        collected = resolver.collectAndResolveSubtypesByClass(config, null, new JavaType(List.class));
        assertEquals(3, collected.size());
        List<NamedType> collectedTypes = new ArrayList<>(collected);
        assertEquals("StringArray", collectedTypes.get(0).getName());
        assertEquals("IntArray", collectedTypes.get(1).getName());
        assertEquals("CharArray", collectedTypes.get(2).getName());

        // Test baseType that is a collection with annotated subtypes
        resolver.registerSubtypes(new NamedType[]{ new NamedType(Integer[].class, "IntArrayAnnotated"),
                new NamedType(Long[].class, "LongArrayAnnotated") });
        collected = resolver.collectAndResolveSubtypesByClass(config, null, new JavaType(List.class));
        assertEquals(5, collected.size());
        collectedTypes = new ArrayList<>(collected);
        assertEquals("StringArray", collectedTypes.get(0).getName());
        assertEquals("IntArray", collectedTypes.get(1).getName());
        assertEquals("IntArrayAnnotated", collectedTypes.get(2).getName());
        assertEquals("LongArrayAnnotated", collectedTypes.get(3).getName());
        assertEquals("CharArray", collectedTypes.get(4).getName());
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId() {
        MapperConfig<?> config = configureMapper(newMapperConfig());
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        resolver.registerSubtypes(new NamedType[]{ new NamedType(String[].class, "StringArray") });

        // Test empty collections
        Collection<NamedType> collected = resolver.collectAndResolveSubtypesByTypeId(config, null, null);
        assertNull(collected);

        collected = resolver.collectAndResolveSubtypesByTypeId(config, null, new JavaType(String[].class));
        assertNull(collected);

        // Test null baseType
        collected = resolver.collectAndResolveSubtypesByTypeId(config, null, null);
        assertNull(collected);

        // Test baseType that is not a collection
        collected = resolver.collectAndResolveSubtypesByTypeId(config, null, new JavaType(Integer.class));
        assertNull(collected);

        // Test baseType that is a collection with registered subtypes
        collected = resolver.collectAndResolveSubtypesByTypeId(config, null, new JavaType(List.class));
        assertEquals(3, collected.size());
        List<NamedType> collectedTypes = new ArrayList<>(collected);
        assertEquals("StringArray", collectedTypes.get(0).getName());
        assertEquals("IntArray", collectedTypes.get(1).getName());
        assertEquals("CharArray", collectedTypes.get(2).getName());

        // Test baseType that is a collection with annotated subtypes
        resolver.registerSubtypes(new NamedType[]{ new NamedType(Integer[].class, "IntArrayAnnotated"),
                new NamedType(Long[].class, "LongArrayAnnotated") });
        collected = resolver.collectAndResolveSubtypesByTypeId(config, null, new JavaType(List.class));
        assertEquals(5, collected.size());
        collectedTypes = new ArrayList<>(collected);
        assertEquals("StringArray", collectedTypes.get(0).getName());
        assertEquals("IntArray", collectedTypes.get(1).getName());
        assertEquals("IntArrayAnnotated", collectedTypes.get(2).getName());
        assertEquals("LongArrayAnnotated", collectedTypes.get(3).getName());
        assertEquals("CharArray", collectedTypes.get(4).getName());

        // Test baseType with explicit type id
        JavaType baseType = TypeFactory.defaultInstance().constructType(List.class);
        JavaType subType1 = TypeFactory.defaultInstance().constructType(Integer[].class);
        JavaType subType2 = TypeFactory.defaultInstance().constructType(Long[].class);
        JavaType subTypeId1 = TypeFactory.defaultInstance().constructType(Integer[].class.getName());
        JavaType subTypeId2 = TypeFactory.defaultInstance().constructType(Long[].class.getName());
        collected = resolver.collectAndResolveSubtypesByTypeId(config, null, subTypeId1);
        assertEquals(3, collected.size());
        collectedTypes = new ArrayList<>(collected);
        assertEquals("StringArray", collectedTypes.get(0).getName());
        assertEquals("IntArray", collectedTypes.get(1).getName());
        assertEquals("CharArray", collectedTypes.get(2).getName());

        collected = resolver.collectAndResolveSubtypesByTypeId(config, null, subTypeId2);
        assertEquals(5, collected.size());
        collectedTypes = new ArrayList<>(collected);
        assertEquals("StringArray", collectedTypes.get(0).getName());
        assertEquals("IntArray", collectedTypes.get(1).getName());
        assertEquals("IntArrayAnnotated", collectedTypes.get(2).getName());
        assertEquals("LongArrayAnnotated", collectedTypes.get(3).getName());
        assertEquals("CharArray", collectedTypes.get(4).getName());

        // Test baseType with explicit type id and annotation
        Class<JsonTypeName> JsonTypeNameAnnotation = JsonTypeName.class;
        Class<JsonTypeNameSeparator> JsonTypeNameSeparatorAnnotation = JsonTypeNameSeparator.class;
        List<Annotation> annotations1 = Arrays.asList(new JsonTypeName("IntArrayExplicit"),
                new JsonTypeNameSeparator("_"));
        List<Annotation> annotations2 = Arrays.asList(new JsonTypeName("LongArrayExplicit"),
                new JsonTypeNameSeparator("_"));
        JavaType subTypeAnnotated1 = TypeFactory.constructType(Integer[].class, annotations1.toArray(new Annotation[0]));
        JavaType subTypeAnnotated2 = TypeFactory.constructType(Long[].class, annotations2.toArray(new Annotation[0]));
        collected = resolver.collectAndResolveSubtypesByTypeId(config, null, subTypeAnnotated1);
        assertEquals(3, collected.size());
        collectedTypes = new ArrayList<>(collected);
        assertEquals("StringArray", collectedTypes.get(0).getName());
        assertEquals("IntArray", collectedTypes.get(1).getName());
        assertEquals("IntArrayExplicit", collectedTypes.get(2).getName());

        collected = resolver.collectAndResolveSubtypesByTypeId(config, null, subTypeAnnotated2);
        assertEquals(5, collected.size());
        collectedTypes = new ArrayList<>(collected);
        assertEquals("StringArray", collectedTypes.get(0).getName());
        assertEquals("IntArray", collectedTypes.get(1).getName());
        assertEquals("IntArrayAnnotated", collectedTypes.get(2).getName());
        assertEquals("LongArrayAnnotated", collectedTypes.get(3).getName());
        assertEquals("CharArray", collectedTypes.get(4).getName());
        assertEquals("LongArrayExplicit", collectedTypes.get(5).getName());
    }

    @Test
    public void testCombineNamedAndUnnamed() {
        MapperConfig<?> config = configureMapper(newMapperConfig());
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        resolver.registerSubtypes(new NamedType[]{ new NamedType(String[].class, "StringArray") });

        // Test empty collection
        Collection<NamedType> collected = resolver._combineNamedAndUnnamed(String[].class, new HashSet<>(), new LinkedHashMap<>());
        assertNull(collected);

        // Test collection with no explicit names
        Set<Class<?>> handledTypes = new HashSet<>();
        handledTypes.add(String[].class);
        Map<String, NamedType> namedTypes = new LinkedHashMap<>();
        collected = resolver._combineNamedAndUnnamed(String[].class, handledTypes, namedTypes);
        assertNotNull(collected);
        List<NamedType> collectedTypes = new ArrayList<>(collected);
        assertEquals(4, collectedTypes.size());
        assertEquals("StringArray", collectedTypes.get(0).getName());
        assertEquals("IntArray", collectedTypes.get(1).getName());
        assertEquals("IntArrayAnnotated", collectedTypes.get(2).getName());
        assertEquals("CharArray", collectedTypes.get(3).getName());

        // Test collection with explicit names
        handledTypes.clear();
        namedTypes.clear();
        handledTypes.add(String[].class);
        handledTypes.add(Integer[].class);
        collected = resolver._combineNamedAndUnnamed(String[].class, handledTypes, namedTypes);
        assertNotNull(collected);
        collectedTypes = new ArrayList<>(collected);
        assertEquals(5, collectedTypes.size());
        assertEquals("StringArray", collectedTypes.get(0).getName());
        assertEquals("IntArray", collectedTypes.get(1).getName());
        assertEquals("IntArrayAnnotated", collectedTypes.get(2).getName());
        assertEquals("CharArray", collectedTypes.get(3).getName());
        assertEquals("IntArray", collectedTypes.get(4).getName());
    }

    private MapperConfig<?> newMapperConfig() {
        MapperConfig<?> config = new MapperConfig<>();
        config.disable(MapperFeature.AUTO_DETECT_TYPES);
        return config;
    }
}