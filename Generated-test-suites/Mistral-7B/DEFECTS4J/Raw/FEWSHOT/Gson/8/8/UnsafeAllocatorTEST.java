package com.google.gson.internal;

import com.google.gson.internal.UnsafeAllocator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.Serializable;
import java.lang.reflect.Modifier;

@RunWith(JUnit4.class)
public class UnsafeAllocatorTest {

    @Test
    public void testNewInstance() {
        UnsafeAllocator allocator = UnsafeAllocator.create();

        // Test with valid classes
        Class<String> stringClass = String.class;
        assertEquals("Test string", (String) allocator.newInstance(stringClass));

        Class<Integer> integerClass = Integer.class;
        assertEquals(42, (Integer) allocator.newInstance(integerClass));

        Class<Serializable> serializableClass = Serializable.class;
        assertNotNull(allocator.newInstance(serializableClass));

        // Test with abstract classes
        Class<AbstractClass> abstractClass = AbstractClass.class;
        try {
            allocator.newInstance(abstractClass);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        // Test with interface classes
        Class<InterfaceClass> interfaceClass = InterfaceClass.class;
        try {
            allocator.newInstance(interfaceClass);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        // Test with final classes
        Class<FinalClass> finalClass = FinalClass.class;
        try {
            allocator.newInstance(finalClass);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public void testNewInstanceWithPrivateConstructor() {
        UnsafeAllocator allocator = UnsafeAllocator.create();

        // Test with a class having a private constructor
        Class<PrivateConstructorClass> privateConstructorClass = PrivateConstructorClass.class;
        try {
            allocator.newInstance(privateConstructorClass);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    // Add more tests if necessary for other methods or edge cases

    private static <T> T assertNotNull(T object) {
        if (object == null) {
            throw new AssertionError("Expected object to not be null");
        }
        return object;
    }

    private static void assertEquals(String expected, Object actual) {
        assertEquals(expected, actual.toString());
    }

    private static void assertEquals(int expected, Object actual) {
        assertEquals(expected, ((Number) actual).intValue());
    }

    private static void assertEquals(Object expected, Object actual) {
        assertEquals(expected.getClass(), actual.getClass());
        if (!expected.equals(actual)) {
            throw new AssertionError("Expected: <" + expected + ">, but was: <" + actual + ">");
        }
    }

    private static void fail(String message) {
        throw new AssertionError(message);
    }

    // Abstract class for testing purposes
    static abstract class AbstractClass {}

    // Interface class for testing purposes
    static interface InterfaceClass {}

    // Final class for testing purposes
    static final class FinalClass {}

    // Class with a private constructor for testing purposes
    static class PrivateConstructorClass {
        private PrivateConstructorClass() {}
    }
}