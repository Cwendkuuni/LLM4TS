package com.fasterxml.jackson.databind.module;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.ClassKey;

public class SimpleAbstractTypeResolverTest {

    private SimpleAbstractTypeResolver resolver;

    @Before
    public void setUp() {
        resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(List.class, LinkedList.class);
        resolver.addMapping(Collection.class, LinkedList.class);
    }

    @Test
    public void testFindTypeMapping() {
        JavaType type = TypeFactory.defaultInstance().constructType(List.class, Integer.class);
        JavaType resolvedType = resolver.findTypeMapping(null, type);
        assertNotNull(resolvedType);
        assertEquals(LinkedList.class, resolvedType.getRawClass());
    }

    @Test
    public void testFindTypeMappingWithAbstractType() {
        JavaType superType = TypeFactory.defaultInstance().constructType(AbstractList.class, Integer.class);
        JavaType resolvedType = resolver.findTypeMapping(null, superType);
        assertNotNull(resolvedType);
        assertEquals(LinkedList.class, resolvedType.getRawClass());
    }

    @Test
    public void testFindTypeMappingWithNullType() {
        JavaType resolvedType = resolver.findTypeMapping(null, null);
        assertNull(resolvedType);
    }

    @Test
    public void testFindTypeMappingWithInvalidType() {
        JavaType type = TypeFactory.defaultInstance().constructType(String.class);
        JavaType resolvedType = resolver.findTypeMapping(null, type);
        assertNull(resolvedType);
    }

    @Test
    public void testResolveAbstractType() {
        JavaType type = null;
        JavaType resolvedType = resolver.resolveAbstractType(null, type);
        assertNull(resolvedType);
    }
}