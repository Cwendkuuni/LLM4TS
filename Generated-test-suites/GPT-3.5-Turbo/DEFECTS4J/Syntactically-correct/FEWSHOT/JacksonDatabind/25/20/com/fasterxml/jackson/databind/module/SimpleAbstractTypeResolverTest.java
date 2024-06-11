package com.fasterxml.jackson.databind.module;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;

public class SimpleAbstractTypeResolverTest {
    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        
        // Test adding mapping
        resolver.addMapping(Number.class, Integer.class);
        assertTrue(resolver._mappings.containsKey(new ClassKey(Number.class)));
        assertEquals(Integer.class, resolver._mappings.get(new ClassKey(Number.class)));

        // Test adding mapping with class to itself
        try {
            resolver.addMapping(String.class, String.class);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not add mapping from class to itself", e.getMessage());
        }

        // Test adding mapping with subType not being a subtype of superType
        try {
            resolver.addMapping(Number.class, String.class);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not add mapping from class java.lang.Number to java.lang.String, as latter is not a subtype of former", e.getMessage());
        }
        
        // Test adding mapping with superType not being abstract
        try {
            resolver.addMapping(Integer.class, Number.class);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not add mapping from class java.lang.Integer since it is not abstract", e.getMessage());
        }
    }

    @Test
    public void testFindTypeMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        
        // Test finding type mapping
        resolver.addMapping(Number.class, Integer.class);
        DeserializationConfig config = null;
        JavaType type = null;
        JavaType result = resolver.findTypeMapping(config, type);
        assertNotNull(result);
        assertTrue(result.isTypeOrSubTypeOf(Integer.class));
        
        // Test finding type mapping for unmapped type
        type = new JavaType(String.class);
        result = resolver.findTypeMapping(config, type);
        assertNull(result);
    }

    @Test
    public void testResolveAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        
        // Test resolving abstract type
        DeserializationConfig config = null;
        JavaType type = new JavaType(Number.class);
        JavaType result = resolver.resolveAbstractType(config, type);
        assertNull(result);
    }
}