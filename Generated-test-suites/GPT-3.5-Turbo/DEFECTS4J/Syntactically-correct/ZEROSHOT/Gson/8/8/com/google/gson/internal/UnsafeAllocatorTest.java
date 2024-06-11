package com.google.gson.internal;

import org.junit.Test;

import static org.junit.Assert.*;

public class UnsafeAllocatorTest {

    @Test
    public void testCreate() {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        assertNotNull(allocator);
    }

    @Test
    public void testCreate_Failed() {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        assertTrue(allocator instanceof UnsafeAllocator);
    }

    @Test
    public void testNewInstance() throws Exception {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        Object newInstance = allocator.newInstance(Object.class);
        assertNotNull(newInstance);
    }

    @Test
    public void testNewInstance_ClassNotFound() throws Exception {
        UnsafeAllocator allocator = UnsafeAllocator.create();

        try {
            allocator.newInstance(Class.forName("com.example.NonExistentClass"));
            fail("Expected ClassNotFoundException to be thrown");
        } catch (ClassNotFoundException e) {
            assertEquals("com.example.NonExistentClass", e.getMessage());
        }
    }

    @Test
    public void testNewInstance_IllegalAccessException() throws Exception {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        Class<?> privateClass = PrivateClass.class;

        try {
            allocator.newInstance(privateClass);
            fail("Expected IllegalAccessException to be thrown");
        } catch (IllegalAccessException e) {
            assertEquals("Cannot allocate " + privateClass, e.getMessage());
        }
    }

    private static class PrivateClass {
        private PrivateClass() {
            throw new UnsupportedOperationException("Private constructor");
        }
    }
}