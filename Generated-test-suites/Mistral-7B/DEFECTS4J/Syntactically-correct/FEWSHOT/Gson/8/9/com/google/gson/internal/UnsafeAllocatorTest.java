package com.google.gson.internal;

import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class UnsafeAllocatorTest {

    @Test
    public void testNewInstanceForObject() {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        assertThrows(UnsupportedOperationException.class, () -> allocator.newInstance(Object.class));
    }

    @Test
    public void testNewInstanceForInterface() {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        assertThrows(UnsupportedOperationException.class, () -> allocator.newInstance(Comparable.class));
    }

    @Test
    public void testNewInstanceForAbstractClass() {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        assertThrows(UnsupportedOperationException.class, () -> allocator.newInstance(AbstractClass.class));
    }

    @Test
    public void testNewInstanceForValidClass() {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        try {
            String result = (String) allocator.newInstance(String.class);
            assertNull(result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static class AbstractClass {
    }
}