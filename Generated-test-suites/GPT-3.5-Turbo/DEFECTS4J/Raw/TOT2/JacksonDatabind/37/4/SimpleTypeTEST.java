package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testConstructor1() {
        SimpleType simpleType = new SimpleType(String.class);
        assertNotNull(simpleType);
        assertEquals(String.class, simpleType.getRawClass());
    }

    @Test
    public void testConstructor2() {
        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        assertNotNull(simpleType);
        assertEquals(String.class, simpleType.getRawClass());
    }

    // Add more test cases for other constructors

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(Integer.class);
        assertNotNull(simpleType);
        assertEquals(Integer.class, simpleType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructMap() {
        SimpleType.construct(HashMap.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructCollection() {
        SimpleType.construct(ArrayList.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructArray() {
        SimpleType.construct(String[].class);
    }

    // Add more test cases for other static methods

    @Test
    public void testNarrow() {
        SimpleType simpleType = new SimpleType(Number.class);
        JavaType narrowedType = simpleType.narrow(Integer.class);
        assertNotNull(narrowedType);
        assertEquals(Integer.class, narrowedType.getRawClass());
    }

    // Add more test cases for other methods

    @Test
    public void testToString() {
        SimpleType simpleType = new SimpleType(Long.class);
        String expected = "[simple type, class java.lang.Long]";
        assertEquals(expected, simpleType.toString());
    }

    @Test
    public void testEquals() {
        SimpleType simpleType1 = new SimpleType(Long.class);
        SimpleType simpleType2 = new SimpleType(Long.class);
        assertEquals(simpleType1, simpleType2);
    }

    // Add more test cases for other standard methods

}