package com.google.gson.internal;

import org.junit.Test;
import static org.junit.Assert.*;

public class UnsafeAllocatorTest {

    @Test
    public void testNewInstanceWithNullClass() {
        UnsafeAllocator allocator = UnsafeAllocator.create();

        try {
            allocator.newInstance(null);
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Test passed
        } catch (Exception e) {
            fail("Expected an IllegalArgumentException to be thrown, but got " + e.getClass().getSimpleName());
        }
    }

    @Test
    public void testNewInstanceWithInterfaceClass() {
        UnsafeAllocator allocator = UnsafeAllocator.create();

        try {
            allocator.newInstance(TestInterface.class);
            fail("Expected an UnsupportedOperationException to be thrown");
        } catch (UnsupportedOperationException e) {
            // Test passed
        } catch (Exception e) {
            fail("Expected an UnsupportedOperationException to be thrown, but got " + e.getClass().getSimpleName());
        }
    }

    @Test
    public void testNewInstanceWithExceptionThrowingClass() {
        UnsafeAllocator allocator = UnsafeAllocator.create();

        try {
            allocator.newInstance(ExceptionThrowingClass.class);
            fail("Expected an Exception to be thrown");
        } catch (Exception e) {
            // Test passed
        }
    }
    
    public interface TestInterface {
        // empty interface
    }
    
    public abstract static class ExceptionThrowingClass {
        public ExceptionThrowingClass() throws Exception {
            throw new Exception("Exception during instantiation");
        }
    }
}
