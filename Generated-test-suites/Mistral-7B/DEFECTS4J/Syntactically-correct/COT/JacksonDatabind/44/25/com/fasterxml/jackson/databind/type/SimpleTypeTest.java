package com.example.junit4;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    

    // Basic test case for constructors with a Class argument
    @Test
    public void testConstructWithClass() {
        Class<String> stringClass = String.class;
        SimpleType stringType = new SimpleType(stringClass);
        assertEquals(String.class, stringType.getRawClass());
    }

    // Basic test case for copy constructor
    @Test
    public void testCopyConstructor() {
        Class<String> stringClass = String.class;
        SimpleType originalStringType = new SimpleType(stringClass);
        SimpleType copyStringType = new SimpleType(originalStringType);
        assertEquals(originalStringType, copyStringType);
    }

    // Edge case: Testing constructUnsafe method with null argument
    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeWithNullArgument() {
        SimpleType.constructUnsafe(null);
    }

    // Edge case: Testing construct method with Collection class
    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithCollectionClass() {
        SimpleType.construct(Collection.class);
    }

    // Edge case: Testing construct method with Map class
    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithMapClass() {
        SimpleType.construct(Map.class);
    }

    // Edge case: Testing construct method with array class
    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithArrayClass() {
        SimpleType.construct(int[].class);
    }

    // Testing _narrow method with equal classes
    @Test
    public void testNarrowWithEqualClasses() {
        Class<String> stringClass = String.class;
        SimpleType stringType1 = new SimpleType(stringClass);
        SimpleType stringType2 = new SimpleType(stringClass);
        assertEquals(stringType1, stringType2._narrow(stringClass));
    }

    // Testing _narrow method with different classes
    @Test(expected = IllegalArgumentException.class)
    public void testNarrowWithDifferentClasses() {
        Class<String> stringClass = String.class;
        Class<Integer> intClass = Integer.class;
        SimpleType stringType = new SimpleType(stringClass);
        SimpleType intType = new SimpleType(intClass);
        assertNotEquals(stringType, intType);
        assertNotEquals(stringType, intType._narrow(stringClass));
    }

    
}