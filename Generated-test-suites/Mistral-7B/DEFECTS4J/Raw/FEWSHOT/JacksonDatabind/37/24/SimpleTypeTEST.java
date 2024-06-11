package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testIsContainerType() {
        SimpleType simpleType = new SimpleType(String.class);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testConstructUnsafe() {
        Class<Integer> integerClass = Integer.class;
        SimpleType simpleType = SimpleType.constructUnsafe(integerClass);
        assertEquals(integerClass, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstruct_Map() {
        Class<Map<String, Integer>> mapClass = Map.class;
        SimpleType.construct(mapClass);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstruct_Collection() {
        Class<List<Integer>> listClass = List.class;
        SimpleType.construct(listClass);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstruct_Array() {
        Class<int[]> intArrayClass = int[].class;
        SimpleType.construct(intArrayClass);
    }

    @Test
    public void testNarrow() {
        Class<Integer> integerClass = Integer.class;
        Class<Integer> intWrapperClass = Integer.class;
        SimpleType simpleTypeInt = new SimpleType(integerClass);
        SimpleType simpleTypeWrapper = new SimpleType(intWrapperClass);

        SimpleType narrowedTypeInt = simpleTypeInt._narrow(intWrapperClass);
        SimpleType narrowedTypeWrapper = simpleTypeWrapper._narrow(integerClass);

        assertEquals(simpleTypeInt, narrowedTypeInt);
        assertEquals(simpleTypeWrapper, narrowedTypeWrapper);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrow_InvalidSubclass() {
        Class<Integer> integerClass = Integer.class;
        Class<String> stringClass = String.class;
        SimpleType simpleTypeInt = new SimpleType(integerClass);

        simpleTypeInt._narrow(stringClass);
    }

    @Test
    public void testBuildCanonicalName() {
        Class<Map<String, List<Integer>>> mapListClass = Map.class;
        SimpleType simpleType = new SimpleType(mapListClass);
        String canonicalName = simpleType.buildCanonicalName();
        String expectedCanonicalName = "Map<java.lang.String, List<java.lang.Integer>>";
        assertEquals(expectedCanonicalName, canonicalName);
    }

    @Test
    public void testToString() {
        Class<String> stringClass = String.class;
        SimpleType simpleTypeString = new SimpleType(stringClass);
        String toString = simpleTypeString.toString();
        String expectedToString = "[simple type, class [Ljava.lang.String;]]";
        assertEquals(expectedToString, toString);
    }

    @Test
    public void testEquals() {
        Class<Integer> integerClass1 = Integer.class;
        Class<Integer> integerClass2 = Integer.class;
        SimpleType simpleTypeInt1 = new SimpleType(integerClass1);
        SimpleType simpleTypeInt2 = new SimpleType(integerClass2);

        SimpleType simpleTypeString = new SimpleType(String.class);

        assertTrue(simpleTypeInt1.equals(simpleTypeInt1));
        assertTrue(simpleTypeInt1.equals(simpleTypeInt2));
        assertFalse(simpleTypeInt1.equals(simpleTypeString));
    }
}