package com.fasterxml.jackson.databind.module;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SimpleAbstractTypeResolverTest {

    private SimpleAbstractTypeResolver resolver;

    @Before
    public void setUp() {
        resolver = new SimpleAbstractTypeResolver();
    }

    @Test
    public void addMapping_shouldThrowIllegalArgumentException_whenSuperTypeIsSameAsSubType() {
        try {
            resolver.addMapping(Object.class, Object.class);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not add mapping from class to itself", e.getMessage());
        }
    }

    @Test
    public void addMapping_shouldThrowIllegalArgumentException_whenNoSuperTypeSubTypeRelationship() {
        try {
            resolver.addMapping(Number.class, String.class);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not add mapping from class java.lang.Number to java.lang.String, " +
                    "as latter is not a subtype of former", e.getMessage());
        }
    }

    @Test
    public void addMapping_shouldThrowIllegalArgumentException_whenSuperTypeIsNotAbstract() {
        try {
            resolver.addMapping(Integer.class, Long.class);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not add mapping from class java.lang.Integer since it is not abstract", e.getMessage());
        }
    }

    @Test
    public void addMapping_shouldAddMapping_whenValidSuperTypeAndSubType() {
        resolver.addMapping(Number.class, Integer.class);
        resolver.addMapping(Number.class, Double.class);

        assertNotNull(resolver._mappings.get(new ClassKey(Number.class)));
        assertEquals(Integer.class, resolver._mappings.get(new ClassKey(Number.class)));
        assertEquals(Double.class, resolver._mappings.get(new ClassKey(Number.class)));
    }

    @Test
    public void findTypeMapping_shouldReturnNull_whenNoMappingExists() {
        assertNull(resolver.findTypeMapping(null, null));
    }

    @Test
    public void findTypeMapping_shouldReturnMappedType_whenMappingExists() {
        resolver.addMapping(Number.class, Float.class);

        JavaType javaType = new JavaType(Float.class, null, null, null, null,
                false, false);

        assertEquals(javaType, resolver.findTypeMapping(null, javaType));
    }

    @Test
    public void resolveAbstractType_shouldReturnNull() {
        assertNull(resolver.resolveAbstractType(null, null));
    }

}