package com.fasterxml.jackson.databind.module;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class SimpleAbstractTypeResolverTest {

    private SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

    @Test
    public void testAddMapping() {
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);
        Map<ClassKey, Class<?>> mappings = resolver._mappings;
        assertEquals(LinkedList.class, mappings.get(new ClassKey(Collection.class)));
        assertEquals(LinkedList.class, mappings.get(new ClassKey(List.class)));
    }

    @Test
    public void testFindTypeMapping() {
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);
        JavaType javaType = TypeFactory.defaultInstance().constructType(Collection.class);
        assertEquals(LinkedList.class, resolver.findTypeMapping(null, javaType).getRawClass());
        javaType = TypeFactory.defaultInstance().constructType(List.class);
        assertEquals(LinkedList.class, resolver.findTypeMapping(null, javaType).getRawClass());
    }

    @Test
    public void testFindTypeMapping_NullType() {
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);
        assertNull(resolver.findTypeMapping(null, null));
    }

    @Test
    public void testFindTypeMapping_InvalidType() {
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);
        JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);
        assertNull(resolver.findTypeMapping(null, javaType));
    }

    @Test
    public void testResolveAbstractType() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(Collection.class);
        assertNull(resolver.resolveAbstractType(null, javaType));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMapping_SelfMapping() {
        resolver.addMapping(Collection.class, Collection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMapping_InvalidSubType() {
        resolver.addMapping(Collection.class, String.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMapping_NonAbstractSuperType() {
        resolver.addMapping(LinkedList.class, List.class);
    }
}