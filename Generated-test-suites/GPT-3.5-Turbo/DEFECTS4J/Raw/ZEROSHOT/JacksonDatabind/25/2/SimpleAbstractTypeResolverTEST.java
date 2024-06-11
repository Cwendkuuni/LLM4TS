package com.fasterxml.jackson.databind.module;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ClassKey;

public class SimpleAbstractTypeResolverTest {

    private SimpleAbstractTypeResolver resolver;

    @Before
    public void setUp() {
        resolver = new SimpleAbstractTypeResolver();
    }

    @Test
    public void testAddMapping_withValidInputs() {
        resolver.addMapping(List.class, LinkedList.class);

        HashMap<ClassKey, Class<?>> mappings = resolver._mappings;
        assertEquals(1, mappings.size());
        assertEquals(LinkedList.class, mappings.get(new ClassKey(List.class)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMapping_withSameClass() {
        resolver.addMapping(List.class, List.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMapping_withNotSubType() {
        resolver.addMapping(List.class, HashMap.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMapping_withNotAbstractClass() {
        resolver.addMapping(LinkedList.class, List.class);
    }

    @Test
    public void testFindTypeMapping_withValidInput() {
        resolver.addMapping(List.class, LinkedList.class);

        JavaType type = JavaType.fromClass(List.class);
        DeserializationConfig config = null;
        JavaType result = resolver.findTypeMapping(config, type);

        assertEquals(LinkedList.class, result.getRawClass());
    }

    @Test
    public void testFindTypeMapping_withInvalidInput() {
        resolver.addMapping(List.class, LinkedList.class);

        JavaType type = JavaType.fromClass(ArrayList.class);
        DeserializationConfig config = null;
        JavaType result = resolver.findTypeMapping(config, type);

        assertNull(result);
    }

    @Test
    public void testResolveAbstractType() {
        JavaType type = JavaType.fromClass(List.class);
        DeserializationConfig config = null;
        JavaType result = resolver.resolveAbstractType(config, type);

        assertNull(result);
    }
}