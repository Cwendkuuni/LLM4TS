package com.google.gson.internal;

import static org.junit.Assert.*;

import org.junit.Test;

public class UnsafeAllocatorTest {
    @Test
    public void testNewInstanceForObject() {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        Object instance = allocator.newInstance(Object.class);
        assertNotNull(instance);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNewInstanceForInterface() {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        assertThrows(UnsupportedOperationException.class, () -> allocator.newInstance(Comparable.class));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNewInstanceForAbstractClass() {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        assertThrows(UnsupportedOperationException.class, () -> allocator.newInstance(AbstractClass.class));
    }

    @Test
    public void testNewInstanceForConcreteClass() {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        ConcreteClass instance = allocator.newInstance(ConcreteClass.class);
        assertNotNull(instance);
    }

    private static abstract class AbstractClass {
        // abstract class
    }

    private static class ConcreteClass {
        // concrete class
    }
}