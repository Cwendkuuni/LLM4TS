package com.google.gson.internal;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class UnsafeAllocatorTest {

    @Test
    public void testNewInstance() {
        UnsafeAllocator allocator = UnsafeAllocator.create();

        assertInstanceOf(Object.class.getClass(), allocator.newInstance(Object.class));
        assertNotNull(allocator.newInstance(String.class));

        // Test for interface and abstract classes
        try {
            allocator.newInstance(ObjectStreamConstants.class);
            assert false : "Should not be able to instantiate interface or abstract classes.";
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            allocator.newInstance(AbstractClass.class);
            assert false : "Should not be able to instantiate abstract classes.";
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    private static class AbstractClass {
        // Abstract class
    }

    private static class ObjectStreamConstants implements Serializable {
        // Interface
    }
}