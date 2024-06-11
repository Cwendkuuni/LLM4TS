package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        // Test direct construction
        JavaType simpleType1 = TypeFactory.construct(String.class);
        assertNotNull(simpleType1);

        // Test copy constructor
        JavaType simpleType2 = new SimpleType(simpleType1);
        assertNotNull(simpleType2);
        assertNotSame(simpleType1, simpleType2);

        // Test deprecated construct method
        JavaType simpleType3 = TypeFactory.construct(Integer.class);
        assertNotNull(simpleType3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidTypeConstruction() {
        TypeFactory.construct(List.class);
    }

    @Test
    public void testTypeHierarchy() {
        JavaType simpleType1 = TypeFactory.construct(String.class);
        JavaType simpleType2 = TypeFactory.construct(String[].class);

        JavaType refinedType = simpleType1.refine(String[].class, TypeBindings.emptyBindings(), null, null);
        assertNull(refinedType);

        JavaType derivedType = simpleType2.withNarrowType(String.class);
        assertNotNull(derivedType);
        assertNotSame(simpleType1, derivedType);
        assertNotSame(simpleType2, derivedType);
    }

    @Test
    public void testConstructUnsafe() {
        JavaType simpleType = TypeFactory.constructUnsafe(String.class);
        assertNotNull(simpleType);

        // Calling methods that require proper super-type introspection should fail
        try {
            simpleType.withContentType(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            simpleType.withTypeHandler(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}