package com.google.gson.internal;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class UnsafeAllocatorTest {

    @Test
    public void testNewInstanceForObject() {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        Object obj = null;
        try {
            obj = allocator.newInstance(Object.class);
            assertNotNull(obj);
        } catch (Exception e) {
            e.printStackTrace();
            assert false : "Unexpected exception";
        }
    }

    @Test
    public void testNewInstanceForInterface() {
        assertThrows(UnsupportedOperationException.class, () -> {
            UnsafeAllocator allocator = UnsafeAllocator.create();
            allocator.newInstance(Object.class.getInterfaces()[0]);
        });
    }

    @Test
    public void testNewInstanceForAbstractClass() {
        assertThrows(UnsupportedOperationException.class, () -> {
            UnsafeAllocator allocator = UnsafeAllocator.create();
            allocator.newInstance(AbstractClass.class);
        });
    }

    @Test
    public void testNewInstanceForClassWithPrivateConstructor() {
        class PrivateConstructorClass {
            private PrivateConstructorClass() {
            }
        }

        UnsafeAllocator allocator = UnsafeAllocator.create();
        assertThrows(UnsupportedOperationException.class, () -> {
            allocator.newInstance(PrivateConstructorClass.class);
        });
    }

    static class AbstractClass {
        abstract void doSomething();
    }
}