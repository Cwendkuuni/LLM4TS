package com.example.junit; // replace with your package name

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    // Basic test cases for constructors

    @Test
    public void testConstructUnsafe() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        assertNotNull(type);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructInvalidClass() {
        SimpleType.construct(Map.class);
    }

    // Test cases for basic methods

    @Test
    public void testIsContainerType() {
        SimpleType type = new SimpleType(String.class);
        assertFalse(type.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType type = new SimpleType(String.class);
        StringBuilder expected = new StringBuilder("java.lang.String");
        assertEquals(expected.toString(), type.getErasedSignature(new StringBuilder()).toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType type = new SimpleType(String[].class);
        StringBuilder expected = new StringBuilder("[Ljava.lang.String;");
        assertEquals(expected.toString(), type.getGenericSignature(new StringBuilder()).toString());
    }

    @Test
    public void testToString() {
        SimpleType type = new SimpleType(String.class);
        StringBuilder expected = new StringBuilder("[simple type, class [Ljava.lang.String;]]");
        assertEquals(expected.toString(), type.toString());
    }

    @Test
    public void testEquals() {
        SimpleType type1 = new SimpleType(String.class);
        SimpleType type2 = new SimpleType(String.class);
        assertTrue(type1.equals(type2));
    }

    // Edge cases and exception handling test cases

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowInvalidClass() {
        SimpleType type = new SimpleType(String.class);
        type.narrow(int.class);
    }

    @Test
    public void testNarrowSameClass() {
        SimpleType type1 = new SimpleType(String.class);
        SimpleType type2 = new SimpleType(String.class);
        SimpleType result = type1._narrow(String.class);
        assertEquals(type1, result);
    }

    @Test
    public void testNarrowSubClass() {
        SimpleType parentType = new SimpleType(Object.class);
        SimpleType childType = new SimpleType(String.class);
        SimpleType result = parentType._narrow(String.class);
        assertEquals(childType, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowInvalidClassWithSuperTypes() {
        SimpleType type = new SimpleType(String[].class);
        type.narrow(Integer.class);
    }

    @Test
    public void testNarrowSameClassWithSuperTypes() {
        SimpleType parentType = new SimpleType(List.class);
        SimpleType childType = new SimpleType(ArrayList.class);
        SimpleType result = parentType._narrow(ArrayList.class);
        assertEquals(childType, result);
    }

    @Test
    public void testNarrowSubClassWithSuperTypes() {
        SimpleType parentType = new SimpleType(List.class);
        SimpleType childType = new SimpleType(ArrayList.class);
        SimpleType result = parentType._narrow(LinkedList.class);
        assertNull(result);
    }
}