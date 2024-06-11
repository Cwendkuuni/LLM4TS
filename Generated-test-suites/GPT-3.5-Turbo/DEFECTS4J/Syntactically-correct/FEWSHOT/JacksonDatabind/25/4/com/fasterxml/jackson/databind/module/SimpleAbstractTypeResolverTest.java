package com.fasterxml.jackson.databind.module;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;

public class SimpleAbstractTypeResolverTest {
    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        // Add mapping
        resolver.addMapping(Number.class, Integer.class);

        // Test mapping
        DeserializationConfig config = null; // provided necessary object
        JavaType type = null; // provided necessary type
        assertEquals(Integer.class, resolver.findTypeMapping(config, type));
    }

    @Test
    public void testAddMappingWithInvalidArguments() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        // Test mapping from class to itself
        try {
            resolver.addMapping(Integer.class, Integer.class);
        } catch (IllegalArgumentException e) {
            assertEquals("Can not add mapping from class to itself", e.getMessage());
        }

        // Test mapping with non-subtype
        try {
            resolver.addMapping(Number.class, String.class);
        } catch (IllegalArgumentException e) {
            assertEquals("Can not add mapping from class java.lang.Number to java.lang.String, as latter is not a subtype of former", e.getMessage());
        }

        // Test mapping with non-abstract superType
        try {
            resolver.addMapping(Integer.class, Number.class);
        } catch (IllegalArgumentException e) {
            assertEquals("Can not add mapping from class java.lang.Integer since it is not abstract", e.getMessage());
        }
    }

    @Test
    public void testFindTypeMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        // Add mappings
        resolver.addMapping(Number.class, Integer.class);
        resolver.addMapping(Number.class, Double.class);

        DeserializationConfig config = null; // provided necessary object
        JavaType type = null; // provided necessary type

        // Test finding type mapping
        assertEquals(Integer.class, resolver.findTypeMapping(config, type));
    }

    @Test
    public void testFindTypeMappingWithUnmappedType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        // Add mappings
        resolver.addMapping(Number.class, Integer.class);
        resolver.addMapping(Number.class, Double.class);

        DeserializationConfig config = null; // provided necessary object
        JavaType type = null; // provided necessary type

        // Test finding type mapping for unmapped type
        assertNull(resolver.findTypeMapping(config, type));
    }

    @Test
    public void testResolveAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        // Test resolving abstract type
        DeserializationConfig config = null; // provided necessary object
        JavaType type = null; // provided necessary type
        assertNull(resolver.resolveAbstractType(config, type));
    }
}