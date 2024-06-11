package com.example.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ClassKey;

public class SimpleAbstractTypeResolverTest {

    private static final long serialVersionUID = 8635483102371490919L;

    private SimpleAbstractTypeResolver resolver;
    private DeserializationConfig config;

    @Before
    public void setup() {
        resolver = new SimpleAbstractTypeResolver();
        config = mock(DeserializationConfig.class);
    }

    @Test
    public void testAddMapping() {
        Class<String> superType = String.class;
        Class<Integer> subType = Integer.class;
        resolver.addMapping(superType, subType);
        assertEquals(subType, resolver._mappings.get(new ClassKey(superType)));
    }

    @Test
    public void testAddMappingSelf() {
        Class<String> superType = String.class;
        assertThrows(IllegalArgumentException.class,
                () -> resolver.addMapping(superType, superType));
    }

    @Test
    public void testAddMappingIncorrectSubType() {
        Class<String> superType = String.class;
        Class<Double> subType = Double.class;
        assertThrows(IllegalArgumentException.class,
                () -> resolver.addMapping(superType, subType));
    }

    @Test
    public void testAddMappingAbstractType() {
        Class<List<Integer>> superType = List.class;
        Class<ArrayList<Integer>> subType = ArrayList.class;
        resolver.addMapping(superType, subType);
        assertEquals(subType, resolver._mappings.get(new ClassKey(superType)));
    }

    @Test
    public void testFindTypeMappingNullType() {
        JavaType nullType = null;
        assertNull(resolver.findTypeMapping(config, nullType));
    }

    @Test
    public void testFindTypeMappingValidMapping() {
        Class<List<Integer>> superType = List.class;
        Class<ArrayList<Integer>> subType = ArrayList.class;
        resolver.addMapping(superType, subType);

        JavaType javaType = new JavaType();
        when(javaType.getRawClass()).thenReturn(superType);
        JavaType result = resolver.findTypeMapping(config, javaType);
        assertEquals(subType, result);
    }

    @Test
    public void testFindTypeMappingEmptyMapping() {
        JavaType javaType = new JavaType();
        assertNull(resolver.findTypeMapping(config, javaType));
    }

    @Test
    public void testResolveAbstractTypeNullType() {
        DeserializationConfig config = mock(DeserializationConfig.class);
        JavaType javaType = null;
        assertNull(resolver.resolveAbstractType(config, javaType));
    }

    @Test
    public void testResolveAbstractTypeValidMapping() {
        DeserializationConfig config = mock(DeserializationConfig.class);
        Class<List<Integer>> superType = List.class;
        Class<ArrayList<Integer>> subType = ArrayList.class;
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(superType, subType);

        JavaType javaType = new JavaType();
        when(javaType.getRawClass()).thenReturn(superType);
        Class<?> result = resolver.resolveAbstractType(config, javaType);
        assertNull(result);
    }

    @Test
    public void testResolveAbstractTypeEmptyMapping() {
        DeserializationConfig config = mock(DeserializationConfig.class);
        JavaType javaType = new JavaType();
        assertNull(resolver.resolveAbstractType(config, javaType));
    }

    @Test
    public void testResolveAbstractTypeNullConfig() {
        DeserializationConfig nullConfig = null;
        JavaType javaType = new JavaType();
        assertNull(resolver.resolveAbstractType(nullConfig, javaType));
    }

    @Test
    public void testResolveAbstractTypeEmptyMappingHashMap() {
        DeserializationConfig config = mock(DeserializationConfig.class);
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        JavaType javaType = new JavaType();
        assertNull(resolver.resolveAbstractType(config, javaType));
    }

    @Test
    public void testResolveAbstractTypeWithMissingMapping() {
        DeserializationConfig config = mock(DeserializationConfig.class);
        JavaType javaType = new JavaType();
        when(javaType.getRawClass()).thenReturn(String.class);
        assertNull(resolver.resolveAbstractType(config, javaType));
    }
}