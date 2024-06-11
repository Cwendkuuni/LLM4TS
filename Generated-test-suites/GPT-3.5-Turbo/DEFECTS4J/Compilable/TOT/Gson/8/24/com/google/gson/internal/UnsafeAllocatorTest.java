package com.google.gson.internal;

import static org.junit.Assert.*;
import org.junit.Test;

public class UnsafeAllocatorTest {

    @Test
    public void testNewInstance_JvmAllocation() {
        try {
            UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
            Object obj = unsafeAllocator.newInstance(TestClass.class);
            assertNotNull(obj);
            assertTrue(obj instanceof TestClass);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testNewInstance_DalvikVmPostGingerBreadAllocation() {
        try {
            UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
            Object obj = unsafeAllocator.newInstance(TestClass.class);
            assertNotNull(obj);
            assertTrue(obj instanceof TestClass);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNewInstance_DalvikVmPreGingerBreadAllocation() throws Exception {
        UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
        unsafeAllocator.newInstance(TestClass.class);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNewInstance_GiveUpAllocation() throws Exception {
        UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
        unsafeAllocator.newInstance(TestClass.class);
    }

    private static class TestClass {
        // Empty Test Class
    }
}

