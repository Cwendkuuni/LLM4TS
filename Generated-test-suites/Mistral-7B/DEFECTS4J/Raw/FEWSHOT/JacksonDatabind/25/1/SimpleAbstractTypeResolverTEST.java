package com.fasterxml.jackson.databind.module;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.core.type.ClassIdResolver;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.type.ClassKey;

public class SimpleAbstractTypeResolverTest {

    @Test
    public void testSimpleAbstractTypeResolver() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        // Test adding mappings
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);
        Map<ClassKey, Class<?>> mappings = resolver._mappings;

        // Test getMapping returns correct mappings
        assertEquals(LinkedList.class, mappings.get(new ClassKey(Collection.class)));
        assertEquals(LinkedList.class, mappings.get(new ClassKey(List.class)));
        assertNull(mappings.get(new ClassKey(String.class)));
    }

    @Test
    public void testFindTypeMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);

        DeserializationConfig config = new DeserializationConfig();
        JavaType type = config.constructType(Collection.class);

        JavaType resolvedType = resolver.findTypeMapping(config, type);
        assertEquals(LinkedList.class, resolvedType.getRawClass());

        type = config.constructType(LinkedList.class);
        resolvedType = resolver.findTypeMapping(config, type);
        assertNull(resolvedType);
    }

    @Test
    public void testResolveAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        JavaType type = null;
        JavaType resolvedType = resolver.resolveAbstractType(new DeserializationConfig(), type);
        assertNull(resolvedType);

        type = new ClassIdResolver().constructTypeId(new ClassKey(Collection.class), null, null, null, null, null);
        resolvedType = resolver.resolveAbstractType(new DeserializationConfig(), type);
        assertNull(resolvedType);
    }
}