package com.fasterxml.jackson.databind.module;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Modifier;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.AbstractTypeResolver;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ClassKey;

public class SimpleAbstractTypeResolverTest {

    private SimpleAbstractTypeResolver resolver;
    
    @Before
    public void setup() {
        resolver = new SimpleAbstractTypeResolver();
    }

    @Test
    public void testAddMapping() {
        // test adding mapping
        resolver.addMapping(Number.class, Integer.class);
        assertTrue(resolver._mappings.containsKey(new ClassKey(Number.class)));
        assertEquals(Integer.class, resolver._mappings.get(new ClassKey(Number.class)));

        // test adding mapping from class to itself
        try {
            resolver.addMapping(Number.class, Number.class);
            fail("Should throw IllegalArgumentException when adding mapping from class to itself");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not add mapping from class to itself", e.getMessage());
        }

        // test adding mapping with no subtype relationship
        try {
            resolver.addMapping(Number.class, String.class);
            fail("Should throw IllegalArgumentException when adding mapping with no subtype relationship");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not add mapping from class java.lang.Number to class java.lang.String, as latter is not a subtype of former", e.getMessage());
        }

        // test adding mapping of non-abstract type
        try {
            resolver.addMapping(Integer.class, Number.class);
            fail("Should throw IllegalArgumentException when adding mapping of non-abstract type");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not add mapping from class java.lang.Integer since it is not abstract", e.getMessage());
        }
    }

    @Test
    public void testFindTypeMapping() {
        // setup resolver with mappings
        resolver.addMapping(Number.class, Integer.class);
        resolver.addMapping(Number.class, Double.class);

        // test finding type mapping
        DeserializationConfig config = null;
        JavaType type = null;

        type = resolver.findTypeMapping(config, new JavaTypeMock(Number.class));
        assertEquals(Integer.class, type.getRawClass());

        type = resolver.findTypeMapping(config, new JavaTypeMock(Integer.class));
        assertNull(type);

        type = resolver.findTypeMapping(config, new JavaTypeMock(String.class));
        assertNull(type);
    }

    @Test
    public void testResolveAbstractType() {
        // test resolving abstract type
        DeserializationConfig config = null;
        JavaType type = null;

        type = resolver.resolveAbstractType(config, new JavaTypeMock(Number.class));
        assertNull(type);
    }

    // Helper class for testing
    static class JavaTypeMock extends JavaType {
        private static final long serialVersionUID = 1L;

        public JavaTypeMock(Class<?> rawClass) {
            super(null, rawClass, new HashMap<ClassKey, JavaType>(), null, null, false);
        }
    }
}