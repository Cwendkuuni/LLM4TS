package com.google.gson.internal;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class UnsafeAllocatorTest {

    @Test
    public void testCanAllocateSimpleClass() {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        Integer i = allocator.newInstance(Integer.class);
        assertNotNull(i);
    }

    @Test
    public void testCannotAllocateInterface() {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        assertThrows(UnsupportedOperationException.class, () -> {
            allocator.newInstance(Object.class);
        });
    }

    @Test
    public void testCannotAllocateAbstractClass() {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        assertThrows(UnsupportedOperationException.class, () -> {
            allocator.newInstance(AbstractClass.class);
        });
    }

    @Test
    public void testCannotAllocatePrivateClass() {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        assertThrows(IllegalAccessError.class, () -> {
            allocator.newInstance(PrivateClass.class);
        });
    }

    @Test
    public void testCannotAllocateProtectedClass() {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        assertThrows(IllegalAccessError.class, () -> {
            allocator.newInstance(ProtectedClass.class);
        });
    }

    @Test
    public void testCannotAllocateFinalClass() {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        assertThrows(UnsupportedOperationException.class, () -> {
            allocator.newInstance(FinalClass.class);
        });
    }

    @Test
    public void testCannotAllocateArray() {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        assertThrows(UnsupportedOperationException.class, () -> {
            allocator.newInstance(int[].class);
        });
    }

    @Test
    public void testCannotAllocateGenericClass() {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        assertThrows(UnsupportedOperationException.class, () -> {
            allocator.newInstance(GenericClass.class);
        });
    }

    @Test
    public void testCannotAllocateNullClass() {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        assertThrows(NullPointerException.class, () -> {
            allocator.newInstance(null);
        });
    }

    private static abstract class AbstractClass {}
    private static final class FinalClass {}
    private static class PrivateClass {
        private PrivateClass() {}
    }
    private static class ProtectedClass {
        protected ProtectedClass() {}
    }
    private static class GenericClass<T> {}
}