package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        // Test default constructor
        SimpleType simpleType1 = new SimpleType(String.class);
        assertEquals(String.class, simpleType1.getClass());
        assertEquals(String.class, simpleType1.getType());
        assertNull(simpleType1.getSuperClass());
        assertNull(simpleType1.getSuperInterfaces());
        assertFalse(simpleType1.isStatic());

        // Test constructor with bindings, superClass and superInterfaces
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = TypeFactory.unknownType();
        JavaType[] superInterfaces = new JavaType[0];
        SimpleType simpleType2 = new SimpleType(Integer.class, bindings, superClass, superInterfaces);
        assertEquals(Integer.class, simpleType2.getType());
        assertEquals(Integer.class, simpleType2.getClass());
        assertEquals(superClass, simpleType2.getSuperClass());
        assertArrayEquals(superInterfaces, simpleType2.getSuperInterfaces());
        assertFalse(simpleType2.isStatic());

        // Test copy constructor
        SimpleType simpleType3 = new SimpleType(simpleType1);
        assertEquals(simpleType1, simpleType3);

        // Test constructUnsafe method
        SimpleType simpleType4 = SimpleType.constructUnsafe(String[].class);
        assertEquals(String[].class, simpleType4.getType());
        assertEquals(String[].class, simpleType4.getClass());
        assertNull(simpleType4.getSuperClass());
        assertNull(simpleType4.getSuperInterfaces());
        assertFalse(simpleType4.isStatic());

        // Test deprecated construct method
        try {
            SimpleType simpleType5 = SimpleType.construct(String[].class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testNarrowMethod() {
        SimpleType simpleType1 = new SimpleType(String.class);
        SimpleType simpleType2 = new SimpleType(String[].class);

        assertNull(simpleType1.narrow(String[].class));
        assertEquals(simpleType2, simpleType2.narrow(String[].class));
    }

    @Test
    public void testIsContainerTypeMethod() {
        SimpleType simpleType1 = new SimpleType(String.class);
        SimpleType simpleType2 = new SimpleType(ArrayList.class);

        assertFalse(simpleType1.isContainerType());
        assertTrue(simpleType2.isContainerType());
    }

    @Test
    public void testBuildCanonicalNameMethod() {
        SimpleType simpleType1 = new SimpleType(String.class);
        SimpleType simpleType2 = new SimpleType(List.class, new TypeBindings(), new SimpleType(String.class), new JavaType[0]);

        assertEquals("java.lang.String", simpleType1.buildCanonicalName());
        assertEquals("java.util.List<java.lang.String>", simpleType2.buildCanonicalName());
    }

    @Test
    public void testEqualsMethod() {
        SimpleType simpleType1 = new SimpleType(String.class);
        SimpleType simpleType2 = new SimpleType(String.class);
        SimpleType simpleType3 = new SimpleType(Integer.class);

        assertTrue(simpleType1.equals(simpleType1));
        assertTrue(simpleType1.equals(simpleType2));
        assertFalse(simpleType1.equals(simpleType3));
    }

    @Test
    public void testRefineMethod() {
        SimpleType simpleType1 = new SimpleType(String.class);
        SimpleType refinedType = simpleType1.refine(String[].class, new TypeBindings(), null, new JavaType[0]);
        assertNull(refinedType);
    }
}