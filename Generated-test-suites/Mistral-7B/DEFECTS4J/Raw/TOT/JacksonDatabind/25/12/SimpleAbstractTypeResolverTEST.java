package com.fasterxml.jackson.databind.module.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Test cases for {@link SimpleAbstractTypeResolver} class
 */
public class SimpleAbstractTypeResolverTest {

    @Test
    public void testSimpleAbstractTypeResolver() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        // Test adding mappings
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);

        // Test finding type mapping
        JavaType type = TypeFactory.defaultInstance().constructType(new TypeReference<List<String>>() {}.getType());
        JavaType resolvedType = resolver.findTypeMapping(new DeserializationConfig(), type);
        assertTrue(resolvedType != null);
        assertTrue(LinkedList.class.isAssignableFrom(resolvedType.getRawClass()));

        // Test resolving abstract type
        type = TypeFactory.defaultInstance().constructType(new TypeReference<AbstractMap<String, Integer>>() {}.getType());
        resolvedType = resolver.resolveAbstractType(new DeserializationConfig(), type);
        assertNull(resolvedType);

        // Test with invalid arguments
        try {
            resolver.addMapping(String.class, Integer.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            resolver.addMapping(Integer.class, String.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            resolver.addMapping(String.class, String.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            resolver.addMapping(List.class, String.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}