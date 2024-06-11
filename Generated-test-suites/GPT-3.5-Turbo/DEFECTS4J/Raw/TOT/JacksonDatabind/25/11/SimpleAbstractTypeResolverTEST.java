package com.fasterxml.jackson.databind.module;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SimpleAbstractTypeResolverTest {

    SimpleAbstractTypeResolver resolver;

    @Before
    public void setUp() {
        resolver = new SimpleAbstractTypeResolver();
    }

    @Test
    public void testAddMapping_typicalUseCase() {
        try {
            resolver.addMapping(AbstractType.class, SubType.class);
            assertTrue(resolver._mappings.containsKey(new ClassKey(AbstractType.class)));
            assertEquals(SubType.class, resolver._mappings.get(new ClassKey(AbstractType.class)));
        } catch (Exception e) {
            fail("Exception thrown during typical use case test");
        }
    }

    @Test
    public void testAddMapping_sameType() {
        try {
            resolver.addMapping(AbstractType.class, AbstractType.class);
            fail("Should throw IllegalArgumentException for mapping class to itself");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testAddMapping_nonSubType() {
        try {
            resolver.addMapping(AbstractType.class, NonSubType.class);
            fail("Should throw IllegalArgumentException for mapping non subtype to abstract type");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testAddMapping_nonAbstractType() {
        try {
            resolver.addMapping(AbstractType.class, ConcreteType.class);
            fail("Should throw IllegalArgumentException for mapping non abstract type to abstract type");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testFindTypeMapping_mappingExists() {
        resolver.addMapping(AbstractType.class, SubType.class);
        assertNull(resolver.findTypeMapping(null, new JavaTypeFor(AbstractType.class)));
        assertEquals(new JavaTypeFor(SubType.class), resolver.findTypeMapping(null, new JavaTypeFor(SubType.class)));
    }

    @Test
    public void testFindTypeMapping_mappingDoesNotExist() {
        assertNull(resolver.findTypeMapping(null, new JavaTypeFor(UnmappedType.class)));
    }

    @Test
    public void testResolveAbstractType() {
        assertNull(resolver.resolveAbstractType(null, new JavaTypeFor(AbstractType.class)));
    }

    private static class AbstractType {}

    private static class SubType extends AbstractType {}

    private static class NonSubType {}

    private static class ConcreteType extends AbstractType {}

    private static class UnmappedType {}

    private static class JavaTypeFor {
        private final Class<?> clazz;

        public JavaTypeFor(Class<?> clazz) {
            this.clazz = clazz;
        }

        public Class<?> getRawClass() {
            return clazz;
        }

        public JavaTypeFor narrowBy(Class<?> clazz) {
            return new JavaTypeFor(clazz);
        }
    }
}