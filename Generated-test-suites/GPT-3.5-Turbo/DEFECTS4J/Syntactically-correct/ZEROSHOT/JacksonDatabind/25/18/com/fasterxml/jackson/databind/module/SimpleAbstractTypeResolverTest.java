package com.fasterxml.jackson.databind.module;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.LinkedList;
import java.util.List;

public class SimpleAbstractTypeResolverTest {

    private SimpleAbstractTypeResolver resolver;

    @Before
    public void setUp() {
        resolver = new SimpleAbstractTypeResolver();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMapping_sameClasses() {
        resolver.addMapping(String.class, String.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMapping_notSubType() {
        resolver.addMapping(List.class, String.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMapping_notAbstract() {
        resolver.addMapping(LinkedList.class, LinkedList.class);
    }

    @Test
    public void testAddMapping_validMapping() {
        resolver.addMapping(List.class, LinkedList.class);
        resolver.addMapping(LinkedList.class, LinkedList.class);

        assertEquals(LinkedList.class, resolver._mappings.get(new ClassKey(List.class)));
        assertEquals(LinkedList.class, resolver._mappings.get(new ClassKey(LinkedList.class)));
    }

    @Test
    public void testFindTypeMapping_noMappingFound() {
        DeserializationConfig config = new DeserializationConfig();
        JavaType type = config.constructType(String.class);

        JavaType result = resolver.findTypeMapping(config, type);

        assertNull(result);
    }

    @Test
    public void testFindTypeMapping_mappingFound() {
        resolver.addMapping(List.class, LinkedList.class);

        DeserializationConfig config = new DeserializationConfig();
        JavaType type = config.constructType(List.class);

        JavaType result = resolver.findTypeMapping(config, type);

        assertEquals(LinkedList.class, result.getRawClass());
    }

    @Test
    public void testResolveAbstractType_alwaysReturnsNull() {
        DeserializationConfig config = new DeserializationConfig();
        JavaType type = config.constructType(String.class);

        JavaType result = resolver.resolveAbstractType(config, type);

        assertNull(result);
    }
}