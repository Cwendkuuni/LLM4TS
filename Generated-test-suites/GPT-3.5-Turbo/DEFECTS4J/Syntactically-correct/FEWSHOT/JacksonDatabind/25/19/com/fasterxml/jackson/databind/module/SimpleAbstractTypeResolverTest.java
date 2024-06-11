package com.fasterxml.jackson.databind.module;

import org.junit.Test;

import static org.junit.Assert.*;

public class SimpleAbstractTypeResolverTest {

    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        // Add mapping from super type to subtype
        resolver.addMapping(CharSequence.class, String.class);

        // Test if the mapping is added correctly
        Class<?> subType = resolver._mappings.get(new ClassKey(CharSequence.class));
        assertEquals(String.class, subType);

        // Test if the same super type and subtype throws an exception
        try {
            resolver.addMapping(CharSequence.class, String.class);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not add mapping from class to itself", e.getMessage());
        }

        // Test if the super type is not abstract
        try {
            resolver.addMapping(String.class, StringBuilder.class);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not add mapping from class java.lang.String since it is not abstract", e.getMessage());
        }

        // Test if the subtype is not assignable to the super type
        try {
            resolver.addMapping(CharSequence.class, StringBuilder.class);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not add mapping from class java.lang.CharSequence to java.lang.StringBuilder, as latter is not a subtype of former", e.getMessage());
        }
    }

    @Test
    public void testFindTypeMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        // Add mapping from super type to subtype
        resolver.addMapping(CharSequence.class, String.class);

        // Test if findTypeMapping returns the correct JavaType
        assertNotNull(resolver.findTypeMapping(null, new JavaType(CharSequence.class)));
    }

    @Test
    public void testResolveAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        // Test if resolveAbstractType always returns null
        assertNull(resolver.resolveAbstractType(null, new JavaType(CharSequence.class)));
    }
}