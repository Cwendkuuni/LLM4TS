package com.google.gson.internal;

import com.google.gson.internal.UnsafeAllocator;

import org.junit.Test;

import static org.junit.Assert.*;

public class UnsafeAllocatorTest {

    private UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();

    @Test
    public void testNewInstance() throws Exception {
        assertNotNull(unsafeAllocator.newInstance(Object.class));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNewInstanceUnsupported() throws Exception {
        unsafeAllocator.newInstance(String.class);
    }

    // Additional test cases for edge cases and exception handling

    @Test
    public void testNewInstanceWithAbstractClass() {
        try {
            unsafeAllocator.newInstance(AbstractClass.class);
            fail("Expected exception UnsupportedOperationException was not thrown");
        } catch (Exception e) {
            assertTrue(e instanceof UnsupportedOperationException);
        }
    }

    @Test
    public void testNewInstanceWithInterface() {
        try {
            unsafeAllocator.newInstance(Interface.class);
            fail("Expected exception UnsupportedOperationException was not thrown");
        } catch (Exception e) {
            assertTrue(e instanceof UnsupportedOperationException);
        }
    }

    @Test
    public void testNewInstanceWithPrimitiveType() {
        try {
            unsafeAllocator.newInstance(int.class);
            fail("Expected exception UnsupportedOperationException was not thrown");
        } catch (Exception e) {
            assertTrue(e instanceof UnsupportedOperationException);
        }
    }

    // Helper classes for testing

    private static abstract class AbstractClass {
    }

    private interface Interface {
    }
}
