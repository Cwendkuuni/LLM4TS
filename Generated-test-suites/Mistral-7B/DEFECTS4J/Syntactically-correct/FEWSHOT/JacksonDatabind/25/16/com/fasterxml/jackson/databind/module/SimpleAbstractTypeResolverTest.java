package com.fasterxml.jackson.databind.module;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.core.type.ClassIdType;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ClassKey;

public class SimpleAbstractTypeResolverTest {

    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        Class<?> superType = Collection.class;
        Class<? extends Collection> subType = LinkedList.class;
        resolver.addMapping(superType, subType);
        assertEquals(subType, resolver._mappings.get(new ClassKey(superType)));
    }

    @Test
    public void testFindTypeMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        Class<?> superType = Collection.class;
        Class<? extends Collection> subType = LinkedList.class;
        resolver.addMapping(superType, subType);

        JavaType javaType = new ClassIdType(Collection.class.getName(), Collections.emptyList());
        JavaType result = resolver.findTypeMapping(null, javaType);
        assertNotNull(result);
        assertEquals(subType, result.getRawClass());

        javaType = new ClassIdType(List.class.getName(), Collections.emptyList());
        result = resolver.findTypeMapping(null, javaType);
        assertNotNull(result);
        assertEquals(subType, result.getRawClass());

        javaType = new ClassIdType(Map.class.getName(), Arrays.asList(Integer.TYPE, String.class));
        result = resolver.findTypeMapping(null, javaType);
        assertNull(result);
    }

    @Test
    public void testFindTypeMappingWithGenericType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        Class<?> superType = List.class;
        Class<? extends List> subType = ArrayList.class;
        resolver.addMapping(superType, subType);

        JavaType javaType = new ClassIdType(List.class.getName(), Arrays.asList(Integer.TYPE, String.class));
        JavaType result = resolver.findTypeMapping(null, javaType);
        assertNotNull(result);
        assertEquals(subType, result.getRawClass());
    }

    @Test
    public void testFindTypeMappingWithAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        Class<?> superType = Map.class;
        Class<? extends Map> subType = LinkedHashMap.class;
        resolver.addMapping(superType, subType);

        JavaType javaType = new ClassIdType(Map.class.getName(), Collections.emptyList());
        JavaType result = resolver.findTypeMapping(null, javaType);
        assertNotNull(result);
        assertEquals(subType, result.getRawClass());
    }

    @Test
    public void testFindTypeMappingWithNullType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        assertNull(resolver.findTypeMapping(null, null));
    }

    @Test
    public void testResolveAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        assertNull(resolver.resolveAbstractType(null, null));
    }
}