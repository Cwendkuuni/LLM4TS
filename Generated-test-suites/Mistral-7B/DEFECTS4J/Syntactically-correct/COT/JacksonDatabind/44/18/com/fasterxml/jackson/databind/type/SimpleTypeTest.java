package com.fasterxml.jackson.databind.type; // Import necessary packages

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class SimpleTypeTest {

    // Instance of SimpleType under test
    private SimpleType simpleType;

    @Before
    public void setUp() {
        simpleType = SimpleType.constructUnsafe(String.class);
    }

    // Test case 1: Basic functionality of the constructor
    @Test
    public void testSimpleTypeConstructor() {
        assertNotNull(simpleType);
        assertEquals(String.class, simpleType.getRawClass());
    }

    // Test case 2: Basic functionality of the isContainerType() method
    @Test
    public void testIsContainerType() {
        assertFalse(simpleType.isContainerType());
    }

    // Test case 3: Basic functionality of the getErasedSignature() method
    @Test
    public void testGetErasedSignature() {
        StringBuilder expectedSignature = new StringBuilder("[Ljava/lang/String;");
        StringBuilder actualSignature = simpleType.getErasedSignature(new StringBuilder());
        assertEquals(expectedSignature.toString(), actualSignature.toString());
    }

    // Test case 4: Basic functionality of the getGenericSignature() method
    @Test
    public void testGetGenericSignature() {
        StringBuilder expectedSignature = new StringBuilder("[Ljava/lang/String;");
        StringBuilder actualSignature = new StringBuilder();
        simpleType.getGenericSignature(actualSignature);
        assertEquals(expectedSignature.toString(), actualSignature.toString());
    }

    // Test case 5: Test handling of null arguments in getErasedSignature() and getGenericSignature() methods
    @Test(expected = IllegalArgumentException.class)
    public void testGetSignatureWithNullArguments() {
        simpleType.getErasedSignature(null);
        simpleType.getGenericSignature(null);
    }

    // Test case 6: Test handling of an unrecognized class in the constructor
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidClassInConstructor() {
        SimpleType invalidType = SimpleType.constructUnsafe(int[].class);
    }

    // Test case 7: Test the deprecated construct() method
    @Test(expected = IllegalArgumentException.class)
    @Deprecated
    public void testDeprecatedConstructMethod() {
        SimpleType invalidType = SimpleType.construct(int[].class);
    }

    // Test case 8: Test the _narrow() method with a subclass
    @Test
    public void testNarrowMethodWithSubclass() {
        SimpleType subclassType = new SimpleType(Integer.class, null, simpleType, null, null, null, false);
        JavaType result = simpleType._narrow(Integer.class);
        assertEquals(subclassType, result);
    }

    // Test case 9: Test the _narrow() method with a non-subclass
    @Test
    public void testNarrowMethodWithNonSubclass() {
        JavaType result = simpleType._narrow(Double.class);
        assertNull(result);
    }
}