package com.google.gson.internal;

import static org.junit.Assert.*;
import org.junit.Test;

public class UnsafeAllocatorTest {

    @Test
    public void testNewInstance() throws Exception {
        UnsafeAllocator allocator = UnsafeAllocator.create();

        // Test for class that can be instantiated
        assertNotNull(allocator.newInstance(String.class));

        // Test for class with interface or abstract modifiers
        try {
            allocator.newInstance(AbstractClass.class);
            fail("Expected UnsupportedOperationException to be thrown");
        } catch (UnsupportedOperationException e) {
            // expected
        }

        try {
            allocator.newInstance(InterfaceClass.class);
            fail("Expected UnsupportedOperationException to be thrown");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    private static abstract class AbstractClass {
    }

    private static interface InterfaceClass {
    }
}