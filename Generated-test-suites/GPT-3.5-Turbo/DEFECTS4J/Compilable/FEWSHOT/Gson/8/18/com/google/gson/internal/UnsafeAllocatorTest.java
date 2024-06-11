package com.google.gson.internal;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

public class UnsafeAllocatorTest {
    @Test
    public void testCreate() {
        try {
            UnsafeAllocator allocator = UnsafeAllocator.create();
            assertTrue(allocator != null);
        } catch (Exception e) {
            fail("Exception caught");
        }
    }

    @Test
    public void testNewInstance() {
        try {
            UnsafeAllocator allocator = UnsafeAllocator.create();
            Class<TestClass> testClass = TestClass.class;
            TestClass obj = allocator.newInstance(testClass);
            assertTrue(obj != null);
        } catch (Exception e) {
            fail("Exception caught");
        }
    }

    @Test
    public void testUnsupportedOperationException() {
        try {
            UnsafeAllocator allocator = UnsafeAllocator.create();
            Class<UnsupportedClass> unsupportedClass = UnsupportedClass.class;
            allocator.newInstance(unsupportedClass);
            fail("Exception not caught");
        } catch (UnsupportedOperationException e) {
            assertTrue(e.getMessage().equals("Cannot allocate class"));
        } catch (Exception e) {
            fail("Unexpected exception caught");
        }
    }

    private static class TestClass {
        public TestClass() {

        }
    }

    private interface UnsupportedClass {

    }
}