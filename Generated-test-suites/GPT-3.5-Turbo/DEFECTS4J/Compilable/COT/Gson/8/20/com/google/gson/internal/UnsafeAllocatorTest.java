package com.google.gson.internal;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnsafeAllocatorTest {

    @Test
    public void testNewInstance() throws Exception {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        assertNotNull(allocator);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNewInstance_UnsupportedClass() throws Exception {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        allocator.newInstance(Object.class);
    }

    @Test
    public void testNewInstance_ValidClass() throws Exception {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        String instance = allocator.newInstance(String.class);
        assertNotNull(instance);
    }

    @Test
    public void testNewInstance_ClassNotFound() {
        try {
            Class<?> missingClass = Class.forName("com.google.gson.MissingClass");
            // This should throw ClassNotFoundException
            UnsafeAllocator allocator = UnsafeAllocator.create();
            allocator.newInstance(missingClass);
            fail("Expected ClassNotFoundException to be thrown");
        } catch (ClassNotFoundException e) {
            // Do nothing since expected
        } catch (Exception e) {
            fail("Unexpected exception thrown: " + e);
        }
    }
}