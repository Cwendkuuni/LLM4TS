package com.fasterxml.jackson.databind.module;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class SimpleAbstractTypeResolverTest {

    private SimpleAbstractTypeResolver resolver;

    @Before
    public void setUp() {
        resolver = new SimpleAbstractTypeResolver();
    }

    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver result = resolver.addMapping(List.class, LinkedList.class);
        assertEquals(resolver, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMapping_sameType() {
        resolver.addMapping(List.class, List.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMapping_subTypeNotAssignable() {
        resolver.addMapping(Collection.class, LinkedList.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMapping_notAbstractSuperType() {
        resolver.addMapping(LinkedList.class, ArrayList.class);
    }

    @Test
    public void testFindTypeMapping_mapped() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);

        DeserializationConfig config = null;
        JavaType type = TypeFactory.defaultInstance().constructType(Collection.class);
        JavaType result = resolver.findTypeMapping(config, type);

        JavaType expected = TypeFactory.defaultInstance().constructType(LinkedList.class);
        assertEquals(expected, result);
    }

    @Test
    public void testFindTypeMapping_notMapped() {
        DeserializationConfig config = null;
        JavaType type = TypeFactory.defaultInstance().constructType(Collection.class);
        JavaType result = resolver.findTypeMapping(config, type);

        assertNull(result);
    }

    @Test
    public void testResolveAbstractType() {
        DeserializationConfig config = null;
        JavaType type = TypeFactory.defaultInstance().constructType(Collection.class);
        JavaType result = resolver.resolveAbstractType(config, type);

        assertNull(result);
    }
}