package com.fasterxml.jackson.databind.module;

import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.AbstractTypeResolver;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ClassKey;
import java.lang.reflect.Modifier;
import java.util.*;

public class SimpleAbstractTypeResolverTest {

    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);

        Map<ClassKey, Class<?>> mappings = resolver._mappings;
        assertEquals(2, mappings.size());
        assertEquals(LinkedList.class, mappings.get(new ClassKey(Collection.class)));
        assertEquals(LinkedList.class, mappings.get(new ClassKey(List.class)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingWithSameClasses() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, Collection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingWithNonSubType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, Integer.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingWithNonAbstractSuperType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(List.class, LinkedList.class);
    }

    @Test
    public void testFindTypeMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);

        DeserializationConfig config = null;
        JavaType type = null;

        type = new JavaType(config, LinkedList.class, null, null);
        JavaType result = resolver.findTypeMapping(config, type);
        assertEquals(LinkedList.class, result.getRawClass());

        type = new JavaType(config, List.class, null, null);
        result = resolver.findTypeMapping(config, type);
        assertEquals(LinkedList.class, result.getRawClass());

        type = new JavaType(config, ArrayList.class, null, null);
        result = resolver.findTypeMapping(config, type);
        assertEquals(null, result);
    }

    @Test
    public void testResolveAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        DeserializationConfig config = null;
        JavaType type = null;
        JavaType result = resolver.resolveAbstractType(config, type);
        assertEquals(null, result);
    }
}